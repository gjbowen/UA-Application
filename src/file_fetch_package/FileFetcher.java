package file_fetch_package;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.jcraft.jsch.Session;
import com.sshtools.sftp.SftpClient;
import public_package.SSH_Connection;

public class FileFetcher
{
	public JFrame frameMenu;
	private SFTP connection;
	private SSH_Connection ssh;
	private JTextField textField;
	private String mode;
	private String location=null;
	private JTextField textField_count;
	private int count;

	private JRadioButton rdbtnLatest;
	private JRadioButton rdbtnAll;
	private JRadioButton radio_specified; // Amount
	private JRadioButton rdbtnContains;
	private JRadioButton rdbtnEquals;
	private JRadioButton rdbtnStartsWith;
	private JRadioButton rdbtnEndsWith;// Mode

	public FileFetcher(SftpClient conn_sftp, Session con_ssh, String first, String user, String pass, String env){
		connection=new SFTP(conn_sftp,env,user,pass);

		ssh = new SSH(con_ssh,env,user,pass);
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

		rdbtnContains = new JRadioButton("Contains");
		panel.add(rdbtnContains);
		rdbtnContains.setSelected(true);
		mode = "CONTAINS";

		rdbtnEquals = new JRadioButton("Equals");
		panel.add(rdbtnEquals);


		rdbtnStartsWith = new JRadioButton("Starts With");
		panel.add(rdbtnStartsWith);

		rdbtnEndsWith = new JRadioButton("Ends With");
		panel.add(rdbtnEndsWith);

		/////////////////////////////////////////////////////////////////////////////////////////////////

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

		JButton btnShow = new JButton("Show");
		btnShow.setBounds(166, 45, 89, 23);
		frameMenu.getContentPane().add(btnShow);

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

		rdbtnLatest = new JRadioButton("Latest          ");
		panel_2.add(rdbtnLatest);

		rdbtnAll = new JRadioButton("All             ");
		rdbtnAll.setSelected(true);
		panel_2.add(rdbtnAll);

		radio_specified = new JRadioButton("#               ");
		panel_2.add(radio_specified);

		textField_count = new JTextField();
		panel_2.add(textField_count);
		textField_count.setColumns(10);
		textField_count.setEnabled(false);

		// MODE
		rdbtnEquals.addActionListener(e -> {
			rdbtnEquals.setSelected(true);
			rdbtnContains.setSelected(false);
			rdbtnStartsWith.setSelected(false);
			rdbtnEndsWith.setSelected(false);
			mode="EQUALS";

			lockRetrieve();
		});
		rdbtnContains.addActionListener(e -> {
			rdbtnEquals.setSelected(false);
			rdbtnContains.setSelected(true);
			rdbtnStartsWith.setSelected(false);
			rdbtnEndsWith.setSelected(false);
			mode="CONTAINS";
			unlockRetrieve();
		});
		rdbtnStartsWith.addActionListener(e -> {
			rdbtnEquals.setSelected(false);
			rdbtnContains.setSelected(false);
			rdbtnStartsWith.setSelected(true);
			rdbtnEndsWith.setSelected(false);
			mode="STARTSWITH";
			unlockRetrieve();
		});
		rdbtnEndsWith.addActionListener(e -> {
			rdbtnEquals.setSelected(false);
			rdbtnContains.setSelected(false);
			rdbtnStartsWith.setSelected(false);
			rdbtnEndsWith.setSelected(true);
			mode="ENDSWITH";
			unlockRetrieve();
		});

		// LOCATIONS
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

		///////////////////////////////////////
		// AMOUNT TO RETRIEVE
		//////////////////////////////////////
		rdbtnLatest.addActionListener(e -> {
			rdbtnLatest.setSelected(true);
			rdbtnAll.setSelected(false);
			radio_specified.setSelected(false);
			textField_count.setEnabled(false);
			count=1;
			textField_count.setEnabled(false);

		});
		rdbtnAll.addActionListener(e -> {
			rdbtnLatest.setSelected(false);
			rdbtnAll.setSelected(true);
			radio_specified.setSelected(false);
			textField_count.setEnabled(false);
			count=99999999;
			textField_count.setEnabled(false);
		});
		radio_specified.addActionListener(e -> {
			rdbtnLatest.setSelected(false);
			rdbtnAll.setSelected(false);
			radio_specified.setSelected(true);
			textField_count.setEnabled(true);
		});
		//

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Show Settings", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_3.setBounds(10, 50, 133, 100);
		frameMenu.getContentPane().add(panel_3);
		panel_3.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JCheckBox box_showInfo = new JCheckBox("Show Info");
		box_showInfo.setSelected(true);
		panel_3.add(box_showInfo);

		JCheckBox box_timeSort = new JCheckBox("Order by time");
		box_timeSort.setSelected(true);
		panel_3.add(box_timeSort);

		//GET
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
				protected Void doInBackground() {
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
		btnShow.addActionListener(e -> {
			if(radio_specified.isSelected()) {
				try{
					count=Integer.parseInt(textField_count.getText());
				}catch (NumberFormatException e1){
					return;//count = 9999999;
				}
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
				protected Void doInBackground() {
					//connection.getFiles(textField.getText().trim(), location, mode, count);
					String path=null;
					if(location.equals("EXPORT")||location.equals("IMPORT")||location.equals("ARCHIVE")){
						path = "/u03/"+location.toLowerCase()+"/"+connection.environment.toUpperCase()+"/";
					}
					else if(location.equals("GURJOBS")){
						path = "/u03/banjobs/bin/gurjobs/"+connection.environment.toUpperCase()+"/";
					}
					String file=null;
					if(mode.equals("STARTSWITH"))
						file=textField.getText().trim()+"*";
					else if(mode.equals("ENDSWITH"))
						file="*"+textField.getText().trim();
					else if(mode.equals("CONTAINS"))
						file="*"+textField.getText().trim()+"*";
					else if(mode.equals("EQUALS"))
						file=textField.getText().trim();

					String command = "ls ";
					if(box_showInfo.isSelected()||box_timeSort.isSelected()){
						command+="-";
						if(box_showInfo.isSelected())
							command+="l";
						if(box_timeSort.isSelected())
							command+="t";
						command+=" ";
					}

					command+=path+file;
					System.out.println(command);
					String message = ssh.run(command);

					JTextArea textArea = new JTextArea(message);
					JScrollPane scrollPane = new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					textArea.setLineWrap(false);
					textArea.setWrapStyleWord(true);
					if(box_showInfo.isSelected())
						scrollPane.setPreferredSize(new Dimension(1000, 700));
					else
						scrollPane.setPreferredSize(new Dimension(600, 700));

					textArea.setFont(new Font("monospaced", Font.BOLD, 14));
					JOptionPane.showMessageDialog(null, scrollPane, "Directory Contents", JOptionPane.PLAIN_MESSAGE);

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

	private void lockRetrieve(){
		rdbtnLatest.setEnabled(false);
		rdbtnAll.setEnabled(false);
		radio_specified.setEnabled(false);

		rdbtnLatest.setSelected(false);
		rdbtnAll.setSelected(false);
		radio_specified.setSelected(true);
		count=1;
		textField_count.setEnabled(false);
		textField_count.setText("1");
	}
	private void unlockRetrieve(){
		rdbtnLatest.setEnabled(true);
		if(radio_specified.isSelected())
			textField_count.setEnabled(true);
		rdbtnAll.setEnabled(true);
		radio_specified.setEnabled(true);
	}

}
