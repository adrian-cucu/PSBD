import java.util.Vector;
import java.sql.*;	

class MyConnection {

	private static final long serialVersionUID = 0xad1;
	private Connection conn = null;	

	public MyConnection (String url, String user, String password) 
		throws DriverNotFoundException, ConnectionErrorException
	{
		try {
			Class.forName ("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection (url, user, password);	
			conn.setAutoCommit (false);

		} catch (SQLException sqlex) {
			throw new ConnectionErrorException (sqlex.getMessage ());	

		} catch (ClassNotFoundException cnfe) {	
			throw new DriverNotFoundException (cnfe.getMessage ());
		}
	}


	public Vector <Vector <Object>> fetchClase ()
	{
		Vector <Vector <Object>> fetchedData = new Vector <> ();
	
		ResultSet result_set = null;
		Statement statement = null;	
		Vector <Object> fetchedRow = null;

		try {
			statement = getStatement ();
			result_set = statement.executeQuery ("SELECT * FROM clasa");

			while (result_set.next ()) {
				fetchedRow = ClasaDataModel.make (result_set);
				fetchedData.add (fetchedRow);
			}

		} catch (java.sql.SQLException e) {
			return null;

		} finally {
			closeQuietly (result_set);
			closeQuietly (statement);
		}

		return fetchedData;
	}


	public Vector <ElevDataModel> fetchElevi ()
	{
		Vector <ElevDataModel> data = new Vector <>();
		
		ResultSet result_set = null;
		Statement statement = null;	

		try {
			statement = getStatement ();
			result_set = statement.executeQuery ("SELECT * FROM elev");

			while (result_set.next ()) {
				data.add (new ElevDataModel (result_set));
			}

		} catch (java.sql.SQLException e) {
			return null;
	
		} finally {
			closeQuietly (result_set);
			closeQuietly (statement);
		}	
		return data;
	}


	public Vector<ProfilDataModel> fetchProfil ()
	{
		Vector <ProfilDataModel> data = new Vector <>();
		
		ResultSet result_set = null;
		Statement statement = null;	

		try {
			statement = getStatement ();
			result_set = statement.executeQuery ("SELECT * FROM profil");
			
			while (result_set.next ()) {
				data.add (new ProfilDataModel (result_set));
			}

		} catch (java.sql.SQLException e) {
			return null;

		} finally {
			closeQuietly (result_set);
			closeQuietly (statement);
		} 
		return data;
	}


	public Vector <MaterieDataModel> fetchMaterie ()
	{
		Vector <MaterieDataModel> data = new Vector <>();
		
		ResultSet result_set = null;
		Statement statement = null;	

		try {
			statement = getStatement ();
			result_set = statement.executeQuery ("SELECT * FROM materie");
			
			while (result_set.next ()) {
				data.add (new MaterieDataModel (result_set));
			}

		} catch (java.sql.SQLException e) {
			return null;

		} finally {
			closeQuietly (result_set);
			closeQuietly (statement);
		}
		return data;	
	}
	
	
	public synchronized Statement getStatement ()
		throws SQLException
	{
		Statement st = conn.createStatement ();
		return st;
	}


	public synchronized 
		PreparedStatement getPreparedStatement (String prepared)
			throws SQLException
	{
		PreparedStatement pst = conn.prepareStatement (prepared);
		return pst;
	}


	public synchronized 	
		PreparedStatement getPreparedStatement (String prepared, int sts)
			throws SQLException
	{
		PreparedStatement pst = conn.prepareStatement (prepared, sts);
		return pst;
	}


	public synchronized 
		PreparedStatement getPreparedStatement (String prepared, String[] col)
			throws SQLException
	{
		PreparedStatement pst = conn.prepareStatement (prepared, col);
		return pst;
	}

	
	public boolean isConnected ()
	{
		return (conn != null) ? true : false;
	}

	
	public Connection get ()
	{
		return conn;
	}

	
	public void close (boolean commitOnClose)
		throws SQLException
	{
		if (commitOnClose) {
			conn.commit ();

		} else {
			conn.rollback ();
		}
		conn.close ();
	}


    public static void closeQuietly (Statement statement)
    {
        try {
            if (statement != null) {
                statement.close ();
            }
        } catch (SQLException sqlException) {
            /* quiet */
        }
    }


    public static void closeQuietly (ResultSet resultSet)
    {
        try {
            if (resultSet != null) {
                resultSet.close ();
            }
        } catch (SQLException sqlException) {
            /* quiet */
        }
    }
}
