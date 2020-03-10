package public_package;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import ar_package.Football_Menu;
import git_package.Function_Library;

public class Login
{
	private JTextField userField;
	private JPasswordField passwordField;
	private String env="PROD";
	private JLabel status;
	private JFrame frameLogin;
	private JCheckBox chckbxSaveLogin,debugMode;
	private boolean debug;
	public Login(String app){
		this.initialize(app);
	}
	@SuppressWarnings("deprecation")
	private void connect(String app) {
		if(debug||debugMode.isSelected()){
			Preferences.addPreference("debug","true");
			public_package.Console console = new public_package.Console();
		}

		JDBC_Connection jdbc = new JDBC_Connection(userField.getText(), passwordField.getText(),env);
		SFTP_Connection sftp = new SFTP_Connection(userField.getText(), passwordField.getText(),env);
		SSH_Connection ssh = new SSH_Connection(userField.getText(), passwordField.getText(),env);

		Thread t1 = new Thread(() -> jdbc.jdbcConnect());
		t1.start();

		if(app!=null && app.equals("football")){
			while (t1.isAlive()) {
				//holds off until its done...
			}
		}
		else{
			Thread t2 = new Thread(() -> sftp.sftpConnect());
			t2.start();
			Thread t3 = new Thread(() -> ssh.sshConnect());
			t3.start();

			while (t1.isAlive() ||t2.isAlive() ||t3.isAlive()  ) {
				//holds off until both are done...
			}
		}






		if(jdbc.connected()) {
			frameLogin.dispose(); // hide login menu
			//use for passing in parameters via shortcuts in Windows
			if(app==null) {
				new Master_Menu(
						jdbc.connection,
						jdbc.userFirstName,
						userField.getText(),
						JDBC_Connection.password,
						sftp.connection,
						ssh.session,
						env);
			}
			else if(app.equals("concur"))
				new concur_package.Main_Menu(jdbc.connection,sftp.connection,ssh.session,userField.getText(), JDBC_Connection.password,env);
			else if(app.equals("ar"))
				new ar_package.Main_Menu(jdbc.connection,sftp.connection,ssh.session,userField.getText(), JDBC_Connection.password,env);
			else if(app.equals("git"))
				new git_package.Main_Menu(new Function_Library());
			else if(app.equals("football"))
				new ar_package.Football_Menu  ( new ar_package.Function_Library(jdbc.connection,sftp.connection,ssh.session,userField.getText(),JDBC_Connection.password,env));;

			if(chckbxSaveLogin.isSelected())
				this.save();

		}
		else {
			Encryption.deleteFile();
			status.setForeground(Color.RED);
			status.setText("INVALID USERNAME OR PASSWORD");
			frameLogin.setVisible(true);
		}
	}




	public String getEnv() {
		return env;
	}

	private void setEnv(String env) {
		this.env = env;
	}

	@SuppressWarnings("deprecation")
	private void initialize(String app){
		frameLogin = new JFrame();
		frameLogin.setIconImage(Toolkit.getDefaultToolkit().getImage(Master_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
		frameLogin.setTitle("Login Menu");
		frameLogin.setBounds(100, 100, 382, 311);
		frameLogin.setDefaultCloseOperation(3);
		frameLogin.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout)panel.getLayout();
		flowLayout_1.setAlignment(0);
		panel.setBounds(10, 24, 183, 122);
		frameLogin.getContentPane().add(panel);
		JLabel lblUsername = new JLabel("Username");
		panel.add(lblUsername);

		userField = new JTextField();
		panel.add(userField);
		userField.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		panel.add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setColumns(10);
		panel.add(passwordField);

		chckbxSaveLogin = new JCheckBox("Save login");
		chckbxSaveLogin.setEnabled(true);
		panel.add(chckbxSaveLogin);

		debugMode = new JCheckBox("Enable Debug Mode");
		debugMode.setSelected(false);
		panel.add(debugMode);


		JButton button_Submit = new JButton("Submit");

		status = new JLabel("Status: Ready");
		status.setFont(new Font("Dialog", Font.BOLD, 14));
		status.setBounds(12, 170, 350, 37);
		status.setForeground(Color.BLACK);
		frameLogin.getContentPane().add(status);

		button_Submit.addActionListener(e -> {
			status.setText("Connecting now..");
			if ((userField.getText().isEmpty()) || (passwordField.getText().isEmpty())){
				status.setForeground(Color.RED);
				status.setText("ENTER USERNAME AND PASSWORD");
			}
			else
				this.connect(app);
		});
		button_Submit.setBounds(10, 207, 91, 55);
		frameLogin.getContentPane().add(button_Submit);

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout)panel_1.getLayout();
		flowLayout.setAlignment(0);
		panel_1.setBounds(283, 24, 73, 86);
		frameLogin.getContentPane().add(panel_1);

		final JRadioButton rdbtnSEVL = new JRadioButton("SEVL");
		panel_1.add(rdbtnSEVL);
		final JRadioButton rdbtnTEST = new JRadioButton("TEST");
		panel_1.add(rdbtnTEST);
		final JRadioButton rdbtnPROD = new JRadioButton("PROD");
		panel_1.add(rdbtnPROD);
		rdbtnPROD.setSelected(true);

		JButton btnExit = new JButton("Exit");
		btnExit.setBounds(267, 207, 91, 55);
		frameLogin.getContentPane().add(btnExit);

		btnExit.addActionListener(e -> System.exit(0));
		rdbtnSEVL.addActionListener(e -> {
			if (rdbtnSEVL.isSelected()){
				this.setEnv("SEVL");
				rdbtnTEST.setSelected(false);
				rdbtnPROD.setSelected(false);
			}
		});
		rdbtnTEST.addActionListener(e -> {
			if (rdbtnTEST.isSelected()){
				this.setEnv("TEST");
				rdbtnSEVL.setSelected(false);
				rdbtnPROD.setSelected(false);
			}
		});
		rdbtnPROD.addActionListener(e -> {
			if (rdbtnPROD.isSelected()){
				this.setEnv("PROD");
				rdbtnSEVL.setSelected(false);
				rdbtnTEST.setSelected(false);
			}
		});

		if (Encryption.readFile() != null){
			Map<String, String> map = Encryption.readFile();
			userField.setText(map.get("alpha"));
			passwordField.setText(map.get("omega"));
			if(Preferences.contents.get("environment").equals("SEVL"))
				this.setEnv("SEVL");
			else if(Preferences.contents.get("environment").equals("TEST"))
				this.setEnv("TEST");
			else
				this.setEnv("PROD");
			if(Preferences.contents.containsKey("debug")&&
					Preferences.contents.get("debug").equals("false"))
				debug = false;
			else
				debug = true;

			this.connect(app);
		}
		else {
			frameLogin.setVisible(true);
		}
	}
	@SuppressWarnings("deprecation")
	private void save() {
		Encryption.encryptToFile(userField.getText(), passwordField.getText());
	}
}
