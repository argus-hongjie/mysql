package mysql51;

public class badclassname {
	public void ha() {
		String str1 = "blue";
		String str2 = "blue";
		String str3 = str1;

		if (str1 == str2)
		{
		  System.out.println("they're both 'blue'"); // this doesn't print because the objects are different
		}

		if (str1 == "blue")
		{
		  System.out.println("they're both 'blue'"); // this doesn't print because the objects are different
		}

		if (str1 == str3)
		{
		  System.out.println("they're the same object"); // this prints
		}
	}
}
