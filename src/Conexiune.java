import static java.lang.System.*;
import java.sql.*;	
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
//import javax.sql.*;
//import oracle.jdbc.*;

class Conexiune extends JFrame {

	private static final long serialVersionUID = 1L;

	private static Connection con = null;
	private static Statement st = null;


	public static void createConnection (String url, String user, String password)
	{
		try {
				long start = currentTimeMillis ();

				Class.forName ("oracle.jdbc.driver.OracleDriver");
				Conexiune.con = DriverManager.getConnection (url, user, password);	

				long millisec_took = currentTimeMillis ();
				millisec_took -= start;

				out.printf ("Connected in \033[32m%d.%d\033[00m sec.\n",
							millisec_took / 1000, 
							millisec_took % 1000);		
		} 
		catch (SQLException e) {
			err.println ("Nu merge!");
			e.printStackTrace ();
		} 
		catch (ClassNotFoundException e) {	
			err.println ("Class not found");
		} 
		finally {
		}
	}

	public synchronized static ResultSet queryResultSet (String query)
	{

		if (Conexiune.con == null)
			return null;
		
		try {
			Conexiune.st  = con.createStatement ();
			ResultSet result = st.executeQuery (query);
			return result;
		} catch (SQLException e) {	
		
		}
		return null;
	}

		/*

		try {
				long start = currentTimeMillis ();
				Class.forName ("oracle.jdbc.driver.OracleDriver");
				con = DriverManager.getConnection (url, user, pwd);	
				long millisec_took = currentTimeMillis ();
				millisec_took -= start;

				out.printf ("Connected in \033[32m%d.%d\033[00m sec.\n",
							millisec_took / 1000, 
							millisec_took % 1000);		
	
				st = con.createStatement();		
				
				
				String query = "SELECT * FROM elev";
		
				start = currentTimeMillis ();
				ResultSet res = st.executeQuery (query);		
				millisec_took = currentTimeMillis ();
				millisec_took -= start;

				out.printf ("Query took: \033[32m%d.%d\033[00m sec.\n",
							millisec_took / 1000, 
							millisec_took % 1000);		
	
				int x = 10;
				while (res.next ()) {
					DataModel data_model = new DataModel (res);	
					JButton button = new JButton (data_model + "");
					button.addActionListener (e -> {
					});
					conex.add (button);
					x += 10;
				}
				res.close ();
		} 
		catch (SQLException e) {
			err.println ("Nu merge!");
			e.printStackTrace ();
		} 
		catch (ClassNotFoundException e) {	
			err.println ("Class not found");
		} 
		finally {
			try {
				if (st != null || con != null) 
					con.close ();	
			}
	*/

	private Conexiune () 
	{
		super ("Made by acu " + "  running on: " + getProperty ("os.name"));
		setLocation (200, 200);
		setSize (600, 600);
		setDefaultCloseOperation (EXIT_ON_CLOSE);

		setLayout (new GridLayout (20, 1));				

		/*
		JPanel contentPanel = new JPanel ();
		contentPanel.setLayout (null);		
		
		contentPanel.add (new JButton ("f,mm"));
	
		setContentPane (contentPanel);	
		*/
		setVisible (true);
	}


	public static void main (String[] args)
	{
		Conexiune conex = new Conexiune ();

		/*
		java.util.Properties pro = getProperties ();

		for (java.util.Enumeration<Object> e  = pro.keys();
				e.hasMoreElements(); )
		{
				Object o = e.nextElement ();
				out.println (o + " ==> " + pro.get (o));
		}
		*/
		/*
		for (java.util.Map.Entry <Object, Object> entry : getProperties ().entrySet ()) {
			out.println (entry.getKey () + " => " + entry.getValue ());
		}
		*/
			
		String url = String.format ("%s:%s:%s:@%s:%d:%s",
						"jdbc", "oracle", "thin",
						"localhost", 1521, "xe");	
	
		String user = "adrian";
		String pwd = "biblioteca";
	
		Conexiune.createConnection (url, user, pwd);
	
		ResultSet res = Conexiune.queryResultSet ("SELECT * FROM elev");
		
		if (res != null)
			try {
				while (res.next ()){
						DataModel dm = new DataModel (res);
						out.println (dm.getId());

						String query = "SELECT * FROM elev_clasa WHERE id_elev = " + dm.getId ();
						
						ResultSet r = Conexiune.queryResultSet (query);

						while (r.next ()){
							out.println ("a");		
						}							
				}
			} catch (SQLException e) {}
	}
}
