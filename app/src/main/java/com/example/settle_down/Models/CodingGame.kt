package com.example.settle_down.Models


import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize


@Parcelize
data class CodingGame(
    val answer: Int = 0,
    val choice1: String = "",
    val choice2: String = "",
    val choice3: String = "",
    val choice4: String = "",
    val problem: String = ""
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