

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

public abstract class Comms {
	protected String commsPath;
	protected Encryption encryption;
	
	/**
	 * @param pathToCommsFileDirectory
	 */
	public Comms(String path) {
		File commsFile = new File(path + "Comms");
		commsPath = commsFile.getAbsolutePath();
		encryption = new Encryption();
	}
	
	protected void makeFolder(File dir){
		if(dir.exists())
			purgeDirectory(dir);
		else
			dir.mkdir();
	}
	
	//empties directory so it can be deleted after client logout (used recursivley)
	private void purgeDirectory(File dir) {
	    for (File file: dir.listFiles()) {
	        if (file.isDirectory()) purgeDirectory(file);
	        file.delete();
	    }
	}
	
	protected void sendMessage(Message m, String filePath, byte[] cypherKey){
		try {
			FileOutputStream fOut = new FileOutputStream(filePath+"_"+encryption.randomString(5)+".bin");
			ObjectOutputStream oOut = new ObjectOutputStream (fOut);
			encryption.encrypt(m, oOut,cypherKey);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IOException e) {
			e.printStackTrace();
		}
	}
	
	//when the decryption key is not specified, previous key is used
	protected Message recieveMessage(String path){
		return recieveMessage(path,null);
	}
	
	protected Message recieveMessage(String path, byte[] key){
		try {
			Thread.sleep(7);
			FileInputStream fin = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fin);	
			Message msg;
			//premature read of file often causes failure(ie. write not yet finished)
			//so process is repeated until clean read achieved
			if((msg =(Message)encryption.decrypt(ois, key)) == null){
				Thread.sleep(5);
				ois.close();
				fin.close();
				return recieveMessage(path, key);
			}
			File toDelete = new File(path);
			while(toDelete.exists()){
				Thread.sleep(3);
				toDelete.delete();
			}
			return msg;
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
