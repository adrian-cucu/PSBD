
class ConnectionErrorException extends Exception {

	
	public static final long serialVersionUID = 1L;


	public ConnectionErrorException (String errMsg)
	{
		super (errMsg);
	}	


	public ConnectionErrorException (String errMsg, Throwable thr)
	{
		super (errMsg, thr);
	}	
}
