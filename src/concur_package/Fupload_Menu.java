package concur_package;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class Fupload_Menu {
	JFrame frame;
    private Function_Library connection;
	private String location="LOCAL";

	public Fupload_Menu(Function_Library fun) {
		connection = fun;

		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Fupload_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
		frame.setTitle("Fupload Validation - "+connection.environment);
		frame.setBounds(100, 100, 461, 412);
		frame.setDefaultCloseOperation(3);
		frame.getContentPane().setLayout(null);
		// Submit button
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setFont(new Font("Dialog", 0, 15));
		btnSubmit.setBounds(10, 205, 122, 57);
		frame.getContentPane().add(btnSubmit);
		// Exit button
		JButton btnExit = new JButton("Exit");
		btnExit.setFont(new Font("Dialog", 0, 15));
		btnExit.setBounds(10, 305, 122, 57);
		frame.getContentPane().add(btnExit);
		// Close button
		JButton btnClose = new JButton("Close");
		btnClose.setFont(new Font("Dialog", 0, 15));
		btnClose.setBounds(313, 305, 122, 57);
		frame.getContentPane().add(btnClose);
		// Text Field for file name
		TextField textField = new TextField(System.getProperty("user.home")+"\\Downloads\\concur_sae_pCard_EXAMPLE.processed");
		frame.getContentPane().add(textField);
		textField.setBounds(10, 75, 300, 23);

		JButton btnExplore = new JButton("Browse local..");
		btnExplore.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File(System.getProperty("user.home")));
			chooser.setDialogTitle("Select file for validation");
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			//chooser.setAcceptAllFileFilterUsed(false);

			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				textField.setText(chooser.getSelectedFile().toString());
			}
		});
		btnExplore.setBounds(220, 100, 90, 23);
		frame.getContentPane().add(btnExplore);

		// location assets
		JPanel location_panel = new JPanel();
		location_panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		location_panel.setBounds(329, 11, 108, 125);
		FlowLayout flowLayout_1 = (FlowLayout)location_panel.getLayout();
		flowLayout_1.setAlignment(0);
		frame.getContentPane().add(location_panel);
		// Label
		final JLabel location_label= new JLabel("FILE LOCATION");
		location_label.setFont(new Font("Dialog", Font.BOLD,12 ));
		location_panel.add(location_label);
		// Options
		final JRadioButton rdbtnLocal = new JRadioButton("Local");
		location_panel.add(rdbtnLocal);
		rdbtnLocal.setSelected(true); //default
		final JRadioButton rdbtnImport = new JRadioButton("IMPORT");
		location_panel.add(rdbtnImport);
		final JRadioButton rdbtnExport = new JRadioButton("EXPORT");
		location_panel.add(rdbtnExport);

		rdbtnLocal.addActionListener(e2 -> {
			location="LOCAL";
			textField.setText(System.getProperty("user.home"));
			rdbtnImport.setSelected(false);
			rdbtnExport.setSelected(false);
			rdbtnLocal.setSelected(true);
			btnExplore.setEnabled(true);
		});
		rdbtnImport.addActionListener(e2 -> {
			location="IMPORT";
			textField.setText("/u03/import/"+connection.environment+"/");
			rdbtnLocal.setSelected(false);
			rdbtnExport.setSelected(false);
			rdbtnImport.setSelected(true);
			btnExplore.setEnabled(false);
		});
		rdbtnExport.addActionListener(e2 -> {
			location="EXPORT";
			textField.setText("/u03/export/"+connection.environment+"/concur_sae_DashCard.txt");
			rdbtnLocal.setSelected(false);
			rdbtnImport.setSelected(false);
			rdbtnExport.setSelected(true);
			btnExplore.setEnabled(false);
		});

		btnSubmit.addActionListener(e2 ->{
			String input = textField.getText().trim();
			String fileName;
			if(location.equals("LOCAL")) {
				input=input.replace("\\",",");
				String[] inputList = input.split(",");
				fileName = inputList[inputList.length - 1];
				try {
					Path temp = Files.copy(
							Paths.get(textField.getText().trim()),
							Paths.get(System.getProperty("user.home")+"\\Concur_Files\\misc\\"+fileName)
					);
				} catch (IOException e) {
					System.err.println("Failed to move file - "+textField.getText().trim());
				}
			}
			else{// in EXPORT or IMPORT folder
				String[] inputList = input.split("/");
				fileName = inputList[inputList.length - 1];
				String path = input.replace(fileName, "");

				connection.sftp.downloadFile(fileName, path);
			}

			connection.validateFupload(fileName);
		});


		btnExit.addActionListener(e2 -> System.exit(0));
		btnClose.addActionListener(e2 -> frame.dispose());
	}
}

