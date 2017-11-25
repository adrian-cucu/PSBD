import java.sql.*;	

class MyConnection {


	private static final long serialVersionUID = 10L;
	private Connection conn = null;	


	public MyConnection (String url, String user, String password) 
		throws DriverNotFoundException, ConnectionErrorException
	{
		try {
			Class.forName ("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection (url, user, password);	
		} 
		catch (SQLException sqlex) {
			throw new ConnectionErrorException (sqlex.getMessage ());	
		} 
		catch (ClassNotFoundException cnfe) {	
			throw new DriverNotFoundException (cnfe.getMessage ());
		}
	}


	public Connection getConnection ()
	{
		return conn;
	}

	
	public boolean isConnected ()
	{
		return (conn != null) ? true : false;
	}

}
