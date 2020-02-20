package ar_package;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.jcraft.jsch.Session;
import com.sshtools.sftp.SftpClient;

class Function_Library {
	final String environment;
	final String firstName;
	final JDBC jdbc;
	final SFTP sftp;
	final SSH ssh;
	final String userName;

	final String password;
	Function_Library(Connection conn_jdbc, SftpClient conn_sftp, Session conn_ssh, String user, String pass, String env) {
		jdbc=new JDBC(conn_jdbc,user,pass,env);
		sftp=new SFTP(conn_sftp,user,pass,env);
		ssh =new SSH(conn_ssh,user,pass,env);

		firstName=jdbc.getUserFirstName(user);
		environment = env;
		userName=user;
		password = pass;
	}

	void getCadence(String range) {
		if(range.equals("ALL")){
			Thread t1,t2;
			t1 = new Thread(() -> getLockBox("lScript1.sh","*", "*"));
			t1.start();

			t2 = new Thread(() -> getReturnedChecks("rScript1.sh","*", "*"));
			t2.start();
			while (t1.isAlive() || t2.isAlive()) {
				//holds off until both are done...
			}
		}
		else if(range.equals("RECENT")){
			Thread t1,t2,t3,t4;

			t1 = new Thread(() -> getLockBox("lScript1.sh",getMonth(), getYear()));
			t1.start();

			t2 = new Thread(() -> getReturnedChecks("rScript1.sh",getMonth(), getYear()));
			t2.start();
			while (t1.isAlive() || t2.isAlive()) {
				//holds off until both are done...
			}
			t3 = new Thread(() -> getLockBox("lScript2.sh",getLastMonth(),getLastMonthsYear()));
			t3.start();

			t4 = new Thread(() -> getReturnedChecks("rScript2.sh",getLastMonth(),getLastMonthsYear()));
			t4.start();

			while (t3.isAlive() || t4.isAlive()) {
				//holds off until both are done...
			}
		}

		ssh.run("chmod -R 0700 /home/"+userName);

		System.out.println("DONE GETTING FILES!");
	}
	private void getLockBox(String fileName, String month, String year){
		ssh.writeEFile_LB(fileName,month,year);
		sftp.putFile(System.getProperty("user.dir"),fileName,"/home/"+userName,fileName);
		sftp.lrm(System.getProperty("user.dir")+sftp.getDelim()+fileName);
		ssh.run("mkdir -p /home/"+userName+"/LOCKBOX");
		ssh.run("chmod -R 0700 /home/"+userName);
		ssh.run("./"+fileName);
		sftp.getFolder("/home/"+userName+"/LOCKBOX",sftp.makePath(new String[]{"Box Sync","SAS-OIT Shared","Cadence - Lockbox"}));
		sftp.rm("/home/"+userName+"/"+fileName);
	}
	private void getReturnedChecks(String fileName, String month, String year){
		ssh.writeEFile_RC(fileName,month,year);
		sftp.putFile(System.getProperty("user.dir"),fileName,"/home/"+userName,fileName);
		sftp.lrm(System.getProperty("user.dir")+sftp.getDelim()+fileName);
		ssh.run("mkdir -p /home/"+userName+"/RETURNED_CHECKS");
		ssh.run("chmod -R 0700 /home/"+userName);
		ssh.run("./"+fileName);
		sftp.getFolder("/home/"+userName+"/RETURNED_CHECKS",sftp.makePath(new String[]{"Box Sync","SAS-OIT Shared","Cadence - Returned Checks"}));
		sftp.rm("/home/"+userName+"/"+fileName);
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
	private String getMonth(){
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM");
		LocalDateTime now = LocalDateTime.now();

		String month =  dtf.format(now);
		return month;
	}
	private String getLastMonth(){
		String month=getMonth();
		int iMonth;
		if(month.equals("01"))
			iMonth = 12;
		else
			iMonth = Integer.parseInt(month) - 1;
		if(iMonth<10)
			return "0"+iMonth;
		else
			return ""+iMonth;
	}
	private String getYear(){
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yy");
		LocalDateTime now = LocalDateTime.now();

		String year =  dtf.format(now);
		return year;
	}
	private String getLastMonthsYear(){
		String year =  getYear();
		if(getLastMonth().equals("12"))
			return ""+(Integer.parseInt(year)-1);
		else
			return year;
	}
}