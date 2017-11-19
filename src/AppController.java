import java.awt.event.*;
import javax.swing.*;

class AppController {

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
		this.login_view.addLoginListener (new LoginListener ());
		this.login_view.setVisible (true);
	}  

	class LoginListener implements ActionListener {
	
		public void actionPerformed (ActionEvent e)
		{
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
					con.query ("SELECT * FROM elev");

				} 

			} else {
				login_view.displayError ("Invalid input field !");
			}	
		}	
	}

}
