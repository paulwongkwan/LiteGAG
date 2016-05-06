package mememe.litegag.object;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Wong Kwan on 18/2/2016.
 * For storing data fetch from the web.
 * Each object is one GAG.
 */
public class GagObject {
	public String id;
	public String caption;
	public HashMap<String, String> images = new HashMap<>();
	public HashMap<String, String> media = new HashMap<>();
	public String link;
	public int votes = 0, comments = 0;

	public GagObject(){}

	public void setImages(JSONObject obj){
		try {
			images.put("small", obj.getString("small"));
			images.put("cover", obj.getString("cover"));
			images.put("normal", obj.getString("normal"));
			images.put("large", obj.getString("large"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void setMedia(JSONObject obj){
		try {
			media.put("mp4", obj.getString("mp4"));
			media.put("webm", obj.getString("webm"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
