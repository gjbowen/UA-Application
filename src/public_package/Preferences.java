package public_package;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Preferences {
	//static String fileNsame=getFileName();
	public static Map<String,String> contents = read();

	private static Map<String,String> read(){
		if(!fileExists()) {
			writeFile(null);
		}

		HashMap<String,String> cons= new HashMap<String,String>();
		String line;
		try {
			BufferedReader br = new BufferedReader(new FileReader(getFileName()));
			while ((line = br.readLine()) != null) 
				cons.put(line.split(",")[0],line.split(",")[1]);
			br.close();
		}
		catch (IOException e) {
			System.err.println("Cannot read "+getFileName());
		}
		return cons;
	}
	public static boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().contains("windows");
	}
	private static String getFileName() {
		return System.getProperty("user.home")+"/.preferences";
	}
	private static void writeFile(Map<String, String> m)  {
		deleteFile();
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(getFileName()));
			if(m!=null)
				for (Map.Entry<String,String> entry : m.entrySet())
					output.write(entry.getKey()+","+entry.getValue()+"\n");
			else {
				Date date = new Date();
			    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
				output.write("Developer,Greg Bowen\n");
				output.write("created,"+formatter.format(date)+"\n");
				output.write("environment,prod\n");
				output.write("debug,false\n");
			}
			output.close();
			hide(getFileName());
		}
		catch (IOException e2) {
			System.err.println("Cannot write to "+getFileName());
		}
	}
	public static void addPreference(String key,String value) {
		readFile();
		contents.put(key,value);
		writeFile(contents);
	}

	private static boolean fileExists() {
        return new File(getFileName()).exists();
	}
	private static void readFile(){
		contents = new HashMap<String,String>();
		String line;
		try {
			BufferedReader br = new BufferedReader(new FileReader(getFileName()));
			while ((line = br.readLine()) != null) 
				contents.put(line.split(",")[0],line.split(",")[1]);
			br.close();
		}
		catch (IOException e) {
			System.err.println("Cannot read "+getFileName());
		}
	}
	private static void deleteFile(){
		File file = new File(getFileName());
		if(file.exists())
			file.delete();
	}

	static void hide(String file)  {
		// win32 command line variant
		Process p;
		try {
			p = Runtime.getRuntime().exec("attrib +h " + file);
			p.waitFor(); // p.waitFor() important, so that the file really appears as hidden immediately after function exit.
		} catch (IOException | InterruptedException e) {
			System.out.println("Failed to hide - "+e.getMessage());
		}
	}
}
