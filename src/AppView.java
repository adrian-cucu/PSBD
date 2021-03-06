import javax.swing.event.ListDataListener;
import com.toedter.calendar.JDateChooser;
import static javax.swing.JOptionPane.*;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.border.*;
import javax.swing.table.*;
import java.util.HashMap;
import javax.swing.JList;
import java.util.Vector;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.beans.*;

class AppView extends JFrame {	

	static final long serialVersionUID = 0xad1;

	private JTabbedPane tabbed_pane = null;

	private JPanel query_panel, query_result_panel;

	private JScrollPane scroll_pane;
	private JTextArea query_text;
	private JButton execute;

	/* materie */
	private JTable table_materie;
	private MaterieTableModel materie_table_model;

	private JTextField nume_materie; 
	private JButton insert_materie, delete_materie, update_materie;
	private DefaultListModel <MaterieDataModel> model_lista_materii; 
	private Vector <DefaultListModel <MaterieDataModel>> materii_an_scolar; 
	/* materie */

	/* profil */
	private JTable table_profil;
	private ProfilTableModel profil_table_model;		
	private JPanel profilInputPanel;
	private	JTextField nume_profil; 
	private JButton insert_profil, delete_profil, update_profil;	
	/* profil */

	/* clasa */
	private JTable table_clasa;
	private ClasaTableModel clasa_table_model;
	private JButton insert_clasa, delete_clasa;	
	private JComboBox <ProfilDataModel> profiluri = null;
	private Vector <ProfilDataModel> prof = null;
	private JComboBox <Integer> cbAnStudiu = 
		new JComboBox <> (new Integer[] {9, 10, 11, 12});
	private JTextField cod, an_studiu;
	/* clasa */

	/* elev */
	private JTable table_elev;
	private ElevTableModel elev_table_model;
	private JTextArea adresa;
	private JTextField nume_elev; 	
	private JTextField prenume_elev; 	
	private JTextField cnp_elev; 	
	private JTextField etnie_elev; 	
	private JTextField nationalitate_elev; 	
	private JDateChooser dataNasterii;
	private JButton insert_elev, delete_elev, update_elev; 
	private JComboBox <ClasaDataModel> clase = null;
	private Vector <ClasaDataModel> clasaVector = null;
	/* elev */

	/* bursa */
	private JComboBox <BursaDataModel> bursa = null;
	private JPanel bursaTablePanel;	
	private JTextField anStudiuBursa;
	private JTextField nrBurse;
	private JButton bursaFilter;
	/* bursa */	

	private final JPanel status_panel;
	private final JLabel status_label;	

	private HashMap <String, Object> obs = new HashMap<>();	
	private HashMap <String, JPanel> goPanels = new HashMap<>();
	
	private java.util.Vector <java.util.Vector <Object>> data = null;

	private MyConnection con = null;

	public static final int SUCCESS = 1;
	public static final int ERROR = 0;	

	private String panelsName[] = {
		"Elev", "Clasa", "Materie", "Profil",
		"Medie", "Elev-Clasa", "Run query", "Bursa"};


	AppView (String title, MyConnection con) 
	{
		super (title);	

		this.con = con;

		setDefaultCloseOperation (DO_NOTHING_ON_CLOSE);
		setBounds (250, 150, 800, 500);	
		setResizable (true);

		JMenuBar menuBar = new JMenuBar ();
		setJMenuBar (menuBar);
		JMenu open = new JMenu ("Open");
		menuBar.add (open);

		goPanels.put (panelsName [0], createElevPanel ());
		goPanels.put (panelsName [1], createClasaPanel ());
		goPanels.put (panelsName [2], createPanelAdaugaMaterie ());
		goPanels.put (panelsName [3], createPanelAdaugaProfil ());
		goPanels.put (panelsName [4], createPanelAdaugaMedie ());
		goPanels.put (panelsName [5], createPanelElevClasa ());
		goPanels.put (panelsName [6], createQueryPanel ());
		goPanels.put (panelsName [7], createBursaPanel ());

		JPanel allPanels = new JPanel (new CardLayout ());	

		add (allPanels);
			
		for (String name : panelsName) {		
			JMenuItem frame = new JMenuItem (name);

			allPanels.add (goPanels.get (name), name);
			frame.addActionListener (e -> {
				CardLayout cl = (CardLayout) (allPanels.getLayout ());
				cl.show (allPanels, ((JMenuItem) e.getSource ()).getText ());	
			});

			open.add (frame);	
		}

		status_panel = new JPanel (null);
		status_panel.setBorder (new BevelBorder (BevelBorder.LOWERED));
		add (status_panel, BorderLayout.SOUTH);

		status_panel.setLayout (new BoxLayout (status_panel, BoxLayout.X_AXIS));
		status_label = new JLabel ("...");	
		status_label.setHorizontalAlignment (SwingConstants.RIGHT);
		status_label.setFont (new Font ("Serif", Font.PLAIN, 11));
		status_panel.add (status_label);		
	}	

	
	private JPanel bursaPanel ()
	{
		return null;
	}


	private JPanel createBursaPanel ()
	{
		JPanel panel = new JPanel (new GridLayout (1, 2, 5, 0));	
		//////////////////////////////////////////////////////////////////		
		///////////////////////// insert panel ///////////////////////////
		JPanel insertPanelWrapper = new JPanel (new BorderLayout ());
		insertPanelWrapper.setBorder (
			new TitledBorder (new EtchedBorder (), "Insert")
		);
		
		JPanel northWrapper = new JPanel ();
		bursa = new JComboBox <BursaDataModel> (con.fetchTableBursaObjData ());
		northWrapper.add (bursa);

		JPanel centerWrapper = new JPanel (new GridLayout (10, 1, 5, 5));

		JLabel lbl1 = new JLabel ("An studiu");
		centerWrapper.add (new JPanel ().add (lbl1));

		anStudiuBursa = new JTextField (10);
		centerWrapper.add (new JPanel ().add (anStudiuBursa));		

		JLabel lbl2 = new JLabel ("Numar maxim de burse");
		centerWrapper.add (new JPanel ().add (lbl2));

		nrBurse = new JTextField (10);
		centerWrapper.add (new JPanel ().add (nrBurse));			
		
		bursaFilter = new JButton ("Filter");
		obs.put ("bursa-filter", bursaFilter);

		insertPanelWrapper.add (northWrapper, BorderLayout.NORTH);			
		insertPanelWrapper.add (centerWrapper, BorderLayout.CENTER);		
		insertPanelWrapper.add (bursaFilter, BorderLayout.SOUTH);		
		panel.add (insertPanelWrapper);				
		/////////////////////////////////////////////////////////////////
		//////////////////////// table panel ////////////////////////////	
		bursaTablePanel = new JPanel (new GridLayout (1, 1));						
		bursaTablePanel.setBorder (
			new TitledBorder (new EtchedBorder (), "Table")
		);
		


		panel.add (bursaTablePanel);				
		/////////////////////////////////////////////////////////////////		
		return panel;
	}
	

