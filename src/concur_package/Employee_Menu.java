package concur_package;

import java.awt.*;
import javax.swing.*;

public class Employee_Menu {
	protected JFrame frameEmployeeMenu;
	protected Function_Library connection;
	private API_Package api;

	private JTextField cwidField_emp;

	private JTextField textField_name;
	private JTextField textField_cwid;
	private JTextField textField_pidm;
	private JTextField textField_column;

	public Employee_Menu(Function_Library conn) {
		connection = conn;
		initialize();
		api = new API_Package(connection);

	}

	private void initialize() {
		frameEmployeeMenu = new JFrame();
		frameEmployeeMenu.setIconImage(Toolkit.getDefaultToolkit().getImage(Employee_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
		frameEmployeeMenu.setTitle("Employee Menu");
		frameEmployeeMenu.setBounds(100, 100, 718, 508);
		frameEmployeeMenu.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frameEmployeeMenu.getContentPane().setLayout(null);
		frameEmployeeMenu.setResizable(false);

		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setBounds(6, 6, 686, 453);
		frameEmployeeMenu.getContentPane().add(panel);
		panel.setLayout(null);

		final JLabel lblStatus = new JLabel("Status: WAIT!");
		lblStatus.setFont(new Font("AppleGothic", 0, 16));
		lblStatus.setBounds(138, 404, 328, 35);
		panel.add(lblStatus);
		lblStatus.setText("");

		Panel subPanel_1 = new Panel();
		subPanel_1.setBackground(Color.LIGHT_GRAY);
		subPanel_1.setBounds(5, 10, 300, 150);
		panel.add(subPanel_1);
		subPanel_1.setLayout(null);

		//////////////////////////////////////////////////////////////////////////////////
		JLabel lbl_person = new JLabel("CWID/PIDM");
		lbl_person.setFont(new Font("Tahoma", 1, 13));
		lbl_person.setBounds(5, 8, 80, 23);
		subPanel_1.add(lbl_person);

		cwidField_emp = new JTextField();
		cwidField_emp.setBounds(150, 6, 130, 26);
		subPanel_1.add(cwidField_emp);
		cwidField_emp.setColumns(10);

		//////////////////////////////////////////////////////////////////////

		textField_column = new JTextField();
		textField_column.setText("5");
		textField_column.setColumns(10);
		textField_column.setBounds(220, 35, 46, 26);
		subPanel_1.add(textField_column);
		
		JButton btnSubmit_findEmp = new JButton("Find Employee sent");
		btnSubmit_findEmp.setBounds(5, 35, 210, 29);
		subPanel_1.add(btnSubmit_findEmp);
		
		btnSubmit_findEmp.addActionListener(e2 -> {
			lblStatus.setText("Status: Finding CWID info..");
			String message;
			String person = cwidField_emp.getText().trim();
			if (!person.equals("")) {
				if(textField_column.getText().trim().equals("5") && !connection.isCWID(person))
					person = connection.jdbc.getCWIDFromPIDM(person);
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
		});

		//////////////////////////////////////////////////////////////////////////////////////////

		JButton submitButton6 = new JButton("Get information from tracking table");
		submitButton6.setBounds(5, 70, 255, 29);
		subPanel_1.add(submitButton6);
		submitButton6.addActionListener(e2 -> {
			lblStatus.setText("Status: Getting person info from tracking table..");
			String person = cwidField_emp.getText().trim();
			String message;
			if (!person.equals("")) {
				if(!connection.isCWID(person))
					person = connection.jdbc.getCWIDFromPIDM(person);
				if (connection.jdbc.cwidTracked(person)) {
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
			} else {
				lblStatus.setText("Status: CWID not given.");
			}
		});

		//////////////////////////////////////////////////////////////////////////

		JButton btnSubmit_API = new JButton("Find via API");
		btnSubmit_API.setBounds(5, 105, 240, 29);
		subPanel_1.add(btnSubmit_API);
		btnSubmit_API.addActionListener(e2 -> {
			lblStatus.setText("Status: Sending API GET request..");
			String message;
			String person = cwidField_emp.getText().trim();
			if (!person.equals("")) {
				if(!connection.isCWID(person))
					person = connection.jdbc.getCWIDFromPIDM(person);

				//do stuff
				lblStatus.setText("");
				api.reinit();
				api.sendGetRequest("/api/v3.0/common/users?employeeID="+person);
				message=api.usersToString();

				JTextArea textArea = new JTextArea(message);
				JScrollPane scrollPane = new JScrollPane(textArea);
                textArea.setFont(new Font("monospaced", Font.BOLD, 16));
				scrollPane.setPreferredSize(new Dimension(700, 500));
				scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
				JOptionPane.showMessageDialog(null, scrollPane, "API Search Results - "+person, -1);

			} else {
				lblStatus.setText("Status: CWID not given.");
			}
		});
		////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////
		Panel subPanel_2 = new Panel();
		subPanel_2.setBackground(Color.LIGHT_GRAY);
		subPanel_2.setBounds(5, 170, 230, 102);
		panel.add(subPanel_2);
		subPanel_2.setLayout(null);
		////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////

		JButton button_Reactivated = new JButton("Find all reactivated employees");
		button_Reactivated.setBounds(5, 5, 220, 29);
		button_Reactivated.addActionListener(arg0 -> {
			String message;
			lblStatus.setText("Status: Finding all reactivated employees.");
			message = connection.findAllDeleted();
			JTextArea textArea = new JTextArea(message);
			JScrollPane scrollPane = new JScrollPane(textArea);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			scrollPane.setPreferredSize(new Dimension(500, 500));
			JOptionPane.showMessageDialog(null, scrollPane, "All Reactivated Employees", -1);
			lblStatus.setText("");
		});
		subPanel_2.add(button_Reactivated);

		//////////////////////////////////////////////////////////////////////////

		JButton btnSubmit_changedLogins = new JButton("Find all changed login IDs");
		btnSubmit_changedLogins.setBounds(5, 35, 220, 29);
		subPanel_2.add(btnSubmit_changedLogins);
		btnSubmit_changedLogins.addActionListener(e2 -> {
			String message;
			lblStatus.setText("Status: Finding changed Login IDs.");
			message = connection.findChangedLogin();
			JTextArea textArea = new JTextArea(message);
			JScrollPane scrollPane = new JScrollPane(textArea);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			scrollPane.setPreferredSize(new Dimension(800, 500));
			JOptionPane.showMessageDialog(null, scrollPane, "Changed Login IDs", -1);
			lblStatus.setText("");
		});

		/////////////////////////////////////////////////////////////////////////////////////////

		JButton btnSubmit_4 = new JButton("Find all deleted employees");
		btnSubmit_4.setBounds(5, 65, 220, 29);
		subPanel_2.add(btnSubmit_4);
		btnSubmit_4.addActionListener(e2 -> {
			String message;
			lblStatus.setText("Status: Finding all deactivated employees.");
			message = connection.findAllDeleted();
			JTextArea textArea = new JTextArea(message);
			JScrollPane scrollPane = new JScrollPane(textArea);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			scrollPane.setPreferredSize(new Dimension(500, 500));
			JOptionPane.showMessageDialog(null, scrollPane, "All Deactivated Employees", -1);
			lblStatus.setText("");
		});


		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JLabel lblBatchSearch = new JLabel("Batch job");
		lblBatchSearch.setFont(new Font("Tahoma", 1, 13));
		lblBatchSearch.setBounds(556, 8, 88, 23);
		panel.add(lblBatchSearch);

		final JEditorPane editorPane = new JEditorPane();

		JScrollPane scrollPane2 = new JScrollPane(editorPane,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane2.setForeground(Color.BLACK);
		scrollPane2.setBounds(556, 30, 100, 211);
		panel.add(scrollPane2);

		JButton button305 = new JButton("Create 305");
		button305.setBounds(556, 245, 100, 29);
		panel.add(button305);

		button305.addActionListener(arg0 -> {
			lblStatus.setText("Status: Creating 305 file..");
			String people = editorPane.getText().trim();
			if (!people.equals("")) {
				String message = connection.findBatch305(people);
				JTextArea textArea = new JTextArea(message);
				JScrollPane scrollPane = new JScrollPane(textArea);
				textArea.setLineWrap(true);
				textArea.setWrapStyleWord(true);
				scrollPane.setPreferredSize(new Dimension(1200, 500));
				JOptionPane.showMessageDialog(null, scrollPane, "305 Batch Search", -1);
				lblStatus.setText("");
			} else {
				lblStatus.setText("Status: CWIDs not given");
			}
		});


		JButton button350 = new JButton("Create 350");
		button350.setBounds(556, 275, 100, 29);
		panel.add(button350);

		button350.addActionListener(arg0 -> {
			lblStatus.setText("Status: Creating 350 file..");
			String people = editorPane.getText().trim();
			if (!people.equals("")) {
				String message = connection.findBatch350(people);
				JTextArea textArea = new JTextArea(message);
				JScrollPane scrollPane = new JScrollPane(textArea);
				textArea.setLineWrap(true);
				textArea.setWrapStyleWord(true);
				scrollPane.setPreferredSize(new Dimension(600, 500));
				JOptionPane.showMessageDialog(null, scrollPane, "350 Batch Search", -1);
				lblStatus.setText("");

			} else {
				lblStatus.setText("Status: CWIDs not given");
			}
		});

		////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////
		Panel subPanel_3 = new Panel();
		subPanel_3.setBackground(Color.LIGHT_GRAY);
		subPanel_3.setBounds(5, 280, 500, 80);
		panel.add(subPanel_3);
		subPanel_3.setLayout(null);

		//////////////////////////////////////////////////////////////////
		JButton convertButton = new JButton("CONVERT");
		convertButton.setBounds(100, 50, 100, 23);
		subPanel_3.add(convertButton);

		JButton resetButton = new JButton("RESET");
		resetButton.setBounds(260, 50, 100, 23);
		subPanel_3.add(resetButton);
		//////////////////////////////////////////////////////////////////

		textField_cwid = new JTextField();
		textField_cwid.setBounds(5, 20, 146, 26);
		subPanel_3.add(textField_cwid);
		textField_cwid.setColumns(10);
		textField_cwid.setText("");

		textField_pidm = new JTextField();
		textField_pidm.setBounds(155, 20, 146, 26);
		subPanel_3.add(textField_pidm);
		textField_pidm.setColumns(10);
		textField_pidm.setText("");

		textField_name = new JTextField();
		textField_name.setBounds(305, 20, 146, 26);
		subPanel_3.add(textField_name);
		textField_name.setColumns(10);
		textField_name.setText("");

		JLabel lblCwid = new JLabel("CWID");
		lblCwid.setBounds(5, 5, 46, 14);
		subPanel_3.add(lblCwid);

		JLabel lblPidm = new JLabel("PIDM");
		lblPidm.setBounds(155, 5, 46, 14);
		subPanel_3.add(lblPidm);

		JLabel lblFullName = new JLabel("Full Name");
		lblFullName.setBounds(305, 5, 80, 14);
		subPanel_3.add(lblFullName);

		convertButton.addActionListener(e2 -> {
			//do stuff
			String cwid = textField_cwid.getText().trim();
			String pidm = textField_pidm.getText().trim();
			String name = textField_name.getText().trim();
			if(pidm.equals("") && cwid.equals("") && name.equals("")) { //use pidm
				//nothing given!
			}
			else if(cwid.equals("") && name.equals("")) { //use pidm
				textField_cwid.setText(connection.jdbc.getCWIDFromPIDM(pidm));
				textField_name.setText(connection.jdbc.getNameFromPIDM(pidm));
			}
			else if (pidm.equals("") && name.equals("")){ //use cwid
				textField_pidm.setText(connection.jdbc.getPIDMFromCWID(cwid));
				textField_name.setText(connection.jdbc.getNameFromPIDM(cwid));
			}
			else if (pidm.equals("") && cwid.equals("")){//use name
				textField_cwid.setText(connection.jdbc.getCWIDFromName(name));
				textField_pidm.setText(connection.jdbc.getPIDMFromCWID(textField_cwid.getText()));
			}
			cwidField_emp.setText(textField_cwid.getText());
		});

		resetButton.addActionListener(e2 -> {
			textField_name.setText("");
			textField_cwid.setText("");
			textField_pidm.setText("");
		});

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnClose = new JButton("Close");
		btnClose.setFont(new Font("Lucida Grande", 0, 15));
		btnClose.addActionListener(e2 -> frameEmployeeMenu.dispose());
		btnClose.setBounds(554, 395, 122, 54);
		panel.add(btnClose);

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnExit = new JButton("Exit");
		btnExit.setFont(new Font("Lucida Grande", 0, 15));
		btnExit.addActionListener(e2 -> {
			public_package.Exit_Confirmation window = new public_package.Exit_Confirmation(connection.firstName);
			window.frameExitConfirmation.setVisible(true);
			System.exit(0);
		});
		btnExit.setBounds(6, 395, 122, 57);
		panel.add(btnExit);
	}
}

