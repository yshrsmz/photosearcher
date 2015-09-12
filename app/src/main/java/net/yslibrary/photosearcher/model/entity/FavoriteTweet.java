package net.yslibrary.photosearcher.model.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by yshrsmz on 15/08/28.
 */
public class FavoriteTweet extends RealmObject {

    @PrimaryKey
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
