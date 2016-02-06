package hq.king.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;

public class FileUtil {
	private static String path="";
	static{
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			path=Environment.getExternalStorageDirectory()+"/HTQ";
		}else{
			path=Environment.getDataDirectory().getAbsolutePath()+"/HTQ";
		}
	}
	public static boolean isSdCardReady()
	{
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

		
	}
	public static String getSignPath(){
		File file=new File(path+"/Sign/");
		if(!file.exists()){
			file.mkdirs();
		}
		return path+"/Sign/";
	}
	public static String readSignFromFile() throws IOException
	{
		File file=new File(getSignPath(),"sign.txt");
		FileInputStream fis=new FileInputStream(file);
		DataInputStream dis=new DataInputStream(fis);
		String str=dis.readUTF();
		dis.close();
		return str;

		
	}
	public static String getImgPath()
	{
		File file=new File(path+"/Images/");
		if(!file.exists()){
			file.mkdirs();
		}
		return path+"/Images/";
		
	}
	public static boolean writeSignToFile(String content) throws IOException
	
	{
		if(isSdCardReady())
		{
			File file=new File(getSignPath(),"sign.txt");
			if(!file.exists()){
			file.createNewFile();
			}
			FileOutputStream fos=new FileOutputStream(file);
			DataOutputStream dos=new DataOutputStream(fos);
			dos.writeUTF(content);
			dos.close();
			
			
		}
			return true;
	}
	

}
