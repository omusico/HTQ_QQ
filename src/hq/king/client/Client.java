package hq.king.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.R.integer;
import android.util.Log;
public class Client {
	private Socket mSocket;
	private String ip;
	private int port;
	private boolean flag=true;
	private ClientInputThread clientInput;
	private ClientOutputThread clientOutput;
	
	public Client(String ip,int port) throws UnknownHostException, IOException {
	// TODO Auto-generated constructor stub
		this.ip=ip;
		this.port=port;
	//    start();
}

public boolean  create() throws UnknownHostException, IOException
{

		//	new Thread(){
		//		public void run ()
				{
					try {
						mSocket=new Socket(ip,port);
						if(mSocket!=null)
						{
						    Log.i("Client","socket is create");
							clientInput=new ClientInputThread(mSocket);
							clientOutput=new ClientOutputThread(mSocket);
							clientInput.setStart(true);
							clientOutput.setStart(true);
						
							clientInput.start();
							clientOutput.start();
							
						}
						else {
							Log.i("Client","socket is not create");
						//	Toast.makeText(, "亲，服务器端连接出错",0).show();
						}
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						flag=false;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						flag=false;
					}
				}
				
		//	}.start();
			
			return flag;
			
}
public void start()
{
	
	
}
public ClientInputThread getClientInputThread() 
{
	return clientInput;

}
public ClientOutputThread getClientOutputThread() 
{
	
	return clientOutput;
}

}