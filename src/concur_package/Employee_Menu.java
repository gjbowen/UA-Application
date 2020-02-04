package concur_package;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Panel;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

class Employee_Menu {
	JFrame frameEmployeeMenu;
	private final Function_Library connection;
	private final API_Package api;
	private JTextField cwidField_emp;

	private JTextField textField_column;

	private JTextField textField_cwid;
	private JTextField textField_pidm;
	private JTextField textField_name;
	private JTextField textField_myBama;
	private JTextField textField_email;

	public Employee_Menu(Function_Library conn) {
		connection = conn;
		this.initialize();
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
		lblStatus.setFont(new Font("AppleGothic", Font.PLAIN, 16));
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
		lbl_person.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbl_person.setBounds(5, 8, 80, 23);
		subPanel_1.add(lbl_person);

		cwidField_emp = new JTextField();
		cwidField_emp.setBounds(150, 6, 130, 26);
		cwidField_emp.setText("11256098");

		subPanel_1.add(cwidField_emp);
		cwidField_emp.setColumns(10);

		//////////////////////////////////////////////////////////////////////
		textField_cwid = new JTextField();
		textField_pidm = new JTextField();
		textField_name = new JTextField();
		textField_myBama = new JTextField();
		textField_email = new JTextField();

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

				//make the pane
				final JEditorPane myPane = new JEditorPane();
				myPane.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
				myPane.setEditable(false);
				myPane.setAutoscrolls(true);
				myPane.addHyperlinkListener(new HyperlinkListener() {
					public void hyperlinkUpdate(HyperlinkEvent e) {
						if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
							try {
								Runtime.getRuntime().exec("explorer.exe /select," + e.getDescription());
							} catch (IOException ex) {ex.printStackTrace();}
					}
				});
				myPane.setText(message);

				//set the frame
				JFrame myFrame = new JFrame("Search results");
				//myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				myFrame.setSize(1500, 600);
				myFrame.setResizable(true);

				//add the pane to the frame
				myFrame.setContentPane(myPane);

				//finally, show it!
				myFrame.setVisible(true);

				lblStatus.setText("");
			}
			else {
				lblStatus.setText("Status: CWID not given.");
				//connection.findEmployeeCustom();
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
					JOptionPane.showMessageDialog(null, scrollPane, "Tracking Table Contents for " + person, JOptionPane.PLAIN_MESSAGE);
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
				JOptionPane.showMessageDialog(null, scrollPane, "API Search Results - "+person, JOptionPane.PLAIN_MESSAGE);

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
			JOptionPane.showMessageDialog(null, scrollPane, "All Reactivated Employees", JOptionPane.PLAIN_MESSAGE);
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
			JOptionPane.showMessageDialog(null, scrollPane, "Changed Login IDs", JOptionPane.PLAIN_MESSAGE);
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
			JOptionPane.showMessageDialog(null, scrollPane, "All Deactivated Employees", JOptionPane.PLAIN_MESSAGE);
			lblStatus.setText("");
		});

		//////////////////////////////////////////////////////
		Panel subPanel_3 = new Panel();
		subPanel_3.setBackground(Color.LIGHT_GRAY);
		subPanel_3.setBounds(315, 10, 110, 280);
		panel.add(subPanel_3);
		subPanel_3.setLayout(null);
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JLabel lblBatchSearch = new JLabel("Batch job");
		lblBatchSearch.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblBatchSearch.setBounds(5, 1, 88, 23);
		subPanel_3.add(lblBatchSearch);

		final JEditorPane editorPane = new JEditorPane();

		JScrollPane scrollPane2 = new JScrollPane(editorPane,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane2.setForeground(Color.BLACK);
		scrollPane2.setBounds(5, 20, 100, 190);
		subPanel_3.add(scrollPane2);

		JButton button305 = new JButton("Create 305");
		button305.setBounds(5, 213, 100, 29);
		subPanel_3.add(button305);

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
				JOptionPane.showMessageDialog(null, scrollPane, "305 Batch Search", JOptionPane.PLAIN_MESSAGE);
				lblStatus.setText("");
			} else {
				lblStatus.setText("Status: CWIDs not given");
			}
		});

		JButton button350 = new JButton("Create 350");
		button350.setBounds(5, 245, 100, 29);
		subPanel_3.add(button350);

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
				JOptionPane.showMessageDialog(null, scrollPane, "350 Batch Search", JOptionPane.PLAIN_MESSAGE);
				lblStatus.setText("");

			} else {
				lblStatus.setText("Status: CWIDs not given");
			}
		});

		////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////
		Panel subPanel_4 = new Panel();
		subPanel_4.setBackground(Color.LIGHT_GRAY);
		subPanel_4.setBounds(5, 300, 670, 80);
		panel.add(subPanel_4);
		subPanel_4.setLayout(null);
		//////////////////////////////////////
		JTextField textField_cwid = new JTextField();
		JTextField textField_pidm = new JTextField();
		JTextField textField_name = new JTextField();
		JTextField textField_myBama = new JTextField();
		JTextField textField_email = new JTextField();

		JLabel lblCwid = new JLabel("CWID");
		JLabel lblPidm = new JLabel("PIDM");
		JLabel lblFullName = new JLabel("Full Name");
		JLabel lblMyBama = new JLabel("MyBama");
		JLabel lblEmail = new JLabel("Email");

		textField_cwid.setBounds(5, 20, 75, 26);
		textField_pidm.setBounds(85, 20, 75, 26);
		textField_name.setBounds(165, 20, 175, 26);
		textField_myBama.setBounds(345, 20, 120, 26);
		textField_email.setBounds(470, 20, 190, 26);

		lblCwid.setBounds(5, 5, 46, 14);
		lblPidm.setBounds(85, 5, 46, 14);
		lblFullName.setBounds(165, 5, 80, 14);
		lblMyBama.setBounds(345, 5, 80, 14);
		lblEmail.setBounds(470, 5, 80, 14);

		subPanel_4.add(textField_cwid);
		subPanel_4.add(textField_pidm);
		subPanel_4.add(textField_name);
		subPanel_4.add(textField_myBama);
		subPanel_4.add(textField_email);

		subPanel_4.add(lblCwid);
		subPanel_4.add(lblPidm);
		subPanel_4.add(lblFullName);
		subPanel_4.add(lblMyBama);
		subPanel_4.add(lblEmail);

		JButton convertButton = new JButton("CONVERT");
		convertButton.setBounds(100, 50, 100, 23);
		subPanel_4.add(convertButton);

		convertButton.addActionListener(e2 -> {
			ArrayList<String> content = connection.jdbc.convert(
					textField_pidm.getText().trim(),
					textField_cwid.getText().trim(),
					textField_name.getText().trim(),
					textField_email.getText().trim(),
					textField_myBama.getText().trim());

			textField_pidm.setText(content.get(0));
			textField_cwid.setText(content.get(1));
			textField_name.setText(content.get(2));
			textField_email.setText(content.get(3));
			textField_myBama.setText(content.get(4));

			cwidField_emp.setText(content.get(1));
		});

		JButton resetButton = new JButton("RESET");
		resetButton.setBounds(260, 50, 100, 23);
		subPanel_4.add(resetButton);

		resetButton.addActionListener(e2 -> {
			textField_name.setText("");
			textField_cwid.setText("");
			textField_email.setText("");
			textField_myBama.setText("");
			textField_pidm.setText("");
		});

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnClose = new JButton("Close");
		btnClose.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		btnClose.addActionListener(e2 -> frameEmployeeMenu.dispose());
		btnClose.setBounds(554, 395, 122, 54);
		panel.add(btnClose);

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnExit = new JButton("Exit");
		btnExit.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		btnExit.addActionListener(e2 -> {
			public_package.Exit_Confirmation window = new public_package.Exit_Confirmation(connection.firstName);
			window.frameExitConfirmation.setVisible(true);
			System.exit(0);
		});
		btnExit.setBounds(6, 395, 122, 57);
		panel.add(btnExit);
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	}
}

