
class InputCheck {


	private static final char[] invalid_chars = 
		"!@#$%^&*()!`\"\\/<>;:'".toCharArray ();


	public static boolean 
		contains_invalid_chars (String str)
	{
		for (char c : invalid_chars) {
			if (str.indexOf (c) != -1) {
				return true;
			}
		}
		return false;
	}


	public static boolean check (String str)
 	{
		return !str.isEmpty () && !contains_invalid_chars (str); 
	}

}
