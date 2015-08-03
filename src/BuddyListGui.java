import java.awt.Color;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.Presence;
import javax.swing.JComboBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
/*
 * Homework 4 
 * Daniel Cheng - dyc8av
 * Kendall Combs - kmc9aa
 * Shi Liu -sl3bg
 * Nick -ngm3dz
 * Lab 101
 */
public class BuddyListGui {

	public JFrame frame;
	private JLabel buddyLabel;
	private JButton logoutBtn;
	private JButton optionsBtn;
	private JList budList;
	ListSelectionModel listSelectionModel;
	DefaultListModel model = new DefaultListModel();
	private JButton btnStatus;
	private String status = "";
	private IMGui IMGui;
	private BuddyGui gui;
	private int counter = -1;
	private int[] selected;
	public IMChat chat;
	private JScrollPane budListSlider;
	private Presence presence;
	private RosterEntry rosterEntry;
	public ArrayList<String> chatNameList = new ArrayList<String>();
	private ImageIcon icon;
	public int counter1 = 1;
	public int counter2 = 1;
	private JComboBox comboBox;
	private String[] options = {"Available", "Away", "Busy", "Invisible"};
	

	/**
	 * Launch the application.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BuddyListGui window = new BuddyListGui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @wbp.parser.entryPoint
	 */

	public BuddyListGui() {
		initialize();
	}

	public BuddyListGui(IMGui g, IMChat chat) {
		this.IMGui = g;
		this.chat = chat;
		this.counter = 1;
		this.presence = new Presence(Presence.Type.available);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 200, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		buddyLabel = new JLabel("Buddies");
		buddyLabel.setBounds(72, 73, 46, 14);
		frame.getContentPane().add(buddyLabel);

		logoutBtn = new JButton("  Logout");
		logoutBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleLogoutBtnActionPerformed(e);
			}
		});
		logoutBtn.setBounds(50, 511, 89, 23);
		frame.getContentPane().add(logoutBtn);

		optionsBtn = new JButton("Options");
		optionsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleOptionsBtnActionPerformed(e);
			}
		});
		optionsBtn.setBounds(50, 477, 89, 23);
		frame.getContentPane().add(optionsBtn);

		 if (counter > 0) {
		 for (String s : IMGui.getBuddies()) {
		 model.addElement(s);
		 }
		 counter = -1;
		 }

		budList = new JList(model);
		budList.setCellRenderer(new myRenderer(IMGui.getIcons(), IMGui.getPresenceList()));
		budListSlider = new JScrollPane(budList);
		budListSlider.setBounds(25, 98, 129, 351);
		frame.getContentPane().add(budListSlider);

		btnStatus = new JButton("Set Status");
		btnStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleBtnStatusActionPerformed(e);
			}
		});
		btnStatus.setBounds(34, 15, 114, 23);
		frame.getContentPane().add(btnStatus);
		
		comboBox = new JComboBox(options);
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				handleComboBoxItemStateChanged(arg0);
			}
		});
		comboBox.setBounds(25, 49, 129, 20);
		frame.getContentPane().add(comboBox);
		
		budList.addMouseListener(new MouseListenerClick());
		
		frame.setVisible(true);
	}

	/**
	 * Helper class to add pictures/background colors into the JList
	 * Gray - unavailable
	 * Green - available
	 * Red - Busy
	 * Yellow - away
	 */
	public class myRenderer extends DefaultListCellRenderer {
		private Map<Object, ImageIcon> icons = null;
		private Map<Object, Presence> pList = null;

		private myRenderer(Map<Object, ImageIcon> icons, Map<Object, Presence> pList) {
			this.icons = icons;
			this.pList = pList;

		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list,
					value, index, isSelected, cellHasFocus);
			Icon icon = icons.get(value + "@gmail.com");
            label.setIcon(icon);
            if(pList.get(value + "@gmail.com") != null &&
					pList.get(value + "@gmail.com").toString().equals("unavailable")){
				label.setBackground( Color.LIGHT_GRAY );
			}
            else if(pList.get(value + "@gmail.com") != null &&
					pList.get(value + "@gmail.com").toString().contains("away")){
            	label.setBackground(Color.yellow);
            }
            else if(pList.get(value + "@gmail.com") != null &&
					pList.get(value + "@gmail.com").toString().contains("dnd")){
            	label.setBackground(Color.red);
            }
			else {
				label.setBackground( Color.green );
			}
            
            return label;
		}
	}
	
	// calls this method when a buddys Presence changes
	public void refreshPresence() {
		budList.setCellRenderer(new myRenderer(IMGui.getIcons(), IMGui.getPresenceList()));
	}
	
	public boolean addBuddy(BuddyGui b) {
		String text = b.getName();
		if (model.contains(text)) {
			JOptionPane.showMessageDialog(frame, "You are already friends with " + text);
			return false;
		}
		if (!text.equals("")) {
			IMGui.setIcons(text);
			model.addElement(text);
			return true;
		}
		return false;

	}

	public boolean removeBuddy(BuddyGui b) {
		String text = b.getName();
		if (!model.contains(text)) {
			JOptionPane.showMessageDialog(frame, "That Buddy does not exist.");
			return false;
		} else if (!text.equals("")) {
			model.removeElement(text);
			return true;
		}
		return false;
	}

	public void startChatGui() {
		String chatName = "";
		selected = budList.getSelectedIndices();
		for (int i = 0; i < selected.length; i++) {
			chatName = (String) budList.getModel().getElementAt(selected[i]) + "@gmail.com";
		}
		if (!chatNameList.contains(chatName)) {
			chat.createChat(chatName, "");
			chatNameList.add(chatName);
		}
	}

	public void startChatGui(String s, String message) {
		String chatName = s;
		if (!chatNameList.contains(chatName)) {
			chat.createChat(chatName, message);
			chatNameList.add(chatName);
		}
	}

	protected void handleLogoutBtnActionPerformed(ActionEvent e) {
		System.exit(0);
	}
	
	protected void handleOptionsBtnActionPerformed(ActionEvent e) {
		if(counter1 > 0) {
		gui = new BuddyGui(this);	
		counter1 = -1;
		}
	}

	class MouseListenerClick implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			if (arg0.getClickCount() == 2) {
				startChatGui();
			}

		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}
	}

	// this JOptionPane allows you to set your personalized status
	protected void handleBtnStatusActionPerformed(ActionEvent e) {
		String status = "";
		status = JOptionPane.showInputDialog(null, "Set your status");
		chat.setStatus(status);
	}
	
	// handles the JComboBox presence options
	protected void handleComboBoxItemStateChanged(ItemEvent arg0) {
		int position = comboBox.getSelectedIndex();
		String selectedPresence = options[position];
		presence.setType(Presence.Type.available);
		if(selectedPresence.equals("Available")){
			presence.setMode(Presence.Mode.available);
		}
		if(selectedPresence.equals("Away")){
			presence.setMode(Presence.Mode.away);
		}
		if(selectedPresence.equals("Busy")){
			presence.setMode(Presence.Mode.dnd);
		}
		if(selectedPresence.equals("Invisible")){
			presence.setType(Presence.Type.unavailable);
		}
		chat.connection.sendPacket(presence);
	}
}
