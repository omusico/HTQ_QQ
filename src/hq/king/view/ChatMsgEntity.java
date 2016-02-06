package hq.king.view;



public class ChatMsgEntity {
	 private static final String TAG = ChatMsgEntity.class.getSimpleName();

	    private String name;

	    private String date;

	    private String text;

	    private String message;// ÏûÏ¢ÄÚÈÝ
		private int img;
	    private boolean isComMeg = true;

	    public ChatMsgEntity(String name, String date, String text, int img,
				boolean isComMsg) {
			super();
			this.name = name;
			this.date = date;
			this.message = text;
			this.img = img;
			this.isComMeg = isComMsg;
		}
	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getDate() {
	        return date;
	    }

	    public void setDate(String date) {
	        this.date = date;
	    }

	    public String getText() {
	        return message;
	    }

	    public void setText(String text) {
	        this.message = text;
	    }

	    public boolean getMsgType() {
	        return isComMeg;
	    }

	    public void setMsgType(boolean isComMsg) {
	    	isComMeg = isComMsg;
	    }

	    public int getImg() {
			return img;
		}

		public void setImg(int img) {
			this.img = img;
		}
		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	    public ChatMsgEntity() {
	    }

	    public ChatMsgEntity(String name, String date, String text, boolean isComMsg) {
	        super();
	        this.name = name;
	        this.date = date;
	        this.text = text;
	        this.isComMeg = isComMsg;
	    }
	

}
