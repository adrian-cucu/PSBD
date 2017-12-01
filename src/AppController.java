import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

class AppController 
	implements ActionListener, WindowListener {

	private LoginView login_view = null;
	private AppView app_view = null;
	private MyConnection con = null;


	public AppController (LoginView login_view)
	{		
		this.login_view = login_view;
		this.login_view.addLoginListener (this);
		this.login_view.setVisible (true);
	}  


	/*
	 *@Override
	 */	
	public void actionPerformed (ActionEvent e)
	{
		if (login_view != null && e.getSource () == login_view.getObs ()) {
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
		else if (app_view != null && 
				e.getSource() == app_view.getObs().get ("execute")) {	
	
			String query = app_view.getQueryText ();	
			query = query.trim ().replaceAll ("\n", " ").replace (";", "");
			
			String op = query.split (" ")[0].toUpperCase();
			int rr = 0;

			switch (op) {
				case "SELECT":	
					long start, stop;
					Statement statement = null;
					ResultSet result_set = null;

					java.util.Vector<String> columns; 
					java.util.Vector<java.util.Vector<Object>> values;	

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
							java.util.Vector<Object> row_values = new java.util.Vector<>(m);	
                			for (int i = 1; i <= m; ++ i) {
                    			row_values.add (result_set.getObject (i));
                			}
                			values.add(row_values);
            			}
			
        				JTable table = new JTable (values, columns);
						table.setPreferredScrollableViewportSize (new Dimension (700, 130));
    	    			table.setFillsViewportHeight (true);	
	 		   			app_view.addQueryResult (table);

            			if (statement != null) 
                			statement.close ();
       
        			} catch (SQLException sqlex) {
            			sqlex.printStackTrace ();
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
		else if (app_view != null &&
					e.getSource () == app_view.getObs ().get ("adauga elev") ) {
	
			ElevDataModel p = app_view.getElevData ();
			if (p != null) {
				System.out.println (p);	
				
				try {	
	
					String prepared = "INSERT INTO elev (nume, prenume, adresa, cnp, etnie, nationalitate) VALUES (?, ?, ?, ?, ?, ?)";

					PreparedStatement ps = con.getPreparedStatement (prepared); 

					ps.setString (1, p.getNume ());	
					ps.setString (2, p.getPren ());	
					ps.setString (3, p.getCnp ());	
					ps.setString (4, p.getAdresa ());	
					ps.setString (5, p.getEtnie ());	
					ps.setString (6, p.getNat ());	
	
					int r = ps.executeUpdate ();										
					if (r != -1) {
						app_view.updateStatus ("Elevul a fost adaugat.", 
									AppView.SUCCESS);
						app_view.refresh ();
					}	

				} 
				catch (SQLException sqlex) {
					app_view.displayError (sqlex.getMessage ());
				}

			} else {
				app_view.displayError ("Cannot insert data!");
			}		
		}
		else if (app_view != null &&
				e.getSource () == app_view.getObs ().get ("adauga profil") ) {	

			try {
	
				String prepared = "INSERT INTO profil (nume_profil) VALUES (?)";
 	
				PreparedStatement ps = con.getPreparedStatement (prepared); 	

				String profil = app_view.getProfil ();

				if (profil.isEmpty () ) {
					throw new java.sql.SQLException ("Campul nu poate fi gol");
				}

				ps.setString (1, profil);
				ps.executeUpdate ();

				app_view.refresh ();
				app_view.updateStatus ("Profilul nou a fost adaugat.", AppView.SUCCESS);
			}
			catch (java.sql.SQLException sqlex) {	
				app_view.displayError (sqlex.getMessage ());
			}	
		} else if (app_view != null &&
					e.getSource () == app_view.getObs ().get ("adauga clasa") ) {
	
			
			System.out.println ("Adauga clasa");
		
			try {
				int an_scolar = Integer.parseInt (app_view.getAnScolar ());	
				String codClasa = app_view.getCodClasa ();
				int clasa = Integer.parseInt (app_view.getClasa ());		
				int id_profil = app_view.getComboBoxSelectedID ();

				System.out.println ("ID profil: " + id_profil);
				System.out.println ("Cod clasa: " + codClasa);
				System.out.println ("Clasa: " + clasa);
				System.out.println ("an scolar: " + an_scolar);
				
				String prepared = 
					"INSERT INTO clasa(id_profil, an_scolar, cod, clasa) VALUES (?, ?, ?, ?)";

				PreparedStatement ps = con.getPreparedStatement (prepared); 	

				ps.setInt (1, id_profil);
				ps.setInt (2, an_scolar);
				ps.setString (3, codClasa);
				ps.setInt (4, clasa);
			
				ps.executeUpdate ();
				app_view.updateStatus ("Clasa a fost adaugata.", AppView.SUCCESS);

			} 
			catch (SQLException sqlex) {
				app_view.displayError (sqlex.getMessage ());
			}
			catch (Exception ex) {
				app_view.displayError (ex.getMessage ());
			}	
		}
		else if (app_view != null && 
			e.getSource () == app_view.getObs ().get ("profil insert") ) {
			
			String ID = app_view.getProfilID ();
			String nume_profil = app_view.getProfil ();

			if (ID.length () > 0 ) {

				System.out.println ("Profil insert: " + ID + " " + nume_profil);
			} else {
				System.out.println ("Profil insert: AUTO INC ID " + nume_profil);
			}
				
				 


		}
		else if (app_view != null && 
			e.getSource () == app_view.getObs ().get ("profil delete") ) {
				
			int ID = app_view.getProfilTableSelectedID ();
			
			System.out.println ("Profil delete " + ID);

			try {
				String prepared = 
					"DELETE FROM profil WHERE id_profil = ?";

				PreparedStatement ps = con.getPreparedStatement (prepared); 	

				ps.setInt (1, ID);
			
				ps.executeUpdate ();
				app_view.updateStatus ("Profilul a fost sters", AppView.SUCCESS);

			} 
			catch (SQLException sqlex) {
				app_view.displayWarning (sqlex.getMessage ());
			}
			catch (Exception ex) {
				app_view.displayError (ex.getMessage ());
			}	

		}
		else if (app_view != null && 
			e.getSource () == app_view.getObs ().get ("profil update") ) {
				
			System.out.println ("Profil update");
			
		}
		else {
			System.exit (0);
		}
	}


	/*
	 *@Override
	 */			
	public void windowActivated (WindowEvent e) 
	{
		System.out.println (
			Thread.currentThread ().getStackTrace ()[1].getMethodName ());
	}


	/*
	 *@Override
	 */	
	public void windowClosed (WindowEvent e) 
	{
		System.out.println (
		Thread.currentThread ().getStackTrace ()[1].getMethodName ());
	}


	/*
	 *@Override
	 */	
	public void windowClosing (WindowEvent e) 
	{
		System.out.println (
			Thread.currentThread ().getStackTrace ()[1].getMethodName ());

		try {
			int x = 
			JOptionPane.showConfirmDialog (null, "Commit changes ?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			
			if (x == 0) {
				con.close (true);
				System.out.println ("Connection succefully closed! [COMMIT] :)");
			}
			else {
				con.close (false);
				System.out.println ("Connection succefully closed! [ROLLBACK] :)");
			}
		}
		catch (java.sql.SQLException sqlex) {	
			System.out.println ("Connection wasn't closed closed! :<");
		}		
	}


	/*
	 *@Override
	 */	
	public void windowDeactivated (WindowEvent e) 
	{
		System.out.println (
			Thread.currentThread ().getStackTrace ()[1].getMethodName ());
	}


	/*
	 *@Override
	 */	
	public void windowDeiconified (WindowEvent e) 
	{
		System.out.println (
			Thread.currentThread ().getStackTrace ()[1].getMethodName ());
	}


	/*
	 *@Override
	 */		
	public void windowIconified (WindowEvent e) 
	{
		System.out.println (
			Thread.currentThread ().getStackTrace ()[1].getMethodName ());
	}


	/*
	 *@Override
	 */	
	public void windowOpened (WindowEvent e) 
	{
		System.out.println (
			Thread.currentThread ().getStackTrace ()[1].getMethodName ());
	}

}	
