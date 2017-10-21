

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.crypto.NoSuchPaddingException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


/**
 * server uses default constructor, and go() method must be called after de-serializing/initializing
 *
 * Several threads act on the server:
 * 1 x persistence thread (in dedicated class) periodically saves state
 * 1 x timekeeping thread (the runnable method of this class) to start/end auctions etc
 * 1 x message reading thread (the main thread, so server.go() must be the last call in the main method as it contains an infinite loop)
 * n x message processing/writing threads (Worker Thread inner class) in a variable sized thread pool.
 */
public class Server implements Serializable, Runnable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3029622608852377557L;
	
	private Map<Integer,User> users = new ConcurrentHashMap<Integer,User>();//concurrent hashMaps are much faster than loads of locking. Sometimes return out of date data, but that could happen anyway 
	private Map<Integer,Item> items = new ConcurrentHashMap<Integer,Item>();//particularly useful for speeding up iterating (which a server does a lot)
	private Map<Integer,Item> finishedItems = new ConcurrentHashMap<Integer,Item>();//ie. Status == SOLD || FAILED
	
	private static final Logger logger = Logger.getLogger("Server");
	//holds logs in RAM to keep persistence thread simple and store log with server
	//specifies fixed size of 200 log records because memory is finite after all
	//oldest are overwritten first
	private Collection<String> logList = new CircularArrayRing<String>(200);	
	
	transient private ServerComms comms;
	transient private List<Integer> loggedInUsers;
	
	//this method ensures that when items are deleted; their ID's can be used again
	private synchronized int generateItemID(){
		int i;
		for(i = 1; items.containsKey(i) || finishedItems.containsKey(i);i++);
		return i;
	}
	//this method ensures that when users are deleted; their ID's can be used again
	private synchronized int generateUserID(){
		int i;
		for(i = 1; users.containsKey(i);i++);
		return i;
	}
	
	public void go(){
		this.go("");
	}
	
	private void initLogger(){
		
		logger.addHandler(new Handler() {
		      public void publish(LogRecord logRecord) {
		        String log = new Date() + ": " + logRecord.getSourceClassName() + " " + logRecord.getSourceMethodName() + "\n" + 
		        logRecord.getLevel() + ": " + logRecord.getMessage() + "\n";
		    	logList.add(log);
		      }

		      public void flush() {}
		      public void close() {}
		    });
		
		logger.setLevel(Level.ALL);
	}
	
	/**
	 * @param dirToPutCommsFolderIn
	 */
	public void go(String dirToPutCommsFolderIn){
		
		initLogger();
		
		new ServerGUI();
		
		comms = new ServerComms(dirToPutCommsFolderIn);
		loggedInUsers = Collections.synchronizedList(new ArrayList<Integer>());//synchronized for obvious reasons
		
		ExecutorService executor = Executors.newCachedThreadPool();
		MsgToServer msg;
		
		/* 
		 * this thread(the main thread) constantly reads in messages 
		* and passes them to a collection of worker threads for processing
		* use of thread pool avoids thread creation overhead
		* multithreading with concurrent hashmaps should be very fast for multiple clients
		*/
		new Thread(this).start();
		logger.info("Server Started - OK");
		while(true){
			while((msg = (MsgToServer) comms.recieveMessage()) == null){try {Thread.sleep(2);} catch (InterruptedException e) {e.printStackTrace();}};
			Runnable worker = new WorkerThread(msg);
			executor.execute(worker);
		}
		
	}

	private class WorkerThread implements Runnable {
	    private MsgToServer message;
	    
	    public WorkerThread(MsgToServer msg){
	    	this.message= msg;
	    }
	    
	    /* (non-Javadoc)
	     * @see java.lang.Runnable#run()
	     */
	    @Override
	    public void run(){
	    	try{
		        if (message instanceof MsgToServer.Register){MsgToServer.Register msg = (MsgToServer.Register)message;
		        	
		        	Integer UID = generateUserID();
		        	users.put(UID, new User(msg.first, msg.family, UID, msg.pwd));
		        	comms.sendMessage(new MsgToClient.LoginResponse(msg.clientID, UID));
		        	loggedInUsers.add(UID);
		        	note(new Notification(UID, Notification.TYPE.WELCOME, "Welcome to Notbay " + msg.first));
		        	logger.fine("new user added " + UID + " ");
		        }
		        else if(message instanceof MsgToServer.Login){MsgToServer.Login msg = (MsgToServer.Login)message;
		        	
		        	if(!users.containsKey(msg.userID)){
		        		comms.sendMessage(new MsgToClient.LoginResponse(msg.clientID, MsgToClient.Status.NOT_REGISTERED));
		        		logger.fine("user login not recognised " + msg.userID);
		        		
		        	}else if (!users.get(msg.userID).getPassword().equals(msg.pwd)){
		        		comms.sendMessage(new MsgToClient.LoginResponse(msg.clientID, MsgToClient.Status.WRONG_PWD));
		        		logger.fine("user password not recognised " + msg.userID);
		        		
		        	}else{
		        		loggedInUsers.add(msg.userID);
		        		comms.sendMessage(new MsgToClient.LoginResponse(msg.clientID, msg.userID, users.get(msg.userID).getNotifications()));
		        		note(new Notification(msg.userID, Notification.TYPE.WELCOME, "Welcome back :) this page will show you what has happened while you were away"));
		        		logger.fine("user logged in OK " + msg.userID);
		        	}
		        }
		      //user must be logged in to do anything other than login/register
		        else if(loggedInUsers.contains(message.userID)){
		        	
			        if(message instanceof MsgToServer.EndSession){MsgToServer.EndSession msg = (MsgToServer.EndSession)message;
			        	loggedInUsers.remove(msg.userID);
			        	logger.fine("user logged out " + msg.userID);
			        }
			        else if(message instanceof MsgToServer.SellItem){MsgToServer.SellItem msg = (MsgToServer.SellItem)message;
			        	Integer ID = generateItemID();
			        	items.put(ID, new Item(msg.title, msg.description, msg.cat, msg.start, msg.end, msg.reserve, ID, msg.userID, msg.startPrice));
			        	comms.sendMessage(new MsgToClient.submitItemOK(msg.userID));
			        	Thread.sleep(150);//waits for client to process message before sending notification. not essential, but cleaner user experience
			        	note(new Notification(msg.userID, Notification.TYPE.AUCTION_CREATED, "your auction of '" + msg.title + "' has successfully been created"));
			        	logger.fine("new item for sale " + ID);
			        }
			        else if(message instanceof MsgToServer.requestBidOnItems){MsgToServer.requestBidOnItems msg = (MsgToServer.requestBidOnItems)message;
			        	List<Item> matched = new ArrayList<Item>();
			        	for(Item item : searchItems(msg)){
				        	if(item.bidOnBy(msg.userID))
				        		matched.add(item);
				        }
			        	comms.sendMessage(new MsgToClient.Items(msg.userID, matched, users.keySet(), items.keySet()));
			        }
			        
			        else if(message instanceof MsgToServer.requestItems){MsgToServer.requestItems msg = (MsgToServer.requestItems)message;
		        		comms.sendMessage(new MsgToClient.Items(msg.userID, searchItems(msg), users.keySet(), items.keySet()));
			        }
			        
			        else if(message instanceof MsgToServer.PlaceBid){MsgToServer.PlaceBid msg = (MsgToServer.PlaceBid)message;
			        	Bid highestBid = items.get(msg.bid.itemID).getHighestBid();
			        	items.get(msg.bid.itemID).addBid(msg.bid);
			        	//notifies prev highest bidder of being outbid
			        	if(highestBid != null)
			        		note(new Notification(highestBid.userID,Notification.TYPE.OUTBID,"you are being outbid on an item: '" + items.get(msg.bid.itemID).getTitle()+"', you may want to bid again"));
			        	logger.fine("new bid by " + msg.userID + " on item " + msg.bid.itemID);
			        }
			        
		        }
	    	}catch(Exception e){
	    		logger.log(Level.SEVERE, e.toString(), e );
			}    
	    }
	    private List<Item> searchItems(MsgToServer.requestItems msg){
	    	if (msg.includeSold){
	    		List<Item> matched = searchItems(msg, items);
	    		matched.addAll(searchItems(msg, finishedItems));
	    		return matched;
	    	}else	
	    		return searchItems(msg, items);
	    }
	
	    private List<Item> searchItems(MsgToServer.requestItems msg, Map<Integer,Item> map){
			List<Item> matched = new ArrayList<Item>();
	    	if (msg.itemID != null){
	    		matched.add(map.get(msg.itemID));
	    		return matched;
	    	}

	    	for(Item item : map.values()){
	
	    		if(msg.sellerID != null){
					if(msg.sellerID.equals(item.vendorID) && msg.createdSince.before(item.getStart()) && (msg.cat == Category.ALL || msg.cat == item.getCategory()))
							matched.add(item);
				}else if(msg.createdSince.before(item.getStart()) && (msg.cat == Category.ALL || msg.cat == item.getCategory()))
					matched.add(item);
			}
	    	return matched;
	    }
	}
	
	/**
	 * saves notification to user field and sends message to 
	 * client immediatley if they're logged in
	 */
	private void note(Notification n){
		users.get(n.userID).addNotification(n);
		if (loggedInUsers.contains(n.userID))
			comms.sendMessage(n);
	}
	
	private void closeUnsuccessfulItem(Item item){
		item.setStatus(Status.CLOSED);
		note(new Notification(item.vendorID, Notification.TYPE.FAILED_SALE, "your item '"+item.ID + "' has closed but failed to sell :( "));
		logger.fine("item " + item.ID + " closed and failed to sell ");
		if(item.getHighestBid() != null)
			note(new Notification(item.getHighestBid().userID, Notification.TYPE.FAILED_BUY, "the item you bid on '"+item.ID + "' has closed but bidding failed reach the reserve price "));
	}
	
	/* 
	 * timekeeping thread
	 * periodically checks all items to see if their start and end time has been reached
	 * then takes appropriate action and sends notifications etc.
	 */
	@Override
	public void run() {
		while (true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, e.toString(), e );
			}
			
			Date now = new Date();
			
			for (Item item : items.values()){
				
				if (item.getStatus() == Status.NOT_STARTED && item.getStart().before(now))
					item.setStatus(Status.ONGOING);
				
				else if (item.getStatus() == Status.ONGOING && item.getEnd().before(now)){
					
					if(item.getHighestBid() != null){
						Bid winningBid = item.getHighestBid();
						if(winningBid.amount > item.getReserve()){
							item.setStatus(Status.SOLD);
							
							note(new Notification(winningBid.userID, Notification.TYPE.SUCCESSFUL_BUY, "you have won item '"+item.ID + "' from seller '"+item.vendorID));
							note(new Notification(item.vendorID, Notification.TYPE.SUCCESSFUL_SALE, "you have sold item '"+item.ID + "' to buyer '"+item.getHighestBid().userID));
							
							users.get(winningBid.userID).addWonItem(winningBid.itemID);
							logger.fine("item " + item.ID + "sold successfully " );	
						}
						else
							closeUnsuccessfulItem(item);	
					}
					else
						closeUnsuccessfulItem(item);
					
					finishedItems.put(item.ID, item);
					items.remove(item.ID);
				}
			}
			
		}

	}
	
	public class ServerGUI extends JFrame{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ServerGUI() {
			JPanel contentPane = new JPanel(new CardLayout());
			this.add(contentPane);
			this.setContentPane(contentPane);
			contentPane.setLayout(new BorderLayout());
			
			JButton load = new JButton("Load Logs");
			JTextArea logText = new JTextArea();
			JTextArea salesText = new JTextArea();
			JScrollPane logJSP = new JScrollPane(logText);
			JScrollPane salesJSP = new JScrollPane(salesText);
			JRadioButton fineLevel = new JRadioButton("Exclude Fine Level Future Records");
			
			this.add(new JPanel(){
				private static final long serialVersionUID = 1L;
			{
				this.setLayout(new GridLayout(1,4));
				this.add(new JLabel("Notbay Server Log:		 "));
				this.add(fineLevel);
				this.add(new JLabel("Users and Purchase History:"));
				this.add(load);
				
			}}, BorderLayout.PAGE_START);
			
			this.add(new JPanel(){
				private static final long serialVersionUID = 1L;
			{
				this.setLayout(new GridLayout(1,2));
				this.add(logJSP);
				this.add(salesJSP);
			}}, BorderLayout.CENTER);
			
			this.setSize(1000,500);
			this.setVisible(true);
			
			logger.info(" server gui started - OK");
			load.doClick();
			
			fineLevel.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					if(fineLevel.isSelected())
						logger.setLevel(Level.INFO);
					else
						logger.setLevel(Level.ALL);
				}
				
			});
			
			load.addActionListener(new ActionListener(){
				
				@Override
				public void actionPerformed(ActionEvent e) {
					logText.setText("");
					salesText.setText("");
					for(String s : logList)
						logText.append(s);
					for(User usr : users.values()){
						if (usr.getItemsWon().isEmpty())
							salesText.append(usr.firstName + " " + usr.lastName + ": - no wins\n");
						else{
							String wins = "";
							for (Integer itemID : usr.getItemsWon())
								wins = wins + finishedItems.get(itemID).getTitle() + " ,";
							salesText.append(usr.firstName + usr.lastName + ": - has won: " + wins + "\n");
						}
					}
				}
			});
		}
	}

	
	public static void main(String[] args) {
		
		Server server = new Server();
		
		//tries to read in server state from file if present. file must be in same Dir 
		String path = "Notbay_State.bin";
		File file = new File(path);
		Encryption encryption = new Encryption();
		if (file.exists()){
			FileInputStream fin;
			try {
				fin = new FileInputStream(path);
				ObjectInputStream ois = new ObjectInputStream(fin);	
				server =(Server)encryption.decrypt(ois);
				if (server == null)
					throw new IOException("failed to read previous server state");
			}catch (IOException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
				System.err.println("you are trying to read the server from a file that is probably a past version");
				System.err.println("creating a new server of correct version, with no data");
				server = new Server();
			}
		}
		
		PersistenceThread persistence = new PersistenceThread(server,path,3000);
		
		//ensures that a final log and save is conducted on shutdown
		Runtime.getRuntime().addShutdownHook(new Thread(){           
			public void run() {
				logger.info("Server Shut Down ");  ;
				System.out.println("hey");
				persistence.interrupt();
				persistence.setSleepTime(0);
				persistence.run();
			}     
		});

		persistence.start();
		server.go();
	}

	

}
