package public_package;

import com.jcraft.jsch.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class SSH_Connection {
    public Session session;
    public String host;
    protected final String user;
    private final String password;
    protected String fileName;
    public SSH_Connection(String userName, String pass, String env) {
        host = getInstance(env);
        user = userName;
        password = pass;
        session=null;
    }

    public void sshConnect(){
        System.out.println("Connecting SSH to " +user+"@"+ host+"...");

        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch jsch = new JSch();
        try {
            session=jsch.getSession(user, host, 22);
            session.setPassword(password);
            session.setConfig(config);
            session.connect();
            System.out.println("SSH Successful: "+user+"@"+host);
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }
    public String getInstance(String environment) {
        if (environment.equals("SEVL"))
            return "js-dev.ua.edu";
        else if (environment.equals("TEST"))
            return "js-test.ua.edu";
        else
            return "js-prod.ua.edu";
    }
    public void close(){session.disconnect();}

    public String ArrayList_to_String(ArrayList<String> array){
        StringBuilder str = new StringBuilder();
        for(int i=0;i<array.size();++i)
            str.append(array.get(i));
        return str.toString();
    }

    public String run(String command) {
        StringBuilder str = new StringBuilder();
        ArrayList<String> contents = new ArrayList<String>();
        try{
            Channel channel=session.openChannel("exec");
            ((ChannelExec)channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec)channel).setErrStream(System.err);

            InputStream in=channel.getInputStream();
            channel.connect();
            byte[] tmp=new byte[1024];
            while(true){
                while(in.available()>0){
                    int i=in.read(tmp, 0, 1024);
                    if(i<0)
                        break;
                    str.append(new String(tmp, 0, i));
                    System.out.print(new String(tmp, 0, i));
                }
                if(channel.isClosed())
                    break;
                try{
                    Thread.sleep(1000);
                }
                catch(Exception ee){}
            }
            channel.disconnect();
        }catch(Exception e){
            e.printStackTrace();
        }
        return str.toString();
    }

    protected String getPassword(String fileName){
        ArrayList<String> content = getFileCredentials(fileName);

        String password = null;
        String[] parsedLine=null;
        for(int i=0;i<content.size();++i){
            if(content.get(i).contains("="))
                parsedLine=content.get(i).split("=");
            else if(content.get(i).contains(":"))
                parsedLine=content.get(i).split(":");
            else if(content.get(i).contains(","))
                parsedLine=content.get(i).split(",");

            if(parsedLine[0].toLowerCase().contains("password"))
                password=parsedLine[1];
        }
        password.replace("\n","");
        password.replace("\r","");
        return password;
    }

    private ArrayList<String> getFileCredentials(String fileName){
        String line;
        ArrayList<String> content = new ArrayList<String>();
        File file = new File(fileName);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null)
                content.add(line);
            br.close();
        } catch (IOException e2) {
            System.err.println("File not found - "+file.getAbsolutePath());
            JFileChooser chooser = new JFileChooser(System.getProperty("user.home")+"\\Box Sync");
            chooser.setPreferredSize(new Dimension(700, 500));
            Action details = chooser.getActionMap().get("viewTypeDetails");
            details.actionPerformed(null);
            chooser.setDialogTitle("INVALID FILE - USE FILE IN \""+System.getProperty("user.home")+"\\Box Sync\\Business Admin Team Shared\"");
            if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                return getFileCredentials(chooser.getSelectedFile().getPath());
            else
                return null;
        }
        return content;
    }

}
