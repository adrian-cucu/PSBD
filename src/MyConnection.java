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


	public Vector <Vector <Object>> fetchTableClasaRawData ()
	{
		Vector <Vector <Object>> fetchedData = null;
	
		ResultSet result_set = null;
		Statement statement = null;	
		Vector <Object> fetchedRow = null;

		try {
			statement = getStatement ();
			result_set = statement.executeQuery ("SELECT * FROM clasa");

			while (result_set.next ()) {
				fetchedRow = ClasaDataModel.make (result_set);
				if (fetchedData == null) {
					fetchedData = new  Vector <Vector <Object>> ();
				}	
				fetchedData.add (fetchedRow);
			}

		} catch (SQLException e) {
			/* ignore */
		
		} finally {
			closeQuietly (result_set);
			closeQuietly (statement);
		}

		return fetchedData;
	}


	public Vector <ClasaDataModel> fetchTableClasaObjData ()
	{
		Vector <ClasaDataModel> fetchedData = null;
	
		ResultSet result_set = null;
		Statement statement = null;	

		try {
			statement = getStatement ();
			result_set = statement.executeQuery ("SELECT * FROM clasa");

			while (result_set.next ()) {
				if (fetchedData == null) {
					fetchedData = new Vector <>();
				}
				fetchedData.add (new ClasaDataModel (result_set));
			}

		} catch (SQLException e) {
			/* ignore */
		
		} finally {
			closeQuietly (result_set);
			closeQuietly (statement);
		}

		return fetchedData;
	}


	public Vector <Vector <Object>> fetchTableElevRawData ()
	{	
		Vector <Vector <Object>> fetchedData = null;	
		Vector <Object> fetchedRow = null;

		ResultSet result_set = null;
		Statement statement = null;	

		try {
			statement = getStatement ();
			result_set = statement.executeQuery ("SELECT * FROM elev");

			while (result_set.next ()) {
				fetchedRow = ElevDataModel.make (result_set);
				if (fetchedData == null) {
					fetchedData = new Vector <>();
				}
				fetchedData.add (fetchedRow);
			}

		} catch (SQLException e) {
			/* ignore */
	
		} finally {
			closeQuietly (result_set);
			closeQuietly (statement);
		}	

		return fetchedData;
	}


	public Vector <ElevDataModel> fetchTableElevObjData ()
	{
		Vector <ElevDataModel> fetchedData = null;	

		ResultSet result_set = null;
		Statement statement = null;	

		try {
			statement = getStatement ();
			result_set = statement.executeQuery ("SELECT * FROM elev");

			while (result_set.next ()) {
				if (fetchedData == null) {
					fetchedData = new Vector <>();
				}
				fetchedData.add (new ElevDataModel (result_set));
			}

		} catch (SQLException e) {
			/* ignore */
	
		} finally {
			closeQuietly (result_set);
			closeQuietly (statement);
		}	

		return fetchedData;
	}


	public Vector<ProfilDataModel> fetchTableProfilObjData ()
	{
		Vector <ProfilDataModel> fetchedData = null;
		
		ResultSet result_set = null;
		Statement statement = null;	

		try {
			statement = getStatement ();
			result_set = statement.executeQuery ("SELECT * FROM profil");
			
			while (result_set.next ()) {
				if (fetchedData == null) {
					fetchedData = new Vector <ProfilDataModel> ();
				}
				fetchedData.add (new ProfilDataModel (result_set));
			}

		} catch (SQLException e) {
			/* ignore */

		} finally {
			closeQuietly (result_set);
			closeQuietly (statement);
		} 

		return fetchedData;
	}


	public Vector<Vector<Object>> fetchTableProfilRawData ()
	{
		Vector <Vector<Object>> fetchedData = null;
		
		ResultSet result_set = null;
		Statement statement = null;	

		try {
			statement = getStatement ();
			result_set = statement.executeQuery ("SELECT * FROM profil");
			
			while (result_set.next ()) {
				if (fetchedData == null) {
					fetchedData = new Vector <Vector<Object>> ();
				}
				fetchedData.add (ProfilDataModel.make (result_set));
			}

		} catch (SQLException e) {
			/* ignore */

		} finally {
			closeQuietly (result_set);
			closeQuietly (statement);
		} 

		return fetchedData;
	}


	public Vector <MaterieDataModel> fetchTableMaterieObjData ()
	{	
		Vector <MaterieDataModel> fetchedData = null;

		ResultSet result_set = null;
		Statement statement = null;	

		try {
			statement = getStatement ();
			result_set = statement.executeQuery ("SELECT * FROM materie");
			
			while (result_set.next ()) {
				if (fetchedData == null) {
					fetchedData = new  Vector <MaterieDataModel> ();
				}
				fetchedData.add (new MaterieDataModel (result_set));
			}

		} catch (SQLException e) {
			/* ignore */

		} finally {
			closeQuietly (result_set);
			closeQuietly (statement);
		}

		return fetchedData;	
	}


	public Vector <Vector<Object>> fetchTableMaterieRawData ()
	{	
		Vector <Vector<Object>> fetchedData = null;

		ResultSet result_set = null;
		Statement statement = null;	

		try {
			statement = getStatement ();
			result_set = statement.executeQuery ("SELECT * FROM materie");
			
			while (result_set.next ()) {
				if (fetchedData == null) {
					fetchedData = new  Vector <Vector<Object>> ();
				}
				fetchedData.add (MaterieDataModel.make (result_set));
			}

		} catch (SQLException e) {
			/* ignore */

		} finally {
			closeQuietly (result_set);
			closeQuietly (statement);
		}

		return fetchedData;	
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
