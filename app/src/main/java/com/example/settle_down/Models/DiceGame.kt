package com.example.settle_down.Models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize


@Parcelize
class DiceGame :Parcelable, Game{
    @get:Exclude var id = ""
    companion object {
        const val LAST_TOUCHED_KEY = "lastTouched"

        fun diceGameFromSnapshot(snapshot: DocumentSnapshot): DiceGame {
            val dice_game = snapshot.toObject(DiceGame::class.java)!!
            dice_game.id = snapshot.id
            return dice_game
        }
    }
}