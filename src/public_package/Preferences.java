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

	public Preferences(){
		if(!fileExists())
			writeFile(null);
		readFile();

	}
	public static Map<String,String> read(){
		if(!fileExists()) {
			writeFile(null);
		}

		HashMap<String,String> cons= new HashMap<String,String>();
		String line = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(getFileName()));
			while ((line = br.readLine()) != null) 
				cons.put(line.split(",")[0],line.split(",")[1]);
			br.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return cons;
	}
	public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
		}
	static String getFileName() {
		if(isWindows()) 
			return "H:\\.preferences";
		else 
			return System.getProperty("user.home")+"/.preferences";
	}
	static void writeFile(Map<String,String> m)  {
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
			}
			output.close();
			hide(getFileName());
		}
		catch (IOException e2) {
			e2.printStackTrace();
		}
	}
	public static void addPreference(String key,String value) {
		readFile();
		contents.put(key,value);
		writeFile(contents);
	}
	public void deletePreference(String key) {
		readFile();
		contents.remove(key);
		writeFile(contents);
	}
	static boolean fileExists() {
        return new File(getFileName()).exists();
	}
	static void readFile(){
		contents = new HashMap<String,String>();
		String line = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(getFileName()));
			while ((line = br.readLine()) != null) 
				contents.put(line.split(",")[0],line.split(",")[1]);
			br.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	static void deleteFile(){
		File file = new File(getFileName());
		if(file.exists())
			file.delete();
	}
	void show() {
		readFile();
		System.out.println("SHOWING PREFERENCES:");
		if(contents!=null)
			for (Map.Entry<String,String> entry : contents.entrySet())  
				System.out.println("\t" + entry.getKey() + 
						"\t" + entry.getValue()); 
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
