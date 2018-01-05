import static javax.swing.JOptionPane.*;
import static java.lang.System.*;
import java.util.Vector;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

class AppController implements ActionListener, WindowListener {

	private LoginView login_view = null;
	private AppView app_view = null;
	private MyConnection con = null;

	public AppController (LoginView login_view)
	{		
		this.login_view = login_view;
		this.login_view.addLoginListener (this);
		this.login_view.setVisible (true);
	}  


	private void login ()
	{
		String host = login_view.getHost ();	
		String user = login_view.getUsername ();
		int portno = login_view.getPort ();
		String sid = login_view.getSID ();
	
		if (InputCheck.check (host) && InputCheck.check (user) && 
			InputCheck.check (login_view.getPass ()) &&
			portno > 0 && portno < (1 << 16) && InputCheck.check (sid)) {

			String url = String.format ("jdbc:oracle:thin:@%s:%d:%s", 
											host, portno, sid);
		
			try {
				con = new MyConnection (url, user, login_view.getPass ());
				login_view.dispose ();
				login_view = null;	
				String conInfo = user + "@" + host + ":" + portno + ":" + sid;
				app_view = new AppView (conInfo, con);
				app_view.addListener (this);	
				app_view.windowCloseListener (this);
				app_view.setVisible (true);		

			} catch (ConnectionErrorException ce) {
				login_view.displayError (ce.getMessage ());

			} catch (DriverNotFoundException dnfe) {
				login_view.displayError (dnfe.getMessage ());

			} 			

		} else { 
			login_view.displayError ("Invalid input field !");
		}
	}


