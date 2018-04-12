package be.tabtabstudio.veganapp.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import be.tabtabstudio.veganapp.data.entities.Product;
import be.tabtabstudio.veganapp.data.entities.User;

@Database(entities = { Product.class }, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "vegdb";

    private static final Object LOCK = new Object();
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME).build();
            }
        }
        return sInstance;
    }

    public abstract FavoritesDao favoritesDao();
}
