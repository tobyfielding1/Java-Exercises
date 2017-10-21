

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.NumberFormatter;


@SuppressWarnings("serial")
public class Client extends JFrame implements Runnable {
	
	final long DAY_IN_MS = 1000 * 60 * 60 * 24;//useful for increasing or decreasing a util.Date variable
	private int userID;
	private ClientComms comms;
	private JPanel contentPane;
	private ArrayList<Notification> notifications = new ArrayList<Notification>();//stores all notifications from server to client
	private int loadingDots = 0;//slightly silly but effective way of displaying a loading graphic. using dots in the titloe bar

	/* JFrame title bar
	 * @see java.awt.Frame#setTitle(java.lang.String)
	 */
	public void setTitle(String t){
		if (userID != 0)
			super.setTitle("NotBay - Logged in as User "+ userID +" - " + t);
	}
	
	private void setFrameSize(int x, int y){
		this.setSize(x,y);
	}
	
	/**
	 * @param dirContainingCommsFolder
	 * @throws IOException
	 */
	public Client(String dirContainingCommsFolder) throws IOException{
		super("NotBay");
		comms = new ClientComms(dirContainingCommsFolder);
	}
	
	public Client() throws IOException{
		this("");
	}
	
	/**
	 * provides a string of dots to animate Please Wait...... etc. 
	 * Its a bit silly but effective and not annoying like loading bars
	 * the performance overhead is irrelevant because the panel is waiting for a file anyway
	 * 
	 * @returns number of dots to display in loading bar
	 */
	private String dots(){
		String dots = "";
		loadingDots++;
		if (loadingDots >= 1000)
			loadingDots = 0;
		for (int i = 0; i<loadingDots/16; i++)
			dots = dots + ".";
		return dots;
	}
	
