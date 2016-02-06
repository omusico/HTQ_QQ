package hq.king.client;

import hq.king.transport.TransportObject;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import android.util.Log;

public class ClientOutputThread extends Thread {

	private Socket mSocket;
	private ObjectOutputStream oos;
	private TransportObject msg;
	private boolean isStart;
	public ClientOutputThread(Socket socket) throws IOException
	{
		mSocket=socket;
		oos=new ObjectOutputStream(socket.getOutputStream());
		
	}
	public void setMsg(TransportObject msg)
	{

		this.msg=msg;
	}
	public void setStart(boolean start)
	{
		isStart =start;
		
	}
	public void run()
	{
		while(isStart)
		{
			//注意，此语句也必须写在while循环中，因为该线程的开启是在client类中开启的，但是setMsg方法
			//是在需要时才调用的，即必须让该线程一直运行，一直尝试写，加一个判空的语句来操作具体是否写
			if(msg!=null)
			{
				try {
					oos.writeObject(msg);
					oos.flush();
					msg=null;//此语句一定不能去掉，否则oos.writeObject（msg）语句会一直执行
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		try {
			//Log.i("ClientOutputThread","this print is after while loop");
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	

}
