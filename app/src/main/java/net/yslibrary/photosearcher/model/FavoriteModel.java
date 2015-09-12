package net.yslibrary.photosearcher.model;

import com.twitter.sdk.android.core.models.Tweet;

import net.yslibrary.photosearcher.model.api.RxFavoriteService;
import net.yslibrary.photosearcher.model.entity.FavoriteTweet;
import net.yslibrary.photosearcher.util.RealmUtil;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import retrofit.RetrofitError;
import rx.Observable;
import timber.log.Timber;


/**
 * Created by yshrsmz on 15/08/28.
 */
public class FavoriteModel {


    RxFavoriteService mFavoriteService;

    @Inject
    public FavoriteModel(RxFavoriteService favoriteService) {
        mFavoriteService = favoriteService;
    }

    public Observable<Tweet> create(@NonNull Long id) {
        return mFavoriteService.create(id, true)
                .doOnNext(tweet -> {
                    insertFavorited(tweet.id);
                }).doOnError(throwable -> {
                    if (throwable instanceof RetrofitError) {
                        RetrofitError error = (RetrofitError) throwable;
                        if (RetrofitError.Kind.HTTP.equals(error.getKind()) && error.getResponse().getStatus() == 403) {
                            // probably already favorited
                            insertFavorited(id);
                        }
                    }
                });
    }

    private void insertFavorited(Long id) {
        RealmUtil.executeTransaction(realm -> {
            FavoriteTweet favoriteTweet = realm.where(FavoriteTweet.class).equalTo("id", id).findFirst();
            if (favoriteTweet == null) {
                favoriteTweet = new FavoriteTweet();
                favoriteTweet.setId(id);
                realm.copyToRealm(favoriteTweet);
            }
        }, throwable -> {
            Timber.e(throwable, throwable.getMessage());
        });
    }
}