	private void query_btn ()
	{
		String query = app_view.getQueryText ();	
		query = query.trim ()
					 .replaceAll ("\n", " ")
					 .replace (";", "");
			
		String op = query.split (" ")[0].toUpperCase();
		Statement statement = null;
		ResultSet result_set = null;
		long start = 0, stop = 0;	
		int rr = 0;

		switch (op) {
			case "SELECT":	
				Vector <String> columns = null; 
				Vector <Object> row = null;
				Vector <Vector<Object>> values = null;	
				int columnCount;
				int i;

     			try {
            		statement = con.getStatement ();
					start = System.currentTimeMillis ();	
           			result_set = statement.executeQuery (query);
					stop = System.currentTimeMillis ();	
					stop -= start;		
					app_view.updateStatus ("query took: " + 
						(stop / 1000) + "." + 
						(stop % 1000) + " s", AppView.SUCCESS);		

           			ResultSetMetaData rsmd = result_set.getMetaData ();
            		columnCount = rsmd.getColumnCount ();
					columns = new Vector <> (columnCount);

           			for (i = 1; i <= columnCount; ++ i) {
                		columns.add (rsmd.getColumnLabel (i));
            		}

					values = new Vector <Vector <Object>> ();	

            		while (result_set.next ()) {
						row = new Vector <Object> (columnCount);	
                		for (i = 1; i <= columnCount; ++ i) {
                    		row.add (result_set.getObject (i));
                		}
                		values.add(row);
            		}
			
        			JTable table = new JTable (values, columns);
					table.setPreferredScrollableViewportSize (
							new Dimension (700, 130)
					);
    	    		table.setFillsViewportHeight (true);	
					table.setRowHeight (table.getRowHeight ());
					table.setAutoCreateRowSorter (true);
					table.setEnabled (false);
	 		   		app_view.addQueryResult (table);
       
        		} catch (SQLException sqlex) {
					app_view.addQueryResult (sqlex.getMessage ());
					app_view.updateStatus ("query error!", AppView.ERROR);

				} finally {
					MyConnection.closeQuietly (statement);
					MyConnection.closeQuietly (result_set);
				}

				break;
			case "INSERT":
			case "UPDATE":
			case "DELETE":
				try {
            		statement = con.getStatement ();
					start = System.currentTimeMillis ();	
            					
					rr = statement.executeUpdate (query);
					stop = System.currentTimeMillis ();	
					stop -= start;		
					app_view.updateStatus ("query took: " + 
						(stop / 1000) + "." + 
						(stop % 1000) + " s", AppView.SUCCESS);		

					if (op.equals ("INSERT")) {
						app_view.addQueryResult (rr + " row inserted.");

					} else if (op.equals ("UPDATE")) {
						app_view.addQueryResult (rr + " rows updated.");

					} else {
						app_view.addQueryResult (rr + " rows deleted.");
					}
					app_view.refresh ();

        		} catch (SQLException sqlex) {
            		sqlex.printStackTrace ();
					String err_msg = sqlex.getMessage ();
					err_msg = err_msg.substring (err_msg.indexOf (":") + 2);
					app_view.addQueryResult (err_msg);
					app_view.updateStatus ("query error!", AppView.ERROR);
	
        		} finally {
					MyConnection.closeQuietly (statement);
				}	
				break;
			default:
				app_view.addQueryResult ("Unknown statement");	
				app_view.updateStatus ("Error!", AppView.ERROR);
		}		
	}

	
	private void insert_elev_btn ()
	{
		String prepared = "INSERT INTO elev (nume, prenume, adresa, cnp, datan, etnie, nationalitate) VALUES (?, ?, ?, ?, ?, ?, ?)";
			
		/* Asta in caz de vrei sa alegi si o clasa cand adaugi un elev nou
		 	dar acest lucru se va rezolva in alt panel in care
			adaugi un elev existent in baza de date la o anumita clasa
		*/
		/*
		String prepared2 = 
			"INSERT INTO elev_clasa (id_elev, id_clasa) VALUES (?, ?)";	
		PreparedStatement ps2 = null;
		ResultSet generatedKeys = null;			
		*/

		PreparedStatement ps = null;

		try {
			/* Asta in caz de vrei sa alegi si o clasa cand adaugi un elev nou
				pentru ca trebuie retinut id-ul elevului adaugat
				pentru a fi inserat in tabela elev_clasa
				ID - ul elevului fiind generat de catre ORACLE cu ajutorul 
				trigger-ului
		 		dar acest lucru se va rezolva in alt panel in care
				adaugi un elev existent in baza de date la o anumita clasa
			*/
			//ps = con.getPreparedStatement (prepared, new String[]{"id_elev"});
			ps = con.getPreparedStatement (prepared);
	
			String nume = app_view.getNumeElev();
			String prenume = app_view.getPrenumeElev();
			String adresa = app_view.getAdresaElev();
			String cnp = app_view.getCnpElev();
			String etnie = app_view.getEtnieElev();
			String nationalitate = app_view.getNationalotateElev();
			java.util.Date datan = app_view.getDataNasterii ();

			ElevDataModel.checkNume (nume);	
			ElevDataModel.checkPrenume (prenume);	
			ElevDataModel.checkAdresa (adresa);	
			ElevDataModel.checkCnp (cnp);	
			ElevDataModel.checkEtnie (etnie);		
			ElevDataModel.checkNationalitate (nationalitate);	
			ElevDataModel.checkDataNasterii (datan);	
		
			ps.setString (1, nume);
			ps.setString (2, prenume);
			ps.setString (3, adresa);
			ps.setString (4, cnp);
			ps.setDate (5, new java.sql.Date (datan.getTime ()));
			ps.setString (6, etnie);
			ps.setString (7, nationalitate);

			ps.executeUpdate ();

			/* Asta in caz de vrei sa alegi si o clasa cand adaugi un elev nou
				pentru ca trebuie retinut id-ul elevului adaugat
				pentru a fi inserat in tabela elev_clasa
				ID - ul elevului fiind generat de catre ORACLE cu ajutorul 
				trigger-ului
		 		dar acest lucru se va rezolva in alt panel in care
				adaugi un elev existent in baza de date la o anumita clasa
			*/
			/*	
			generatedKeys = ps.getGeneratedKeys ();

			if (generatedKeys.next ()) {
				int id_elev = Integer.parseInt (generatedKeys.getString (1));	
				int id_clasa = app_view.getClasaComboBoxSelectedID ();
				out.println ("ID - ul ultimului elev: " + id_elev + 
							 " cu clasa " + id_clasa);

				ps2 = con.getPreparedStatement (prepared2);				
				ps2.setInt (1, id_elev);
				ps2.setInt (2, id_clasa);
				ps2.executeUpdate ();
			}
			*/

			ElevTableModel.refresh (con);			
			app_view.displayInformation (ps.getUpdateCount () + " rows inserted.");
			app_view.updateStatus ("-[INSERT]- success", AppView.SUCCESS);

		} catch (SQLException sqlex) {	
			app_view.displayError (sqlex.getMessage ());
			app_view.updateStatus ("-[INSERT]- fail", AppView.ERROR);

		} catch (Exception ex) {
			app_view.displayError (ex.getMessage ());
			app_view.updateStatus ("--[INSERT]- fail", AppView.ERROR);	
			
		} finally {
			MyConnection.closeQuietly (ps);				
			// MyConnection.closeQuietly (generatedKeys);
		}		
		
	}

		
	private void delete_elev_btn ()
	{
		String prepared = "SELECT * FROM elev_clasa WHERE id_elev = ?";
		String prepared2 = "DELETE FROM elev WHERE id_elev = ?";
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;

		Vector <Integer> selected_id = app_view.getTableElevSelectedIDs (true);

		if (selected_id == null) {
			return;
		}

		try {
			ps = con.getPreparedStatement (prepared); 		
			ps2 = con.getPreparedStatement (prepared2); 		

			for (int id : selected_id) {

				ps.setInt (1, id);
				rs = ps.executeQuery ();

				if (rs != null)
					if (!rs.next ()) {
						ps2.setInt (1, id);
						ps2.addBatch ();
					}
			}
		
			ps2.executeBatch ();
	
			int rowsAffected = ps2.getUpdateCount ();

			if (rowsAffected > 0) {		
				ElevTableModel.refresh (con);
				app_view.displayInformation (rowsAffected + " rows deleted.");
				app_view.updateStatus ("-[DELETE]- success", AppView.SUCCESS);
		
			} else {	
				app_view.displayInformation ("No rows affected");
			}

		} catch (SQLException sqlex) {
			app_view.displayError (sqlex.getMessage ());
			app_view.updateStatus ("-[DELETE]- success", AppView.ERROR);

		} catch (Exception ex) {
			app_view.displayError (ex.getMessage ());
			app_view.updateStatus ("--[DELETE]- fail", AppView.ERROR);	
			
		} finally {
			MyConnection.closeQuietly (ps2);
			MyConnection.closeQuietly (ps);
			MyConnection.closeQuietly (rs);
		} 

	}
	
	
	private void insert_profil_btn ()
	{		
		Vector <Vector <MaterieDataModel>> lista_materii = 
			app_view.getMateriiIDs ();

		if (lista_materii == null) {
			return;
		}

		String prepared = "INSERT INTO profil (nume_profil) VALUES (?)";
		String prepared2 = "INSERT INTO profil_materie (id_profil, id_materie, an_studiu) VALUES (?, ?, ?)";
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet generatedKeys = null;

		try {
			ps = con.getPreparedStatement (prepared, new String[]{"id_profil"}); 	
			String profil = app_view.getProfil ();

			ProfilDataModel.checkNumeProfil (profil);

			ps.setString (1, profil);
			ps.executeUpdate ();

			generatedKeys = ps.getGeneratedKeys ();

			if (generatedKeys.next ()) {
				int id = Integer.parseInt (generatedKeys.getString (1));
				
				ps2 = con.getPreparedStatement (prepared2);	

				int clasa = 9;
				for (Vector <MaterieDataModel> materii : lista_materii)
				{
					for (MaterieDataModel materie : materii) {
						ps2.setInt (1, id);
						ps2.setInt (2, materie.getID ());
						ps2.setInt (3, clasa);
						ps2.addBatch ();
					}
					++clasa;
				}
				ps2.executeBatch ();

				ProfilTableModel.refresh (con);
				app_view.refresh ();
	
			} else {	
				throw new SQLException ("Failed to insert");
			}		

			app_view.displayInformation (ps.getUpdateCount () + " rows inserted.");
			app_view.updateStatus ("-[INSERT]- success", AppView.SUCCESS);

		} catch (SQLException sqlex) {	
			app_view.displayError (sqlex.getMessage ());
			app_view.updateStatus ("-[INSERT]- fail", AppView.ERROR);

		} catch (Exception ex) {
			app_view.displayError (ex.getMessage ());
			app_view.updateStatus ("--[INSERT]- fail", AppView.ERROR);	
			
		} finally {
			MyConnection.closeQuietly (ps);
			MyConnection.closeQuietly (ps2);
			MyConnection.closeQuietly (generatedKeys);
		}	
	}


