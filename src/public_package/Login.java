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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import git_package.Main_Menu;
import git_package.Function_Library;

public class Login
{
	private JTextField userField;
	private JPasswordField passwordField;
	String mode="PROD";
	JLabel status;
	JFrame frameLogin;
	JCheckBox chckbxSaveLogin;
	String application;
	public Login(String app){
		application=app;
		initialize();
	}
	@SuppressWarnings("deprecation")
	private void connect() {

		//Connection_SFTP_JDBC connection = new Connection_SFTP_JDBC(mode, userField.getText(), passwordField.getText());		
		JDBC_Connection jdbc = new JDBC_Connection(mode, userField.getText(), passwordField.getText());
		SFTP_Connection sftp = new SFTP_Connection(mode, userField.getText(), passwordField.getText());
		SSH_Connection ssh = new SSH_Connection(mode, userField.getText(), passwordField.getText());

		Thread t3 = new Thread(() -> ssh.sshConnect());
		t3.start();

		Thread t2 = new Thread(() -> sftp.sftpConnect());
		t2.start();

		Thread t1 = new Thread(() -> jdbc.jdbcConnect());
		t1.start();

		while (t1.isAlive() ||t2.isAlive() ||t3.isAlive()  ) {
			//holds off until both are done...
		}

		if(jdbc.connected()) {
			frameLogin.dispose(); // hide login menu
			//use for passing in parameters via shortcuts in Windows
			if(application==null) {
				new Master_Menu(
						jdbc.connection,
						jdbc.userFirstName,
						userField.getText(),
						jdbc.password,
						sftp.connection,
						ssh.session,
						mode);
			}
			else if(application.equals("concur"))
				new concur_package.Main_Menu(jdbc.connection,sftp.connection,ssh.session,userField.getText(),jdbc.password,mode);
			else if(application.equals("ar"))
				new ar_package.Main_Menu(jdbc.connection,sftp.connection,ssh.session,userField.getText(),jdbc.password,mode);
			else if(application.equals("git"))
				new Main_Menu(new Function_Library());

			if(chckbxSaveLogin.isSelected())
				save();
		}
		else {
			Encryption.deleteFile();
			status.setForeground(Color.RED);
			status.setText("INVALID USERNAME OR PASSWORD");
			frameLogin.setVisible(true);
		}
	}

	@SuppressWarnings("deprecation")
	private void initialize(){
		mode = "PROD";
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


		JButton button_Submit = new JButton("Submit");

		status = new JLabel("Status: Ready");
		status.setFont(new Font("Dialog", Font.BOLD, 14));
		status.setBounds(12, 158, 255, 37);
		status.setForeground(Color.BLACK);
		frameLogin.getContentPane().add(status);

		button_Submit.addActionListener(e -> {
			status.setText("Connecting now..");
			if ((userField.getText().isEmpty()) || (passwordField.getText().isEmpty())){
				status.setForeground(Color.RED);
				status.setText("ENTER USERNAME AND PASSWORD");
			}
			else
				connect();
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

		JButton btnNewButton = new JButton("About");
		btnNewButton.addActionListener(arg0 -> {
			String message = "How it works:\n\tTBA"
					+ "\n\n\n-greg ";

			JOptionPane.showMessageDialog(null, message);
		});
		btnNewButton.setBounds(267, 144, 89, 23);
		frameLogin.getContentPane().add(btnNewButton);
		btnExit.addActionListener(e -> System.exit(0));
		rdbtnSEVL.addActionListener(e -> {
			if (rdbtnSEVL.isSelected()){
				mode = "SEVL";
				rdbtnTEST.setSelected(false);
				rdbtnPROD.setSelected(false);
			}
		});
		rdbtnTEST.addActionListener(e -> {
			if (rdbtnTEST.isSelected()){
				mode = "TEST";
				rdbtnSEVL.setSelected(false);
				rdbtnPROD.setSelected(false);
			}
		});
		rdbtnPROD.addActionListener(e -> {
			if (rdbtnPROD.isSelected()){
				mode = "PROD";
				rdbtnSEVL.setSelected(false);
				rdbtnTEST.setSelected(false);
			}
		});
		
		if (Encryption.readFile() != null){
			Map<String, String> map = Encryption.readFile();
			userField.setText(map.get("alpha"));
			passwordField.setText(map.get("omega"));
			if(Preferences.contents.get("environment").equals("SEVL"))
				mode = "SEVL";
			else if(Preferences.contents.get("environment").equals("TEST"))
				mode = "TEST";
			else
				mode = "PROD";
			connect();
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
