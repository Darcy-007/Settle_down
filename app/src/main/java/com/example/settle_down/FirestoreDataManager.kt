import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

//package com.example.settle_down
//
//import com.google.firebase.firestore.CollectionReference
//import com.google.firebase.firestore.FirebaseFirestore
//
//object FirestoreDataManager {
//    private val data = ArrayList<MatchResult>()
//    private lateinit var dataRef: CollectionReference
//    fun beginListening() {
//        quotesRef = FirebaseFirestore.getInstance().collection()
//        quotesRef.orderBy(MovieQuote.CREATED_KEY, Query.Direction.ASCENDING)
//            .addSnapshotListener { snapshot, exception ->
//                for (docChange in snapshot!!.documentChanges) {
//                    val mq = MovieQuote.from(docChange.document)
//                    when (docChange.type) { … }
//                }
//                fun getNumQuotes() = movieQuotes.size
//                fun getQuoteAt(position: Int) = movieQuotes[position]
//                fun add(movieQuote: MovieQuote) {
//                    quotesRef.add(movieQuote)
//                } ...
//            }

object FirestoreDataManager {
    var typingGameRef = FirebaseFirestore
        .getInstance()
        .collection("TypeGame")
    var codingingGameRef = FirebaseFirestore
        .getInstance()
        .collection("CodingGame")
    var matchresultRef = FirebaseFirestore
        .getInstance()
        .collection("MatchResult")
    var mathGameRef = FirebaseFirestore
        .getInstance()
        .collection("MathGame")
}
