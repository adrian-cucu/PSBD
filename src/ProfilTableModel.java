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

	public static final long serialVersionUID = 0xad1;		

	public static Vector <ProfilDataModel> data = null;	

	public static final Vector <String> columns =
		new Vector <String> (Arrays.asList (
			new String[] {
				"id_profil", "nume_profil"
			}
		));	

	public static Vector <ProfilTableModel> listeners = new Vector <> ();
	

	public ProfilTableModel ()
	{
		super ();
		listeners.add (this);
	}
	
	
	public void setData (Vector <ProfilDataModel> datas)
	{
		data = datas;
		notifyListeners ();
	}


    public String getRow (int row)
    {
        if (row < data.size ())
            return data.get (row).toString ();
        return "";
    }

	
	public static void refresh (MyConnection con)
	{
		data.clear ();
		data = con.fetchProfil ();
		notifyListeners ();
	}


	public static void addRow (ProfilDataModel newRow)
	{
		if (data == null) { 
			data = new Vector <>();
		}
		data.add (newRow); 
	}		

	
	private static void notifyListeners ()
	{
		for (ProfilTableModel listener : listeners)
			listener.fireTableDataChanged ();
	}	

	

	@Override
	public Class<?> getColumnClass (int column) 
	{
		Class <?> type = Object.class;
		if (column == 0) {
			return Integer.class;
		}
		if (column == 1) {
			return String.class;
		}

		return type;
	}
	

	@Override
	public String getColumnName (int column)
	{
		return columns.get (column);
	}

	
	@Override
	public Object getValueAt (int row, int column)
	{
		if (data != null) {	
			if (column == 0) 
				return data.get (row).getID ();
			if (column == 1)
				return data.get (row).getNumeProfil ();
		}	
		return null;
	}

	
	@Override
	public boolean isCellEditable (int row, int column) 
	{
		return false;
	} 
		
	
	@Override
	public int getColumnCount ()
	{
		return columns.size ();
	}
		
	
	@Override
	public int getRowCount ()
	{
		return data != null ? data.size () : 0;
	}
}
