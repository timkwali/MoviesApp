package com.example.common.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.common.utils.Constants.REMOTE_KEYS_TABLE

@Entity(tableName = REMOTE_KEYS_TABLE)
data class RemoteKeys(
    @PrimaryKey(autoGenerate = true)
    val movieId: Int = 0,
    val prevKey: Int?,
    val nextKey: Int?
)
