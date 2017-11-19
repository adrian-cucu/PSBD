import static java.lang.System.*;
import java.sql.*;	

class Connection {

	private static final long serialVersionUID = 10L;
	private java.sql.Connection connection = null;	

	Connection (String url, String user, String password) 
	{
		try {
			Class.forName ("oracle.jdbc.driver.OracleDriver");
			this.connection = DriverManager.getConnection (url, user, password);	
		} 
		catch (SQLException e) {
			err.println ("Nu merge!");
			e.printStackTrace ();
		} 
		catch (ClassNotFoundException e) {	
			err.println ("Class not found");
			e.printStackTrace();
		}
	}

	public java.sql.Connection getConnection ()
	{
		return this.connection;
	}
	
	public boolean isConnected ()
	{
		if (this.connection != null) {
			return true;
		}
		return false;
	}

	public synchronized boolean query (String sql_query)
	{		
		java.sql.Statement statement = null;
		java.sql.ResultSet result_set = null; 		
		
		boolean success = true;	
		
		try {
			statement = this.connection.createStatement ();
			result_set = statement.executeQuery (sql_query); 		

			ResultSetMetaData rsmd = result_set.getMetaData ();
			int m = rsmd.getColumnCount ();	

			for (int i=1; i<=m; ++i) {
				System.out.print (rsmd.getColumnLabel (i) + "   ");
			}			
			System.out.println ("--------------------------------");
			
			while (result_set.next ()) {
				//DataModel data_model = new DataModel (result_set);
				//out.println (data_model);
				for (int i=1; i<=m; ++i) {
					System.out.print (result_set.getObject (i)+ "   ");
				}			
				System.out.println ();
			}
			

			if (statement != null) {
				statement.close ();
			}
	
	
		} catch (java.sql.SQLException sqlex) {
			sqlex.printStackTrace ();
			success = false;
		
		} finally {

		return success;
		}   
  	}
}
