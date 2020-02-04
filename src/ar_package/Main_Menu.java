package ar_package;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.jcraft.jsch.Session;
import com.sshtools.sftp.SftpClient;

import javax.swing.JSeparator;
import javax.swing.JTextArea;

public class Main_Menu
{	
	public JFrame frameMenu;
	private JTextField textField_name;
	private JTextField textField_cwid;
	private JTextField textField_pidm;
	private JTextField textField_balance;
	private JTextField textField_transactions;
	private JTextField texfield_detailCodes;
	private JTextField textField_categoryCodes;
	private JTextField textField_email;
	private JTextField textField_myBama;
	private Function_Library fun;
	private String environment;

	public Main_Menu(Connection conn_jdbc, SftpClient conn_sftp, Session conn_ssh, String user, String pass, String env){
		environment=env;
		fun = new Function_Library(conn_jdbc,conn_sftp,conn_ssh,user,pass,environment);
		initialize();


	}
	private void openFootball() {
		EventQueue.invokeLater(() -> {
			try{
				Football_Menu window = new Football_Menu(fun,environment);
				window.frameMenu.setVisible(true);
				//frameMenu.setVisible(false);
			}
			catch (Exception e){
				e.printStackTrace();
			}
		});
	}
	private void initialize()
	{
		frameMenu = new JFrame();
		frameMenu.getContentPane().setFont(new Font("Tahoma", Font.BOLD, 12));
		frameMenu.setIconImage(Toolkit.getDefaultToolkit().getImage(Main_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
		frameMenu.setTitle("Main Menu ("+environment+") - Welcome, " + fun.firstName);
		frameMenu.setBounds(100, 100, 722, 457);
		frameMenu.setDefaultCloseOperation(3);
		frameMenu.getContentPane().setLayout(null);
		//
		//frameMenu.setVisible(true);
		//
		JButton button_exit = new JButton("Exit");
		button_exit.setFont(new Font("Lucida Grande", 0, 15));
		button_exit.setBounds(10, 350, 122, 57);
		frameMenu.getContentPane().add(button_exit);

		textField_name = new JTextField();
		textField_name.setText("");
		textField_name.setColumns(10);
		textField_name.setBounds(237, 237, 165, 26);
		frameMenu.getContentPane().add(textField_name);

		JButton resetButton = new JButton("RESET");
		resetButton.setBounds(309, 284, 94, 23);
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
		convertButton.setBounds(177, 284, 122, 23);
		frameMenu.getContentPane().add(convertButton);	

		textField_cwid = new JTextField();
		textField_cwid.setText("");
		textField_cwid.setColumns(10);
		textField_cwid.setBounds(10, 237, 94, 26);
		frameMenu.getContentPane().add(textField_cwid);

		JLabel label_cwid = new JLabel("CWID");
		label_cwid.setFont(new Font("Tahoma", Font.BOLD, 12));
		label_cwid.setBounds(10, 212, 46, 14);
		frameMenu.getContentPane().add(label_cwid);

		JLabel label_name = new JLabel("Name");
		label_name.setFont(new Font("Tahoma", Font.BOLD, 12));
		label_name.setBounds(237, 212, 46, 14);
		frameMenu.getContentPane().add(label_name);

		JLabel label_pidm = new JLabel("PIDM");
		label_pidm.setFont(new Font("Tahoma", Font.BOLD, 12));
		label_pidm.setBounds(118, 212, 46, 14);
		frameMenu.getContentPane().add(label_pidm);

		textField_pidm = new JTextField();
		textField_pidm.setText("");
		textField_pidm.setColumns(10);
		textField_pidm.setBounds(114, 237, 113, 26);
		frameMenu.getContentPane().add(textField_pidm);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 202, 682, 14);
		frameMenu.getContentPane().add(separator);

		textField_balance = new JTextField();
		textField_balance.setText("");
		textField_balance.setColumns(10);
		textField_balance.setBounds(10, 55, 94, 26);
		frameMenu.getContentPane().add(textField_balance);

		JLabel lblGetAccountBalance = new JLabel("Get Account Information");
		lblGetAccountBalance.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblGetAccountBalance.setBounds(10, 26, 221, 18);
		frameMenu.getContentPane().add(lblGetAccountBalance);

		JButton btn_balance = new JButton("Submit");
		btn_balance.addActionListener(arg0 -> {
			String cwid = textField_balance.getText().trim();
			if(!cwid.equals("")) {
				String message = fun.jdbc.getAccountInformation(cwid);
				JOptionPane.showMessageDialog(null, message);
			}
		});
		btn_balance.setBounds(114, 57, 83, 23);
		frameMenu.getContentPane().add(btn_balance);

		JLabel label_studentTransactions = new JLabel("Get Student Transactions");
		label_studentTransactions.setFont(new Font("Tahoma", Font.PLAIN, 17));
		label_studentTransactions.setBounds(10, 92, 221, 28);
		frameMenu.getContentPane().add(label_studentTransactions);

		textField_transactions = new JTextField();
		textField_transactions.setText("");
		textField_transactions.setColumns(10);
		textField_transactions.setBounds(10, 131, 94, 26);
		frameMenu.getContentPane().add(textField_transactions);

		JButton button_transactions = new JButton("Submit");
		button_transactions.addActionListener(arg0 -> {
			if(!textField_transactions.getText().equals("")) {
				String message = fun.jdbc.getTransactions(textField_transactions.getText().trim());
				JTextArea textArea = new JTextArea(message);
				textArea.setFont(new Font("monospaced", Font.BOLD, 16));
				JScrollPane scrollPane = new JScrollPane(textArea);
				scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
				scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPane.setPreferredSize(new Dimension(1200, 800));

				JOptionPane.showMessageDialog(null, scrollPane, "Transaction Codes - " + textField_transactions.getText().trim(), -1);
			}
		});
		button_transactions.setBounds(114, 133, 83, 23);
		frameMenu.getContentPane().add(button_transactions);


		texfield_detailCodes = new JTextField();
		texfield_detailCodes.setText("%");
		texfield_detailCodes.setColumns(10);
		texfield_detailCodes.setBounds(297, 134, 94, 26);
		frameMenu.getContentPane().add(texfield_detailCodes);

		JButton button_detailCodes = new JButton("Submit");
		button_detailCodes.addActionListener(e -> {
			String message = fun.jdbc.getDetailCodes(texfield_detailCodes.getText().trim());
			JTextArea textArea = new JTextArea(message);
			textArea.setFont(new Font("monospaced", Font.BOLD, 16));
			JScrollPane scrollPane = new JScrollPane(textArea);
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane.setPreferredSize(new Dimension(1200, 800));
			JOptionPane.showMessageDialog(null, scrollPane, "Detail Codes " + texfield_detailCodes.getText().trim(), -1);
		});
		button_detailCodes.setBounds(402, 133, 82, 23);
		frameMenu.getContentPane().add(button_detailCodes);

		JLabel lbl_detailCodes = new JLabel("Get Detail Codes");
		lbl_detailCodes.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lbl_detailCodes.setBounds(297, 92, 221, 28);
		frameMenu.getContentPane().add(lbl_detailCodes);

		JLabel lbl_categoryCodes = new JLabel("Get Category Codes");
		lbl_categoryCodes.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lbl_categoryCodes.setBounds(297, 26, 221, 23);
		frameMenu.getContentPane().add(lbl_categoryCodes);

		textField_categoryCodes = new JTextField();
		textField_categoryCodes.setText("%");
		textField_categoryCodes.setColumns(10);
		textField_categoryCodes.setBounds(297, 58, 94, 26);
		frameMenu.getContentPane().add(textField_categoryCodes);

		JButton button_categoryCodes = new JButton("Submit");
		button_categoryCodes.addActionListener(e -> {
			String message = fun.jdbc.getCategoryCodes(textField_categoryCodes.getText().trim());
			JTextArea textArea = new JTextArea(message);
			textArea.setFont(new Font("monospaced", Font.BOLD, 16));
			JScrollPane scrollPane = new JScrollPane(textArea);
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane.setPreferredSize(new Dimension(1200, 800));
			JOptionPane.showMessageDialog(null, scrollPane, "Category Codes " + textField_categoryCodes.getText().trim(), -1);
		});
		button_categoryCodes.setBounds(402, 57, 82, 24);
		frameMenu.getContentPane().add(button_categoryCodes);

		textField_email = new JTextField();
		textField_email.setText("");
		textField_email.setColumns(10);
		textField_email.setBounds(527, 237, 165, 26);
		frameMenu.getContentPane().add(textField_email);

		textField_myBama = new JTextField();
		textField_myBama.setText("");
		textField_myBama.setColumns(10);
		textField_myBama.setBounds(412, 237, 108, 26);
		frameMenu.getContentPane().add(textField_myBama);

		JLabel lblMybama = new JLabel("MyBama");
		lblMybama.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblMybama.setBounds(413, 212, 71, 14);
		frameMenu.getContentPane().add(lblMybama);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblEmail.setBounds(527, 212, 46, 14);
		frameMenu.getContentPane().add(lblEmail);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 318, 682, 14);
		frameMenu.getContentPane().add(separator_1);

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(arg0 -> {
			frameMenu.dispose();
			EventQueue.invokeLater(() -> {
				new public_package.Master_Menu(
						fun.jdbc.connection,//jdbc.connection,
						fun.firstName,//jdbc.userFirstName,
						fun.userName,
						fun.password,
						fun.sftp.getConnection(),
						fun.ssh.session,
						fun.environment);
			});
		});
		btnClose.setFont(new Font("Dialog", Font.PLAIN, 15));
		btnClose.setBounds(570, 350, 122, 57);
		frameMenu.getContentPane().add(btnClose);

		JButton btnFootballStream = new JButton("Football Program");
		btnFootballStream.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnFootballStream.addActionListener(e -> {

			openFootball();

			//frameMenu.setVisible(false);
		});

		btnFootballStream.setBounds(494, 75, 181, 65);
		frameMenu.getContentPane().add(btnFootballStream);

		button_exit.addActionListener(e -> System.exit(0));
		frameMenu.setVisible(true);
	}
}
