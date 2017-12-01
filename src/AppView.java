import static javax.swing.JOptionPane.*;
import javax.swing.table.*;
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
	private JButton execute, adauga_elev, adauga_clasa;

	private JButton profil_insert, profil_update, profil_delete;

	private JTable profil_table;
	private JTextField id_profil, nume_profil;
	private JButton adauga_profil;

	private JTextField nume, prenume, cnp, etnie, nationalitate, 
			an_scolar, cod, clasa;
	private JTextArea adresa;



	/*  materie */
	private JTable table_materie;
	private JTextField id_materie, nume_materie; 
	private JButton insert_materie, delete_materie, update_materie;
	/*  materie */


	private JComboBox<ProfilDataModel> profiluri = null;

	private final JPanel status_panel;
	private final JLabel status_label;	


	private HashMap <String, Object> obs = new HashMap<>();
	
	private java.util.Vector <ProfilDataModel> prof = null;

	private java.util.Vector <java.util.Vector <Object>> data = null;


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
	
		tabbed_pane = new JTabbedPane ();
		/*
		tabbed_pane.add ("Run query", createQueryPanel ());			
		tabbed_pane.add ("Adauga elev", createInfoPanel ());			
		tabbed_pane.add ("Adauga profil", createProfilPanel ());			
		tabbed_pane.add ("Adauga clasa", createClasaPanel ());			
		*/

		tabbed_pane.add ("Materie", createPanelAdaugaMaterie ());			
		
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

	private JTable createTable (AbstractTableModel tableModel)
	{
		JTable tbl = new JTable (tableModel);
		tbl.setAutoCreateRowSorter (true);
		tbl.setFont (new Font ("serif", Font.PLAIN, 11));
		tbl.setSelectionMode (0);
		tbl.setSelectionBackground (new Color (0xea, 0xee, 0xaf));
		tbl.setSelectionForeground (new Color (0x22, 0x22, 0x22));
        tbl.setRowHeight (tbl.getRowHeight () + 10);
		return tbl;
	}
	
	
	private JPanel createPanelAdaugaMaterie ()
	{
		JPanel panel = new JPanel (new GridLayout (1, 2));

	//	JPanel commands = new JPanel (new GridLayout (1, 3));
		JPanel table = new JPanel (new BorderLayout ());
	
		JPanel insertPanel = new JPanel ();	
		insertPanel.setBorder (
			new TitledBorder (new EtchedBorder (), "Insert")
		);	
	//	commands.add (insertPanel);

		panel.add (insertPanel);
		panel.add (table);
		
		insertPanel.setLayout (new BorderLayout ());
				
		JPanel north = new JPanel (new GridLayout (2, 2));

		north.add (new JLabel ("ID"));
		id_materie = new JTextField (10); 
		north.add (id_materie);

		north.add (new JLabel ("Materie"));
		nume_materie = new JTextField (10);
		north.add (nume_materie);

		insertPanel.add (north, BorderLayout.NORTH);
	

		insert_materie = new JButton ("INSERT");
		insert_materie.setEnabled (false);
	
		obs.put ("insert-materie", insert_materie);
		
		insertPanel.add (insert_materie, BorderLayout.SOUTH);		
	
		KeyListener klsnr = new KeyListener () {
			public void keyPressed (KeyEvent e) {
			}		
			public void keyReleased (KeyEvent e) {
				insert_materie.setEnabled (
					nume_materie.getText ().trim ().length () > 0
				);	
			}		
			public void keyTyped (KeyEvent e) {
			}	
		};
	
		id_materie.addKeyListener (klsnr);
		nume_materie.addKeyListener (klsnr);
		/*************************************************/

		JPanel south = new JPanel ();
	
		delete_materie = new JButton ("DELETE");
		update_materie = new JButton ("UPDATE");
		
		delete_materie.setEnabled (false);
		update_materie.setEnabled (false);
		south.add (delete_materie);
		south.add (update_materie);
	
		table.add (south, BorderLayout.SOUTH);

		AbstractTableModel tableModel = new MaterieTableModel ();
		
		tableModel.setData (con.getAllProfil ());	
					
		table_materie = createTable (tableModel);

		table_materie.getSelectionModel ().addListSelectionListener (e -> {
			
			int selected = profil_table.getSelectedRow ();
			
			if (selected != -1) {
				int s = profil_table.convertRowIndexToModel (selected);

				if (s != -1) {
					/*
					System.out.println ("hope not buggy: " + 
						profil_table.getModel ().getValueAt (s, 0));	
					*/

					System.out.println ("Selection: " + s);

					System.out.println (
						profil_table.getModel ().getValueAt (s, 1));
		
					delete.setEnabled (true);
					update.setEnabled (true);
				}
			} else {
					delete.setEnabled (false);
					update.setEnabled (false);
			}
		});

		JScrollPane scrollPane = new JScrollPane (profil_table);
		scrollPane.setVerticalScrollBarPolicy (
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
		);

		table.add (scrollPane, BorderLayout.CENTER);
		
		//////////////////////////////////////////////////////////
		JTextField searchID = new JTextField (10);			
		JTextField searchName = new JTextField (10);			

		JPanel searchInputPanel = new JPanel ();
		
		searchInputPanel.add (searchID);
		searchInputPanel.add (searchName);
		
		table.add (searchInputPanel, BorderLayout.NORTH);

        TableRowSorter <AbstractTableModel> trs =
                new TableRowSorter <> (dtm);

        profil_table.setRowSorter (trs);

        KeyListener searchKeyLsnr = new KeyListener () {
            public void keyTyped (KeyEvent e) {}
            public void keyPressed (KeyEvent e) {}
            public void keyReleased (KeyEvent e)
            {
                String id = searchID.getText ().trim ();
                String name = searchName.getText ().trim ();

                if (!(id.length () > 0 || name.length () > 0)) {
            
			        trs.setRowFilter (null);	
                } else {

                    java.util.List <RowFilter <Object, Object>> filters =
                        new java.util.ArrayList <> ();

                    if (id.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + java.util.regex.Pattern.quote (id) + ".*",  0
                            )
                        );
                    }

                    if (name.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + java.util.regex.Pattern.quote (name) + ".*",  1
                            )
                        );

                    }
                    trs.setRowFilter (RowFilter.andFilter (filters));
                }
            }
        };


        searchID.addKeyListener (searchKeyLsnr);
        searchName.addKeyListener (searchKeyLsnr);
		//////////////////////////////////////////////////////////

		panel.add (table);
		
		return panel;
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
		prof = con.getAllProfil ();
		profiluri.setModel (new DefaultComboBoxModel<> (prof));
		profiluri .setBounds (120, 20, 200, 20);

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

		DefaultTableModel dtm = ClasaDataModel.getTableModel ();

		for (ClasaDataModel o : con.getAllClase ()) {
			dtm.addRow (o.get ());
		}
					

		JTable table = new JTable (dtm);
		table.setRowHeight (table.getRowHeight () + 10);
		table.setFont (new Font ("Serif", Font.PLAIN, 20));
		table.setAutoCreateRowSorter (true);

		JScrollPane scrollPane = new JScrollPane (table);
	
	
		scrollPane.setBounds (350, 10, 435, 390);		

		clasa_panel.add (scrollPane);
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
		profiluri.setSelectedIndex (0);
		//profiluri.setModel (new DefaultComboBoxModel<> (con.getAllProfil ()));
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


	public String getProfilID ()
	{
		return id_profil.getText ().trim ();
	}

	
	public String getProfil ()
	{
		return nume_profil.getText ().trim ();
	}

	
	public String getQueryText ()
	{
		return query_text.getText ();
	}
	

	public int getProfilTableSelectedID ()
	{
 		int selected = profil_table.getSelectedRow ();
			
		// ((ProfilDataModel)profil_table.getModel ()).removeRow (4);

		if (selected != -1) {
			int s = profil_table.convertRowIndexToModel (selected);
	
			System.out.println ("SELECTED: " + s);	
	
			if (s != -1) {
				return 
					((Integer) profil_table.getModel ().getValueAt (s, 0)).intValue ();	
			}
		}
		return selected;
	}


	public void displayError (String errorMsg)
	{
		showMessageDialog (this, errorMsg, "Error", ERROR_MESSAGE);
	}	


	public void displayWarning (String warnMsg)
	{
		showMessageDialog (this, warnMsg, "Warning", WARNING_MESSAGE);
	}	


	public void addListener (ActionListener lsnr)
	{
		//execute.addActionListener (lsnr);
		//adauga_elev.addActionListener (lsnr);
		adauga_profil.addActionListener (lsnr);
		//adauga_clasa.addActionListener (lsnr);
		//exit.addActionListener (lsnr);
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
