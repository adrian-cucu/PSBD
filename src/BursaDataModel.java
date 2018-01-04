import javax.swing.table.*;
import java.util.Calendar;
import java.util.Vector;
import java.util.Arrays;
import java.lang.Class;
import java.sql.*;

class BursaDataModel {		

    public static final Vector <String> colName =
		new Vector <String> (Arrays.asList (
            new String[] {
                "id_bursa", "tip_bursa", "valoare"
            }
        ));

    public static final Vector <Class <?>> colType =
		new Vector <Class <?>> (Arrays.asList (
            new Class <?>[] {
                Integer.class, String.class, Double.class
           }
        ));	

	private int id_bursa;
	private String tip_bursa = null;
	private double valoare;

	public BursaDataModel (int id_bursa, String tip_bursa, double valoare)
	{
		this.id_bursa = id_bursa;
		this.tip_bursa = tip_bursa;
		this.valoare = valoare;
	}


	public BursaDataModel (ResultSet r) 
		throws SQLException 
	{
		id_bursa = r.getInt (1);
		tip_bursa = r.getString (2);
		valoare = r.getDouble (3);
	}	


	public int getID () 
	{
		return id_bursa;
	}


	public String getNumeBursa () 
	{
		return tip_bursa;
	}


	public double getValoare ()
	{
		return valoare;
	}
	

	@Override	
	public String toString ()
	{
		return tip_bursa;
	}


	public static Vector <Object> make (ResultSet r)
		throws SQLException
	{
		if (r == null) {
			return null;
		}

		int id_bursa = r.getInt (1);
		String tip_bursa = r.getString (2);
		double valoare = r.getDouble (3);	

		Vector <Object> rowData = new Vector <Object> (2);
		rowData.add (id_bursa);
		rowData.add (tip_bursa);
		rowData.add (valoare);
		return 	rowData;
	}


	public static BursaDataModel make (Vector <Object> r)
	{
		if (r == null) {
			return null;
		}

		int id_bursa = ((Integer) r.get (0)).intValue ();
		String tip_bursa = (String) r.get (1);
		double valoare = ((Double) r.get (2)).doubleValue ();

		return new BursaDataModel (id_bursa, tip_bursa, valoare);
	}

	public static boolean checkAnStudiu (String tip_bursa) 
		throws DataModelTypeMismatchError
	{
		if (tip_bursa == null || tip_bursa.isEmpty () || tip_bursa.length () < 30) {
			throw new DataModelTypeMismatchError ("Se incepe cu clasa a 9 a");
		}
		return true;
	}
	
}
