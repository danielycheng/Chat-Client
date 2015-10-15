# Chat Client - Java

##Purpose
This program is a custom Chat Client using Google's Smack/XMPP that is able to interact with Google Chat. The project description, features, and major classes are outlined below. **This program was written in my first-year of University and has not been updated since Spring 2012.**

##How to Use
When application starts, it will display a screen that allows the user to input his/her username and password for the GoogleTalk IM network. After logging in to the GoogleTalk network, the program can successfully support all the core functionalities, such as adding and removing a buddy using his/her username, loading and displaying the user’s buddyList, and starting and receiving a chat window. In addition, the program implements a few extra features to make the app more useful.

The first extra feature is the ability to display and post current statuses (called Presence in Smack). It can display the user’s buddies’ current status in their buddyList using different colors: green for available, red for busy, yellow for away, and grey for offline. The app also auto-updates the buddyList accordingly as the buddies come and go from whatever status. Moreover, the user can set and change their own status by clicking on the drop-down box near the top of the window. The options are Available, Away, Busy, and Invisible.

The second extra feature is the ability to write up a personal status (in words) and display a buddy's personal status. After clicking on the “Set Status” button at the very top of the main window, the user can let other people know what’s on their mind by entering text into the JTextField. The user can then click on the “Okay” button and the application will auto-update their status. In addition, the user can view a specific buddy’s status (if he/she has one) at the top of the chat window beside his/her username when talking to them.

The third extra feature is the ability to have multiple chat windows running concurrently.  The user will be able to start and engage in multiple conversations with his/her buddies at the same time.

Last but not least, the fourth extra feature will display photos of the user and their buddies. The app will load all of the buddies’ profile pictures (if they have one), and display a mini version of the photos beside their usernames. The user can also see a specific buddy’s profile picture, as well as their own, beside their usernames in the chat windows.

##Program Design
Program Design

Essentially the application is formatted like a tree. It starts with the Client class, which leads directly to the IMGui class. If the IMGui class runs successfully it leads to the BuddyListGui class, and from there it has branches that are the chat clients. The actual chatting is handled by IMChat as kind of the "water" of the tree. Chats are passed into each IMChatGui where the chats are displayed and you can send messages etc. There are three more options on BuddyListGui. The first is Logout, which is self-explanatory. Using other options will open the BuddyGui class, giving the use options to add or remove a buddy, which will also be handled by IMChat. Set Status will allow the user to set their own status through a JOptionPane, and again, the actual setting is handled by IMChat.

###Major Classes & Methods
1. Client
(1) High-level Responsibility: serves as an overarching class that can call the methods from all other classes.
(2) Major Method: calls the method start( ) and directs the application to the IMGui class.

2. IMGui
(1) High-level Responsibility: handles all the actions and components associated with main login window. Also creates maps that relate username to avatar picture as well as username to presence.

(2) Major Methods:
i. handleLoginBtnActionPerformed(ActionEvent arg0)
After the Login Button is clicked, it starts the connection to the internet and loads buddies based upon the two textFields (username and password). The method calls the BuddyListGui class if the login is successful.

ii. getPresenceList()
This method gets the Presence of a Buddy and adds it to a Map<Object, Presence> presenceList with the Buddy’s username as key

iii. in the method initialize( ), creates the associated GUI Components:
a JFrame for the login window when you first start the application, a titleLabel that creates a JLabel displaying the title "IM Chat - Version 1.0", a userLabel, a userTF that takes in the username of the Gmail account, a passLabel, a passTF that takes in the password of the Gmail account, a loginBtn, and a noticeLabel informing the user that "You Do NOT Need To Type ‘@gmail.com’" along with your username.

3. BuddyGui
(1) High-level Responsibility: handles the GUI components associated with all the actions that can be performed to a Buddy. More specifically, the adding and removing of a Buddy from both our displayed BuddyList and the actual Gmail account.

(2) Major Methods:
i. handleAddBtnActionPerformed(ActionEvent e)
Calls the addBuddy( ) method on the BuddyListGui of this buddy, which will add the buddy to your BuddyList. It also calls the addBuddyRoster( ) method to add the buddy into the user’s actual Gmail account.

