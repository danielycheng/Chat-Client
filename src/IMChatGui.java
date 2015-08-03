import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
/*
 * Homework 4 
 * Daniel Cheng - dyc8av
 * Kendall Combs - kmc9aa
 * Shi Liu -sl3bg
 * Nick -ngm3dz
 * Lab 101
 */
public class IMChatGui {

	public JFrame frame;
	private JTextField msgTF;
	private JButton enterBtn;
	public JLabel budNameLabel;
	DefaultListModel model;
	private JScrollPane userSlider;
	private String buddyName = "A buddy";
	public BuddyListGui bList;
	public JTextArea userTP;
	private IMChat chat;
	private Chat newChat;
	private String message;
	private Runnable runnable;
	Timer timer;
	private JLabel buddyLabel;
	private JLabel userLabel;
	private String buddyMessage = "";
	private String buddyUser = "";
	public JLabel statusLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IMChatGui window = new IMChatGui();
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
	public IMChatGui() {
		initialize();
	}

	public IMChatGui(BuddyListGui blg, IMChat chat, Chat newChat, String user) {
		this.chat = chat;
		this.bList = blg;
		this.newChat = newChat;
		this.buddyUser = user;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		startChat();
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.getContentPane().setLayout(null);

		msgTF = new JTextField();
		msgTF.setBounds(54, 217, 256, 21);
		msgTF.setText("");
		frame.getContentPane().add(msgTF);
		msgTF.setColumns(10);
		msgTF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleEnterBtnActionPerformed(e);

			}
		});
		enterBtn = new JButton("Send");
		enterBtn.setBounds(335, 215, 89, 23);
		enterBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handleEnterBtnActionPerformed(arg0);
			}
		});
		frame.getContentPane().add(enterBtn);

		model = new DefaultListModel();

		budNameLabel = new JLabel(buddyUser.split("@")[0]);
		budNameLabel.setBounds(10, 11, 123, 21);
		frame.getContentPane().add(budNameLabel);

		buddyLabel = new JLabel();
		buddyLabel.setIcon(chat.generateAvatar(buddyUser));
		buddyLabel.setBounds(10, 43, 81, 65);
		frame.getContentPane().add(buddyLabel);
		userTP = new JTextArea();
		userSlider = new JScrollPane(userTP);
		userSlider.setBounds(98, 42, 309, 159);
		frame.getContentPane().add(userSlider);
		userSlider.getVerticalScrollBar().addAdjustmentListener(
				new AdjustmentListener() {
					public void adjustmentValueChanged(AdjustmentEvent e) {
						userTP.select(userTP.getHeight() + 1000, 0);
					}
				});

		userLabel = new JLabel();
		userLabel.setBounds(10, 119, 81, 65);
		userLabel.setIcon(chat.generateAvatar(chat.gui.userTF.getText()
				+ "@gmail.com"));
		frame.getContentPane().add(userLabel);
		userTP.setEditable(false);
		userTP.setWrapStyleWord(true);
		userTP.setLineWrap(true);

		statusLabel = new JLabel("New label");
		statusLabel.setBounds(149, 11, 258, 21);
		frame.getContentPane().add(statusLabel);
		statusLabel.setText(chat.getStatus(buddyUser));
		timer = new Timer(1000, new updateTextArea());
		timer.start();
		frame.addWindowListener(new WindowCloseListener());
		frame.setVisible(true);

	}

	public void setBuddyUser(String buddyUser) {
		this.buddyUser = buddyUser;
	}

	public void startChat() {
		newChat.addMessageListener(new MessageListener() {
			public void processMessage(Chat chat, Message message) {
				buddyMessage = message.getBody();
				buddyUser = message.getFrom();
				frame.setVisible(true);
				if (!bList.chatNameList.contains(buddyUser.split("/")[0])) {
					bList.chatNameList.add(buddyUser.split("/")[0]);
				}
			}

		});
	}

	public boolean sendChat(String message) {
		String msg = message;
		try {
			newChat.sendMessage(msg);
		} catch (XMPPException e) {
			return false;
		}
		msg = "";
		return true;
	}

	// called when the "send" button is activated
	protected void handleEnterBtnActionPerformed(ActionEvent arg0) {
		String text = msgTF.getText();
		if (!text.equals("")) {
			userTP.append(chat.gui.userTF.getText() + ": " + text + "\n");
			msgTF.setText("");
			sendChat(text);
		}
	}

	// listens for incomming messages and updates once a second. Prevents
	// excessive spam messages.
	class updateTextArea implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!buddyMessage.equals("")) {
				userTP.append(buddyUser.split("@")[0] + ": " + buddyMessage
						+ "\n");
				buddyMessage = "";
			}
			statusLabel.setText(chat.getStatus(buddyUser));
		}
	}

	// listens for attemps to close the window
	class WindowCloseListener implements WindowListener {

		@Override
		public void windowActivated(WindowEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowClosed(WindowEvent arg0) {

		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			bList.chatNameList.remove(buddyUser.split("/")[0]);
			userTP.setText("");
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowIconified(WindowEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowOpened(WindowEvent arg0) {
			// TODO Auto-generated method stub

		}

	}
}
