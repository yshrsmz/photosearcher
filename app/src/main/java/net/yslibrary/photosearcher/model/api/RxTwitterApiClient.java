package net.yslibrary.photosearcher.model.api;

import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterApiClient;

/**
 * Created by yshrsmz on 15/08/28.
 */
public class RxTwitterApiClient extends TwitterApiClient {
    public RxTwitterApiClient(Session session) {
        super(session);
    }

    public RxFavoriteService getRxFavoriteService() {
        return getService(RxFavoriteService.class);
    }
}
