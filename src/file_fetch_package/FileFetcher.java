package file_fetch_package;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.sshtools.sftp.SftpClient;

public class FileFetcher
{
	public JFrame frameMenu;
	protected SFTP_Connection connection;
	private JTextField textField;
	private String mode;
	private String location;
	private JTextField textField_count;
	private int count;
	//sftp,firstName,userName,password,environment
	public FileFetcher(SftpClient conn_sftp,String first,String user,String pass,String env){
		connection=new SFTP_Connection(conn_sftp,user,pass,env);
		count=1;
		//connection = conn_sftp;
		initialize();
	}

	private void initialize()
	{
		frameMenu = new JFrame();
		frameMenu.setIconImage(Toolkit.getDefaultToolkit().getImage(FileFetcher.class.getResource("/Jar Files/ua_background_mobile.jpg")));
		frameMenu.setTitle("Get Files  -  "+connection.getInstance(connection.environment));
		frameMenu.setBounds(100, 100, 678, 290);
		frameMenu.setDefaultCloseOperation(3);
		frameMenu.getContentPane().setLayout(null);

		JButton btnNewButton = new JButton("Exit");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewButton.addActionListener(arg0 -> System.exit(0));
		btnNewButton.setBounds(10, 178, 101, 62);
		frameMenu.getContentPane().add(btnNewButton);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Mode", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel.setBounds(264, 11, 133, 137);
		frameMenu.getContentPane().add(panel);

		JRadioButton rdbtnContains = new JRadioButton("Contains");
		panel.add(rdbtnContains);
		rdbtnContains.setSelected(true);
		mode = "CONTAINS";

		JRadioButton rdbtnEquals = new JRadioButton("Equals");
		panel.add(rdbtnEquals);


		JRadioButton rdbtnStartsWith = new JRadioButton("Starts With");
		panel.add(rdbtnStartsWith);

		JRadioButton rdbtnEndsWith = new JRadioButton("Ends With");
		panel.add(rdbtnEndsWith);

		/////////////////////////////////////////////////////////////////////////////////////////////////

		rdbtnEquals.addActionListener(e -> {
			rdbtnEquals.setSelected(true);
			rdbtnContains.setSelected(false);
			rdbtnStartsWith.setSelected(false);
			rdbtnEndsWith.setSelected(false);
			mode="EQUALS";
		});
		rdbtnContains.addActionListener(e -> {
			rdbtnEquals.setSelected(false);
			rdbtnContains.setSelected(true);
			rdbtnStartsWith.setSelected(false);
			rdbtnEndsWith.setSelected(false);
			mode="CONTAINS";
		});
		rdbtnStartsWith.addActionListener(e -> {
			rdbtnEquals.setSelected(false);
			rdbtnContains.setSelected(false);
			rdbtnStartsWith.setSelected(true);
			rdbtnEndsWith.setSelected(false);
			mode="STARTSWITH";
		});
		rdbtnEndsWith.addActionListener(e -> {
			rdbtnEquals.setSelected(false);
			rdbtnContains.setSelected(false);
			rdbtnStartsWith.setSelected(false);
			rdbtnEndsWith.setSelected(true);
			mode="ENDSWITH";
		});
		//////////////////////////////////////////////////////////////////////////////////////////////////

		textField = new JTextField();
		textField.setBounds(10, 11, 146, 32);
		frameMenu.getContentPane().add(textField);
		textField.setColumns(10);

		JButton button = new JButton("Close");
		button.setFont(new Font("Tahoma", Font.PLAIN, 15));
		button.addActionListener(e -> {
			frameMenu.dispose();
			EventQueue.invokeLater(() -> {
				new public_package.Login(null);
			});
		});
		button.setBounds(551, 178, 101, 62);
		frameMenu.getContentPane().add(button);

		JButton btnGet = new JButton("Get");

		btnGet.setBounds(166, 16, 89, 23);
		frameMenu.getContentPane().add(btnGet);

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Location", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1.setBounds(407, 11, 101, 137);
		frameMenu.getContentPane().add(panel_1);

		JRadioButton rdbtnExport = new JRadioButton("Export");
		rdbtnExport.setSelected(true);
		panel_1.add(rdbtnExport);
		location="EXPORT";

		JRadioButton rdbtnImport = new JRadioButton("Import");
		panel_1.add(rdbtnImport);

		JRadioButton rdbtnArchive = new JRadioButton("Archive");
		panel_1.add(rdbtnArchive);

		JRadioButton rdbtnGurjobs = new JRadioButton("GURJOBS");
		panel_1.add(rdbtnGurjobs);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Retrieve", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_2.setBounds(518, 11, 133, 137);
		frameMenu.getContentPane().add(panel_2);
		panel_2.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JRadioButton rdbtnLatest = 		new JRadioButton("Latest          ");
		panel_2.add(rdbtnLatest);

		JRadioButton rdbtnAll = 		new JRadioButton("All             ");
		rdbtnAll.setSelected(true);
		panel_2.add(rdbtnAll);

		JRadioButton radio_specified = 	new JRadioButton("#               ");
		panel_2.add(radio_specified);

		textField_count = new JTextField();
		panel_2.add(textField_count);
		textField_count.setColumns(10);
		textField_count.setEnabled(false);
		


		rdbtnLatest.addActionListener(e -> {
			rdbtnLatest.setSelected(true);
			rdbtnAll.setSelected(false);
			radio_specified.setSelected(false);
			textField_count.setEnabled(false);
			count=1;
		});
		rdbtnAll.addActionListener(e -> {
			rdbtnLatest.setSelected(false);
			rdbtnAll.setSelected(true);
			radio_specified.setSelected(false);
			textField_count.setEnabled(false);
			count=99999999;
		});
		radio_specified.addActionListener(e -> {
			rdbtnLatest.setSelected(false);
			rdbtnAll.setSelected(false);
			radio_specified.setSelected(true);
			textField_count.setEnabled(true);
		});


		rdbtnExport.addActionListener(e -> {
			rdbtnExport.setSelected(true);
			rdbtnImport.setSelected(false);
			rdbtnArchive.setSelected(false);
			rdbtnGurjobs.setSelected(false);
			location="EXPORT";
		});
		rdbtnImport.addActionListener(e -> {
			rdbtnExport.setSelected(false);
			rdbtnImport.setSelected(true);
			rdbtnArchive.setSelected(false);
			rdbtnGurjobs.setSelected(false);
			location="IMPORT";
		});
		rdbtnArchive.addActionListener(e -> {
			rdbtnExport.setSelected(false);
			rdbtnImport.setSelected(false);
			rdbtnArchive.setSelected(true);
			rdbtnGurjobs.setSelected(false);
			location="ARCHIVE";
		});
		rdbtnGurjobs.addActionListener(e -> {
			rdbtnExport.setSelected(false);
			rdbtnImport.setSelected(false);
			rdbtnArchive.setSelected(false);
			rdbtnGurjobs.setSelected(true);
			location="GURJOBS";
		});

		btnGet.addActionListener(e -> {
			if(radio_specified.isSelected()) {
				count=Integer.parseInt(textField_count.getText());
			}


			JDialog dlgProgress;
			dlgProgress = new JDialog((java.awt.Frame)null, "Please wait.", true);//true means that the dialog created is modal
			JLabel lblStatus = new JLabel("Searching for files.."); // this is just a label in which you can indicate the state of the processing
			dlgProgress.setIconImage(Toolkit.getDefaultToolkit().getImage(FileFetcher.class.getResource("/Jar Files/ua_background_mobile.jpg")));
			JProgressBar pbProgress = new JProgressBar(100, 100);
			pbProgress.setIndeterminate(true); //we'll use an indeterminate progress bar

			dlgProgress.getContentPane().add(BorderLayout.NORTH, lblStatus);
			dlgProgress.getContentPane().add(BorderLayout.CENTER, pbProgress);
			dlgProgress.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // prevent the user from closing the dialog
			dlgProgress.setSize(300, 90);
			dlgProgress.setLocationRelativeTo(frameMenu);
			SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
				protected Void doInBackground() throws Exception {
					connection.getFiles(textField.getText().trim(), location, mode, count);
					return null;
				}
				protected void done() {
					dlgProgress.dispose();//close the modal dialog
				}
			};
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(e1 -> sw.cancel(true));
			dlgProgress.getContentPane().add(BorderLayout.EAST, cancelButton);
			sw.execute(); // this will start the processing on a separate thread
			dlgProgress.setVisible(true); //this will block user input as long as the processing task is working
		});

		frameMenu.setVisible(true);
	}
}
