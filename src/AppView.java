import java.awt.event.*;
import static javax.swing.JOptionPane.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

class AppView extends JFrame {	
	private final boolean DEBUG = true;

	private JTabbedPane tabbed_pane = null;

	private JPanel query_panel, info_panel; 

	private final JPanel status_panel;
	private final JLabel status_label;	

	public static final int SUCCESS = 1;
	public static final int ERROR = 0;

	AppView (String title) 
	{
		super (title);	
		setDefaultCloseOperation (EXIT_ON_CLOSE);
		setBounds (250, 150, 800, 500);	
		setResizable (true);
		setTheme ();		
		setJMenuBar (menuBar ());		
		setLayout (new BorderLayout ());

	
		tabbed_pane = new JTabbedPane ();
		tabbed_pane.add ("Run query", createQueryPanel ());			
		tabbed_pane.add ("Info", createInfoPanel ());			
		
		add (tabbed_pane, BorderLayout.CENTER);
		

		status_panel = new JPanel ();
		status_panel.setBorder (new BevelBorder (BevelBorder.LOWERED));
		add (status_panel, BorderLayout.SOUTH);

		status_panel.setLayout (new BoxLayout (status_panel, BoxLayout.X_AXIS));
		status_label = new JLabel (" -- ");	
		status_label.setHorizontalAlignment (SwingConstants.LEFT);
		status_panel.add (status_label);

		/*		
		JPanel panel = new JPanel ();

		String[] column = {
			"First name", "Last name", "Sport", "# of years", "Vegeta"
		};	
		
		Object[][] data = {
			{"Adrian", "Cucu", "Laba", "21 + ", "true"},
			{"Adrian", "Cucu", "Laba", "21 + ", "true"},
			{"Adrian", "Cucu", "Laba", "21 + ", "true"},
			{"Adrian", "Cucu", "Laba", "21 + ", "true"},
			{"Adrian", "Cucu", "Laba", "21 + ", "true"},
			{"Adrian", "Cucu", "Laba", "21 + ", "true"},
			{"Adrian", "Cucu", "Laba", "21 + ", "true"},
			{"Adrian", "Cucu", "Laba", "21 + ", "true"},
			{"Adrian", "Cucu", "Laba", "21 + ", "true"},
			{"Adrian", "Cucu", "Laba", "21 + ", "true"}
		};		

		final JTable table = new JTable (data, column);
//		table.setPreferredScrollableViewportSize (new Dimension (500, 70));
		table.setFillsViewportHeight (true);
		
		java.util.Vector <Integer> v = new java.util.Vector<>();

		for (int i=1; i<100; ++i)
			v.add (i);
		
		for (int i : v)		
			System.out.println (i);	
		

		if (DEBUG) {
			table.addMouseListener (new MouseAdapter () {
				public void mouseClicked (MouseEvent e) {
					System.out.println (table.getSelectedRow ());

					//printDebugData (table);
				}
			});
		}

		JScrollPane scroll_pane = new JScrollPane (table);

		panel.add (scroll_pane);
		
		setLayout (new BorderLayout ());
		add (panel, BorderLayout.CENTER);	
		*/
	}	
	
	public void updateStatus (String status, int sts)
	{
		switch (sts) {
			case AppView.SUCCESS:
					status_label.setForeground (new Color (0x05, 0x63, 0x0c));		
				break;
			case AppView.ERROR:
					status_label.setForeground (new Color (0xdf, 0x06, 0x06));
				break;
			default:
					status_label.setForeground (Color.BLACK);		
		}
		status_label.setText (status);
	}

	public void updateStatus (String status)
	{
		status_label.setForeground (Color.BLACK);		
		status_label.setText (status);
	}

	private JPanel createQueryPanel ()
	{
		query_panel = new JPanel ();
		/*
		JButton b1 = new JButton ("SUCCESS");
		JButton b2 = new JButton ("FAILD");
		JButton b3 = new JButton ("NORMAL");

		b1.addActionListener (e -> { updateStatus ("heeeeey", AppView.ERROR);});
		b2.addActionListener (e -> { updateStatus ("heeeeey", AppView.SUCCESS);});
		b3.addActionListener (e -> { updateStatus ("heeeeey");});
		*/

		query_panel.add (b1);
		query_panel.add (b2);
		query_panel.add (b3);
		return query_panel;
	}

	private JPanel createInfoPanel ()
	{
		info_panel = new JPanel ();
		info_panel.add (new JLabel ("Info panel"));
		return info_panel;
	}
	
	
	private void printDebugData (JTable table)
	{
		int n = table.getRowCount ();
		int m = table.getColumnCount ();
		javax.swing.table.TableModel model = 
			table.getModel ();

		System.out.println ("Value of data: ");

		for (int i=0; i<n; ++i) {
			System.out.println (" row " + i + ":");
			for (int j=0; j<m; ++j) {
				System.out.println (" " + model.getValueAt (i, j));
			}
			System.out.println ();
		}
		System.out.println ("--------------------------");
			

	}

    private void setTheme ()
    {
        setDefaultLookAndFeelDecorated (true);
        try {
           UIManager.setLookAndFeel ("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch (Exception e) {
        }
    }


	private JMenuBar menuBar ()
	{
		JMenu option_menu = new JMenu ("Options");
		option_menu.setForeground (Color.BLUE);

		JMenuItem exit = new JMenuItem ("Exit");
		exit.setIcon (new ImageIcon ("../img/close.jpg"));			
		exit.addActionListener (e -> {
			System.exit (0);
		});
		option_menu.add (exit);		
	
		JMenuBar menu_bar = new JMenuBar ();
		menu_bar.add (option_menu);
		return menu_bar;
	}

	/*
	private Image getImage (String path)
	{
		return Toolkit.getDefaultTolkit ()
					  .getImage (AppView.class.getResource (path));
	}
	*/

	public void displayError (String errorMsg)
	{
		showMessageDialog (this, errorMsg, "Error", ERROR_MESSAGE);
	}	
	
	/*	
	public void addLoginListener (ActionListener loginListener)
	{
		login.addActionListener (loginListener);
	} 

	public void addExitListener (ActionListener loginListener)
	{
		login.addActionListener (loginListener);
	} 	
	*/
}
