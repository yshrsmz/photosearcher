package net.yslibrary.photosearcher.util;

import android.content.Context;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import rx.functions.Action1;

/**
 * Created by yshrsmz on 15/04/26.
 */
public class RealmUtil {

    private static final long SCHEMA_VERSION = 1;

    private static final String DATABASE_NAME = "photosearcher.realm";

    private static RealmConfiguration sRealmConfiguration;

    public static void setupDatabase(Context context) {

        sRealmConfiguration = buildRealmConfiguration(context);
        Realm.setDefaultConfiguration(sRealmConfiguration);

        Realm.getDefaultInstance().close();
    }

    private static RealmConfiguration buildRealmConfiguration(Context context) {
        return new RealmConfiguration.Builder(context)
                .name(DATABASE_NAME)
                .schemaVersion((int) SCHEMA_VERSION) // FIXME remove cast when fixed in the library
                .migration(new DatabaseMigration())
                .build();
    }

    private static File getRealmFile(Context context) {
        return new File(context.getFilesDir(), DATABASE_NAME);
    }

    public static void executeTransaction(Action1<Realm> transaction, Action1<Throwable> onFailure) {
        Realm realm = null;

        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();

            transaction.call(realm);

            realm.commitTransaction();
        } catch (Exception e) {
            if (realm != null) {
                realm.cancelTransaction();
            }
            onFailure.call(e);
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }


    public static class DatabaseMigration implements RealmMigration {

        @Override
        public long execute(Realm realm, long version) {
            if (version < 1) {
                version = SCHEMA_VERSION;
            }

//            if (version == 1) {
//                // migrate to version 2
//                version++;
//            }

            return SCHEMA_VERSION;
        }
    }
}