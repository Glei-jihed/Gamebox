package com.example.game_box.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    val id: Long = 0,
    val title: String,
    val description: String,
    val tags: List<String>,
    val sourceUrl: String? = "",  // rendue nullable avec valeur par défaut
    val userId: String,
    val isFavorite: Boolean = false
) : Parcelable

