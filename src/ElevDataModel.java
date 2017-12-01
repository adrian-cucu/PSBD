import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;
import java.util.*;

class ElevDataModel {

	private static DefaultTableModel dTableModel = null;

	private static Object[] columns = new Object[]{
		"id_elev", "nume", "prenume", "adresa", "cnp", "etnie", "nationalitate"
	};

	private int id_elev;
	private String nume;
	private String prenume;
	private String adresa;
	private String cnp;
	private String etnie;
	private String nationalitate;


	public ElevDataModel (String nume, String prenume, String adresa,
						String cnp, String etnie, String nationalitate)
	{
		this.nume = nume;
		this.prenume = prenume;
		this.adresa = adresa;
		this.cnp = cnp;
		this.etnie = etnie;
		this.nationalitate = nationalitate;
	}


	public ElevDataModel (ResultSet set) 
		throws SQLException 
	{
		id_elev = set.getInt ("id_elev");
		nume = set.getString ("nume");
		prenume = set.getString ("prenume");
		adresa = set.getString ("adresa");
		cnp = set.getString ("cnp");
		etnie = set.getString ("etnie");
		nationalitate = set.getString ("nationalitate");
	}	

	
	public Object[] get ()
	{
		return new Object[]{
			id_elev, nume, prenume, adresa, cnp, etnie, nationalitate
		};
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


	public int getID () 
	{
		return id_elev;
	}		


	public String getNume () 
	{
		return nume;
	}		


	public String getPren () 
	{
		return prenume;
	}

			
	public String getAdresa () 
	{
		return adresa;
	}		


	public String getCnp () 
	{
		return cnp;
	}		


	public String getEtnie () 
	{
		return etnie;
	}


	public String getNat () 
	{
		return nationalitate;		
	}


	public String toString () 
	{
		return this.getNume () + " " + 
			this.getPren () + "  " + 
			this.getAdresa () + " " +
			this.getCnp () + " " + 
			this.getEtnie () + " " + 
			this.getNat ();
	}


	public static void main (String[] args)
		throws DriverNotFoundException, ConnectionErrorException
	{
		MyConnection con = 
			new MyConnection ("jdbc:oracle:thin:@localhost:1521:XE", "adrian", "biblioteca");

		DefaultTableModel dtm = ElevDataModel.getTableModel ();

		Vector <ElevDataModel> data = con.getAllElevi ();

		for (ElevDataModel o : data) {
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
}
