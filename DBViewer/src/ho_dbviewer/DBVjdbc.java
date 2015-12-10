package ho_dbviewer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class DBVjdbc {
	String sqlDriver = "com.mysql.jdbc.Driver";
	String dbConnInfo = "jdbc:mysql://127.0.0.1:8889/pcparts";
	String dbAccoutnName = "root";
	String dbAccountPwd  = "";
		
	Connection dbConnection;
	Statement  dbStatement;
	ResultSet  dbQueryResults;

	// Class Constructor, initializes the driver and opens a connection.
	DBVjdbc() throws ClassNotFoundException{
		this.loadDriver();  // loads the driver at initialization  
		this.connectDB();	// opens the DB connection	
	}
		
	// Internal Method to connect to the driver
	private void loadDriver() {
		try {
			Class.forName(sqlDriver);  // Connects to Mysql driver
			System.out.println("Driver Loaded");
		} catch (ClassNotFoundException e) {
			System.out.println("Unable to load Driver");
			e.printStackTrace();
		}	
	}
		
	// Internal Method to connect to the Database
	private void connectDB(){
		try {
			dbConnection = DriverManager.getConnection(dbConnInfo, dbAccoutnName, dbAccountPwd);
			System.out.println("Connection made");
		} catch (SQLException e) {
			System.out.println("Unable to Connect to DB" + dbConnInfo);
			e.printStackTrace();
		}
	}
		
	// Public connection to submit SQL query and the return the results.
	// Takes a String parameter with a fully formed SQl query statement.
	// results are stored in global class.
	public void submitQuery(String queryStatement){
		try {
			dbStatement = dbConnection.createStatement();
			dbQueryResults = dbStatement.executeQuery(queryStatement);
		} catch (SQLException e) {
			System.out.println("Error submitting query, check DB conenction or query string");
			e.printStackTrace();
		}	
	}
		
	// Public Method to close the DB connection
	public void closeDB(){
		try {
			dbConnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

		/**
	 * @return the sqlDriver
	 */
	public String getSqlDriver() {
		return sqlDriver;
	}

	/**
	 * @param sqlDriver the sqlDriver to set
	 */
	public void setSqlDriver(String sqlDriver) {
		this.sqlDriver = sqlDriver;
	}

	/**
	 * @return the dbConnInfo
	 */
	public String getDbConnInfo() {
		return dbConnInfo;
	}

	/**
	 * @param dbConnInfo the dbConnInfo to set
	 */
	public void setDbConnInfo(String dbConnInfo) {
		this.dbConnInfo = dbConnInfo;
	}

	/**
	 * @return the dbAccoutnName
	 */
	public String getDbAccoutnName() {
		return dbAccoutnName;
	}

	/**
	 * @param dbAccoutnName the dbAccoutnName to set
	 */
	public void setDbAccoutnName(String dbAccoutnName) {
		this.dbAccoutnName = dbAccoutnName;
	}

	/**
	 * @return the dbAccountPwd
	 */
	public String getDbAccountPwd() {
		return dbAccountPwd;
	}

	/**
	 * @param dbAccountPwd the dbAccountPwd to set
	 */
	public void setDbAccountPwd(String dbAccountPwd) {
		this.dbAccountPwd = dbAccountPwd;
	}

	/**
	 * @return the dbConnection
	 */
	public Connection getDbConnection() {
		return dbConnection;
	}

	/**
	 * @param dbConnection the dbConnection to set
	 */
	public void setDbConnection(Connection dbConnection) {
		this.dbConnection = dbConnection;
	}

	/**
	 * @return the dbStatement
	 */
	public Statement getDbStatement() {
		return dbStatement;
	}

	/**
	 * @param dbStatement the dbStatement to set
	 */
	public void setDbStatement(Statement dbStatement) {
		this.dbStatement = dbStatement;
	}

	/**
	 * @return the dbQueryResults
	 */
	public ResultSet getDbQueryResults() {
		return dbQueryResults;
	}

	/**
	 * @param dbQueryResults the dbQueryResults to set
	 */
	public void setDbQueryResults(ResultSet dbQueryResults) {
		this.dbQueryResults = dbQueryResults;
	}


}
