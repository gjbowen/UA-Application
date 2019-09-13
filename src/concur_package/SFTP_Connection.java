package concur_package;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

	public List<String> getCurrentDownloaded() {
		List<String> allFiles = new ArrayList<String>();

		File rootF = new File(System.getProperty("user.home")+dirDelim+"Concur_Files");
		String rootPath = rootF.getAbsolutePath() ;
		if(!rootF.isDirectory())
			rootF.mkdir();


		File rootMisc = new File(System.getProperty("user.home")+dirDelim+"Concur_Files//misc");
		if(!rootMisc.isDirectory())
			rootMisc.mkdir();

		File environmentF = new File(rootPath+dirDelim+environment);
		if(!environmentF.isDirectory())
			environmentF.mkdir();	

		//update the root path to reflect the environment
		rootPath=rootPath+dirDelim+environment+dirDelim;

		File[] files;
		File emp1 = new File(rootPath + "Generated_Files");
		if(!emp1.isDirectory())
			emp1.mkdir();	

		
		File empF = new File(rootPath + "Employee_Files");
		if(!empF.isDirectory())
			empF.mkdir();	
		else{
			files=empF.listFiles();
			for(int i=0;i<files.length;++i)
				allFiles.add(files[i].getName());
		}
		File emp710 = new File(rootPath + "710_Feed_Files");
		if(!emp710.isDirectory())
			emp710.mkdir();	
		else{
			files=emp710.listFiles();
			for(int i=0;i<files.length;++i)
				allFiles.add(files[i].getName());
		}

		File saeF = new File(rootPath + "SAE_Files");
		if (!saeF.isDirectory())
			saeF.mkdir();
		else {
			files = saeF.listFiles();
			for(int i=0;i<files.length;++i)
				allFiles.add(files[i].getName());
		}
		File sreF = new File(rootPath + "SRE_Files");
		if (!sreF.isDirectory())
			sreF.mkdir();
		else {
			files = sreF.listFiles();
			for(int i=0;i<files.length;++i)
				allFiles.add(files[i].getName());
		}
		File praeF = new File(rootPath + "PRAE_Files");
		if (!praeF.isDirectory())
			praeF.mkdir();
		else {
			files = praeF.listFiles();
			for(int i=0;i<files.length;++i)
				allFiles.add(files[i].getName());
		}
		File f350 = new File(rootPath + "350_Files");
		if (!f350.isDirectory())
			f350.mkdir();
		else {
			files = f350.listFiles();
			for(int i=0;i<files.length;++i)
				allFiles.add(files[i].getName());
		}
		File vendor_feed = new File(rootPath + "Vendor_Feed_Files");
		if (!vendor_feed.isDirectory())
			vendor_feed.mkdir();
		else {
			files = vendor_feed.listFiles();
			for(int i=0;i<files.length;++i)
				allFiles.add(files[i].getName());
		}
		File vendor_term_feed = new File(rootPath + "Vendor_Term_Files");
		if (!vendor_term_feed.isDirectory())
			vendor_term_feed.mkdir();
		else {
			files = vendor_term_feed.listFiles();
			for(int i=0;i<files.length;++i)
				allFiles.add(files[i].getName());
		}
		File saeReports = new File(rootPath + "SAE_Report_Files");
		if (!saeReports.isDirectory()) 
			saeReports.mkdir();
		else {
			files = saeReports.listFiles();
			for(int i=0;i<files.length;++i)
				allFiles.add(files[i].getName());
		}
		File praeReports = new File(rootPath + "PRAE_Report_Files");
		if (!praeReports.isDirectory())
			praeReports.mkdir();
		else {
			files = praeReports.listFiles();
			for(int i=0;i<files.length;++i)
				allFiles.add(files[i].getName());
		}
		File activeDepartments = new File(rootPath + "Active_Departments");
		if (!activeDepartments.isDirectory())
			activeDepartments.mkdir();
		else {
			files = activeDepartments.listFiles();
			for(int i=0;i<files.length;++i)
				allFiles.add(files[i].getName());
		}
		File paymentRequest = new File(rootPath + "Payment_Request");
		if (!paymentRequest.isDirectory())
			paymentRequest.mkdir();
		else {
			files = paymentRequest.listFiles();
			for(int i=0;i<files.length;++i)
				allFiles.add(files[i].getName());
		}
		File paymentReceipt = new File(rootPath + "Payment_Receipt");
		if (!paymentReceipt.isDirectory())
			paymentReceipt.mkdir();
		else {
			files = paymentReceipt.listFiles();
			for(int i=0;i<files.length;++i)
				allFiles.add(files[i].getName());
		}
		return allFiles;
	}
	
	public void movePRAE(String dest,String fileName){
		moveFile(dest,"PRAE_Files",fileName,"prae.txt");
	}

	public void moveSAE(String dest,String fileName){
		moveFile(dest,"SAE_Files",fileName,"sae.txt");
	}
	public void moveSRE(String dest,String fileName){
		moveFile(dest,"SRE_Files",fileName,"sre.txt");
	}

	public void syncFiles() {
		try {
			List<String> myFiles = getCurrentDownloaded();
			connection.cd("/u03/archive/" + environment);
			//File f2 = new File("");
			String localPath = System.getProperty("user.home") + dirDelim+"Concur_Files"+dirDelim + environment + dirDelim;
			if(!public_package.Preferences.isWindows())
				localPath=System.getProperty("user.home") + dirDelim+"Concur_Files"+dirDelim + environment + dirDelim;
				
			
			connection.lcd(localPath);
			SftpFile[] sftpFiles = connection.ls();
			String fileName = null;
			System.out.println("Getting files...");
			String trimmedFileName = null;

			for (int i=0;i<sftpFiles.length;++i) {
				if (sftpFiles[i].getFilename().toLowerCase().contains("sae") ||
						sftpFiles[i].getFilename().toLowerCase().contains("sre") ||
						sftpFiles[i].getFilename().toLowerCase().contains("activedepartment") || 
						sftpFiles[i].getFilename().toLowerCase().contains("prae") || 
						sftpFiles[i].getFilename().toLowerCase().contains("concur_employee_feed_report") || 
						sftpFiles[i].getFilename().toLowerCase().contains("concur_employee_feed_305") || 
						sftpFiles[i].getFilename().toLowerCase().contains("concur_employee_feed_350") || 
						sftpFiles[i].getFilename().toLowerCase().contains("concur_employee_feed_710") || 
						sftpFiles[i].getFilename().toLowerCase().contains("concur_pymt_req") || 
						sftpFiles[i].getFilename().toLowerCase().contains("concur_pymt_receipt") || 
						sftpFiles[i].getFilename().toLowerCase().contains("vendor_term_feed") || 
						sftpFiles[i].getFilename().toLowerCase().contains("vendor_feed")) {
					fileName = sftpFiles[i].getFilename();
					trimmedFileName = fileName.endsWith(".gz") ? fileName.substring(0, fileName.length() - 3) : fileName;
					if (fileName.toLowerCase().contains("concur_employee_feed_305") && !myFiles.contains(trimmedFileName)) {
						connection.lcd(localPath + "Employee_Files");
						connection.get(fileName);
						if (fileName.endsWith(".gz")) {
							gunzipIt(fileName, localPath + "Employee_Files");
						}
					} 
					else if (fileName.toLowerCase().contains("concur_employee_feed_350") && !myFiles.contains(trimmedFileName)) {
						connection.lcd(localPath + "350_Files");
						connection.get(fileName);
						if (fileName.endsWith(".gz")) {
							gunzipIt(fileName, localPath + "350_Files");
						}
					} 
					else if (trimmedFileName.toLowerCase().contains("activedepartments") && !myFiles.contains(trimmedFileName)) {
						connection.lcd(localPath+ "Active_Departments");
						connection.get(fileName);
						if (fileName.endsWith(".gz")) {
							gunzipIt(fileName, localPath + "Active_Departments");
						}
					} 
					else if (fileName.toLowerCase().contains("sae") && !myFiles.contains(trimmedFileName)) {
						if (fileName.toLowerCase().contains("report")) {
							connection.lcd(localPath + "SAE_Report_Files");
							connection.get(fileName);
							if (fileName.endsWith(".gz")) {
								gunzipIt(fileName, localPath + "SAE_Report_Files");
							}
						} 
						else {
							connection.lcd(localPath + "SAE_Files");
							connection.get(fileName);
							if (fileName.endsWith(".gz")) {
								gunzipIt(fileName, localPath + "SAE_Files");
							}
						}
					}
					else if (fileName.toLowerCase().contains("prae") && !myFiles.contains(trimmedFileName)) {
						if (fileName.toLowerCase().contains("report")) {
							connection.lcd(localPath + "PRAE_Report_Files");
							connection.get(fileName);
							if (fileName.endsWith(".gz")) {
								gunzipIt(fileName, localPath + "PRAE_Report_Files");
							}
						}
						else {
							connection.lcd(localPath + "PRAE_Files");
							connection.get(fileName);
							if (fileName.endsWith(".gz")) {
								gunzipIt(fileName, localPath + "PRAE_Files");
							}
						}
					}
					else if (fileName.toLowerCase().contains("sre") && !myFiles.contains(trimmedFileName)) {
							connection.lcd(localPath + "SRE_Files");
							connection.get(fileName);
							if (fileName.endsWith(".gz")) {
								gunzipIt(fileName, localPath + "SRE_Files");
							}
					}
					else if (fileName.toLowerCase().contains("vendor_feed") && !myFiles.contains(trimmedFileName)) {
						connection.lcd(localPath + "Vendor_Feed_Files");
						connection.get(fileName);
						if (fileName.endsWith(".gz")) {
							gunzipIt(fileName, localPath + "Vendor_Feed_Files");
						}
					} 
					else if (fileName.toLowerCase().contains("vendor_term_feed") && !myFiles.contains(trimmedFileName)) {
						connection.lcd(localPath + "Vendor_Term_Files");
						connection.get(fileName);
						if (fileName.endsWith(".gz")) {
							gunzipIt(fileName, localPath + "Vendor_Term_Files");
						}
					}
					else if (fileName.toLowerCase().contains("concur_employee_feed_710") && !myFiles.contains(trimmedFileName)) {
						connection.lcd(localPath + "710_Feed_Files");
						connection.get(fileName);
						if (fileName.endsWith(".gz")) {
							gunzipIt(fileName, localPath + "710_Feed_Files");
						}
					}
					else if (fileName.toLowerCase().contains("concur_pymt_req") && !myFiles.contains(trimmedFileName)) {
						connection.lcd(localPath + "Payment_Request");
						connection.get(fileName);
						if (fileName.endsWith(".gz")) {
							gunzipIt(fileName, localPath + "Payment_Request");
						}
					}
					else if (fileName.toLowerCase().contains("concur_pymt_receipt") && !myFiles.contains(trimmedFileName)) {
						connection.lcd(localPath + "Payment_Receipt");
						connection.get(fileName);
						if (fileName.endsWith(".gz")) {
							gunzipIt(fileName, localPath + "Payment_Receipt");
						}
					}
				}
			}
		}
		catch (SftpStatusException | TransferCancelledException | SshException | IOException e2) {
			e2.printStackTrace();
		}
		System.out.println("Done getting files in " + environment + ".");
	}
}
