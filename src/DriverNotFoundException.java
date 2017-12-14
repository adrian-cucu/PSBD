
class DriverNotFoundException extends Exception {

    public static final long serialVersionUID = 0xad1;

	public DriverNotFoundException (String errMsg)
	{
		super (errMsg);
	}	


	public DriverNotFoundException (String errMsg, Throwable thr)
	{
		super (errMsg, thr);
	}	
}
