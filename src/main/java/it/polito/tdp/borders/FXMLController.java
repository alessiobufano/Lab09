package it.polito.tdp.borders;

import java.net.URL;
import java.util.*;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;
	private ObservableList<Country> countriesList = FXCollections.observableArrayList();
	private ObservableList<String> visitsList = FXCollections.observableArrayList();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtYear;

    @FXML
    private ComboBox<Country> cmbStates;

    @FXML
    private ChoiceBox<String> cmbVisits;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCalculateBorders(ActionEvent event) {
    	
    	this.txtResult.clear();
    	countriesList.clear();
    	this.cmbStates.setItems(countriesList);
    	this.cmbVisits.setValue("SearchType");
    	String text = this.txtYear.getText();
    	Integer year = null;
    	
    	try {
    		year = Integer.parseInt(text);
    	} catch(NumberFormatException e ) {
    		this.txtResult.appendText("Error! Insert a numerical value for the year!!\n");
    		return;
    	}
    	
    	if(year<1816 || year>2016) {
    		this.txtResult.appendText("Error! Insert a value between 1816 and 2016 for the year!!\n");
    		return;
    	}
    	
    	this.model.setGraph(year);
    	this.countriesList.addAll(model.getGraph().vertexSet());
    	this.cmbStates.setItems(countriesList);
    	
    	this.txtResult.appendText("Graph created with "+model.getVertexesNumber()+" vertexes and "+model.getEdgesNumber()+" edges\n");
    	this.txtResult.appendText(model.getStatesList(year));
    	this.txtResult.appendText("In the graph there are "+model.getConnectedComponents().size()+" connected components:\n");
    	for(Set<Country> s : model.getConnectedComponents())
    		this.txtResult.appendText(s+";\n");
    }

    @FXML
    void doFindCloseStates(ActionEvent event) {
    	
    	this.txtResult.clear();
    	Country source = this.cmbStates.getValue();
    	String searchType = this.cmbVisits.getValue();
    	List<Country> result = new LinkedList<>();
    	
    	if(source==null) {
    		this.txtResult.appendText("Please, select a State!");
    		return;
    	}
    	
    	long start = System.nanoTime();
    	
    	if(searchType.equals("SearchType") || searchType==null) {
    		this.txtResult.appendText("Please, select a type of search!");
    		return;
    	}
    	else if(searchType.equals("Breadth"))
    		result.addAll(model.breadthVisit(source));
    	else if (searchType.equals("Depth"))
    		result.addAll(model.depthVisit(source));
    	else if (searchType.equals("Recursive"))
    		result.addAll(model.recursiveVisit(source));
    	else if (searchType.equals("Iterative"))
    		result.addAll(model.iterativeVisit(source));
    	
    	long end = System.nanoTime();
    	double time = (end - start)/1000;
    	
    	this.txtResult.appendText("The "+searchType+" visit of the graph from "+source+" produced a list of "+result.size()+" States over "+model.getVertexesNumber()+" States:\n");
		for(Country c : result)
			this.txtResult.appendText(c+"\n");
		
		this.txtResult.appendText("The search takes "+time+" microseconds");
		  	
    }

    @FXML
    void initialize() {
        assert txtYear != null : "fx:id=\"txtYear\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbStates != null : "fx:id=\"cmbStates\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbVisits != null : "fx:id=\"cmbVisits\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }

    public void setModel(Model model) {
    	this.model = model;
    	this.visitsList.addAll("SearchType","Breadth", "Depth", "Recursive", "Iterative");
    	this.cmbVisits.setItems(visitsList);
    	this.cmbVisits.setValue("SearchType");
    }
}
