package concur_package;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

class Vendor_Menu {
    private final Function_Library connection;
    JFrame frame;
    private JTextField textField;

    public Vendor_Menu(Function_Library conn) {
        connection = conn;
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Vendor_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
        frame.setTitle("Vendor Menu");
        frame.setBounds(100, 100, 461, 355);
        frame.setDefaultCloseOperation(3);
        frame.getContentPane().setLayout(null);
        JButton btnExit = new JButton("Exit");
        btnExit.setFont(new Font("Dialog", 0, 15));
        btnExit.setBounds(10, 248, 122, 57);
        frame.getContentPane().add(btnExit);
        JButton btnClose = new JButton("Close");
        btnClose.setFont(new Font("Dialog", 0, 15));
        btnClose.setBounds(313, 248, 122, 57);
        frame.getContentPane().add(btnClose);
        final JLabel status = new JLabel("Status: Ready for tasking");
        status.setFont(new Font("Tahoma", 0, 18));
        status.setBounds(10, 204, 271, 35);
        frame.getContentPane().add(status);
        JLabel lblReportKey = new JLabel("Vendor ID");
        lblReportKey.setFont(new Font("Tahoma", 0, 18));
        lblReportKey.setBounds(10, 37, 134, 35);
        frame.getContentPane().add(lblReportKey);
        textField = new JTextField();
        textField.setBounds(136, 42, 167, 30);
        frame.getContentPane().add(textField);
        textField.setColumns(10);
        textField.setText("");
        JButton btnReportKey = new JButton("Submit");
        btnReportKey.setFont(new Font("Tahoma", 0, 15));
        btnReportKey.setBounds(313, 42, 105, 30);
        frame.getContentPane().add(btnReportKey);
        btnReportKey.addActionListener(e2 -> {
            if (textField.getText().equals("")) {
                status.setText("Status: Vendor not given.");
            } else {
                JDialog dlgProgress;
                dlgProgress = new JDialog((java.awt.Frame)null, "Please wait.", true);//true means that the dialog created is modal
                JLabel lblStatus = new JLabel("Searching files.."); // this is just a label in which you can indicate the state of the processing
                dlgProgress.setIconImage(Toolkit.getDefaultToolkit().getImage(SAE_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
                JProgressBar pbProgress = new JProgressBar(100, 100);
                pbProgress.setIndeterminate(true); //we'll use an indeterminate progress bar

                dlgProgress.getContentPane().add(BorderLayout.NORTH, lblStatus);
                dlgProgress.getContentPane().add(BorderLayout.CENTER, pbProgress);
                dlgProgress.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // prevent the user from closing the dialog
                dlgProgress.setSize(300, 90);
                dlgProgress.setLocationRelativeTo(frame);
                SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
                    protected Void doInBackground() {
                        status.setText("Status: Finding Vendor..");
                        System.out.println("SEARCHING..");
                        String message = connection.searchVendor(textField.getText().trim());
                        System.out.println("DONE..");
                        status.setText("Displaying Vendor Info..");
                        if(message.startsWith("Vendor not found - ")) {
                            JOptionPane.showMessageDialog(null, message);
                        }else {
                            JTextArea textArea = new JTextArea(message);
                            JScrollPane scrollPane = new JScrollPane(textArea);
                            textArea.setLineWrap(true);
                            textArea.setWrapStyleWord(true);			                    scrollPane.setPreferredSize(new Dimension(1000, 800));// x WIDTH x HEIGHT
                            JOptionPane.showMessageDialog(null, scrollPane, "Vendor " + textField.getText().trim(), -1);
                        }

                        status.setText("");
                        return null;
                    }
                    protected void done() {
                        dlgProgress.dispose();//close the modal dialog
                    }
                };
                sw.execute(); // this will start the processing on a separate thread
                dlgProgress.setVisible(true); //this will block user input as long as the processing task is working
            }
        });
        btnExit.addActionListener(e2 -> System.exit(0));
        btnClose.addActionListener(e2 -> frame.dispose());
    }
}

