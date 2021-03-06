package concur_package;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class PaidReceipted_Menu {
    JFrame frame;
    private JTextField textField;
    private final Function_Library connection;

    public PaidReceipted_Menu(Function_Library conn) {
        connection = conn;
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(PaidReceipted_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(3);
        frame.getContentPane().setLayout(null);
        frame.setTitle("Paid/Receipted Menu");
        JButton button_Exit = new JButton("Exit");
        button_Exit.setFont(new Font("Dialog", 0, 15));
        button_Exit.setBounds(10, 193, 122, 57);
        frame.getContentPane().add(button_Exit);
        button_Exit.addActionListener(e2 -> {
            public_package.Exit_Confirmation window = new public_package.Exit_Confirmation(connection.firstName);
            window.frame.setVisible(true);
            //System.exit(0);
        });
        JButton button_Close = new JButton("Close");
        button_Close.setFont(new Font("Dialog", 0, 15));
        button_Close.setBounds(302, 193, 122, 57);
        frame.getContentPane().add(button_Close);
        button_Close.addActionListener(e2 -> frame.dispose());
        JLabel lblCheckDate = new JLabel("Key");
        lblCheckDate.setHorizontalAlignment(2);
        lblCheckDate.setFont(new Font("Tahoma", 1, 11));
        lblCheckDate.setBounds(10, 77, 296, 28);
        frame.getContentPane().add(lblCheckDate);
        textField = new JTextField();
        textField.setFont(new Font("Tahoma", 0, 16));
        textField.setBounds(10, 109, 205, 38);
        frame.getContentPane().add(textField);
        textField.setColumns(10);
        JButton btnReceipted = new JButton("Receipted");
        btnReceipted.setBounds(317, 81, 107, 20);
        frame.getContentPane().add(btnReceipted);
        btnReceipted.addActionListener(e2 -> {
            String message;
            message = connection.searchPaymentReceipt(textField.getText().trim());
            JTextArea textArea = new JTextArea(message);
            JScrollPane scrollPane = new JScrollPane(textArea);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            scrollPane.setPreferredSize(new Dimension(800, 500));
            JOptionPane.showMessageDialog(null, scrollPane, "Receipted Search", -1);
        });
        JButton btnPaid = new JButton("Paid/Request");
        btnPaid.setBounds(307, 120, 127, 20);
        frame.getContentPane().add(btnPaid);
        btnPaid.addActionListener(e2 -> {
            String message;
            message = connection.searchPaymentRequest(textField.getText().trim());
            JTextArea textArea = new JTextArea(message);
            JScrollPane scrollPane = new JScrollPane(textArea);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            scrollPane.setPreferredSize(new Dimension(600, 500));
            JOptionPane.showMessageDialog(null, scrollPane, "Payment Request Search", -1);
        });
    }

}

