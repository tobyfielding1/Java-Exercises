

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * messages are expected to be created, read, and thrown away. This enables use of final fields
 * use of final fields where possible makes things fast, safe and succinct
 * the constructors are specified to make communication foolproof; if the parameters aren't right, no message created;
 */
public abstract class MsgToClient extends Message {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5488081732351773299L;

	public static enum Status{NOT_REGISTERED,WRONG_PWD};//useful for telling client why login failed

	
	protected MsgToClient(Integer userID){
		this.userID = userID;
	}
	
	static class LoginResponse extends MsgToClient{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		Collection<Notification> notifications = new ArrayList<Notification>();
		Status status;
		/** failed login
		 * @param ClientID
		 */
		public LoginResponse(int ClientID, Status status){
			super(null);
			this.clientID = ClientID;
			this.status = status;
		}
		
		
		/**
		 * @param clientID
		 * @param userID
		 * @param collection
		 */
		public LoginResponse(int clientID, int userID, Collection<Notification> collection){
			super(userID);
			this.notifications = collection;
			this.clientID = clientID;
		}
		
		/**
		 * @param clientID
		 * @param userID
		 */
		public LoginResponse(int clientID, int userID){
			super(userID);
			this.clientID = clientID;
		}
	}	
	
	static class submitItemOK extends MsgToClient{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public submitItemOK(Integer userID){
			super(userID);
		}
	}
	
	static class Items extends MsgToClient{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * @param userID
		 * @param items
		 * @param allSellers
		 * @param allItems
		 */
		public Items(Integer userID, List<Item> items, Set<Integer> allSellers, Set<Integer> allItems) {
			super(userID);
			this.items = items;
			this.allSellers = allSellers;
			this.allItemIDs = allItems;
		}
		final List<Item> items;
		final Set<Integer> allSellers;
		final Set<Integer> allItemIDs;	
	}	
	
	
	

}
