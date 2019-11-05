package concur_package;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class VendorTerm_Menu {
	protected Function_Library connection;
	protected JFrame vendorTermFrame;
	private JTextField textField;
	private JTextField fileNameField;
	private String message;
	public VendorTerm_Menu(Function_Library conn) {
		connection = conn;
		initialize();
	}

	private void initialize() {
		vendorTermFrame = new JFrame();
		vendorTermFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(VendorTerm_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
		vendorTermFrame.setTitle("Vendor Term Menu");
		vendorTermFrame.setBounds(100, 100, 461, 355);
		vendorTermFrame.setDefaultCloseOperation(3);
		vendorTermFrame.getContentPane().setLayout(null);
		JButton btnExit = new JButton("Exit");
		btnExit.setFont(new Font("Dialog", 0, 15));
		btnExit.setBounds(10, 248, 122, 57);
		vendorTermFrame.getContentPane().add(btnExit);
		JButton btnClose = new JButton("Close");
		btnClose.setFont(new Font("Dialog", 0, 15));
		btnClose.setBounds(313, 248, 122, 57);
		vendorTermFrame.getContentPane().add(btnClose);
		JLabel lblReportKey = new JLabel("Vendor ID");
		lblReportKey.setFont(new Font("Tahoma", 0, 18));
		lblReportKey.setBounds(10, 37, 134, 35);
		vendorTermFrame.getContentPane().add(lblReportKey);
		textField = new JTextField();
		textField.setBounds(136, 42, 167, 30);
		vendorTermFrame.getContentPane().add(textField);
		textField.setColumns(10);
		textField.setText("");
		JButton btnReportKey = new JButton("Submit");
		btnReportKey.setFont(new Font("Tahoma", 0, 15));
		btnReportKey.setBounds(313, 42, 105, 30);
		vendorTermFrame.getContentPane().add(btnReportKey);

		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(e -> {
			if (!fileNameField.getText().equals("") ) {
				JOptionPane.showMessageDialog(null,
						connection.retireVendorTerms(fileNameField.getText().trim()));
			}
		});
		btnSubmit.setBounds(10, 191, 89, 23);
		vendorTermFrame.getContentPane().add(btnSubmit);

		JLabel lblRetireVendorTerms = new JLabel("Retire Vendor Terms with file \r\nof CWID's and RT Addresses.");
		lblRetireVendorTerms.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblRetireVendorTerms.setBounds(10, 117, 425, 35);
		vendorTermFrame.getContentPane().add(lblRetireVendorTerms);

		fileNameField = new JTextField();
		fileNameField.setText(connection.pwd()+"\\active_to_delete.csv");
		fileNameField.setColumns(10);
		fileNameField.setBounds(10, 150, 373, 30);
		vendorTermFrame.getContentPane().add(fileNameField);
		btnReportKey.addActionListener(e2 -> {

			JDialog dlgProgress;
			dlgProgress = new JDialog((java.awt.Frame)null, "Please wait.", true);//true means that the dialog created is modal
			dlgProgress.setIconImage(Toolkit.getDefaultToolkit().getImage(Main_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));

			JLabel lblStatus = new JLabel("Searching terminated vendor files..."); // this is just a label in which you can indicate the state of the processing
			lblStatus.setFont(new Font("Lucida Grande",Font.BOLD, 15));
			dlgProgress.getContentPane().add(BorderLayout.NORTH, lblStatus);

			JProgressBar pbProgress = new JProgressBar();
			pbProgress.setIndeterminate(true); //we'll use an indeterminate progress bar
			dlgProgress.getContentPane().add(BorderLayout.CENTER, pbProgress);


			//DO_NOTHING_ON_CLOSE, HIDE_ON_CLOSE, or DISPOSE_ON_CLOSE
			dlgProgress.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // prevent the user from closing the dialog
			dlgProgress.setSize(300, 110);
			dlgProgress.setResizable(true);
			dlgProgress.setLocationRelativeTo(vendorTermFrame);
			///////////
			SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
				protected Void doInBackground() {
					System.out.println("SEARCHING..");
					message = connection.searchVendorTerm(textField.getText().trim());
					System.out.println("DONE..");
					if(message.startsWith("Vendor not found - ")) {
						JOptionPane.showMessageDialog(null, message);
					}else {
						JTextArea textArea = new JTextArea(message);
						JScrollPane scrollPane = new JScrollPane(textArea);
						textArea.setLineWrap(true);
						textArea.setWrapStyleWord(true);
						scrollPane.setPreferredSize(new Dimension(1000, 800));// x WIDTH x HEIGHT
						JOptionPane.showMessageDialog(null, scrollPane, "Vendor " + textField.getText().trim(), -1);
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
			// this will start the processing on a separate thread
			sw.execute();
			//this will block user input as long as the processing task is working
			dlgProgress.setVisible(true);
		});
		btnExit.addActionListener(e2 -> System.exit(0));
		btnClose.addActionListener(e2 -> vendorTermFrame.dispose());
	}
}

