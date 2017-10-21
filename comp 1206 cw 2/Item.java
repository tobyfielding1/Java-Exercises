

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Item implements Serializable, Comparable<Item>{

	private static final long serialVersionUID = -3260486538259646613L;
	
	/**
	 * @param title
	 * @param description
	 * @param category
	 * @param start
	 * @param end
	 * @param reserve
	 * @param iD
	 * @param vendorID
	 * @param startPrice
	 */
	public Item(String title, String description, Category category, Date start, Date end, double reserve, int iD, int vendorID, double startPrice) {
		this.title = title;
		this.description = description;
		this.category = category;
		this.start = start;
		this.end = end;
		this.reserve = reserve;
		ID = iD;
		this.vendorID = vendorID;
		this.startPrice = startPrice;
		bids = new HashMap<Integer, Bid>();
		highestBid = null;
		this.status = Status.NOT_STARTED;
	}
	private String title;
	private String description;
	private Category category;
	private Date start;
	private Date end;
	private double reserve;
	private double startPrice;
	private Status status;
	private Map<Integer,Bid> bids;
	private Bid highestBid;
	
	final int ID;
	final int vendorID;
	
	public double getStartPrice() {
		return startPrice;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	/*
	 * it is unclear how much synchronisation is required as this object will be stored in concurrentHashMap,
	 * which has minimal (and dubious) internal synchronisation (does the "lock" on Map elements extend to methods of those elements?)
	 * however i chose to be safe rather than sorry and there is no performance cost over what I would expect concurrentHashMap to do anyway, so i used locking 
	 */
	public void addBid(Bid bid) {
		synchronized (bids){
			if (highestBid != null){
				if (bid.amount > highestBid.amount){
					bids.put(bid.userID,bid);
					highestBid = bid;
				}
			}else if (bid.amount > startPrice){
				bids.put(bid.userID,bid);
				highestBid = bid;
			}else
				throw new IllegalArgumentException("bid cannot be added, not above reserve/highest, despite checks");
		}
	}


	public String getTitle() {
		return title;
	}


	public String getDescription() {
		return description;
	}


	public Category getCategory() {
		return category;
	}


	public Date getStart() {
		return start;
	}


	public Date getEnd() {
		return end;
	}


	public double getReserve() {
		return reserve;
	}


	public Status getStatus() {
		return status;
	}

	public boolean bidOnBy(int UID) {
		synchronized (bids){
			return bids.containsKey(UID);
		}
	}
	
	
	public Bid getHighestBid(){
		synchronized (bids){
			if (bids.isEmpty())
				return null;
			else
				return highestBid;
		}
	}
	
	public int compareTo(Item arg1) {
		if(this.start.before(arg1.start))
			return 1;
		else
			return -1;
	}
	
}