	private void insert_clasa_btn ()
	{
		String prepared = "INSERT INTO clasa(id_profil, an_scolar, cod, an_studiu) VALUES (?, ?, ?, ?)";

		PreparedStatement ps = null; 	
		
		try {
			int an_scolar = app_view.getAnScolar ();
			String codClasa = app_view.getCodClasa ();
			int an_studiu = app_view.getAnStudiu ();		
			int id_profil = app_view.getComboBoxSelectedID ();
	
			ClasaDataModel.checkAnScolar (an_scolar);	
			ClasaDataModel.checkCod (codClasa);	
			ClasaDataModel.checkAnStudiu (an_studiu);	

			ps = con.getPreparedStatement (prepared); 	

			ps.setInt (1, id_profil);
			ps.setInt (2, an_scolar);
			ps.setString (3, codClasa);
			ps.setInt (4, an_studiu);
			
			ps.executeUpdate ();
			ClasaTableModel.refresh (con);
		
			app_view.displayInformation (ps.getUpdateCount () + " rows inserted.");
			app_view.updateStatus ("-[INSERT]- success", AppView.SUCCESS);

		} catch (SQLException sqlex) {	
			app_view.displayError (sqlex.getMessage ());
			app_view.updateStatus ("-[INSERT]- fail", AppView.ERROR);

		} catch (Exception ex) {
			app_view.displayError (ex.getMessage ());
			app_view.updateStatus ("--[INSERT]- fail", AppView.ERROR);	
		
		} finally {
			MyConnection.closeQuietly (ps);
		}
	}