	/**
	 * waits for comms.recieveMessage() but adds waiting graphics 
	 * and logic for dealing with unexpected messages
	 * 
	 * @param messageType client is awaiting from server
	 * @return
	 */
	private <T extends MsgToClient> T blockingRecieveMsg(Class<T> messageType){
		Message msg;
		Date startTime = new Date();
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		String title = this.getTitle();
		while ((msg = comms.recieveMessage()) == null){
			try {
				Thread.sleep(4);
				this.setTitle("Please Wait" + dots());
				//once the client has waited for over ten seconds it gives up
				if(((new Date().getTime() - startTime.getTime())/1000 % 60) > 10){
					this.setCursor(Cursor.getDefaultCursor());
					super.setTitle(title);
					JOptionPane.showMessageDialog(new JFrame(), "Action Failed: Server did not respond to request");
					return null;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};super.setTitle(title);
		this.setCursor(Cursor.getDefaultCursor());
		/*if message is a notification, it will processed and stored
		*and the method will keep looking for the expected message(this EDT Thread shouldnt be used for notifications)
		*/
		if (msg.getClass() != messageType){
			if (msg instanceof Notification){
				notifications.add((Notification)msg);
				return blockingRecieveMsg(messageType);
			}else
				throw new NullPointerException("client recieved unexpected message while waiting for response");
		}
		return (T) msg;
	}
	
	/**
	 * first panel the user sees
	 * @author Toby
	 *
	 */
	class LoginPanel extends JPanel{
		
		LoginPanel(){
			this.setSize(300, 200);
			this.setLayout(null);
			
			JLabel text = new JLabel("Welcome to NotBay, please login or register:");
			text.setBounds(10, 10, 260,60);
			
			JTextField UID = new JTextField();
			JLabel lbl1 = new JLabel("UserID:");
			lbl1.setBounds(10, 80, 160, 25);
			UID.setBounds(100, 80, 160, 25);
			
			JPasswordField pwd = new JPasswordField();
			JLabel lbl2 = new JLabel("Password:");//asking for users password
			lbl2.setBounds(10, 120, 160, 25);
			
			JTextField pwdEntry = new JTextField("Password");//asking to set up new password
			pwd.setBounds(100, 120, 160, 25);
			
			JButton login = new JButton("Login");
			login.setBounds(180, 160, 80, 25);
			
			JButton register = new JButton("Register");
			register.setBounds(10,160,100,25);
			
			JTextField first = new JTextField("First Name");
			JTextField family = new JTextField("Family Name");
			JButton submitReg = new JButton("Register");
			
			login.addActionListener(new ActionListener(){
				
				@Override
				public void actionPerformed(ActionEvent pressed) {
					
					comms.sendMessage(new MsgToServer.Login(new Integer(UID.getText()), pwd.getText()));
					MsgToClient.LoginResponse msg = blockingRecieveMsg(MsgToClient.LoginResponse.class);
					
					//login successful
					if (msg.userID != null){
						proceedToDashboard(msg);
					//login failed
					}else{
						switch(msg.status){
							case NOT_REGISTERED: JOptionPane.showMessageDialog(new JFrame(), "User ID not found");
							break;
							case WRONG_PWD: JOptionPane.showMessageDialog(new JFrame(), "Wrong password for User ID");
						}
					}
				}
			});
			
			//re-jigs the panel for the user to enter registration details
			register.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent pressed) {
					setSize(300, 200);
					remove(login);
					remove(UID);
					remove(register);
					remove(pwd);
					remove(text);
					remove(lbl1);
					remove(lbl2);
					add(first);
					first.setBounds(100, 10, 160, 25);
					add(family);
					family.setBounds(100, 40, 160, 25);
					add(pwdEntry);
					pwdEntry.setBounds(100, 70, 160, 25);
					add(submitReg);
					submitReg.setBounds(140, 110, 120, 25);
					validate();
					repaint();
				}
			});
			
			submitReg.addActionListener(new ActionListener(){
				
				@Override
				public void actionPerformed(ActionEvent pressed) {
					
					comms.sendMessage(new MsgToServer.Register(first.getText(), family.getText(), pwdEntry.getText()));
					MsgToClient.LoginResponse msg = blockingRecieveMsg(MsgToClient.LoginResponse.class);
					
					if (msg.userID != null){
						proceedToDashboard(msg);
						JOptionPane.showMessageDialog(new JFrame(), "Please note your new User ID: " + userID);
					}else
						JOptionPane.showMessageDialog(new JFrame(), "registration failed for unknown reason");
				}
			});
			this.add(lbl1);
			this.add(lbl2);
			this.add(text);
			this.add(UID);
			this.add(pwd);
			this.add(login);
			this.add(register);
		}
		
