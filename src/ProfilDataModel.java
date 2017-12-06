
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


	public int getID ()
	{
		return id_profil;
	}
	

	public String getNumeProfil ()
	{
		return nume_profil;
	}

	
	public String toString()
	{
		return nume_profil;
	} 
}
