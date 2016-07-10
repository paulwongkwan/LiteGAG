package mememe.litegag.service;

import mememe.litegag.object.Comments;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by MEMEME-iClass on 8/7/2016.
 */
public interface CommentService {

	@GET("comments/{id}")
	Call<Comments> getComments(@Path("id") String id, @Query("level") int lv, @Query("order") String order, @Query("count") int count);
}
