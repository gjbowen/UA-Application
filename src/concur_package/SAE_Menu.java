package concur_package;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EtchedBorder;

public class SAE_Menu {
	protected Function_Library connection;
	protected JFrame saeFrame;
	private JTextField searchString;
	private JTextField searchColumn;
	private String mode;
	private String module;
	private JTextField textField;

	public SAE_Menu(Function_Library conn) {
		connection = conn;
		initialize();
	}

	private void setMode(String str) {
		mode = str;
	}

	private void setModule(String mod) {
		module = mod;
	}

	private void initialize() {
		saeFrame = new JFrame();
		saeFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(SAE_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
		saeFrame.setTitle("SAE Menu");
		saeFrame.setBounds(100, 100, 592, 502);
		saeFrame.setDefaultCloseOperation(3);
		saeFrame.getContentPane().setLayout(null);
		JButton btnExit = new JButton("Exit");
		btnExit.setBounds(10, 395, 122, 57);
		btnExit.setFont(new Font("Dialog", 0, 15));
		saeFrame.getContentPane().add(btnExit);
		JButton btnClose = new JButton("Close");
		btnClose.setBounds(444, 395, 122, 57);
		btnClose.setFont(new Font("Dialog", 0, 15));
		saeFrame.getContentPane().add(btnClose);
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 305, 63);
		FlowLayout flowLayout = (FlowLayout)panel.getLayout();
		flowLayout.setAlignment(0);
		saeFrame.getContentPane().add(panel);
		JLabel lblReportKey = new JLabel("String to Search ");
		panel.add(lblReportKey);
		lblReportKey.setFont(new Font("Tahoma", 0, 18));
		searchString = new JTextField();
		panel.add(searchString);
		searchString.setColumns(10);
		searchString.setText("");
		JLabel Column_label = new JLabel("Column to Search");
		Column_label.setFont(new Font("Tahoma", 0, 18));
		panel.add(Column_label);
		searchColumn = new JTextField();
		searchColumn.setText("");
		searchColumn.setColumns(10);
		panel.add(searchColumn);
		setMode("equals");
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBounds(359, 11, 108, 125);
		FlowLayout flowLayout_1 = (FlowLayout)panel_1.getLayout();
		flowLayout_1.setAlignment(0);
		saeFrame.getContentPane().add(panel_1);
		final JRadioButton rdbtnEquals = new JRadioButton("Equals");
		panel_1.add(rdbtnEquals);
		rdbtnEquals.setSelected(true);
		final JRadioButton startsWith = new JRadioButton("Starts with");
		panel_1.add(startsWith);
		final JRadioButton rdbtnEndsWith = new JRadioButton("Ends with");
		panel_1.add(rdbtnEndsWith);
		final JRadioButton rdbtnContains = new JRadioButton("Contains");
		panel_1.add(rdbtnContains);
		rdbtnContains.addActionListener(e2 -> {
			setMode("contains");
			startsWith.setSelected(false);
			rdbtnEndsWith.setSelected(false);
			rdbtnEquals.setSelected(false);
		});
		startsWith.addActionListener(e2 -> {
			setMode("startsWith");
			rdbtnEquals.setSelected(false);
			rdbtnEndsWith.setSelected(false);
			rdbtnContains.setSelected(false);
		});
		rdbtnEndsWith.addActionListener(e2 -> {
			setMode("endsWith");
			startsWith.setSelected(false);
			rdbtnEquals.setSelected(false);
			rdbtnContains.setSelected(false);
		});
		rdbtnEquals.addActionListener(e2 -> {
			setMode("equals");
			startsWith.setSelected(false);
			rdbtnEndsWith.setSelected(false);
			rdbtnContains.setSelected(false);
		});
		JButton btnSubmit = new JButton("Search");
		btnSubmit.setBounds(397, 162, 122, 57);
		saeFrame.getContentPane().add(btnSubmit);
		btnSubmit.setFont(new Font("Tahoma", 0, 15));
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2.setBounds(10, 85, 337, 134);
		saeFrame.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		JLabel lblKey = new JLabel("Column Cheat Sheet");
		lblKey.setFont(new Font("Tahoma", 0, 15));
		lblKey.setBounds(10, 11, 179, 16);
		panel_2.add(lblKey);
		JButton reportKey = new JButton("Report Key - 20");
		reportKey.setBounds(10, 38, 169, 20);
		panel_2.add(reportKey);
		reportKey.addActionListener(e2 -> searchColumn.setText("20"));
		JButton person = new JButton("Person(CWID) - 5");
		person.setBounds(10, 53, 169, 20);
		panel_2.add(person);
		person.addActionListener(e2 -> searchColumn.setText("5"));
		JButton transAmt = new JButton("Trans Amnt - 11");
		transAmt.setBounds(10, 69, 169, 20);
		panel_2.add(transAmt);
		transAmt.addActionListener(e2 -> searchColumn.setText("11"));
		JButton fundCode = new JButton("Fund - 192");
		fundCode.addActionListener(e2 -> searchColumn.setText("192"));
		fundCode.setBounds(186, 69, 138, 20);
		panel_2.add(fundCode);
		fundCode.addActionListener(e2 -> searchColumn.setText("192"));
		JButton orgnCode = new JButton("ORGN - 193");
		orgnCode.setBounds(186, 53, 138, 20);
		panel_2.add(orgnCode);
		orgnCode.addActionListener(e2 -> searchColumn.setText("192"));

		JButton acctCode = new JButton("Account Code - 63");
		acctCode.setBounds(10, 86, 169, 20);
		panel_2.add(acctCode);
		JButton date = new JButton("Date (YYYY-MM-DD) - 2");
		date.setBounds(10, 105, 169, 20);
		panel_2.add(date);

		JButton descrpt = new JButton("Description - 69");
		descrpt.addActionListener(arg0 -> searchColumn.setText("69"));
		descrpt.setBounds(186, 38, 138, 20);
		panel_2.add(descrpt);
		date.addActionListener(e2 -> searchColumn.setText("2"));
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		FlowLayout flowLayout_2 = (FlowLayout)panel_3.getLayout();
		flowLayout_2.setAlignment(0);
		panel_3.setBounds(477, 11, 89, 125);
		saeFrame.getContentPane().add(panel_3);
		JLabel lblModule = new JLabel("Module");
		lblModule.setFont(new Font("Tahoma", 0, 18));
		panel_3.add(lblModule);
		final JRadioButton bothButton = new JRadioButton("Both");
		bothButton.setSelected(true);
		panel_3.add(bothButton);
		final JRadioButton expenseButton = new JRadioButton("Expense");
		panel_3.add(expenseButton);
		final JRadioButton pCardButton = new JRadioButton("P Card");
		panel_3.add(pCardButton);
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 233, 556, 16);
		saeFrame.getContentPane().add(separator);
		textField = new JTextField();
		textField.setText("5,18,20,63,71,191,192,193,194,195,6,7,8,27,33,41,42,43,324,325,31,63,71,250,314,334");
		//textField.setText("20,31,63,70,250,313,334");//
		textField.setColumns(10);
		textField.setBounds(10, 260, 461, 35);
		saeFrame.getContentPane().add(textField);

