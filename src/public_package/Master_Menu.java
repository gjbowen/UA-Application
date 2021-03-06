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

import com.jcraft.jsch.Session;
import com.sshtools.sftp.SftpClient;

import git_package.Function_Library;
import git_package.Main_Menu;


public class Master_Menu
{
	private JFrame frame;
	private Connection jdbc;
	private SftpClient sftp;
	private Session ssh;
	private  String firstName,userName,password;
	public String env;
	//	private JMenu option_button,environment_button,debug_button;
	private JButton arProgram,btnConcur,btnGitProgram,btnFileFetchProgram;

	public Master_Menu(Connection jdbc, String first, String userName, String password, SftpClient sftp, Session ssh, String env){
		this.jdbc=jdbc;
		this.userName=userName;
		this.password=password;
		this.sftp=sftp;
		this.ssh=ssh;
		this.env=env;
		this.firstName=first;
		initialize();
		setButtons();
	}

	public Master_Menu() {

	}

	public JMenuBar getMenuBar() {
		// menu bar
		JMenuItem m1,m3,m4,env_SEVL,env_TEST,env_PROD;
		JMenuBar mb;
		mb = new JMenuBar();

		// item on menu bar
		m4 = new JMenuItem("Sign Out");
		m1 = new JMenuItem("ENABLE");
		m3 = new JMenuItem("DISABLE");

		env_SEVL = new JMenuItem("SEVL");
		env_TEST = new JMenuItem("TEST");
		env_PROD = new JMenuItem("PROD");

		// pop-up menus
		JMenu option_button = new JMenu("Options");
		JMenu environment_button = new JMenu("Change Sessions");
		JMenu debug_button = new JMenu("Debug Mode");

		// add item to the drop-downs
		option_button.add(m4);

		debug_button.add(m1);
		debug_button.add(m3);
		if(Preferences.contents.get("debug").equals("true")) {
			m1.setEnabled(false);
			m3.setEnabled(true);
		}
		else {
			m1.setEnabled(true);
			m3.setEnabled(false);
		}

		if(env.equals("SEVL")) {
			environment_button.add(env_TEST);
			environment_button.add(env_PROD);
		}
		if(env.equals("TEST")) {
			environment_button.add(env_SEVL);
			environment_button.add(env_PROD);
		}
		if(env.equals("PROD")) {
			environment_button.add(env_SEVL);
			environment_button.add(env_TEST);
		}

		// add an item to the menu bar
		mb.add(option_button);
		mb.add(environment_button);
		mb.add(debug_button);

		m1.addActionListener(arg0 -> {
			m1.setEnabled(false);
			m3.setEnabled(true);
			public_package.Console console = new public_package.Console();
			Preferences.addPreference("debug", "true");
		});

		m3.addActionListener(arg0 -> {
			m1.setEnabled(true);
			m3.setEnabled(false);
			Preferences.addPreference("debug", "false");
		});
		// change environments
		env_SEVL.addActionListener(arg0 -> updateEnvironment("SEVL"));
		// change environments
		env_TEST.addActionListener(arg0 -> updateEnvironment("TEST"));
		// change environments
		env_PROD.addActionListener(arg0 -> updateEnvironment("PROD"));
		m4.addActionListener(arg0 -> {
			frame.dispose();
			Encryption.deleteFile();
			Login window1 = new Login(null);
		});
		return mb;
	}
	private void updateEnvironment(String env) {
		this.env = env;

		System.out.println("Switching to "+env+"..");

		JDBC_Connection jdbc_connection =  new JDBC_Connection(userName,password,env);
		jdbc_connection.jdbcConnect();
		jdbc=jdbc_connection.connection;

		SFTP_Connection sftp_connection = new SFTP_Connection(userName, password, this.env);
		sftp_connection.sftpConnect();
		sftp = sftp_connection.connection;

		SSH_Connection ssh_connection = new SSH_Connection(userName,password,this.env);
		ssh_connection.sshConnect();;
		ssh=ssh_connection.session;

		frame.dispose();
		Preferences.addPreference("environment", this.env);

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
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Master_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
		frame.setTitle("Main Menu ("+env+") - Welcome, " + firstName);
		frame.setBounds(100, 100, 445, 336);
		frame.setDefaultCloseOperation(3);
		frame.getContentPane().setLayout(null);
		frame.setJMenuBar(getMenuBar());

		JButton button_exit = new JButton("Exit");
		button_exit.setFont(new Font("Lucida Grande", 0, 15));
		button_exit.setBounds(6, 208, 122, 57);
		frame.getContentPane().add(button_exit);

		JPanel panel = new JPanel();
		panel.setBounds(15, 11, 393, 186);
		frame.getContentPane().add(panel);
		panel.setLayout(new GridLayout(0, 2, 5, 5));

		btnConcur = new JButton("Concur Application");
		btnConcur.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnConcur.addActionListener(arg0 -> EventQueue.invokeLater(() -> {
			try {
				concur_package.Main_Menu window = new concur_package.Main_Menu(jdbc, sftp,ssh, userName, password, env);
				window.frame.setVisible(true);
				frame.setVisible(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}));
		panel.add(btnConcur);

		arProgram = new JButton("AR Application");
		arProgram.setFont(new Font("Tahoma", Font.PLAIN, 16));
		arProgram.addActionListener(arg0 -> {
			frame.setVisible(false);
			EventQueue.invokeLater(() -> {
				try {
					ar_package.Main_Menu window = new ar_package.Main_Menu(jdbc, sftp, ssh, userName, password, env);
					window.frame.setVisible(true);
					frame.setVisible(false);
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
						frame.setVisible(false);
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
				file_fetch_package.FileFetcher window = new file_fetch_package.FileFetcher(sftp,ssh, firstName, userName, password, env);
				window.frame.setVisible(true);
				frame.dispose();
				frame.setVisible(false);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}));
		panel.add(btnFileFetchProgram);

		button_exit.addActionListener(e -> {
			Exit_Confirmation window = new Exit_Confirmation(firstName);
			window.frame.setVisible(true);
		});
		frame.setVisible(true);
		setButtons();
	}
}
