package hq.king.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientThread extends Thread {
	private Socket mSocket;
	private String mIp;
	private int mPort;
	private InputStream is;
	private OutputStream os;
	private StringBuffer mWriteContent=new StringBuffer();
	private String buffer=null;
	private DataOutputStream dos;
	private DataInputStream dis;
	public ClientThread(String ip,int  port,String chatContent) throws UnknownHostException, IOException
	{
       mIp=ip;
       mPort=port;
       mWriteContent.append(chatContent);
     
		
	}
	public void init() throws UnknownHostException, IOException
	{

		mSocket=new Socket(mIp,mPort);
		is=mSocket.getInputStream();
		os=mSocket.getOutputStream();
		
		dis=new DataInputStream(is);
		dos=new DataOutputStream(os);
		
	}
	public void run()
	{
 
		try {
			dos.writeUTF(new String(mWriteContent));
			dos.flush();
			dos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			buffer=dis.readUTF();
			dis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	public String getWriteContent()
	{
		
		return buffer;

		
	}

}
