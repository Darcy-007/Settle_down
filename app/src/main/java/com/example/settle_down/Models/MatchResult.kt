package com.example.settle_down.Models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp

data class MatchResult(
    val Challenger: String = "",
    val ChallengerScore: Int = 1,
    val Code: String = "",
    val EventResolved: String = "",
    val GameType: Game = MathGame(),
    var Receiver: String = "",
    val ReceiverScore: Int = 0,
    val Winner: String = "",
    var isComplete: Boolean = false
    ){
    @get:Exclude var id = ""
    @ServerTimestamp var Date: Timestamp? = null
    companion object {
        const val LAST_TOUCHED_KEY = "lastTouched"

        fun matchResultFromSnapshot(snapshot: DocumentSnapshot): MatchResult {
            val match_result = snapshot.toObject(MatchResult::class.java)!!
            match_result.id = snapshot.id
            return match_result
        }
    }
}