	private void insert_materie_btn ()
	{
		String prepared = "INSERT INTO materie(nume_materie) VALUES (?)";
		PreparedStatement ps = null;

		try {
			ps = con.getPreparedStatement (prepared);

			int id = -1;
			String materie = app_view.getNumeMaterie ();	
			
			MaterieDataModel.checkNumeMaterie (materie);

			ps.setString (1, materie);
			
			ps.executeUpdate ();
			MaterieTableModel.refresh (con);
			app_view.refresh ();
	
			app_view.displayInformation (ps.getUpdateCount () + " rows inserted.");
			app_view.updateStatus ("-[INSERT]- success", AppView.SUCCESS);

		} catch (SQLException sqlex) {
			app_view.displayError (sqlex.getMessage ());
			app_view.updateStatus ("-[INSERT]- fail", AppView.ERROR);

		} catch (Exception ex) {
			app_view.displayError (ex.getMessage ());
			app_view.updateStatus ("--[INSERT]- fail", AppView.ERROR);	
			
		} finally {
			MyConnection.closeQuietly (ps);
		}
	}


	private void delete_materie_btn ()
	{
		String prepared = "DELETE FROM materie WHERE id_materie = ?";
		String prepared2 = "DELETE FROM profil_materie WHERE id_materie = ?";
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;

		Vector <Integer> selected_id = 
			app_view.getTableMaterieSelectedIDs (true);
			
		if (selected_id == null) {
			return ;
		} 

		try {
			ps = con.getPreparedStatement (prepared); 
			ps2 = con.getPreparedStatement (prepared2);									
			for (int id : selected_id) {
				ps.setInt (1, id);
				ps.addBatch ();
		
				ps2.setInt (1, id);
				ps2.addBatch ();
			}

			ps2.executeBatch ();
			ps.executeBatch ();

			MaterieTableModel.refresh (con);
	
			app_view.refresh ();

			app_view.displayInformation (ps.getUpdateCount () + " rows deleted.");
			app_view.updateStatus ("-[DELETE]- success", AppView.SUCCESS);

		} catch (SQLException sqlex) {
			app_view.displayError (sqlex.getMessage ());
			app_view.updateStatus ("-[DELETE]- success", AppView.ERROR);

		} catch (Exception ex) {
			app_view.displayError (ex.getMessage ());
			app_view.updateStatus ("--[DELETE]- fail", AppView.ERROR);	
			
		} finally {
			MyConnection.closeQuietly (ps2);
			MyConnection.closeQuietly (ps);
		}
	}


