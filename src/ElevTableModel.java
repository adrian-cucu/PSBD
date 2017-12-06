import javax.swing.table.AbstractTableModel;
import java.util.Vector;
import java.util.Arrays;

class ElevTableModel extends AbstractTableModel {

	public static final long serialVersionUID = 0xad1;		

	public static Vector <ElevDataModel> data = null;	

	public static final Vector <String> columns =
		new Vector <String> (Arrays.asList (
			new String[] {
				"id_elev", "nume", "prenume", "adresa", "cnp", "etnie", "nationalitate"
			}
		));	

	public static Vector <ElevDataModel> listeners = new Vector <> ();
	

	public ProfilTableModel ()
	{
		super ();
		listeners.add (this);
	}
	
	
	public void setData (Vector <ElevDataModel> datas)
	{
		data = datas;
		notifyListeners ();
	}

	
	public static void refresh (MyConnection con)
	{
		data.clear ();
		data = con.fetchProfil ();
		notifyListeners ();
	}


	public static void addRow (ElevDataModel newRow)
	{
		if (data == null) { 
			data = new Vector <>();
		}
		data.add (newRow); 
	}		

		
	public static String getRow (int row)
	{
		if (row < data.size ())
			return data.get (row).toString ();
		return "";
	}

	
	private static void notifyListeners ()
	{
		for (ProfilTableModel listener : listeners)
			listener.fireTableDataChanged ();
	}	

	

	@Override
	public Class<?> getColumnClass (int column) 
	{
		if (column == 0) {
			return Integer.class;
		}
		if (column == 1) {
			return String.class;
		}

		return Object.class;
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
