import static java.lang.System.*;
import java.sql.*;	

class Connection {

	private static final long serialVersionUID = 10L;
	private java.sql.Connection connection = null;	
	public long connected = -1;

	Connection (String url, String user, String password) 
	{
		try {
				long start = currentTimeMillis ();

				Class.forName ("oracle.jdbc.driver.OracleDriver");
				this.connection = 
					DriverManager.getConnection (url, user, password);	

				long millisec_took = currentTimeMillis ();
				millisec_took -= start;
				connected = millisec_took;
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

	public synchronized void
		query (String sql_query)
	{		
		java.sql.Statement statement = null;
		java.sql.ResultSet result_set = null; 		
			
		try {
			statement = this.connection.createStatement ();
			result_set = statement.executeQuery (sql_query); 		
			if (result_set != null) {
				out.println ("Nu e null");
			}

			while (result_set.next ()) {
				DataModel data_model = new DataModel (result_set);
				out.println (data_model);
			}
	
						if (statement != null) {
				statement.close ();
			}	

	
		
		
		} catch (java.sql.SQLException sqlex) {
			sqlex.printStackTrace ();
		
		} finally {
		}   
  	}
}
