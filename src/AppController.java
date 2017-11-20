import java.awt.event.*;
import javax.swing.*;
import java.awt.Dimension;

class AppController implements ActionListener {

	private LoginView login_view = null;
	private AppView app_view = null;
//	private DataModel model = null;	
	
	private Connection con;

	public AppController (LoginView login_view)
	{
		this.login_view = login_view;
		/*
		this.model = model;
		*/
		this.login_view.addLoginListener (this);
		this.login_view.setVisible (true);
	}  


	/*
	 *@Override
	 */	
	public void actionPerformed (ActionEvent e)
	{
		if (e.getSource () == login_view.getObs ()) {
			String host = login_view.getHost ();	
			String user = login_view.getUsername ();
			int portno = login_view.getPort ();
			String sid = login_view.getSID ();
	
			if (InputCheck.check (host) && InputCheck.check (user) && 
				InputCheck.check (login_view.getPass ()) &&
				portno > 0 && portno < (1 << 16) && InputCheck.check (sid)) {

				String url = String.format ("jdbc:oracle:thin:@%s:%d:%s", host, portno, sid);
		
				con = new Connection (url, user, login_view.getPass ());					

				if (con != null && con.isConnected ()) {

					login_view.dispose ();
					app_view = new AppView (user + "@" + host + ":" + portno + ":" + sid);

					app_view.setVisible (true);		
					app_view.executeAddActionListener (this);
				} 

			} 
			else { 
				login_view.displayError ("Invalid input field !");
			}
		}
		else if (e.getSource() == app_view.getObs()) {
			app_view.updateStatus (".....");
			String query = app_view.getQueryText ();	
			query = query.trim ().replaceAll ("\n", " ").replace (";", "");

			System.out.println ("Query to be executed: " + query);
			
			String op = query.split (" ")[0].toUpperCase();
			int rr = 0;

			switch (op) {
					case "SELECT":	
						long start, stop;
						java.sql.Statement statement = null;
						java.sql.ResultSet result_set = null;

						java.util.Vector<String> columns; 
						java.util.Vector<java.util.Vector<Object>> values;	

     					try {
            				statement = con.getConnection().createStatement ();
							start = System.currentTimeMillis ();	
            				result_set = statement.executeQuery (query);
							stop = System.currentTimeMillis ();	
							stop -= start;		
							app_view.updateStatus ("query took: " + 
								(stop / 1000) + "." + 
								(stop % 1000) + " s", AppView.SUCCESS);		

            				java.sql.ResultSetMetaData rsmd = result_set.getMetaData ();
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
       
        				} catch (java.sql.SQLException sqlex) {
            				sqlex.printStackTrace ();
							String err_msg = sqlex.getMessage ();
							err_msg = err_msg.substring (err_msg.indexOf (":") + 2);
							app_view.addQueryResult (err_msg);
							app_view.updateStatus ("query error!", AppView.ERROR);			
        				} finally {}
	
				break;
					case "INSERT":
					case "UPDATE":
					case "DELETE":	     						
							try {
            					statement = con.getConnection().createStatement ();
								start = System.currentTimeMillis ();	
            					
								rr = statement.executeUpdate (query);
								System.out.println (rr);
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
        				} 
						catch (java.sql.SQLException sqlex) {
            				sqlex.printStackTrace ();
							String err_msg = sqlex.getMessage ();
							err_msg = err_msg.substring (err_msg.indexOf (":") + 2);
							app_view.addQueryResult (err_msg);
							rr = -1;
        				} 
						finally {	
							if (rr == -1) {
								app_view.updateStatus ("query error!", AppView.ERROR);		
							}
						}
						break;
					default:
							System.out.println ("------ ERROR");
			}		

		}
			else {
				app_view.updateStatus ("query error!", AppView.ERROR);			
			}	
		}

}	
