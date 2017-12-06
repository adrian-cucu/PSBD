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
			} 
			catch (ConnectionErrorException ce) {
				login_view.displayError (ce.getMessage ());
			}
			catch (DriverNotFoundException dnfe) {
				login_view.displayError (dnfe.getMessage ());
			} 			
		} 
		else { 
			login_view.displayError ("Invalid input field !");
		}
	}


	private void query_btn ()
	{
		String query = app_view.getQueryText ();	
		query = query.trim ().replaceAll ("\n", " ").replace (";", "");
			
		String op = query.split (" ")[0].toUpperCase();
		int rr = 0;

		out.println (query);

		switch (op) {
			case "SELECT":	
				long start, stop;
				Statement statement = null;
				ResultSet result_set = null;

				Vector <String> columns; 
				Vector <Vector<Object>> values;	

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
            		int m = rsmd.getColumnCount ();
					columns = new java.util.Vector<>(m);

           			for (int i = 1; i <= m; ++ i) {
                		columns.add (rsmd.getColumnLabel (i));
            		}

					values = new java.util.Vector<>();	

            		while (result_set.next ()) {
						Vector <Object> row_values = new Vector <>(m);	
                		for (int i = 1; i <= m; ++ i) {
                    		row_values.add (result_set.getObject (i));
                		}
                		values.add(row_values);
            		}
			
        			JTable table = new JTable (values, columns);
					table.setPreferredScrollableViewportSize (
							new Dimension (700, 130)
					);
    	    		table.setFillsViewportHeight (true);	
	 		   		app_view.addQueryResult (table);

            		if (statement != null) 
                		statement.close ();
       
        		} catch (SQLException sqlex) {
					app_view.addQueryResult (sqlex.getMessage ());
					app_view.updateStatus ("query error!", AppView.ERROR);
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
					} 
					else if (op.equals ("UPDATE")) {
						app_view.addQueryResult (rr + " rows updated.");
					}
					else {
						app_view.addQueryResult (rr + " rows deleted.");
					}
								
            		if (statement != null) {
                		statement.close ();
					}

					app_view.refresh ();
        		} 
				catch (SQLException sqlex) {
            		sqlex.printStackTrace ();
					String err_msg = sqlex.getMessage ();
					err_msg = err_msg.substring (err_msg.indexOf (":") + 2);
					app_view.addQueryResult (err_msg);
					app_view.updateStatus ("query error!", AppView.ERROR);
        		} 
				break;
			default:
					app_view.addQueryResult ("Unknown statement");	
				app_view.updateStatus ("Error!", AppView.ERROR);
			}		
	}

	
	private void insert_elev_btn ()
	{
		String prepared = 
			"INSERT INTO elev (nume, prenume, adresa, cnp, etnie, nationalitate) VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement ps = null; 

		ElevDataModel p = app_view.getElevData ();

		if (p == null) {
			app_view.displayError ("Data entered cannot be inserted!");
			app_view.updateStatus ("-[INSERT]- fail", AppView.SUCCESS);
		}				

		try {
			ps = con.getPreparedStatement (prepared);
			
			ps.setString (1, p.getNume ());	
			ps.setString (2, p.getPren ());	
			ps.setString (3, p.getCnp ());	
			ps.setString (4, p.getAdresa ());	
			ps.setString (5, p.getEtnie ());	
			ps.setString (6, p.getNat ());
						
			ps.executeUpdate ();
			
			app_view.displayInformation (ps.getUpdateCount () + " rows inserted.");
			app_view.updateStatus ("-[INSERT]- success", AppView.SUCCESS);

		} catch (SQLException sqlex) {	
			app_view.displayError (sqlex.getMessage ());
			app_view.updateStatus ("-[INSERT]- fail", AppView.SUCCESS);
		
		} finally {
			closeQuietly (ps);
		}		
	}
	
	
	private void insert_profil_btn ()
	{		
		Vector <Vector <MaterieDataModel>> m = app_view.getMateriiIDs ();
		String prepared = "INSERT INTO profil (nume_profil) VALUES (?)";
		String prepared2 = 
			"INSERT INTO profil_materie (id_profil, id_materie, an_clasa) VALUES (?, ?, ?)";
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet generatedKeys = null;

		try {
			ps = 
				con.getPreparedStatement (prepared, new String[]{"id_profil"}); 	
			String profil = app_view.getProfil ();

			ps.setString (1, profil);
			ps.executeUpdate ();

			generatedKeys = ps.getGeneratedKeys ();
			if (generatedKeys.next ()) {
				int id = Integer.parseInt (generatedKeys.getString (1));
				
				ps2 = con.getPreparedStatement (prepared2);	

				int clasa = 9;
				for (Vector <MaterieDataModel> materii : m)
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
				app_view.re ();
	
			} else {	
				throw new SQLException ("Failed to insert");
			}		

			app_view.displayInformation (ps.getUpdateCount () + " rows inserted.");
			app_view.updateStatus ("-[INSERT]- success", AppView.SUCCESS);

		} catch (SQLException sqlex) {	
			app_view.displayError (sqlex.getMessage ());
			app_view.updateStatus ("-[INSERT]- fail", AppView.SUCCESS);
		
		} finally {
			closeQuietly (ps);
			closeQuietly (ps2);
			closeQuietly (generatedKeys);
		}	
	}


	private void insert_clasa_btn ()
	{
		String prepared = 
		"INSERT INTO clasa(id_profil, an_scolar, cod, clasa) VALUES (?, ?, ?, ?)";

		PreparedStatement ps = null; 	
		
		try {
			int an_scolar = Integer.parseInt (app_view.getAnScolar ());	
			String codClasa = app_view.getCodClasa ();
			int clasa = Integer.parseInt (app_view.getClasa ());		
			int id_profil = app_view.getComboBoxSelectedID ();

			System.out.println ("ID profil: " + id_profil);
			System.out.println ("Cod clasa: " + codClasa);
			System.out.println ("Clasa: " + clasa);
			System.out.println ("an scolar: " + an_scolar);
				
			ps = con.getPreparedStatement (prepared); 	

			ps.setInt (1, id_profil);
			ps.setInt (2, an_scolar);
			ps.setString (3, codClasa);
			ps.setInt (4, clasa);
			
			ps.executeUpdate ();
		
			app_view.displayInformation (ps.getUpdateCount () + " rows inserted.");
			app_view.updateStatus ("-[INSERT]- success", AppView.SUCCESS);

		} catch (SQLException sqlex) {	
			app_view.displayError (sqlex.getMessage ());
			app_view.updateStatus ("-[INSERT]- fail", AppView.SUCCESS);
		
		} finally {
			closeQuietly (ps);
		}
	}


	private void insert_materie_btn ()
	{
		String prepared = "INSERT INTO materie(nume_materie) VALUES (?)";
		PreparedStatement ps = null;

		try {
			ps = con.getPreparedStatement (prepared);

			int id = -1;
			String materie;
								
			materie = app_view.getNumeMaterie ();	
			ps.setString (1, materie);
			
			ps.executeUpdate ();
			MaterieTableModel.refresh (con);
			app_view.re ();
	
			app_view.displayInformation (ps.getUpdateCount () + " rows inserted.");
			app_view.updateStatus ("-[INSERT]- success", AppView.SUCCESS);

		} catch (SQLException sqlex) {
			app_view.displayError (sqlex.getMessage ());
			app_view.updateStatus ("-[INSERT]- fail", AppView.SUCCESS);

		} finally {
			closeQuietly (ps);
		}
	}


	private void delete_materie_btn ()
	{
		String prepared = "DELETE FROM materie WHERE id_materie = ?";
		String prepared2 = "DELETE FROM profil_materie WHERE id_materie = ?";
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;

		try {
			ps = con.getPreparedStatement (prepared); 
			ps2 = con.getPreparedStatement (prepared2);									
			for (int id : app_view.getTableMaterieSelectedIDs (true)) {
				ps.setInt (1, id);
				ps.addBatch ();
		
				ps2.setInt (1, id);
				ps2.addBatch ();
			}

			ps2.executeBatch ();
			ps.executeBatch ();

			MaterieTableModel.refresh (con);
	
			app_view.re ();

			app_view.displayInformation (ps.getUpdateCount () + " rows deleted.");
			app_view.updateStatus ("-[DELETE]- success", AppView.SUCCESS);

		} catch (SQLException sqlex) {
			app_view.displayError (sqlex.getMessage ());
			app_view.updateStatus ("-[DELETE]- success", AppView.SUCCESS);

		} finally {
			closeQuietly (ps2);
			closeQuietly (ps);
		}
	}


	private void update_materie_btn ()
	{
	}


	private void delete_profil_btn ()
	{
		String prepared = "DELETE FROM profil WHERE id_profil = ?";
		String prepared2 = "DELETE FROM profil_materie WHERE id_profil = ?";
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;

		try {
			ps = con.getPreparedStatement (prepared); 		
			ps2 = con.getPreparedStatement (prepared2); 		

			for (int id : app_view.getTableProfilSelectedIDs (true)) {
				ps.setInt (1, id);
				ps.addBatch ();
			
				ps2.setInt (1, id);
				ps2.addBatch ();
			}
		
			ps2.executeBatch ();
			ps.executeBatch ();

			ProfilTableModel.refresh (con);
	
			app_view.re ();

			app_view.displayInformation (ps.getUpdateCount () + " rows deleted.");
			app_view.updateStatus ("-[DELETE]- success", AppView.SUCCESS);

		} catch (SQLException sqlex) {
			app_view.displayError (sqlex.getMessage ());
			app_view.updateStatus ("-[DELETE]- success", AppView.SUCCESS);

		} finally {
			closeQuietly (ps);
		} 
	}

	private void update_profil_btn ()
	{
		out.println ("UPDATE PROFIL");
	}


	private void closeQuietly (Statement statement)
	{
		try {
			if (statement != null) {
				statement.close ();
			}
		} catch (SQLException sqlException) {
			/* quiet */
		}
	}


	private void closeQuietly (ResultSet resultSet)
	{
		try {
			if (resultSet != null) {
				resultSet.close ();
			}
		} catch (SQLException sqlException) {
			/* quiet */
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
			else if (evtSrc == app_view.getObs ().get ("adauga elev") ) {
				insert_elev_btn ();
			}
			else if (evtSrc == app_view.getObs ().get ("adauga clasa") ) {
				insert_clasa_btn ();	
			}
			else if (evtSrc == app_view.getObs ().get ("insert-profil")) {
				insert_profil_btn ();
			}
			else if (evtSrc == app_view.getObs ().get ("update-profil")) {
				update_profil_btn ();
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
				update_materie_btn ();
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
