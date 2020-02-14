package com.example.settle_down

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.settle_down.Models.MatchResult
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.fragment_waiting.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_MR = "WAIGMR"
private const val ARG_ISCHALLENGER = "WAIGIC"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [WaitingAnotherInGameFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [WaitingAnotherInGameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WaitingAnotherInGameFragment : Fragment() {
    private var mr: MatchResult? = null
    private var isChalllenger: Boolean? = null
    private var listener: OnWAIGFragmentInteractionListener? = null
    private var playId:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mr = it.getParcelable(ARG_MR)
            isChalllenger = it.getBoolean(ARG_ISCHALLENGER)
        }
        playId = if (isChalllenger!!) mr!!.challenger else mr!!.receiver

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_waiting_another_in_game, container, false)
        FirestoreDataManager.matchresultRef.addSnapshotListener{ snapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
            if(firebaseFirestoreException != null){
                Log.e(Constants.TAG, "Listen error: $firebaseFirestoreException")
                return@addSnapshotListener
            }
            for(doChange in snapshot!!.documentChanges){
                if(doChange.document.id == mr!!.id){
                    val match = MatchResult.matchResultFromSnapshot(doChange.document)
                    when (doChange.type){
                        DocumentChange.Type.MODIFIED -> {
                            Log.d(Constants.TAG, "Database modified!")
                            incomingUpdate(match, view!!)
                        }
                    }
                }
            }
        }
        return view
    }

    private fun incomingUpdate(match: MatchResult, view: View) {
        if (match.winner.equals(playId) && match.numCompleted == 1){
            Log.d(Constants.TAG, "Another player still not finished")
        }else if(!match.winner.equals(playId) || match.numCompleted == 2){
            Log.d(Constants.TAG, "Another player FINISHED")
            listener!!.onWAIGFragmentInteraction(mr, isChalllenger)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnWAIGFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
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
    interface OnWAIGFragmentInteractionListener {
        fun onWAIGFragmentInteraction(mr: MatchResult?, isChalllenger: Boolean?)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WaitingFragment.
         */
        @JvmStatic
        fun newInstance(mr: MatchResult, isChalllenger: Boolean) =
            WaitingAnotherInGameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_MR, mr)
                    putBoolean(ARG_ISCHALLENGER, isChalllenger)
                }
            }
    }
}