		JCheckBox chckbxValidateItems = new JCheckBox("Validate Items");
		chckbxValidateItems.setBounds(10, 302, 122, 23);
		saeFrame.getContentPane().add(chckbxValidateItems);

		JButton button = new JButton("Create SAE");
		
		JPanel panel_trasfer = new JPanel();
		panel_trasfer.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		FlowLayout fl_panel_trasfer = (FlowLayout) panel_trasfer.getLayout();
		fl_panel_trasfer.setAlignment(FlowLayout.LEFT);
		panel_trasfer.setBounds(477, 244, 89, 125);
		saeFrame.getContentPane().add(panel_trasfer);
		
		JLabel lblTranser = new JLabel("Transer");
		lblTranser.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_trasfer.add(lblTranser);
		
		JCheckBox chckbx_dev = new JCheckBox("JS-DEV");
		chckbx_dev.setSelected(true);
		panel_trasfer.add(chckbx_dev);
		
		JCheckBox chckbx_test = new JCheckBox("JS-TEST");
		chckbx_test.setSelected(true);
		panel_trasfer.add(chckbx_test);
		
		JCheckBox chckbx_prod = new JCheckBox("JS-PROD");
		chckbx_prod.setSelected(true);
		panel_trasfer.add(chckbx_prod);
		button.addActionListener(arg0 -> {
			String transfer="";
			if(chckbx_dev.isSelected())
				transfer+="SEVL.";
			if(chckbx_test.isSelected())
				transfer+="TEST.";
			if(chckbx_prod.isSelected())
				transfer+="PROD";
			connection.generateSAE(textField.getText().trim(),chckbxValidateItems.isSelected(),transfer);
		});

