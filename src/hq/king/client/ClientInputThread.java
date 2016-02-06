package hq.king.client;

import hq.king.transport.TransportObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.net.Socket;

public class ClientInputThread extends Thread {
	private ObjectInputStream ois;
    private TransportObject msg;
    private MessageListener messageListener;
    private boolean isStart;
	public ClientInputThread(Socket socket) throws IOException
	{
      
       ois=new ObjectInputStream(socket.getInputStream());
		
	}
	
	public void setMessageListener(MessageListener messageListener)
	{
		this.messageListener=messageListener;
		/*synchronized (this)
		{
		 notify();
		}*/
		
	}
	public void setStart(boolean start)
	{

		isStart=start;
	}
   public void run()
   {
	  /* try {
		   synchronized (this)
		   {
			wait();
		   }
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
      while(isStart)
      {
    	 try {
			msg= (TransportObject) ois.readObject();
			
			if(msg!=null)
			{
				 messageListener.getMessage(msg);
			}
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	  
      }
   }
   
  
}