	private void update_materie_btn ()
	{	
		String prepared = 
			"UPDATE materie SET nume_materie = ? WHERE id_materie = ?";
		PreparedStatement ps = null;

		Vector <Vector <Object>> selected_id = app_view.getTableMaterieUpdate ();

		if (selected_id == null) {
			return ;
		} 

		try {
			ps = con.getPreparedStatement (prepared); 	

			for (Vector <Object> rr : selected_id)
			{
				int ID = (( Integer )rr.get (0)).intValue ();
				String materie = ( String )rr.get (1);

				ps.setInt (2, ID);
				ps.setString (1, materie);
				ps.addBatch ();
			}

			ps.executeBatch ();
			MaterieTableModel.refresh (con);

			app_view.displayInformation (ps.getUpdateCount () + " rows updated.");
			app_view.updateStatus ("-[UPDATE]- success", AppView.SUCCESS);

		} catch (SQLException sqlex) {	
			app_view.displayError (sqlex.getMessage ());
			app_view.updateStatus ("-[UPDATE]- fail", AppView.ERROR);
		
		} catch (Exception ex) {
			app_view.displayError (ex.getMessage ());
			app_view.updateStatus ("--[UPDATE]- fail", AppView.ERROR);		

		} finally {
			MyConnection.closeQuietly (ps);
		}	
	}


	private void delete_profil_btn ()
	{
		String prepared = "SELECT * FROM clasa WHERE id_profil = ?";
		String prepared2 = "DELETE FROM profil WHERE id_profil = ?";
		String prepared3 = "DELETE FROM profil_materie WHERE id_profil = ?";
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs = null;

		Vector <Integer> selected_id = app_view.getTableProfilSelectedIDs (true);
		if (selected_id == null) {
			return;
		}

		try {
			ps = con.getPreparedStatement (prepared); 		
			ps2 = con.getPreparedStatement (prepared2); 		
			ps3 = con.getPreparedStatement (prepared3);

			for (int id : selected_id) {

				ps.setInt (1, id);
				rs = ps.executeQuery ();

				if (rs != null)
					if (!rs.next ()) {
						ps2.setInt (1, id);
						ps2.addBatch ();
						ps3.setInt (1, id);
						ps3.addBatch ();
					}
			}
		
			ps3.executeBatch ();
			ps2.executeBatch ();
	
			int rowsAffected = ps2.getUpdateCount ();

			if (rowsAffected > 0) {		
				ProfilTableModel.refresh (con);
				app_view.refresh ();
				app_view.displayInformation (rowsAffected + " rows deleted.");
				app_view.updateStatus ("-[DELETE]- success", AppView.SUCCESS);
		
			} else {	
				app_view.displayInformation ("No rows affected");
			}

		} catch (SQLException sqlex) {
			app_view.displayError (sqlex.getMessage ());
			app_view.updateStatus ("-[DELETE]- success", AppView.ERROR);

		} catch (Exception ex) {
			app_view.displayError (ex.getMessage ());
			app_view.updateStatus ("--[DELETE]- fail", AppView.ERROR);	
			
		} finally {
			MyConnection.closeQuietly (ps2);
			MyConnection.closeQuietly (ps);
			MyConnection.closeQuietly (rs);
		} 
	}

	
	private void delete_clasa_btn ()
	{
		String prepared = "SELECT * FROM elev_clasa WHERE id_clasa = ?";
		String prepared2 = "DELETE FROM clasa WHERE id_clasa = ?";
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;

		Vector <Integer> indices = app_view.getTableClasaSelectedIDs (true);
					
		if (indices == null) {
			return;
		}					
	
		try {
			ps = con.getPreparedStatement (prepared); 	
			ps2 = con.getPreparedStatement (prepared2);

			for (int id : indices) {
				
				ps.setInt (1, id);
				rs = ps.executeQuery ();
				
				if (rs != null)
					if (!rs.next ()) {
						out.print (" trebuie sters");
						ps2.setInt (1, id);
						ps2.addBatch ();
				}
			}

			ps2.executeBatch ();

			int rowsAffected = ps2.getUpdateCount ();

			if (rowsAffected > 0) {		
				ClasaTableModel.refresh (con);
				app_view.displayInformation (rowsAffected + " rows deleted.");
				app_view.updateStatus ("-[DELETE]- success", AppView.SUCCESS);
		
			} else {	
				app_view.displayInformation ("No rows affected");
			}

		} catch (SQLException sqlex) {
			app_view.displayError (sqlex.getMessage ());
			app_view.updateStatus ("--[DELETE]- fail", AppView.ERROR);	
			
		} catch (Exception ex) {
			app_view.displayError (ex.getMessage ());
			app_view.updateStatus ("--[DELETE]- fail", AppView.ERROR);		
			
		} finally {
			MyConnection.closeQuietly (ps2);
			MyConnection.closeQuietly (ps);
			MyConnection.closeQuietly (rs);
		}  
	}

