package ar_package;

import com.sshtools.sftp.SftpClient;

class SFTP_Connection extends public_package.SFTP_Connection{

	protected SFTP_Connection(SftpClient conn_sftp, String user, String pass, String env) {
		super(env, user, pass);
		connection = conn_sftp;
	}

}
