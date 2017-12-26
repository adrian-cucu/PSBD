import javax.swing.table.AbstractTableModel;
import java.util.Vector;
import java.util.Arrays;

class MaterieTableModel extends AbstractTableModel {

	public static final long serialVersionUID = 0xad1;		

	public static Vector <MaterieDataModel> data = null;	

	public static final Vector <String> column =
		new Vector <String> (Arrays.asList (
			new String[] {
				"id_materie", "nume_materie"
			}
		));	

    public static Vector <MaterieTableModel> listeners = new Vector <> ();


    public MaterieTableModel ()
    {
        super ();
        listeners.add (this);
    }


    public static void setData (Vector <MaterieDataModel> datas)
    {
        data = datas;
        notifyListeners ();
    }


    public static void refresh (MyConnection con)
    {
        data.clear ();
        data = con.fetchTableMaterieObjData ();
        notifyListeners ();
    }


    private static void notifyListeners ()
    {
        for (MaterieTableModel listener : listeners)
            listener.fireTableDataChanged ();
    }


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
	

	@Override
	public String getColumnName (int col)
	{
		return column.get (col);
	}

	
	@Override
	public Object getValueAt (int row, int col)
	{
		if (data != null) 
		{	
			if (col == 0) { return data.get (row).getID (); }
			if (col == 1) { return data.get (row).getNumeMaterie (); }
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
			data.remove (row);			
			fireTableRowsDeleted (0, 0);
		} 
	}	
		
	
	public MaterieDataModel get (int index)
	{
		return data.get (index);
	}


	public String getRow (int x)
	{
		return data.get (x).toString ();
	}

	
	@Override
	public int getColumnCount ()
	{
		return column.size ();
	}
		
	
	@Override
	public int getRowCount ()
	{
		return data != null ? data.size () : 0;
	}
}
