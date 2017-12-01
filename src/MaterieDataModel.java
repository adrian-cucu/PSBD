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

	public int getID ()
	{
		return id_materie;
	}
	

	public String getNumeMaterie ()
	{
		return nume_materie;
	}

	
	public String toString()
	{
		return nume_materie;
	} 
}
