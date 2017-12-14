
class DataModelTypeMismatchError extends Exception {

    public static final long serialVersionUID = 0xad1;

	public DataModelTypeMismatchError (String errMsg)
	{
		super (errMsg);
	}	


	public DataModelTypeMismatchError (String errMsg, Throwable thr)
	{
		super (errMsg, thr);
	}	
}
