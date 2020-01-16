package file_fetch_package;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

import com.sshtools.sftp.SftpClient;
import com.sshtools.sftp.SftpFile;
import com.sshtools.sftp.SftpStatusException;
import com.sshtools.sftp.TransferCancelledException;
import com.sshtools.ssh.SshException;

class SFTP_Connection extends public_package.SFTP_Connection{

	SFTP_Connection(SftpClient conn_sftp,String user,String pass,String env) {
		super(env, user, pass);
		connection = conn_sftp;
	}

	//FUNCTION CALL:
	//connection.getFiles(textField.getText().trim(), location, mode, count);
	protected void getFiles(String fileName,String location,String mode,Integer count) {
		ArrayList<String> fileList = new ArrayList<String>();
		String pwd;
		if(location.equals("GURJOBS"))
			pwd = "/u03/banjobs/bin/gurjobs/"+environment;
		else
			pwd = "/u03/"+location.toLowerCase()+"/"+environment.toUpperCase();

		try {
			String localPath=
					System.getProperty("user.home")
							+ "\\Downloads\\"
							+ location.toLowerCase()
							+ "_"+mode.toLowerCase()
							+ "_"+fileName.toLowerCase();
			if(count==99999999) {
				localPath = localPath+"_all";
			}else if(count==1) {
				localPath = localPath+"_latest";
			}else {
				localPath = localPath+"_"+count;
			}

			File folder	= new File(localPath);

			if(!folder.isDirectory())
				folder.mkdir();

			connection.lcd(folder.getAbsolutePath());
			connection.cd(pwd);
			SftpFile[] sftpFiles = connection.ls();


			//get suspected files
			for (int i=0;i<sftpFiles.length;++i) {
				if(mode.equals("EQUALS")){
					if (sftpFiles[i].getFilename().toLowerCase().equals(fileName.toLowerCase())) {
						fileList.add(sftpFiles[i].getFilename());
					}
				}
				else if(mode.equals("CONTAINS")){
					if(sftpFiles[i].getFilename().toLowerCase().contains(fileName.toLowerCase()))
						fileList.add(sftpFiles[i].getFilename());
				}
				else if(mode.equals("STARTSWITH")){
					if(sftpFiles[i].getFilename().toLowerCase().startsWith(fileName.toLowerCase()))
						fileList.add(sftpFiles[i].getFilename());
				}
				else if(mode.equals("ENDSWITH")){
					if(sftpFiles[i].getFilename().toLowerCase().endsWith(fileName.toLowerCase()))
						fileList.add(sftpFiles[i].getFilename());
				}
			}

			// if we want more then there are, use the
			System.out.println("ALPHA count: "+count+"\t fileList.size(): "+fileList.size());
			if(count>fileList.size())
				count=fileList.size();
			System.out.println("BRAVO count: "+count+"\t fileList.size(): "+fileList.size());

			fileList.sort(Collections.reverseOrder());
			for(int i=0;i<=count-1;++i) {
				System.out.println(i+"/"+fileList.size());
				connection.get(fileList.get(i));
			}

			for(int i=0;i<fileList.size();++i)
				System.out.println(i+") "+fileList.get(i));

		}
		catch (SftpStatusException | SshException | TransferCancelledException | FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Done getting "+fileName+" in " + pwd);
		//sftpDisconnect();
	}

	ArrayList<String> getLatest(){

		return null;
	}


}
