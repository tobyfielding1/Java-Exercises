import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Date;

public class PageSource extends Source {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 *            single argument: The name given to the page source, which will
	 *            be used by pagers to access it. (ie. "PagerService-1")
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		PageSource ps = new PageSource(args[0]);

		ps.inputPages();// waits for user to input messages and recipients

	}

	public void inputPages() throws IOException {

		while (true) {
			System.out.println("type pager message here: ");
			String m = new BufferedReader(new InputStreamReader(System.in)).readLine();
			System.out.println("type recipient name or 'all' to send to all subscribed recipients");
			String recipient = new BufferedReader(new InputStreamReader(System.in)).readLine();

			send(new Page(new Date(), m, this.ID), recipient);
		}
	}

	public PageSource(String nme) throws RemoteException, MalformedURLException {
		super(nme);
	}

}
