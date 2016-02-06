package hq.king.client;

import hq.king.transport.TranObjectType;
import hq.king.transport.TransportObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.UnknownHostException;












import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class ClientReadThread extends Thread {
	private Handler mHandler;
    public Handler mReceiveHandler;//组合模式，将内部类作为外部类的成员，这样通过外部类对象就可以访问该内部类对象的方法
    private InputStream mInputStreamReader;
	private BufferedReader mReader;
    private ObjectInputStream ois;
    private TransportObject  tranMsg;
   public ClientReadThread(Handler handler,InputStream inputStreamReader) throws StreamCorruptedException, IOException {
		// TODO Auto-generated constructor stub
		mHandler=handler;
		mInputStreamReader=inputStreamReader;
	//	ois=new ObjectInputStream(mInputStreamReader);
		//注意当不把ois的实例化语句单独放到一个线程中时，那就要注意客户端与服务器端ois和oos创建的先后顺序
		//因为一旦客户端与服务器都是先创建的ois，则客户端的ois实例化会阻塞线程，所以最好把ois/oos的实例化
		//单独放到一个线程中
	}
    
	public void run()
	{
		try {
			ois=new ObjectInputStream(mInputStreamReader);
			//注意当服务器端oos未创建时该语句会阻塞线程
			//注意此语句必须写在一个线程中，因为从网络中读取信息可能会产生阻塞
			//一旦产生阻塞，则ChatActivity中的线程也会被阻塞，导致
			//该线程中mClientThread.start();以后的语句都不会执行
			//而直接执行ChatActivity主线程中的代码，即在ChatActivity中创建
			//子线程之后的代码
		} catch (StreamCorruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	new Thread(){
			public void run()
			{
			try {
					while((tranMsg=(TransportObject) ois.readObject()) != null)
					{
						if(tranMsg.getType()==TranObjectType.MESSAGE)
						{
							Message msg=new Message();
							msg.what=0x12;
							msg.obj=tranMsg;
							mHandler.sendMessage(msg);
						}else if(tranMsg.getType()==TranObjectType.LOGIN){
							Message msg=new Message();
							msg.what=0x34;
							msg.obj=tranMsg;
							mHandler.sendMessage(msg);
						}
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
			
		}.start();
	}

}
