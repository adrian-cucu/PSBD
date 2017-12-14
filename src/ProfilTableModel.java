import javax.swing.table.AbstractTableModel;
import java.util.Vector;
import java.util.Arrays;

class ProfilTableModel extends AbstractTableModel {

	public static final long serialVersionUID = 0xad1;		
	public static Vector <ProfilDataModel> data = null;	
	public static Vector <ProfilTableModel> listeners = new Vector<>();	

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
		data = con.fetchTableProfilObjData ();
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
		return ProfilDataModel.colType.get (column);
	}
	

	@Override
	public String getColumnName (int column)
	{
		return ProfilDataModel.colName.get (column);
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
		return ProfilDataModel.colType.size ();
	}
		
	
	@Override
	public int getRowCount ()
	{
		return data != null ? data.size () : 0;
	}
}
