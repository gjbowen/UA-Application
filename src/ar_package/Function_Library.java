package ar_package;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.util.Calendar;
import java.util.List;

import com.sshtools.sftp.SftpClient;


class Function_Library {
	protected String environment;
	protected String firstName;
	protected JDBC_Connection jdbc;
	String userName;
	Function_Library(Connection conn_jdbc,SftpClient conn_sftp,String user,String pass,String mode) {
		jdbc=new JDBC_Connection(conn_jdbc,mode,user,pass);

		firstName=jdbc.getUserFirstName(user);
		environment = mode;
		userName=user;
	}

 	protected String getYear() {
 		Calendar now = Calendar.getInstance();
 		int year = now.get(Calendar.YEAR);
 		return String.valueOf(year);
	}  
	public void openLink(String URL) {
		try {
			Desktop.getDesktop().browse(new URI(URL));
		} catch (IOException | URISyntaxException   e) {
			e.printStackTrace();
		} 	
	}
	protected String getApexLink() {
		if(environment.equals("SEVL"))
			return "http://testssb.ua.edu:9075/pls/APEX_SEVL/f?p=145";
		 if(environment.equals("TEST"))
			return "http://testssb.ua.edu:9025/pls/APEX_TEST/f?p=145";
		else 
			return "https://ssb.ua.edu/pls/APEX_PROD/f?p=145";
	}
	
	@SuppressWarnings("unused")
	private void writeListToFile(String fileName, String header, List<String> stuff) {
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(fileName, false));
			output.write(header + '\n');
			for (int i2=0;i2 < stuff.size();++i2)
				output.write(stuff.get(i2) + '\n');
			output.close();
		}
		catch (IOException e2) {
			e2.printStackTrace();
		}
	}


}