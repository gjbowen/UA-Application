package public_package;

import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JCheckBox;

public class Exit_Confirmation
{
	public JFrame frame;
	public Exit_Confirmation(String name){

		if(Preferences.contents.containsKey("promptOnExit") && Preferences.contents.get("promptOnExit").equals("false"))
			System.exit(0);

		initialize(name);
	}

	private void initialize(String name)
	{
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Exit_Confirmation.class.getResource("/Jar Files/ua_background_mobile.jpg")));
		frame.setTitle("Confirmation");
		frame.setBounds(100, 100, 379, 240);
		frame.setDefaultCloseOperation(3);
		frame.getContentPane().setLayout(null);

		JButton button_yes = new JButton("Yes");
		button_yes.setFont(new Font("Lucida Grande", 0, 15));
		button_yes.setBounds(10, 133, 122, 57);
		frame.getContentPane().add(button_yes);

		JButton button_no = new JButton("No");
		button_no.addActionListener(arg0 -> frame.dispose());
		button_no.setFont(new Font("Dialog", Font.PLAIN, 15));
		button_no.setBounds(231, 133, 122, 57);
		frame.getContentPane().add(button_no);
		String message;
		if(name==null || name.equals("")) 
			message = "Are you sure you want to quit?";
		else
			message = name+", are you sure you want to quit?";
		JLabel lblAreYouSure = new JLabel(message);
		lblAreYouSure.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblAreYouSure.setBounds(10, 11, 343, 57);
		frame.getContentPane().add(lblAreYouSure);

		JCheckBox chckbxRememberChoice = new JCheckBox("Remember choice");
		chckbxRememberChoice.setBounds(10, 94, 149, 23);
		frame.getContentPane().add(chckbxRememberChoice);
		button_yes.addActionListener(e -> {
			if(chckbxRememberChoice.isSelected()) {
				Preferences.addPreference("promptOnExit", "false");
			}
			System.exit(0);
		});
	}
}
