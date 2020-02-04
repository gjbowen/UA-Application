package concur_package;

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
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;

import java.awt.Panel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;

class Vendor_API_Menu {
	private API_Package api;
	JFrame frame;

	private JTextField textField_vendors;
	private JTextField textField_deleteID;
	private JTextField textField_deleteRT;
	private JTextField textField;
	private String message = null;

	public Vendor_API_Menu(API_Package a) {
		api = a;
		initialize();
	}
	private void  getVendorsViaAPI() {
	
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
				api.sendGetRequest("/api/v3.0/invoice/vendors?limit=1000");
				return null;
			}
			protected void done() {
				System.out.println("Done with getting vendors.");
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
				ArrayList<Vendor> vendors = api.functions.jdbc.getActiveVendors();

				message = api.writeTrackingTableVendors(vendors);
				message += "\n";
				message += api.writeApiVendors();
				message += "\n";
				message +=  api.compareVendorsNotInBanner(vendors);
				message += "\n";
				message += api.compareVendorsNotInConcur(vendors);

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
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Vendor_API_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
		frame.setTitle("Vendor API Menu");
		frame.setBounds(100, 100, 462, 407);
		frame.setDefaultCloseOperation(3);
		frame.getContentPane().setLayout(null);

		JButton btnExit = new JButton("Exit");
		btnExit.setFont(new Font("Dialog", 0, 15));
		btnExit.setBounds(10, 300, 122, 57);
		frame.getContentPane().add(btnExit);

		JButton btnClose = new JButton("Close");
		btnClose.setFont(new Font("Dialog", 0, 15));
		btnClose.setBounds(313, 300, 122, 57);
		frame.getContentPane().add(btnClose);


		textField_vendors = new JTextField();
		textField_vendors.setBounds(11, 241, 320, 48);
		frame.getContentPane().add(textField_vendors);
		textField_vendors.setColumns(10);
		textField_vendors.setText("/api/v3.0/invoice/vendors?vendorCode=11074958");

		JButton btnSubmit_vendors = new JButton("GET");
		btnSubmit_vendors.addActionListener(e -> {
			api.reinit();

			api.sendGetRequest(textField_vendors.getText().trim());
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
		btnSubmit_vendors.setBounds(341, 239, 95, 50);
		frame.getContentPane().add(btnSubmit_vendors);
		
		Panel panel_GET = new Panel();
		panel_GET.setBackground(Color.LIGHT_GRAY);
		panel_GET.setBounds(10, 10, 197, 152);
		frame.getContentPane().add(panel_GET);
		panel_GET.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(52, 6, 87, 41);
		panel_GET.add(textField);
		textField.setText("11728965");
		textField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField.setColumns(10);
		
		JButton button_vendorID = new JButton("GET Request");
		button_vendorID.setBounds(10, 58, 177, 42);
		panel_GET.add(button_vendorID);
		
		JButton button = new JButton("Banner Vendor");
		button.setBounds(10, 99, 177, 42);
		panel_GET.add(button);
		button.addActionListener(e -> {
			api.reinit();

			String message=api.functions.jdbc.getVendorInfo(textField.getText().trim());

			JTextArea textArea = new JTextArea(message);
			JScrollPane scrollPane = new JScrollPane(textArea);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			textArea.setFont(new Font("Tahoma", Font.PLAIN, 18));
			scrollPane.setPreferredSize(new Dimension(500, 500));
			JOptionPane.showMessageDialog(null, scrollPane, "Banner Tracking", -1);
		});
		button_vendorID.addActionListener(e -> {
			api.reinit();
			api.sendGetRequest("/api/v3.0/invoice/vendors?vendorCode="+textField.getText().trim());
			String message=api.vendorsToString();
			System.out.println("OUTPUT MESSAGE: "+message);

			JTextArea textArea = new JTextArea(message);
			JScrollPane scrollPane = new JScrollPane(textArea);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			textArea.setFont(new Font("Tahoma", Font.PLAIN, 18));
			scrollPane.setPreferredSize(new Dimension(700, 700));
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			JOptionPane.showMessageDialog(null, scrollPane, "API Search Results", -1);
		});
		
		Panel panel_DELETE = new Panel();
		panel_DELETE.setBackground(Color.LIGHT_GRAY);
		panel_DELETE.setLayout(null);
		panel_DELETE.setBounds(238, 10, 197, 118);
		
		frame.getContentPane().add(panel_DELETE);
		
		textField_deleteID = new JTextField();
		textField_deleteID.setText("11074958");
		textField_deleteID.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField_deleteID.setColumns(10);
		textField_deleteID.setBounds(10, 6, 99, 41);
		panel_DELETE.add(textField_deleteID);
		
		JButton btnDelete = new JButton("DELETE");
		btnDelete.addActionListener(arg0 -> {
			if (JOptionPane.showConfirmDialog(null,
					"Are you sure you want to delete this address? There's no going back..",
					"Warning", JOptionPane.YES_NO_OPTION) == 0) { //Yes
				api.reinit();
				api.sendDeleteRequest("/api/v3.0/invoice/vendors?vendorCode="+textField_deleteID.getText().trim()+"&addressCode="+textField_deleteRT.getText().trim());
				JTextArea textArea = new JTextArea(api.serverResponse);
				JScrollPane scrollPane = new JScrollPane(textArea);
				textArea.setLineWrap(true);
				textArea.setWrapStyleWord(true);
				scrollPane.setPreferredSize(new Dimension(400, 200));
				//JOptionPane.showMessageDialog(null, scrollPane, "Concur Server Response", -1);
				JOptionPane.showMessageDialog(null, api.serverResponse);
			}



		});
		btnDelete.setBounds(48, 58, 99, 42);
		panel_DELETE.add(btnDelete);
		
		textField_deleteRT = new JTextField();
		textField_deleteRT.setText("RT-1");
		textField_deleteRT.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField_deleteRT.setColumns(10);
		textField_deleteRT.setBounds(119, 6, 68, 41);
		panel_DELETE.add(textField_deleteRT);
		
		JButton btn_createReport = new JButton("Analyze Banner/Concur Differences");
		btn_createReport.addActionListener(arg0 -> {
			getVendorsViaAPI();
			compareBanner();
		});

		btn_createReport.setBounds(10, 182, 250, 48);
		frame.getContentPane().add(btn_createReport);

		btnExit.addActionListener(e2 -> System.exit(0));
		btnClose.addActionListener(e2 -> frame.dispose());
	}
}

