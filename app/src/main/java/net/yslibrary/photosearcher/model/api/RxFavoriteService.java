package net.yslibrary.photosearcher.model.api;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by yshrsmz on 15/08/28.
 */
public interface RxFavoriteService {

    @GET("/1.1/favorites/list.json")
    Observable<List<Tweet>> list(@Query("user_id") Long var1, @Query("screen_name") String var2, @Query("count") Integer var3, @Query("since_id") String var4, @Query("max_id") String var5, @Query("include_entities") Boolean var6);

    @FormUrlEncoded
    @POST("/1.1/favorites/destroy.json")
    Observable<Tweet> destroy(@Field("id") Long var1, @Field("include_entities") Boolean var2);

    @FormUrlEncoded
    @POST("/1.1/favorites/create.json")
    Observable<Tweet> create(@Field("id") Long var1, @Field("include_entities") Boolean var2);
}
