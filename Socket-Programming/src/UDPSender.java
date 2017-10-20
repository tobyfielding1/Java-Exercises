import java.io.*;
import java.net.*;

class UDPSender{

	public static void main(String [] args){
		try{
			InetAddress address =
					InetAddress.getByName("localhost");
			DatagramSocket socket2 = new DatagramSocket(5555);
			for(int i=0;i<10;i++){
				byte[] buf = String.valueOf(i).getBytes();
				DatagramSocket socket = new DatagramSocket();
				DatagramPacket packet =
						new DatagramPacket(buf, buf.length, address, 4321);
				socket.send(packet);
				System.out.println("send DatagramPacket "
						+ new String(packet.getData()) + " "
						+ packet.getAddress() + ":"
						+ packet.getPort());
				
				byte[] buf2 = new byte[256];
				DatagramPacket packet2 = new DatagramPacket(buf, buf.length);
				socket2.receive(packet2);
				System.out.println("receive DatagramPacket "
						+ (new String(packet2.getData())).trim() + " "
						+ packet2.getAddress() + ":"
						+ packet2.getPort());

				Thread.sleep(2000);
			}
		}catch(Exception e){System.out.println("error" + e.getMessage());}
	}
}