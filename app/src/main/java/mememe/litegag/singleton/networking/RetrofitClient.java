package mememe.litegag.singleton.networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Wong Kwan on 8/7/2016.
 */
public class RetrofitClient {
	private static Retrofit ourInstance = new Retrofit.Builder()
			.baseUrl("http://infinigag.k3min.eu/")
			.client(HttpClient.getInstance())
			.addConverterFactory(GsonConverterFactory.create())
			.build();
	
	public static Retrofit getInstance() {
		return ourInstance;
	}
	
	private RetrofitClient() {
	}
}
