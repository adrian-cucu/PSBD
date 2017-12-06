import java.sql.*;

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
