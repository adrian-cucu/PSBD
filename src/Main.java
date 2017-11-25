import static java.lang.System.*;

class Main {


	public static void main (String[] args)
	{		
		try {
			java.awt.EventQueue.invokeAndWait (new Runnable () {
				public void run () {
					AppController controller = 
						new AppController (new LoginView ());
				}
			});
		}
		catch (InterruptedException e) {
		}	
		catch (java.lang.reflect.InvocationTargetException e) {

		}
	}

}