	private void filter_bursa ()
	{
		String preparedQuery = 
			"SELECT * " + 
			"FROM (" + 
			 "SELECT " +
			 " e.nume||' '||e.prenume \"Nume elev\", " +
			 " c.an_studiu||' '||c.cod \"Clasa\", " +
			 " p.nume_profil \"Profil\", " + 
			 " TO_CHAR (med.medie_anuala, 'FM99999.90') \"Medie\" " + 
			 " FROM elev e " +
			"JOIN (" + 
			 " SELECT id_elev, id_clasa, AVG(med_sem) medie_anuala " + 
			 " FROM medie " +
			 " GROUP BY id_elev, id_clasa " + 
			") med ON e.id_elev = med.id_elev " + 
			"JOIN clasa c ON c.id_clasa = med.id_clasa " + 
			"JOIN profil p ON p.id_profil = c.id_profil " + 
			"WHERE c.an_scolar = ? " + 
			"ORDER BY med.medie_anuala DESC " + 
			") WHERE ROWNUM <= ? ";

		PreparedStatement ps = null;
		ResultSet rs = null;
        ResultSetMetaData rsmd = null;	
		Vector <String> columns = null;
		Vector <Vector<Object>> values = null;
		Vector <Object> row = null;
		int tip_bursa = app_view.getBursaID ();
		long start = 0;
		long stop = 0;
		int m = 0;
		int i;
		int nr_burse = -1;
		int an_scolar = -1;

		if (1 == tip_bursa) {
			try {
            	ps = con.getPreparedStatement (preparedQuery);

				nr_burse = app_view.getNrBurse ();
				an_scolar = app_view.getAnScolarBursa ();

				ps.setInt (1, an_scolar);
				ps.setInt (2, nr_burse);

				start = System.currentTimeMillis ();
           		rs = ps.executeQuery ();
				stop = System.currentTimeMillis ();	
				stop -= start;		
				app_view.updateStatus ("query took: " + 
						(stop / 1000) + "." + 
						(stop % 1000) + " s", AppView.SUCCESS);		

           		rsmd = rs.getMetaData ();
            	m = rsmd.getColumnCount ();
				columns = new Vector <String> (m);

           		for (i = 1; i <= m; ++ i) {
                	columns.add (rsmd.getColumnLabel (i));
            	}

				values = new Vector <> ();	

            	while (rs.next ()) {
					row = new Vector <Object> (m);	
                	for (i = 1; i <= m; ++ i) {
                    	row.add (rs.getObject (i));
                	}
                	values.add(row);
            	}
			
        		JTable table = new JTable (values, columns);
			
    	    	table.setFillsViewportHeight (true);	
				table.setRowHeight (table.getRowHeight ());
				table.setAutoCreateRowSorter (true);
				table.setEnabled (false);
				app_view.addFilterBursaResult (table);

			} catch (SQLException sqlex) {
				app_view.displayError (sqlex.getMessage ());
				app_view.updateStatus ("--[FETCH]- fail", AppView.ERROR);	
			
			} catch (Exception ex) {
				app_view.displayError (ex.getMessage ());
				app_view.updateStatus ("--[FETCH]- fail", AppView.ERROR);		
			
			} finally {
				MyConnection.closeQuietly (ps);
				MyConnection.closeQuietly (rs);
			}  

		}
	}


