package com.example.settle_down

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.example.settle_down.Models.MatchResult
import kotlinx.android.synthetic.main.fragment_historylist.view.*

class HistoryFragmentAdapter(
    private val context: Context
) : RecyclerView.Adapter<HistoryFragmentAdapter.ViewHolder>() {

    private val mrList = ArrayList<MatchResult>()

    init{
        FirestoreDataManager.matchresultRef
            .orderBy(MatchResult.LAST_TOUCHED_KEY, Query.Direction.ASCENDING)
            .addSnapshotListener{ querySnapshot, firebaseFirestoreException ->
                if(firebaseFirestoreException!=null){
                    Log.e("error", "Listen error: $firebaseFirestoreException")
                }

                for(docChange in querySnapshot!!.documentChanges){
                    val mr = MatchResult.matchResultFromSnapshot(docChange.document)
                    when(docChange.type){

                        DocumentChange.Type.ADDED->{
                            mrList.add(0, mr)
                            notifyItemInserted(0)
                        }
                        DocumentChange.Type.REMOVED -> {
                            val pos = mrList.indexOfFirst { mr.id == it.id }
                            mrList.removeAt(pos)
                            notifyItemRemoved(pos)
                        }
                        DocumentChange.Type.MODIFIED -> {
                            val pos = mrList.indexOfFirst { mr.id == it.id }
                            mrList[pos]=mr
                            notifyItemChanged(pos)
                        }

                    }
                }
                notifyDataSetChanged()
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_history, parent, false)
        return ViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mrList[position]
        holder.mEvent.text = if(item.eventResolved.length < 60)  (if(item.eventResolved.length<36) item.eventResolved+"\n" else item.eventResolved) else item.eventResolved.substring(0,50)+" ..."
        holder.mDate.text = item.Date.toString()
        holder.mWinner.text = item.winner
        with(holder.mView) {
            tag = item
        }
    }

    override fun getItemCount(): Int = mrList.size

    inner class ViewHolder(val mView: View, val adapter: HistoryFragmentAdapter) : RecyclerView.ViewHolder(mView) {
        val mEvent: TextView = mView.historyFragmentEvent
        val mDate: TextView = mView.date
        val mWinner: TextView = mView.winner

    }
}
