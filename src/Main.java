import static java.lang.System.*;

class Main {

	public static void main (String[] args)
	{		
		try {
			java.awt.EventQueue.invokeAndWait ( ()-> {
				AppController controller = new AppController (new LoginView ());
			});

		} catch (InterruptedException e) {
			out.println (e.getMessage ());		

		} catch (java.lang.reflect.InvocationTargetException e) {
			out.println (e.getMessage ());
		}
	}
}