		/**
		 * given a successful login message this sets up the session and displays the dashboard
		 * @param msg
		 */
		private void proceedToDashboard(MsgToClient.LoginResponse msg){
			userID = msg.userID;
			notifications.addAll(msg.notifications);
			CardLayout cardLayout = (CardLayout) contentPane.getLayout();
			setFrameSize(1200,550);
			cardLayout.show(contentPane,"dashboard");
		}
		
	}
	
	/**
	 * this is the main home page of the GUI, 
	 * it contains and controls access to all other panels
	 * 
	 * @author Toby
	 *
	 */
	class Dashboard extends JPanel{
		
		DashboardPage first;//first page to be shown after login(ie. notifications page)
		
		Dashboard(){
			this.setLayout(new BorderLayout());
			JPanel content = new JPanel();
			JPanel navBanner = new JPanel();//buttons to navigate between cardLayout pages
			navBanner.setLayout(new BoxLayout(navBanner, BoxLayout.LINE_AXIS));
			content.setLayout(new CardLayout());
			
			//ensures first page displays up to date info
			this.addComponentListener(new ComponentAdapter(){

				@Override
				public void componentShown(ComponentEvent arg0) {
					setTitle("Notifications");
					first.loadPanel();
					super.componentShown(arg0);
				}
				
			});
			
			/**
			 * to be applied to all nav buttons,
			 * takes the string name of button and goes to that card
			 * button names and cardLayout names MUST be kept the same!!
			 */
			class PanelSelect implements ActionListener {
				@Override
				public void actionPerformed(ActionEvent e) {
					CardLayout cardLayout = (CardLayout) content.getLayout();
					String panelName = ((JButton)e.getSource()).getText();
					cardLayout.show(content,panelName);
					setTitle(panelName);
				}
			}
			
			JButton[] navButtons = {new JButton("View Items"),new JButton("Sell New Item"),new JButton("My Bids"),new JButton("My Items For Sale"),new JButton("Notifications")};
			
			this.add(content, BorderLayout.CENTER);
			this.add(navBanner, BorderLayout.PAGE_START);
			
			for(JButton btn : navButtons){
				navBanner.add(btn);
				btn.addActionListener(new PanelSelect());
			}
			
			JButton logout = new JButton("Logout");
			logout.setBackground(Color.white);
			navBanner.add(logout);
			
			logout.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					logout();
				}
			});
			
			content.add((Component)(first = new NotificationsPage()), "Notifications");
			content.add(new ItemsPage(), "View Items");
			content.add(new SubmitPage(), "Sell New Item");
			content.add(new BidsPage(), "My Bids");
			content.add(new MyItemsPage(), "My Items For Sale");
			
			for(Component panel : content.getComponents()){
				panel.addComponentListener(
				/**
				 *loads the relevant panel (ie.loads auction items list) when user navigates to it
				 */
				new ComponentAdapter(){
					@Override
					public void componentShown(ComponentEvent e) {
						((DashboardPage)e.getComponent()).loadPanel();
					}
				});
			}

		}
	}
	
	/**
	 * useful method for setting Jtable column widths, used all over
	 * 
	 * @param table
	 * @param tablePreferredWidth
	 * @param percentages
	 */
	public static void setJTableColumnsWidth(JTable table, int tablePreferredWidth, double... percentages) {
	    double total = 0;
	    for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
	        total += percentages[i];
	    }
	 
	    for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
	        TableColumn column = table.getColumnModel().getColumn(i);
	        column.setPreferredWidth((int)
	                (tablePreferredWidth * (percentages[i] / total)));
	    }
	}
	
	/**
	 * generic page for displaying list of auction items
	 */
	class ItemsPage extends JPanel implements DashboardPage{
		
		//these components are all used in search refinement
		JRadioButton sold;
		JSpinner createdSince;
		JComboBox<Object> seller;
		JComboBox<Category> category;
		JComboBox<Object> id;
		JButton loadItems;
		JLabel sellerLabel;
		
		JTable table;
		JScrollPane itemsPane;
		final String[] columnNames = {"ID","Seller ID","Category","Title","Description","Start","End","Current Bid","Status"};
		
		JButton bid;// allows user to bid on selected item

		/**
		 * replaces current jtable with a new one according to 'rowData' parameter
		 * also updates all search fields with allowed options (ie. users and item ID's)
		 * @param rowData
		 * @param allItemIDs
		 * @param allSellers
		 * @return JTable
		 */
		public JTable drawTable(Object[][] rowData,  Set<Integer> allItemIDs, Set<Integer> allSellers){
			//keeps previous selection so user doesnt have to repeatedly re-enter choices
			Object selectedID = id.getSelectedItem();
			Object selectedSeller =seller.getSelectedItem();
			
			//updates all search fields with allowed options (ie. users and item ID's)
			id.removeAllItems();
			seller.removeAllItems();
			
			id.addItem("All");
			seller.addItem("All");
			
			for(Integer ItemID : allItemIDs)
				id.addItem(ItemID);
			for(Integer SellerID : allSellers)
				seller.addItem(SellerID);	
			
			seller.setSelectedItem(selectedSeller);
			id.setSelectedItem(selectedID);
			
			JTable itemsTable = new JTable(rowData, columnNames){
				@Override
				public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
					Component c = super.prepareRenderer(renderer, row, column);
					
					//  Colour each row based on "status"
					if (!isRowSelected(row)){
						c.setBackground(getBackground());
						int modelRow = convertRowIndexToModel(row);
						Object type;
						
						if(getModel().getValueAt(modelRow, 8) != null)
							type = getModel().getValueAt(modelRow, 8);
						else
							type = "";
						if ("Winning".equals(type) || Status.SOLD.equals(type)) c.setBackground(Color.CYAN);
						else if ("Won".equals(type) || Status.ONGOING.equals(type)) c.setBackground(Color.green);
						else if ("Lost".equals(type) || Status.CLOSED.equals(type)) c.setBackground(Color.pink);
						else if ("Losing".equals(type) || Status.NOT_STARTED.equals(type)) c.setBackground(Color.orange);
					}
					return c;
				}
			};
			itemsTable.getTableHeader().setOpaque(false);
			itemsTable.getTableHeader().setBackground(Color.LIGHT_GRAY);
			itemsTable.setRowHeight(30);
			setJTableColumnsWidth(itemsTable,1200,4,5,7,11,32,10,10,12,9);
			return itemsTable;
		}
		
		public ItemsPage() {
			
			this.setLayout(new BorderLayout());
			sold = new JRadioButton("Include Sold");
			loadItems = new JButton("Load Items");
			seller = new JComboBox<Object>();
			category = new JComboBox<Category>();
			id = new JComboBox<Object>();
			bid = new JButton("Bid on Selected Item");
			
			createdSince = new JSpinner(new SpinnerDateModel());
			createdSince.setEditor(new JSpinner.DateEditor(createdSince, "dd/MM/yyyy"));
			createdSince.setModel(new SpinnerDateModel());
		    createdSince.setValue(new Date(System.currentTimeMillis() - (7 * DAY_IN_MS)));
			
			
			this.add(new JPanel(){{
				this.setLayout(new FlowLayout());
				this.add(bid);
				bid.setBackground(Color.pink);
			}}, BorderLayout.PAGE_END);

			this.add(new JPanel(){{
				this.setLayout(new GridLayout(2,5));
				this.add(sellerLabel = new JLabel("Select Seller:"));
				this.add(new JLabel("Select Category:"));
				this.add(new JLabel("Created After:"));
				this.add(new JLabel("Search Item ID:"));
				this.add(sold);
				this.add(seller);
				this.add(category);
				this.add(createdSince);
				this.add(id);
				this.add(loadItems);
			}},BorderLayout.PAGE_START);
			for(Category catgry : Category.values())
				category.addItem(catgry);
			
			bid.addActionListener(new ActionListener(){
				
				/* there is lots of bid verification logic here (ie.checking amount, whether user == seller etc)
				 * it is here rather than the server side to increase responsiveness
				 */
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try{
						if ((int)table.getValueAt(table.getSelectedRow(), 1) == userID){
							JOptionPane.showMessageDialog(new JFrame(), "you cannot bid on your own item");
							return;
						}
						if ((table.getValueAt(table.getSelectedRow(), 8) instanceof Status && table.getValueAt(table.getSelectedRow(), 8) != Status.ONGOING) || table.getValueAt(table.getSelectedRow(), 8).equals("Won") || table.getValueAt(table.getSelectedRow(), 8).equals("Lost")){
							JOptionPane.showMessageDialog(new JFrame(), "you cannot bid on an auction that isn't active. ");
							return;
						}
						Double amount = new Double(JOptionPane.showInputDialog(new JFrame("Place Bid"), "How much would you like to bid on '" + table.getValueAt(table.getSelectedRow(), 3)+"' in £?"));
						Bid b = new Bid(amount, userID, (int)table.getValueAt(table.getSelectedRow(), 0), new Date());
						
						//gets up to date bids to check new bid is over the previous
						comms.sendMessage(new MsgToServer.requestItems(userID, null, Category.ALL, (int)table.getValueAt(table.getSelectedRow(), 0), null, false));
						MsgToClient.Items msg = blockingRecieveMsg(MsgToClient.Items.class);
						
						// check bid over previous bids
						if((msg.items.get(0).getHighestBid() == null && msg.items.get(0).getStartPrice() < amount) || msg.items.get(0).getHighestBid().amount < amount){
							comms.sendMessage(new MsgToServer.PlaceBid(userID, b));
							JOptionPane.showMessageDialog(new JFrame(), "bid successful");
						}
						else{
							JOptionPane.showMessageDialog(new JFrame(), "your bid amount is below previous bids, bid again");
							actionPerformed(new ActionEvent(bid, 1, "re-bid"));
						}
					}catch(ArrayIndexOutOfBoundsException e){
						//trying to bid without selecting item
						JOptionPane.showMessageDialog(new JFrame(), "you must select an item (table row) before placing a bid");
					}catch(NumberFormatException e2){
						JOptionPane.showMessageDialog(new JFrame(), "wrong format: enter any decimal number, no other characters");
					}catch(NullPointerException e3){}//means that process was cancelled by user
					
					loadItems.doClick(450);	
				}
				
			});
			
			loadItems.addActionListener(new ActionListener(){
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Integer sellerID;
					Integer itemID;
					//null values indicate the server should return all itemsID's and sellerID's
					if(seller.getSelectedItem().equals(new String("All")))
						sellerID = null;
					else
						sellerID = (Integer)seller.getSelectedItem();
					if(id.getSelectedItem().equals(new String("All")))
						itemID = null;
					else
						itemID = (Integer)id.getSelectedItem();
					
					updateTable(sellerID, (Category)category.getSelectedItem(), itemID, (Date)createdSince.getValue(), sold.isSelected());	
				}
			});	
		}		
		
		/**
		 * reads message and returns table rows as 2D array 
		 * @param msg
		 * @return
		 */
		protected Object[][] generateRowData(MsgToClient.Items msg){
			Object[][] rowData = new Object[msg.items.size()][9];
			int i = 0;
			for(Item item : msg.items){
				rowData[i][0] = item.ID;
				rowData[i][1] = item.vendorID;
				rowData[i][2] = item.getCategory();
				rowData[i][3] = item.getTitle();
				rowData[i][4] = item.getDescription();
				rowData[i][5] = item.getStart();
				rowData[i][6] = item.getEnd();
				if (item.getHighestBid() != null)
					rowData[i][7] = new String("£" + item.getHighestBid().amount + " by user " + item.getHighestBid().userID);
				else
					rowData[i][7] = new String("£" + item.getStartPrice());
				rowData[i][8] = item.getStatus();
				i++;
			}
			return rowData;
		}
		
		protected void requestItems(Integer sellerID, Category cat, Integer itemID, Date createdSince, boolean includeSold){
			comms.sendMessage(new MsgToServer.requestItems(userID, sellerID, cat, itemID, createdSince, includeSold));
		}
		
		/**
		 * takes in user's search criteria and updates the table appropriatley, 
		 * this sends requests to server, recieves response, and calls DrawTable()
		 * 
		 * @param sellerID
		 * @param cat
		 * @param itemID
		 * @param createdSince
		 * @param includeSold
		 */
		protected void updateTable(Integer sellerID, Category cat, Integer itemID, Date createdSince, boolean includeSold){
			
			requestItems(sellerID, cat, itemID, createdSince, includeSold);
			MsgToClient.Items msg = blockingRecieveMsg(MsgToClient.Items.class);
			Collections.sort(msg.items);//newest on top
			
			Object[][] rowData = generateRowData(msg);
			
			if (itemsPane != null){
				itemsPane.removeAll();
				this.remove(itemsPane);
				validate();
			}
			table = null;
			itemsPane = null;
			table = drawTable(rowData, msg.allItemIDs, msg.allSellers);
			this.add(itemsPane = new JScrollPane(table),BorderLayout.CENTER);
			repaint();	
		}

		@Override
		public void loadPanel() {
			id.addItem("All");
			seller.addItem("All");
			loadItems.doClick();
		}
		
	}
	
	/**
	 * page for submitting a new item for auction
	 *
	 */
	class SubmitPage extends JPanel implements DashboardPage{
		
		//fields for user input
		JTextField title;
		JComboBox<Category> category;
		JTextPane description;
		
		JSpinner start;
		JSpinner end;
		JFormattedTextField reserve;
		JFormattedTextField startPrice;
		JButton submit;
		
		JPanel left;
		JPanel right;
		
		public SubmitPage() {
			super();
			//formatting for currency fields
			NumberFormat format = NumberFormat.getCurrencyInstance(Locale.UK);
			format.setMaximumFractionDigits(2);
			NumberFormatter formatter = new NumberFormatter(format);

			this.title = new JTextField();
			title.setPreferredSize(new Dimension(300,28));
			
			this.category = new JComboBox<Category>();
			for(Category catgry : Category.values())
				category.addItem(catgry);
			category.setPreferredSize(new Dimension(300,28));
			
			this.description = new JTextPane();
			JScrollPane describeJSP = new JScrollPane(description);
			description.setPreferredSize(new Dimension(400,180));
			
			this.start = new JSpinner(new SpinnerDateModel());
			start.setEditor(new JSpinner.DateEditor(start, "dd/MM/yyyy HH:mm"));
			start.setPreferredSize(new Dimension(250,28));
			
			this.end = new JSpinner(new SpinnerDateModel());
			end.setEditor(new JSpinner.DateEditor(end, "dd/MM/yyyy HH:mm"));
			end.setPreferredSize(new Dimension(250,28));
			end.setValue(new Date(System.currentTimeMillis() + (7 * DAY_IN_MS)));
			
			this.reserve = new JFormattedTextField(formatter);
			reserve.setPreferredSize(new Dimension(300,28));
			reserve.setValue(1.00);
			
			this.startPrice = new JFormattedTextField(formatter);
			startPrice.setPreferredSize(new Dimension(300,28));
			startPrice.setValue(1.00);
			
			this.submit = new JButton("Submit Item");
			submit.setBackground(Color.yellow);
			submit.setMultiClickThreshhold(1000);
			
			//panel is split down the middle left/right to spread fields out
			JPanel left = new JPanel();
			FlowLayout fl;
			left.setLayout(fl = new FlowLayout());
			fl.setAlignment(FlowLayout.TRAILING);
			JPanel right = new JPanel();
			right.setLayout(fl = new FlowLayout());
			fl.setAlignment(FlowLayout.TRAILING);
			
			this.setLayout(new GridLayout(1,2));
			this.add(left);
			this.add(right);
			
			left.add(new JPanel(){{
				this.setLayout(new FlowLayout(FlowLayout.TRAILING));
				this.add(new JLabel("Item Title:	"));
				this.add(title);
			}});
			
			left.add(new JPanel(){{
				this.setLayout(new FlowLayout(FlowLayout.TRAILING));
				this.add(new JLabel("Select Category:	"));
				this.add(category);
			}});
			
			left.add(new JPanel(){{
				this.setLayout(new FlowLayout(FlowLayout.TRAILING));
				this.add(new JLabel("Item Description:	"));
				this.add(describeJSP);
			}});
			
			right.add(new JPanel(){{
				this.setLayout(new FlowLayout(FlowLayout.TRAILING));
				this.add(new JLabel("Start Date and Time:	"));
				this.add(start);
			}});
			
			right.add(new JPanel(){{
				this.setLayout(new FlowLayout(FlowLayout.TRAILING));
				this.add(new JLabel("End Date and Time:	"));
				this.add(end);
			}});
			
			right.add(new JPanel(){{
				this.setLayout(new FlowLayout(FlowLayout.TRAILING));
				this.add(new JLabel("Reserve:	"));
				this.add(reserve);
			}});
			
			right.add(new JPanel(){{
				this.setLayout(new FlowLayout(FlowLayout.TRAILING));
				this.add(new JLabel("Starting Price:	"));
				this.add(startPrice);
			}});
			right.add(new JPanel(){{
				this.setLayout(new FlowLayout(FlowLayout.TRAILING));
				this.add(new JLabel("                                           "));
				this.add(submit);
			}});
			
			submit.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					
					comms.sendMessage(new MsgToServer.SellItem(userID, title.getText(), description.getText(), (Category) category.getSelectedItem(), (Date) start.getValue(), (Date) end.getValue(), new Double(reserve.getValue().toString()), new Double(startPrice.getValue().toString())));
					blockingRecieveMsg(MsgToClient.submitItemOK.class);
				}
			});
		}

		@Override
		public void loadPanel() {
		}
		
	}
	
	/**
	 * essentially an items page but only shows items on which the user has bid
	 * the colour coding and status field is different to help the user keep track 
	 * of whether they are winning/ losing/ won / lost
	 */
	class BidsPage extends ItemsPage implements DashboardPage{
		
		//ensures that only items bid on by the user are returned by using MsgToServer.requestBidOnItems() message
		@Override
		protected void requestItems(Integer sellerID, Category cat, Integer itemID, Date createdSince, boolean includeSold) {
			comms.sendMessage(new MsgToServer.requestBidOnItems(userID, sellerID, cat, itemID, createdSince, includeSold));
		}


		public BidsPage(){
			super();
			sold.setSelected(true);//probably useful to see items previously won straight away
		}
	

		/*
		 * similar to superclass method but different values for status to help user keep track of bids
		 */
		@Override
		protected Object[][] generateRowData(MsgToClient.Items msg){
			
			Object[][] rowData = new Object[msg.items.size()][9];
			int i = 0;
			for(Item item : msg.items){
				rowData[i][0] = item.ID;
				rowData[i][1] = item.vendorID;
				rowData[i][2] = item.getCategory();
				rowData[i][3] = item.getTitle();
				rowData[i][4] = item.getDescription();
				rowData[i][5] = item.getStart();
				rowData[i][6] = item.getEnd();
				if (item.getHighestBid() != null){
					rowData[i][7] = new String("£" + item.getHighestBid().amount + " by user " + item.getHighestBid().userID);
					if (item.getHighestBid().userID == userID && item.getStatus().equals(Status.ONGOING))
						rowData[i][8] = "Winning";
					else if (item.getHighestBid().userID == userID && item.getStatus().equals(Status.SOLD))
						rowData[i][8] = "Won";
					else if (item.getStatus().equals(Status.ONGOING))
						rowData[i][8] = "Losing";
					else if (item.getStatus().equals(Status.SOLD))
						rowData[i][8] = "Lost";
					else
						rowData[i][8] = item.getStatus();
				}else{
					rowData[i][7] = new String("£" + item.getStartPrice());
					rowData[i][8] = item.getStatus();
				}
				i++;
			}
			return rowData;
		}
		
	}
	
	/**
	 * essentially items page but removes ability to choose seller and bid
	 * also overrides server items request to ensure responses are sold by user
	 */
	class MyItemsPage extends ItemsPage implements DashboardPage{
		public MyItemsPage() {
			super();
			seller.setVisible(false);
			sellerLabel.setVisible(false);
			bid.setVisible(false);
		}
		
		//ensures only seller items are requested
		@Override
		protected void updateTable(Integer sellerID, Category cat, Integer itemID, Date createdSince,
				boolean includeSold) {
			super.updateTable(userID, cat, itemID, createdSince, includeSold);
		}
		
	}
	
	class NotificationsPage extends JPanel implements DashboardPage{
		
		public NotificationsPage() {
			super();
			this.setLayout(new BorderLayout());
			
			this.refresh = new JButton("Refresh");
			
			this.add(new JPanel(){{
				this.setLayout(new FlowLayout(FlowLayout.TRAILING));
				this.add(refresh);
			}}, BorderLayout.PAGE_START);
			
			refresh.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					refresh();
				}
				
			});
		}

		JTable table;
		JButton refresh;
		JScrollPane tablePane = null;
		final String[] columnNames = {"Date", "Notification", "Content"};
		
		private void refresh(){	
			Collections.sort(notifications);//newest first

			Object[][] rowData = new Object[notifications.size()][3];
			int i = 0;
			for(Notification n : notifications){
				rowData[i][0] = n.date;
				rowData[i][1] = n.type;
				rowData[i][2] = n.content;
				i++;
			}
		
			if (tablePane != null){
				tablePane.removeAll();
				this.remove(tablePane);
				validate();
			}
			table = null;
			tablePane = null;
			
			table = new JTable(rowData,columnNames){
				@Override
				public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
					Component c = super.prepareRenderer(renderer, row, column);
					
					//  Colour each row based on "status"
					if (!isRowSelected(row)){
						c.setBackground(getBackground());
						int modelRow = convertRowIndexToModel(row);
						Object type;
						
						if(getModel().getValueAt(modelRow, 1) != null)
							type = getModel().getValueAt(modelRow, 1);
						else
							type = "";
						if (Notification.TYPE.SUCCESSFUL_BUY.equals(type) || Notification.TYPE.SUCCESSFUL_SALE.equals(type)) c.setBackground(Color.green);
						else if (Notification.TYPE.FAILED_SALE.equals(type)) c.setBackground(Color.pink);
						else if (Notification.TYPE.OUTBID.equals(type)) c.setBackground(Color.orange);
					}
					return c;
				}
			};
			table.setRowHeight(30);
			setJTableColumnsWidth(table,1300,15,15,70);
			table.getTableHeader().setBackground(Color.CYAN);
			this.add(tablePane = new JScrollPane(table),BorderLayout.CENTER);
			validate();
			repaint();
			
		}

		@Override
		public void loadPanel() {
			refresh.doClick();
		}
	}
	
	private void logout(){
		if (JOptionPane.showConfirmDialog(new JFrame(), 
	            "Are you sure you wish to quit and log out?", "Really Closing?", 
	            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
	        		comms.sendMessage(new MsgToServer.EndSession(userID));
	        		System.exit(0);
				}
	}
	public void init(){
		
		contentPane = new JPanel(new CardLayout());
		this.add(contentPane);
		this.setContentPane(contentPane);

		contentPane.add(new LoginPanel(), "login");
		contentPane.add(new Dashboard(), "dashboard");
		this.setSize(300,240);
		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        logout();
		    }
		});
		
		/*
		 * this thread only runs when an event is not being handled, it is used to check for notifications
		 */
		while (true){
			try {
				Thread.sleep(600);
				SwingUtilities.invokeAndWait(this);
			} catch (InterruptedException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param args1 - path to directory containing Comms folder (not the folder itself)
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Client c = new Client();
		c.init();
	}

	/* 
	 * this thread constantly checks for notifications. 
	 * it is never be running while the client is communicating with the server
	 * it should not come across anything other than notifications, 
	 * it has to wait for user events to be handled, before executing
	 */
	@Override
	public void run() {
		Message msg = comms.recieveMessage();	
		if (msg != null){
			if (msg instanceof Notification){
				notifications.add((Notification)msg);
				JOptionPane.showMessageDialog(new JFrame("Notification"), msg.toString() + "\n(see notifications tab)");
			}else
				throw new NullPointerException("client recieved unexpected message instead of notification");
		}
	}

}
