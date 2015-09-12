package net.yslibrary.photosearcher.model.helper;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import android.support.annotation.NonNull;

import java.lang.reflect.Type;

/**
 * Created by yshrsmz on 15/08/24.
 */
public class JsonHelper {

    private static final String JSON_PARSE_ERROR = "Unable to parse JSON: ";

    private static Gson sGson;

    static {
        sGson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    public static Gson getGson() {
        return sGson;
    }

    public static String toJson(@NonNull Object object) {
        return sGson.toJson(object);
    }

    public static String toJson(@NonNull Object object, @NonNull Type type) {
        return sGson.toJson(object, type);
    }

    public static <T> T fromJson(@NonNull String content, @NonNull Class<T> clazz) {
        try {
            return sGson.fromJson(content, clazz);
        } catch (JsonSyntaxException e) {
            throw new IllegalStateException(JSON_PARSE_ERROR + content, e);
        }
    }

    public static <T> T fromJson(@NonNull String content, @NonNull Type type) {
        try {
            return sGson.fromJson(content, type);
        } catch (JsonSyntaxException e) {
            throw new IllegalStateException(JSON_PARSE_ERROR + content, e);
        }
    }

}
