package ar_package;

import com.jcraft.jsch.Session;
import com.sshtools.sftp.SftpStatusException;
import com.sshtools.ssh.SshException;

import java.io.*;

class SSH extends public_package.SSH_Connection {
    SSH(Session sess, String env, String user, String pass) {
        super(env, user, pass);
        session=sess;

    }
    private String getFileName() {
        return "C:\\Users\\"+user+"\\Box Sync\\Business Admin Team Shared\\AR\\Cadence SFTP.txt";
    }
    void writeExpectFile(String file) {
        String line,sshPassword;
        sshPassword=getPassword(getFileName());
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(file));

            output.write("#!/usr/bin/expect -f\n");
            output.write("set timeout -1\n");
            output.write("spawn mkdir -p /home/"+user+"/LOCKBOX\n");
            output.write("spawn sftp uofalasas@filetransmissions.cadencebank.com:/LBX_OUT/1001121.LBXo."+"* /home/"+user+"/LOCKBOX\n");
            output.write("expect \"uofalasas@filetransmissions.cadencebank.com's password: \"\n");
            output.write("send -- \""+sshPassword+"\\r\"\n");
            output.write("expect eof\n");
            output.close();
        }
        catch (IOException e2) {
            System.err.println("Cannot write to ");
        }
    }
}
