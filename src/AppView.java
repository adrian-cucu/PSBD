import static javax.swing.JOptionPane.*;
import java.util.Vector;
import javax.swing.table.*;
import java.util.HashMap;
import javax.swing.border.*;
import javax.swing.JList;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.ListDataListener;
import javax.swing.ListSelectionModel;

/*
	TODO de rezolvat problema la jlist model
*/

class AppView extends JFrame {	

	static final long serialVersionUID = 0xad1;

	private JTabbedPane tabbed_pane = null;

	private JPanel query_panel, info_panel, query_result_panel, 
				profil_panel, clasa_panel; 

	private JScrollPane scroll_pane;
	private JTextArea query_text;
	private JButton execute, adauga_elev, adauga_clasa;


	private JTextField nume, prenume, cnp, etnie, nationalitate, 
			an_scolar, cod, clasa;
	private JTextArea adresa;

	/* materie */
	private JTable table_materie;
	private JTextField nume_materie; 
	private JButton insert_materie, delete_materie, update_materie;
	private MaterieTableModel materie_table_model;
	private DefaultListModel <MaterieDataModel> lista_materii; 
	private Vector <DefaultListModel <MaterieDataModel>> materii_an_scolar; 
	/* materie */


	/* profil */
	private	JTextField nume_profil; 
	private JTable table_profil;
	private ProfilTableModel profil_table_model;	
	private JButton insert_profil, delete_profil, update_profil;	
	/* profil */


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

		setDefaultCloseOperation (DO_NOTHING_ON_CLOSE);
		setBounds (250, 150, 800, 500);	
		setResizable (true);
	
		tabbed_pane = new JTabbedPane ();
	
		tabbed_pane.add ("Run query", createQueryPanel ());			
		/*
		tabbed_pane.add ("Adauga elev", createInfoPanel ());			
		tabbed_pane.add ("Adauga profil", createProfilPanel ());			
		tabbed_pane.add ("Adauga clasa", createClasaPanel ());			
		*/

