import static java.lang.System.*;

class Main {

	private static final boolean DEBUG = true;

	public static void main (String[] args)
	{
		AppController controller = new AppController (new LoginView ());
		
		if (DEBUG) {
			out.println ("Started...");
		}		
	}
}
