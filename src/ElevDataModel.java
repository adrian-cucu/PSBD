
class ElevDataModel {

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


	public ElevDataModel (java.sql.ResultSet set) 
		throws java.sql.SQLException 
	{

		id_elev = set.getInt ("id_elev");
		nume = set.getString ("nume");
		prenume = set.getString ("prenume");
		adresa = set.getString ("adresa");
		cnp = set.getString ("cnp");
		etnie = set.getString ("etnie");
		nationalitate = set.getString ("nationalitate");
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

}
