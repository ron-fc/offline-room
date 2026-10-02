package com.nemesis.offlinefroom.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nemesis.offlinefroom.data.local.entity.CharacterRemoteKeysEntity

@Dao
interface CharacterRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<CharacterRemoteKeysEntity>)

    @Query("SELECT * FROM character_remote_keys WHERE characterId = :id")
    suspend fun getRemoteKeysByCharacterId(id: Int): CharacterRemoteKeysEntity?

    @Query("DELETE FROM character_remote_keys")
    suspend fun clearRemoteKeys()
}