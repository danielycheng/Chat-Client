import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
/*
 * Homework 4 
 * Daniel Cheng - dyc8av
 * Kendall Combs - kmc9aa
 * Shi Liu -sl3bg
 * Nick -ngm3dz
 * Lab 101
 */
public class IMGui {

	public JFrame frame;
	private JLabel titleLabel;
	private JLabel userLabel;
	private JLabel passLabel;
	public JTextField userTF;
	private JPasswordField passTF;
	private JButton loginBtn;
	public IMChat chat;
	public BuddyListGui bList;
	private String email = "";
	private String password = "";
	public ArrayList<String> rosterList = new ArrayList<String>();
	public Map<Object, ImageIcon> fullRosterList = new HashMap<Object, ImageIcon>();
	private ImageIcon icon;
	private JLabel noticeLabel;
	public Map<Object, Presence> presenceList = new HashMap<Object, Presence>();

	/**
	 * Launch the application.
	 */
	public void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IMGui window = new IMGui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */

	// first thing Client calls
	public IMGui() {
		this.chat = new IMChat(this);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		titleLabel = new JLabel("   IM Chat - Version 1.0");
		titleLabel.setBounds(172, 39, 135, 14);
		frame.getContentPane().add(titleLabel);

		userLabel = new JLabel("Username:");
		userLabel.setBounds(85, 74, 77, 14);
		frame.getContentPane().add(userLabel);

		passLabel = new JLabel("Password:");
		passLabel.setBounds(85, 115, 77, 14);
		frame.getContentPane().add(passLabel);

		userTF = new JTextField();
		userTF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleLoginBtnActionPerformed(e);
			}
		});
		userTF.setBounds(172, 71, 148, 20);
		frame.getContentPane().add(userTF);
		userTF.setColumns(20);
		userTF.setText("Username");

		passTF = new JPasswordField("Password");
		passTF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleLoginBtnActionPerformed(e);
			}
		});
		passTF.setBounds(172, 112, 148, 20);
		frame.getContentPane().add(passTF);

		loginBtn = new JButton("   Login");
		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handleLoginBtnActionPerformed(arg0);
			}
		});
		loginBtn.setBounds(172, 164, 107, 34);
		frame.getContentPane().add(loginBtn);

		noticeLabel = new JLabel("You Do NOT Need To Type \"@gmail.com\"");
		noticeLabel.setBounds(113, 225, 251, 14);
		frame.getContentPane().add(noticeLabel);
	}

	// starts the connection and loads buddies based upon the two textfields
	// calls the BuddyListGui class if successful
	protected void handleLoginBtnActionPerformed(ActionEvent arg0) {
		email = userTF.getText() + "@gmail.com";
		password = passTF.getText();
		chat.start();
		if (chat.logInCheck()) {
			chat.loadBuddies();
			chat.listenChat();
			this.bList = new BuddyListGui(this, chat);
			frame.setVisible(false);
		} else {
			JOptionPane
					.showMessageDialog(frame, "Incorrect, Please try again.");
		}

	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	// gets the buddyList from the user and adds it to an ArrayList<String>
	public ArrayList<String> getBuddies() {
		for (RosterEntry tempRos : chat.roster.getEntries()) {
			rosterList.add(tempRos.getUser().split("@")[0]);
		}
		return rosterList;

	}

	public void addNewBuddy(String s) {
		if (!rosterList.contains(s)) {
			rosterList.add(s);
		}
	}

	// this methods gets the image of the buddy and adds it to a map with usernames as keys
	public Map<Object, ImageIcon> getIcons() {
		for (String s : rosterList) {
			fullRosterList.put(s + "@gmail.com",
					chat.generateAvatar(s + "@gmail.com"));
		}
		return fullRosterList;
	}

	public void setIcons(String s) {
		fullRosterList.put(s + "@gmail.com",
				chat.generateAvatar(s + "@gmail.com"));
	}
	
	// this method gets the Presence of a buddy and adds it to a map with usernames as keys
	public Map<Object, Presence> getPresenceList() {
		for(String s: rosterList) {
			presenceList.put(s + "@gmail.com", chat.getPresence(s + "@gmail.com"));
		}
		return presenceList;
	}
	


}
