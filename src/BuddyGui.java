import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
/*
 * Homework 4 
 * Daniel Cheng - dyc8av
 * Kendall Combs - kmc9aa
 * Shi Liu -sl3bg
 * Nick -ngm3dz
 * Lab 101
 */
public class BuddyGui {

	public JFrame frame;
	public String name = "";
	private ArrayList<String> messages;
	private JLabel addBuddyLabel;
	private JLabel usernameLabel;
	public JTextField userTF;
	private JButton addBtn;
	private BuddyListGui bList;
	private JButton removeBtn;
	private JLabel lblNewLabel;

	public String getName() {
		return name;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BuddyGui window = new BuddyGui();
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

	public BuddyGui() {
		initialize();
	}

	public BuddyGui(BuddyListGui bList) {
		this.bList = bList;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.getContentPane().setLayout(null);

		addBuddyLabel = new JLabel("Enter a Buddy!");
		addBuddyLabel.setBounds(177, 43, 90, 14);
		frame.getContentPane().add(addBuddyLabel);

		usernameLabel = new JLabel("Username:");
		usernameLabel.setBounds(88, 91, 64, 14);
		frame.getContentPane().add(usernameLabel);

		userTF = new JTextField();
		userTF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleAddBtnActionPerformed(e);
			}
		});
		userTF.setBounds(163, 88, 121, 20);
		frame.getContentPane().add(userTF);
		userTF.setColumns(10);

		addBtn = new JButton("Add");
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleAddBtnActionPerformed(e);

			}
		});
		addBtn.setBounds(82, 147, 105, 48);
		frame.getContentPane().add(addBtn);

		removeBtn = new JButton("Remove");
		removeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handleRemoveBtnActionPerformed(arg0);
			}
		});
		removeBtn.setBounds(241, 147, 105, 48);
		frame.getContentPane().add(removeBtn);
		
		lblNewLabel = new JLabel("You Do NOT Need To Type \"@gmail.com\"");
		lblNewLabel.setBounds(105, 222, 241, 14);
		frame.getContentPane().add(lblNewLabel);
		frame.addWindowListener(new WindowCloseListener());
		frame.setVisible(true);
	}

	// Called when you want to add a buddy
	protected boolean handleAddBtnActionPerformed(ActionEvent e) {
		name = userTF.getText();
		if (name.equals("")) {
			return false;
		}
		if (!name.contains("@")) {
			bList.chat.addBuddyRoster(name + "@gmail.com");
			bList.addBuddy(this);
			frame.setVisible(false);
			bList.counter1 = 1;
			return true;
		}
		return false;

	}

	// Called when you want to remove a buddy
	protected boolean handleRemoveBtnActionPerformed(ActionEvent arg0) {
		name = userTF.getText();
		if (name.equals("")) {
			return false;
		}
		if (!name.contains("@")) {
			bList.chat.removeBuddyRoster(name + "@gmail.com");
			bList.removeBuddy(this);
			frame.setVisible(false);
			bList.counter1 = 1;
			return true;
		}
		return false;
	}

	// listens for attemps to close the window, prevents multiple windows from being opened
	class WindowCloseListener implements WindowListener {

		@Override
		public void windowActivated(WindowEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowClosed(WindowEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			bList.counter1 = 1;

		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {

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