	@Override	
	public void actionPerformed (ActionEvent e)
	{
		if (login_view != null) {	
			if (e.getSource () == login_view.getObs ()) 
				login ();
			
		} else if (app_view != null) {
		
			Object evtSrc = e.getSource ();
		
			if (evtSrc == app_view.getObs().get ("execute")) {
				query_btn ();		
			}
			else if (evtSrc == app_view.getObs ().get ("insert-elev") ) {
				insert_elev_btn (); 
			}
			else if (evtSrc == app_view.getObs ().get ("update-elev") ) {
				out.println ("update elev");
			}
			else if (evtSrc == app_view.getObs ().get ("delete-elev") ) {
				out.println ("delete elev");
				delete_elev_btn ();
			}
			else if (evtSrc == app_view.getObs ().get ("insert-clasa") ) {
				insert_clasa_btn ();	
			}
			else if (evtSrc == app_view.getObs ().get ("delete-clasa") ) {
				delete_clasa_btn ();
			}		
			else if (evtSrc == app_view.getObs ().get ("insert-profil")) {
				insert_profil_btn ();
			}
			else if (evtSrc == app_view.getObs ().get ("update-profil")) {
						
				out.println ("-delete-clasa");
			}
			else if (evtSrc == app_view.getObs ().get ("delete-profil")) {
				delete_profil_btn ();
			}
			else if (evtSrc == app_view.getObs ().get ("insert-materie") ) {
				insert_materie_btn ();
			}
			else if (evtSrc == app_view.getObs ().get ("delete-materie") ) {
				delete_materie_btn ();
			}
			else if (evtSrc == app_view.getObs ().get ("update-materie") ) {	
				System.out.println ("Update 000");
				update_materie_btn ();
			}	
			else if (evtSrc == app_view.getObs ().get ("bursa-filter") ) {	
				filter_bursa ();
			}
	
		}
	}


	@Override		
	public void windowActivated (WindowEvent e) 
	{
		System.out.println (
			Thread.currentThread ().getStackTrace ()[1].getMethodName ());
	}


	@Override	
	public void windowClosed (WindowEvent e) 
	{
		System.out.println (
		Thread.currentThread ().getStackTrace ()[1].getMethodName ());
	}


	@Override
	public void windowClosing (WindowEvent e) 
	{	
		String[] opt = {"YES", "NO"};

		if (showOptionDialog (null, "Are you sure you want to exit?", "PSBD", 
			DEFAULT_OPTION, WARNING_MESSAGE, null, opt, opt[1]) == YES_OPTION)
		{
			try {
				if (showConfirmDialog (null, "Commit changes ?", "Warning", 
					YES_NO_OPTION, WARNING_MESSAGE) == YES_OPTION)
				{
					con.close (true);
				} else {
					con.close (false);
				}	

				exit (0);			

			} catch (SQLException sqlex) {
				showMessageDialog (null, 
					"Error while trying to close connection: " + sqlex.getMessage (),"ERROR ON EXIT", ERROR_MESSAGE);
			}		
		}
	}


	@Override 
	public void windowDeactivated (WindowEvent e) 
	{
		System.out.println (
			Thread.currentThread ().getStackTrace ()[1].getMethodName ());
	}


	@Override
	public void windowDeiconified (WindowEvent e) 
	{
		System.out.println (
			Thread.currentThread ().getStackTrace ()[1].getMethodName ());
	}


	@Override		
	public void windowIconified (WindowEvent e) 
	{
		System.out.println (
			Thread.currentThread ().getStackTrace ()[1].getMethodName ());
	}


	@Override	
	public void windowOpened (WindowEvent e) 
	{
		System.out.println (
			Thread.currentThread ().getStackTrace ()[1].getMethodName ());
	}
}	
