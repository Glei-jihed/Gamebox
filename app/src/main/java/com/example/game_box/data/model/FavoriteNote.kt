package com.example.game_box.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteNote(
    val id: Long,
    val note: Note,
    val userId: String
) : Parcelable
