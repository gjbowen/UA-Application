package ar_package;

import com.jcraft.jsch.Session;
import com.sshtools.sftp.SftpStatusException;
import com.sshtools.ssh.SshException;

import java.io.*;

class SSH extends public_package.SSH_Connection {
    SSH(Session sess, String user, String pass, String env) {
        super(user, pass,env);
        session=sess;

    }
    private String getFileName() {
        return "C:\\Users\\"+user+"\\Box Sync\\Business Admin Team Shared\\AR\\Cadence SFTP.txt";
    }

    void writeEFile_RC(String file) {
        String line,sshPassword;
        sshPassword=getPassword(getFileName());
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(file));

            output.write("#!/usr/bin/expect -f\n");
            output.write("set timeout -1\n");
            output.write("spawn mkdir -p /home/"+user+"/RETURNED_CHECKS\n");
            output.write("spawn sftp uofalasas@filetransmissions.cadencebank.com:/ACH_OUT/ACH_RETURNS/* /home/"+user+"/RETURNED_CHECKS\n");
            output.write("expect \"uofalasas@filetransmissions.cadencebank.com's password: \"\n");
            output.write("send -- \""+sshPassword+"\\r\"\n");
            output.write("expect eof\n");
            output.close();
        }
        catch (IOException e2) {
            System.err.println("Cannot write to ");
        }
    }

    void writeEFile_LB(String file) {
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
