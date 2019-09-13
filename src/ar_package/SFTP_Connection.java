package ar_package;

import com.sshtools.sftp.SftpClient;
import com.sshtools.sftp.SftpFile;
import com.sshtools.sftp.SftpStatusException;
import com.sshtools.sftp.TransferCancelledException;
import com.sshtools.ssh.SshException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class SFTP_Connection extends public_package.SFTP_Connection{

	SFTP_Connection(SftpClient conn_sftp, String user, String pass, String env) {
		super(env, user, pass);
		connection = conn_sftp;
	}

}
