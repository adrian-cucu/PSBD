import javax.swing.table.AbstractTableModel;
import java.util.Vector;
import java.util.Arrays;

class MedieTableModel extends AbstractTableModel {

	public static final long serialVersionUID = 0xad1;		
	private Vector <String> colIdentifiers = null;
	private Vector <Vector <Object>> data = null;	
	private static Vector <MedieTableModel> listeners = new Vector<>();	

	public MedieTableModel (Vector <String> colIdentifiers)
	{
		this.colIdentifiers = colIdentifiers;	
		data = new Vector <Vector <Object>>();
		data.add (new Vector <Object> ());
		data.add (new Vector <Object> ());
		data.add (new Vector <Object> ());
		listeners.add (this);
	}
	

	public void setData (int  row, Vector <Object> datas)
	{
		System.out.println (data.size ());
		data.set (row, datas);
		notifyListeners ();
	}
	

	/*
    public String getRow (int row)
    {
        if (row < data.size ())
            return data.get (row).toString ();
        return "";
    }
	*/

	/*	
	public static void addRow (ProfilDataModel newRow)
	{
		if (data == null) { 
			data = new Vector <>();
		}
		data.add (newRow); 
	}		
	*/
	
	public void notifyListeners ()
	{
		for (MedieTableModel listener : listeners)
			listener.fireTableDataChanged ();
	}	
	
	

	@Override
	public Class<?> getColumnClass (int column) 
	{
		return Object.class;
	}
	

	@Override
	public String getColumnName (int column)
	{
		return colIdentifiers.get (column);
	}

	
	@Override
	public Object getValueAt (int row, int column)
	{
		if (data != null) {
			if (row == 2) {
				int medie1 = -1;
				int medie2 = -1;

				Object med_sem1 = data.get (0).get (column);
				if (med_sem1 instanceof String) {
					medie1 = Integer.parseInt (data.get (0).get (column) + "");

				} else if (med_sem1 instanceof Integer) {
					medie1 = ((Integer)data.get (0).get (column)).intValue ();
				}

				Object med_sem2 = data.get (1).get (column);
				if (med_sem2 instanceof String) {
					medie2 = Integer.parseInt (data.get (1).get (column) + "");

				} else if (med_sem2 instanceof Integer) {
					medie2 = ((Integer)data.get (1).get (column)).intValue ();
				}




				if (medie1 != -1 && medie2 != -1) {
					return new Double ((medie1 + medie2) / 2.0f);
				}
				return "-";
			}			
		
			/*	
			if (data.get (row).get (column) == null ||
				((Integer) data.get (row).get (column)).intValue () == -1) {
				return "-";
			} 
			*/
			return data.get (row).get (column);	
		}	
		return null;
	}

	
	@Override
	public boolean isCellEditable (int row, int column) 
	{
		return row == 0 || row == 1;
	} 
		
	
	@Override
	public int getColumnCount ()
	{
		return colIdentifiers.size ();
	}
		
	
	@Override
	public int getRowCount ()
	{
		return data != null ? data.size () : 0;
	}

	@Override
	public void setValueAt (Object aValue, int row, int column)
	{
		System.out.println ("CALLED!!!!!!!!!!!!!!!!");
		if (isCellEditable (row, column)) {
			data.get (row).set (column, aValue);
		}
	}
}
