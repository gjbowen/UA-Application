package concur_package;
import java.awt.*;
import java.io.IOException;
import java.sql.Connection;

import javax.swing.*;

import com.jcraft.jsch.Session;
import com.sshtools.sftp.SftpClient;


public class Main_Menu{
	public JFrame frame;
	private final Function_Library fun;

	public Main_Menu(Connection connection, SftpClient conn_sftp, Session conn_ssh,String user, String pass, String env){
		fun = new Function_Library(connection,conn_sftp,conn_ssh,user,pass,env);

		getFiles();
		initialize();
	}

	private void getFiles() {
		JDialog dlgProgress;
		dlgProgress = new JDialog((java.awt.Frame)null, "Please wait.", true);//true means that the dialog created is modal
		dlgProgress.setIconImage(Toolkit.getDefaultToolkit().getImage(Main_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));

		JLabel lblStatus = new JLabel("Retrieving files from "+ fun.sftp.getInstance(fun.sftp.environment)+".."); // this is just a label in which you can indicate the state of the processing
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
				fun.sftp.syncFiles();
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
	}
	private void initialize()
	{
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Main_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
		frame.setTitle("Main Menu ("+fun.environment+") - Welcome, " + fun.firstName);

		frame.setBounds(100, 100, 445, 336);
		frame.setDefaultCloseOperation(3);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setBounds(15, 6, 400, 198);
		frame.getContentPane().add(panel);
		panel.setLayout(new GridLayout(0, 2, 5, 5));

		////////////////
		// START GRID
		////////////////
		JButton btnEmp = new JButton("Employee Feed");
		panel.add(btnEmp);

		JButton btnReceipted_Paid = new JButton("Receipted or Paid");
		panel.add(btnReceipted_Paid);

		JButton btnPRAE = new JButton("PRAE");
		panel.add(btnPRAE);

		JButton btnSAE = new JButton("SAE");
		panel.add(btnSAE);

		JButton btnSRE = new JButton("SRE");
		panel.add(btnSRE);

		JButton btnVendorFeed = new JButton("Vendor Feed");
		panel.add(btnVendorFeed);

		JButton btnVendorTermFeed = new JButton("Vendor Term Feed");
		panel.add(btnVendorTermFeed);

		JButton btnFeed = new JButton("710 Feed");
		panel.add(btnFeed);

		JButton btnApi = new JButton("API");
		panel.add(btnApi);

		JButton btnFupload = new JButton("FUPLOAD");
		panel.add(btnFupload);

		JButton btnSHOW = new JButton("Show in Windows Explorer");
		panel.add(btnSHOW);

		JButton btnMoveToProd = new JButton("Move to SEVL/TEST");
		panel.add(btnMoveToProd);

		JButton btnClose = new JButton("Close");
		btnClose.setFont(new Font("Dialog", Font.PLAIN, 15));
		btnClose.setBounds(297, 221, 122, 57);
		frame.getContentPane().add(btnClose);

		JButton button_exit = new JButton("Exit");
		button_exit.setFont(new Font("Lucida Grande", 0, 15));
		button_exit.setBounds(6, 221, 122, 57);
		frame.getContentPane().add(button_exit);

		btnFeed.addActionListener(arg0 -> EventQueue.invokeLater(() -> {
			try {
				emp710_Menu window = new emp710_Menu(fun);
				window.frame.setVisible(true);
				//frame.dispose();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}));
		btnApi.addActionListener(arg0 -> EventQueue.invokeLater(() -> {
			try {
				API_Menu window = new API_Menu(fun);
				window.frame.setVisible(true);
				//frame.dispose();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}));
		btnFupload.addActionListener(arg0 -> EventQueue.invokeLater(() -> {
			try {
				Fupload_Menu window = new Fupload_Menu(fun);
				window.frame.setVisible(true);
				//frame.dispose();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}));
		btnSHOW.addActionListener(e -> {
			try{
				if(System.getProperty("os.name").toLowerCase().contains("windows")) { //for Windows..
					Runtime.getRuntime().exec("explorer.exe /select,"+System.getProperty("user.home")+"\\Concur_Files\\"+fun.environment);
				} //for other..

			}
			catch (IOException e1){
				e1.printStackTrace();
			}
		});
		btnMoveToProd.addActionListener(arg0 -> {
			JDialog dlgProgress;
			dlgProgress = new JDialog((java.awt.Frame)null, "Please wait.", true);//true means that the dialog created is modal
			JLabel lblStatus = new JLabel("Moving files.."); // this is just a label in which you can indicate the state of the processing
			dlgProgress.setIconImage(Toolkit.getDefaultToolkit().getImage(Main_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
			JProgressBar pbProgress = new JProgressBar(100, 100);
			pbProgress.setIndeterminate(true); //we'll use an indeterminate progress bar

			dlgProgress.getContentPane().add(BorderLayout.NORTH, lblStatus);
			dlgProgress.getContentPane().add(BorderLayout.CENTER, pbProgress);
			dlgProgress.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // prevent the user from closing the dialog
			dlgProgress.setSize(300, 90);
			dlgProgress.setLocationRelativeTo(frame);
			SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
				protected Void doInBackground() {
					fun.sftp.moveSAE("SEVL", fun.getLatestSAE());
					fun.sftp.moveSRE("SEVL", fun.getLatestSRE());
					fun.sftp.movePRAE("SEVL", fun.getLatestPRAE());
					fun.sftp.moveActiveDept("SEVL", fun.getLatestActiveDept());
					fun.sftp.moveSAE("TEST", fun.getLatestSAE());
					fun.sftp.moveSRE("TEST", fun.getLatestSRE());
					fun.sftp.movePRAE("TEST", fun.getLatestPRAE());
					fun.sftp.moveActiveDept("TEST", fun.getLatestActiveDept());

					return null;
				}

				protected void done() {
					dlgProgress.dispose();//close the modal dialog

				}
			};
			sw.execute(); // this will start the processing on a separate thread
			dlgProgress.setVisible(true); //this will block user input as long as the processing task is working

		});
		btnClose.addActionListener(arg0 -> {
			EventQueue.invokeLater(() -> {
				new public_package.Master_Menu(
						fun.jdbc.connection,//jdbc.connection,
						fun.firstName,//jdbc.userFirstName,
						fun.userName,
						fun.password,
						fun.sftp.getConnection(),
						fun.ssh.session,
						fun.environment);
			});
			frame.dispose();
		});
		btnEmp.addActionListener(e -> EventQueue.invokeLater(() -> {
			try {
				Employee_Menu window = new Employee_Menu(fun);
				window.frame.setVisible(true);
				//frame.dispose();
			} catch (Exception e16) {
				e16.printStackTrace();
			}
		}));
		btnReceipted_Paid.addActionListener(e -> EventQueue.invokeLater(() -> {
			try {
				PaidReceipted_Menu window = new PaidReceipted_Menu(fun);
				window.frame.setVisible(true);
				//frame.dispose();
			} catch (Exception e15) {
				e15.printStackTrace();
			}
		}));
		btnSAE.addActionListener(e -> EventQueue.invokeLater(() -> {
			try {
				SAE_Menu window = new SAE_Menu(fun);
				window.frame.setVisible(true);
				//frame.dispose();
			} catch (Exception e14) {
				e14.printStackTrace();
			}
		}));
		btnPRAE.addActionListener(e -> EventQueue.invokeLater(() -> {
			try {
				PRAE_Menu window = new PRAE_Menu(fun);
				window.frame.setVisible(true);
				//frame.dispose();
			} catch (Exception e17) {
				e17.printStackTrace();
			}
		}));
		btnSRE.addActionListener(e -> EventQueue.invokeLater(() -> {
			try {
				SRE_Menu window = new SRE_Menu(fun);
				window.frame.setVisible(true);
				//frame.dispose();
			} catch (Exception e14) {
				e14.printStackTrace();
			}
		}));
		btnVendorFeed.addActionListener(e -> EventQueue.invokeLater(() -> {
			try {
				Vendor_Menu window = new Vendor_Menu(fun);
				window.frame.setVisible(true);
				//frame.dispose();
			} catch (Exception e13) {
				e13.printStackTrace();
			}
		}));
		btnVendorTermFeed.addActionListener(e -> EventQueue.invokeLater(() -> {
			try {
				VendorTerm_Menu window = new VendorTerm_Menu(fun);
				window.frame.setVisible(true);
				//frame.dispose();
			} catch (Exception e12) {
				e12.printStackTrace();
			}
		}));
		button_exit.addActionListener(e -> {
			public_package.Exit_Confirmation window = new public_package.Exit_Confirmation(fun.firstName);
			window.frame.setVisible(true);
			//System.exit(0);
		});

		frame.setVisible(true);
	}
}
