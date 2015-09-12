package net.yslibrary.photosearcher.model;

import java.util.List;

/**
 * Created by yshrsmz on 15/08/28.
 */
public interface IDataSetManager<T> {

    int getItemCount();

    T getItem(int position);

    List<T> getItems();

    int setItem(T item);
}
