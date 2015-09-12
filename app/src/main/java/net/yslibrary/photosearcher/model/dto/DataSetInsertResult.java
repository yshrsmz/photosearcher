package net.yslibrary.photosearcher.model.dto;

/**
 * Created by yshrsmz on 15/09/12.
 */
public class DataSetInsertResult {
    // inserted from
    private int insertedPosition;

    // inserted count
    private int insertedCount;

    public DataSetInsertResult(int insertedPosition, int insertedCount) {
        this.insertedPosition = insertedPosition;
        this.insertedCount = insertedCount;
    }

    public int getInsertedCount() {
        return insertedCount;
    }

    public void setInsertedCount(int insertedCount) {
        this.insertedCount = insertedCount;
    }

    public int getInsertedPosition() {

        return insertedPosition;
    }

    public void setInsertedPosition(int insertedPosition) {
        this.insertedPosition = insertedPosition;
    }
}