ii. handleRemoveBtnActionPerformed(ActionEvent arg0)
Calls the removeBuddy( ) method on the BuddyListGui of this buddy, which will remove the buddy to your BuddyList. It also calls the removeBuddyRoster( ) method to delete the buddy from the user’s actual Gmail account.

iii. in the method initialize( ), creates the associated GUI Components:
a JFrame for the add/remove Buddy option, an addBuddyLabel, an usernameLabel, an userTF that takes in the username of a Buddy, an addBtn that can be clicked to perform the action of adding a Buddy, a removeBtn that can be clicked to perform the action of removing a Buddy, a lblNewLabel that reminds the user that "You Do NOT Need To Type ‘@gmail.com’" along with your username.

4. BuddyListGui
(1)   High-level Responsibility: handles the GUI components associated with all the actions that can be performed to a BuddyList.

(2) Major Methods:
i. getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
Takes in the JList of your buddies and adjust the color of each Buddy’s status (presence) when they change.

ii. handleLogoutBtnActionPerformed(ActionEvent e)
Clicking on the logout button handles the action for when you click on the Logout Button: exits the application.

iii. handleOptionsBtnActionPerformed(ActionEvent e)
Clicking on the options button will start a new window written in the BuddyGui

iv. handleBtnStatusActionPerformed(ActionEvent e)
This JOptionPane allows you to set your personalized status – a short text that will show up in the chat window

v. handleComboBoxItemStateChanged(ItemEvent arg0)
Handles the JComboBox presence options – changes your actual status (presence) according to the options you clicked on the drop-down box

vi. MouseListenerClick implements MouseListener
Handles the actions performed when the window is clicked, pressed entered etc.

vii. in the method initialize( ), creates the associated GUI Components:
a JFrame for the display window after you login, a buddyLabel, a logoutBtn, a JList budList that will display all of the buddies and their presences, an optionsBtn that will open another window to add/remove a Buddy,  a btnStatus Button can be clicked to set your status (in words)

5. IMChat
(1) High-level Responsibility: This is the “skeleton” of the code, where all the actual networking goes on. In this class, the connection is established and everything from sending/receiving messages as well as presence, pictures, and chat listener.

(2) Major Methods:
i. addBuddyRoster(String s) & removeBuddyRoster(String s)
Takes in a String s, which is the username of a GTalk account (without the “@...”), connects to the internet and adds/removes the user to the actual Roster of your Gmail account. Your buddyList (friend list in gmail) will then auto-update.

ii. createChat(String user, String message)
Creates a new Chat and passes it to the IMChatGui class.

iii. loadBuddies()
manages changes made to the actual Roster of your Gmail account such as entriesAdded (Collection<String> arg0), entriesDeleted(Collection<String> arg0), entriesUpdated (Collection<String> arg0), and presenceChanged(Presence presence)

iv. listenChat()
Listens for incoming chats: chat window will pop up when another user initializes the conversation

v. generateAvatar(String jid)
Taking in a String jid (the username of a Gmail account), the class gets the picture associated with that account. A default picture is assigned when the account doesn’t originally have a profile picture.

6. IMChatGui
(1) High-level Responsibility: handles the GUI components associated with all the actions that can be performed with the actual chat window. These include getting the received messages, displaying statuses, sending chats, and displaying photos.

(2) Major Methods:
i. handleEnterBtnActionPerformed(ActionEvent arg0)
Handles the action when the "send" button is activated: if the text entered is not empty, sends the chat to the designated Gmail user

ii. updateTextArea implements ActionListener
Listens for incoming messages and updates once a second. (So as to prevent excessive spam messages)

iii. in the method initialize( ), creates the associated GUI Components:
a JFrame for the chat window after a conversation is initialized by either user, a msgTF, a userTP (text area) with a userSlider that autoscrolls, an enterBtn performing the "Send" action of the message, a budNameLabel displaying the Buddy’s username and your profile photo , a userLabel displaying your username and your profile photo, a statusLabel that displays and auto-updates the status of the Buddy you are talking to, and a timer that updates the Text Area.

