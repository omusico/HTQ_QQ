package hq.king.entity;

public class RecentChatEntity {
	private int id;
	private int img;
	private int count;
	private String name;
	private String time;
	private String msg;

	public RecentChatEntity() {
		// TODO Auto-generated constructor stub
	}

	public RecentChatEntity(int id, int img, int count, String name,
			String time, String msg) {
		super();
		this.id = id;
		this.img = img;
		this.count = count;
		this.name = name;
		this.time = time;
		this.msg = msg;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getImg() {
		return img;
	}

	public void setImg(int img) {
		this.img = img;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean equals(Object object) {
		if (object == null)// 如果是空对象，肯定是不同�?
			return false;

		if (object == this)// 如果是同�?个对象，肯定是相同的
			return true;

		if (object instanceof RecentChatEntity) {// 如果id相同，我们就认为是同�?个对�?,因为id是唯�?的，对于我这个小项目来说
			RecentChatEntity entity = (RecentChatEntity) object;
			if (entity.id == this.id)
				return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "RecentChatEntity [id=" + id + ", img=" + img + ", count="
				+ count + ", name=" + name + ", time=" + time + ", msg=" + msg
				+ "]";
	}
}
