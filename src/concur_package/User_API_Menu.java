package concur_package;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import java.awt.Panel;
import java.awt.Color;

public class User_API_Menu {
	protected API_Package api;
	protected JFrame frame;

	private JTextField textField_users;
	private JTextField textField_userGET;
	private JTextField textField_deleteID;
	private JTextField textField_deleteRT;
	public User_API_Menu(API_Package a) {
		api = a;
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.GRAY);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(User_API_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
		frame.setTitle("Vendor API Menu");
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

		JButton btnSubmit_vendors = new JButton("GET");
		btnSubmit_vendors.addActionListener(e -> {
			api.prep();

			api.sendGetRequest(textField_users.getText().trim());
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
		btnSubmit_vendors.setBounds(340, 176, 95, 50);
		frame.getContentPane().add(btnSubmit_vendors);
		
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
				api.prep();
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
		
		JButton btn_createReport = new JButton("Inactive Users in Concur");
		btn_createReport.addActionListener(arg0 -> {
			api.prep();
			api.sendGetRequest("/api/v3.0/invoice/vendors");
			api.compareVendorsNotInBanner(api.functions.jdbc.getVendors());
		});
		btn_createReport.setBounds(10, 92, 197, 48);
		frame.getContentPane().add(btn_createReport);
		button_vendorID.addActionListener(e -> {
			api.prep();
			api.sendGetRequest("/api/v3.0/invoice/vendors?vendorCode="+textField_userGET.getText().trim());
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

