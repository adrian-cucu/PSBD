import java.awt.event.*;
import static javax.swing.JOptionPane.*;
import javax.swing.*;
import java.awt.*;

class LoginView extends JFrame {	
	private JTextField hostname, username, port, sid, password;
	private JButton login, reset, exit;
	private JLabel status;

	LoginView () 
	{
		super ("Oracle login -- Login view");	
		setDefaultCloseOperation (EXIT_ON_CLOSE);
		setBounds (450, 250, 450, 300);	
		setResizable (false);
		
		getContentPane ().setLayout (null);
	
		JLabel hostname_label = new JLabel ("Hostname: ");  
		hostname = new JTextField (30);
		hostname_label.setBounds (50, 30, 100, 20);
		hostname.setBounds (150, 30, 210, 20);		
		
		JLabel username_label = new JLabel ("Username: ");  
		username = new JTextField (30);
		username_label.setBounds (50, 60, 100, 20);
		username.setBounds (150, 60, 210, 20);

		JLabel password_label = new JLabel ("Password: "); 
		password = new JPasswordField (30);
		password_label.setBounds (50, 90, 100, 20);
		password.setBounds (150, 90, 210, 20);

		JLabel sid_label = new JLabel ("SID");
		sid = new JTextField ("XE", 7);
		sid_label.setBounds (50, 120, 100, 20);
		sid.setBounds (150, 120, 70, 20);

		JLabel port_label = new JLabel ("PORT");
		port = new JTextField ("1521", 7);
		port_label.setBounds (230, 120, 100, 20);
		port.setBounds (290, 120, 70, 20);
		
		login = new JButton ("Login");	
		login.setBounds (50, 200, 80, 30);

		reset = new JButton ("Reset");	
		reset.setBounds (170, 200, 80, 30);

		reset.addActionListener (new ActionListener () {
			public void actionPerformed (ActionEvent e)
			{
				hostname.setText ("");
				username.setText ("");
				password.setText ("");
			}
		});

		exit = new JButton ("Exit");	
		exit.setBounds (290, 200, 80, 30);	
		exit.addActionListener (new ActionListener () {
			public void actionPerformed (ActionEvent e)
			{
				dispose ();
				System.exit (0);	
			}
		});


	
		JSeparator sep = new JSeparator();
		sep.setBounds (50, 170, 360, 22);
	
		status = new JLabel ("Not connected");	
		status.setBounds (0, 0, 150, 10);

		add (hostname_label);
		add (hostname);
		add (username_label);
		add (username);
		add (password_label);
		add (password);
		add (sid_label);
		add (sid);
		add (port_label);
		add (port);		
		add (reset);
		add (login);
		add (exit);

		add (sep);
		add (status);
		setTheme ();
	}	

 	private void setTheme () 
    {
		setDefaultLookAndFeelDecorated (true);
        try {
            

           //UIManager.setLookAndFeel ("javax.swing.plaf.metal.MetalLookAndFeel");
    //       UIManager.setLookAndFeel ("com.sun.java.swing.plaf.motif.MotifLookAndFeel");

           UIManager.setLookAndFeel ("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            //UIManager.setLookAndFeel (UIManager.getCrossPlatformLookAndFeelClassName ());
        }
        catch (Exception e) {
        }

    }


	public String getHost ()
	{
		return hostname.getText ().trim ();
	}

	public String getUsername ()
	{
		return username.getText ().trim ();
	}

	public String getSID ()
	{
		return sid.getText ().trim ();
	}

	public int getPort ()
	{
		return Integer.parseInt (port.getText ().trim ());		
	}

	public String getPass ()
	{
		return password.getText ().trim ();
	}	

	public void displayError (String errorMsg)
	{
		showMessageDialog (this, errorMsg, "Error", ERROR_MESSAGE);
	}	
	
	public void addLoginListener (ActionListener loginListener)
	{
		login.addActionListener (loginListener);
	}  	
}
