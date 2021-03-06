package com.example.settle_down.Models

import android.os.Parcelable
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MatchResult(
    val challenger: String = "",
    var challengerScore: Int = 0,
    var code: String = "",
    var complete: Boolean = false,
    var eventResolved: String = "",
    var gameType: Int = 0,
    var receiver: String = "",
    var receiverScore: Int = 0,
    var winner: String = "",
    var gameId: ArrayList<String>? = null,
    var numCompleted: Int = 0
    ): Parcelable {
    @get:Exclude var id = ""
    @ServerTimestamp var date: Timestamp? = null
    companion object {

        const val LAST_TOUCHED_KEY = "date"
        fun matchResultFromSnapshot(snapshot: DocumentSnapshot): MatchResult {
            val match_result = snapshot.toObject(MatchResult::class.java)!!
            match_result.id = snapshot.id
            return match_result
        }
    }
}
