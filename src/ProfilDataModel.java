import java.sql.*;


class ProfilDataModel {

	private int id_profil;	
	private String nume_profil;


	public ProfilDataModel (int id_profil, String nume_profil)
	{
		this.id_profil = id_profil;
		this.nume_profil = nume_profil;
	}	


	public ProfilDataModel (ResultSet r) 
		throws SQLException 
	{
		id_profil = r.getInt (1);
		nume_profil = r.getString (2);
	}	


	public Object[] get ()
	{
		return new Object[]{id_profil, nume_profil};
	}

	/*	
	public static void main (String[] args)
	{
		JFrame fr = new JFrame ("DEMOE");
		fr.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		fr.setBounds (300, 300, 400, 400);
		fr.setLayout (new BorderLayout ());

		JTabbedPane tab = new JTabbedPane ();	
		fr.add (tab);
	
		JPanel panel1 = new JPanel (new BorderLayout ());
		JPanel panel2 = new JPanel (new BorderLayout ());

		tab.add ("panou 1", panel1);
		tab.add ("panou 2", panel2);
	
		AbstractTableModel model1 = new ProfilDataModel ();		
		JTable table1 = new JTable ();	
		table1.setModel (model1);	
		JScrollPane scrollPane1 = new JScrollPane (table1);	
		panel1.add (scrollPane1, BorderLayout.CENTER);

		AbstractTableModel model2 = new ProfilDataModel ();		
		JTable table2 = new JTable ();	
		table2.setModel (model1);	
		JScrollPane scrollPane2 = new JScrollPane (table2);	
		panel2.add (scrollPane2, BorderLayout.CENTER);
		
		JButton click1 = new JButton ("DELETE");
		click1.addActionListener (e -> {
			((ProfilDataModel )table1.getModel ()).removeRow (0);	
		});
		
		JPanel pnl1 = new JPanel ();

		JButton click2 = new JButton ("PUT");
		click2.addActionListener (e -> {
			Vector <ProfilDataModel> v = new Vector <> ();
			
			for (int i = 1; i < 10; ++i)
			  {
				for (int j=1; j<=2; ++j)
				  {
					v.add (new ProfilDataModel (i, "Profil " + i + " " + j));
				  } 
			  }	
			((ProfilDataModel )table1.getModel ()).setData (v);	
		});

	
		JTextField search = new JTextField (10);				
	
		TableRowSorter <AbstractTableModel> trs = 
				new TableRowSorter <> (model1);
	
		table1.setRowSorter (trs);
		
		KeyListener klsnr = new KeyListener () {
			public void keyTyped (KeyEvent e) 
			{
			}	
			public void keyPressed (KeyEvent e) 
			{	
			}
			public void keyReleased (KeyEvent e) 
			{
				
				RowFilter <Object, Object> filter = 
 					new RowFilter <Object, Object> () {
						public boolean include (Entry entry) 
						{
							Integer p = (Integer) entry.getValue (0);
							System.out.println ("CE MA ? " + p.intValue ());
							return p.intValue () > 5;		
						}					
					};	

				String inputText = search.getText ().trim ();

				if (inputText.length () > 0) {
					//trs.setRowFilter (filter);
					trs.setRowFilter (RowFilter.regexFilter (
						inputText + ".*", 0));	
				}
				else {
					trs.setRowFilter (null);
				}
			}
		};
	
		search.addKeyListener (klsnr);
	
		pnl1.add (click1);
		pnl1.add (click2);	
		pnl1.add (search);

		panel1.add (pnl1, BorderLayout.SOUTH);
		
		fr.setVisible (true);
	}
	*/

	/*
	public static void main (String[] args)
		throws DriverNotFoundException, ConnectionErrorException
	{
		MyConnection con = 
			new MyConnection ("jdbc:oracle:thin:@localhost:1521:XE", "adrian", "biblioteca");

		DefaultTableModel dtm = ProfilDataModel.getTableModel ();

		for (ProfilDataModel o : con.getAllProfil ()) {
			dtm.addRow (o.get ());
		}
					
		
		JFrame frame = new JFrame ("Data Table Demo");
		frame.setSize (800, 800);
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	
		JTable table = new JTable (dtm);
		table.setRowHeight (table.getRowHeight () + 10);
//		table.setFont (new Font ("Serif", Font.PLAIN, 20));
		table.setAutoCreateRowSorter (true);
//		table.setAutoResizeMode (JTable.AUTO_RESIZE_OFF);

		JScrollPane scrollPane = new JScrollPane (table);
		JPanel panel = new JPanel ();
		panel.setLayout (new BorderLayout ());	
		panel.add (scrollPane, BorderLayout.CENTER);

		frame.add (panel);
		frame.setVisible (true);	
	}
	*/


	public int getID ()
	{
		return id_profil;
	}
	

	public String getNumeProfil ()
	{
		return nume_profil;
	}

	
	public String toString()
	{
		return nume_profil;
	} 

}
