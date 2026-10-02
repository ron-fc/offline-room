package com.nemesis.offlinefroom.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nemesis.offlinefroom.data.local.dao.CharacterDao
import com.nemesis.offlinefroom.data.local.dao.CharacterRemoteKeysDao
import com.nemesis.offlinefroom.data.local.entity.CharacterEntity
import com.nemesis.offlinefroom.data.local.entity.CharacterRemoteKeysEntity

@Database(
    entities = [CharacterEntity::class, CharacterRemoteKeysEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    abstract fun characterRemoteKeysDao(): CharacterRemoteKeysDao

    companion object {
        const val DATABASE_NAME = "offline_froom_database"
    }
}