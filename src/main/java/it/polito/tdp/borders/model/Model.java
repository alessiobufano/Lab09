package it.polito.tdp.borders.model;

import java.util.*;
import org.jgrapht.*;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.event.*;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.*;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {

	private BordersDAO dao;
	private Map<Integer, Country> cIdMap;
	private Graph<Country, DefaultEdge> graph;
	
	public Model() {
		this.dao = new BordersDAO();
		cIdMap = new HashMap<>();
		dao.loadAllCountries(cIdMap);	
	}

	public Map<Integer, Country> getcIdMap() {
		return cIdMap;
	}

	public void setGraph(int year) {
		
		graph = new SimpleGraph<>(DefaultEdge.class);
		
		Graphs.addAllVertices(graph, dao.loadAllVertexes(cIdMap, year));
		
		for(Border b : dao.getCountryPairs(year, cIdMap))
			graph.addEdge(b.getState1(), b.getState2());
	}
	
	public Graph<Country, DefaultEdge> getGraph() {
		return graph;
	}

	public int getVertexesNumber() {
		return this.graph.vertexSet().size();
	}
	
	public int getEdgesNumber() {
		return this.graph.edgeSet().size();
	}
	
	public String getStatesList(int year) {
		String list="The States list in the year "+year+" was composed by: \n";
		for(Country v : this.graph.vertexSet())
			list += ""+v+" with "+graph.degreeOf(v)+" neighbouring States\n";
		return list;
	}
	
	public List<Set<Country>> getConnectedComponents(){
		ConnectivityInspector<Country, DefaultEdge> connectedGraph = new ConnectivityInspector<>(graph);
		List<Set<Country>> list = connectedGraph.connectedSets();
		return list;
	}
	
	//4 different methods of graph visit:
	
	public List<Country> breadthVisit(Country source) {
		List<Country> visit = new LinkedList<>();
		GraphIterator<Country, DefaultEdge> bfv = new BreadthFirstIterator<>(graph, source);
		while(bfv.hasNext()) {
			visit.add( bfv.next() );
		}
		return visit;
	}
	
	public List<Country> depthVisit(Country source) {
		List<Country> visit = new LinkedList<>();
		GraphIterator<Country, DefaultEdge> dfv = new DepthFirstIterator<>(graph, source);
		while(dfv.hasNext()) {
			visit.add( dfv.next() );
		}
		return visit;
	}
	
	public List<Country> recursiveVisit(Country source) {
		List<Country> toVisit = new LinkedList<>(this.graph.vertexSet());
		List<Country> visited = new LinkedList<>();
		
		this.recursiveFunction(source, toVisit, visited);
		
		return visited;
	}
	
	private void recursiveFunction(Country source, List<Country> toVisit, List<Country> visited) {
		
		if(!visited.contains(source))
			visited.add(source);
		toVisit.remove(source);
		
		if(toVisit.size()==0)
			return;
		
		Set<Country> toVisitNext = new HashSet<>();
		
		Set<DefaultEdge> edgesToVisitOut = this.graph.outgoingEdgesOf(source);
		Set<DefaultEdge> edgesToVisitIn = this.graph.incomingEdgesOf(source);
		for(DefaultEdge e : edgesToVisitOut)
			if(toVisit.contains(graph.getEdgeTarget(e)))
				toVisitNext.add(graph.getEdgeTarget(e));
		for(DefaultEdge e : edgesToVisitIn)
			if(toVisit.contains(graph.getEdgeSource(e)))
				toVisitNext.add(graph.getEdgeSource(e));	
		
		if(toVisitNext.size()==0)
			return;
		
		for(Country c : toVisitNext)
			this.recursiveFunction(c, toVisit, visited);
	}
	
	public List<Country> iterativeVisit(Country source) {
		List<Country> toVisit = new LinkedList<>(this.graph.vertexSet());
		List<Country> visited = new LinkedList<>();
		Set<Country> toVisitNow = new HashSet<>();
		
		toVisitNow.add(source);
		this.iterativeFunction(toVisitNow, toVisit, visited);
		
		return visited;
	}
	
	private void iterativeFunction(Set<Country> toVisitNow, List<Country> toVisit, List<Country> visited) {
		
		visited.addAll(toVisitNow);
		toVisit.removeAll(toVisitNow);
		
		if(toVisit.size()==0)
			return;
		
		Set<Country> toVisitNext = new HashSet<>();
		for(Country source : toVisitNow)
		{
			Set<DefaultEdge> edgesToVisitOut = this.graph.outgoingEdgesOf(source);
			Set<DefaultEdge> edgesToVisitIn = this.graph.incomingEdgesOf(source);
			for(DefaultEdge e : edgesToVisitOut)
				if(toVisit.contains(graph.getEdgeTarget(e)))
					toVisitNext.add(graph.getEdgeTarget(e));
			for(DefaultEdge e : edgesToVisitIn)
				if(toVisit.contains(graph.getEdgeSource(e)))
					toVisitNext.add(graph.getEdgeSource(e));
		}
		
		if(toVisitNext.size()==0)
			return;
		
		this.iterativeFunction(toVisitNext, toVisit, visited);
		
	}

}
