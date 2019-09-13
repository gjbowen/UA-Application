package git_package;

import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.JOptionPane;

import public_package.Preferences;
public class Function_Library {
	String gitFolder;
	final String userName;
	private ArrayList<String> remoteBranches;

	public Function_Library() {
		gitFolder = getUserHome();
		userName = getUserName();
	}
	public boolean done(){
		 return Preferences.contents.containsKey("git")
				 && sshKeysExist()
				 && GITprogramExists()
				 && tortoiseProgramExists();
	 }
	public boolean ready(){
		if(done()) {
			readPreferenceFile();
			return true;
		}
		else {
			int val=0;
			if (!tortoiseProgramExists()) {
				val = yesNo("Tortoise was not found. Would you like to install it now? (During the install process, it will install GIT)");
				if (val == 0) {
					downloadTortoise();
				}
			}
			else if (!GITprogramExists()) {
				val = yesNo("GIT was not found. Would you like to install it now?");
				if (val == 0) {
					downloadGIT();
				}

			}
			else if (!sshKeysExist()) {
				val = yesNo("SSH keys were not found. Would you like to setup now?");
				if (val == 0) {
					EventQueue.invokeLater(() -> {
						try {
							SSH_Menu sshWindow = new SSH_Menu(this);
							sshWindow.frameMainMenu.setVisible(true);
						}
						catch (Exception e2) {
							e2.printStackTrace();
						}
					});

				}
			}
			else if(!Preferences.contents.containsKey("git")){ //launch setup mentu
				val = yesNo("This program is not configured for your GIT. Would you like to setup now?");
				if (val == 0) {
					EventQueue.invokeLater(() -> {
						try {
							new Setup_Menu(this);
						}
						catch (Exception e2) {
							e2.printStackTrace();
						}
					});
				}
			}
			return false;
		}
	}

