import java.util.Vector;
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
			conn.setAutoCommit (false);
		} 
		catch (SQLException sqlex) {
			throw new ConnectionErrorException (sqlex.getMessage ());	
		} 
		catch (ClassNotFoundException cnfe) {	
			throw new DriverNotFoundException (cnfe.getMessage ());
		}
	}


	public Vector<ClasaDataModel> getAllClase ()
	{
		Vector <ClasaDataModel> data = new Vector <>();
		
		ResultSet result_set;
		Statement statement;	

		try {
			statement = getStatement ();
			result_set = statement.executeQuery ("SELECT * FROM clasa");

			while (result_set.next ()) {
				data.add (new ClasaDataModel (result_set));
			}
		}
		catch (java.sql.SQLException e) {
			return null;
		}	
		return data;
	}


	public Vector<ElevDataModel> getAllElevi ()
	{
		Vector <ElevDataModel> data = new Vector <>();
		
		ResultSet result_set;
		Statement statement;	

		try {
			statement = getStatement ();
			result_set = statement.executeQuery ("SELECT * FROM elev");

			while (result_set.next ()) {
				data.add (new ElevDataModel (result_set));
			}
		}
		catch (java.sql.SQLException e) {
			return null;
		}	
		return data;
	}


	public Vector<ProfilDataModel> getAllProfil ()
	{
		Vector <ProfilDataModel> data = new Vector <>();
		
		ResultSet result_set;
		Statement statement;	

		try {
			statement = getStatement ();
			result_set = statement.executeQuery ("SELECT * FROM profil");
			
			while (result_set.next ()) {
				data.add (new ProfilDataModel (result_set));
			}
		}
		catch (java.sql.SQLException e) {
			return null;
		}
		return data;
	}


	public Vector <MaterieDataModel> fetchMaterie ()
	{
		Vector <MaterieDataModel> data = new Vector <>();
		
		ResultSet result_set;
		Statement statement;	

		try {
			statement = getStatement ();
			result_set = statement.executeQuery ("SELECT * FROM materie");
			
			while (result_set.next ()) {
				data.add (new MaterieDataModel (result_set));
			}
		}
		catch (java.sql.SQLException e) {
			return null;
		}
		return data;
	
	
	}
	
	
	public synchronized Statement getStatement ()
		throws SQLException
	{
		Statement st = conn.createStatement ();
		return st;
	}


	public synchronized PreparedStatement getPreparedStatement (String prepared)
		throws SQLException
	{
		PreparedStatement pst = conn.prepareStatement (prepared);
		return pst;
	}

	
	public boolean isConnected ()
	{
		return (conn != null) ? true : false;
	}

	
	public void close (boolean commit)
		throws SQLException
	{
		if (commit) {
			conn.commit ();
		} else {
			conn.rollback ();
		}
		conn.close ();
	}

}
