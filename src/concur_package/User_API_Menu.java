package concur_package;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class User_API_Menu {
	protected API_Package api;
	protected JFrame frame;

	private JTextField textField_users;
	private JTextField textField_userGET;
	String message;
	public User_API_Menu(API_Package a) {
		api = a;
		initialize();
	}
	private void  getUsersViaAPI() {

		JDialog dlgProgress;
		dlgProgress = new JDialog((java.awt.Frame)null, "Please wait.", true);//true means that the dialog created is modal
		dlgProgress.setIconImage(Toolkit.getDefaultToolkit().getImage(Main_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));

		JLabel lblStatus = new JLabel("Pulling data from API..");// this is just a label in which you can indicate the state of the processing
		lblStatus.setFont(new Font("Lucida Grande",Font.BOLD, 15));
		dlgProgress.getContentPane().add(BorderLayout.NORTH, lblStatus);

		JProgressBar pbProgress = new JProgressBar();
		pbProgress.setIndeterminate(true); //we'll use an indeterminate progress bar
		dlgProgress.getContentPane().add(BorderLayout.CENTER, pbProgress);


		//DO_NOTHING_ON_CLOSE, HIDE_ON_CLOSE, or DISPOSE_ON_CLOSE
		dlgProgress.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // prevent the user from closing the dialog
		dlgProgress.setSize(300, 110);
		dlgProgress.setResizable(true);
		dlgProgress.setLocationRelativeTo(frame);
		///////////
		SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
			protected Void doInBackground() {
				api.reinit();
				// puts in the object variable vendors
				api.sendGetRequest("/api/v3.0/common/users?Active=true&limit=100");
				return null;
			}
			protected void done() {
				System.out.println("Done getting API Users.");
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
	}
	private void compareBanner() {
		JDialog dlgProgress;
		dlgProgress = new JDialog((java.awt.Frame)null, "Please wait.", true);//true means that the dialog created is modal
		dlgProgress.setIconImage(Toolkit.getDefaultToolkit().getImage(Main_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));

		JLabel lblStatus = new JLabel("Comparing to what's in Banner..");// this is just a label in which you can indicate the state of the processing
		lblStatus.setFont(new Font("Lucida Grande",Font.BOLD, 15));
		dlgProgress.getContentPane().add(BorderLayout.NORTH, lblStatus);

		JProgressBar pbProgress = new JProgressBar();
		pbProgress.setIndeterminate(true); //we'll use an indeterminate progress bar
		dlgProgress.getContentPane().add(BorderLayout.CENTER, pbProgress);


		//DO_NOTHING_ON_CLOSE, HIDE_ON_CLOSE, or DISPOSE_ON_CLOSE
		dlgProgress.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // prevent the user from closing the dialog
		dlgProgress.setSize(300, 110);
		dlgProgress.setResizable(true);
		dlgProgress.setLocationRelativeTo(frame);
		///////////
		SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
			protected Void doInBackground() {
				ArrayList<User> people = api.functions.jdbc.getActiveUsers();

				message=api.writeApiUsers();
				message+="\n";
				message+=api.writeTrackingTableUsers(people);
				message+="\n";
				message+=api.compareUsersNotInBanner(people);
				message+="\n";
				message+=api.compareUsersNotInConcur(people);

				System.out.println("Done comparing.");
				return null;
			}
			protected void done() {
				dlgProgress.dispose();//close the modal dialog
				JOptionPane.showMessageDialog(null,message);
			}
		};
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(e -> sw.cancel(true));
		dlgProgress.getContentPane().add(BorderLayout.EAST, cancelButton);
		// this will start the processing on a separate thread
		sw.execute();
		//this will block user input as long as the processing task is working
		dlgProgress.setVisible(true);
	}
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.GRAY);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(User_API_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
		frame.setTitle("User API Menu");
		frame.setBounds(100, 100, 461, 355);
		frame.setDefaultCloseOperation(3);
		frame.getContentPane().setLayout(null);
		JButton btnExit = new JButton("Exit");
		btnExit.setFont(new Font("Dialog", 0, 15));
		btnExit.setBounds(10, 248, 122, 57);
		frame.getContentPane().add(btnExit);
		JButton btnClose = new JButton("Close");
		btnClose.setFont(new Font("Dialog", 0, 15));
		btnClose.setBounds(313, 248, 122, 57);
		frame.getContentPane().add(btnClose);


		textField_users = new JTextField();
		textField_users.setBounds(10, 177, 320, 48);
		frame.getContentPane().add(textField_users);
		textField_users.setColumns(10);
		textField_users.setText("/api/v3.0/common/user");

		JButton btnSubmit_users = new JButton("GET");
		btnSubmit_users.addActionListener(e -> {
			api.reinit();

			api.sendGetRequest(textField_users.getText().trim());
			String message=api.usersToString();

			JTextArea textArea = new JTextArea(message);
			JScrollPane scrollPane = new JScrollPane(textArea);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			textArea.setFont(new Font("monospaced", Font.BOLD, 16));
			scrollPane.setPreferredSize(new Dimension(500, 500));
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			JOptionPane.showMessageDialog(null, scrollPane, "API Search Results", -1);

		});
		btnSubmit_users.setBounds(340, 176, 95, 50);
		frame.getContentPane().add(btnSubmit_users);

		Panel panel_GET = new Panel();
		panel_GET.setBackground(Color.LIGHT_GRAY);
		panel_GET.setBounds(10, 10, 197, 57);
		frame.getContentPane().add(panel_GET);
		panel_GET.setLayout(null);


		
		textField_userGET = new JTextField();
		textField_userGET.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField_userGET.setBounds(10, 6, 87, 41);
		panel_GET.add(textField_userGET);
		textField_userGET.setText("11074958");
		textField_userGET.setColumns(10);
		
		JButton button_vendorID = new JButton("GET");
		button_vendorID.setBounds(119, 7, 68, 42);
		panel_GET.add(button_vendorID);



		JButton btn_createReport = new JButton("Inactive Users in Concur");
		btn_createReport.addActionListener(arg0 -> {
			getUsersViaAPI();
			compareBanner();
		});
		btn_createReport.setBounds(10, 92, 197, 48);
		frame.getContentPane().add(btn_createReport);

		button_vendorID.addActionListener(e -> {
			api.reinit();
			api.sendGetRequest("/api/v3.0/common/users?employeeID="+textField_userGET.getText().trim());
			String message=api.vendorsToString();

			JTextArea textArea = new JTextArea(message);
			JScrollPane scrollPane = new JScrollPane(textArea);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			textArea.setFont(new Font("monospaced", Font.BOLD, 16));
			scrollPane.setPreferredSize(new Dimension(500, 500));
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			JOptionPane.showMessageDialog(null, scrollPane, "API Search Results", -1);
		});





		btnExit.addActionListener(e2 -> System.exit(0));
		btnClose.addActionListener(e2 -> frame.dispose());
	}
}

