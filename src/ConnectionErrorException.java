
class ConnectionErrorException extends Exception {
	
	public static final long serialVersionUID = 0xad1;

	public ConnectionErrorException (String errMsg)
	{
		super (errMsg);
	}	


	public ConnectionErrorException (String errMsg, Throwable thr)
	{
		super (errMsg, thr);
	}	
}
