package public_package;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Encryption {
	protected static String fileName;
	protected static void setFileName() {
		fileName = System.getProperty("user.home")+"/.ssh/.project2";
	}
	protected boolean fileExists() {
		setFileName();
		File f=new File(fileName);
		if(f.exists())
			return true;
		else {
			System.out.println("BOOOOO");
			return false;
		}
	}
	protected static Map<String, String> readFile(){
		setFileName();
		Map<String, String> map = new HashMap<String, String>();
		File file = new File(fileName);
		int key;
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			key=Integer.parseInt(br.readLine());
			String x= br.readLine();
			x += br.readLine() ;
			try{key=Integer.parseInt(decrypt(br.readLine(),key,x));
			}catch (NumberFormatException e) {}
			String phrase ;
			String user = decrypt(br.readLine(),key,x);
			phrase = br.readLine();
			String password = decrypt(phrase,key,x);
			map.put("alpha", user);
			map.put("omega", password);

		} catch (IOException e1) {
			return null;
		}
		return map;
	}
	protected static void deleteFile(){
		setFileName();
		File file = new File(fileName);
		if(file.exists())
			file.delete();
	}
	protected static void encryptToFile(String str1,String str2){
		try{
			setFileName();
			PrintWriter writer = new PrintWriter(fileName);
			String x = shuffleString();
			int randomKey =(int) System.currentTimeMillis();

			writer.println(randomKey);
			for(int i=0;i<x.length();++i){
				writer.print(x.charAt(i));
				if(i+1==x.length()/2)
					writer.print("\n");
			}
			writer.print("\n");
			writer.println(encrypt(Integer.toString(randomKey) ,randomKey,x));
			writer.println(encrypt(str1,randomKey,x));
			writer.println(encrypt(str2,randomKey,x));

			for(int j=0;j<x.length();++j){
				x = shuffleString();
				for(int i=0;i<x.length();++i){
					writer.print(x.charAt(i));
					if(i+1==x.length()/2)
						writer.print("\n");
				}
			}
			writer.close();
			Preferences.hide(fileName);
		} catch (IOException e) {
			System.out.println("Error in writeFile!");
			e.printStackTrace();
		}		
	}
	protected static String shuffleString(){
		String x = "wEVY!3MUxutQiS*TOkF-9WB8qf@5LbcljAD61#zrKm2vdg7NX$%nshyCJ<GoPR>apHeI04Z";
		List<String> letters = Arrays.asList(x.split(""));
		Collections.shuffle(letters);
		String shuffled = "";
		for (String letter : letters)
			shuffled += letter;
		return shuffled;
	}
	protected static  String encrypt(String cipherText, int shiftKey,String c){    	
		String plainText = "";
		for (int i = 0; i < cipherText.length(); i++)
			if(c.indexOf(cipherText.charAt(i))<0)
				System.out.println("INVALID CHARACTER: "+cipherText.charAt(i));           	
			else {
				if ((c.indexOf(cipherText.charAt(i)) + shiftKey) % c.length() < 0)
					plainText += c.charAt(c.length() + (c.indexOf(cipherText.charAt(i)) + shiftKey) % c.length());
				else
					plainText += c.charAt((c.indexOf(cipherText.charAt(i)) + shiftKey) % c.length());}
		return plainText;
	}	
	protected static String decrypt(String cipherText, int shiftKey,String alpha){    	
		String plainText = "";
		int inverse = (alpha.length()-shiftKey %alpha.length())%alpha.length();
		// System.out.println(inverse);
		for (int i = 0; i < cipherText.length(); i++)
			if(alpha.indexOf(cipherText.charAt(i))<0)
				System.out.println("INVALID CHARACTER");
			else {
				if ((alpha.indexOf(cipherText.charAt(i)) + inverse) % alpha.length() < 0)
					plainText += alpha.charAt(alpha.length()+(alpha.length() + (alpha.indexOf(cipherText.charAt(i)) + inverse)) % alpha.length());
				else
					plainText += alpha.charAt((alpha.indexOf(cipherText.charAt(i)) + inverse) % alpha.length());}
		return plainText;
	}
}
