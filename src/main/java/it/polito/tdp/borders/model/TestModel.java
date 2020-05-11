package it.polito.tdp.borders.model;

public class TestModel {

	public static void main(String[] args) {

		Model model = new Model();

		System.out.println("TestModel -- TODO");
		
//		System.out.println("Creo il grafo relativo al 2000");
//		model.createGraph(2000);
		
//		List<Country> countries = model.getCountries();
//		System.out.format("Trovate %d nazioni\n", countries.size());

//		System.out.format("Numero componenti connesse: %d\n", model.getNumberOfConnectedComponents());
		
//		Map<Country, Integer> stats = model.getCountryCounts();
//		for (Country country : stats.keySet())
//			System.out.format("%s %d\n", country, stats.get(country));		
		
		model.setGraph(1816);
		System.out.println(model.iterativeVisit(model.getcIdMap().get(210)));
		System.out.println(model.breadthVisit(model.getcIdMap().get(210)));
		System.out.println("\n");
		System.out.println(model.recursiveVisit(model.getcIdMap().get(210)));
		System.out.println(model.depthVisit(model.getcIdMap().get(210)));
		
		System.out.println("\n");
		model.setGraph(2016);
		System.out.println(model.getConnectedComponents());
		
	}

}
