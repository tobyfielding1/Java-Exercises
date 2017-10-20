import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * @author tobyf Client side class intended to be extended by client application
 *         Key exposed methods are subscribe() and unsubscribe(). Able to
 *         reconnect with source if connection lost.
 *
 */
public abstract class Sink extends UnicastRemoteObject implements SinkInterface, Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final String ID;
	/**
	 * stores all connections with sources
	 */
	private ArrayList<Connection> connections = new ArrayList<Connection>();

	private class Connection {
		SourceInterface stub;
		String host;
		String sourceID;

		public Connection(SourceInterface stub, String host, String sourceID) {
			super();
			this.stub = stub;
			this.host = host;
			this.sourceID = sourceID;
		}
	}

	public Sink(String name) throws RemoteException {
		this.ID = name;
	}

	/**
	 * used for when source is on localhost
	 * 
	 * @param instanceName
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public void subscribe(String instanceName) throws MalformedURLException, RemoteException, NotBoundException {
		subscribe("localhost", instanceName);
	}

	/**
	 * @param host
	 *            ie. localhost
	 * @param instanceName
	 *            ie. source-1
	 * @throws NotBoundException
	 * @throws RemoteException
	 * @throws MalformedURLException
	 */
	public void subscribe(String host, String instanceName)
			throws MalformedURLException, RemoteException, NotBoundException {

		SourceInterface stub = null;
		stub = (SourceInterface) Naming.lookup("rmi://" + host + ":" + 1099 + "/" + instanceName);// rmi
																									// always
																									// uses
																									// 1099
		stub.subscribe(this, this.ID);// registers sink with source
		connections.add(new Connection(stub, host, instanceName));
	}

	public void unSubscribe(Connection c) throws Exception {
		c.stub.unSubscribe(this.ID);
		connections.remove(c);
	}

	/**
	 * will cycle through all connections and check the server is live, will
	 * remove lost connections and try to create new one with same source ID.
	 * 
	 * @throws InterruptedException
	 */
	private void keepAlive() throws InterruptedException {
		Connection ctn = null;
		try {
			for (Connection c : connections) {
				ctn = c;
				if (!c.stub.connectionAlive())
					throw new UnmarshalException("connection lost");
			}
		} catch (RemoteException e) {
			System.err.println("connection lost, attempting to reconnect");
			Thread.sleep(1000);
			connections.remove(ctn);
			try {
				subscribe(ctn.host, ctn.sourceID);
				System.err.println("***connection re-established***");
			} catch (Exception e1) {
				connections.add(ctn);
			}
		}
	}

	/*
	 * allows subclass to run the keepAlive() process in the background if they
	 * wish (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(3000);
				keepAlive();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
