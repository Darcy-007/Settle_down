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
