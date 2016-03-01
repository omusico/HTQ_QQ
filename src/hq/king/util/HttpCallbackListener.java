package hq.king.util;

public interface HttpCallbackListener {

	public void onComplete(String result);
	public void onError(Exception e);
}
