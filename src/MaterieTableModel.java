import javax.swing.table.AbstractTableModel;
import java.util.Vector;
import java.util.Arrays;


class MaterieTableModel extends AbstractTableModel {

	public static final long serialVersionUID = 0xad1;		

	public Vector <MaterieDataModel> data = null;	

	public static final Vector <String> column =
		new Vector <String> (Arrays.asList (
			new String[] {
				"id_materie", "nume_materie"
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
	

	@Override
	public String getColumnName (int col)
	{
		return column.get (col);
	}

	
	@Override
	public Object getValueAt (int row, int col)
	{
		if (this.data != null) 
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
		
			System.out.println ("Linia a fost stearsa...");
			data.remove (row);			
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
		return data != null ? data.size () : 0;
	}

	
	public void setData (Vector <MaterieDataModel> data)
	{
		this.data = data;
		fireTableDataChanged ();
	}
}
