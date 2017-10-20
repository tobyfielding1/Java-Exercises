
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author tobyf allows source to talk to sink
 */
public interface SinkInterface extends Remote {

	public void notification(Notification p) throws RemoteException;
}