		button.setFont(new Font("Tahoma", 0, 15));
		button.setBounds(331, 306, 140, 39);
		saeFrame.getContentPane().add(button);

		JLabel lblCreateMaster = new JLabel("Creates SAE from all files. Enter desired columns seperated by a comma. Use * for all.");
		lblCreateMaster.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblCreateMaster.setBounds(10, 244, 509, 16);
		saeFrame.getContentPane().add(lblCreateMaster);
		
	

		bothButton.addActionListener(e2 -> {
			setModule("both");
			pCardButton.setSelected(false);
			expenseButton.setSelected(false);
		});
		expenseButton.addActionListener(e2 -> {
			setModule("expense");
			bothButton.setSelected(false);
			pCardButton.setSelected(false);
		});
		pCardButton.addActionListener(e2 -> {
			setModule("pCard");
			bothButton.setSelected(false);
			expenseButton.setSelected(false);
		});
		btnSubmit.addActionListener(e2 -> {
			if (searchString.getText().trim().equals("") || searchColumn.getText().trim().equals("")) {
			}
			else {

				JDialog dlgProgress;
				dlgProgress = new JDialog((java.awt.Frame)null, "Please wait.", true);//true means that the dialog created is modal
				JLabel lblStatus = new JLabel("Searching files.."); // this is just a label in which you can indicate the state of the processing
				dlgProgress.setIconImage(Toolkit.getDefaultToolkit().getImage(SAE_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
				JProgressBar pbProgress = new JProgressBar(100, 100);
				pbProgress.setIndeterminate(true); //we'll use an indeterminate progress bar

				dlgProgress.getContentPane().add(BorderLayout.NORTH, lblStatus);
				dlgProgress.getContentPane().add(BorderLayout.CENTER, pbProgress);
				dlgProgress.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // prevent the user from closing the dialog
				dlgProgress.setSize(300, 90);
				dlgProgress.setLocationRelativeTo(saeFrame);
				SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
					protected Void doInBackground() throws Exception {
						String message = connection.searchSAE(searchString.getText().trim(), Integer.parseInt(searchColumn.getText().trim()), mode, module);

						if(message.startsWith("No results for: ")) {
							JOptionPane.showMessageDialog(null, message);
						}else {


							JTextArea textArea = new JTextArea(message);
							JScrollPane scrollPane = new JScrollPane(textArea);
							textArea.setLineWrap(true);
							textArea.setWrapStyleWord(true);
							scrollPane.setPreferredSize(new Dimension(800, 800));// x WIDTH x HEIGHT
							JOptionPane.showMessageDialog(null, scrollPane, "SAE", -1);
						}

						return null;
					}

					protected void done() {
						dlgProgress.dispose();//close the modal dialog

					}
				};
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(e -> sw.cancel(true));
				dlgProgress.getContentPane().add(BorderLayout.EAST, cancelButton);
				sw.execute(); // this will start the processing on a separate thread
				dlgProgress.setVisible(true); //this will block user input as long as the processing task is working

			}
		});
		btnExit.addActionListener(e2 -> {
			public_package.Exit_Confirmation window = new public_package.Exit_Confirmation(connection.firstName);
			window.frameExitConfirmation.setVisible(true);
		});
		btnClose.addActionListener(e2 -> saeFrame.dispose());
	}
}