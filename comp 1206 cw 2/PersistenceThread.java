

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

public class PersistenceThread extends Thread {
	private Encryption encryption;
	private Server server;
	private String path;//where to save to
	private int delayBetweenSaves;//in milliseconds

	public PersistenceThread(Server s, String path, int sleepTime) {
		encryption = new Encryption();
		this.server = s;
		this.path = path;
		this.delayBetweenSaves = sleepTime;
	}
	
	//how long between saves
	public void setSleepTime(int sleepTime) {
		this.delayBetweenSaves = sleepTime;
	}

	//repeatedly saves encrypted state of server to file 
	@Override
    public void run() {
    	while(true){
			try {
				Thread.sleep(delayBetweenSaves);
				FileOutputStream fOut = new FileOutputStream(path);
				ObjectOutputStream oOut = new ObjectOutputStream (fOut);
				encryption.encrypt(server, oOut);
			} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }

}
