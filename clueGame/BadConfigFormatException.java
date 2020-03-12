/*
 * @author Cole Callihan, Carter Pasqualini
 * 
 * BadConfigFormatException is a custom exception that we design to output its corresponding error messages to a log file
 */ 

package clueGame;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class BadConfigFormatException extends Exception{

	/*
	 * Constructor that constructs a default Exception and writes the default message to the error log file
	 */
	public BadConfigFormatException() throws FileNotFoundException {
		super("Configuration files not formatted properly");
		PrintWriter out = new PrintWriter("error_log.txt");
		out.println("Configuration files not formatted properly");
		out.close();
	}

	/*
	 * Constructor that constructs the default exception with the passed in message and writes the message to the error log file
	 */
	public BadConfigFormatException(String message) throws FileNotFoundException {
		super(message);
		PrintWriter out = new PrintWriter("error_log.txt");
		out.println(message);
		out.close();
	}
	
}
