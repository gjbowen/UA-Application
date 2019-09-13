package concur_package;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class PRAE_Menu {
    protected Function_Library connection;
    protected JFrame praeFrame;
    private JTextField searchString;
    private JTextField searchColumn;
    private String mode;
    private JTextField textField;

    public PRAE_Menu(Function_Library conn) {
        this.connection = conn;
        this.initialize();
    }

    private void setMode(String str) {
        this.mode = str;
    }

    private void initialize() {
        this.praeFrame = new JFrame();
        this.praeFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(PRAE_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
        this.praeFrame.setTitle("PRAE Menu");
        this.praeFrame.setBounds(100, 100, 583, 448);
        this.praeFrame.setDefaultCloseOperation(3);
        this.praeFrame.getContentPane().setLayout(null);
        JButton btnExit = new JButton("Exit");
        btnExit.setBounds(10, 341, 122, 57);
        btnExit.setFont(new Font("Dialog", 0, 15));
        this.praeFrame.getContentPane().add(btnExit);
        JButton btnClose = new JButton("Close");
        btnClose.setBounds(435, 341, 122, 57);
        btnClose.setFont(new Font("Dialog", 0, 15));
        this.praeFrame.getContentPane().add(btnClose);
        final JLabel status = new JLabel("Status: Ready for tasking");
        status.setBounds(142, 363, 281, 35);
        status.setFont(new Font("Tahoma", 0, 18));
        this.praeFrame.getContentPane().add(status);
        JPanel panel = new JPanel();
        panel.setBounds(10, 11, 305, 63);
        FlowLayout flowLayout = (FlowLayout)panel.getLayout();
        flowLayout.setAlignment(0);
        this.praeFrame.getContentPane().add(panel);
        JLabel lblReportKey = new JLabel("String to Search ");
        panel.add(lblReportKey);
        lblReportKey.setFont(new Font("Tahoma", 0, 18));
        this.searchString = new JTextField();
        panel.add(this.searchString);
        this.searchString.setColumns(10);
        this.searchString.setText("");
        JLabel Column_label = new JLabel("Column to Search");
        Column_label.setFont(new Font("Tahoma", 0, 18));
        panel.add(Column_label);
        this.searchColumn = new JTextField();
        this.searchColumn.setText("");
        this.searchColumn.setColumns(10);
        panel.add(this.searchColumn);
        this.setMode("contains");
        JPanel panel_1 = new JPanel();
        panel_1.setBounds(422, 11, 135, 146);
        FlowLayout flowLayout_1 = (FlowLayout)panel_1.getLayout();
        flowLayout_1.setAlignment(0);
        this.praeFrame.getContentPane().add(panel_1);
        final JRadioButton rdbtnContains = new JRadioButton("Contains");
        panel_1.add(rdbtnContains);
        final JRadioButton rdbtnEndsWith = new JRadioButton("Ends with");
        panel_1.add(rdbtnEndsWith);
        final JRadioButton startsWith = new JRadioButton("Starts with");
        panel_1.add(startsWith);
        final JRadioButton rdbtnEquals = new JRadioButton("Equals");
        rdbtnEquals.setSelected(true);
        panel_1.add(rdbtnEquals);
        final JRadioButton doesNotEqual = new JRadioButton("Does not equal");
        panel_1.add(doesNotEqual);
        doesNotEqual.addActionListener(e2 -> {
            PRAE_Menu.this.setMode("doesNotEqual");
            rdbtnEquals.setSelected(false);
            startsWith.setSelected(false);
            rdbtnEndsWith.setSelected(false);
            rdbtnContains.setSelected(false);
        });
        rdbtnEquals.addActionListener(e2 -> {
            PRAE_Menu.this.setMode("equals");
            doesNotEqual.setSelected(false);
            startsWith.setSelected(false);
            rdbtnEndsWith.setSelected(false);
            rdbtnContains.setSelected(false);
        });
        startsWith.addActionListener(e2 -> {
            PRAE_Menu.this.setMode("startsWith");
            doesNotEqual.setSelected(false);
            rdbtnEquals.setSelected(false);
            rdbtnEndsWith.setSelected(false);
            rdbtnContains.setSelected(false);
        });
        rdbtnEndsWith.addActionListener(e2 -> {
            PRAE_Menu.this.setMode("endsWith");
            doesNotEqual.setSelected(false);
            startsWith.setSelected(false);
            rdbtnEquals.setSelected(false);
            rdbtnContains.setSelected(false);
        });
        rdbtnContains.addActionListener(e2 -> {
            PRAE_Menu.this.setMode("contains");
            doesNotEqual.setSelected(false);
            startsWith.setSelected(false);
            rdbtnEndsWith.setSelected(false);
            rdbtnEquals.setSelected(false);
        });
        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setBounds(435, 168, 122, 57);
        this.praeFrame.getContentPane().add(btnSubmit);
        btnSubmit.setFont(new Font("Tahoma", 0, 15));
        JPanel panel_2 = new JPanel();
        panel_2.setBounds(10, 85, 387, 125);
        this.praeFrame.getContentPane().add(panel_2);
        panel_2.setLayout(null);
        JLabel lblKey = new JLabel("Column Cheat Sheet");
        lblKey.setFont(new Font("Tahoma", 0, 15));
        lblKey.setBounds(10, 11, 179, 16);
        panel_2.add(lblKey);
        JButton reportKey = new JButton("Report Key - 2");
        reportKey.setBounds(10, 38, 179, 20);
        panel_2.add(reportKey);
        reportKey.addActionListener(e2 -> PRAE_Menu.this.searchColumn.setText("2"));
        JButton person = new JButton("Person(CWID) - 165");
        person.setBounds(10, 53, 179, 20);
        panel_2.add(person);
        person.addActionListener(e2 -> PRAE_Menu.this.searchColumn.setText("165"));
        JButton transAmt = new JButton("Approved Unit Price - 62");
        transAmt.addActionListener(e2 -> PRAE_Menu.this.searchColumn.setText("62"));
        transAmt.setBounds(10, 70, 179, 20);
        panel_2.add(transAmt);
        JButton acctCode = new JButton("Account Code - 135");
        acctCode.setBounds(10, 86, 179, 20);
        panel_2.add(acctCode);
        acctCode.addActionListener(e2 -> PRAE_Menu.this.searchColumn.setText("135"));
        JButton date = new JButton("FIX Date (YYYY-MM-DD) - 2");
        date.setBounds(10, 105, 179, 20);
        panel_2.add(date);
        date.addActionListener(e2 -> PRAE_Menu.this.searchColumn.setText("2"));
        JButton dispCode = new JButton("Disbursemet Code - 20");
        dispCode.addActionListener(e2 -> PRAE_Menu.this.searchColumn.setText("20"));
        dispCode.setBounds(198, 37, 179, 21);
        panel_2.add(dispCode);
        JButton holdCode = new JButton("Hold Code - 19");
        holdCode.addActionListener(e2 -> PRAE_Menu.this.searchColumn.setText("19"));
        holdCode.setBounds(198, 53, 179, 19);
        panel_2.add(holdCode);
        JButton generatePRAE = new JButton("Generate PRAE");
        generatePRAE.addActionListener(e2 -> PRAE_Menu.this.connection.rewritePRAE(textField.getText().trim()));
        generatePRAE.setFont(new Font("Tahoma", 0, 15));
        generatePRAE.setBounds(10, 244, 146, 35);
        this.praeFrame.getContentPane().add(generatePRAE);
        JSeparator separator = new JSeparator();
        separator.setBounds(8, 236, 547, 16);
        this.praeFrame.getContentPane().add(separator);
        this.textField = new JTextField();
        this.textField.setText("2,4,5,10,16,96,97,98,164,165,168,169,170,171,172,173,174");
        this.textField.setColumns(10);
        this.textField.setBounds(166, 243, 389, 40);
        this.praeFrame.getContentPane().add(this.textField);
        
        JLabel lblSeperatedByA = new JLabel("Create 1 master PRAE from all files.\r\nEnter desired columns seperated by a comma, or use * for all.");
        lblSeperatedByA.setBounds(10, 293, 523, 16);
        praeFrame.getContentPane().add(lblSeperatedByA);
        btnSubmit.addActionListener(e2 -> {
            if (PRAE_Menu.this.searchColumn.getText().trim().equals("")) {
                status.setText("Status: Fields not given.");
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
                dlgProgress.setLocationRelativeTo(praeFrame);
                SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
                    protected Void doInBackground() throws Exception {
                        status.setText("Status: Searching PRAE files..");
                        String message = connection.searchPRAE(searchString.getText().trim(), Integer.parseInt(searchColumn.getText().trim()), mode);
                        JTextArea textArea = new JTextArea(message);
                        JScrollPane scrollPane = new JScrollPane(textArea);
                        textArea.setLineWrap(true);
                        textArea.setWrapStyleWord(true);
                        scrollPane.setPreferredSize(new Dimension(600, 800));
                        JOptionPane.showMessageDialog(null, scrollPane, "PRAE", -1);
                        return null;
                    }

                    protected void done() {
                        dlgProgress.dispose();//close the modal dialog

                    }
                };
                sw.execute(); // this will start the processing on a separate thread
                dlgProgress.setVisible(true); //this will block user input as long as the processing task is working

                status.setText("Status: Ready for tasking");

            }
        });
        btnExit.addActionListener(e2 -> System.exit(0));
        btnClose.addActionListener(e2 -> PRAE_Menu.this.praeFrame.dispose());
    }
}

