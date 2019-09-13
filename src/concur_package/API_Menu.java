package concur_package;

import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import java.awt.Button;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Panel;

public class API_Menu {
	protected JFrame frame;
	private API_Package api;
	String url;
	Function_Library connection;
	public API_Menu(Function_Library fun) {
		connection = fun;

		//connection.environment="TEST";//OVERRIDE TO TEST ENVIRONMENT

		api = new API_Package(connection);
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(API_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
		frame.setTitle("API Menu");
		frame.setBounds(100, 100, 461, 412);
		frame.setDefaultCloseOperation(3);
		frame.getContentPane().setLayout(null);
		JButton btnExit = new JButton("Exit");
		btnExit.setFont(new Font("Dialog", 0, 15));
		btnExit.setBounds(10, 305, 122, 57);
		frame.getContentPane().add(btnExit);
		JButton btnClose = new JButton("Close");
		btnClose.setFont(new Font("Dialog", 0, 15));
		btnClose.setBounds(313, 305, 122, 57);
		frame.getContentPane().add(btnClose);

		JPanel panel = new JPanel();
		panel.setForeground(Color.BLACK);
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Documentation", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(157, 237, 146, 125);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		frame.getContentPane().add(panel);
		
		Button button_users = new Button("Users");
		button_users.addActionListener(e -> api.openLink("https://developer.concur.com/api-explorer/v3-0/Users.html"));
		panel.add(button_users);

		
		Button button_vendors = new Button("Vendors");
		button_vendors.addActionListener(e -> api.openLink("https://developer.concur.com/api-reference/invoice/v3.vendor.html"));
		panel.add(button_vendors);
		
		Panel panel_1 = new Panel();
		panel_1.setBounds(106, 101, 227, 92);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(new GridLayout(0, 2, 0, 0));
		
		JButton btnUsers = new JButton("Users");
		panel_1.add(btnUsers);
		btnUsers.addActionListener(e -> EventQueue.invokeLater(() -> {
			try {
				User_API_Menu window = new User_API_Menu(api);
				window.frame.setVisible(true);
				frame.dispose();
			} catch (Exception e12) {
				e12.printStackTrace();
			}
		}));
		
		
		
		JButton btnVendors = new JButton("Vendors");
		btnVendors.addActionListener(e -> EventQueue.invokeLater(() -> {
			try {
				Vendor_API_Menu window = new Vendor_API_Menu(api);
				window.frame.setVisible(true);
				frame.dispose();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}));
		panel_1.add(btnVendors);


		btnExit.addActionListener(e2 -> System.exit(0));
		btnClose.addActionListener(e2 -> frame.dispose());
	}
}

