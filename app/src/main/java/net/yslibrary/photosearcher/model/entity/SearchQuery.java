package net.yslibrary.photosearcher.model.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by yshrsmz on 15/08/29.
 */
public class SearchQuery extends RealmObject {

    @PrimaryKey
    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
