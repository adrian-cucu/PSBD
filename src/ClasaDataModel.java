
class ClasaDataModel {

	private int id_clasa;
	private int id_profil;
	private int an_scolar;
	private String cod;
	private int clasa;


	public ClasaDataModel (int id_clasa, int id_profil, int an_scolar, 
						String cod, int clasa)
	{
		this.id_clasa = id_clasa;
		this.id_profil = id_profil;
		this.an_scolar = an_scolar;
		this.cod = cod;
		this.clasa = clasa;
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
