import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author tobyf server side class to be extended by source. will keep a record
 *         of subscribed clients, and hold notifications in memory if client
 *         cannot be reached. Notifications will be sent when client with same
 *         ID re-subscribes.
 */
public abstract class Source extends UnicastRemoteObject implements SourceInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final String ID;

	private Map<String, Collection<Notification>> unsent = new HashMap<String, Collection<Notification>>();
	/**
	 * record of all subscribed clients and their ID's
	 */
	private Map<String, SinkInterface> subscribedSinks = new HashMap<String, SinkInterface>();

	public Collection getSinkIDs() {
		return subscribedSinks.keySet();
	}

	public Source(String ID) throws RemoteException, MalformedURLException {
		super();
		this.ID = ID;
		Naming.rebind(ID, this);// registers the source with the RMI registry at
								// the machine's IP under its ID
		System.err.println("PagerService " + ID + " ready");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see NotificationSourceInterface#addNotificationMonitor(
	 * NotificationSinkInterface, java.lang.String) registers sink with source.
	 * Sends saved unsent notifications to it.
	 */
	@Override
	public void subscribe(SinkInterface pm, String sinkID) throws RemoteException {
		synchronized (subscribedSinks) {
			if (subscribedSinks.containsKey(sinkID))
				throw new RemoteException(
						"***********\n\n there is already a subscriber with this name; use a different one \n\n**************\n\n");

			subscribedSinks.put(sinkID, pm);
			System.err.println("notification monitor added: " + sinkID);
			if (unsent.containsKey(sinkID)) {
				System.err.println("client connection resumed, commensing resend");
				for (Notification n : unsent.get(sinkID)) {
					send(n, sinkID);
				}
				unsent.remove(sinkID);
			}
		}
	}

	@Override
	public void unSubscribe(String sinkID) throws RemoteException {
		synchronized (subscribedSinks) {
			subscribedSinks.remove(sinkID);
			System.err.println("notification monitor removed");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see NotificationSourceInterface#connectionAlive() used by client to
	 * check connection
	 */
	@Override
	public boolean connectionAlive() throws RemoteException {
		return true;
	}

	/**
	 * @param n
	 *            Notification
	 * @param recipient
	 *            The ID of the sink as it was when it subscribed.
	 */
	protected void send(Notification n, String recipient) {
		synchronized (subscribedSinks) {
			if (recipient.equals("all")) {
				Iterator<String> it = unsent.keySet().iterator();
				while (it.hasNext())
					send(n, it.next());
				for (String key : subscribedSinks.keySet())
					send(n, key);
			} else {
				try {
					subscribedSinks.get(recipient).notification(n);
				} catch (RemoteException | NullPointerException e) {
					System.err.println(
							"connection appears to have been lost with a client, will resend once connection returns");
					Collection<Notification> fails = new ArrayList<Notification>();
					fails.add(n);
					if (unsent.containsKey(recipient))
						fails.addAll(unsent.get(recipient));
					unsent.put(recipient, fails);
					subscribedSinks.remove(recipient);
				}
			}
		}

	}

}
