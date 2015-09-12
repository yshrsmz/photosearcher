package net.yslibrary.photosearcher.model;

/**
 * Created by yshrsmz on 15/08/29.
 */
public interface ITabDataSetManager<T> {
    T getItem(int position);

    int getCount();
}
