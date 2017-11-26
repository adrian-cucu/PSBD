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


	public java.util.Vector<ClasaDataModel> getAllClase ()
	{
		java.util.Vector<ClasaDataModel> data = new java.util.Vector<>();
		
		ResultSet result_set;
		Statement statement;	

		try {
			statement = getConnection().createStatement ();
			result_set = statement.executeQuery ("SELECT * FROM clasa");

			while (result_set.next ()) {
				int id_clasa = result_set.getInt (1);
				int id_profil = result_set.getInt (2);
				int an_scolar = result_set.getInt (3);
				String cod = result_set.getString (4);
				int clasa = result_set.getInt (5);		
				data.add (new ClasaDataModel (id_clasa, id_profil, an_scolar, cod, clasa));
			}
		}
		catch (java.sql.SQLException e) {
			return null;
		}	
		return data;
	}


	public java.util.Vector<ProfilDataModel> getAllProfil ()
	{
		java.util.Vector<ProfilDataModel> data = new java.util.Vector<>();
		
		ResultSet result_set;
		Statement statement;	

		try {
			statement = getConnection().createStatement ();
			result_set = statement.executeQuery ("SELECT * FROM profil");

			while (result_set.next ()) {
				int id_profil = result_set.getInt (1);
				String nume_profil = result_set.getString (2);	
				data.add (new ProfilDataModel (id_profil, nume_profil));
			}
		}
		catch (java.sql.SQLException e) {
			return null;
		}	
		return data;
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
