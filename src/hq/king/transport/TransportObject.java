package hq.king.transport;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TransportObject<T>implements Serializable {
	private int fromUser;
	private int toUser;
	private String name;
	private T object;
	private TranObjectType type;

	public TransportObject( TranObjectType type) {
		// TODO Auto-generated constructor stub
		this.type = type;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getName()
	{

		return name;
	}
	
	public int getFromUser() {
		return fromUser;
	}

	public void setFromUser(int fromUser) {
		this.fromUser = fromUser;
	}

	public int getToUser() {
		return toUser;
	}

	public void setToUser(int toUser) {
		this.toUser = toUser;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public TranObjectType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "TranObject [type=" + type + ", fromUser=" + fromUser
				+ ", toUser=" + toUser + ", object=" + object + "]";
	}
}


