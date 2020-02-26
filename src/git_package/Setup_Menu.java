package git_package;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.Color;

class Setup_Menu
{
	private JFrame frame;
	private final Function_Library f;
	private JTextField textField;
	private JLabel lblVerified;
	private JButton buttonSubmit;
	private JButton btnValidate;
	private JLabel lblMode;
	private JButton btnExplore;
	public Setup_Menu(Function_Library obj ){
		f = obj;
		initialize();
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Main_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
		frame.setTitle("Setup Menu - Welcome, " + f.userName);

		frame.setBounds(100, 100, 445, 336);
		frame.setDefaultCloseOperation(3);
		frame.getContentPane().setLayout(null);


		JButton button_close = new JButton("Close");
		button_close.setFont(new Font("Lucida Grande", 0, 15));
		button_close.setBounds(128, 221, 122, 57);
		frame.getContentPane().add(button_close);

		JButton button_exit = new JButton("Exit");
		button_exit.setFont(new Font("Lucida Grande", 0, 15));
		button_exit.setBounds(6, 221, 122, 57);
		frame.getContentPane().add(button_exit);

		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setBounds(6, 6, 148, 71);
		frame.getContentPane().add(panel);
		panel.setLayout(new GridLayout(2, 2, 0, 0));

		JRadioButton rbNew = new JRadioButton("Clone and create");
		panel.add(rbNew);
		rbNew.setSelected(true);

		JRadioButton rbExists = new JRadioButton("GIT Folder Exists");
		panel.add(rbExists);

		textField = new JTextField();
		textField.setBounds(10, 126, 231, 31);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		textField.setText(f.getUserHome()+"/Desktop");

		lblMode = new JLabel("Enter location to clone GIT folder");
		lblMode.setBounds(10, 109, 231, 14);
		frame.getContentPane().add(lblMode);

		rbExists.addActionListener(e -> {
			lblMode.setText("Enter current GIT folder containing .git");
			rbNew.setSelected(false);
			rbExists.setSelected(true);
			reset();
		});

		rbNew.addActionListener(e -> {
			lblMode.setText("Enter location to clone GIT folder");
			rbExists.setSelected(false);
			rbNew.setSelected(true);
			reset();
		});

		btnValidate = new JButton("Validate");
		btnValidate.setBounds(251, 126, 148, 31);
		frame.getContentPane().add(btnValidate);

		lblVerified = new JLabel("Verified");

		lblVerified.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblVerified.setBounds(6, 168, 235, 31);
		frame.getContentPane().add(lblVerified);

		buttonSubmit = new JButton("Submit");

		buttonSubmit.setBounds(251, 170, 148, 31);
		frame.getContentPane().add(buttonSubmit);



		JButton btnViewRepos = new JButton("View All Repos Available");
		btnViewRepos.addActionListener(arg0 -> f.openLink("http://fisheye01.ua.edu:7990/stash/projects/"));
		btnViewRepos.setBounds(251, 28, 189, 23);
		//frame.getContentPane().add(btnViewRepos); //hold off on supporting multiple repos

		btnExplore = new JButton("Browse");
		btnExplore.addActionListener(e -> {
			reset();
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle("choosertitle");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);

			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			  textField.setText(chooser.getSelectedFile().toString());
			}
		});
		
		
		
		btnExplore.setBounds(251, 75, 89, 23);
		frame.getContentPane().add(btnExplore);
		lblVerified.setVisible(false);
		buttonSubmit.setVisible(false);
		button_exit.addActionListener(e -> System.exit(0));
		button_close.addActionListener(e -> frame.dispose() );

		btnValidate.addActionListener(e -> {
			if(rbExists.isSelected()){
				if(btnValidate.getText().equals("Change"))
					reset(); //reset it out
				else if(f.GITfolderExists(textField.getText().trim()))
					valid();
				else
					invalid("Invalid GIT Location");
			}
			else if(rbNew.isSelected()){
				if(btnValidate.getText().equals("Change"))
					reset(); //reset it out
				else if(f.GITfolderExists(textField.getText().trim()+"/uabanner"))
					invalid("GIT folder already exists!");
				else if(f.folderExists(textField.getText().trim()))
					valid();
				else invalid("Invalid GIT Location");
			}
		});

		buttonSubmit.addActionListener(arg0 -> {
			if(rbExists.isSelected()) {
				//create preference file with the location
				f.gitFolder = textField.getText();
				f.print(f.gitFolder);
				done();
			}
			else if(rbNew.isSelected()){
				//clone and create preference file
				f.gitFolder = textField.getText()+"/uabanner";
				f.cloneGIT(textField.getText());
				done();
			}
		});

		frame.setVisible(true);		

	}
	private void done() {
		public_package.Preferences.addPreference("git",f.gitFolder);

		frame.setVisible(false);



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
}
