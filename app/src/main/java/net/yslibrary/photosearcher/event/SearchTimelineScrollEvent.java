package net.yslibrary.photosearcher.event;

/**
 * Created by yshrsmz on 15/08/31.
 */
public class SearchTimelineScrollEvent {

    public final String query;
    public final int position;

    public SearchTimelineScrollEvent(String query, int position) {
        this.query = query;
        this.position = position;
    }
}
