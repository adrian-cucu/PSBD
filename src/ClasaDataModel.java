import javax.swing.table.*;
import java.util.Calendar;
import java.util.Vector;
import java.util.Arrays;
import java.sql.*;

class ClasaDataModel {		

    public static final java.util.Vector <String> colName =
        new java.util.Vector <String> (java.util.Arrays.asList (
            new String[] {
                "id_clasa", "id_profil", "an_scolar", "cod", "an_studiu"
            }
        ));

    public static final java.util.Vector <Class <?>> colType =
        new java.util.Vector <Class <?>> (java.util.Arrays.asList (
            new Class <?>[] {
                Integer.class, Integer.class, Integer.class,
				String.class, Integer.class
           }
        ));	

	private int id_clasa;
	private int id_profil;
	private String nume_profil = null;
	private int an_scolar;
	private String cod = null;
	private int an_studiu;

	public ClasaDataModel (int id_clasa, int id_profil, int an_scolar, String cod, int an_studiu)
	{
		this.id_clasa = id_clasa;
		this.id_profil = id_profil;
		this.an_scolar = an_scolar;
		this.cod = cod;
		this.an_studiu = an_studiu;
	}


	public ClasaDataModel (ResultSet r) 
		throws SQLException 
	{
		id_clasa = r.getInt (1);
		id_profil = r.getInt (2);
		an_scolar = r.getInt (3);
		cod = r.getString (4);
		an_studiu = r.getInt (5);
	}	

	
	public void setNumeProfil (String nume_profil)
	{
		this.nume_profil = nume_profil;
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


	public int getAnStudiu () 
	{
		return an_studiu;
	}


	@Override	
	public String toString ()
	{
		return (nume_profil == null ? "" : nume_profil + " ") + an_studiu + cod + " " + an_scolar;
	}


	public static Vector <Object> make (ResultSet r)
		throws SQLException
	{
		if (r == null) {
			return null;
		}

		int id_clasa = r.getInt (1);
		int id_profil = r.getInt (2);
		int an_scolar = r.getInt (3);
		String cod = r.getString (4);
		int an_studiu = r.getInt (5);

		Vector <Object> rowData = new Vector <Object> (5);
		rowData.add (id_clasa);
		rowData.add (id_profil);
		rowData.add (an_scolar);
		rowData.add (cod);
		rowData.add (an_studiu);
		return 	rowData;
	}


	public static ClasaDataModel make (Vector <Object> r)
	{
		if (r == null) {
			return null;
		}

		int id_clasa = ((Integer) r.get (0)).intValue ();
		int id_profil = ((Integer) r.get (1)).intValue ();
		int an_scolar = ((Integer) r.get (2)).intValue ();
		String cod = (String) r.get (3);
		int an_studiu = ((Integer) r.get (4)).intValue ();

		return new ClasaDataModel (
			id_clasa,
			id_profil,
			an_scolar,
			cod,
			an_studiu
		);
	}


	public static boolean checkAnScolar (int an_scolar) 
		throws DataModelTypeMismatchError
	{
		if (an_scolar < 1900 || an_scolar > Calendar.getInstance ().get(Calendar.YEAR) + 1) {
			throw new DataModelTypeMismatchError ("Anul scolar este invlid");
		}
		return true;
	}


	public static boolean checkCod (String cod) 
		throws DataModelTypeMismatchError
	{
		if (cod == null || cod.isEmpty () || cod.length () > 5) {
			throw new DataModelTypeMismatchError ("Codul clasei este prea lung");
		}
		return true;
	}


	public static boolean checkAnStudiu (int an_studiu) 
		throws DataModelTypeMismatchError
	{
		if (12 < an_studiu) {
			throw new DataModelTypeMismatchError ("Anul de studiu este pentru extraterestrii");
		}
		if (9 > an_studiu) {
			throw new DataModelTypeMismatchError ("Se incepe cu clasa a 9 a");
		}	
		return true;
	}
}
