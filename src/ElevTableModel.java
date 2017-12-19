import javax.swing.table.AbstractTableModel;
import java.util.Vector;
import java.util.Arrays;

class ElevTableModel extends AbstractTableModel {

	public static final long serialVersionUID = 0xad1;		
	public static Vector <Vector <Object>> data = null;	
	public static Vector <ElevTableModel> listeners = new Vector <> ();

	public ElevTableModel ()
	{
		super ();
		listeners.add (this);
	}
	
	
	public void setData (Vector <Vector <Object>> datas)
	{
		data = datas;
		notifyListeners ();
	}

	
	public static void refresh (MyConnection con)
	{
		data.clear ();
		data = con.fetchTableElevRawData ();
		notifyListeners ();
	}

		
	public static ElevDataModel getRow (int row)
	{
		if (row < data.size ()) {
			return ElevDataModel.makeObj (data.get (row));
		}
		return null;
	}

	
	private static void notifyListeners ()
	{
		for (ElevTableModel listener : listeners)
			listener.fireTableDataChanged ();
	}	
	

	@Override
	public Class<?> getColumnClass (int column) 
	{
		return ElevDataModel.colType.get (column);
	}
	

	@Override
	public String getColumnName (int column)
	{
		return ElevDataModel.colName.get (column);
	}

	
	@Override
	public boolean isCellEditable (int row, int column) 
	{
		return column != 0 && column != 4;
	} 
		

	@Override
	public int getColumnCount ()
	{
		return ElevDataModel.colName.size ();
	}

	@Override
	public void setValueAt (Object aValue, int row, int column) 
	{
		if (isCellEditable (row, column)) {
			data.get (row).set (column, aValue);
		}
	}
	
	@Override
	public Object getValueAt (int row, int column)
	{
		if (data != null) {
			return data.get (row).get (column);
		}	
		return null;
	}	


	@Override
	public int getRowCount ()
	{
		return data != null ? data.size () : 0;
	}
}
