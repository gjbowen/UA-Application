package public_package;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import com.sshtools.sftp.SftpClient;

import git_package.Function_Library;
import git_package.Main_Menu;


public class Master_Menu
{
	JFrame frameMenu;
	Connection jdbc;
	SftpClient sftp;
	String userName;
	private String password;
	String environment;
	private String firstName;
	JMenu option_button,help_button,environment_button;
    JButton arProgram,btnConcur,btnGitProgram,btnFileFetchProgram;

	public Master_Menu(Connection conn_jdbc, String first, String user, String pass, SftpClient conn_sftp, String env){
		jdbc=conn_jdbc;
		userName=user;
		password=pass;
		sftp=conn_sftp;
		environment=env;
		firstName=first;
		initialize();
		setButtons();
	}
	
	private void addMenuBar() {
		// menu bar
		JMenuItem m1,m2,m4,env_SEVL,env_TEST,env_PROD;
		JMenuBar mb;
		mb = new JMenuBar();
		frameMenu.setJMenuBar(mb);

		// item on menu bar
		m1 = new JMenuItem("Preferences");
		m4 = new JMenuItem("Sign Out");
		
		m2 = new JMenuItem("About");

		env_SEVL = new JMenuItem("SEVL");
		env_TEST = new JMenuItem("TEST");
		env_PROD = new JMenuItem("PROD");

		// pop-up menus
		option_button = new JMenu("Options");
		help_button = new JMenu("Help");
		environment_button = new JMenu("Change Sessions");
		
		// add item to the drop-downs
		option_button.add(m1);
		option_button.add(m4);
		
		help_button.add(m2);

		if(environment.equals("SEVL")) {
			environment_button.add(env_TEST);
			environment_button.add(env_PROD);
		}
		if(environment.equals("TEST")) {
			environment_button.add(env_SEVL);
			environment_button.add(env_PROD);
		}
		if(environment.equals("PROD")) {
			environment_button.add(env_SEVL);
			environment_button.add(env_TEST);
		}
		
		// add an item to the menu bar
		mb.add(option_button);
		mb.add(help_button);
		mb.add(environment_button);
		
		m1.addActionListener(arg0 -> {

		});
		m2.addActionListener(arg0 -> {

		});
		// change environments
		env_SEVL.addActionListener(arg0 -> updateEnvironment("SEVL"));
		// change environments
		env_TEST.addActionListener(arg0 -> updateEnvironment("TEST"));
		// change environments
		env_PROD.addActionListener(arg0 -> updateEnvironment("PROD"));
		m4.addActionListener(arg0 -> {
			frameMenu.dispose();
			Encryption.deleteFile();
			Login window1 = new Login(null);
		});
		
	}
	private void updateEnvironment(String env) {
		environment = env;

		Preferences.addPreference("environment", environment);
		
		System.out.println("Switching to "+environment+"..");
		JDBC_Connection jdbc_connection =  new JDBC_Connection(environment, userName,password);
		jdbc_connection.jdbcConnect();
		jdbc=jdbc_connection.connection;
		
		SFTP_Connection sftp_connection = new SFTP_Connection(environment, userName,password);		
		sftp_connection.sftpConnect();
		sftp=sftp_connection.connection;
		frameMenu.dispose();
		Preferences.addPreference("environment", environment);
		initialize();
	}
	private void setButtons(){
		//fix to those that don't have OIT access for SFTP.
		if(sftp==null||sftp.isClosed()){
			if(btnConcur!=null)
				btnConcur.setEnabled(false);
			if(btnGitProgram!=null)
				btnGitProgram.setEnabled(false);
			if(btnFileFetchProgram!=null)
				btnFileFetchProgram.setEnabled(false);
		}
	}

	private void initialize(){
		frameMenu = new JFrame();
		frameMenu.setIconImage(Toolkit.getDefaultToolkit().getImage(Master_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
		frameMenu.setTitle("Main Menu ("+environment+") - Welcome, " + firstName);
		frameMenu.setBounds(100, 100, 445, 336);
		frameMenu.setDefaultCloseOperation(3);
		frameMenu.getContentPane().setLayout(null);
		addMenuBar();
		
		JButton button_exit = new JButton("Exit");
		button_exit.setFont(new Font("Lucida Grande", 0, 15));
		button_exit.setBounds(6, 208, 122, 57);
		frameMenu.getContentPane().add(button_exit);

		JPanel panel = new JPanel();
		panel.setBounds(15, 11, 393, 186);
		frameMenu.getContentPane().add(panel);
		panel.setLayout(new GridLayout(0, 2, 5, 5));

		btnConcur = new JButton("Concur Application");
		btnConcur.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnConcur.addActionListener(arg0 -> EventQueue.invokeLater(() -> {
			try {
				concur_package.Main_Menu window = new concur_package.Main_Menu(jdbc, sftp, userName, password, environment);
				window.frameMenu.setVisible(true);
				frameMenu.setVisible(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}));
		panel.add(btnConcur);

		arProgram = new JButton("AR Application");
		arProgram.setFont(new Font("Tahoma", Font.PLAIN, 16));
		arProgram.addActionListener(arg0 -> {
			frameMenu.setVisible(false);
			EventQueue.invokeLater(() -> {
				try {
					ar_package.Main_Menu window = new ar_package.Main_Menu(jdbc, sftp, userName, password, environment);
					window.frameMenu.setVisible(true);
					frameMenu.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		});
		panel.add(arProgram);


		btnGitProgram = new JButton("GIT Program");
		btnGitProgram.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnGitProgram.addActionListener(arg0 -> {
			Function_Library f = new Function_Library();
			EventQueue.invokeLater(() -> {
				try {
					Main_Menu window = new Main_Menu(f);
					if(window.done)
						frameMenu.setVisible(false);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			});
		});
		panel.add(btnGitProgram);

		btnFileFetchProgram = new JButton("File Fetch Program");
		btnFileFetchProgram.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnFileFetchProgram.addActionListener(arg0 -> EventQueue.invokeLater(() -> {
			try {
				file_fetch_package.FileFetcher window = new file_fetch_package.FileFetcher(sftp, firstName, userName, password, environment);
				window.frameMenu.setVisible(true);
				frameMenu.dispose();
				frameMenu.setVisible(false);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}));
		panel.add(btnFileFetchProgram);

		button_exit.addActionListener(e -> {
			Exit_Confirmation window = new Exit_Confirmation(firstName);
			window.frameExitConfirmation.setVisible(true);
		});
		frameMenu.setVisible(true);
		setButtons();
	}
}
