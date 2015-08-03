import java.awt.Toolkit;
import java.util.Collection;

import javax.swing.ImageIcon;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.RosterPacket.ItemStatus;
import org.jivesoftware.smackx.packet.VCard;
/*
 * Homework 4 
 * Daniel Cheng - dyc8av
 * Kendall Combs - kmc9aa
 * Shi Liu -sl3bg
 * Nick -ngm3dz
 * Lab 101
 */
public class IMChat {

	public IMGui gui;
	private boolean logIn;
	private ConnectionConfiguration config;
	public XMPPConnection connection;
	public Roster roster;
	private ChatManager chatmanager;
	private String myBuddy = "";
	private String myMessage = "";
	private String buddyMessage = "";
	private Chat newChat;
	public String buddyUser = "";
	private ChatManager autoChatManager;
	private IMChatGui chatGui;
	public String status = "";

	public IMChat(IMGui g) {
		this.gui = g;
	}

	public boolean logInCheck() {
		return logIn;
	}

	public boolean addBuddyRoster(String s) {
		if (!roster.contains(s)) {
			try {
				roster.createEntry(s, s.split("@")[0], null);
			} catch (XMPPException e) {
				return false;
			}
		}
		return true;
	}

	public boolean removeBuddyRoster(String s) {
		for (RosterEntry tempRos : roster.getEntries()) {
			if (tempRos.getUser().equals(s)) {
				try {
					roster.removeEntry(tempRos);
				} catch (XMPPException e) {
					return false;
				}
			}
		}
		return true;
	}

	// our "main" method here
	public void start() {
		config = new ConnectionConfiguration("talk.google.com", 5222,
				"gmail.com");
		connection = new XMPPConnection(config);
		try {
			connection.connect();
			if (!gui.getEmail().equals("")) {
				connection.login(gui.getEmail(), gui.getPassword());
			}
			logIn = connection.isAuthenticated();
		} catch (XMPPException e1) {
		}
	}

	public void loadBuddies() {
		roster = connection.getRoster();
		roster.addRosterListener(new RosterListener() {

			@Override
			public void entriesAdded(Collection<String> arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void entriesDeleted(Collection<String> arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void entriesUpdated(Collection<String> arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void presenceChanged(Presence presence) {
				String user = presence.getFrom().split("@")[0];
				Presence newPresence = roster.getPresence(user + "@gmail.com");
				gui.presenceList.put(user, newPresence);
				gui.bList.refreshPresence();
				
			}
		});
	}

	public void closeChat() {
		connection.disconnect();
	}

	// creates a new Chat and passes it to IMChatGui
	public void createChat(String user, String message) {
		String myBuddy = user;
		ChatManager chatmanager = connection.getChatManager();
		Chat newChat = chatmanager.createChat(myBuddy, new MessageListener() {
			public void processMessage(Chat chat, Message message) {
			}
		});
		IMChatGui chatGui = new IMChatGui(gui.bList, this, newChat, myBuddy);
		if (!message.equals("")) {
			chatGui.userTP.append(user.split("@")[0] + ": " + message + "\n");
		}
	}

	// listens for incomming chats
	public void listenChat() {
		connection.getChatManager().addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat chat, boolean createdLocally) {
				if (!createdLocally) {			
					chat.addMessageListener(new MessageListener() {
						public void processMessage(Chat chat, Message message) {
							String buddyMessage = message.getBody();
							String buddyUser = message.getFrom();
							gui.bList.startChatGui(buddyUser.split("/")[0],
									buddyMessage);
						}
					});
				}
			}
		});
	}

	
	// gets the image associated with your buddys, assigns a default one if none exists
	public ImageIcon generateAvatar(String jid) {
		VCard card = new VCard();
		ImageIcon avatar = new ImageIcon();
		ImageIcon defaultAvatar = new ImageIcon(getClass().getResource(
				"Google-Talk.png"));
		defaultAvatar.setImage(defaultAvatar.getImage().getScaledInstance(50,
				50, 1));
		try {
			card.load(connection, jid);
			avatar = new ImageIcon(card.getAvatar());
			avatar.setImage(avatar.getImage().getScaledInstance(50, 50, 1));
		} catch (XMPPException e) {
			avatar = defaultAvatar;
		} catch (NullPointerException e) {
			avatar = defaultAvatar;
		}
		avatar.setImage(avatar.getImage().getScaledInstance(50, 50, 1));
		return avatar;
	}

	// gets the Presence of the buddy
	public Presence getPresence(String s) {
		Presence presence = connection.getRoster().getPresence(s);
		return presence;
	}

	// sets the Status
	public void setStatus(String s) {
		Presence presence = new Presence(Presence.Type.available);
		presence.setPriority(24);
		presence.setMode(Presence.Mode.available);
		presence.setStatus(s);
		connection.sendPacket(presence);
	}

	// gets the Status
	public String getStatus(String s) {
		String status = "";
		Presence presence = connection.getRoster().getPresence(s);
		status = presence.getStatus();
		return status;

	}

}
