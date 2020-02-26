package git_package;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import javax.swing.JOptionPane;

import public_package.Preferences;
public class Function_Library {
	String gitFolder;
	final String userName;
	private ArrayList<String> remoteBranches;

	public Function_Library() {
		userName = getUserName();
	}
	boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().contains("windows");
	}
	void info(){
		String nameOS = "os.name";
		String versionOS = "os.version";
		String architectureOS = "os.arch";
		System.out.println("\n    The information about OS");
		System.out.println("\nName of the OS: " +
				System.getProperty(nameOS));
		System.out.println("Version of the OS: " +
				System.getProperty(versionOS));
		System.out.println("Architecture of THe OS: " +
				System.getProperty(architectureOS));
	}
	private boolean done(){
		if(isWindows())
			return Preferences.contents.containsKey("git")
					&& sshKeysExist()
					&& GITprogramExists()
					&& tortoiseProgramExists();
		else
			return Preferences.contents.containsKey("git")
					&& sshKeysExist();
	}
	public boolean ready(){
		if(done()) {
			readPreferenceFile();
			return true;
		}
		else {
			int val=0;
			if(isWindows()) {
				if (!tortoiseProgramExists()) {
					val = yesNo("Tortoise was not found. Would you like to install it now? (During the install process, it will install GIT)");
					if (val == 0)
						downloadTortoise();
				}
				else if (!GITprogramExists()) {
					val = yesNo("GIT was not found. Would you like to install it now?");
					if (val == 0)
						downloadGIT();
				}
			}
			if (!sshKeysExist()) {
				val = yesNo("SSH keys were not found. Would you like to setup now?");
				if (val == 0) {
					EventQueue.invokeLater(() -> {
						try {
							SSH_Menu sshWindow = new SSH_Menu(this);
							sshWindow.frame.setVisible(true);
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
		}
		return false;
	}

	boolean GITfolderExists(String location) {
		File folder = new File(location+"/.git");
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
	private void downloadGIT() {
		try {
			Desktop.getDesktop().browse(new URI("https://git-scm.com/download"));
		} catch (IOException | URISyntaxException   e) {
			e.printStackTrace();
		}
	}
	private void downloadTortoise() {
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
			System.out.println("command: "+command);
			if(isWindows())
				Runtime.getRuntime().exec("cmd /c start cmd.exe /K \""+command);
			else {
				try {
					BufferedWriter output = new
							BufferedWriter(new FileWriter(getUserHome()+"/commands.sh",false));
					output.write("#!/bin/bash\n");
					output.write(command);
					output.close();
					File f = new File(getUserHome()+"/commands.sh");
					f.setExecutable(true);
					ProcessBuilder pb = new ProcessBuilder(getUserHome()+"/commands.sh");
					Process p = pb.start();
					BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String line = null;
					while ((line = reader.readLine()) != null) {
						System.out.println(line);
					}
					f.delete();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	public void cloneGIT(String location) {
		String command =  "cd \""+ location +"\" && "
				+ "git clone ssh://git@fisheye01.ua.edu:7999/ban/uabanner.git && "
				+ "exit\"";
		cmd(command);
	}
	private void createBranch(String newBranch) {
		String command;
		if(isRemoteGIT(newBranch))
			command=
					"cd \"" +	gitFolder +"\" && "
							+ "git checkout prod && "
							+ "git pull --progress -v --no-rebase \"origin\" prod && "
							+ "git checkout -b " + newBranch + " && "
							+ "git pull --progress -v --no-rebase \"origin\" "+newBranch +" && "
							+ "exit ";
		else
			command=
					"cd \"" +	gitFolder +"\" && "
							+ "git checkout prod && "
							+ "git pull --progress -v --no-rebase \"origin\" prod && "
							+ "git checkout -b " + newBranch + " && "
							//+ "git pull --no-rebase && "
							+ "exit";
		cmd(command);
	}
	public void switchBranch(String branch) {
		System.out.println("gitFolder: "+gitFolder);
		String command;
		if (!branch.equals(getBranch())) {
			if(!isLocalGIT(branch))
				createBranch(branch);
			else {
				if(isRemoteGIT(branch))
					command =
							"cd \"" +	gitFolder +"\" && "
									+ "git checkout "+branch + " && "
									+ "git pull --no-rebase \"origin\" "+branch+" && "
									+ "exit";
				else
					command =
							"cd \"" +	gitFolder +"\" && "
									+ "git checkout "+branch + " && "
									//+ "git pull --no-rebase \"origin\" "+switchedBranch+" && "
									+ "exit";
				cmd(command);
			}
		}
	}
	public void commitAndPush(String comment) {
		String branch = getBranch();
		if(comment.equals("")|| comment == null)
			comment=branch;
		String command =
				"cd \"" +	gitFolder +"\" && "
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
		String command =
				"cd \"" +	gitFolder +"\" && "
						+ "git add --all && "
						+ "git commit -v -a -m \""+comment+"\" && "
						+ "git push --progress \"origin\" " + branch + " && "
						+ "exit \"";
		cmd(command);
	}
	public void fetch() {
		String command =
				"cd \"" +	gitFolder +"\" && "
						+ "git fetch -v --progress \"origin\" && "
						+ "exit \"";
		cmd(command);
	}
	public void hardReset() {
		String command =
				"cd \"" +	gitFolder +"\" && "
						+ "git fetch && "
						+ "git reset --hard origin/master && "
						+ "git pull && "
						+ "exit \"";
		cmd(command);
	}
	public void diff() {
		String command =
				"cd \"" +	gitFolder +"\" && "
						+ "git diff --name-only\"";
		cmd(command);
	}
	public void pull() {
		String command =
				"cd \"" +	gitFolder +"\" && "
						+ "git pull --progress -v --no-rebase \"origin\" "+getBranch()+" ";
		cmd(command);
	}

	private void readPreferenceFile(){
		//public_package.Preferences p = new public_package.Preferences();
		gitFolder=Preferences.contents.get("git");
	}
	public String getSSH_RSA(){
		String line=null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(getUserHome()+ "/.ssh/id_rsa.pub"));
			line= br.readLine();
			line=line.trim();
			br.close();
		}
		catch (IOException e2) {
			e2.printStackTrace();
		}
		return line;
	}
	int okCancel(String message) {
		return JOptionPane.showConfirmDialog(
				null,
				message,
				"ALERT",
				JOptionPane.CANCEL_OPTION);
	}

	private int yesNo(String message) {
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
			BufferedReader br = new BufferedReader(new FileReader(gitFolder+ "/.git/packed-refs"));
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
					BufferedWriter(new FileWriter(getUserHome()+"/.ssh/known_hosts", false));
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
			if(input.startsWith("BAN-"))
				return input;
			else if(Integer.parseInt(input)>0)
				return "BAN-"+input;
			else
				return input;
		}
		catch(NumberFormatException e) {
			return input;
		}

	}
	private boolean isLocalGIT(String branch) {
		File file = new File(gitFolder+"/.git/refs/heads/"+branch);
		return file.exists();
	}
	public void generateKeys(String email) {
		writeHostFile();
		File file = new File(getUserHome()+"/.ssh");
		if(!file.isDirectory())
			file.mkdir();

		String command = "cmd start cmd.exe /K \"start \"\" \"%ProgramFiles%\\Git\\git-bash.exe\" -c \""
				+ "ssh-keygen -f /c/Users/"+getUserName()+"/.ssh/id_rsa -t rsa -N '' -C \""+email+"\" "
				//+ "&&  /usr/bin/bash --login -i" //keep window open
				+ "\"";

		if(isWindows())
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

	public void openLink(String url) {
		if(isWindows())
			try {
				Desktop.getDesktop().browse(new URI(url));
			} catch (IOException | URISyntaxException   e) {
				e.printStackTrace();
			}
		else{
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				try {
					Desktop.getDesktop().browse(new URI(url));
				} catch (IOException e) {
					e.printStackTrace();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
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
		File folder = new File(getUserHome()+"/.ssh");
		File public_rsa  = new File(getUserHome()+"/.ssh/id_rsa.pub");
		File private_rsa = new File(getUserHome()+"/.ssh/id_rsa");
		if(!folder.isDirectory()) {
			System.out.println("1");
			return false;
		}
		else if( !public_rsa.exists() ) {
			System.out.println("2");
			return false;
		}
		else if( !private_rsa.exists()) {
			System.out.println("3");
			return false;
		}
		else
			return true;
	}
	private boolean isRemoteGIT(String branch) {
		return remoteBranches.contains(branch);
	}
	public String getBranch() {
		String line;
		String branch = null;
		try {
			BufferedReader br = new BufferedReader(
					new FileReader(gitFolder+"/.git/HEAD"));
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