	private void setTortoiseSSH() {
		try {
			Runtime.getRuntime().exec("reg add HKCU\\SOFTWARE\\TortoiseGit /t REG_SZ /v SSH /d \"C:\\Program Files\\Git\\usr\\bin\\ssh.exe\" /f");
			print("ALL SET!");
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	protected boolean GITfolderExists(String location) {
		File folder = new File(location+"\\.git");
		return folder.isDirectory();
	}
	private boolean GITprogramExists() {
		File program = new File("C:\\Program Files\\Git\\git-bash.exe");
		return program.exists();
	}
	private boolean tortoiseProgramExists() {
		File program = new File("C:\\Program Files\\TortoiseGit");
		return program.isDirectory();
	}
	public void downloadGIT() {
		try {
			Desktop.getDesktop().browse(new URI("https://git-scm.com/download"));
		} catch (IOException | URISyntaxException   e) {
			e.printStackTrace();
		}
	}
	public void downloadTortoise() {
		try {
			Desktop.getDesktop().browse(new URI("https://tortoisegit.org/download/"));
		} catch (IOException | URISyntaxException   e) {
			e.printStackTrace();
		}
	}
	//////////////////////////////////////////////////////////////////////////////////////////////
	public void print(String i){System.out.println(i);}
	private void cmd(String command) {
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void cloneGIT(String location) {
		String command = "cmd /c start cmd.exe /K \""
				+ "cd "
				+ "\""+	location +"\" && "
				+ "git clone ssh://git@fisheye01.ua.edu:7999/ban/uabanner.git && "
				+ "exit\"";
		cmd(command);
	}
	private void createBranch(String newBranch) {
		String command;
		if(isRemoteGIT(newBranch)) 
			command="cmd /c start cmd.exe /K \""
					+ "cd \"" +	gitFolder +"\" && "
					+ "git checkout prod && "
					+ "git pull --progress -v --no-rebase \"origin\" prod && "
					+ "git checkout -b " + newBranch + " && "
					+ "git pull --progress -v --no-rebase \"origin\" "+newBranch +" && "
					+ "exit \"";
		else
			command="cmd /c start cmd.exe /K \""
					+ "cd \"" +	gitFolder +"\" && "
					+ "git checkout prod && "
					+ "git pull --progress -v --no-rebase \"origin\" prod && "
					+ "git checkout -b " + newBranch + " && "
					//+ "git pull --no-rebase && "
					+ "exit \"";
		cmd(command);
	}
	public void switchBranch(String branch) {
		String command;
		if (!branch.equals(getBranch())) {		
			if(!isLocalGIT(branch))
				createBranch(branch);
			else { 
				if(isRemoteGIT(branch)) 
					command = "cmd /c start cmd.exe /K \""
							+ "cd \"" +	gitFolder +"\" && "
							+ "git checkout "+branch + " && "
							+ "git.exe pull --no-rebase \"origin\" "+branch+" && "
							+ "exit \"";
				else
					command = "cmd /c start cmd.exe /K \""
							+ "cd \"" +	gitFolder +"\" && "
							+ "git checkout "+branch + " && "
							//+ "git.exe pull --no-rebase \"origin\" "+switchedBranch+" && "
							+ "exit \"";
				cmd(command);
			}
		}
	}
	public void commitAndPush(String comment) {
		String branch = getBranch();
		if(comment.equals("")|| comment == null)
			comment=branch;
		String command = "cmd /c start cmd.exe /K \""
				+ "cd \"" +	gitFolder +"\" && "
				+ "git add --all && "
				+ "git commit -v -a -m \""+comment+"\" && "
				+ "git checkout sevl && "
				+ "git pull && "
				+ "git merge " +branch+" && "
				+ "git push --progress \"origin\" sevl:sevl && "
				+ "git checkout " +branch+ " && "
				+ "git push --progress \"origin\" " + branch + " && "
				+ "exit \"";
		cmd(command);	
	}
	public void push(String comment) {
		String branch = getBranch();
		if(comment.equals("")|| comment == null) {
			comment=branch;
		}
		String command = "cmd /c start cmd.exe /K \""
				+ "cd \"" +	gitFolder +"\" && "
				+ "git add --all && "
				+ "git commit -v -a -m \""+comment+"\" && "
				+ "git push --progress \"origin\" " + branch + " && "
				+ "exit \"";
		cmd(command);	
	}
	public void fetch() {
		String command = "cmd /c start cmd.exe /K \""
				+ "cd \"" +	gitFolder +"\" && "
				+ "git fetch -v --progress \"origin\" && "
				+ "exit \"";
		cmd(command);	
	}
	public void hardReset() {
		String command = "cmd /c start cmd.exe /K \""
				+ "cd \"" +	gitFolder +"\" && "
				+ "git fetch && "
				+ "git reset --hard origin/master && "
				+ "git pull && "
				+ "exit \"";
		cmd(command);		
	}
	public void diff() {
		String command = "cmd /c start cmd.exe /K \""
				+ "cd \"" +	gitFolder +"\" && "
				+ "git.exe diff --name-only\"";
		cmd(command);		
	}
	public void pull() {
		String command = "cmd /c start cmd.exe /K \""
				+ "cd \"" +	gitFolder +"\" && "
				+ "git.exe pull --progress -v --no-rebase \"origin\" "+getBranch()+" && "
				+ "exit \"";
		cmd(command);		
	}
	private void readPreferenceFile(){
		//public_package.Preferences p = new public_package.Preferences();
		gitFolder=Preferences.contents.get("git");
		String sshLocation = Preferences.contents.get("ssh");
	}
	public String getSSH_RSA(){
		String line=null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(getUserHome()+ "\\.ssh\\id_rsa.pub"));
			line= br.readLine();
			line=line.trim();
			br.close();
		}
		catch (IOException e2) {
			e2.printStackTrace();
		}
		return line;
	}
	protected int okCancel(String message) {
		return JOptionPane.showConfirmDialog(
				null,
				message,
				"ALERT",
				JOptionPane.CANCEL_OPTION);
	}
	protected int yesNoCancel(String message) {
		return JOptionPane.showConfirmDialog(
				null,
				message,
				"ALERT",
				JOptionPane.YES_NO_CANCEL_OPTION);
	}
	protected int yesNo(String message) {
		return JOptionPane.showConfirmDialog(
				null,
				message,
				"ALERT",
				JOptionPane.YES_NO_OPTION);
	}
	public void deleteFile(String f) {
		File file = new File(f);
		if(file.exists())
			file.delete();
	}	
	public void readPacked_Refs(){
		String line;
		remoteBranches = new ArrayList<String>();
		String item;
		try {
			String[] parsedLine;
			BufferedReader br = new BufferedReader(new FileReader(gitFolder+ "\\.git\\packed-refs"));
			while ((line = br.readLine()) != null) {
				parsedLine = line.split("/");
				if(parsedLine.length>1) {
					item=parsedLine[parsedLine.length-1];
					remoteBranches.add(item); 
				}
			}
			br.close();
		}
		catch (IOException e2) {
			e2.printStackTrace();
		}
	}

	private void writeHostFile() {
		String host=
				"[fisheye01.ua.edu]:7999,[10.8.81.153]:7999 ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDSNpv15GY6jxbrfiTagxc1233xtqHhUfzGXtx+sCIzQUHWmZ0zEf0aYPD/xjNKE48oB3SZ0ciKoV/YT/D8x3Hm7E6P8CldunWjny/WPd4EGZLNd43vmD0xXVZeszL+T1zXnrBVVD0WU3Cdg0+Bk+bpoG/OAlis671+zXFBtMDD+U7IfKE892KQfXbjtJXsc88FLTR5Sr/xamQrC8xBhUGSIgN170m/y0/tG04uABcyCdHmyJVcU6+kf+x+mmbU1nL9NeRYWRcnYG/aqjAogV5vB8F8kFUW3OzYCXhqYR7XQlVJmVL38IAG67+0UEakr1G5v6Xa3FwccYL9J7jjZIs3\r\n";
		try {
			BufferedWriter output = new 
					BufferedWriter(new FileWriter(getUserHome()+"\\.ssh\\known_hosts", false));
			output.write(host);	
			output.close();	
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String getUserHome() {
		return System.getProperty("user.home");
	}
	public String getUserName() {
		return System.getProperty("user.name");
	}
	public boolean folderExists(String location) {
		File folder = new File(location);
        return folder.isDirectory();
	}
	public String validateInput(String input) {
		try{
			if(input.startsWith("BAN-")) {
				return input;
			}
			else if(Integer.parseInt(input)>0) {
				return "BAN-"+input;
			}else {
				return input;
			}
		}
		catch(NumberFormatException e) {
			return input;
		}

	}
	private boolean isLocalGIT(String branch) {
		File file = new File(gitFolder+"\\.git\\\\refs\\heads\\"+branch);
        return file.exists();
	}	
	public void generateKeys(String email) {
		File file = new File(getUserHome()+"\\.ssh");
		if(!file.isDirectory())
			file.mkdir();

		String command = "cmd start cmd.exe /K \"start \"\" \"%ProgramFiles%\\Git\\git-bash.exe\" -c \""
				+ "ssh-keygen -f /c/Users/"+getUserName()+"/.ssh/id_rsa -t rsa -N '' -C \""+email+"\" "
				//+ "&&  /usr/bin/bash --login -i" //keep window open
				+ "\"";
		writeHostFile();

		cmd(command);	
		//add host file...
		if(folderExists("H:\\")){
			String destination = "H:\\.ssh";
			File destDir = new File(destination);

			try {Thread.sleep(3000);// wait 3 seconds to allow files to transfer
			} catch (InterruptedException ignored) {}

			copyFolder(file, destDir);
		}
	}
	private void copyFolder(File source, File destination){
		if (source.isDirectory()){
			if (!destination.exists()){
				destination.mkdirs();
			}
            String[] files = source.list();

			for (String file : Objects.requireNonNull(files)){
				File srcFile = new File(source, file);
				File destFile = new File(destination, file);

				copyFolder(srcFile, destFile);
			}
		}
		else{
			InputStream in = null;
			OutputStream out = null;

			try{
				in = new FileInputStream(source);
				out = new FileOutputStream(destination);

				byte[] buffer = new byte[1024];

				int length;
				while ((length = in.read(buffer)) > 0){
					out.write(buffer, 0, length);
				}
			}
			catch (Exception e){
				try{
					Objects.requireNonNull(in).close();
				}
				catch (IOException e1){
					e1.printStackTrace();
				}

				try{
					Objects.requireNonNull(out).close();
				}
				catch (IOException e1){
					e1.printStackTrace();
				}
			}
		}
	}
	public void copyFolder(String sourceSTR, String destinationSTR){
		File source = new File(sourceSTR);
		File destination = new File(destinationSTR);

		copyFolder(source, destination);
	}

	public void openLink(String URL) {
		try {
			Desktop.getDesktop().browse(new URI(URL));
		} catch (IOException | URISyntaxException   e) {
			e.printStackTrace();
		} 	
	}
	public void addSSHPage() {
		try {
			Desktop.getDesktop().browse(new URI("http://fisheye01.ua.edu:7990/stash/plugins/servlet/ssh/account/keys/add"));
		} catch (IOException | URISyntaxException   e) {
			e.printStackTrace();
		} 	
	}
	private boolean sshKeysExist() {
		File folder = new File(getUserHome()+"\\.ssh");
		File public_rsa  = new File(getUserHome()+"\\.ssh\\id_rsa.pub");
		File private_rsa = new File(getUserHome()+"\\.ssh\\id_rsa");
		if(!folder.isDirectory() ) {
			return false;
		} 
		if(!public_rsa.exists() ) {
			return false;
		}
        return private_rsa.exists();

    }
	private boolean isRemoteGIT(String branch) {
        return remoteBranches.contains(branch);
	}	
	public String getBranch() {
		String line;
		String branch = null;
		try {
			BufferedReader br = new BufferedReader(
					new FileReader(gitFolder+"\\.git\\HEAD"));
			if ((line = br.readLine()) != null) {
				String[] parsedLine;
				parsedLine = line.split("/");
				branch = parsedLine[parsedLine.length-1].trim();
			}
			br.close();
		}
		catch (IOException e2) {
			e2.printStackTrace();
		}
		return branch;
	}
}
