package mememe.litegag.singleton.setting;

/**
 * Created by Wong Kwan on 8/7/2016.
 */
public class CommentSetting {
	private static CommentSetting ourInstance = new CommentSetting();
	
	public static CommentSetting getInstance() {
		return ourInstance;
	}

	private int commentLevel = 3;
	private int commentAmount = 50;
	
	private CommentSetting() {
	}

	public int getCommentLevel(){return commentLevel;}
	public void setCommentLevel(int lv){commentLevel = lv;}

	public int getCommentAmount(){return commentAmount;}
	public void setCommentAmount(int am){commentAmount = am;}
}
