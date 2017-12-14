import java.util.Vector;
import java.sql.*;

class ProfilDataModel {

 	public static final java.util.Vector <String> colName =
        new java.util.Vector <String> (java.util.Arrays.asList (
            new String[] {
                "id_profil", "nume_profil"
            }
        ));

 	public static final java.util.Vector <Class <?>> colType =
        new java.util.Vector <Class <?>> (java.util.Arrays.asList (
            new Class <?>[] {
                Integer.class, String.class
           }
        ));

	private int id_profil;	
	private String nume_profil;

	public ProfilDataModel (int id_profil, String nume_profil)
	{
		this.id_profil = id_profil;
		this.nume_profil = nume_profil;
	}	


	public ProfilDataModel (java.sql.ResultSet r) 
		throws java.sql.SQLException 
	{
		id_profil = r.getInt (1);
		nume_profil = r.getString (2);
	}


	public static Vector <Object> make (ResultSet r)
        throws SQLException
    {
        if (r == null) {
            return null;
        }

        int id_profil = r.getInt (1);
        String nume_profil = r.getString (2);

        Vector <Object> rowData = new Vector <Object> (2);

        rowData.add (id_profil);
        rowData.add (nume_profil);
      	return rowData;
    }


	public int getID ()
	{
		return id_profil;
	}
	

	public String getNumeProfil ()
	{
		return nume_profil;
	}


	@Override	
	public String toString()
	{
		return nume_profil;
	} 


	public static boolean checkNumeProfil (String nume_profil) 
		throws DataModelTypeMismatchError
	{
		if (nume_profil.isEmpty () || nume_profil.length () > 50 || !nume_profil.matches ("^[A-Za-z ]+$")) {
			throw new DataModelTypeMismatchError ("Numele profilului este invalid");
		}
		return true;
	}	
}
