package public_package;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import com.sshtools.net.SocketTransport;
import com.sshtools.sftp.SftpClient;
import com.sshtools.sftp.SftpStatusException;
import com.sshtools.sftp.TransferCancelledException;
import com.sshtools.ssh.PasswordAuthentication;
import com.sshtools.ssh.SshAuthentication;
import com.sshtools.ssh.SshClient;
import com.sshtools.ssh.SshConnector;
import com.sshtools.ssh.SshException;
import com.sshtools.ssh2.Ssh2Client;

public class SFTP_Connection {
	public String environment;
	protected String username;
	protected String password;
	public SftpClient connection;
	protected String dirDelim;

	public SftpClient getConnection(){
		return connection;
	}
	protected SFTP_Connection(String env, String user, String pass) {
		environment = env;
		username = user;
		password = pass;
		connection = null;
		setDirDelim();
	}
	private void setDirDelim() {
		if(public_package.Preferences.isWindows())
			dirDelim="\\";
		else
			dirDelim = "/";

	}

    protected void sftpConnect(){
		try {
			String hostname = getInstance(environment);

			if (username == null || username.trim().equals("")){
				username = System.getProperty("user.name");
			}
			System.out.println("Connecting SFTP to " +username+"@"+ hostname+"...");

			/**
			 * Create an SshConnector instance
			 */
			SshConnector con = SshConnector.createInstance();

			/** 
			 * Connect to the host
			 */
			SocketTransport t = new SocketTransport(hostname, 22);
			t.setTcpNoDelay(true);

			SshClient ssh = con.connect(t, username, true);

			Ssh2Client ssh2 = (Ssh2Client) ssh;
			/**
			 * Authenticate the user using password authentication
			 */
			PasswordAuthentication pwd;
			pwd = new PasswordAuthentication();

			do {
				pwd.setPassword(password);
			} 
			while (ssh2.authenticate(pwd) != SshAuthentication.COMPLETE
					&& ssh.isConnected());
			/**
			 * Start a session and do basic IO
			 */
			if (ssh.isAuthenticated()) {
				connection = new SftpClient(ssh2);
				System.out.println("SFTP Successful: "+username+"@"+ hostname);
			}
		} catch (Throwable th) {
			connection = null;
			System.out.println("FAIL");
		}
	}
	protected void sftpDisconnect() {
		try {
			connection.quit();
			connection.exit();
		} catch (SshException e) {
			e.printStackTrace();
		}
	}
	public String getInstance(String environment) {
		if (environment.equals("SEVL"))
			return "js-dev.ua.edu";
		else if (environment.equals("TEST"))
			return "js-test.ua.edu";
		else
			return "js-prod.ua.edu";
	}
	protected void gunzipIt(String fileName, String path) {
		String outFile = fileName.substring(0, fileName.length() - 3);
		String line = null;
		try {
			FileWriter fw = new FileWriter(path + "//" + outFile);
			GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(path + "//" + fileName));
			BufferedReader br = new BufferedReader(new InputStreamReader(gzip));
			line = br.readLine();
			while (line != null) {
				fw.write(line + "\r\n");
				line = br.readLine();
			}
			fw.close();
			br.close();
			File f2 = new File(path + "//" + fileName);
			f2.delete();
		}
		catch (IOException e2) {
			System.out.println("Shit - "+e2.getMessage()+"\n\t"+fileName);
		}
	}
	public void moveFile(String dest,String localFolder,String localFileName,String remoteName){
		SftpClient destConnection=null;
		try {
			String hostname = getInstance(dest);

			if (username == null || username.trim().equals("")){
				username = System.getProperty("user.name");
			}
			System.out.println("Connecting to " +username+"@"+ hostname+"...");

			/**
			 * Create an SshConnector instance
			 */
			SshConnector con = SshConnector.createInstance();

			/*
			  Connect to the host
			 */
			SocketTransport t = new SocketTransport(hostname, 22);
			t.setTcpNoDelay(true);

			SshClient ssh = con.connect(t, username, true);

			Ssh2Client ssh2 = (Ssh2Client) ssh;
			/*
			  Authenticate the user using password authentication
			 */
			PasswordAuthentication pwd;
			pwd = new PasswordAuthentication();

			do {
				pwd.setPassword(password);
			} 
			while (ssh2.authenticate(pwd) != SshAuthentication.COMPLETE
					&& ssh.isConnected());
			/**
			 * Start a session and do basic IO
			 */
			String localPath = System.getProperty("user.home")+ "\\Concur_Files\\" + environment + "\\"+localFolder+"\\";
			if (ssh.isAuthenticated()) {
				System.out.println("Local path: "+localPath);
				System.out.println("Moving to: "+"/u03/import/" + dest+"/"+localFileName);
				destConnection = new SftpClient(ssh2);
				destConnection.cd("/u03/import/" + dest);
				destConnection.lcd(localPath);
				destConnection.put(localFileName, remoteName);
				destConnection.chmod(0777, "/u03/import/" + dest+"/"+remoteName);
				destConnection.exit();
				System.out.println("  "+remoteName + " has been transfered");
			}
		} 
		catch (Throwable th) {
			if(th.getLocalizedMessage().trim().equals("Permission denied.: Permission denied")) {
				try {
					destConnection.rm(remoteName);
					destConnection.put(localFileName, remoteName);
					destConnection.chmod(0777, "/u03/import/" + dest+"/"+remoteName);
					destConnection.exit();
					System.out.println("  "+remoteName + " has been transfered");
				} catch (SftpStatusException | 
						SshException | 
						FileNotFoundException | 
						TransferCancelledException e) {
					e.printStackTrace();
					System.err.println("File failed to transfer");
				}
			}else {
				System.err.println(th.getMessage());
			}
		}
	}
	@SuppressWarnings("unused")
	private String getFilePath(String location){
		String path="";
		switch (location.toUpperCase()){
		case "ARCHIVE":
			path = "/u03/archive/" + environment;
		case "EXPORT":
			path = "/u03/export/" + environment;
		case "IMPORT":
			path = "/u03/import/" + environment;
		}		
		return path;
	}

}
