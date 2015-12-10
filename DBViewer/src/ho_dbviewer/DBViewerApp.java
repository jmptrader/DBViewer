package ho_dbviewer;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DBViewerApp extends Application {
	DBVLogic dbLogic;
	VBox dbInterface ;
	ListView<String> srcList = new ListView<>();
	ObservableList<String> srcItems;
	ArrayList<String> srcItemsList = new ArrayList<String>(Arrays.asList("custid", "name", "address", "city" , "state" , "zip"));
	ListView<String> dstList = new ListView<>();
	ObservableList<String> dstItems;
	ArrayList<String> dstItemsList = new ArrayList<>();
	ComboBox<String> cmbColumnSearch ;
	ComboBox<String> cmbConditions ;
	TextField txtfSearchCriteria ;
	TextArea dbTableView ;
	String dbQueryString ;
	
	@Override
	public void start(Stage primaryStage) throws ClassNotFoundException {
	
		// Open and Initialize the DB connection
		dbLogic = new DBVLogic();
		
		// Initialize outside container VBox and BorderPane to hold ListViews
		dbInterface = new VBox();
		dbInterface.setAlignment(Pos.CENTER);
		BorderPane bPane = new BorderPane();
		bPane.setPrefSize(300, 330);
		
		// Link Array List to the Observable Array list objects
		srcItems = FXCollections.observableArrayList(srcItemsList);
		dstItems = FXCollections.observableArrayList(dstItemsList);
		
		// Populates the src list object
		srcList.setItems(srcItems);
		srcList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		dstList.setItems(dstItems);
		dstList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		//Set size parameters on the ListView Obejcts
		srcList.setMaxWidth(130);
		srcList.setMaxHeight(330);
		dstList.setMaxWidth(130);
		dstList.setMaxHeight(330);	
		
		// Configure a VBox to hold the button in center or BorderPane
		VBox verticalBox = new VBox(5);		
		HBox centerButtons = new HBox(10); // Holds the two buttons side by side
		centerButtons.setMaxWidth(60);
		centerButtons.setAlignment(Pos.CENTER);
		
		// Button to move ListView item to opposite list view. Calls method via Lamba functions call.
		Button btnLeft = new Button("<");
		btnLeft.setOnMouseClicked(e -> {
			this.moveItemLeftList();			
		});
		
		// Button to move ListView item to opposite list view. Calls method via Lamba functions call.		
		Button btnRight = new Button(">");
		btnRight.setOnMouseClicked(e -> {
			this.moveItemRightList();
		});

		// Bind buttons to HBox and then add it to the VBox
		centerButtons.getChildren().addAll(btnLeft, btnRight);
		verticalBox.setAlignment(Pos.CENTER);
		verticalBox.getChildren().addAll(centerButtons);

		// Setup Borderpane to locate Listviews and the VBox 
		BorderPane.setAlignment(srcList, Pos.CENTER_LEFT);
		BorderPane.setAlignment(centerButtons, Pos.CENTER);
		BorderPane.setAlignment(dstList, Pos.CENTER_RIGHT);
		BorderPane.setMargin(srcList, new Insets(0,10,0,10));
		BorderPane.setMargin(centerButtons, new Insets(15,15,15,15));
		BorderPane.setMargin(dstList, new Insets(0,10,0,10));
		bPane.setLeft(srcList);
		bPane.setCenter(verticalBox);
		bPane.setRight(dstList);

		// This VBox contins a HBox with the search critera comboboxs and search textfield.
		VBox bottomArea = new VBox();

		// Set seperators to define query critiera area.
		Separator searchTopSeperator = new Separator();
		Separator searchBottomSeperator = new Separator();
		HBox searchCriteraArea = new HBox(5);
		
		// Comboboxes put together the search criteria
		cmbColumnSearch = new ComboBox<String>();
		cmbConditions = new ComboBox<String>();
		cmbConditions.getItems().addAll("=","!=","<","<=",">",">=");
		txtfSearchCriteria = new TextField();

		searchCriteraArea.setAlignment(Pos.CENTER_LEFT);
		searchCriteraArea.getChildren().addAll(cmbColumnSearch,cmbConditions,txtfSearchCriteria);
		
		// This Button searches the DB and then resturns the results via the displayData method.
		Button btnSearch = new Button("Search");
		btnSearch.setAlignment(Pos.CENTER_RIGHT);
		btnSearch.setOnMouseClicked(e -> {
			this.displayData();
		});

		VBox.setMargin(searchTopSeperator, new Insets(0,10,5,10));
		VBox.setMargin(searchCriteraArea, new Insets(0,10,5,10));		
		VBox.setMargin(btnSearch, new Insets(0,10,5,10));
		VBox.setMargin(searchBottomSeperator, new Insets(0,10,5,10));

		bottomArea.getChildren().addAll(searchTopSeperator, searchCriteraArea, btnSearch, searchBottomSeperator);

		// Sets up the DataView  object for presentation of the data results.
		dbTableView = new TextArea();
		dbTableView.setMinHeight(100);

		// Vbox sets up the general interface layout and then binds the other objects to the scene
		VBox.setMargin(bPane, new Insets(10,0,10,0));
		VBox.setMargin(bottomArea, new Insets(10,0,10,0));
		VBox.setMargin(dbTableView, new Insets(10,10,10,10));
		dbInterface.getChildren().addAll(bPane, bottomArea, dbTableView);
		
		// General Windows information
		Scene myScene = new Scene(dbInterface, 400, 600);
		primaryStage.setScene(myScene);
		primaryStage.setTitle("Customer DB View");
		primaryStage.show();
	}
	
	// Method moves listView Items from the left side to the right side
	public void moveItemRightList(){
		String switchItem = srcItems.get(srcList.getFocusModel().getFocusedIndex());
		dstItems.add(switchItem);
		dstList.getItems();
		srcItems.remove(srcList.getFocusModel().getFocusedIndex());
		cmbColumnSearch.getItems().clear();
		cmbColumnSearch.getItems().addAll(dstItems);
	}
	
	// Method moves listView Items from the right side to the left side
	public void moveItemLeftList(){
		String switchItem = dstItems.get(dstList.getFocusModel().getFocusedIndex());
		srcItems.add(switchItem);
		srcList.getItems();
		dstItems.remove(srcList.getFocusModel().getFocusedIndex());
		cmbColumnSearch.getItems().clear();
		cmbColumnSearch.getItems().addAll(dstItems);

	}
	
	// Method to initiate DB query and then return the results back to the DataTable view
	public void displayData(){
		String conditionColumn = cmbColumnSearch.getValue();      // Get Search field for WHERE
		String operator = cmbConditions.getValue();				  // Gets the logical operator	
		String condition = txtfSearchCriteria.getText();		  // the search critiera
/*		ObservableList<ArrayList> rowData = FXCollections.observableArrayList();
		ArrayList<TableColumn> tcData = new ArrayList<>();
*/		@SuppressWarnings("rawtypes")
		ArrayList<ArrayList> columnResults = new ArrayList<>();
		StringBuffer resultsOutput = new StringBuffer();
		
		System.out.println(dbLogic.makeDBQuery(dstItems, conditionColumn, operator, condition));

		// Initiates a DB query
		try {
			dbLogic.queryDB(dbLogic.makeDBQuery(dstItems, conditionColumn, operator, condition));
		} catch (Exception e1) {
			System.out.println("Query Failed");
			e1.printStackTrace();
		}

		
		columnResults = dbLogic.getResults() ;
		System.out.println("DstItems " + dstItems.size());
		
		System.out.println("Columne Size : " + columnResults.size());

		// Pulls the Column Names and populates Text Field
		for(int loop = 0; loop < dstItems.size(); loop++){
			resultsOutput.append(dstItems.get(loop));
			resultsOutput.append("\t");
		}
		resultsOutput.append("\n");

		for(int loop = 0; loop < dstItems.size(); loop++){
			resultsOutput.append("====");
			resultsOutput.append("\t");
		}
		resultsOutput.append("\n");

		
		for(int loop = 0; loop  < columnResults.size(); loop++){
			for(int innerloop = 0; innerloop < dstItems.size(); innerloop++ ){
				resultsOutput.append(columnResults.get(loop).get(innerloop));
				resultsOutput.append("\t");						
			}
			resultsOutput.append("\n");			
		}
		
		dbTableView.setText(resultsOutput.toString());


		// Closes the DB
		dbLogic.closeDB();
	}
	
	// Exectues main application
	public static void main(String[] args) {
		Application.launch(args);
	}

}
