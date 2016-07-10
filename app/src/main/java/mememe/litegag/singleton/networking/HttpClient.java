package mememe.litegag.singleton.networking;

import okhttp3.OkHttpClient;

/**
 * Created by Wong Kwan on 7/7/2016.
 */
public class HttpClient {
	private static OkHttpClient ourInstance = new OkHttpClient();
	
	public static OkHttpClient getInstance() {
		return ourInstance;
	}
	
	private HttpClient() {
	}
}
