import javax.swing.table.AbstractTableModel;
import java.util.Vector;
import java.util.Arrays;

class ClasaTableModel extends AbstractTableModel {

	public static final long serialVersionUID = 0xad1;		
	public static Vector <Vector <Object>> data = null;
	public static Vector <ClasaTableModel> listeners = new Vector<>();
	
	public ClasaTableModel () 
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
		data = con.fetchTableClasaRawData ();
		notifyListeners ();
	}	


    public String getRow (int row)
    {
        if (row < data.size ())
            return data.get (row).toString ();
        return "";
    }

	
	private static void notifyListeners ()
	{
		for (ClasaTableModel listener : listeners)
			listener.fireTableDataChanged ();
	}

	

	@Override
	public Class<?> getColumnClass (int column) 
	{
		return ClasaDataModel.colType.get (column);
	}
	

	@Override
	public String getColumnName (int column)
	{
		return ClasaDataModel.colName.get (column);
	}

	
	@Override
	public boolean isCellEditable (int row, int column) 
	{
		return false;
	} 
		
	
	@Override
	public int getColumnCount ()
	{
		return ClasaDataModel.colName.size ();
	}


    @Override
    public Object getValueAt (int row, int column)
    {
        if (data != null) {
            return data.get (row).get(column);
     	}
		return null;
    }


    @Override
    public int getRowCount ()
    {
        return data != null ? data.size () : 0;
    }
}
