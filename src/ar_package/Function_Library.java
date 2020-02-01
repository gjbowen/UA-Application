package ar_package;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.util.Calendar;
import java.util.List;

import com.jcraft.jsch.Session;
import com.sshtools.sftp.SftpClient;
import com.sshtools.sftp.SftpStatusException;
import com.sshtools.sftp.TransferCancelledException;
import com.sshtools.ssh.SshException;
import public_package.SSH_Connection;

class Function_Library {
	protected String environment;
	protected String firstName;
	protected JDBC_Connection jdbc;
	protected SFTP_Connection sftp;
	protected SSH_Connection ssh;
	String userName;
	protected String password;
	Function_Library(Connection conn_jdbc, SftpClient conn_sftp, Session conn_ssh, String user, String pass, String env) {
		jdbc=new JDBC_Connection(conn_jdbc,env,user,pass);
		sftp=new SFTP_Connection(conn_sftp,env,user,pass);
		ssh =new ar_package.SSH_Connection(conn_ssh,env,user,pass);
		try {
			System.out.println(sftp.getConnection().ls());
			System.out.println(System.getProperty("user.dir"));
			sftp.connection.lcd("");
			sftp.connection.put("C:\\Users\\gjbowen\\IdeaProjects\\UA-Application\\eFile.txt","/u03/home/"+user+"/eFile.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SftpStatusException e) {
			e.printStackTrace();
		} catch (SshException e) {
			e.printStackTrace();
		} catch (TransferCancelledException e) {
			e.printStackTrace();
		}
		firstName=jdbc.getUserFirstName(user);
		environment = env;
		userName=user;
		password = pass;
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