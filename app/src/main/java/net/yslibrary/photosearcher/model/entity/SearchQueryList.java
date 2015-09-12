package net.yslibrary.photosearcher.model.entity;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by yshrsmz on 15/08/31.
 */
public class SearchQueryList extends RealmObject {

    private RealmList<SearchQuery> searchQueries;

    public RealmList<SearchQuery> getSearchQueries() {
        return searchQueries;
    }

    public void setSearchQueries(RealmList<SearchQuery> searchQueries) {
        this.searchQueries = searchQueries;
    }
}
