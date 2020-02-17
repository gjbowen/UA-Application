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
import com.sshtools.sftp.SftpFile;
import com.sshtools.sftp.SftpStatusException;
import com.sshtools.sftp.TransferCancelledException;
import com.sshtools.ssh.PasswordAuthentication;
import com.sshtools.ssh.SshAuthentication;
import com.sshtools.ssh.SshClient;
import com.sshtools.ssh.SshConnector;
import com.sshtools.ssh.SshException;
import com.sshtools.ssh2.Ssh2Client;

public class SFTP_Connection {
	public final String environment;
	protected String username;
	protected final String password;
	public SftpClient connection;
	protected String dirDelim;

	protected SFTP_Connection(String user, String pass, String env) {
		environment = env;
		username = user;
		password = pass;
		connection = null;
		setDirDelim();
	}
	public SftpClient getConnection(){
		return connection;
	}
	private void setDirDelim() {
		if(public_package.Preferences.isWindows())
			dirDelim="\\";
		else
			dirDelim = "/";
	}

    void sftpConnect(){
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

	public void putFile(String localFolder,String localFileName,String remoteFolder,String remoteName){
		try {
			connection.put(localFolder+"\\"+localFileName,remoteFolder+"/"+remoteName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SftpStatusException e) {
			e.printStackTrace();
		} catch (SshException e) {
			e.printStackTrace();
		} catch (TransferCancelledException e) {
			e.printStackTrace();
		}

	}
	public void getFile(String localFolder,String localFileName,String remoteFolder,String remoteName){
		try {
			connection.getFiles(remoteFolder+"/"+remoteName,localFolder+"\\"+localFileName);
		}
		catch (FileNotFoundException e) {e.printStackTrace();}
		catch (SftpStatusException e) {e.printStackTrace();}
		catch (SshException e) {e.printStackTrace();}
		catch (TransferCancelledException e) {e.printStackTrace();}
	}
	public void getFolder(String remoteFolder,String localFolder) {
		SftpFile[] files = new SftpFile[0];

		File folder = new File(localFolder);
		if(!folder.isDirectory())
			folder.mkdir();

		try {
			files = connection.ls(remoteFolder);
			connection.lcd(localFolder);
			connection.cd(remoteFolder);
		}
		catch (SftpStatusException e){e.printStackTrace();}
		catch (SshException e){e.printStackTrace();}

		for(int i=0; i<files.length;i++){
			//System.out.println(files[i].getFilename());
			// Grap and get the file by WORKING_DIR/filelist.get(i).toString();
			// Save it to your local directory with its original name.

			try {
				connection.get(files[i].getFilename());
			}
			catch (FileNotFoundException e){e.printStackTrace();}
			catch (TransferCancelledException e){e.printStackTrace();}
			catch (SshException e){e.printStackTrace();}
			catch (SftpStatusException e) {e.printStackTrace();}
		}
	}
	public void rm(String path){
		try {
			connection.rm(path);
		} catch (SftpStatusException e) {
			e.printStackTrace();
		} catch (SshException e) {
			e.printStackTrace();
		}

	}
	public void moveFile(String env,String localFolder,String localFileName,String remoteFolder,String remoteName){
		SftpClient destConnection=null;
		try {
			String hostname = getInstance(env);

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
			String localPath = localFolder;//System.getProperty("user.home")+ "\\Concur_Files\\" + environment + "\\"+localFolder+"\\";
			if (ssh.isAuthenticated()) {
				System.out.println("Local path: "+localPath);
				System.out.println("Moving to: "+remoteFolder+"/"+remoteName);
				destConnection = new SftpClient(ssh2);
				destConnection.cd(remoteFolder);
				destConnection.lcd(localPath);
				destConnection.put(localFileName, remoteName);
				destConnection.chmod(0777, remoteFolder+"/"+remoteName);
				destConnection.exit();
				System.out.println("  "+remoteName + " has been transfered");
			}
		}
		catch (Throwable th) {
			if(th.getLocalizedMessage().trim().equals("Permission denied.: Permission denied")) {
				try {
					destConnection.rm(remoteName);
					destConnection.put(localFileName, remoteName);
					destConnection.chmod(0777, remoteFolder+"/"+remoteName);
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
	public void lrm(String path){
		File f = new File(path);
		if(f.exists())
			f.delete();
	}

}
