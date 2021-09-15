package edu.upenn.cit594.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Logger {
	private PrintWriter out;
	private static String filename;
	private static Logger instance = null;

	private Logger() {
		try {
			File file = new File(Logger.filename);
			if(!file.exists()) file.createNewFile();
			
			FileWriter fileWriter = new FileWriter(file, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			this.out = new PrintWriter(bufferedWriter);
		} catch (Exception e){
			throw new IllegalArgumentException("Could not create or write to log file: " + Logger.filename);
		}
	}

	public static void setFilename(String _filename) {
		if (_filename != null && _filename.length() > 0 && _filename.toLowerCase().endsWith(".txt")) {
			filename = _filename;
		} else {
			throw new IllegalArgumentException(filename + " is not a valid filename for log file. (Usage: <filename>.txt)");
		}
	}

	public static Logger getInstance() {
		if (filename != null && filename.length() > 0) {
			if (instance == null) {
				instance = new Logger();
			}
		} 
		return instance;
	}

	public void write(String msg) {
		out.println(String.format("%d %s", System.currentTimeMillis(), msg));
		out.flush();
	}
	
	public void close() {
		try {
			out.close();
		} catch (Exception e) {}
	}
}
