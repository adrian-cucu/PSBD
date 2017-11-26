

class ProfilDataModel {

	private int id_profil;	
	private String nume_profil;


	public ProfilDataModel (int id_profil, String nume_profil)
	{
		this.id_profil = id_profil;
		this.nume_profil = nume_profil;
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
