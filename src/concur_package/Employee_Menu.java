package concur_package;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Color;

public class Employee_Menu {
	protected JFrame frameEmployeeMenu;
	protected Function_Library connection;
	private JTextField cwidField_1;
	private JTextField cwidField_2;
	private JTextField cwidField_6;
	private JTextField textField_name;
	private JTextField textField_cwid;
	private JTextField textField_pidm;
	private JTextField textField_column;

	public Employee_Menu(Function_Library conn) {
		this.connection = conn;
		this.initialize();
	}

	private void initialize() {
		this.frameEmployeeMenu = new JFrame();
		this.frameEmployeeMenu.setIconImage(Toolkit.getDefaultToolkit().getImage(Employee_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
		this.frameEmployeeMenu.setTitle("Employee Menu");
		this.frameEmployeeMenu.setBounds(100, 100, 718, 508);
		frameEmployeeMenu.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.frameEmployeeMenu.getContentPane().setLayout(null);
		JPanel label_In_Tracking_Table = new JPanel();
		label_In_Tracking_Table.setBorder(null);
		label_In_Tracking_Table.setBounds(6, 6, 686, 453);
		this.frameEmployeeMenu.getContentPane().add(label_In_Tracking_Table);
		label_In_Tracking_Table.setLayout(null);
		final JLabel lblStatus = new JLabel("Status: WAIT!");
		lblStatus.setFont(new Font("AppleGothic", 0, 16));
		lblStatus.setBounds(138, 404, 328, 35);
		label_In_Tracking_Table.add(lblStatus);
		lblStatus.setText("");
		JLabel lblFindEmployee =new JLabel("Find Employee sent(value, column)");
		lblFindEmployee.setBounds(6, 11, 174, 16);
		label_In_Tracking_Table.add(lblFindEmployee);
		this.cwidField_1 = new JTextField();
		this.cwidField_1.setBounds(205, 6, 130, 26);
		label_In_Tracking_Table.add(this.cwidField_1);
		this.cwidField_1.setColumns(10);
		JLabel lblFindDeleted = new JLabel("Find if deactivated from Concur");
		lblFindDeleted.setBounds(6, 53, 231, 16);
		label_In_Tracking_Table.add(lblFindDeleted);
		this.cwidField_2 = new JTextField();
		this.cwidField_2.setBounds(205, 48, 130, 26);
		label_In_Tracking_Table.add(this.cwidField_2);
		this.cwidField_2.setColumns(1);
		JButton btnSubmit_1 = new JButton("Submit");
		btnSubmit_1.setBounds(401, 5, 88, 29);
		label_In_Tracking_Table.add(btnSubmit_1);
		btnSubmit_1.addActionListener(e2 -> {
			lblStatus.setText("Status: Finding CWID info..");
			String message = null;
			String person = cwidField_1.getText().trim();
			if (!person.equals("")) {
				//if(!connection.isCWID(person))
				//	person = connection.jdbc.getPIDMFromCWID(person);
				message = connection.findEmployee(person,Integer.parseInt(textField_column.getText().trim()));
				JTextArea textArea = new JTextArea(message);
				JScrollPane scrollPane = new JScrollPane(textArea);
				textArea.setLineWrap(true);
				textArea.setWrapStyleWord(true);
				scrollPane.setPreferredSize(new Dimension(1000, 600));
				JOptionPane.showMessageDialog(null, scrollPane, "CWID " + person, -1);
				lblStatus.setText("");
			}
			else {
				lblStatus.setText("Status: CWID not given.");
			}
			cwidField_1.setText("");
		});
		JButton btnSubmit_2 = new JButton("Submit");
		btnSubmit_2.setBounds(345, 47, 88, 29);
		label_In_Tracking_Table.add(btnSubmit_2);
		btnSubmit_2.addActionListener(e2 -> {
			lblStatus.setText("Status: Finding if CWID has been removed.");
			String message = null;
			String person = cwidField_2.getText().trim();
			if (!person.equals("")) {
				if(!connection.isCWID(person))
					person = connection.jdbc.getPIDMFromCWID(person);

				else {
					message = connection.findIfDeleted(person);
					lblStatus.setText("");
					JOptionPane.showMessageDialog(null, message);
					cwidField_2.setText("");
				}
			} else {
				lblStatus.setText("Status: CWID not given.");
			}
		});
		JButton btnSubmit_3 = new JButton("Submit");
		btnSubmit_3.setBounds(205, 80, 88, 29);
		label_In_Tracking_Table.add(btnSubmit_3);
		btnSubmit_3.addActionListener(e2 -> {
			String message = null;
			lblStatus.setText("Status: Finding changed Login IDs.");
			message = Employee_Menu.this.connection.findChangedLogin();
			JTextArea textArea = new JTextArea(message);
			JScrollPane scrollPane = new JScrollPane(textArea);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			scrollPane.setPreferredSize(new Dimension(800, 500));
			JOptionPane.showMessageDialog(null, scrollPane, "Changed Login IDs", -1);
			lblStatus.setText("");
		});
		JButton btnSubmit_4 = new JButton("Submit");
		btnSubmit_4.setBounds(205, 120, 88, 29);
		label_In_Tracking_Table.add(btnSubmit_4);
		btnSubmit_4.addActionListener(e2 -> {
			String message = null;
			lblStatus.setText("Status: Finding all deactivated employees.");
			message = Employee_Menu.this.connection.findAllDeleted();
			JTextArea textArea = new JTextArea(message);
			JScrollPane scrollPane = new JScrollPane(textArea);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			scrollPane.setPreferredSize(new Dimension(500, 500));
			JOptionPane.showMessageDialog(null, scrollPane, "All Deactived Employees", -1);
			lblStatus.setText("");
		});
		JButton btnClose = new JButton("Close");
		btnClose.setFont(new Font("Lucida Grande", 0, 15));
		btnClose.addActionListener(e2 -> frameEmployeeMenu.dispose());
		btnClose.setBounds(554, 386, 122, 54);
		label_In_Tracking_Table.add(btnClose);
		JButton btnExit = new JButton("Exit");
		btnExit.setFont(new Font("Lucida Grande", 0, 15));
		btnExit.addActionListener(e2 -> {
			public_package.Exit_Confirmation window = new public_package.Exit_Confirmation(connection.firstName);
			window.frameExitConfirmation.setVisible(true);
			System.exit(0);
		});
		btnExit.setBounds(6, 385, 122, 57);
		label_In_Tracking_Table.add(btnExit);
		JLabel lblFindChangedLogin = new JLabel("Find changed login IDs");
		lblFindChangedLogin.setBounds(6, 91, 189, 16);
		label_In_Tracking_Table.add(lblFindChangedLogin);
		JLabel lblFindAllDeleted = new JLabel("Find all deleted employees");
		lblFindAllDeleted.setBounds(6, 126, 189, 16);
		label_In_Tracking_Table.add(lblFindAllDeleted);


		
		this.cwidField_6 = new JTextField();
		this.cwidField_6.setColumns(10);
		this.cwidField_6.setBounds(238, 256, 130, 26);
		label_In_Tracking_Table.add(this.cwidField_6);

		JButton submitButton6 = new JButton("Submit");
		submitButton6.setBounds(378, 255, 88, 29);
		label_In_Tracking_Table.add(submitButton6);
		submitButton6.addActionListener(e2 -> {
			lblStatus.setText("Status: Getting person info from tracking table..");
			String person = cwidField_6.getText().trim();
			String message = "";
			if (!person.equals("")) {
				if(!connection.isCWID(person))
					person = connection.jdbc.getPIDMFromCWID(person);
				if (connection.jdbc.cwidExistsInTrackingTable(person)) {
					message = connection.jdbc.getInformationTrackingTable(person);
					JTextArea textArea = new JTextArea(message);
					JScrollPane scrollPane = new JScrollPane(textArea);
					textArea.setLineWrap(true);
					textArea.setWrapStyleWord(true);
					scrollPane.setPreferredSize(new Dimension(500, 500));
					JOptionPane.showMessageDialog(null, scrollPane, "Tracking Table Contents for " + person, -1);
				} else {
					JOptionPane.showMessageDialog(null, "CWID - " + person + " is not in tracking table");
				}
				lblStatus.setText("");
				Employee_Menu.this.cwidField_6.setText("");
			} else {
				lblStatus.setText("Status: CWID not given.");
			}
		});
		JLabel lblGetInformationFrom = new JLabel("Get information from tracking table");
		lblGetInformationFrom.setBounds(6, 262, 255, 16);
		label_In_Tracking_Table.add(lblGetInformationFrom);
		textField_name = new JTextField();
		textField_name.setBounds(502, 347, 146, 26);
		label_In_Tracking_Table.add(textField_name);
		textField_name.setColumns(10);
		textField_name.setText("");
		textField_pidm = new JTextField();
		textField_pidm.setBounds(344, 347, 146, 26);
		label_In_Tracking_Table.add(textField_pidm);
		textField_pidm.setColumns(10);
		textField_pidm.setText("");
		textField_cwid = new JTextField();
		textField_cwid.setColumns(10);
		textField_cwid.setBounds(189, 347, 146, 26);
		label_In_Tracking_Table.add(this.textField_cwid);
		textField_cwid.setText("");
		JLabel lblCwid = new JLabel("CWID");
		lblCwid.setBounds(189, 333, 46, 14);
		label_In_Tracking_Table.add(lblCwid);
		JButton resetButton = new JButton("RESET");
		resetButton.setBounds(82, 349, 98, 23);
		label_In_Tracking_Table.add(resetButton);
		JButton convertButton = new JButton("CONVERT");
		convertButton.setBounds(82, 324, 98, 23);
		label_In_Tracking_Table.add(convertButton);
		JLabel lblFullName = new JLabel("Full Name");
		lblFullName.setBounds(502, 333, 80, 14);
		label_In_Tracking_Table.add(lblFullName);
		final JEditorPane editorPane = new JEditorPane();
		editorPane.setForeground(Color.BLACK);
		editorPane.setBounds(530, 43, 146, 201);
		label_In_Tracking_Table.add(editorPane);
		JLabel lblBatchSearch = new JLabel("Batch search");
		lblBatchSearch.setFont(new Font("Tahoma", 1, 13));
		lblBatchSearch.setBounds(556, 9, 88, 23);
		label_In_Tracking_Table.add(lblBatchSearch);
		JButton button = new JButton("Submit");
		button.addActionListener(arg0 -> {
			lblStatus.setText("Status: Finding if person is in tracking table..");
			String people = editorPane.getText().trim();
			System.out.println(people);
			if (!people.equals("")) {
				JOptionPane.showMessageDialog(null, connection.findBatch(people));
				lblStatus.setText("");
			} else {
				lblStatus.setText("Status: CWIDs not given");
			}
		});
		button.setBounds(556, 255, 88, 29);
		label_In_Tracking_Table.add(button);
		JLabel lblFindAllReactivated = new JLabel("Find all reactivated employees");
		lblFindAllReactivated.setBounds(6, 169, 189, 16);
		label_In_Tracking_Table.add(lblFindAllReactivated);
		JButton button_Reactivated = new JButton("Submit");
		button_Reactivated.addActionListener(arg0 -> {
			String message = null;
			lblStatus.setText("Status: Finding all reactivated employees.");
			message = Employee_Menu.this.connection.findAllDeleted();
			JTextArea textArea = new JTextArea(message);
			JScrollPane scrollPane = new JScrollPane(textArea);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			scrollPane.setPreferredSize(new Dimension(500, 500));
			JOptionPane.showMessageDialog(null, scrollPane, "All Deactived Employees", -1);
			lblStatus.setText("");
		});
		button_Reactivated.setBounds(205, 160, 88, 29);
		label_In_Tracking_Table.add(button_Reactivated);

		JLabel lblPidm = new JLabel("PIDM");
		lblPidm.setBounds(344, 333, 46, 14);
		label_In_Tracking_Table.add(lblPidm);
		
		textField_column = new JTextField();
		textField_column.setText("5");
		textField_column.setColumns(10);
		textField_column.setBounds(345, 6, 46, 26);
		label_In_Tracking_Table.add(textField_column);
		resetButton.addActionListener(e2 -> {

			textField_name.setText("");
			textField_cwid.setText("");
			textField_pidm.setText("");
		});
		convertButton.addActionListener(e2 -> {
			//do stuff

			if(textField_cwid.getText().equals("") && textField_name.getText().equals("")) { //use pidm
				textField_cwid.setText(connection.jdbc.getCWIDFromPIDM(textField_pidm.getText()));
				textField_name.setText(connection.jdbc.getNameFromPIDM(textField_pidm.getText()));
			}
			else if (textField_pidm.getText().equals("") && textField_name.getText().equals("")){ //use cwid
				String cwid = textField_cwid.getText();
				textField_pidm.setText(connection.jdbc.getPIDMFromCWID(cwid));
				textField_name.setText(connection.jdbc.getNameFromPIDM(cwid));			}
			else if (textField_pidm.getText().equals("") && textField_cwid.getText().equals("")){//use name
				textField_cwid.setText(connection.jdbc.getCWIDFromName(textField_name.getText()));
				textField_pidm.setText(connection.jdbc.getPIDMFromName(textField_name.getText()));
			}

		});

	}
}

