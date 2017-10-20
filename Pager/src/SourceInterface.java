
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author tobyf. used by sink to register with source.
 */
public interface SourceInterface extends Remote {

	public void subscribe(SinkInterface tm, String sinkID) throws RemoteException;

	/**
	 * checks that sink can still access source. ensures recipient knows if they
	 * may be missing notifiations.
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public boolean connectionAlive() throws RemoteException;

	public void unSubscribe(String sinkID) throws RemoteException;
}
