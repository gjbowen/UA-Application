package ar_package;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;

import com.jcraft.jsch.Session;
import com.sshtools.sftp.SftpClient;
import com.sshtools.sftp.SftpStatusException;
import com.sshtools.ssh.SshException;

class Function_Library {
	String environment;
	String firstName;
	JDBC jdbc;
	SFTP sftp;
	SSH ssh;
	String userName;

	String password;
	Function_Library(Connection conn_jdbc, SftpClient conn_sftp, Session conn_ssh, String user, String pass, String env) {
		jdbc=new JDBC(conn_jdbc,user,pass,env);
		sftp=new SFTP(conn_sftp,user,pass,env);
		ssh =new SSH(conn_ssh,user,pass,env);



		firstName=jdbc.getUserFirstName(user);
		environment = env;
		userName=user;
		password = pass;
		getCadence();
	}
	void getCadence(){
		String fileName = "eScript";

		ssh.writeExpectFile(fileName);
		sftp.putFile(System.getProperty("user.dir"),fileName,"/home/"+userName,fileName);
		ssh.run("chmod -R 0700 /home/"+userName);
		ssh.run("./"+fileName);
		ssh.run("chmod -R 0700 /home/"+userName);

		// sftp files to Box
	}
    public void openLink(String URL) {
		try {
			Desktop.getDesktop().browse(new URI(URL));
		} catch (IOException | URISyntaxException   e) {
			e.printStackTrace();
		} 	
	}
	String getApexLink() {
		if(environment.equals("SEVL"))
			return "http://testssb.ua.edu:9075/pls/APEX_SEVL/f?p=145";
		 if(environment.equals("TEST"))
			return "http://testssb.ua.edu:9025/pls/APEX_TEST/f?p=145";
		else 
			return "https://ssb.ua.edu/pls/APEX_PROD/f?p=145";
	}

}