		tabbed_pane.add ("Materie", createPanelAdaugaMaterie ());			
		tabbed_pane.add ("Profil", createPanelAdaugaProfil ());			
		
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
		boolean multipleSelection = true;	
		JTable tbl = new JTable (tableModel);
		tbl.setAutoCreateRowSorter (true);
		tbl.setFont (new Font ("serif", Font.PLAIN, 11));
		if (!multipleSelection) {
			tbl.setSelectionMode (0);
		}
		tbl.setSelectionBackground (new Color (0xea, 0xee, 0xaf));
		tbl.setSelectionForeground (new Color (0x22, 0x22, 0x22));
        tbl.setRowHeight (tbl.getRowHeight () + 10);
		return tbl;
	}
	
	
	private JPanel createPanelAdaugaProfil ()
	{
		JPanel panel = new JPanel (new GridLayout (1, 2));
		panel.setBorder (
			new TitledBorder (new EtchedBorder (), "Insert")
		);	
	
		JList <MaterieDataModel> test = new JList <> (lista_materii);		

		test.setSelectionMode (ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		JPanel insertPanel = new JPanel (new BorderLayout ());

		panel.add (insertPanel);		
		
		ProfilTableModel profilModel = new ProfilTableModel ();
		profilModel.setData (con.fetchProfil ());

		JPanel panel00 = new JPanel (new BorderLayout ());

		JPanel panel01 = new JPanel ();
		delete_profil =  new JButton ("DELETE");
		delete_profil.setEnabled (false);
		update_profil = new JButton ("UPDATE");	
		update_profil.setEnabled (false);

		obs.put ("delete-profil", delete_profil);
		obs.put ("update-profil", update_profil);

		panel01.add (delete_profil);
		panel01.add (update_profil);

		profil_table_model = new ProfilTableModel ();
		table_profil = createTable (profil_table_model);
		table_profil.getSelectionModel ().addListSelectionListener (e -> {
			if (table_profil.getSelectedRow () != -1) {
				delete_profil.setEnabled (true);
				update_profil.setEnabled (true);
			} else {
				delete_profil.setEnabled (false);
				update_profil.setEnabled (false);
			}
		});
		
		panel00.add (new JScrollPane (table_profil), BorderLayout.CENTER);
		panel00.add (panel01, BorderLayout.SOUTH);
		panel.add (panel00);

		JPanel pnl = new JPanel (new GridLayout (2, 2));		

		insertPanel.add (pnl, BorderLayout.NORTH);		
		
		nume_profil = new JTextField (10);
		
		pnl.add (new JLabel ("Profil"));
		pnl.add (nume_profil);
		
		JPanel cards = new JPanel (new CardLayout ());

		Vector <JList <MaterieDataModel>> lists = new Vector <>();
		materii_an_scolar = new Vector <> ();	
		JPanel[] panels = new JPanel [4];

		for (int i = 0; i < 4; ++ i) {
			panels [i] = new JPanel (new GridLayout (1, 2));
			DefaultListModel <MaterieDataModel> model =
				new DefaultListModel <MaterieDataModel> ();
			/*  {
					public static final long serialVersionUID = 0xad1;
					@Override
					public MaterieDataModel getElementAt (int index) {
						return materii_an_scolar.get (index);
					}
					@Override
					public int getSize () {
						return materii_an_scolar.getRowCount ();
					}
				};
			*/	
			materii_an_scolar.add (model);
			
			JList <MaterieDataModel> list = new JList <> (model);
			lists.add (list);
			panels [i].add (new JScrollPane (lists.get (i)));
			JPanel buttonsPanel = new JPanel (new GridLayout (3, 1));

			JButton push = new JButton ("add");
			JButton pop = new JButton ("delete");
			JButton removeAll = new JButton ("delete all");		

			JPanel pnll1 = new JPanel ();
			pnll1.add (push);
			buttonsPanel.add (pnll1);

			JPanel pnll2 = new JPanel ();
			pnll2.add (pop);	
			buttonsPanel.add (pnll2);

			JPanel pnll3 = new JPanel ();
			pnll3.add (removeAll);	
			buttonsPanel.add (pnll3);
			
			push.addActionListener (e -> {	
				for (MaterieDataModel m : test.getSelectedValuesList ()) {
					if (!model.contains (m)) {		
						model.addElement (m);
					}
					System.out.println (m);
				}
			});

			pop.addActionListener (e -> {
				int index;
				while (	(index = list.getSelectedIndex ()) != -1) {
					model.remove (index);
				}
			});
			
			removeAll.addActionListener (e -> {
				model.removeAllElements ();
			});
		
			panels [i].add (buttonsPanel);	
		}
			
		String CLS9 = "Clasa 9";	
		String CLS10 = "Clasa 10";	
		String CLS11 = "Clasa 11";	
		String CLS12 = "Clasa 12";	

		cards.add (panels [0], CLS9);	
		cards.add (panels [1], CLS10);	
		cards.add (panels [2], CLS11);	
		cards.add (panels [3], CLS12);	

		String comboBoxItems[] = {CLS9, CLS10, CLS11, CLS12};

		JComboBox <String> cb = new JComboBox <>(comboBoxItems);
		cb.setEditable (false);		
		cb.addItemListener (e -> {
			CardLayout cl = (CardLayout) (cards.getLayout ());
			cl.show (cards, (String)e.getItem ());
		});

		JPanel center = new JPanel (new BorderLayout ());
		center.add (cb, BorderLayout.NORTH);

		JPanel cntr = new JPanel (new GridLayout (2, 1));
		cntr.add (cards);	
		cntr.add (new JScrollPane (test));
		center.add (cntr, BorderLayout.CENTER);
		insertPanel.add (center, BorderLayout.CENTER);
				
		insert_profil = new JButton ("INSERT");
		insert_profil.setEnabled (false);
		obs.put ("insert-profil", insert_profil);

		nume_profil.addKeyListener (new KeyListener () {
			public void keyPressed (KeyEvent e) {
			}		
			public void keyReleased (KeyEvent e) {
				insert_profil.setEnabled (
					nume_profil.getText ().trim ().length () > 0
				);	
			}		
			public void keyTyped (KeyEvent e) {
			}	
		});
	

		insertPanel.add (insert_profil, BorderLayout.SOUTH);
		obs.put ("insert-profil", insert_profil);
	
		return panel;
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
	
		nume_materie.addKeyListener (klsnr);
		/*************************************************/

		JPanel south = new JPanel ();
	
		delete_materie = new JButton ("DELETE");
		update_materie = new JButton ("UPDATE");

		obs.put ("delete-materie", delete_materie);		
		obs.put ("update-materie", update_materie);		

		delete_materie.setEnabled (false);
		update_materie.setEnabled (false);
		south.add (delete_materie);
		south.add (update_materie);
	
		table.add (south, BorderLayout.SOUTH);

		materie_table_model = new MaterieTableModel ();

		Vector <MaterieDataModel> materii = con.fetchMaterie ();	
	
		MaterieTableModel.setData (materii);	

		lista_materii = 
			new DefaultListModel <MaterieDataModel> () {
				public static final long serialVersionUID = 0xad1;
				@Override
				public MaterieDataModel getElementAt (int index) {
					return materie_table_model.get (index);
				}
				@Override
				public int getSize () {
					int size = materie_table_model.getRowCount ();
					fireContentsChanged (this, 0, size);
					return size;
				}
			};
	
					
		table_materie = createTable (materie_table_model);
		table_materie.getSelectionModel ().addListSelectionListener (e -> {
			if (table_materie.getSelectedRow () != -1) {
				delete_materie.setEnabled (true);
				update_materie.setEnabled (true);
			} else {
				delete_materie.setEnabled (false);
				update_materie.setEnabled (false);
			}
		});

		JScrollPane scrollPane = new JScrollPane (table_materie);
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
                new TableRowSorter <> (materie_table_model);

        table_materie.setRowSorter (trs);

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

	/*	
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
	*/

	private JPanel createQueryPanel ()
	{
		query_panel = new JPanel (new BorderLayout ()); 	
		query_panel.setBorder (
				new TitledBorder (
					new EtchedBorder (), "Execute a query")
		);	
	
		query_text = new JTextArea ();
		query_text.setWrapStyleWord (true);					
		query_text.setEditable (true);
		
		JScrollPane scroll = new JScrollPane (query_text);
		scroll.setVerticalScrollBarPolicy (
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
		);
	
		execute = new JButton ("Go");				
			
		obs.put ("execute", execute);
		
		JPanel north_wrapper = new JPanel (new GridBagLayout ());
		
		GridBagConstraints c = new GridBagConstraints ();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.9;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.ipady = 70;
		north_wrapper.add (scroll, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0.1;

		JPanel buttonWrapper = new JPanel ();
		buttonWrapper.add (execute);
		north_wrapper.add (buttonWrapper, c);

		query_result_panel = new JPanel (new GridLayout (1, 1));
		query_panel.add (north_wrapper, BorderLayout.NORTH);
		query_panel.add (query_result_panel, BorderLayout.CENTER);	
  		return query_panel;
	}


	public void refresh ()
	{
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


	public void re ()
	{
		lista_materii.getSize ();
		tabbed_pane.revalidate ();	
		tabbed_pane.repaint ();
	}


	public void addQueryResult (String text)
	{
		query_result_panel.removeAll ();	
		query_result_panel.revalidate ();
		query_result_panel.repaint ();
        query_result_panel.add (
			new JScrollPane (
				new JTextArea (text)
			)
		);
	}

	public Vector <Vector <MaterieDataModel>> getMateriiIDs ()
	{
		Vector <Vector <MaterieDataModel>> ret = new Vector <>();

		for (DefaultListModel <MaterieDataModel> m : materii_an_scolar) {
		
			Vector <MaterieDataModel> materii_an = new Vector <>();
			for (java.util.Enumeration <MaterieDataModel> x = m.elements ();
					x.hasMoreElements (); ) 
			{
				materii_an.add (x.nextElement ());	
			}
			ret.add (materii_an);
		}
		return ret;
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


	public String getNumeMaterie ()
	{
		return nume_materie.getText ().trim ();
	}

	
	public String getProfil ()
	{
		return nume_profil.getText ().trim ();
	}

	
	public String getQueryText ()
	{
		return query_text.getText ();
	}
	

	public Vector <Integer> getTableMaterieSelectedIDs (boolean confirmEachDelete)
	{
		Vector <Integer> selectedIDs = new Vector <>();

		for (int index : table_materie.getSelectedRows ()) {
			int s = table_materie.convertRowIndexToModel (index);
			
			String str = ((MaterieTableModel)table_materie.getModel ()).getRow (s);	
			
			if (confirmEachDelete && 	
				displayConfirmation ("Are you sure you want to delete: " + str) == YES_OPTION)
			{
				selectedIDs.add ((Integer)table_materie.getModel ().getValueAt (s, 0));
			}
		}	
		return selectedIDs;
	}


	public Vector <Integer> getTableProfilSelectedIDs (boolean confirmEachDelete)
	{
		Vector <Integer> selectedIDs = new Vector <>();

		for (int index : table_profil.getSelectedRows ()) {
			int s = table_profil.convertRowIndexToModel (index);
			
			String str = ((ProfilTableModel)table_profil.getModel ()).getRow (s);	
			if (confirmEachDelete && 	
				displayConfirmation ("Are you sure you want to delete: " + str) == YES_OPTION)
			{
				selectedIDs.add ((Integer)table_profil.getModel ().getValueAt (s, 0));
			}
		}	
		return selectedIDs;
	}




	public void displayError (String errorMsg)
	{
		showMessageDialog (this, errorMsg, "Error", ERROR_MESSAGE);
	}	


	public void displayWarning (String warnMsg)
	{
		showMessageDialog (this, warnMsg, "Warning", WARNING_MESSAGE);
	}


	public int displayConfirmation (String confirmMsg)
	{
		return showConfirmDialog (this, confirmMsg, "Confirmation", 
									YES_NO_OPTION, WARNING_MESSAGE);
	}
		
	
	public void displayInformation (String infoMsg)
	{
		showMessageDialog (this, infoMsg, "Notice", INFORMATION_MESSAGE);
	}


	public void addListener (ActionListener lsnr)
	{
		execute.addActionListener (lsnr);
		//adauga_elev.addActionListener (lsnr);
		//adauga_profil.addActionListener (lsnr);
		//adauga_clasa.addActionListener (lsnr);
		//exit.addActionListener (lsnr);
		insert_materie.addActionListener (lsnr);
		delete_materie.addActionListener (lsnr);
		update_materie.addActionListener (lsnr);
		
		insert_profil.addActionListener (lsnr);
		update_profil.addActionListener (lsnr);
		delete_profil.addActionListener (lsnr);
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
