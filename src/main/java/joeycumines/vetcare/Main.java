package joeycumines.vetcare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * A simple command line program to test out the message system for rapid
 * development.
 * 
 * The basic structure is as such: - A simple switch allows us to check top
 * level commands - Some commands require more in depth interaction, handled in
 * script style code - Output is limited, but displayed through tab-ified
 * information.
 * 
 * @author Joey
 *
 */
public class Main {

	/**
	 * Simply split the strings.
	 * 
	 * @param _input
	 * @return
	 */
	public static ArrayList<String> separateArgs(String _input) {
		ArrayList<String> result = new ArrayList<String>();
		String[] temp = _input.split(" ");
		for (int x = 0; x < temp.length; x++)
			result.add(temp[x]);
		//set the actual command to lowercase
		result.set(0, result.get(0).toLowerCase());
		return result;
	}
	
	/**
	 * Make an intelligently sized table. Requires standard size characters.
	 * @return
	 */
	public static String makeTable(ArrayList<ArrayList<String>> _rows, boolean _heading) {
		if (_rows.isEmpty())
			return "";
		if (_rows.get(0).isEmpty())
			return "";
		
		String result = "";
		String colBuffer = " | ";
		String leftBuffer = "| ";
		String rightBuffer = " |";
		char horiChar = '_';
		char headingChar = '=';
		//we need the maximum width for each column, which requires examining the entirety of the data
		ArrayList<Integer> lengths = new ArrayList<Integer>();
		for(ArrayList<String> row : _rows) {
			for(int x = 0; x < row.size(); x++) {
				//each column in every row
				if (lengths.size() <= x)
					lengths.add(new Integer(0));
				//if we need to then increase the size of the column
				if (lengths.get(x).intValue() < row.get(x).length())
					lengths.set(x, row.get(x).length());
			}
		}
		
		int totalLength = 0;
		for(Integer i : lengths)
			totalLength+= i.intValue();
		
		int totalLengthIncludingBuffers = leftBuffer.length() + rightBuffer.length() + totalLength + ((lengths.size()-1)*colBuffer.length());
		
		//now, make the top row
		for(int x = 0; x < totalLengthIncludingBuffers;x++) {
			result += horiChar;
		}
		result+="\n";
		
		//add the data together for every row.
		boolean firstRow = true;
		for(ArrayList<String> row : _rows) {
			result+=leftBuffer;
			int colNum = 0;
			for (String col : row) {
				//for every character we iterate
				for (int x = 0; x < lengths.get(colNum).intValue(); x++) {
					if (x >= col.length()) {
						result+= ' ';
					} else {
						result+= col.charAt(x);
					}
				}
				colNum++;
				if (colNum < row.size())
					result+= colBuffer;
			}
			result+=rightBuffer+"\n";
			//make the bottom row
			for(int x = 0; x < totalLengthIncludingBuffers;x++) {
				if (_heading && firstRow)
					result += headingChar;
				else
					result += horiChar;
			}
			firstRow = false;
			result+="\n";
		}
		
		return result;
	}

	public static final String EXIT_COMMAND = "exit";
	public static final String HELP_COMMAND = "help";
	public static final String LIST_COMMAND = "list";

	public static void main(String[] args) {
		// create a new logger
		Logger logger = Logger.getLogger(Main.class.getName());
		// disable console info
		logger.setUseParentHandlers(false);
		FileHandler fh;
		try {
			// This block configure the logger with handler and formatter
			fh = new FileHandler("log.txt");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// logger is setup, load up the message API
		MessageAPI api = new MessageAPI();
		// message api is setup, load up the thread that performs background
		// tasks, such as sending messages
		MessageWorker worker = new MessageWorker("VetcareMessageWorker", logger, api);
		// start the worker thread
		worker.start();
		logger.info("Loaded the program successfully.");

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("type '" + HELP_COMMAND + "' for help, or '" + EXIT_COMMAND + "' to quit");

		// here, we demo a console version of the full web application.
		while (!api.shouldStop()) {
			try {
				System.out.print("> ");
				String input = br.readLine();
				
				ArrayList<String> command = separateArgs(input);
				
				switch (command.get(0)) {
				case EXIT_COMMAND:
					System.out.println("Exiting.");
					api.stopWorker();
					break;
				case HELP_COMMAND:
					System.out.println(	"Commands:\n"
							+ EXIT_COMMAND+":\tExit the application\n"
							+ LIST_COMMAND+":\tDisplay a table of the upcoming notifications\n"
							+ "\t\toptions:"
							+ "\t\t"
							+ "");
					break;
				case LIST_COMMAND:
					
					break;
				default:
					System.out.println("Did not recognize your command '"+command.get(0)+"', type '"+HELP_COMMAND+"' for help.");
					break;
				}

			} catch (Exception e) {
				logger.setUseParentHandlers(true);
				logger.log(Level.SEVERE, "an exception was thrown", e);
				logger.setUseParentHandlers(false);
			}
		}

		// exit
		logger.info("Exiting.");
	}

}
