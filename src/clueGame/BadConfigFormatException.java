/*
 * @authors Cole Callihan, Carter Pasqualini
 * 
 * exception for later use
 */ 

package clueGame;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class BadConfigFormatException extends Exception{

	public BadConfigFormatException() throws FileNotFoundException {
		super("Configuration files not formatted properly");
		PrintWriter out = new PrintWriter("error_log.txt");
		out.println("Configuration files not formatted properly");
		out.close();
	}

	public BadConfigFormatException(String message) throws FileNotFoundException {
		super(message);
		PrintWriter out = new PrintWriter("error_log.txt");
		out.println(message);
		out.close();
	}
	
}
