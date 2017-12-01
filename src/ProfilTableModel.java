import javax.swing.table.AbstractTableModel;
import java.util.Vector;
import java.util.Arrays;

import static javax.swing.JOptionPane.*;
import javax.swing.table.*;
import java.util.HashMap;
import java.awt.*;
import javax.swing.border.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;
import java.awt.event.*;


class ProfilTableModel extends AbstractTableModel {

	public static final long serialVersionUID = 0xAD1L;		

	public Vector <ProfilDataModel> date = null;	

	public static final Vector <String> column =
		new Vector <String> (Arrays.asList (
			new String[] {
				"id_profil", "nume_profil"
			}
		));	


	@Override
	public Class<?> getColumnClass (int col) 
	{
		if (col == 0) {
			return Integer.class;
		}
		if (col == 1) {
			return String.class;
		}

		return Object.class;
	}
	

	public void setData (Vector <ProfilDataModel> data)
	{
		this.date = data;
		fireTableDataChanged ();	
	}


	@Override
	public String getColumnName (int col)
	{
		return column.get (col);
	}

	
	@Override
	public Object getValueAt (int row, int col)
	{
		if (this.date != null) 
		{	
			if (col == 0) {
				return date.get (row).getID ();
			}
			if (col == 1) {
				return date.get (row).getNumeProfil ();
			}
			
		}	
		return null;
	}

	
	@Override
	public boolean isCellEditable (int row, int col) 
	{
		return false;
	} 
	

	public void removeRow (int row)
	{
		if (row < getRowCount ()) {
		
			System.out.println ("Linia a fost stearsa...");
			date.remove (row);			
			fireTableRowsDeleted (0, 0);
		} 
		else {
	
			System.out.println ("Nu");
		}
	}	
		
	
	@Override
	public int getColumnCount ()
	{
		return column.size ();
	}
		
	
	@Override
	public int getRowCount ()
	{
		return date != null ? date.size () : 0;
	}

	
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
	
		AbstractTableModel model1 = new ProfilTableModel ();		
		JTable table1 = new JTable ();	
		table1.setModel (model1);	
		JScrollPane scrollPane1 = new JScrollPane (table1);	
		panel1.add (scrollPane1, BorderLayout.CENTER);

		AbstractTableModel model2 = new ProfilTableModel ();		
		JTable table2 = new JTable ();	
		table2.setModel (model1);	
		JScrollPane scrollPane2 = new JScrollPane (table2);	
		panel2.add (scrollPane2, BorderLayout.CENTER);
		
		JButton click1 = new JButton ("DELETE");
		click1.addActionListener (e -> {
			((ProfilTableModel )table1.getModel ()).removeRow (0);	
		});
		
		JPanel pnl1 = new JPanel ();

		JButton click2 = new JButton ("PUT");
		click2.addActionListener (e -> {
			Vector <ProfilDataModel> v = new Vector <> ();
			
			for (int i = 1; i < 10; ++i)
				for (int j=1; j<=2; ++j)
					v.add (new ProfilDataModel (i, "Profil " + i + " " + j));
				 
			((ProfilTableModel )table1.getModel ()).setData (v);	
		});	


		JTextField searchID = new JTextField (10);				
		JTextField searchName = new JTextField (10);				

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
				/*
				RowFilter <Object, Object> filter = 
 					new RowFilter <Object, Object> () {
						public boolean include (Entry entry) 
						{
							Integer p = (Integer) entry.getValue (0);
							return p.intValue () > 5;		
							
						}					
					};	
				*/


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


		searchID.addKeyListener (klsnr);
		searchName.addKeyListener (klsnr);
	
		pnl1.add (click1);
		pnl1.add (click2);	

		pnl1.add (searchID);
		pnl1.add (searchName);

		panel1.add (pnl1, BorderLayout.SOUTH);
		
		fr.setVisible (true);
	}


	
}	/*
	public static void main (String[] args)
		throws DriverNotFoundException, ConnectionErrorException
	{
		MyConnection con = 
			new MyConnection ("jdbc:oracle:thin:@localhost:1521:XE", "adrian", "biblioteca");

		DefaultTableModel dtm = ProfilTableModel.getTableModel ();

		for (ProfilTableModel o : con.getAllProfil ()) {
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
