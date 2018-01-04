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

	
	public boolean insertMedie (int id_elev, int id_clasa, int id_materie, int medie, int semestru)
	{	
		ResultSet resSet1 = null;
		PreparedStatement prepStm1 = null;	
		PreparedStatement prepStm2 = null;			
		String prepQuery1 = "SELECT * FROM medie WHERE id_elev = ? AND id_clasa = ? AND id_materie = ? AND semestru = ?";		   
		String prepQuery2 = "INSERT INTO medie (id_elev, id_clasa, id_materie, med_sem, semestru) VALUES (?, ?, ?, ?, ?)";

		String prepQuery3 = "UPDATE medie SET med_sem = ? WHERE id_elev = ? AND id_clasa = ? AND id_materie = ? AND semestru = ?";
	
		boolean ret = false;

		try {
			prepStm1 = getPreparedStatement (prepQuery1);	
			prepStm1.setInt (1, id_elev);
			prepStm1.setInt (2, id_clasa);	
			prepStm1.setInt (3, id_materie);
			prepStm1.setInt (4, semestru);
			resSet1 = prepStm1.executeQuery ();

			if (resSet1.next ()) {
				prepStm2 = getPreparedStatement (prepQuery3);
				prepStm2.setInt (1, medie);
				prepStm2.setInt (2, id_elev);
				prepStm2.setInt (3, id_clasa);
				prepStm2.setInt (4, id_materie);	
				prepStm2.setInt (5, semestru);
				
				ret = prepStm2.execute ();

			} else {
				prepStm2 = getPreparedStatement (prepQuery2);
				prepStm2.setInt (1, id_elev);
				prepStm2.setInt (2, id_clasa);
				prepStm2.setInt (3, id_materie);	
				prepStm2.setInt (4, medie);	
				prepStm2.setInt (5, semestru);
				
				ret = prepStm2.execute ();
			}

		} catch (SQLException e) {
			/* ignore */
		
		} finally {
			closeQuietly (resSet1);
			closeQuietly (prepStm1);
			closeQuietly (prepStm2);
		}

		return true;
	}					


	public boolean 
	executeUpdate (String tableName, String idColName, String updatedColName, int ID, Object newVal, Class<?> classType)
	{
		boolean ret = false;
		PreparedStatement prepStm = null;
		String prepQuery = 
			"UPDATE " + tableName + 
			" SET " +  updatedColName + " = ? " + 
			"WHERE " +  idColName + " = ?";

		try {
			prepStm = getPreparedStatement (prepQuery);
	
			if (classType == Integer.class) {
				prepStm.setInt (1, ((Integer) newVal).intValue ());
			} else if (classType == String.class) {	
				prepStm.setString (1, (String)newVal);	
			}
			
			prepStm.setInt (2, ID);	
			ret = prepStm.execute ();

		} catch (SQLException e) {
			/* ignore */
		
		} finally {
			closeQuietly (prepStm);
		}
		
		return true;
	}	


	public Vector <Vector <Object>> fetchMediCls (int id_elev, int id_clasa, int id_profil, int an_studiu)
	{
		Vector <Vector <Object>> inf = new Vector <Vector <Object>> (3);	
		inf.add (new Vector <>());
		inf.add (new Vector <>());
		inf.add (new Vector <>());

		ResultSet resSet = null;
		PreparedStatement prepStm = null;
		String prepQuery = 
			"SELECT mat.id_materie, mat.nume_materie " + 
			"FROM materie mat " + 
			"INNER JOIN profil_materie pfl_mat ON " + 
			"pfl_mat.id_materie = mat.id_materie " +
			"WHERE pfl_mat.id_profil = ? AND pfl_mat.an_studiu = ?";

		ResultSet resSet1 = null;
		PreparedStatement prepStm1 = null;
		String prepQuery1 = 
			"SELECT med_sem " + 
			"FROM medie " + 
			"WHERE id_elev = ? " + 
			"AND id_clasa = ? " + 
			"AND id_materie = ? " + 
			"AND semestru = ?";

		int med_sem = -1;

		try {
			prepStm = getPreparedStatement (prepQuery);	
			prepStm.setInt (1, id_profil);
			prepStm.setInt (2, an_studiu);
			resSet = prepStm.executeQuery ();

			prepStm1 = getPreparedStatement (prepQuery1);

			while (resSet.next ()) {
				int id_materie = resSet.getInt (1);
				String nume_materie = resSet.getString (2);
		
				inf.get (0).add (new MaterieDataModel (resSet));

				prepStm1.setInt (1, id_elev);
				prepStm1.setInt (2, id_clasa);
				prepStm1.setInt (3, id_materie);

				for (int semestru = 1; semestru <= 2; ++ semestru) {
					prepStm1.setInt (4, semestru);
					resSet1 = prepStm1.executeQuery ();
					med_sem = -1;
					try {
						if (resSet1.next ()) {
							med_sem = resSet1.getInt (1);	
						}
					} catch (SQLException sqlex) {	
						System.out.println ("[DEBUG]: " + sqlex.getMessage ());
					}
					inf.get (semestru).add (new Integer (med_sem));
				}				
			}

		} catch (SQLException e) {
			/* ignore */
			System.out.println (e.getMessage ());
		
		} finally {
			closeQuietly (resSet);
			closeQuietly (prepStm);
			closeQuietly (resSet1);
			closeQuietly (prepStm1);
		}

		/*
		for (int i = 0; i < inf.get (0).size (); ++ i) {
			System.out.print (inf.get (0).get(i));
			int nota_sem1 = ((Integer)inf.get (1).get (i)).intValue ();
			int nota_sem2 = ((Integer)inf.get (2).get (i)).intValue ();
			System.out.println (
				" sem  I " + (nota_sem1 == -1 ? "Fara nota " : nota_sem1) + "  " + 
			    " sem II " + (nota_sem2 == -1 ? "Fara nota " : nota_sem2)
			);
		}		
		*/
		return inf;	
	}


	public Vector <ClasaDataModel> getClsElev (int id_elev)
	{
		Vector <ClasaDataModel> fetchedData = null;
	
		ResultSet resSet1 = null;
		ResultSet resSet2 = null;		
		ResultSet resSet3 = null;
		PreparedStatement prepStm1 = null;	
		PreparedStatement prepStm2 = null;			
		PreparedStatement prepStm3 = null;	
		String prepQuery1 = "SELECT id_clasa FROM elev_clasa WHERE id_elev = ?";		   
		String prepQuery2 = "SELECT * FROM clasa WHERE id_clasa = ?";
		String prepQuery3 = "SELECT nume_profil FROM profil WHERE id_profil = ?";
		int id_cls = -1;
		int id_profil = -1;

		try {
			prepStm3 = getPreparedStatement (prepQuery3);
			prepStm2 = getPreparedStatement (prepQuery2);
			prepStm1 = getPreparedStatement (prepQuery1);
			prepStm1.setInt (1, id_elev);
			resSet1 = prepStm1.executeQuery ();

			while (resSet1.next ()) {
				id_cls = resSet1.getInt (1);
				
				if (fetchedData == null) {
					fetchedData = new  Vector <ClasaDataModel> ();
				}	
				
				prepStm2.setInt (1, id_cls);
				resSet2 = prepStm2.executeQuery ();

				while (resSet2.next ()) {	
					ClasaDataModel cls = new ClasaDataModel (resSet2);
					id_profil = cls.getIdProfil ();	
					prepStm3.setInt (1, id_profil);
					resSet3 = prepStm3.executeQuery ();

					if (resSet3.next ()) {
						cls.setNumeProfil (resSet3.getString (1));
					}
						
					fetchedData.add (cls);
				}
			}

		} catch (SQLException e) {
			/* ignore */
		
		} finally {
			closeQuietly (resSet3);
			closeQuietly (resSet2);
			closeQuietly (resSet1);
			closeQuietly (prepStm3);
			closeQuietly (prepStm2);
			closeQuietly (prepStm1);
		}

		return fetchedData;
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

		ResultSet resSet = null;
		PreparedStatement prepStm = null;	
		String prepQuery = "SELECT nume_profil FROM profil WHERE id_profil = ?";
		int id_profil = -1;

		try {
			prepStm = getPreparedStatement (prepQuery);
			statement = getStatement ();
			result_set = statement.executeQuery ("SELECT * FROM clasa");

			while (result_set.next ()) {
				if (fetchedData == null) {
					fetchedData = new Vector <ClasaDataModel> ();
				}
				ClasaDataModel cls = new ClasaDataModel (result_set);

				id_profil = result_set.getInt (2);
					
				prepStm.setInt (1, id_profil);
			
				resSet = prepStm.executeQuery ();

				if (resSet.next ()) {
					cls.setNumeProfil (resSet.getString (1));
				}		

				fetchedData.add (cls);
			}

		} catch (SQLException e) {
			/* ignore */
		
		} finally {
			closeQuietly (result_set);
			closeQuietly (statement);
			closeQuietly (resSet);
			closeQuietly (prepStm);
		}

		return fetchedData;
	}


	public Vector <BursaDataModel> fetchTableBursaObjData ()
	{
		Vector <BursaDataModel> fetchedData = null;	

		ResultSet result_set = null;
		Statement statement = null;	

		try {
			statement = getStatement ();
			result_set = statement.executeQuery ("SELECT * FROM bursa");

			while (result_set.next ()) {
				if (fetchedData == null) {
					fetchedData = new Vector <>();
				}
				fetchedData.add (new BursaDataModel (result_set));
			}

		} catch (SQLException e) {
			/* ignore */
	
		} finally {
			closeQuietly (result_set);
			closeQuietly (statement);
		}	

		return fetchedData;
	}



	public Vector <Vector <Object>> fetchTableBursaRawData ()
	{
		Vector <Vector <Object>> fetchedData = null;
	
		ResultSet result_set = null;
		Statement statement = null;	
		Vector <Object> fetchedRow = null;

		try {
			statement = getStatement ();
			result_set = statement.executeQuery ("SELECT * FROM bursa");

			while (result_set.next ()) {
				fetchedRow = BursaDataModel.make (result_set);
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
