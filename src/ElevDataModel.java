import java.util.Vector;
import java.sql.*;

class ElevDataModel {

    public static final java.util.Vector <String> colName =
        new java.util.Vector <String> (java.util.Arrays.asList (
            new String[] {
                "id_elev", "nume", "prenume", "adresa", "cnp",
				"etnie", "nationalitate"
            }
        ));

	public static final java.util.Vector <Class <?>> colType =
        new java.util.Vector <Class <?>> (java.util.Arrays.asList (
            new Class <?>[] {
                Integer.class, String.class, String.class,
                String.class, String.class, String.class, String.class
           }
        ));

	private int id_elev;
	private String nume = null;
	private String prenume = null;
	private String adresa = null;
	private String cnp = null;
	private String etnie = null;
	private String nationalitate = null;


	public ElevDataModel (String nume, String prenume, String adresa, String cnp, String etnie, String nationalitate)
	{
		this.nume = nume;
		this.prenume = prenume;
		this.adresa = adresa;
		this.cnp = cnp;
		this.etnie = etnie;
		this.nationalitate = nationalitate;
	}


	public ElevDataModel (int id_elev, String nume, String prenume, String adresa, String cnp, String etnie, String nationalitate)
	{
		this.id_elev = id_elev;
		this.nume = nume;
		this.prenume = prenume;
		this.adresa = adresa;
		this.cnp = cnp;
		this.etnie = etnie;
		this.nationalitate = nationalitate;
	}


	public ElevDataModel (ResultSet r) 
		throws SQLException 
	{
		id_elev = r.getInt ("id_elev");
		nume = r.getString ("nume");
		prenume = r.getString ("prenume");
		adresa = r.getString ("adresa");
		cnp = r.getString ("cnp");
		etnie = r.getString ("etnie");
		nationalitate = r.getString ("nationalitate");
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

	
	public String getNumeComplet ()
	{
		return prenume + " "  + nume;
	}


	@Override
	public String toString () 
	{
		return nume + " " + prenume + "  " + adresa + " " + cnp + " " + etnie + " " + nationalitate;
	}


	public static Vector <Object> make (ResultSet r) 
		throws SQLException
	{
		if (r == null) {
			return null;
		}

		int id_elev = r.getInt (1);
		String nume = r.getString (2);
		String prenume = r.getString (3);
		String adresa = r.getString (4);
		String cnp = r.getString (5);
		String etnie = r.getString (6);
		String nationalitate = r.getString (7);

		Vector <Object> rowData = new Vector <Object> (7);

		rowData.add (id_elev);
		rowData.add (nume);
		rowData.add (prenume);
		rowData.add (adresa);
		rowData.add (cnp);
		rowData.add (etnie);
		rowData.add (nationalitate);
		return rowData;
	}

	
	public static ElevDataModel makeObj (Vector <Object> raw)
	{
		int id_elev = ((Integer)raw.get (0)).intValue ();
		String nume = (String)raw.get (1);
		String prenume = (String)raw.get (2);
		String adresa = (String)raw.get (3);
		String cnp = (String)raw.get (4);
		String etnie = (String)raw.get (5);
		String nationalitate =  (String)raw.get (6);

		ElevDataModel obj = 
			new ElevDataModel (
				id_elev,
				nume,
				prenume,
				adresa,
				cnp,
				etnie,
				nationalitate
			);					
		
		return obj;
	}	


	public static boolean checkNume (String nume) 
		throws DataModelTypeMismatchError
	{
		if (nume == null || nume.isEmpty () || nume.length () > 50 || !nume.matches ("^[A-Za-z]+$")) {	
			throw new DataModelTypeMismatchError ("Numele elevului este invalid");
		}
		return true;
	}


	public static boolean checkPrenume (String prenume) 
		throws DataModelTypeMismatchError
	{
		if (prenume == null || prenume.isEmpty () || prenume.length () > 50 || !prenume.matches ("^[A-Za-z]+$")) {	
			throw new DataModelTypeMismatchError ("Prenumele elevului este invalid");
		}
		return true;
	}


	public static boolean checkAdresa (String adresa) 
		throws DataModelTypeMismatchError
	{
		if (adresa == null || adresa.isEmpty () || adresa.length () > 256) {	
			throw new DataModelTypeMismatchError ("Adresa este invalida");
		}
		return true;
	}


	public static boolean checkCnp (String cnp) 
		throws DataModelTypeMismatchError
	{
		if (cnp == null || cnp.isEmpty () || cnp.length () > 13 || !cnp.matches ("^[0-9]+$")) {	
			throw new DataModelTypeMismatchError ("CNP-ul elevului este invalid");
		}
		return true;
	}


	public static boolean checkEtnie (String etnie) 
		throws DataModelTypeMismatchError
	{
		if (etnie == null || etnie.isEmpty () || etnie.length () > 30 || !etnie.matches ("^[A-Za-z ]+$")) {	
			throw new DataModelTypeMismatchError ("Etnia elevului este invalida");
		}
		return true;
	}


	public static boolean checkNationalitate (String nationalitate) 
		throws DataModelTypeMismatchError
	{
		if (nationalitate == null || nationalitate.isEmpty () ||
			nationalitate.length () > 30 || !nationalitate.matches ("^[A-Za-z ]+$")) {	
			throw new DataModelTypeMismatchError ("Nationalitatea elevului este invalida");
		}
		return true;
	}
}
