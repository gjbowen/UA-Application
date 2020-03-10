package ar_package;
import public_package.*;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.*;

public class Football_Menu {
	JFrame frame;
	private JTextField textField_name;
	private JTextField textField_cwid;
	private JTextField textField_pidm;
	private JTextField textField_person;
	private JTextField textField_email;
	private JTextField textField_myBama;
	private final Function_Library fun;
	private String environment;
	private JTextField textField_year;

	public Football_Menu(Function_Library function_lib){
		environment=function_lib.environment;
		fun = function_lib;
		initialize();
	}
	public JMenuBar getMenuBar(String env) {
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
		this.environment = env;

		System.out.println("Switching to "+env+"..");

		fun.jdbc =  new JDBC(null,fun.userName,fun.password,env);
		fun.jdbc.jdbcConnect();

		if(fun.sftp.connection!=null){
			fun.sftp = new SFTP(null,fun.userName,fun.password,fun.environment);
			fun.sftp.sftpConnect();
		}
		if(fun.ssh.session!=null) {
			fun.ssh = new SSH(null,fun.userName,fun.password,fun.environment);
			fun.ssh.sshConnect();;
		}

		frame.dispose();
		Preferences.addPreference("environment", env);

		initialize();
	}

	private void initialize()
	{
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Tahoma", Font.BOLD, 12));
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Football_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
		frame.setTitle("Football Menu ("+environment+") - Welcome, " + fun.firstName);
		frame.setBounds(100, 100, 722, 553);
		frame.setDefaultCloseOperation(3);
		frame.getContentPane().setLayout(null);
		frame.setJMenuBar(getMenuBar(environment));

		JButton button_exit = new JButton("Exit");
		button_exit.setFont(new Font("Lucida Grande", 0, 15));
		button_exit.setBounds(14, 446, 122, 57);
		frame.getContentPane().add(button_exit);

		textField_name = new JTextField();
		textField_name.setText("");
		textField_name.setColumns(10);
		textField_name.setBounds(241, 340, 165, 26);
		frame.getContentPane().add(textField_name);

		JButton resetButton = new JButton("RESET");
		resetButton.setBounds(313, 387, 94, 23);
		frame.getContentPane().add(resetButton);
		resetButton.addActionListener(e -> {
			textField_name.setText("");
			textField_cwid.setText("");
			textField_pidm.setText("");
			textField_email.setText("");
			textField_myBama.setText("");
		});

		JButton convertButton = new JButton("CONVERT");
		convertButton.addActionListener(e -> {
			//do stuff
			ArrayList<String> values;
			values = fun.jdbc.convert(
					textField_pidm.getText().trim(),
					textField_cwid.getText().trim(),
					textField_name.getText().trim(),
					textField_email.getText().trim(),
					textField_myBama.getText().trim());
			textField_pidm.setText(values.get(0));
			textField_cwid.setText(values.get(1));
			textField_name.setText(values.get(2));
			textField_email.setText(values.get(3));
			textField_myBama.setText(values.get(4));
		});
		convertButton.setBounds(181, 387, 122, 23);
		frame.getContentPane().add(convertButton);

		textField_cwid = new JTextField();
		textField_cwid.setText("");
		textField_cwid.setColumns(10);
		textField_cwid.setBounds(14, 340, 94, 26);
		frame.getContentPane().add(textField_cwid);

		JLabel label_cwid = new JLabel("CWID");
		label_cwid.setFont(new Font("Tahoma", Font.BOLD, 12));
		label_cwid.setBounds(14, 315, 46, 14);
		frame.getContentPane().add(label_cwid);

		JLabel label_name = new JLabel("Name");
		label_name.setFont(new Font("Tahoma", Font.BOLD, 12));
		label_name.setBounds(241, 315, 46, 14);
		frame.getContentPane().add(label_name);

		JLabel label_pidm = new JLabel("PIDM");
		label_pidm.setFont(new Font("Tahoma", Font.BOLD, 12));
		label_pidm.setBounds(122, 315, 46, 14);
		frame.getContentPane().add(label_pidm);

		textField_pidm = new JTextField();
		textField_pidm.setText("");
		textField_pidm.setColumns(10);
		textField_pidm.setBounds(118, 340, 113, 26);
		frame.getContentPane().add(textField_pidm);

		JSeparator separator = new JSeparator();
		separator.setBounds(14, 305, 682, 14);
		frame.getContentPane().add(separator);

		JLabel lblGeneralInfo = new JLabel("General Info");
		lblGeneralInfo.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblGeneralInfo.setBounds(118, 105, 263, 33);
		frame.getContentPane().add(lblGeneralInfo);

		textField_person = new JTextField();
		textField_person.setText("11256098");
		textField_person.setFont(new Font("Tahoma", Font.PLAIN, 18));
		//textField_person.setText("11256098");
		textField_person.setColumns(10);
		textField_person.setBounds(8, 24, 100, 38);
		frame.getContentPane().add(textField_person);

		JButton button_ticketInfo = new JButton("Submit");
		button_ticketInfo.addActionListener(arg0 -> {
			if(!textField_person.getText().equals("")) {
				String message = fun.jdbc.getFootballInfo(textField_person.getText().trim(),textField_year.getText().trim());
				JTextArea textArea = new JTextArea(message);
				textArea.setFont(new Font("monospaced", Font.BOLD, 16));
				textArea.setPreferredSize(new Dimension(600, 400));
				JOptionPane.showMessageDialog(null, textArea, "Person Info - " + textField_person.getText().trim(), -1);
			}
		});
		button_ticketInfo.setBounds(14, 113, 94, 23);
		frame.getContentPane().add(button_ticketInfo);

		textField_email = new JTextField();
		textField_email.setText("");
		textField_email.setColumns(10);
		textField_email.setBounds(531, 340, 165, 26);
		frame.getContentPane().add(textField_email);

		textField_myBama = new JTextField();
		textField_myBama.setText("");
		textField_myBama.setColumns(10);
		textField_myBama.setBounds(416, 340, 108, 26);
		frame.getContentPane().add(textField_myBama);

		JLabel lblMybama = new JLabel("MyBama");
		lblMybama.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblMybama.setBounds(417, 315, 71, 14);
		frame.getContentPane().add(lblMybama);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblEmail.setBounds(531, 315, 46, 14);
		frame.getContentPane().add(lblEmail);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(14, 421, 682, 14);
		frame.getContentPane().add(separator_1);

		JButton btnAR = new JButton("AR");
		btnAR.addActionListener(arg0 -> {
			frame.dispose();
		});
		btnAR.setFont(new Font("Dialog", Font.PLAIN, 15));
		btnAR.setBounds(574, 446, 122, 57);
		frame.getContentPane().add(btnAR);
		btnAR.addActionListener(e ->{
			ar_package.Main_Menu window = new ar_package.Main_Menu(fun.jdbc.connection,
					fun.sftp.connection,
					fun.ssh.session,
					fun.userName,
					fun.password,
					fun.environment);
			window.frame.setVisible(true);
		});


		textField_year = new JTextField();
		textField_year.setText(fun.jdbc.getFootballYear());
		textField_year.setColumns(10);
		textField_year.setBounds(574, 49, 94, 26);
		frame.getContentPane().add(textField_year);

		JLabel lblYear = new JLabel("Year");
		lblYear.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblYear.setBounds(539, 12, 113, 43);
		frame.getContentPane().add(lblYear);

		JLabel lblStudentsFootballTicket = new JLabel("History");
		lblStudentsFootballTicket.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblStudentsFootballTicket.setBounds(118, 137, 338, 33);
		frame.getContentPane().add(lblStudentsFootballTicket);

		JButton button_ticketHistory = new JButton("Submit");
		button_ticketHistory.addActionListener(e -> {
			if(!textField_person.getText().equals("")) {
				String message = fun.jdbc.getFootballHistory(textField_person.getText().trim(),textField_year.getText().trim());
				JTextArea textArea = new JTextArea(message);
				textArea.setFont(new Font("monospaced", Font.BOLD, 16));
				JScrollPane scrollPane = new JScrollPane(textArea);
				scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
				scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPane.setPreferredSize(new Dimension(1400, 400));
				JOptionPane.showMessageDialog(null, scrollPane, "Person History (most recent)- " + textField_person.getText().trim(), -1);
			}

		});
		button_ticketHistory.setBounds(14, 145, 94, 23);
		frame.getContentPane().add(button_ticketHistory);

		JLabel lblStudentsCwidpidm = new JLabel("Student's CWID/PIDM");
		lblStudentsCwidpidm.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblStudentsCwidpidm.setBounds(118, 27, 263, 33);
		frame.getContentPane().add(lblStudentsCwidpidm);

		JButton button_reason = new JButton("Submit");
		button_reason.addActionListener(e -> {
			if(!textField_person.getText().equals("")) {
				String message = fun.jdbc.getFootballGroupReason(textField_person.getText().trim(),textField_year.getText().trim());
				JTextArea textArea = new JTextArea(message);
				textArea.setLineWrap(true);
				textArea.setWrapStyleWord(true);
				textArea.setFont(new Font("Tahoma", Font.PLAIN, 17));
				textArea.setPreferredSize(new Dimension(400, 200));
				JOptionPane.showMessageDialog(null, textArea, "Person Info - " + textField_person.getText().trim(), -1);
			}

		});
		button_reason.setBounds(14, 179, 94, 23);
		frame.getContentPane().add(button_reason);

		JLabel lblReason = new JLabel("Situation description");
		lblReason.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblReason.setBounds(118, 171, 338, 33);
		frame.getContentPane().add(lblReason);

		JButton button_charges = new JButton("Submit");
		button_charges.addActionListener(e -> {
			if(!textField_person.getText().equals("")) {
				String message = fun.jdbc.getFootballDetailCodes(textField_person.getText().trim(),textField_year.getText().trim());
				JTextArea textArea = new JTextArea(message);
				textArea.setFont(new Font("monospaced", Font.BOLD, 16));
				JScrollPane scrollPane = new JScrollPane(textArea);
				scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
				scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPane.setPreferredSize(new Dimension(1400, 400));
				JOptionPane.showMessageDialog(null, scrollPane, "Person's Account Charges - " + textField_person.getText().trim(), -1);
			}
		});
		button_charges.setBounds(14, 213, 94, 23);
		frame.getContentPane().add(button_charges);

		JLabel lblFootballCharges = new JLabel("Account Charges");
		lblFootballCharges.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblFootballCharges.setBounds(118, 205, 338, 33);

		frame.getContentPane().add(lblFootballCharges);

		JLabel label_header = new JLabel("Student's Football Ticket:");
		label_header.setFont(new Font("Tahoma", Font.BOLD, 17));
		label_header.setBounds(40, 73, 263, 33);
		frame.getContentPane().add(label_header);

		JButton btnOpenApex = new JButton("Open APEX");
		btnOpenApex.addActionListener(e -> fun.openLink(fun.getApexLink()));
		btnOpenApex.setBounds(574, 256, 122, 38);
		frame.getContentPane().add(btnOpenApex);

		JButton button_groups = new JButton("Groups");
		button_groups.addActionListener(e -> {
			String message = fun.jdbc.getFootballGroups(textField_year.getText().trim());
			JTextArea textArea = new JTextArea(message);
			textArea.setFont(new Font("monospaced", Font.BOLD, 16));
			JScrollPane scrollPane = new JScrollPane(textArea);
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane.setPreferredSize(new Dimension(1400, 400));
			JOptionPane.showMessageDialog(null, scrollPane, "Groups - " + textField_year.getText().trim(), -1);

		});
		button_groups.setBounds(574, 93, 94, 23);
		frame.getContentPane().add(button_groups);

		JButton button = new JButton("Games");
		button.addActionListener(e -> {
			String message = fun.jdbc.getFootballGames(textField_year.getText().trim());
			JTextArea textArea = new JTextArea(message);
			textArea.setFont(new Font("monospaced", Font.BOLD, 16));
			JScrollPane scrollPane = new JScrollPane(textArea);
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane.setPreferredSize(new Dimension(1400, 400));
			JOptionPane.showMessageDialog(null, scrollPane, "Games - " + textField_year.getText().trim(), -1);

		});
		button.setBounds(574, 143, 94, 23);
		frame.getContentPane().add(button);
		frame.setVisible(true);
		button_exit.addActionListener(e -> System.exit(0));


	}
}
