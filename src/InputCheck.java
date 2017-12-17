
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


	public static void main (String[] args)
	{
		String pass = new String ("duteinpulameadeprost");
		char[] str = pass.toCharArray ();
		System.out.println (str[1]);
		/*
		pass.chars ()
			.mapToObj (c -> (char) c)
			.forEach (System.out::println);
		*/
	
		java.util.stream.
			Stream.of (1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
				  .filter (
						new java.util.function.Predicate <Integer>() {
							public boolean test (Integer x) {	
								return x.intValue () % 2 == 0;
							}
						} .and (
							new java.util.function.Predicate <Integer>() {
								public boolean test (Integer x) {	
									return x.intValue ()  > 5;
								}
							}
						)

				  )
				  .forEach (System.out::println);

		java.util.stream.
			Stream.of (1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
				  .filter (x-> x > 5)
				  .forEach (System.out::println);

	}
}
