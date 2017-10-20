package reciever;

import java.io.*;
import java.net.*;

class UDPReceiver{
	
	public static void main(String [] args){
		try{
			DatagramSocket socket = new DatagramSocket(4321);
			byte[] buf = new byte[256];
			for(int i=0;i<10;i++){
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				System.out.println("receive DatagramPacket "
						+ (new String(packet.getData())).trim() + " "
						+ packet.getAddress() + ":"
						+ packet.getPort());
				DatagramSocket socket2 = new DatagramSocket();
				InetAddress address =
						InetAddress.getByName("localhost");
				
				DatagramPacket packet2 =
						new DatagramPacket(buf, buf.length, address, 5555);
				socket2.send(packet2);
				System.out.println("send DatagramPacket "
						+ new String(packet2.getData()) + " "
						+ packet2.getAddress() + ":"
						+ packet2.getPort());

			}
		} catch(Exception e){System.out.println("error "+e);}
	}

}