package com.example.settle_down.Models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize


@Parcelize
data class TypeGame(
    val Problem: String = ""
):Parcelable, Game{
    @get:Exclude var id = ""
    companion object {
        const val LAST_TOUCHED_KEY = "lastTouched"

        fun typeGameFromSnapshot(snapshot: DocumentSnapshot): TypeGame {
            val type_game = snapshot.toObject(TypeGame::class.java)!!
            type_game.id = snapshot.id
            return type_game
        }
    }
}