	private JPanel createPanelElevClasa ()
	{
		JPanel panel = new JPanel (new GridLayout (1, 2, 5, 0));	
		//////////////////////////////////////////////////////////////////		
		///////////////////////// insert panel ///////////////////////////
		JPanel insertPanelWrapper = new JPanel (new BorderLayout ());
		insertPanelWrapper.setBorder (
			new TitledBorder (new EtchedBorder (), "Insert")
		);

		JLabel lbl1 = new JLabel (" ---- ");
		lbl1.setFont (new Font ("serif", Font.PLAIN, 12));

		JLabel lbl2 = new JLabel (" ---- ");
		lbl2.setFont (new Font ("serif", Font.PLAIN, 10));

		JPanel northWrapper = new JPanel ();	
		northWrapper.add (lbl1);
		northWrapper.add (lbl2);

		JPanel centerWrapper = new JPanel (new GridLayout (1, 2, 1, 10));	

		ClasaTableModel cls_tbl_mdl1 = new ClasaTableModel ();
		cls_tbl_mdl1.setData (con.fetchTableClasaRawData ());

		ClasaTableModel cls_tbl_mdl2 = new ClasaTableModel ();
		cls_tbl_mdl1.setData (con.fetchTableClasaRawData ());

		DefaultListModel <ClasaDataModel> clasa_model1 = 
			new DefaultListModel <ClasaDataModel> () {
				public static final long serialVersionUID = 0xad1;
				@Override
				public ClasaDataModel getElementAt (int index) {
					return cls_tbl_mdl1.get (index);
				}
				@Override
				public int getSize () {
					int size = cls_tbl_mdl1.getRowCount ();
					fireContentsChanged (this, 0, size);
					return size;
				}
			};
	
		JList <ClasaDataModel> list1 = new JList <> (clasa_model1);	

		centerWrapper.add (new JScrollPane (list1));	

		insertPanelWrapper.add (northWrapper, BorderLayout.NORTH);		
		insertPanelWrapper.add (centerWrapper, BorderLayout.CENTER);		
		panel.add (insertPanelWrapper);				
		/////////////////////////////////////////////////////////////////
		//////////////////////// table panel ////////////////////////////
		JPanel tablePanel = new JPanel (new GridLayout (1, 1));				
		tablePanel.setBorder (
			new TitledBorder (new EtchedBorder (), "Table")
		);
			
		//////////////////////// table panel - content //////////////////
		
		JPanel tablePanelContent = new JPanel (new BorderLayout ());

		JTextField searchId = new JTextField (3);			
		JTextField searchNume = new JTextField (4);			
		JTextField searchPren = new JTextField (3);			
		JTextField searchAdresa = new JTextField (6);			
		JTextField searchCnp = new JTextField (5);			
		JTextField searchEtnie = new JTextField (5);			
		JTextField searchNationalitate = new JTextField (5);			

		JPanel searchInputPanel = new JPanel ();
		
		searchInputPanel.add (searchId);
		searchInputPanel.add (searchNume);
		searchInputPanel.add (searchPren);
		searchInputPanel.add (searchAdresa);
		searchInputPanel.add (searchCnp);
		searchInputPanel.add (searchEtnie);
		searchInputPanel.add (searchNationalitate);
	
		tablePanelContent.add (searchInputPanel, BorderLayout.NORTH);

		ElevTableModel elev_table_model1 = new ElevTableModel ();

		Vector <Vector <Object>> data = con.fetchTableElevRawData ();

        elev_table_model1.setData (data);

		JTable table_elev1 = createTable (elev_table_model1) ;
	
		table_elev1.setSelectionMode (0);

		table_elev1.getSelectionModel ().addListSelectionListener (e -> {
			if (e.getValueIsAdjusting ()) {
				return;
			}

			int x = table_elev1.getSelectedRow ();
			if (x != -1) {
				ElevTableModel model = (ElevTableModel)table_elev1.getModel ();

				int index = table_elev1.convertRowIndexToModel (x);			
				ElevDataModel obj = ElevTableModel.getRow (index);
				lbl1.setText (obj.getNumeComplet ());
				int id_elev = obj.getID ();
				lbl2.setText (" ~#" + id_elev);
			}
		});

        TableRowSorter <AbstractTableModel> trs =
			new TableRowSorter <> (elev_table_model);

        table_elev1.setRowSorter (trs);

		Action action = new AbstractAction () {
			public static final long serialVersionUID = 0xad1;
		
			public void actionPerformed (ActionEvent actEvt) {
				TableCellListener tcl = (TableCellListener) actEvt.getSource ();
	
				if (displayConfirmation ("Change " + tcl.getOldValue () + " to " + tcl.getNewValue ()) != YES_OPTION) 
				{
					elev_table_model1.setValueAt (tcl.getOldValue (), tcl.getRow (), tcl.getColumn ());		

				} else {
					/* update in database */

					int col = tcl.getColumn ();
					String newValue = (String) tcl.getNewValue ();
	
					try {
						if (col == 1) {
							ElevDataModel.checkNume (newValue);
						} else if (col == 2) {
					 		ElevDataModel.checkPrenume (newValue);
						} else if (col == 3) {
							ElevDataModel.checkAdresa (newValue);
						} else if (col == 5) {
							ElevDataModel.checkEtnie (newValue);
						} else if (col == 6) {
							ElevDataModel.checkNationalitate (newValue);
						}

					} catch (DataModelTypeMismatchError dmtEx) {

						elev_table_model1.setValueAt (tcl.getOldValue (), tcl.getRow (), tcl.getColumn ());		
						displayError (dmtEx.getMessage ());	
						return;
					}		
					
					boolean success = 
					con.executeUpdate ("elev", "id_elev",
							elev_table_model1.getColumnName (tcl.getColumn ()),
							 ElevTableModel.getRow (tcl.getRow ()).getID (),
							tcl.getNewValue (),	
							elev_table_model1.getColumnClass (tcl.getColumn ())
					);

					if  (success) {
						displayInformation ("Succefully update!");		
					}
				}
			}
		};
	
		TableCellListener tcl = new TableCellListener (table_elev1, action);

		tablePanelContent.add (new JScrollPane (table_elev1), BorderLayout.CENTER);

        KeyListener searchKeyLsnr = new KeyListener () {
            public void keyTyped (KeyEvent e) {}
            public void keyPressed (KeyEvent e) {}
            public void keyReleased (KeyEvent e)
            {
               	String id_elev = searchId.getText ().trim ();			
				String nume_elev = searchNume.getText ().trim ();			
				String pren_elev = searchPren.getText ().trim ();		
				String adresa_elev = searchAdresa.getText ().trim ();			
				String cnp_elev = searchCnp.getText ().trim ();			
				String etnie_elev = searchEtnie.getText ().trim ();			
				String nat_elev = searchNationalitate.getText ().trim ();		
           
                if (!(id_elev.length () > 0 || nume_elev.length () > 0 ||
					pren_elev.length () > 0 || adresa_elev.length () > 0 || 
					cnp_elev.length () > 0  || etnie_elev.length () > 0 ||
					nat_elev.length () > 0)) {
            
			        trs.setRowFilter (null);	
                } else {

                    java.util.List <RowFilter <Object, Object>> filters =
                        new java.util.ArrayList <> ();

                    if (id_elev.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + java.util.regex.Pattern.quote (id_elev) + ".*",  0
                            )
                        );
                    }
                    if (nume_elev.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + java.util.regex.Pattern.quote (nume_elev) + ".*",  1
                            )
                        );
                    }
                    if (pren_elev.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + java.util.regex.Pattern.quote (pren_elev) + ".*",  2
                            )
                        );
                    }
                    if (adresa_elev.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + ".*" + java.util.regex.Pattern.quote (adresa_elev) + ".*",  3
                            )
                        );
                    }
                    if (cnp_elev.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + java.util.regex.Pattern.quote (cnp_elev) + ".*",  4
                            )
                        );
                    }
                    if (etnie_elev.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + java.util.regex.Pattern.quote (etnie_elev) + ".*",  5
                            )
                        );
                    }
                    if (nat_elev.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + java.util.regex.Pattern.quote (nat_elev) + ".*",  6
                            )
                        );
                    }

                    trs.setRowFilter (RowFilter.andFilter (filters));
                }
            }
        };

        searchId.addKeyListener (searchKeyLsnr);
        searchNume .addKeyListener (searchKeyLsnr);
		searchPren .addKeyListener (searchKeyLsnr);		
		searchAdresa .addKeyListener (searchKeyLsnr);		
		searchCnp .addKeyListener (searchKeyLsnr);			
		searchEtnie.addKeyListener (searchKeyLsnr);
		searchNationalitate.addKeyListener (searchKeyLsnr);

		tablePanel.add (tablePanelContent);	
		
		panel.add (tablePanel);				
		/////////////////////////////////////////////////////////////////		
		return panel;

	}			


	private JPanel createPanelAdaugaMedie ()
	{
		JPanel panel = new JPanel (new GridLayout (1, 2, 5, 0));	
		//////////////////////////////////////////////////////////////////		
		///////////////////////// insert panel ///////////////////////////
		JPanel insertPanelWrapper = new JPanel (new BorderLayout ());
		insertPanelWrapper.setBorder (
			new TitledBorder (new EtchedBorder (), "Insert")
		);
		JLabel lbl1 = new JLabel (" ---- ");
		lbl1.setFont (new Font ("serif", Font.PLAIN, 12));


		JLabel lbl2 = new JLabel (" ---- ");
		lbl2.setFont (new Font ("serif", Font.PLAIN, 10));

		JPanel northWrapper = new JPanel ();	
		northWrapper.add (lbl1);
		northWrapper.add (lbl2);

		JPanel centerWrapper = new JPanel (new BorderLayout ());	

		JPanel nth = new JPanel ();
		JPanel ctr = new JPanel (new GridLayout (1, 1));

		JComboBox <ClasaDataModel> clase_elv = new JComboBox <> ();
		nth.add (clase_elv);

		centerWrapper.add (nth, BorderLayout.NORTH);	
		centerWrapper.add (ctr, BorderLayout.CENTER);	

		insertPanelWrapper.add (northWrapper, BorderLayout.NORTH);		
		insertPanelWrapper.add (centerWrapper, BorderLayout.CENTER);		
		panel.add (insertPanelWrapper);				
		/////////////////////////////////////////////////////////////////
		//////////////////////// table panel ////////////////////////////
		JPanel tablePanel = new JPanel (new GridLayout (1, 1));				
		tablePanel.setBorder (
			new TitledBorder (new EtchedBorder (), "Table")
		);
			
		//////////////////////// table panel - content //////////////////
		
		JPanel tablePanelContent = new JPanel (new BorderLayout ());

		JTextField searchId = new JTextField (3);			
		JTextField searchNume = new JTextField (4);			
		JTextField searchPren = new JTextField (3);			
		JTextField searchAdresa = new JTextField (6);			
		JTextField searchCnp = new JTextField (5);			
		JTextField searchEtnie = new JTextField (5);			
		JTextField searchNationalitate = new JTextField (5);			

		JPanel searchInputPanel = new JPanel ();
		
		searchInputPanel.add (searchId);
		searchInputPanel.add (searchNume);
		searchInputPanel.add (searchPren);
		searchInputPanel.add (searchAdresa);
		searchInputPanel.add (searchCnp);
		searchInputPanel.add (searchEtnie);
		searchInputPanel.add (searchNationalitate);
	
		tablePanelContent.add (searchInputPanel, BorderLayout.NORTH);

		ElevTableModel elev_table_model1 = new ElevTableModel ();

		Vector <Vector <Object>> data = con.fetchTableElevRawData ();

        elev_table_model1.setData (data);

		JTable table_elev1 = createTable (elev_table_model1) ;
	
		table_elev1.setSelectionMode (0);

		table_elev1.getSelectionModel ().addListSelectionListener (e -> {
			if (e.getValueIsAdjusting ()) {
				return;
			}

			int x = table_elev1.getSelectedRow ();
			if (x != -1) {
				ElevTableModel model = (ElevTableModel)table_elev1.getModel ();

				int index = table_elev1.convertRowIndexToModel (x);			
				ElevDataModel obj = ElevTableModel.getRow (index);
				lbl1.setText (obj.getNumeComplet ());
				int id_elev = obj.getID ();
				lbl2.setText (" ~#" + id_elev);

				Vector <ClasaDataModel> clase = con.getClsElev (id_elev);

				if (clase == null) {
					clase_elv.removeAllItems ();
					ctr.removeAll ();
					ctr.revalidate ();
					ctr.repaint ();
	

				} else {	
					clase_elv.setModel (
						new DefaultComboBoxModel <ClasaDataModel> (clase)
					);

					ClasaDataModel selectedCls = 
						(ClasaDataModel)clase_elv.getSelectedItem ();
				
					int id_clasa = selectedCls.getID ();
					int id_profil = selectedCls.getIdProfil ();
					int an_studiu = selectedCls.getAnStudiu ();

					Vector <Vector <Object>> medii_sem =   
						con.fetchMediCls (id_elev, id_clasa, id_profil, an_studiu);

					Vector <String> nume_materii = new Vector <String> ();

					for (Object o : medii_sem.get (0)) {
						nume_materii.add (((MaterieDataModel)o).getNumeMaterie ());
					}
					JTable tabela_medii = new JTable ();
					MedieTableModel mdl = 
						new MedieTableModel (nume_materii); 
		
					mdl.setData (0, medii_sem.get (1));
					mdl.setData (1, medii_sem.get (2));

					tabela_medii.setModel (mdl);
					ctr.removeAll ();
					ctr.revalidate ();
					ctr.repaint ();
					ctr.add (new JScrollPane (tabela_medii));					

					Action action = new AbstractAction () {
						public static final long serialVersionUID = 0xad1;
						public void actionPerformed (ActionEvent actEvt) {
							TableCellListener tcl = 
								(TableCellListener) actEvt.getSource ();
						
							if (displayConfirmation ("Change " +  tcl.getOldValue () + " to " +  tcl.getNewValue ()) != YES_OPTION) {
								mdl.setValueAt (tcl.getOldValue (), tcl.getRow (), tcl.getColumn ());		
							
							} else {
								/* update in database */
	
								int semestru = tcl.getRow () + 1;	
								int medie = -1;
								int id_materie = 
									((MaterieDataModel) medii_sem.get (0).get (tcl.getColumn ())).getID ();
								boolean success = false;

								try {
									medie = Integer.parseInt ((String)tcl.getNewValue ());
									if (!(1 < medie && medie <= 10)) {
										throw new Exception ("Media este invalida");
									}
									success = con.insertMedie (id_elev, id_clasa, id_materie, medie, semestru);
									mdl.notifyListeners ();
									
								} catch (Exception e) {
									mdl.setValueAt (tcl.getOldValue (), tcl.getRow (), tcl.getColumn ());		
									displayError (e.getMessage ());
									success = false;
								}
					
								if (success) {
									displayInformation ("Succefully update!");		
								}
							}
						}
					};
					TableCellListener tcl = 
						new TableCellListener (tabela_medii, action);
				}
			}	
		});

		clase_elv.addItemListener (evt -> {
			ClasaDataModel selectedCls = (ClasaDataModel)clase_elv.getSelectedItem ();

			if (selectedCls == null) {
				return;
			}

			int id_clasa = selectedCls.getID ();
			int id_profil = selectedCls.getIdProfil ();
			int an_studiu = selectedCls.getAnStudiu ();

			int id_elev = -1;

			int x = table_elev1.getSelectedRow ();
			if (x != -1) {
				ElevTableModel model = (ElevTableModel)table_elev1.getModel ();

				int index = table_elev1.convertRowIndexToModel (x);			
				ElevDataModel obj = ElevTableModel.getRow (index);
				id_elev = obj.getID ();

			}
							

			Vector <Vector <Object>> medii_sem = con.fetchMediCls (id_elev, id_clasa, id_profil, an_studiu);
			Vector <String> nume_materii = new Vector <String> ();

			for (Object o : medii_sem.get (0)) {
				nume_materii.add (((MaterieDataModel)o).getNumeMaterie ());
			}
		
			JTable tabela_medii = new JTable ();
			MedieTableModel mdl = new MedieTableModel (nume_materii); 
		
			mdl.setData (0, medii_sem.get (1));
			mdl.setData (1, medii_sem.get (2));

			tabela_medii.setModel (mdl);
			ctr.removeAll ();
			ctr.revalidate ();
			ctr.repaint ();
			ctr.add (new JScrollPane (tabela_medii));					

			Action action = new AbstractAction () {
				public static final long serialVersionUID = 0xad1;
				public void actionPerformed (ActionEvent actEvt) {
				TableCellListener tcl = (TableCellListener) actEvt.getSource ();
							
				if (displayConfirmation ("Change " +  tcl.getOldValue () + " to " +  tcl.getNewValue ()) != YES_OPTION) {
					
					mdl.setValueAt (tcl.getOldValue (), tcl.getRow (), tcl.getColumn ());		
							
				} else {
					/* update in database */
		
					int semestru = tcl.getRow () + 1;	
					int medie = -1;
					int id_materie = ((MaterieDataModel) medii_sem.get (0).get (tcl.getColumn ())).getID ();
					boolean success = false;
					int id_elev = -1;

					int x = table_elev1.getSelectedRow ();
					if (x != -1) {
						ElevTableModel model = (ElevTableModel)table_elev1.getModel ();

						int index = table_elev1.convertRowIndexToModel (x);			
						ElevDataModel obj = ElevTableModel.getRow (index);
						id_elev = obj.getID ();
					}
							

					try {
						medie = Integer.parseInt ((String)tcl.getNewValue ());
						if (!(1 < medie && medie <= 10))  {
							throw new Exception ("Media este invalida");
						}
						success = con.insertMedie (id_elev, id_clasa, id_materie, medie, semestru);
						mdl.notifyListeners ();
									
					} catch (Exception e) {
						mdl.setValueAt (tcl.getOldValue (), tcl.getRow (), tcl.getColumn ());		
						displayError (e.getMessage ());
						success = false;
					}
					
					if (success) {
						displayInformation ("Succefully update!");		
					}
				}
			}
		};
		
		TableCellListener tcl = new TableCellListener (tabela_medii, action);
	});

        TableRowSorter <AbstractTableModel> trs =
			new TableRowSorter <> (elev_table_model);

        table_elev1.setRowSorter (trs);

		tablePanelContent.add (new JScrollPane (table_elev1), BorderLayout.CENTER);

        KeyListener searchKeyLsnr = new KeyListener () {
            public void keyTyped (KeyEvent e) {}
            public void keyPressed (KeyEvent e) {}
            public void keyReleased (KeyEvent e)
            {
               	String id_elev = searchId.getText ().trim ();			
				String nume_elev = searchNume.getText ().trim ();			
				String pren_elev = searchPren.getText ().trim ();		
				String adresa_elev = searchAdresa.getText ().trim ();			
				String cnp_elev = searchCnp.getText ().trim ();			
				String etnie_elev = searchEtnie.getText ().trim ();			
				String nat_elev = searchNationalitate.getText ().trim ();		
           
                if (!(id_elev.length () > 0 || nume_elev.length () > 0 ||
					pren_elev.length () > 0 || adresa_elev.length () > 0 || 
					cnp_elev.length () > 0  || etnie_elev.length () > 0 ||
					nat_elev.length () > 0)) {
            
			        trs.setRowFilter (null);	
                } else {

                    java.util.List <RowFilter <Object, Object>> filters =
                        new java.util.ArrayList <> ();

                    if (id_elev.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + java.util.regex.Pattern.quote (id_elev) + ".*",  0
                            )
                        );
                    }
                    if (nume_elev.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + java.util.regex.Pattern.quote (nume_elev) + ".*",  1
                            )
                        );
                    }
                    if (pren_elev.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + java.util.regex.Pattern.quote (pren_elev) + ".*",  2
                            )
                        );
                    }
                    if (adresa_elev.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + ".*" + java.util.regex.Pattern.quote (adresa_elev) + ".*",  3
                            )
                        );
                    }
                    if (cnp_elev.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + java.util.regex.Pattern.quote (cnp_elev) + ".*",  4
                            )
                        );
                    }
                    if (etnie_elev.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + java.util.regex.Pattern.quote (etnie_elev) + ".*",  5
                            )
                        );
                    }
                    if (nat_elev.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + java.util.regex.Pattern.quote (nat_elev) + ".*",  6
                            )
                        );
                    }

                    trs.setRowFilter (RowFilter.andFilter (filters));
                }
            }
        };

        searchId.addKeyListener (searchKeyLsnr);
        searchNume .addKeyListener (searchKeyLsnr);
		searchPren .addKeyListener (searchKeyLsnr);		
		searchAdresa .addKeyListener (searchKeyLsnr);		
		searchCnp .addKeyListener (searchKeyLsnr);			
		searchEtnie.addKeyListener (searchKeyLsnr);
		searchNationalitate.addKeyListener (searchKeyLsnr);

		tablePanel.add (tablePanelContent);	
		
		panel.add (tablePanel);				
		/////////////////////////////////////////////////////////////////		
		return panel;

	}				


	private JPanel inputProfilPanel ()
	{
		JPanel cards = new JPanel (new CardLayout ());

		JList <MaterieDataModel> lista_materii = 
			new JList <> (model_lista_materii);

		lista_materii.setSelectionMode (
			ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
		);

		Vector <JList <MaterieDataModel>> lists = new Vector <>();
		materii_an_scolar = new Vector <> ();	
		JPanel[] panels = new JPanel [4];

		for (int i = 0; i < 4; ++ i) {
			panels [i] = new JPanel (new GridLayout (1, 2));
			DefaultListModel <MaterieDataModel> model =
				new DefaultListModel <MaterieDataModel> ();
			
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
				for (MaterieDataModel m : lista_materii.getSelectedValuesList ()) {
					if (!model.contains (m)) {		
						model.addElement (m);
					}
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

		profilInputPanel = new JPanel (new GridLayout (2, 1, 10, 10));
		profilInputPanel.add (cards);	
		profilInputPanel.add (new JScrollPane (lista_materii));
		center.add (profilInputPanel, BorderLayout.CENTER);
	
		return center;

	}
	
	
	private JPanel createPanelAdaugaProfil ()
	{
		JPanel panel = new JPanel (new GridLayout (1, 2, 5, 5));

		JPanel insertPanel = new JPanel (new BorderLayout ());
		insertPanel.setBorder (
			new TitledBorder (new EtchedBorder (), "Insert")
		);	
	
		//JList <MaterieDataModel> test = new JList <> (lista_materii);
		//test.setSelectionMode (ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);


		panel.add (insertPanel);		
		
		ProfilTableModel profilModel = new ProfilTableModel ();
		profilModel.setData (con.fetchTableProfilObjData ());

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
		//////////////////////////////////////////////////////////
		JTextField searchID = new JTextField (10);			
		JTextField searchName = new JTextField (10);			

		JPanel searchInputPanel = new JPanel ();
		
		searchInputPanel.add (new JLabel ("ID"));
		searchInputPanel.add (searchID);	
		searchInputPanel.add (new JLabel ("Profil"));
		searchInputPanel.add (searchName);

		panel00.add (searchInputPanel, BorderLayout.NORTH);
		panel00.setBorder (
			new TitledBorder (new EtchedBorder (), "Table")
		);	
	

        TableRowSorter <AbstractTableModel> trs = 
			new TableRowSorter <> (profil_table_model);

        table_profil.setRowSorter (trs);

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

                    java.util.List <RowFilter <Object, Object>> filters = new java.util.ArrayList <> ();

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
		panel00.add (new JScrollPane (table_profil), BorderLayout.CENTER);
		panel00.add (panel01, BorderLayout.SOUTH);
		panel.add (panel00);

		JPanel pnl = new JPanel (new GridLayout (2, 2));		

		insertPanel.add (pnl, BorderLayout.NORTH);		
		
		nume_profil = new JTextField (10);
		
		pnl.add (new JLabel ("Profil"));
		pnl.add (nume_profil);

		insertPanel.add (inputProfilPanel (), BorderLayout.CENTER);
				
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
		JPanel panel = new JPanel (new GridLayout (1, 2, 5, 0));
	
		JPanel insertPanel = new JPanel ();	
		insertPanel.setBorder (
			new TitledBorder (new EtchedBorder (), "Insert")
		);	

		panel.add (insertPanel);
		
		insertPanel.setLayout (new BorderLayout ());
				
		JPanel north = new JPanel (new GridLayout (2, 1));

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

		JPanel table = new JPanel (new BorderLayout ());
		table.setBorder (
			new TitledBorder (new EtchedBorder (), "Table")
		);	

		panel.add (table);

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

		Vector <MaterieDataModel> materii = con.fetchTableMaterieObjData ();	
	
		MaterieTableModel.setData (materii);	

		model_lista_materii = 
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
	
					
		table_materie = createTable (materie_table_model) ;


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

		table_materie.getColumnModel ()
					.getColumn (0)
					.setPreferredWidth (35);	
	
		table_materie.setAutoResizeMode (JTable.AUTO_RESIZE_NEXT_COLUMN);

		/*
		JPanel pnl = new JPanel (new GridLayout (1, 1));

		table.add (pnl, BorderLayout.CENTER);
		System.out.println ("@ " + pnl.getWidth ());

		ceplm.add (scrollPane);
		table_materie.getColumnModel ()
					.getColumn (0)
					.setPreferredWidth (35);	
	
		table_materie.getColumnModel ()
					.getColumn (1)
					.setPreferredWidth (scrollPane.getViewport ()
											.getSize ()
											.width - 35);
		*/
			
		//////////////////////////////////////////////////////////
		JTextField searchID = new JTextField (10);			
		JTextField searchName = new JTextField (10);			

		JPanel searchInputPanel = new JPanel ();
		
		searchInputPanel.add (new JLabel ("ID"));
		searchInputPanel.add (searchID);	
		searchInputPanel.add (new JLabel ("Materie"));
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


	private JPanel createElevPanel ()
	{
		JPanel panel = new JPanel (new GridLayout (1, 2, 5, 0));	
		//////////////////////////////////////////////////////////////////		
		///////////////////////// insert panel ///////////////////////////
		JPanel insertPanelWrapper = new JPanel (new BorderLayout ());
		insertPanelWrapper.setBorder (
			new TitledBorder (new EtchedBorder (), "Insert")
		);
		
		JPanel insertPanel = new JPanel ();	
		insertPanel.setLayout (new GridLayout (7, 1));

		JPanel wrapper = null;	

		JLabel lbl_nume_elev = new JLabel ("Nume");
		wrapper = new JPanel ();
		wrapper.add (lbl_nume_elev);
		insertPanel. add (wrapper);

		nume_elev = new JTextField (10);				
		wrapper = new JPanel ();
		wrapper.add (nume_elev);
		insertPanel.add (wrapper);

		JLabel lbl_prenume_elev = new JLabel ("Prenume");		
		wrapper = new JPanel ();
		wrapper.add (lbl_prenume_elev);
		insertPanel. add (wrapper);

		prenume_elev = new JTextField (10);	
		wrapper = new JPanel ();
		wrapper.add (prenume_elev);
		insertPanel. add (wrapper);

		JLabel lbl_cnp = new JLabel ("CNP");
		wrapper = new JPanel ();
		wrapper.add (lbl_cnp);
		insertPanel. add (wrapper);

		cnp_elev = new JTextField (10);
		wrapper = new JPanel ();
		wrapper.add (cnp_elev);
		insertPanel. add (wrapper);

		JLabel lbl_etnie = new JLabel ("Etnie");
		wrapper = new JPanel ();
		wrapper.add (lbl_etnie);
		insertPanel. add (wrapper);

		etnie_elev = new JTextField (10);
		wrapper = new JPanel ();
		wrapper.add (etnie_elev);
		insertPanel. add (wrapper);

		JLabel lbl_nationalitate = new JLabel ("Nationalitate");
		wrapper = new JPanel ();
		wrapper.add (lbl_nationalitate);
		insertPanel. add (wrapper);

		nationalitate_elev = new JTextField (10);
		wrapper = new JPanel ();
		wrapper.add (nationalitate_elev);
		insertPanel. add (wrapper);

		JLabel lbl_adresa = new JLabel ("Adresa");
		wrapper = new JPanel ();
		wrapper.add (lbl_adresa);
		insertPanel. add (wrapper);

		adresa = new JTextArea ();
		insertPanel. add (new JScrollPane (adresa));
	
		JLabel lbl_clasa = new JLabel ("Data nasterii");
		wrapper = new JPanel ();
		wrapper.add (lbl_clasa);
		insertPanel.add (wrapper);

		/*
		clase = new JComboBox <ClasaDataModel> ();
		clasaVector = con.fetchTableClasaObjData ();
		clase.setModel (new DefaultComboBoxModel <> (clasaVector));	
		*/
	
		wrapper = new JPanel ();
//		wrapper.add (clase);
		
		dataNasterii = new JDateChooser ();
		wrapper.add (dataNasterii);

		insertPanel.add (wrapper);
			
		insertPanelWrapper.add (insertPanel, BorderLayout.CENTER);				

		/////////////////////////////////////////////////////////////////
		JPanel insertButtonPanel = new JPanel ();
		
		insert_elev = new JButton ("INSERT");
		obs.put ("insert-elev", insert_elev);
		insertButtonPanel.add (insert_elev);

		insertPanelWrapper.add (insertButtonPanel, BorderLayout.SOUTH);				
		panel.add (insertPanelWrapper);				
		/////////////////////////////////////////////////////////////////
		//////////////////////// table panel ////////////////////////////
		JPanel tablePanel = new JPanel (new GridLayout (1, 1));				
		tablePanel.setBorder (
			new TitledBorder (new EtchedBorder (), "Table")
		);

		//////////////////////// table panel - content //////////////////
		
		JPanel tablePanelContent = new JPanel (new BorderLayout ());

		JTextField searchId = new JTextField (3);			
		JTextField searchNume = new JTextField (3);			
		JTextField searchPren = new JTextField (3);			
		JTextField searchAdresa = new JTextField (4);			
		JTextField searchCnp = new JTextField (4);			
		JTextField searchEtnie = new JTextField (4);			
		JTextField searchNationalitate = new JTextField (4);			

		JPanel searchInputPanel = new JPanel ();
		
		searchInputPanel.add (searchId);
		searchInputPanel.add (searchNume);
		searchInputPanel.add (searchPren);
		searchInputPanel.add (searchAdresa);
		searchInputPanel.add (searchCnp);
		searchInputPanel.add (searchEtnie);
		searchInputPanel.add (searchNationalitate);

		delete_elev = new JButton ("DELETE");
		delete_elev.setEnabled (false);
		obs.put ("delete-elev", delete_elev);

		JPanel sth = new JPanel ();

		sth.add (delete_elev);

		tablePanelContent.add (sth, BorderLayout.SOUTH);		
		
		tablePanelContent.add (searchInputPanel, BorderLayout.NORTH);

		elev_table_model = new ElevTableModel ();

		Vector <Vector <Object>> data = con.fetchTableElevRawData ();

        elev_table_model.setData (data);

		table_elev = createTable (elev_table_model) ;

		TableColumn c1 = table_elev.getColumnModel ().getColumn (0);
		c1.setPreferredWidth (35);	
		
        TableRowSorter <AbstractTableModel> trs =
			new TableRowSorter <> (elev_table_model);

        table_elev.setRowSorter (trs);

		table_elev.getSelectionModel ().addListSelectionListener (e -> {
			delete_elev.setEnabled (
				table_elev.getSelectedRow () != -1
			); 
		});
		

		Action action = new AbstractAction () {
			public static final long serialVersionUID = 0xad1;
		
			public void actionPerformed (ActionEvent actEvt) {
				TableCellListener tcl = (TableCellListener) actEvt.getSource ();
	
				if (displayConfirmation ("Change " + tcl.getOldValue () + " to " + tcl.getNewValue ()) != YES_OPTION) 
				{
					elev_table_model.setValueAt (tcl.getOldValue (), tcl.getRow (), tcl.getColumn ());		

				} else {
					/* update in database */

					int col = tcl.getColumn ();
					String newValue = (String) tcl.getNewValue ();
	
					try {
						if (col == 1) {
							ElevDataModel.checkNume (newValue);
						} else if (col == 2) {
					 		ElevDataModel.checkPrenume (newValue);
						} else if (col == 3) {
							ElevDataModel.checkAdresa (newValue);
						} else if (col == 6) {
							ElevDataModel.checkEtnie (newValue);
						} else if (col == 7) {
							ElevDataModel.checkNationalitate (newValue);
						}

					} catch (DataModelTypeMismatchError dmtEx) {

						elev_table_model.setValueAt (tcl.getOldValue (), tcl.getRow (), tcl.getColumn ());		
						displayError (dmtEx.getMessage ());	
						return;
					}
						
					
					boolean success = 
					con.executeUpdate ("elev", "id_elev",
							elev_table_model.getColumnName (tcl.getColumn ()),
							 ElevTableModel.getRow (tcl.getRow ()).getID (),
							tcl.getNewValue (),	
							elev_table_model.getColumnClass (tcl.getColumn ())
					);

					if  (success) {
						displayInformation ("Succefully update!");		
					}
				}
			}
		};
	
		TableCellListener tcl = new TableCellListener (table_elev, action);

		tablePanelContent.add (new JScrollPane (table_elev), BorderLayout.CENTER);

        KeyListener searchKeyLsnr = new KeyListener () {
            public void keyTyped (KeyEvent e) {}
            public void keyPressed (KeyEvent e) {}
            public void keyReleased (KeyEvent e)
            {
               	String id_elev = searchId.getText ().trim ();			
				String nume_elev = searchNume.getText ().trim ();			
				String pren_elev = searchPren.getText ().trim ();		
				String adresa_elev = searchAdresa.getText ().trim ();			
				String cnp_elev = searchCnp.getText ().trim ();			
				String etnie_elev = searchEtnie.getText ().trim ();			
				String nat_elev = searchNationalitate.getText ().trim ();		
           
                if (!(id_elev.length () > 0 || nume_elev.length () > 0 ||
					pren_elev.length () > 0 || adresa_elev.length () > 0 || 
					cnp_elev.length () > 0  || etnie_elev.length () > 0 ||
					nat_elev.length () > 0)) {
            
			        trs.setRowFilter (null);	
                } else {

                    java.util.List <RowFilter <Object, Object>> filters =
                        new java.util.ArrayList <> ();

                    if (id_elev.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + java.util.regex.Pattern.quote (id_elev) + ".*",  0
                            )
                        );
                    }
                    if (nume_elev.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + java.util.regex.Pattern.quote (nume_elev) + ".*",  1
                            )
                        );
                    }
                    if (pren_elev.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + java.util.regex.Pattern.quote (pren_elev) + ".*",  2
                            )
                        );
                    }
                    if (adresa_elev.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + ".*" + java.util.regex.Pattern.quote (adresa_elev) + ".*",  3
                            )
                        );
                    }
                    if (cnp_elev.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + java.util.regex.Pattern.quote (cnp_elev) + ".*",  4
                            )
                        );
                    }
                    if (etnie_elev.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + java.util.regex.Pattern.quote (etnie_elev) + ".*",  5
                            )
                        );
                    }
                    if (nat_elev.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + java.util.regex.Pattern.quote (nat_elev) + ".*",  6
                            )
                        );
                    }

                    trs.setRowFilter (RowFilter.andFilter (filters));
                }
            }
        };

        searchId.addKeyListener (searchKeyLsnr);
        searchNume .addKeyListener (searchKeyLsnr);
		searchPren .addKeyListener (searchKeyLsnr);		
		searchAdresa .addKeyListener (searchKeyLsnr);		
		searchCnp .addKeyListener (searchKeyLsnr);			
		searchEtnie.addKeyListener (searchKeyLsnr);
		searchNationalitate.addKeyListener (searchKeyLsnr);

		tablePanel.add (tablePanelContent);	
		
		panel.add (tablePanel);				
		/////////////////////////////////////////////////////////////////		
		return panel;

	}


	private JPanel createClasaPanel ()
	{
		JPanel panel = new JPanel (new GridLayout (1, 2, 5, 0));	
		//////////////////////////////////////////////////////////////////		
		///////////////////////// insert panel ///////////////////////////

		JPanel insertPanel = new JPanel (new GridLayout (1, 1));
		insertPanel.setBorder (
			new TitledBorder (new EtchedBorder (), "Insert")
		);	

		insertPanel.setLayout (null);

		JLabel lbl_clasa_profil = new JLabel ("Profil");
		lbl_clasa_profil.setBounds (10, 20, 100, 20);	

		profiluri = new JComboBox <> ();
		prof = con.fetchTableProfilObjData ();
		profiluri.setModel (new DefaultComboBoxModel<> (prof));
		profiluri .setBounds (120, 20, 200, 20);

	
		JLabel lbl_cod = new JLabel ("Cod");
		lbl_cod.setBounds (10, 50, 100, 20);
		cod = new JTextField ();
		cod.setBounds (120, 50, 200, 20);

		JLabel lbl_clasa = new JLabel ("An studiu");
		lbl_clasa.setBounds (10, 80, 100, 20);
		cbAnStudiu.setBounds (120, 80, 200, 20);

		insert_clasa = new JButton ("INSERT");
		insert_clasa.setBounds (100, 110, 100, 30);

		insertPanel.add (lbl_clasa_profil);					
		insertPanel.add (lbl_cod);					
		insertPanel.add (lbl_clasa);				
	
		insertPanel.add (cod);					
		insertPanel.add (cbAnStudiu);					
		insertPanel.add (insert_clasa);					
	
		insertPanel.add (profiluri);					

		obs.put ("insert-clasa", insert_clasa);
		
		panel.add (insertPanel);				
		/////////////////////////////////////////////////////////////////
		//////////////////////// table panel ////////////////////////////
		JPanel tablePanel = new JPanel (new GridLayout (1, 1));				
		tablePanel.setBorder (
			new TitledBorder (new EtchedBorder (), "Table")
		);

		//////////////////////// table panel - content //////////////////
		JPanel tablePanelContent = new JPanel (new BorderLayout ());

		JTextField searchIdClasa = new JTextField (5);			
		JTextField searchIdProfil = new JTextField (5);			
		JTextField searchAnScolar = new JTextField (7);			
		JTextField searchCod = new JTextField (4);			
		JTextField searchAnStudiu = new JTextField (5);			

		JPanel searchInputPanel = new JPanel ();
		
		searchInputPanel.add (searchIdClasa);	
		searchInputPanel.add (searchIdProfil);
		searchInputPanel.add (searchAnScolar);
		searchInputPanel.add (searchCod);
		searchInputPanel.add (searchAnStudiu);

		delete_clasa = new JButton ("DELETE");
		delete_clasa.setEnabled (false);
		obs.put ("delete-clasa", delete_clasa);

		JPanel sth = new JPanel ();

		sth.add (delete_clasa);

		tablePanelContent.add (sth, BorderLayout.SOUTH);		
		

		tablePanelContent.add (searchInputPanel, BorderLayout.NORTH);

		clasa_table_model = new ClasaTableModel ();

		Vector <Vector <Object>> data= con.fetchTableClasaRawData ();

        clasa_table_model.setData (data);

		table_clasa = createTable (clasa_table_model) ;
		
        TableRowSorter <AbstractTableModel> trs =
			new TableRowSorter <> (clasa_table_model);

        table_clasa.setRowSorter (trs);

		table_clasa.getSelectionModel ().addListSelectionListener (e -> {
				delete_clasa.setEnabled (
					table_clasa.getSelectedRow () != -1
				);
		});


		tablePanelContent.add (new JScrollPane (table_clasa), BorderLayout.CENTER);

        KeyListener searchKeyLsnr = new KeyListener () {
            public void keyTyped (KeyEvent e) {}
            public void keyPressed (KeyEvent e) {}
            public void keyReleased (KeyEvent e)
            {
                String id_clasa = searchIdClasa.getText ().trim ();
                String id_profil = searchIdProfil.getText ().trim ();
				String an_scolar = searchAnScolar.getText ().trim ();
				String cod = searchCod.getText ().trim ();
				String an_studiu = searchAnStudiu.getText ().trim ();		
			
                if (!(id_clasa.length () > 0 || 
					 	id_profil.length () > 0 ||
						an_scolar.length () > 0 || 
						cod.length () > 0 || 
						an_studiu.length () > 0)) {
            
			        trs.setRowFilter (null);	
                } else {

                    java.util.List <RowFilter <Object, Object>> filters =
                        new java.util.ArrayList <> ();

                    if (id_clasa.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + java.util.regex.Pattern.quote (id_clasa) + ".*",  0
                            )
                        );
                    }
                    if (id_profil.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + java.util.regex.Pattern.quote (id_profil) + ".*",  1
                            )
                        );
                    }
                    if (an_scolar.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + java.util.regex.Pattern.quote (an_scolar) + ".*",  2
                            )
                        );
                    }
                    if (cod.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + java.util.regex.Pattern.quote (cod) + ".*",  3
                            )
                        );
                    }
                    if (an_studiu.length () > 0) {
                        filters.add (
                            RowFilter.regexFilter (
                            "(?i)^" + java.util.regex.Pattern.quote (an_studiu) + ".*",  4
                            )
                        );
                    }
                    trs.setRowFilter (RowFilter.andFilter (filters));
                }
            }
        };

        searchIdProfil.addKeyListener (searchKeyLsnr);
        searchIdClasa.addKeyListener (searchKeyLsnr);
		searchCod.addKeyListener (searchKeyLsnr);		
		searchAnScolar.addKeyListener (searchKeyLsnr);		
		searchAnStudiu.addKeyListener (searchKeyLsnr);			
		
		tablePanel.add (tablePanelContent);	

		panel.add (tablePanel);				
		/////////////////////////////////////////////////////////////////		
		return panel;
	}
	

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


	public int getNrBurse () 
	{
		int nr = -1;	
		nr = Integer.parseInt (nrBurse.getText ().trim ());
		return  nr;
	}

	public int getAnScolarBursa () 
	{
		int anStudiu = -1;
		anStudiu = Integer.parseInt (anStudiuBursa.getText ().trim ());
		return anStudiu;
	}

	
	public void addFilterBursaResult (JTable table)
	{
		bursaTablePanel.removeAll ();
		bursaTablePanel.revalidate ();
		bursaTablePanel.repaint ();
		bursaTablePanel.add (new JScrollPane (table));
	}


	public void addQueryResult (JTable table)
	{	
		query_result_panel.removeAll ();	
		query_result_panel.revalidate ();
		query_result_panel.repaint ();
  		scroll_pane = new JScrollPane (table);
        query_result_panel.add (scroll_pane);
	}	


	public void refresh ()
	{
		model_lista_materii.getSize ();
		tabbed_pane.revalidate ();	
		tabbed_pane.repaint ();
		//profiluri.setModel (new DefaultComboBoxModel<> (con.getAllProfil ()));
	}


	public void addQueryResult (String text)
	{
		query_result_panel.removeAll ();	
		query_result_panel.revalidate ();
		query_result_panel.repaint ();
        query_result_panel.add (new JScrollPane (new JTextArea (text)));
	}


	public String getNumeElev() 
	{
		return nume_elev.getText ().trim ();
	}


	public String getPrenumeElev() 
	{
		return prenume_elev.getText ().trim ();
	}
	

	public String getCnpElev() 
	{
		return cnp_elev.getText ().trim ();
	}


	public String getEtnieElev() 
	{
		return etnie_elev.getText ().trim ();
	}


	public String getNationalotateElev() 
	{
		return nationalitate_elev.getText ().trim ();
	}


	public String getAdresaElev() 
	{
		return adresa.getText ().trim ();
	}


	public java.util.Date getDataNasterii ()
	{
		return 	dataNasterii.getDate ();
	}


	public int getClasaComboBoxSelectedID ()
	{
		return ((ClasaDataModel) clase.getSelectedItem ()).getID ();
	}


	public int getAnScolar ()
	{
		return java.util.Calendar.getInstance ().get(java.util.Calendar.YEAR);
	} 

	
	public String getCodClasa ()
	{
		return cod.getText ().trim ();
	}

	
	public int getAnStudiu ()
	{
		return ((Integer)cbAnStudiu.getSelectedItem ()).intValue ();
	}
	
	public int getComboBoxSelectedID ()
	{
		return ((ProfilDataModel) profiluri.getSelectedItem ()).getID ();
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


	public Vector <Vector <MaterieDataModel>> getMateriiIDs ()
	{
		Vector <Vector <MaterieDataModel>> ret = null;

		for (DefaultListModel <MaterieDataModel> m : materii_an_scolar) {
		
			Vector <MaterieDataModel> materii_an = new Vector <>();
			for (java.util.Enumeration <MaterieDataModel> x = m.elements ();
					x.hasMoreElements (); ) 
			{
				materii_an.add (x.nextElement ());	
			}
			if (ret == null) {
				ret = new Vector <>();
			}
			ret.add (materii_an);
		}
		return ret;
	}


	public Vector <Object> getTableClasaUpdate ()
	{
		JPanel insertPanel = new JPanel (new GridBagLayout ());
		insertPanel.setBorder (
			new TitledBorder (new EtchedBorder (), "Update")
		);	

		GridBagConstraints c = new GridBagConstraints ();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		JLabel lbl_clasa_profil = new JLabel ("Profil");
		insertPanel.add (lbl_clasa_profil, c);

		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 0;	
		c.insets = new Insets (1, 1, 1, 1);
		JComboBox <ProfilDataModel> profiluri = new JComboBox <> ();
		profiluri.setModel (new DefaultComboBoxModel<> (prof));
		insertPanel.add (profiluri, c);

		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 1;	
		c.insets = new Insets (1, 1, 1, 1);
		JLabel lbl_an_scolar = new JLabel ("An");
		insertPanel.add (lbl_an_scolar, c);

		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 1;	
		c.insets = new Insets (1, 1, 1, 1);
		JTextField an_scolar = new JTextField ();				
		insertPanel.add (an_scolar, c);

		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 2;		
		c.insets = new Insets (1, 1, 1, 1);
		JLabel lbl_cod = new JLabel ("Cod");
		insertPanel.add (lbl_cod, c);

		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 2;	
		c.insets = new Insets (1, 1, 1, 1);
		JTextField cod = new JTextField ();
		insertPanel.add (cod, c);

		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 3;		
		c.insets = new Insets (1, 1, 1, 1);
		JLabel lbl_clasa = new JLabel ("An studiu");
		insertPanel.add (lbl_clasa, c);

		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 3;	
		c.gridwidth = 2;
		c.insets = new Insets (1, 1, 1, 1);
		insertPanel.add (cbAnStudiu, c);

		showInputDialog (insertPanel);
		
		return null;	

	}


	public Vector <Vector <Object>> getTableMaterieUpdate ()
	{
		int[] indices = table_materie.getSelectedRows ();

		if (indices.length == 0) {
			return null;
		} 

		Vector <Vector <Object>> vals = null;
		MaterieTableModel model = (MaterieTableModel)table_materie.getModel ();
		String inp;
		int index;

		for (int i : indices) {
			index = table_materie.convertRowIndexToModel (i);
	
			inp = showInputDialog ("Enter new value: " + model.getRow (index));
			if (inp == null) {
				continue;
			}
			do 
			 {
				if (inp != null) {
					inp = inp.trim ();
					try {	
						MaterieDataModel.checkNumeMaterie (inp);
						Vector <Object> vec = new Vector <>();
						vec.add (model.getValueAt (index, 0)); // id
						vec.add (inp);
						if (vals == null) {
							vals = new Vector <>();
						}
						vals.add (vec); 
						break;
	
					} catch (DataModelTypeMismatchError dtmErr) {
						displayError (dtmErr.getMessage ());	
					}
					
				} 
				inp = showInputDialog ("Enter a valid value: " + model.getRow (index));
				if (inp == null) {
					break;
				}

			} while (true);
		}

		return vals;
	}


	public Vector <Integer> getTableElevSelectedIDs (boolean confirmEachDelete)
	{
		int[] indices = table_elev.getSelectedRows ();
		
		if (indices.length == 0) {
			return null;
		}
	
		Vector <Integer> selectedIDs = null;

		ElevTableModel model = (ElevTableModel)table_elev.getModel ();

		for (int i : indices) {
			int index = table_elev.convertRowIndexToModel (i);			
			ElevDataModel row = ElevTableModel.getRow (index);
		
			if (confirmEachDelete && 	
				displayConfirmation ("Are you sure you want to delete: " + row) == YES_OPTION)
			{
				if (selectedIDs == null) {
					selectedIDs = new Vector <>();
				}
				selectedIDs.add ((Integer)model.getValueAt (index, 0));
			}
		}	
		return selectedIDs;
	}


	public Vector <Integer> getTableMaterieSelectedIDs (boolean confirmEachDelete)
	{
		int[] indices = table_materie.getSelectedRows ();
		
		if (indices.length == 0) {
			return null;
		}
	
		Vector <Integer> selectedIDs = null;

		MaterieTableModel model = (MaterieTableModel)table_materie.getModel ();

		for (int i : indices) {
			int index = table_materie.convertRowIndexToModel (i);			
			String str = model.getRow (index);
		
			if (confirmEachDelete && 	
				displayConfirmation ("Are you sure you want to delete: " + str) == YES_OPTION)
			{
				if (selectedIDs == null) {
					selectedIDs = new Vector <>();
				}
				selectedIDs.add ((Integer)model.getValueAt (index, 0));
			}
		}	
		return selectedIDs;
	}


	public Vector <Integer> getTableClasaSelectedIDs (boolean confirmEachDelete)
	{
		int[] indices = table_clasa.getSelectedRows ();
		
		if (indices.length == 0) {
			return null;
		}
	
		Vector <Integer> selectedIDs = null;

		ClasaTableModel model = (ClasaTableModel)table_clasa.getModel ();

		for (int i : indices) {
			int index = table_clasa.convertRowIndexToModel (i);			
			String str = model.getRow (index);
		
			if (confirmEachDelete && 	
				displayConfirmation ("Are you sure you want to delete: " + str) == YES_OPTION)
			{
				if (selectedIDs == null) {
					selectedIDs = new Vector <>();
				}
				selectedIDs.add ((Integer)model.getValueAt (index, 0));
			}
		}	
		return selectedIDs;
	}


	public Vector <Integer> getTableProfilSelectedIDs (boolean confirmEachDelete)
	{
		int[] indices = table_profil.getSelectedRows ();
			
		if	(indices.length == 0) {
			return null;
		}
	
		Vector <Integer> selectedIDs = null;

		ProfilTableModel model = (ProfilTableModel)table_profil.getModel ();	

		for (int i : indices) {
			int index = table_profil.convertRowIndexToModel (i);
			
			String str = model.getRow (index);	

			if (confirmEachDelete && 	
				displayConfirmation ("Are you sure you want to delete: " + str) == YES_OPTION)
			{
				if (selectedIDs == null) {
					selectedIDs = new Vector <>();
				}
				selectedIDs.add ((Integer)model.getValueAt (index, 0));
			}
		}	
		return selectedIDs;
	}

	
	public int getBursaID ()
	{
		BursaDataModel mdl = (BursaDataModel) bursa.getSelectedItem ();
		return mdl.getID ();
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

		insert_materie.addActionListener (lsnr);
		delete_materie.addActionListener (lsnr);
		update_materie.addActionListener (lsnr);
		
		insert_profil.addActionListener (lsnr);
		update_profil.addActionListener (lsnr);
		delete_profil.addActionListener (lsnr);

		insert_clasa.addActionListener (lsnr);
		delete_clasa.addActionListener (lsnr);
		
		insert_elev.addActionListener (lsnr);
		delete_elev.addActionListener (lsnr);		

		bursaFilter.addActionListener (lsnr);		
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
		JTable table = new JTable (tableModel);
		table.setAutoCreateRowSorter (true);
		if (!multipleSelection) {
			table.setSelectionMode (0);
		}
		table.setSelectionBackground (new Color (0xea, 0xee, 0xaf));
		table.setSelectionForeground (new Color (0x22, 0x22, 0x22));
        table.setRowHeight (table.getRowHeight () + 10);
		return table;
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
