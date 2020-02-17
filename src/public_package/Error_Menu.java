package public_package;

import javax.swing.*;
import java.awt.*;

class Error_Menu
{
	private Error_Menu(String name, String error){
		JFrame frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Error_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
		frame.setTitle("Confirmation");
		frame.setBounds(100, 100, 400, 400);

		JPanel panel = new JPanel();
//NEED TO FIX
		JTextArea textArea = new JTextArea("hello\r\n WORK");
		textArea.setFont(new Font("monospaced", Font.BOLD, 16));

		JScrollPane scroll_pane = new JScrollPane();
		scroll_pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); //SETTING SCHEME FOR HORIZONTAL BAR
		scroll_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		panel.add(scroll_pane);
		frame.getContentPane().add(textArea);


		//Add Yes button
		JButton button_yes = new JButton("Yes");
		button_yes.setFont(new Font("Lucida Grande", 0, 15));
		button_yes.setBounds(10, 300, 122, 57);
		frame.getContentPane().add(button_yes);
		button_yes.addActionListener(arg0 -> System.exit(1));

		//Add No button
		JButton button_no = new JButton("No");
		button_no.addActionListener(arg0 -> frame.dispose());
		button_no.setFont(new Font("Dialog", Font.PLAIN, 15));
		button_no.setBounds(231, 300, 122, 57);
		frame.getContentPane().add(button_no);

		String message = "Error exception. Kill program?<br>\r\nHello";

		JLabel lblAreYouSure = new JLabel(message);
		lblAreYouSure.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblAreYouSure.setBounds(10, 11, 350, 57);
		frame.getContentPane().add(lblAreYouSure);

		frame.setVisible(true);
		frame.setAlwaysOnTop(true);

	}
}
