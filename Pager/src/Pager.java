import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

public class Pager extends Sink {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Page> pages = new ArrayList<Page>();

	/**
	 * @param args
	 *            arg[0]: the name of the pager (ie 'toby's bleeper'). Repeated
	 *            args: arg[n1]: the address of the pagerserver (ie.
	 *            'localhost'). arg[n2]: The name of the pager source (ie.
	 *            'PagerService-1') no upper limit to the number of pairs of n1
	 *            and n2.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Pager pr = new Pager(args[0]);
		int i = 1;
		while (i < args.length && (args.length & 1) != 0)
			pr.subscribe(args[i++], args[i++]);

		pr.run();// superclass' keep alive process. No need to create a separate
					// thread as this class doesn't do anything concurrently
	}

	public Pager(String name) throws RemoteException {
		super(name);
	}

	@SuppressWarnings("unchecked")
	public Collection<Page> getPages() {
		synchronized (pages) {
			return (Collection<Page>) pages.clone();
		}
	}

	/*
	 * fired when new notification comes in. Will always run in seperate thread.
	 * hence the synchronization. (non-Javadoc)
	 * 
	 * @see NotificationSinkInterface#notification(Notification)
	 */
	@Override
	public void notification(Notification n) {
		synchronized (pages) {
			Page p = (Page) n;
			pages.add(p);
			System.out.println("***************\n" + ID + " recieved a new page from: " + p.Sender + " on:\n"
					+ p.date.toString() + ".\nMessage Reads:\n" + p.message + "\n****************");
		}

	}

}
