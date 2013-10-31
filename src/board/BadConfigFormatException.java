package board;

import java.io.FileWriter;
import java.io.IOException;

public class BadConfigFormatException extends Exception {
	public BadConfigFormatException() {
		super();
	}
	
	public BadConfigFormatException(String message) {
		super(message);
		try {
			FileWriter writer = new FileWriter("log.txt");
			writer.write("An error was generated with message: "+message);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
