package com.myfarm.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    void insertAll(Product... products);

    @Delete
    void delete(Product product);

    @Query("SELECT * FROM product")
    List<Product> getAllProducts();
}
