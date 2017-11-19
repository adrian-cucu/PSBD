class DataModel {
	private int id_elev;
	private String nume;
	private String prenume;
	private String adresa;
	private String cnp;
	private String etnie;
	private String nationalitate;

	private static final Object[] columnNames = {
		"ID ELEV", "NUME", "PRENUME", "ADRESA", "CNP", "ETNIE", "NATIONALITATE"
	}; 

	DataModel (java.sql.ResultSet set) throws java.sql.SQLException {

		this.id_elev = set.getInt ("id_elev");
		this.nume = set.getString ("nume");
		this.prenume = set.getString ("prenume");
		this.adresa = set.getString ("adresa");
		this.cnp = set.getString ("cnp");
		this.etnie = set.getString ("etnie");
		this.nationalitate = set.getString ("nationalitate");
	}	


	// Getters 
	public int getId () {
		return this.id_elev;
	}		
	public String getName () {
		return this.nume;
	}		
	public String getPren () {
		return this.prenume;
	}			
	public String getAddress () {
		return this.adresa;
	}		
	public String getCnp () {
		return this.cnp;
	}		
	public String getEtnie () {
		return this.etnie;
	}
	public String getNat () {
		return this.nationalitate;		
	}

	public String toString () {
		return this.getId () + " " +
			this.getName () + " " + 
			this.getPren () + "  " + 
			this.getAddress () + " " +
			this.getCnp () + " " + 
			this.getEtnie () + " " + 
			this.getNat ();
	}
}
