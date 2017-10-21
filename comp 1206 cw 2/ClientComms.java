

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * same as comms class, but attaches client id to every message
 * sets up new encryption key on first message to server (more secure than hard coded key)
 * sets up client comms folder 
 * uses free port to generate unique client id (no two clients on this pc can share a port)
 */
public class ClientComms extends Comms {
	final int clientID;
	boolean firstMessage;
	
	public ClientComms(String path) throws IOException {
		super(path);
		clientID = new ServerSocket(0).getLocalPort();//guaranteed unique on this pc
		firstMessage = true;
	}
	public ClientComms() throws IOException {
		this("");	
	}

	public void sendMessage(MsgToServer m) {
		String oldPath = commsPath;
		byte[] oldKey = encryption.getKey();
		
		if (firstMessage){
			m.setKey(encryption.newKey().getBytes());//refreshes encryption key for security
			commsPath = new String(commsPath +"/"+ Integer.toString(clientID));
			makeFolder(new File(commsPath));
			firstMessage = false;
		}
		m.clientID = clientID;//attaches client ID to message
		
		super.sendMessage(m, oldPath + "/ToS",oldKey);
	}

	public Message recieveMessage() {
		
		File[] fileList = new File(commsPath).listFiles();
		if (fileList == null)
			return null;
		
		String chosenFile = null;
		for (File file : fileList){
			if (file.getName().contains("ToC")){//ToC refers to "meant for client to read"
				chosenFile = file.getAbsolutePath();
				break;	
			}
		}
		if(chosenFile == null){return null;}
		return super.recieveMessage(chosenFile);
	}
	
	

}
