

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ServerComms extends Comms{
	//maps client to encryption key
	private Map<Integer,byte[]> clientKeyLookup = new ConcurrentHashMap<Integer,byte[]>();
	// maps user to client ID number 
	private Map<Integer,Integer> userClientLookup = new ConcurrentHashMap<Integer,Integer>();
	
	public ServerComms() {
		this("");
	}
	
	//removes client comms folder 
	private void terminateSession(Integer clientID, Integer userID){
		new File(commsPath +"/"+ Integer.toString(clientID)).delete();
		clientKeyLookup.remove(clientID);
		userClientLookup.remove(userID);
	}
	
	public ServerComms(String path) {
		super(path);
		makeFolder(new File(commsPath));
	}
	
	public void sendMessage(MsgToClient m) {
		if (m.clientID == null)
			m.clientID = userClientLookup.get(m.userID);
		else if(m.userID != null)
			userClientLookup.put(m.userID, m.clientID);// adds to UserID lookup for first message to client after login
		super.sendMessage(m, commsPath +"/"+ Integer.toString(m.clientID)+ "/ToC",clientKeyLookup.get(m.clientID));
	}

	public Message recieveMessage(){
		File[] fileList;
		for (Integer client : clientKeyLookup.keySet()){//goes through each client folder
			
			String folderPath = new String(commsPath +"/"+client.toString());
			fileList = new File(folderPath).listFiles();
				
			for (File file : fileList){
				if (file.getName().contains("ToS")){
					MsgToServer message = (MsgToServer)super.recieveMessage(file.getAbsolutePath(),clientKeyLookup.get(client));
					if (message != null){
						if (message instanceof MsgToServer.EndSession)
							this.terminateSession(message.clientID, message.userID);
						return message; 	
					}
				}	
			}
		}//looks for new messages from clients without a client folder yet(ie. login. register)
		fileList = new File(commsPath).listFiles();
		MsgToServer msg;
		for (File file : fileList){
			if (file.getName().contains("ToS")){
				msg = (MsgToServer)super.recieveMessage(file.getAbsolutePath());
				clientKeyLookup.put(msg.clientID, msg.getKey());//associates client with generated encryption key sent by client
				if (msg instanceof MsgToServer.EndSession)
					this.terminateSession(msg.clientID, msg.userID);
				return msg;
			}
		}
		return null;
	}
	
}

