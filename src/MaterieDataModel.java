import java.util.Vector;
import java.sql.*;

class MaterieDataModel {

	private int id_materie;	
	private String nume_materie;


	public MaterieDataModel (int id_materie, String nume_materie)
	{
		this.id_materie = id_materie;
		this.nume_materie = nume_materie;
	}	


	public MaterieDataModel (ResultSet r) 
		throws SQLException 
	{
		id_materie = r.getInt (1);
		nume_materie = r.getString (2);
	}


    public static Vector <Object> make (ResultSet r)
        throws SQLException
    {
        if (r == null) {
            return null;
        }

        int id_materie = r.getInt (1);
		String nume_materie = r.getString (2);

        Vector <Object> rowData = new Vector <Object> (2);

        rowData.add (id_materie);
        rowData.add (nume_materie);
        return rowData;
    }


	public int getID ()
	{
		return id_materie;
	}
	

	public String getNumeMaterie ()
	{
		return nume_materie;
	}

	
	@Override
	public String toString()
	{
		return nume_materie;
	} 


	public static boolean checkNumeMaterie (String nume_materie) 
		throws DataModelTypeMismatchError
	{
		if (nume_materie == null || 
			nume_materie.isEmpty () ||		
			nume_materie.length () > 50 || 		
			!nume_materie.matches ("^[A-Za-z ]+$")) {
			throw new DataModelTypeMismatchError ("Numele materiei este invalid");
		}	
		return true;
	}
}
