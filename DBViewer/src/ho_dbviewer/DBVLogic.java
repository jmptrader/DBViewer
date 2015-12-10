package ho_dbviewer;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.ObservableList;

class DBVLogic {
	DBVjdbc db;											
	ArrayList<String> tableList = new ArrayList<>();
	
	// Empty Constructor
	DBVLogic(){
	}
	
	// Method initiates a query with the string parameter outlining the SQl query
	public void queryDB(String queryString) throws ClassNotFoundException{
		db = new DBVjdbc();				// Initiates a DBShim object
		db.submitQuery(queryString);	// Makes the query
		
	}
	
	// Method to return the results from the query. This is workhorse method of this class
	@SuppressWarnings("rawtypes")
	public ArrayList<ArrayList>  getResults() {
		ArrayList<ArrayList> results = new ArrayList<>();

		try {
			// This moves result cursor to first row and keeps cycling until no rows are left
			ResultSetMetaData rsmd = db.dbQueryResults.getMetaData();
		    int numberOfColumns = rsmd.getColumnCount();
			while(db.dbQueryResults.next()){
				ArrayList<String> dbRow = new ArrayList<>();  // used to hold each row of data in a arraylist
				for(int loop =1; loop <= numberOfColumns;loop++){
					dbRow.add(db.dbQueryResults.getString(loop)); 	
				}
				results.add(dbRow); 						  // Adds the temp ArrayList into the return arrayList
			}
		} catch (SQLException e) {
			System.out.println("Unable to reqd query Results object");
			e.printStackTrace();
		}

		return results;
	}

	// Method to construct a DB query from all of the parameters. Call multiple helper methods
	public String makeDBQuery(ObservableList<String> columnList, String conditionColumn, String operator, String condition){
		String selectStatement = "SELECT ";
		String selectColumnList = this.processDBColumnList(columnList);
		String fromStatement = " FROM customers ";
		String whereStatement = "WHERE ";
		String whereCondition = processConditionColumn(conditionColumn);
		String whereOperator  = processWhereOperator(operator);
		String where = whereStatement + whereCondition + whereOperator + "'" + condition +"'" ;
		return selectStatement + selectColumnList + fromStatement + where;
	}

	// Method breaks apart the fields names and sets them up as string. Returns formeted string
	private String processDBColumnList(ObservableList<String> columnList){
		String selectColumnItems = " ";
		
		if(!columnList.isEmpty()){
			for(int loop = 0; loop < columnList.size(); loop++){
				if((columnList.size() - 1 ) > loop){
					selectColumnItems += columnList.get(loop);
					selectColumnItems += ", ";
				} else {
					selectColumnItems += columnList.get(loop);					
				}
			}
		}
		return selectColumnItems;
	}
	
	// Method takes the Datafield for WHERE field condition field
	private String processConditionColumn(String conditionColumn){
		return conditionColumn;
	}
	
	// method to identify and set the WHERE condition logical operator
	private String processWhereOperator(String operator){
		StringBuilder whereOperator = new StringBuilder();
		if(operator.equals("=")){
			whereOperator.append(" = ");
		} else if(operator.equals("!=")){
			whereOperator.append(" != ");			
		} else if(operator.equals("<")){
			whereOperator.append(" < ");			
		} else if(operator.equals("<=")){
			whereOperator.append(" <= ");			
		} else if(operator.equals(">")){
			whereOperator.append(" > ");			
		} else if(operator.equals(">=")){
			whereOperator.append(" >= ");			
		}
		return whereOperator.toString();
	}

	// Method to close the Database.
	public void closeDB(){
		db.closeDB();
	}
}


