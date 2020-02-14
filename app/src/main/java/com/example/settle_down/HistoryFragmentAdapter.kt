package com.example.settle_down

import android.content.Context
import android.service.autofill.FieldClassification
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
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_history.view.*
import kotlinx.android.synthetic.main.fragment_historylist.view.*

class HistoryFragmentAdapter(
    private val mrList: List<MatchResult>
) : RecyclerView.Adapter<HistoryFragmentAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
Log.d("Why???", "")
        val view = LayoutInflater.from(parent.list.context)
            .inflate(R.layout.fragment_historylist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mrList[position]
        Log.d("Why??", item.toString())

        holder.mEvent.text = if(item.eventResolved.length < 60)  (if(item.eventResolved.length<36) item.eventResolved+"\n" else item.eventResolved) else item.eventResolved.substring(0,50)+" ..."
        holder.mDate.text = item.date.toString()
        holder.mWinner.text = item.winner
        with(holder.mView) {
            tag = item
        }
    }

    override fun getItemCount(): Int = mrList.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mEvent: TextView = mView.historyFragmentEvent
        val mDate: TextView = mView.date
        val mWinner: TextView = mView.winner

    }
}
