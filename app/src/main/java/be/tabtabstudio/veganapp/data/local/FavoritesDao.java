package be.tabtabstudio.veganapp.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import be.tabtabstudio.veganapp.data.entities.Product;
import be.tabtabstudio.veganapp.data.entities.User;

@Dao
public interface FavoritesDao {
    @Query("SELECT * FROM favorites")
    LiveData<List<Product>> getAll();

    @Query("SELECT * FROM favorites WHERE ean = :ean LIMIT 1")
    Product getById(long ean);

    @Insert
    void insert(Product product);

    @Delete
    void delete(Product product);
}
