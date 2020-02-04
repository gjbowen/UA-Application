package ar_package;

import com.sshtools.sftp.SftpClient;

class SFTP extends public_package.SFTP_Connection{

	SFTP(SftpClient conn_sftp, String user, String pass, String env) {
		super(user,pass,env);
		connection = conn_sftp;
	}

}
