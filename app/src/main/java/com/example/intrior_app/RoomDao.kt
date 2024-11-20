package com.example.intrior_app


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface RoomDao {
    @Insert
    suspend fun insertRoom(room: RoomEntity)

    @Update
    suspend fun updateRoom(room: RoomEntity)

    @Delete
    suspend fun deleteRoom(room: RoomEntity)

    @Query("SELECT * FROM rooms")
    fun getAllRooms(): LiveData<List<RoomEntity>>

    @Query("SELECT * FROM rooms WHERE category = :category")
    fun getRoomsByCategory(category: String): LiveData<List<RoomEntity>>


    @Query("SELECT * FROM rooms WHERE category = :category")
    fun getItemsForCategory(category: String): List<RoomEntity>


    @Query("SELECT * FROM rooms WHERE title LIKE :search OR description LIKE :search OR category LIKE :search")
    fun searchRooms(search: String): LiveData<List<RoomEntity>>

}
