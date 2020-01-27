package com.example.settle_down.Models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize


@Parcelize
data class MathGame(
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

        fun mathGameFromSnapshot(snapshot: DocumentSnapshot): MathGame {
            val math_game = snapshot.toObject(MathGame::class.java)!!
            math_game.id = snapshot.id
            return math_game
        }
    }
}
