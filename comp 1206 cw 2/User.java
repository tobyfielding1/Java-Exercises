

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5531145995539150412L;

	/**
	 * @param firstName
	 * @param lastName
	 * @param iD
	 * @param password
	 */
	public User(String firstName, String lastName, int iD, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		ID = iD;
		this.password = password;	
		wonItems = new ArrayList<Integer>();
	}
	
	final String firstName;
	final String lastName;
	private String password;
	private List<Integer> wonItems;
	private CircularArrayRing<Notification> notifications = new CircularArrayRing<Notification>(20);
	final int ID;
	
	public List<Integer> getItemsWon() {
		synchronized (wonItems){
			return wonItems;
		}
	}
	
	/**
	 * @param itemID
	 */
	public void addWonItem(int itemID){
		synchronized (wonItems){
			wonItems.add(itemID);
		}
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String oldPassword, String newPassword) {
		if (password.equals(oldPassword))
			this.password = newPassword;
	}

	
	public Collection<Notification> getNotifications() {
		synchronized (notifications){
			return notifications;
		}
	}
	
	public void addNotification(Notification notification) {
		synchronized (notifications){
			this.notifications.add(notification);
		}
	}
	
	

}
