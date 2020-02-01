package ar_package;

import com.jcraft.jsch.Session;

import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

class SSH_Connection extends public_package.SSH_Connection {
    SSH_Connection(Session sess, String env, String user, String pass) {
        super(env, user, pass);
        session=sess;
        setFileName();

        //writeExpectFile("eFile.txt");
        // sftp expect file
        // chmod
        // run ssh script
        // sftp files back
    }


    void setFileName() {
        fileName = "C:\\Users\\"+user+"\\Box Sync\\Business Admin Team Shared\\AR\\Cadence SFTP.txt";
    }
    void writeExpectFile(String eFile) {
        String line,sshPassword=null;
        sshPassword=getPassword(fileName);
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(eFile));

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
