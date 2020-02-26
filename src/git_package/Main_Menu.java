package git_package;

import public_package.Preferences;

import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.*;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.TitledBorder;
import java.awt.GridLayout;
import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;

public class Main_Menu
{
	private JPanel panel;
	private final Function_Library func_lib;
	private JTextField textField_switch;
	private JTextField textField_commit;
	private JLabel lbl_branch;
	private String input;

	private JFrame frame;
	private JComboBox dropDownBox;
	private String[] branches;
	private ArrayList<String> recentBranches;
	public boolean done = false;

	public Main_Menu(Function_Library f2){
		func_lib = f2;
		if(func_lib.ready()) {
			initialize();	
			refreshInfo();
			done = true;
		}
	}
	/**
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		func_lib.readPacked_Refs();
		frame = new JFrame();

		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Main_Menu.class.getResource("/Jar Files/ua_background_mobile.jpg")));
		frame.setTitle("Main Menu");//("+f.userFolder+")");

		frame.setBounds(100, 100, 550, 298);
		frame.setDefaultCloseOperation(3);
		frame.getContentPane().setLayout(null);

		JButton button_exit = new JButton("Exit");
		button_exit.setBounds(10, 191, 122, 57);
		button_exit.setFont(new Font("Lucida Grande", 0, 15));
		frame.getContentPane().add(button_exit);

		lbl_branch = new JLabel(func_lib.getBranch());
		lbl_branch.setBounds(10, 11, 132, 30);
		lbl_branch.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lbl_branch.setSize( lbl_branch.getPreferredSize() );
		frame.getContentPane().add(lbl_branch);
//		frame.pack();
		JButton btnDeletePreferences = new JButton("Reset");
		btnDeletePreferences.setBounds(300, 225, 89, 25);
		btnDeletePreferences.addActionListener(e -> {
			int selection;
			selection=func_lib.okCancel("Are you sure? This will delete the .gitPreference file.");
			if(selection==0) {
				func_lib.deleteFile(func_lib.getUserHome()+"/.gitPreference");

				frame.dispose();
				EventQueue.invokeLater(() -> {
					try {
						new Setup_Menu(func_lib);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				});
			}
		});
		frame.getContentPane().add(btnDeletePreferences);

		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Basic Operations", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 52, 383, 136);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.setLocation(256, 79);
		addPopup(panel, popupMenu);

		JButton btnSubmit_switch = new JButton("Submit");
		btnSubmit_switch.setBounds(179, 38, 75, 23);
		panel.add(btnSubmit_switch);

		textField_switch = new JTextField();
		textField_switch.setBounds(94, 39, 75, 20);
		textField_switch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				textField_switch.setText("");
			}
		});
		textField_switch.setColumns(10);
		panel.add(textField_switch);


		////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////
		getRecentBranches();
		initDropdown();
		////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////

		JLabel lblSwitchBranch = new JLabel("Switch branch");
		lblSwitchBranch.setBounds(10, 42, 5, 14);
		lblSwitchBranch.setFont(new Font("Tahoma", Font.BOLD, 10));
		lblSwitchBranch.setSize( lblSwitchBranch.getPreferredSize() );
		panel.add(lblSwitchBranch);

		JLabel lblBranch = new JLabel("Branch");
		lblBranch.setBounds(94, 20, 46, 14);
		panel.add(lblBranch);
		lblBranch.setFont(new Font("Tahoma", Font.PLAIN, 9));

		JLabel lblRecent = new JLabel("Recent");
		lblRecent.setBounds(265, 20, 46, 14);
		panel.add(lblRecent);
		lblRecent.setFont(new Font("Tahoma", Font.PLAIN, 9));

		///////////////////////////////////////////////////////////////////////////////////////
		JButton btnSubmit_commit = new JButton("Submit");
		btnSubmit_commit.setBounds(280, 93, 89, 23);
		panel.add(btnSubmit_commit);

		textField_commit = new JTextField();
		textField_commit.setBounds(112, 94, 158, 20);
		panel.add(textField_commit);
		textField_commit.setColumns(10);

		JLabel lbl_comment = new JLabel("Comment (optional)");
		lbl_comment.setBounds(114, 79, 132, 14);
		panel.add(lbl_comment);
		lbl_comment.setFont(new Font("Tahoma", Font.PLAIN, 9));

		JLabel lblCommitAndPush = new JLabel("Commit and Push");
		lblCommitAndPush.setBounds(10, 97, 158, 14);
		lblCommitAndPush.setFont(new Font("Tahoma", Font.BOLD, 10));
		lblCommitAndPush.setSize( lblCommitAndPush.getPreferredSize() );
		panel.add(lblCommitAndPush);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Basic Commands", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(403, 52, 129, 136);
		frame.getContentPane().add(panel_1);

		JButton btnFetch = new JButton("FETCH");

		JButton btnPush = new JButton("PUSH");
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));

		JButton btn_pull = new JButton("PULL");
		panel_1.add(btn_pull);
		btn_pull.addActionListener(arg0 -> {
			func_lib.pull();
			refreshInfo();
		});
		panel_1.add(btnFetch);
		panel_1.add(btnPush);

		JButton btnViewDiffs = new JButton("View Diffs To Remote");
		btnViewDiffs.addActionListener(arg0 -> func_lib.diff());
		btnViewDiffs.setBounds(136, 15, 169, 31);
		frame.getContentPane().add(btnViewDiffs);

		JButton btn_hard_reset = new JButton("Hard Reset To Remote");
		btn_hard_reset.setBounds(313, 15, 169, 30);
		frame.getContentPane().add(btn_hard_reset);
		btn_hard_reset.addActionListener(e -> {
			int selection;
			selection=func_lib.okCancel("Are you sure? ALL changes made locally will be lost and the remote branch will be restored.");
			if(selection==0) {
				func_lib.hardReset();
			}
		});
		btnPush.addActionListener(e -> func_lib.push(func_lib.getBranch()));
		btnFetch.addActionListener(arg0 -> func_lib.fetch());
		btnSubmit_commit.addActionListener(e -> {
			input = func_lib.validateInput(textField_commit.getText().trim());
			func_lib.commitAndPush(input);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e2) {
				System.out.println(e2.getMessage());
			}
			textField_commit.setText("");
		});
		btnSubmit_switch.addActionListener(arg0 -> {
			input = func_lib.validateInput(textField_switch.getText().trim());
			if(!input.equals(func_lib.getBranch())) {
				func_lib.switchBranch(input);
			}
			textField_switch.setText("");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
			refreshInfo();
		});
		button_exit.addActionListener(e -> {
			public_package.Exit_Confirmation window = new public_package.Exit_Confirmation(null);
			window.frame.setVisible(true);
		});

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(arg0 -> {
			frame.dispose();
			EventQueue.invokeLater(() -> {
				new public_package.Login(null);
			});
		});
		btnClose.setFont(new Font("Dialog", Font.PLAIN, 15));
		btnClose.setBounds(400, 191, 122, 57);
		frame.getContentPane().add(btnClose);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(arg0 -> refreshInfo());
		btnRefresh.setBounds(300, 191, 89, 25);
		frame.getContentPane().add(btnRefresh);

		JButton btnOpen = new JButton("Open folder");
		btnOpen.addActionListener(e -> {
			if(func_lib.isWindows()) {
				try {
					Runtime.getRuntime().exec("explorer.exe /select," + func_lib.gitFolder + "\\.git");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			else{
				try {
					Runtime.getRuntime().exec("xdg-open " + func_lib.gitFolder);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnOpen.setBounds(150, 191, 140, 25);
		frame.getContentPane().add(btnOpen);

		JButton btnOpenBan = new JButton("Open SCM");
		btnOpenBan.addActionListener(e -> {
			if(lbl_branch.getText().startsWith("BAN-"))
				func_lib.openLink("https://scm.oit.ua.edu/jira/browse/"+lbl_branch.getText());
		});
		btnOpenBan.setBounds(150, 225, 140, 25);

		frame.getContentPane().add(btnOpenBan);
		//final frame
		frame.setVisible(true);
	}
	private void getRecentBranches(){

		recentBranches = new ArrayList<String>();

		if(Preferences.contents.containsKey("recentBranches")) {
			branches = Preferences.contents.get("recentBranches").split("/");
		}
		else{
			branches = new String[] {"","sevl","test","prod"};
		}
		recentBranches=stringArray_to_arrayList(branches);
	}

	private ArrayList<String> stringArray_to_arrayList(String[] sArray){
		ArrayList<String> aList=new ArrayList<String>();
		for(int i=0;i<sArray.length;++i){
			aList.add(sArray[i]);
		}
		return aList;
	}
	private void initDropdown(){
		dropDownBox = new JComboBox(branches);
		dropDownBox.setBounds(265, 39, 105, 20);
		dropDownBox.addActionListener(e -> {
			textField_switch.setText(dropDownBox.getSelectedItem().toString());
		});
		panel.add(dropDownBox);
	}
	private void refreshInfo() {
		func_lib.readPacked_Refs();
		lbl_branch.setText(func_lib.getBranch());
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}