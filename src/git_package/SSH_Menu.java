package git_package;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.EventQueue;

public class SSH_Menu
{
	JFrame frameMainMenu;
	Function_Library f;
	JTextField textField;
	JLabel lblVerified;
	JButton buttonSubmit;
	JButton btnValidate;
	JLabel lblMode;
	SSH_Menu(Function_Library obj ){
		f = obj;
		initialize();
	}

	private void initialize()
	{
		frameMainMenu = new JFrame();
		frameMainMenu.setIconImage(Toolkit.getDefaultToolkit().getImage(Main_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
		frameMainMenu.setTitle("SSH Menu - Welcome, "+f.getUserName());

		frameMainMenu.setBounds(100, 100, 445, 336);
		frameMainMenu.setDefaultCloseOperation(3);
		frameMainMenu.getContentPane().setLayout(null);

		JButton button_exit = new JButton("Exit");
		button_exit.setFont(new Font("Lucida Grande", 0, 15));
		button_exit.setBounds(6, 221, 122, 57);
		frameMainMenu.getContentPane().add(button_exit);

		JButton button_close = new JButton("Close");
		button_close.setFont(new Font("Lucida Grande", 0, 15));
		button_close.setBounds(154, 221, 122, 57);
		frameMainMenu.getContentPane().add(button_close);

		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setBounds(6, 6, 148, 71);
		frameMainMenu.getContentPane().add(panel);
		panel.setLayout(new GridLayout(2, 2, 0, 0));

		JRadioButton rbNew = new JRadioButton("Generate New Keys");
		panel.add(rbNew);
		rbNew.setSelected(true);

		JRadioButton rbExists = new JRadioButton("SSH Keys Exist");
		panel.add(rbExists);
		rbExists.setSelected(false);

		textField = new JTextField();
		textField.setBounds(10, 126, 231, 31);
		frameMainMenu.getContentPane().add(textField);
		textField.setColumns(10);
		textField.setText(f.userName+"@ua.edu");

		rbExists.addActionListener(e -> {
			lblMode.setText("Enter current key location");
			rbNew.setSelected(false);
			rbExists.setSelected(true);
			textField.setEnabled(true);
			textField.setText("");
			btnValidate.setText("Validate");
			reset();
		});

		rbNew.addActionListener(e -> {
			lblMode.setText("Enter your email address");
			textField.setText(f.getUserName()+"@ua.edu");
			rbExists.setSelected(false);
			rbNew.setSelected(true);
			btnValidate.setText("Validate");
			textField.setEnabled(true);
			reset();
		});

		btnValidate = new JButton("Validate");
		btnValidate.setBounds(251, 126, 148, 31);
		frameMainMenu.getContentPane().add(btnValidate);

		lblVerified = new JLabel("Verified");

		lblVerified.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblVerified.setBounds(6, 168, 235, 31);
		frameMainMenu.getContentPane().add(lblVerified);

		buttonSubmit = new JButton("Submit");

		buttonSubmit.setBounds(251, 170, 148, 31);
		frameMainMenu.getContentPane().add(buttonSubmit);

		lblMode = new JLabel("Enter email address");
		lblMode.setBounds(10, 109, 231, 14);
		frameMainMenu.getContentPane().add(lblMode);
		lblVerified.setVisible(false);
		buttonSubmit.setVisible(false);
		button_exit.addActionListener(e -> System.exit(0));
		button_close.addActionListener(e -> frameMainMenu.dispose() );

		btnValidate.addActionListener(e -> {
			if(btnValidate.getText().equals("Change"))
				reset(); //reset it out
			else if(rbExists.isSelected()){
				if(f.folderExists(textField.getText().trim()))
					valid();
				else
					invalid("INVALID LOCATION");
			}
			else if(rbNew.isSelected()){
				//force the new keys to go to the users home
				if(validEmail(textField.getText().trim()))
					valid();
				else
					invalid("INVALID EMAIL ADDRESS");
			}
		});

		buttonSubmit.addActionListener(arg0 -> {
			if(rbExists.isSelected()) {
				//copy to new location?
				f.copyFolder(textField.getText().trim(), f.getUserHome()+"\\.ssh");
				if(f.folderExists("H:\\"))
					f.copyFolder(textField.getText().trim(), "H:\\.ssh");
				frameMainMenu.setVisible(false);
			}
			else if(rbNew.isSelected()){
				f.generateKeys(textField.getText().trim());
				if(f.folderExists(f.getUserHome()+"\\.ssh")) {
					int response;
					String message;
					if(f.folderExists("H:/.ssh"))
						message="SSH key files have been added to: \n"
								+ "    "+f.getUserHome()+"\\.ssh \n"
								+ "    H:\\.ssh \n\n";
					else
						message="SSH key files have been added to: \n"
								+ "    "+f.getUserHome()+"\\.ssh \n";
					message = message
							+"Key has been copied to clipboard to paste on the webpage. \n"
							+ "The new key MUST be added to your Bitbucket account. "
							+ "Would you like to add it now? \n\n";
					response=f.okCancel(message);
					if(response==0) {
						StringSelection selection = new StringSelection(f.getSSH_RSA());
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						clipboard.setContents(selection, selection);
						f.addSSHPage();
					}
				}


			}
			frameMainMenu.setVisible(false);
			done();
		});

		frameMainMenu.setVisible(true);		
		//valid();
	}

	private void done() {
		frameMainMenu.dispose();
		EventQueue.invokeLater(() -> {
			try {
				new Setup_Menu(f);
			}
			catch (Exception e2) {
				e2.printStackTrace();
			}
		});
	}
	private void reset() {
		lblVerified.setVisible(false);
		btnValidate.setText("Validate");
		buttonSubmit.setVisible(false);
		textField.setEnabled(true);
	}
	private void valid() {
		btnValidate.setText("Change");
		lblVerified.setText("Valid");
		textField.setEnabled(false);
		lblVerified.setVisible(true);
		lblVerified.setForeground(new Color(34, 139, 34));
		buttonSubmit.setVisible(true);
	}
	private void invalid(String message) {
		lblVerified.setText(message);
		lblVerified.setVisible(true);
		lblVerified.setForeground(Color.RED);
		buttonSubmit.setVisible(false);
	}
	private boolean validEmail(String email) {
		email=email.toUpperCase();
        return email.contains("@") && email.endsWith("UA.EDU");
    }
}
