package concur_package;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class SRE_Menu {
	protected Function_Library connection;
	protected JFrame sreFrame;
	private JTextField searchString;
	private JTextField searchColumn;
	private String mode;

	public SRE_Menu(Function_Library conn) {
		connection = conn;
		initialize();
	}

	private void setMode(String str) {
		mode = str;
	}


	private void initialize() {
		sreFrame = new JFrame();
		sreFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(SRE_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
		sreFrame.setTitle("SRE Menu");
		sreFrame.setBounds(100, 100, 491, 402);
		sreFrame.setDefaultCloseOperation(3);
		sreFrame.getContentPane().setLayout(null);
		JButton btnExit = new JButton("Exit");
		btnExit.setBounds(10, 295, 112, 57);
		btnExit.setFont(new Font("Dialog", 0, 15));
		sreFrame.getContentPane().add(btnExit);
		JButton btnClose = new JButton("Close");
		btnClose.setBounds(359, 295, 112, 57);
		btnClose.setFont(new Font("Dialog", 0, 15));
		sreFrame.getContentPane().add(btnClose);
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 305, 63);
		FlowLayout flowLayout = (FlowLayout)panel.getLayout();
		flowLayout.setAlignment(0);
		sreFrame.getContentPane().add(panel);
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
		searchColumn.setText("48");
		searchColumn.setColumns(10);
		panel.add(searchColumn);
		setMode("equals");
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBounds(359, 11, 108, 125);
		FlowLayout flowLayout_1 = (FlowLayout)panel_1.getLayout();
		flowLayout_1.setAlignment(0);
		sreFrame.getContentPane().add(panel_1);
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
		btnSubmit.setBounds(359, 162, 108, 57);
		sreFrame.getContentPane().add(btnSubmit);
		btnSubmit.setFont(new Font("Tahoma", 0, 15));
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2.setBounds(10, 85, 337, 134);
		sreFrame.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		JLabel lblKey = new JLabel("Column Cheat Sheet");
		lblKey.setFont(new Font("Tahoma", 0, 15));
		lblKey.setBounds(10, 11, 179, 16);
		panel_2.add(lblKey);
		JButton reportKey = new JButton("Request Key - 48");
		reportKey.setBounds(10, 38, 169, 20);
		panel_2.add(reportKey);
		reportKey.addActionListener(e2 -> searchColumn.setText("48"));
		JButton person = new JButton("Person(CWID) - 5");
		person.setBounds(10, 58, 169, 20);
		panel_2.add(person);
		person.addActionListener(e2 -> searchColumn.setText("5"));

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 233, 556, 16);



		btnSubmit.addActionListener(e2 -> {
			if (searchString.getText().trim().equals("") || searchColumn.getText().trim().equals("")) {
			}
			else {

				JDialog dlgProgress;
				dlgProgress = new JDialog((java.awt.Frame)null, "Please wait.", true);//true means that the dialog created is modal
				JLabel lblStatus = new JLabel("Searching files.."); // this is just a label in which you can indicate the state of the processing
				dlgProgress.setIconImage(Toolkit.getDefaultToolkit().getImage(SRE_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
				JProgressBar pbProgress = new JProgressBar(100, 100);
				pbProgress.setIndeterminate(true); //we'll use an indeterminate progress bar

				dlgProgress.getContentPane().add(BorderLayout.NORTH, lblStatus);
				dlgProgress.getContentPane().add(BorderLayout.CENTER, pbProgress);
				dlgProgress.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // prevent the user from closing the dialog
				dlgProgress.setSize(300, 90);
				dlgProgress.setLocationRelativeTo(sreFrame);
				SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
					protected Void doInBackground() {
						String message = connection.searchSRE(searchString.getText().trim(), Integer.parseInt(searchColumn.getText().trim()), mode);

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
		btnClose.addActionListener(e2 -> sreFrame.dispose());
	}
}