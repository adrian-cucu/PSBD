import static javax.swing.JOptionPane.*;
import javax.swing.border.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

class AppView extends JFrame {	
	private final boolean DEBUG = true;

	private JTabbedPane tabbed_pane = null;

	private JPanel query_panel, info_panel, query_result_panel; 
	private JScrollPane scroll_pane;
	private JTextArea query_text;
	private JButton execute;

	private final JPanel status_panel;
	private final JLabel status_label;	

	private JMenu option_menu ;
	private JMenuItem exit;
	private JMenuBar menu_bar;

	public static final int SUCCESS = 1;
	public static final int ERROR = 0;

	AppView (String title) 
	{
		super (title);	
		setDefaultCloseOperation (EXIT_ON_CLOSE);
		setBounds (250, 150, 800, 500);	
		setResizable (true);
		setJMenuBar (menuBar ());		
		setLayout (new BorderLayout ());
	
		tabbed_pane = new JTabbedPane ();
		tabbed_pane.add ("Run query", createQueryPanel ());			
		tabbed_pane.add ("Info", createInfoPanel ());			
		
		add (tabbed_pane, BorderLayout.CENTER);
		
		status_panel = new JPanel (null);
		status_panel.setBorder (new BevelBorder (BevelBorder.LOWERED));
		add (status_panel, BorderLayout.SOUTH);

		status_panel.setLayout (new BoxLayout (status_panel, BoxLayout.X_AXIS));
		status_label = new JLabel ("...");	
		status_label.setHorizontalAlignment (SwingConstants.RIGHT);
		status_label.setFont (new Font ("Serif", Font.PLAIN, 11));
		status_panel.add (status_label);		
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
		query_panel = new JPanel (new BorderLayout ()); 	
		query_panel.setBorder (new TitledBorder (new EtchedBorder (), "Execute a query"));	
	
		query_text = new JTextArea (6, 60);
		query_text.setWrapStyleWord (true);					
		query_text.setEditable (true);
		
		JScrollPane scroll = new JScrollPane (query_text);
		scroll.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	
		execute = new JButton ("Go");				

		JPanel north_wrapper = new JPanel ();

		north_wrapper.add (scroll);
		north_wrapper.add (execute);

		query_result_panel = new JPanel ();

		query_panel.add (north_wrapper, BorderLayout.NORTH);
		query_panel.add (query_result_panel, BorderLayout.CENTER);	
  		return query_panel;
	}


	public void addQueryResult (JTable table)
	{	
		query_result_panel.removeAll ();	
		query_result_panel.revalidate ();
		query_result_panel.repaint ();
  		scroll_pane = new JScrollPane (table);
        query_result_panel.add (scroll_pane);
	}	

	public void addQueryResult (String text)
	{
		query_result_panel.removeAll ();	
		query_result_panel.revalidate ();
		query_result_panel.repaint ();
        query_result_panel.add (new JTextArea (text, 3, 40));
	}


	private JPanel createInfoPanel ()
	{
		info_panel = new JPanel ();
		info_panel.add (new JLabel ("Info panel"));
		return info_panel;
	}

	
	public String getQueryText ()
	{
		return query_text.getText ();
	}
	

	private JMenuBar menuBar ()
	{
		option_menu = new JMenu ("Options");
		option_menu.setForeground (Color.BLUE);

		exit = new JMenuItem ("Exit");

		option_menu.add (exit);		
	
		menu_bar = new JMenuBar ();
		menu_bar.add (option_menu);
		return menu_bar;
	}


	public void displayError (String errorMsg)
	{
		showMessageDialog (this, errorMsg, "Error", ERROR_MESSAGE);
	}	


	public void addExitListener (ActionListener lsnr)
	{
		exit.addActionListener (lsnr);
	} 


	public void executeAddActionListener (ActionListener lsnr) 
	{
		execute.addActionListener (lsnr);
	}

	public void windowCloseListener (WindowListener lsnr)
	{
		addWindowListener (lsnr);
	}

	public Object getObs ()
	{
		return execute;
	}	
}
