package com.example.settle_down

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.settle_down.Models.MatchResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_history.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HistoryFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var user: FirebaseUser? = null
    private val auth = FirebaseAuth.getInstance()
    private val mrList = ArrayList<MatchResult>()

    private var listener: OnHistoryFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getParcelable(ARG_PARAM1)
        }
        FirestoreDataManager.matchresultRef.orderBy(MatchResult.LAST_TOUCHED_KEY, Query.Direction.ASCENDING).get().addOnCompleteListener {
            for(document in it.result!!.documents){
                val doc = MatchResult.matchResultFromSnapshot(document)

                if(doc.challenger==user!!.uid||doc.receiver==user!!.uid) {
                    mrList.add(MatchResult.matchResultFromSnapshot(document))
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        if (view.list is RecyclerView) {
            with(view.list) {
                layoutManager = LinearLayoutManager(context)
                adapter = HistoryFragmentAdapter(mrList)
                Log.d("Is Recycler?", "Yes")
//
            }
            view.list.setHasFixedSize(true)
                view.list.addItemDecoration(
                    DividerItemDecoration(
                        context,
                        DividerItemDecoration.VERTICAL
                    )
                )
        }
        view.log_out.setOnClickListener {
            auth.signOut()
        }
        view.history_back_button.setOnClickListener {
            listener!!.onHistoryFragmentInteraction()
        }
        return view
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnHistoryFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnHistoryFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onHistoryFragmentInteraction()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(user: FirebaseUser) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, user)
                }
            }
    }
}
