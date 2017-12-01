import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;

class ClasaDataModel {		
	
	private static DefaultTableModel dTableModel = null;

	//private static Vector<ClasaDataModel> data = null;

	private static Object[] columns = new Object[]{
		"id_clasa", "id_profil", "an_scolar", "cod", "clasa"
	};

	private int id_clasa;
	private int id_profil;
	private int an_scolar;
	private String cod;
	private int clasa;


	public ClasaDataModel (
		int id_clasa, int id_profil, int an_scolar, String cod, int clasa)
	{
		this.id_clasa = id_clasa;
		this.id_profil = id_profil;
		this.an_scolar = an_scolar;
		this.cod = cod;
		this.clasa = clasa;
	}


	public ClasaDataModel (ResultSet r) 
		throws SQLException 
	{
		id_clasa = r.getInt (1);
		id_profil = r.getInt (2);
		an_scolar = r.getInt (3);
		cod = r.getString (4);
		clasa = r.getInt (5);
	}	


	public Object[] get ()
	{
		return new Object[]{id_clasa, id_profil, an_scolar, cod, clasa};
	}


    public static DefaultTableModel getTableModel ()
    {
        if (dTableModel == null) {
            dTableModel = new DefaultTableModel () {	
				public static final long serialVersionUID = 1L;	
                public Class<?> getColumnClass (int column)
                {
                    Class<?> returnValue;
                    if ((column >= 0) && (column < getColumnCount ())) {
                        returnValue  = getValueAt (0, column).getClass ();
                    } else {
                        returnValue = Object.class;
                    }
                    return returnValue;
                }
                public boolean isCellEditable (int row, int column) {
                    return false;
                }
            };

            dTableModel.addTableModelListener (
                new javax.swing.event.TableModelListener () {
                public void tableChanged (javax.swing.event.TableModelEvent e) {
                    System.out.println ("Modificare !!");
                }
            });

            for (Object columnName : columns) {
                dTableModel.addColumn (columnName);
            }
        }
        return dTableModel;
    }


	/*
	public static Vector<ClasaDataModel> getAllClase (MyConnection con)
    {
		if (data == null) {
 	       	try {
        		ResultSet rs;
				Statement st = con.getNewStatement ();
            	rs = st.executeQuery ("SELECT * FROM clasa");
				data = new java.util.Vector<> ();

				ResultSetMetaData metaData;

            	while (rs.next ()) {
					data.add (new ClasaDataModel);
            	}
        	}
        	catch (SQLException sqlex) {
				System.out.println (sqlex.getMessage ());
        	}
		}
        return data;
    }
	*/

	/*
	public static void init (MyConnection connection)
	{
		dTableModel = new DefaultTableModel () {
			public Class getColumnClass (int column)
			{
				Class returnValue;	
				if ((column >= 0) && (column < getColumnCount ())) {
					returnValue	 = getValueAt (0, column).getClass ();
				} else {
					returnValue = Object.class;
				}
				return returnValue;
			}
		};
		
		try {	
			Statement stm = connection.getNewStatement ();	
			ResultSet rows = stm.executeQuery ("SELECT * FROM elev");
			ResultSetMetaData metaData = rows.getMetaData ();
		
			int m = metaData.getColumnCount ();
	
			columns = new String [m + 1];
	
			for (int i = 1; i <= m; ++ i) {
				columns [i] = metaData.getColumnName (i);		
				dTableModel.addColumn (metaData.getColumnName (i));
			}

			Object[] tempRow;

			while (rows.next ()) {	
	
				tempRow =  new Object[]{
						rows.getInt(1), rows.getString(2), rows.getString(3),
						rows.getString(4), rows.getString(5),
						rows.getString(6), rows.getString(7)
				};

				dTableModel.addRow (tempRow);
		
			}
		}
		catch (SQLException sqlex) {		
			System.out.println (sqlex.getMessage ());
		}
	}
	*/


	public static void main (String[] args)
		throws DriverNotFoundException, ConnectionErrorException
	{
		MyConnection con = 
			new MyConnection ("jdbc:oracle:thin:@localhost:1521:XE", "adrian", "biblioteca");

		DefaultTableModel dtm = ClasaDataModel.getTableModel ();

		for (ClasaDataModel o : con.getAllClase ()) {
			dtm.addRow (o.get ());
		}
					
		
		
		JFrame frame = new JFrame ("Data Table Demo");
		frame.setSize (800, 800);
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	
		JTable table = new JTable (dtm);
		table.setRowHeight (table.getRowHeight () + 10);
		table.setFont (new Font ("Serif", Font.PLAIN, 20));
		table.setAutoCreateRowSorter (true);
//		table.setAutoResizeMode (JTable.AUTO_RESIZE_ON);

		JScrollPane scrollPane = new JScrollPane (table);
		JPanel panel = new JPanel ();
		panel.setLayout (new BorderLayout ());	
		panel.add (scrollPane, BorderLayout.CENTER);

		frame.add (panel);
		frame.setVisible (true);
		
	}

	
	public int getID () 
	{
		return id_clasa;
	}


	public int getIdProfil () 
	{
		return id_profil;
	}


	public int getAnScolar () 
	{
		return an_scolar;
	}


	public String getCod () 
	{
		return cod;
	}


	public int getClasa () 
	{
		return clasa;
	}

	
	public String toString ()
	{
		return this.id_profil + " " +
			this.an_scolar + " " + 
			this.cod + " " +
			this.clasa;
	}

}
