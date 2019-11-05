package public_package;

import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JCheckBox;

public class Exit_Confirmation
{
	public JFrame frameExitConfirmation;
	public Exit_Confirmation(String name){

		if(Preferences.contents.containsKey("promptOnExit") && Preferences.contents.get("promptOnExit").equals("false"))
			System.exit(0);

		initialize(name);
	}

	private void initialize(String name)
	{
		frameExitConfirmation = new JFrame();
		frameExitConfirmation.setIconImage(Toolkit.getDefaultToolkit().getImage(Exit_Confirmation.class.getResource("/Jar Files/ua_background_mobile.jpg")));
		frameExitConfirmation.setTitle("Confirmation");
		frameExitConfirmation.setBounds(100, 100, 379, 240);
		frameExitConfirmation.setDefaultCloseOperation(3);
		frameExitConfirmation.getContentPane().setLayout(null);

		JButton button_yes = new JButton("Yes");
		button_yes.setFont(new Font("Lucida Grande", 0, 15));
		button_yes.setBounds(10, 133, 122, 57);
		frameExitConfirmation.getContentPane().add(button_yes);

		JButton button_no = new JButton("No");
		button_no.addActionListener(arg0 -> frameExitConfirmation.dispose());
		button_no.setFont(new Font("Dialog", Font.PLAIN, 15));
		button_no.setBounds(231, 133, 122, 57);
		frameExitConfirmation.getContentPane().add(button_no);
		String message;
		if(name==null || name.equals("")) 
			message = "Are you sure you want to quit?";
		else
			message = name+", are you sure you want to quit?";
		JLabel lblAreYouSure = new JLabel(message);
		lblAreYouSure.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblAreYouSure.setBounds(10, 11, 343, 57);
		frameExitConfirmation.getContentPane().add(lblAreYouSure);

		JCheckBox chckbxRememberChoice = new JCheckBox("Remember choice");
		chckbxRememberChoice.setBounds(10, 94, 149, 23);
		frameExitConfirmation.getContentPane().add(chckbxRememberChoice);
		button_yes.addActionListener(e -> {
			if(chckbxRememberChoice.isSelected()) {
				Preferences.addPreference("promptOnExit", "false");
			}
			System.exit(0);
		});
	}
}
