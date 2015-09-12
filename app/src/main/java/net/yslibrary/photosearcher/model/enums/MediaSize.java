package net.yslibrary.photosearcher.model.enums;

import com.twitter.sdk.android.core.models.MediaEntity;

/**
 * Created by yshrsmz on 15/08/27.
 */
public enum MediaSize {

    THUMB {
        @Override
        public MediaEntity.Size getSize(MediaEntity mediaEntity) {
            return mediaEntity.sizes.thumb;
        }
    },
    SMALL {
        @Override
        public MediaEntity.Size getSize(MediaEntity mediaEntity) {
            return mediaEntity.sizes.small;
        }
    },
    MEDIUM {
        @Override
        public MediaEntity.Size getSize(MediaEntity mediaEntity) {
            return mediaEntity.sizes.medium;
        }
    },
    LARGE {
        @Override
        public MediaEntity.Size getSize(MediaEntity mediaEntity) {
            return mediaEntity.sizes.large;
        }
    };

    public abstract MediaEntity.Size getSize(MediaEntity mediaEntity);

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
