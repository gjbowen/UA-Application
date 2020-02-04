package ar_package;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

class Football_Menu
{	
	JFrame frameMenu;
	private JTextField textField_name;
	private JTextField textField_cwid;
	private JTextField textField_pidm;
	private JTextField textField_person;
	private JTextField textField_email;
	private JTextField textField_myBama;
	private Function_Library fun;
	private String environment;
	private JTextField textField_year;

	public Football_Menu(Function_Library function_lib, String mode){
		environment=mode;
		fun = function_lib;
		initialize();
	}

	private void initialize()
	{
		frameMenu = new JFrame();
		frameMenu.getContentPane().setFont(new Font("Tahoma", Font.BOLD, 12));
		frameMenu.setIconImage(Toolkit.getDefaultToolkit().getImage(Football_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
		frameMenu.setTitle("Football Menu ("+environment+") - Welcome, " + fun.firstName);
		frameMenu.setBounds(100, 100, 722, 553);
		frameMenu.setDefaultCloseOperation(3);
		frameMenu.getContentPane().setLayout(null);

		JButton button_exit = new JButton("Exit");
		button_exit.setFont(new Font("Lucida Grande", 0, 15));
		button_exit.setBounds(14, 446, 122, 57);
		frameMenu.getContentPane().add(button_exit);

		textField_name = new JTextField();
		textField_name.setText("");
		textField_name.setColumns(10);
		textField_name.setBounds(241, 340, 165, 26);
		frameMenu.getContentPane().add(textField_name);

		JButton resetButton = new JButton("RESET");
		resetButton.setBounds(313, 387, 94, 23);
		frameMenu.getContentPane().add(resetButton);
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
		frameMenu.getContentPane().add(convertButton);	

		textField_cwid = new JTextField();
		textField_cwid.setText("");
		textField_cwid.setColumns(10);
		textField_cwid.setBounds(14, 340, 94, 26);
		frameMenu.getContentPane().add(textField_cwid);

		JLabel label_cwid = new JLabel("CWID");
		label_cwid.setFont(new Font("Tahoma", Font.BOLD, 12));
		label_cwid.setBounds(14, 315, 46, 14);
		frameMenu.getContentPane().add(label_cwid);

		JLabel label_name = new JLabel("Name");
		label_name.setFont(new Font("Tahoma", Font.BOLD, 12));
		label_name.setBounds(241, 315, 46, 14);
		frameMenu.getContentPane().add(label_name);

		JLabel label_pidm = new JLabel("PIDM");
		label_pidm.setFont(new Font("Tahoma", Font.BOLD, 12));
		label_pidm.setBounds(122, 315, 46, 14);
		frameMenu.getContentPane().add(label_pidm);

		textField_pidm = new JTextField();
		textField_pidm.setText("");
		textField_pidm.setColumns(10);
		textField_pidm.setBounds(118, 340, 113, 26);
		frameMenu.getContentPane().add(textField_pidm);

		JSeparator separator = new JSeparator();
		separator.setBounds(14, 305, 682, 14);
		frameMenu.getContentPane().add(separator);

		JLabel lblGeneralInfo = new JLabel("General Info");
		lblGeneralInfo.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblGeneralInfo.setBounds(118, 105, 263, 33);
		frameMenu.getContentPane().add(lblGeneralInfo);

		textField_person = new JTextField();
		textField_person.setText("11256098");
		textField_person.setFont(new Font("Tahoma", Font.PLAIN, 18));
		//textField_person.setText("11256098");
		textField_person.setColumns(10);
		textField_person.setBounds(8, 24, 100, 38);
		frameMenu.getContentPane().add(textField_person);

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
		frameMenu.getContentPane().add(button_ticketInfo);

		textField_email = new JTextField();
		textField_email.setText("");
		textField_email.setColumns(10);
		textField_email.setBounds(531, 340, 165, 26);
		frameMenu.getContentPane().add(textField_email);

		textField_myBama = new JTextField();
		textField_myBama.setText("");
		textField_myBama.setColumns(10);
		textField_myBama.setBounds(416, 340, 108, 26);
		frameMenu.getContentPane().add(textField_myBama);

		JLabel lblMybama = new JLabel("MyBama");
		lblMybama.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblMybama.setBounds(417, 315, 71, 14);
		frameMenu.getContentPane().add(lblMybama);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblEmail.setBounds(531, 315, 46, 14);
		frameMenu.getContentPane().add(lblEmail);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(14, 421, 682, 14);
		frameMenu.getContentPane().add(separator_1);

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(arg0 -> {
			frameMenu.dispose();
		});
		btnClose.setFont(new Font("Dialog", Font.PLAIN, 15));
		btnClose.setBounds(574, 446, 122, 57);
		frameMenu.getContentPane().add(btnClose);

		textField_year = new JTextField();
		textField_year.setText(fun.jdbc.getFootballYear());
		textField_year.setColumns(10);
		textField_year.setBounds(574, 49, 94, 26);
		frameMenu.getContentPane().add(textField_year);

		JLabel lblYear = new JLabel("Year");
		lblYear.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblYear.setBounds(539, 12, 113, 43);
		frameMenu.getContentPane().add(lblYear);

		JLabel lblStudentsFootballTicket = new JLabel("History");
		lblStudentsFootballTicket.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblStudentsFootballTicket.setBounds(118, 137, 338, 33);
		frameMenu.getContentPane().add(lblStudentsFootballTicket);

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
		frameMenu.getContentPane().add(button_ticketHistory);

		JLabel lblStudentsCwidpidm = new JLabel("Student's CWID/PIDM");
		lblStudentsCwidpidm.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblStudentsCwidpidm.setBounds(118, 27, 263, 33);
		frameMenu.getContentPane().add(lblStudentsCwidpidm);

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
		frameMenu.getContentPane().add(button_reason);

		JLabel lblReason = new JLabel("Situation description");
		lblReason.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblReason.setBounds(118, 171, 338, 33);
		frameMenu.getContentPane().add(lblReason);

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
		frameMenu.getContentPane().add(button_charges);

		JLabel lblFootballCharges = new JLabel("Account Charges");
		lblFootballCharges.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblFootballCharges.setBounds(118, 205, 338, 33);

		frameMenu.getContentPane().add(lblFootballCharges);

		JLabel label_header = new JLabel("Student's Football Ticket:");
		label_header.setFont(new Font("Tahoma", Font.BOLD, 17));
		label_header.setBounds(40, 73, 263, 33);
		frameMenu.getContentPane().add(label_header);

		JButton btnOpenApex = new JButton("Open APEX");
		btnOpenApex.addActionListener(e -> fun.openLink(fun.getApexLink()));
		btnOpenApex.setBounds(574, 256, 122, 38);
		frameMenu.getContentPane().add(btnOpenApex);

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
		frameMenu.getContentPane().add(button_groups);
		
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
		frameMenu.getContentPane().add(button);

		button_exit.addActionListener(e -> System.exit(0));
	}
}
