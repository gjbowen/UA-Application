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

class emp710_Menu {
    private final Function_Library connection;
    JFrame frame;
    private JTextField textField_orgn;
    private JTextField textField_fund;
    private JTextField textField_coas;
    private JTextField textField_cwid;
    private JTextField textField_level;

    public emp710_Menu(Function_Library conn) {
        connection = conn;
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(emp710_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
        frame.setTitle("710 Menu");
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
        JLabel lblReportKey = new JLabel("ORGN");
        lblReportKey.setFont(new Font("Tahoma", 0, 18));
        lblReportKey.setBounds(9, 98, 59, 35);
        frame.getContentPane().add(lblReportKey);
        textField_orgn = new JTextField();
        textField_orgn.setBounds(78, 103, 167, 30);
        frame.getContentPane().add(textField_orgn);
        textField_orgn.setColumns(10);
        textField_orgn.setText("");
        JButton btnReportKey = new JButton("Submit");
        btnReportKey.setFont(new Font("Tahoma", 0, 15));
        btnReportKey.setBounds(273, 129, 98, 57);
        frame.getContentPane().add(btnReportKey);
        
        JLabel lblFund = new JLabel("FUND");
        lblFund.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblFund.setBounds(10, 57, 68, 35);
        frame.getContentPane().add(lblFund);
        
        textField_fund = new JTextField();
        textField_fund.setText("");
        textField_fund.setColumns(10);
        textField_fund.setBounds(78, 62, 167, 30);
        frame.getContentPane().add(textField_fund);
        
        JLabel lblCoas = new JLabel("COAS");
        lblCoas.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblCoas.setBounds(10, 16, 68, 35);
        frame.getContentPane().add(lblCoas);
        
        textField_coas = new JTextField();
        textField_coas.setText("");
        textField_coas.setColumns(10);
        textField_coas.setBounds(78, 21, 167, 30);
        frame.getContentPane().add(textField_coas);

        textField_cwid = new JTextField();
        textField_cwid.setText("");
        textField_cwid.setColumns(10);
        textField_cwid.setBounds(78, 144, 167, 30);
        frame.getContentPane().add(textField_cwid);

        textField_level = new JTextField();
        textField_level.setText("");
        textField_level.setColumns(10);
        textField_level.setBounds(78, 185, 167, 30);
        frame.getContentPane().add(textField_level);

        JLabel lblCwid = new JLabel("CWID");
        lblCwid.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblCwid.setBounds(10, 139, 59, 35);
        frame.getContentPane().add(lblCwid);

        JLabel lblLevel = new JLabel("Level");
        lblLevel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblLevel.setBounds(10, 180, 59, 35);
        frame.getContentPane().add(lblLevel);

        btnReportKey.addActionListener(e2 -> {
            if (textField_coas.getText().trim().equals("")&&textField_fund.getText().trim().equals("")&&textField_orgn.getText().trim().equals("")&&
                    textField_cwid.getText().trim().equals("")&&textField_level.getText().trim().equals("")) {
                status.setText("Nothing have been given.");
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
                dlgProgress.setSize(300, 100);
                dlgProgress.setLocationRelativeTo(frame);
                SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
                    protected Void doInBackground() {
                        status.setText("Status: Finding Vendor..");
                        String message = connection.search710(textField_coas.getText().trim(),textField_fund.getText().trim(),textField_orgn.getText().trim(),textField_cwid.getText().trim(),textField_level.getText().trim());
                        JTextArea textArea = new JTextArea(message);
                        JScrollPane scrollPane = new JScrollPane(textArea);
                        textArea.setLineWrap(true);
                        textArea.setWrapStyleWord(true);
                        scrollPane.setPreferredSize(new Dimension(1000, 600));
                        JOptionPane.showMessageDialog(null, scrollPane, "710 search.. " , -1);
                        status.setText("Ready");
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

