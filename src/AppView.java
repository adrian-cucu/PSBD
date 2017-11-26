import static javax.swing.JOptionPane.*;
import java.util.HashMap;
import javax.swing.border.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

class AppView extends JFrame {	

	static final long serialVersionUID = 0xfL;

	private JTabbedPane tabbed_pane = null;

	private JPanel query_panel, info_panel, query_result_panel, 
				profil_panel, clasa_panel; 
	private JScrollPane scroll_pane;
	private JTextArea query_text;
	private JButton execute, adauga_elev, adauga_profil, adauga_clasa;

	private JTextField nume, prenume, cnp, etnie, nationalitate, nume_profil, 
			an_scolar, cod, clasa;
	private JTextArea adresa;

	private JComboBox<ProfilDataModel> profiluri = null;

	private final JPanel status_panel;
	private final JLabel status_label;	

	private JMenu option_menu ;
	private JMenuItem exit;
	private JMenuBar menu_bar;

	private HashMap <String, Object> obs = new HashMap<>();

	private MyConnection con = null;

	public static final int SUCCESS = 1;
	public static final int ERROR = 0;	


	AppView (String title, MyConnection con) 
	{
		super (title);	

		this.con = con;

		setDefaultCloseOperation (EXIT_ON_CLOSE);
		setBounds (250, 150, 800, 500);	
		setResizable (true);
		setJMenuBar (menuBar ());		
		setLayout (new BorderLayout ());
	
		tabbed_pane = new JTabbedPane ();
		tabbed_pane.add ("Run query", createQueryPanel ());			
		tabbed_pane.add ("Adauga elev", createInfoPanel ());			
		tabbed_pane.add ("Adauga profil", createProfilPanel ());			
		tabbed_pane.add ("Adauga clasa", createClasaPanel ());			
		
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

	
	private JPanel createClasaPanel ()
	{
		clasa_panel = new JPanel ();
		clasa_panel.setBorder (
			new TitledBorder (new EtchedBorder (), "Adauga o noua clasa")
		);	
		
		clasa_panel.setLayout (null);

		JLabel lbl_clasa_profil = new JLabel ("Nume profil");
		lbl_clasa_profil.setBounds (10, 20, 100, 20);	

		profiluri = new JComboBox <> ();
		profiluri.setModel (new DefaultComboBoxModel<> (con.getAllProfil ()));
		profiluri .setBounds (120, 20, 200, 20);
	

		profiluri.addActionListener (e -> {

			System.out.println ("Actiune la combo box !!!");
			System.out.println (

				((ProfilDataModel) profiluri.getSelectedItem ()).getID () +  "------");
		});


		JLabel lbl_an_scolar = new JLabel ("An");
		lbl_an_scolar.setBounds (10, 50, 100, 20);
		an_scolar = new JTextField ();				
		an_scolar.setBounds (120, 50, 200, 20);	
	

		JLabel lbl_cod = new JLabel ("Cod");
		lbl_cod.setBounds (10, 80, 100, 20);
		cod = new JTextField ();
		cod.setBounds (120, 80, 200, 20);

		JLabel lbl_clasa = new JLabel ("clasa");
		lbl_clasa.setBounds (10, 110, 100, 20);
		clasa = new JTextField ();
		clasa.setBounds (120, 110, 200, 20);

		adauga_clasa = new JButton ("Adauga");
		adauga_clasa.setBounds (100, 150, 100, 30);

		clasa_panel.add (lbl_clasa_profil);					
		clasa_panel.add (lbl_an_scolar);					
		clasa_panel.add (lbl_cod);					
		clasa_panel.add (lbl_clasa);				
	
		clasa_panel.add (clasa);					
		clasa_panel.add (cod);					
		clasa_panel.add (an_scolar);					
		clasa_panel.add (adauga_clasa);					
	
		clasa_panel.add (profiluri);					


		obs.put ("adauga clasa", adauga_clasa);
		
		return clasa_panel;
	}


	private JPanel createProfilPanel ()
	{
		profil_panel = new JPanel ();
		profil_panel.setBorder (
			new TitledBorder (new EtchedBorder (), "Adauga un nou profil")
		);	
		
		profil_panel.setLayout (null);

		JLabel lbl_nume_profil = new JLabel ("Nume profil");
		lbl_nume_profil.setBounds (10, 20, 100, 20);	
		nume_profil = new JTextField ();
		nume_profil.setBounds (120, 20, 200, 20);

		profil_panel.add (lbl_nume_profil);					
		profil_panel.add (nume_profil);					
	
		adauga_profil = new JButton ("Adauga");
		adauga_profil.setBounds (100, 50, 100, 30);
		profil_panel.add (adauga_profil);	

		obs.put ("adauga profil", adauga_profil);
		
		return profil_panel;
	}


	private JPanel createQueryPanel ()
	{
		query_panel = new JPanel (new BorderLayout ()); 	
		query_panel.setBorder (
				new TitledBorder (
					new EtchedBorder (), "Execute a query")
		);	
	
		query_text = new JTextArea (6, 60);
		query_text.setWrapStyleWord (true);					
		query_text.setEditable (true);
		
		JScrollPane scroll = new JScrollPane (query_text);
		scroll.setVerticalScrollBarPolicy (
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
		);
	
		execute = new JButton ("Go");				
			
		obs.put ("execute", execute);

		JPanel north_wrapper = new JPanel ();

		north_wrapper.add (scroll);
		north_wrapper.add (execute);

		query_result_panel = new JPanel ();

		query_panel.add (north_wrapper, BorderLayout.NORTH);
		query_panel.add (query_result_panel, BorderLayout.CENTER);	
  		return query_panel;
	}


	public void refresh ()
	{
		profiluri.setModel (new DefaultComboBoxModel<> (con.getAllProfil ()));
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
		/* add new student */
		info_panel = new JPanel ();		
		info_panel.setBorder (
			new TitledBorder (new EtchedBorder (), "Adauga elev")
		);	
		
		info_panel.setLayout (null);

		JLabel lbl_nume = new JLabel ("Nume");
		lbl_nume.setBounds (10, 20, 100, 20);	
		nume = new JTextField ();
		nume.setBounds (120, 20, 200, 20);
		
		JLabel lbl_prenume = new JLabel ("Prenume");
		lbl_prenume.setBounds (10, 50, 100, 20);
		prenume = new JTextField ();
		prenume.setBounds (120, 50, 200, 20);
	
		JLabel lbl_cnp = new JLabel ("CNP");
		lbl_cnp.setBounds (10, 80, 100, 20);
		cnp = new JTextField ();
		cnp.setBounds (120, 80, 200, 20);
	
		JLabel lbl_etnie = new JLabel ("Etnie");
		lbl_etnie.setBounds (10, 110, 100, 20);
		etnie = new JTextField ();
		etnie.setBounds (120, 110, 200, 20);

		JLabel lbl_nationalitate = new JLabel ("Nationalitate");
		lbl_nationalitate.setBounds (10, 140, 100, 20);
		nationalitate = new JTextField ();
		nationalitate.setBounds (120, 140, 200, 20);

		JLabel lbl_adresa = new JLabel ("Adresa");
		lbl_adresa.setBounds (10, 170, 100, 20);
		adresa = new JTextArea ("", 100, 40);
		adresa.setWrapStyleWord (true);					
		adresa.setEditable (true);

		JScrollPane scroll = new JScrollPane (adresa);	
		scroll.setBounds (120, 170, 200, 100);

		adauga_elev = new JButton ("Adauga");
		adauga_elev.setBounds (90, 300, 130, 40);

		obs.put ("adauga elev", adauga_elev);

		info_panel.add (lbl_nume);	
		info_panel.add (nume);	
		info_panel.add (lbl_prenume);	
		info_panel.add (prenume);	
		info_panel.add (lbl_cnp);	
		info_panel.add (cnp);	
		info_panel.add (lbl_etnie);	
		info_panel.add (etnie);	
		info_panel.add (lbl_nationalitate);
		info_panel.add (nationalitate);
		info_panel.add (lbl_adresa);
		info_panel.add (scroll);
		info_panel.add (adauga_elev);

		return info_panel;
	}


	public String getAnScolar ()
	{
		return an_scolar.getText ().trim ();
	} 

	
	public String getCodClasa ()
	{
		return cod.getText ().trim ();
	}

	
	public String getClasa ()
	{
		return clasa.getText ().trim ();
	}

	
	public int getComboBoxSelectedID ()
	{
		return ((ProfilDataModel) profiluri.getSelectedItem ()).getID ();
	}

	
	public ElevDataModel getElevData ()
	{
		String nume = this.nume.getText ().trim ();	
		if (nume.isEmpty ()) {
			return null;
		}		

		String prenume = this.prenume.getText ().trim ();
		if (prenume.isEmpty ()) {
			return null;
		}

		String adresa = this.adresa.getText ().trim ();		
		if (adresa.isEmpty ()) {
			return null;
		}

		String cnp = this.cnp.getText ().trim ();

		if (cnp.isEmpty () || cnp.length () != 13) {
			return null;
		}
		
		String etnie = this.etnie.getText ().trim ();
		if (etnie.isEmpty ()) {
			return null;
		}

		String nationalitate = this.nationalitate.getText ().trim ();
		if (nationalitate.isEmpty ()) {
			return null;
		}

		return 
			new ElevDataModel (nume, prenume, cnp, adresa, etnie, nationalitate);		
	}

	
	public String getProfil ()
	{
		return nume_profil.getText ().trim ();
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


	public void addListener (ActionListener lsnr)
	{
		execute.addActionListener (lsnr);
		adauga_elev.addActionListener (lsnr);
		adauga_profil.addActionListener (lsnr);
		adauga_clasa.addActionListener (lsnr);
		exit.addActionListener (lsnr);
	}
	

	public void windowCloseListener (WindowListener lsnr)
	{
		addWindowListener (lsnr);
	}


	public HashMap<String, Object> getObs ()
	{
		return obs;
	}	

}
