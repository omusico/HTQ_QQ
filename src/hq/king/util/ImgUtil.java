package hq.king.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.R.integer;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.SoftReference;

import javax.security.auth.PrivateCredentialPermission;



public class ImgUtil {
	private static ImgUtil instance;
	private static HashMap<String, SoftReference<Bitmap>> imgCaches;
	private static ExecutorService executorThreadPool = Executors
			.newFixedThreadPool(1);
	static {
		instance = new ImgUtil();
		imgCaches = new HashMap<String, SoftReference<Bitmap>>();
	}

	public static ImgUtil getInstance() {
		if (instance != null) {
			return instance;
		}
		return null;
	}
	
	
	
	public static void saveAvatorImage(Bitmap bitmap) throws IOException
	{
		File file=new File(FileUtil.getImgPath(),"avator.jpg");
		if(!file.exists()){
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
		fos.flush();
		fos.close();
		
	}
	public static void saveQQImage(Bitmap bitmap) throws IOException
	{
		File file=new File(FileUtil.getImgPath(),"qqAvator.jpg");
		if(!file.exists()){
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
		fos.flush();
		fos.close();
		
	}
   public static Bitmap getDefaultAvator()
   {
	   String avatorPath=FileUtil.getImgPath()+"defaultAvator.jpg";
	   Bitmap bitmap=BitmapFactory.decodeFile(avatorPath);
	   return bitmap;
	   
   }
   public static Bitmap getQQAvator()
   {
	   String avatorPath=FileUtil.getImgPath()+"qqAvator.jpg";
	   Bitmap bitmap=BitmapFactory.decodeFile(avatorPath);
	   return bitmap;
	   
   }
	public static Bitmap getAvator()
	{
		//File file=new File(FileUtil.getImgPath(),"avator.jpg");
		String avatorPath=FileUtil.getImgPath()+"avator.jpg";
		Bitmap bitmap=BitmapFactory.decodeFile(avatorPath);
		return bitmap;
	}
	public static String getAvatorPath()
	{
		String avatorPath=FileUtil.getImgPath()+"avator.jpg";
		return avatorPath;

		
	}
	private static  void drawCircleBorder(Canvas canvas,Bitmap bitmap, int color) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		paint.setColor(color);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(0);
		float radius=bitmap.getWidth();
		canvas.drawCircle(radius/2,radius/2,radius,paint);
	}
	
	public static Bitmap createCircleImage(Bitmap source )
	{
		final Paint paint = new Paint();
		final int min=source.getWidth();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_4444);
		/**
		 * 产生一个同样大小的画布
		 */
		Canvas canvas = new Canvas(target);
		/**
		 * 首先绘制圆
		 */
		
		canvas.drawCircle(min / 2, min / 2, min / 2, paint);
		/**
		 * 使用SRC_IN，参考上面的说明
		 */
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		/**
		 * 绘制图片
		 */

		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}

	
	public void loadBitmap(final String path,
			final OnLoadBitmapListener listener) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Bitmap bitmap = (Bitmap) msg.obj;
				listener.loadImage(bitmap, path);
			}
		};
		new Thread() {

			@Override
			public void run() {
				executorThreadPool.execute(new Runnable() {
					public void run() {
						Bitmap bitmap = loadBitmapFromCache(path);
						if (bitmap != null) {
							Message msg = handler.obtainMessage();
							msg.obj = bitmap;
							handler.sendMessage(msg);
						}

					}
				});
			}

		}.start();
	}
	
	@SuppressWarnings("unused")
	private Bitmap loadBitmapFromCache(String path) {
		if (imgCaches == null) {
			imgCaches = new HashMap<String, SoftReference<Bitmap>>();
		}
		Bitmap bitmap = null;
		if (imgCaches.containsKey(path)) {
			bitmap = imgCaches.get(path).get();
		}
		if (bitmap == null) {
			bitmap = loadBitmapFromLocal(path);
		}
		return bitmap;
	}
	
	private Bitmap loadBitmapFromLocal(String path) {
		if (path == null) {
			return null;
		}
		BitmapFactory.Options options = new Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		float height = 800f;
		float width = 480f;
		float scale = 1;
		if (options.outWidth > width && options.outWidth > options.outHeight) {
			scale = options.outWidth / width;
		} else if (options.outHeight > height
				&& options.outHeight > options.outWidth) {
			scale = options.outHeight / height;
		} else {
			scale = 1;
		}
		options.inSampleSize = (int) scale;
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(path, options);
		bitmap = decodeBitmap(bitmap);
		if (!imgCaches.containsKey(path)) {
			//imgCaches.put(path, new SoftReference<Bitmap>(bitmap));
			addCache(path, bitmap);
		}
		return bitmap;
	}

	public static void  cacheImgFromUri(Uri uri) throws IOException
	{
		
		File currentFile=new File(uri.getPath());
		FileInputStream fis=new FileInputStream(currentFile);
		/*byte[] bitmap=new byte[1024*1024*4];
		int length;
		while((length=fis.read(bitmap, 0, 1024))!=-1)*/
		
		//将bitmap中的内容write到reslutFile中
		Bitmap bitmap=BitmapFactory.decodeStream(fis);
		if(FileUtil.isSdCardReady())
		{
			File resultFile=new File(FileUtil.getImgPath(),"AVATOR.jpg");
			if(!resultFile.exists()){
				resultFile.createNewFile();
			}
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			
	/*	FileOutputStream bos = new FileOutputStream(resultFile);
		bos.write(bitmap,0,length);
		fis.close();
		bos.close();*/
		}
	}
	private Bitmap decodeBitmap(Bitmap bitmap) {
		int scale = 100;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, scale, bos);
		while ((bos.toByteArray().length / 1024) > 30) {
			bos.reset();
			bitmap.compress(Bitmap.CompressFormat.JPEG, scale, bos);
			scale -= 10;
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		bitmap = BitmapFactory.decodeStream(bis);
		return bitmap;
	}
	
	public void addCache(String path,Bitmap bitmap){
		imgCaches.put(path, new SoftReference<Bitmap>(bitmap));
	}
	
	
	public interface OnLoadBitmapListener {
		void loadImage(Bitmap bitmap, String path);
	}
}
