package com.example.settle_down.Models


import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize


@Parcelize
data class CodingGame(
    val Answer: Int = 0,
    val Choice1: String = "",
    val Choice2: String = "",
    val Choice3: String = "",
    val Choice4: String = "",
    val Problem: String = ""
):Parcelable, Game{
    @get:Exclude var id = ""
    companion object {
        const val LAST_TOUCHED_KEY = "lastTouched"

        fun codingGameFromSnapshot(snapshot: DocumentSnapshot): CodingGame {
            val coding_game = snapshot.toObject(CodingGame::class.java)!!
            coding_game.id = snapshot.id
            return coding_game
        }
    }
}