package git_package;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
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
	public String getUserHome() {
		return System.getProperty("user.home");
	}
	public String getUserName() {
		return System.getProperty("user.name");
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
			gitFolder=public_package.Preferences.contents.get("git");
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
	private String cmd(String command) {
		StringBuilder str = new StringBuilder();
		try {
			System.out.println(command);
			if(isWindows())
				Runtime.getRuntime().exec("cmd /c start cmd.exe /K \""+command);
			else {
				try {
					BufferedWriter output = new
							BufferedWriter(new FileWriter(getUserHome()+"/commands.sh",false));
					output.write("#!/bin/bash\n");
					output.write(command.replace("&& ","\n"));
					output.close();
					File f = new File(getUserHome()+"/commands.sh");
					f.setExecutable(true);
					ProcessBuilder pb = new ProcessBuilder(getUserHome()+"/commands.sh");
					Process p = pb.start();
					BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String line = null;
					while ((line = reader.readLine()) != null) {
						str.append(line+"\n");
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
		return str.toString();
	}
	public void cloneGIT(String location) {
		System.err.println("Attempting clone..");
		if(isWindows()) {
			String command =  "cd \""+ location +"\" && "
					+ "git clone ssh://git@fisheye01.ua.edu:7999/ban/uabanner.git && "
					+ "exit";
			cmd(command);

		} else{
			String command =
					"cd \""+location+"\"\n" +
					"git clone ssh://git@fisheye01.ua.edu:7999/ban/uabanner.git\n";
			cmd(command);
		}
		System.err.println("Clone complete!");

	}
	private void createBranch(String newBranch) {
		if(hasRemoteBranch(newBranch))
			cmd("cd \"" +	gitFolder +"\" && "
							+ "git checkout prod && "
							+ "git pull --progress -v --no-rebase \"origin\" prod && "
							+ "git checkout -b " + newBranch + " && "
							+ "git pull --progress -v --no-rebase \"origin\" "+newBranch +" && "
							+ "exit ");
		else
			cmd("cd \"" +	gitFolder +"\" && "
							+ "git checkout prod && "
							+ "git pull --progress -v --no-rebase \"origin\" prod && "
							+ "git checkout -b " + newBranch + " && "
							//+ "git pull --no-rebase && "
							+ "exit");
	}
	public void switchBranch(String branch) {
		if (!branch.equals(getCurrentBranch())) {
			if(!isLocalBranch(branch))
				createBranch(branch);
			else {
				if(hasRemoteBranch(branch))
					cmd("cd \"" +	gitFolder +"\" && "
									+ "git checkout "+branch + " && "
									+ "git pull --no-rebase \"origin\" "+branch+" && "
									+ "exit");
				else
					cmd("cd \"" +	gitFolder +"\" && "
									+ "git checkout "+branch + " && "
									//+ "git pull --no-rebase \"origin\" "+switchedBranch+" && "
									+ "exit");
			}
		}
	}
	public void commitAndPush(String comment) {
		String branch = getCurrentBranch();
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
		String branch = getCurrentBranch();
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
						+ "git pull --progress -v --no-rebase \"origin\" "+getCurrentBranch()+" ";
		cmd(command);
	}
	//////////////////////////////////////////////////////////////////////////////////////////////
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
	public void getRemoteBranchList(){
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
	public void generateKeys(String email) {
		File file = new File(getUserHome()+"/.ssh");
		if(!file.isDirectory())
			file.mkdir();

		if(isWindows()) {
			cmd("cmd start cmd.exe /K \"start \"\" \"%ProgramFiles%\\Git\\git-bash.exe\" -c \""
					+ "ssh-keygen -f /c/Users/" + getUserName() + "/.ssh/id_rsa -t rsa -N '' -C \"" + email + "\" "
					//+ "&&  /usr/bin/bash --login -i" //keep window open
					+ "\"");
		}
		else{
			cmd("rm -Rf ~/.ssh\n" +
					"mkdir ~/.ssh\n" +
					"ssh-keygen -f ~/.ssh/id_rsa -t rsa -N '' -C \"" + email + "\"");
		}
		//add host file...
		writeHostFile();

		if(isWindows() && folderExists("H:\\")){
			File destDir = new File("H:\\.ssh");

			try {Thread.sleep(3000);// wait 3 seconds to allow files to transfer
			} catch (InterruptedException ignored) {}

			copyFolder(file, destDir);
		}
	}

	public void deleteFile(String f) {
		File file = new File(f);
		if(file.exists())
			file.delete();
	}
	public boolean folderExists(String location) {
		File folder = new File(location);
		return folder.isDirectory();
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
		openLink("http://fisheye01.ua.edu:7990/stash/plugins/servlet/ssh/account/keys/add");
	}
	private boolean hasRemoteBranch(String branch) {
		return remoteBranches.contains(branch);
	}
	private boolean isLocalBranch(String branch) {
		File file = new File(gitFolder+"/.git/refs/heads/"+branch);
		return file.exists();
	}

	public String getCurrentBranch() {
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
