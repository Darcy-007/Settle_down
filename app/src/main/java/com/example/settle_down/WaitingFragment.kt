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
import kotlinx.android.synthetic.main.fragment_waiting.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_ISCHALLENGER = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [WaitingFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [WaitingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WaitingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var mr: MatchResult? = null
    private var isChalllenger: Boolean? =null
    private var listener: OnWaitingFragmentInteractionListener? = null
    private val ref = FirebaseFirestore
        .getInstance()
        .collection("MatchResult")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mr = it.getParcelable(ARG_PARAM1)
            isChalllenger = it.getBoolean(ARG_ISCHALLENGER)
        }
    }

    init{
        if(mr!!.receiver.isEmpty()) {
            ref.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("error", "Listen error: $firebaseFirestoreException")
                }
                for (docChange in querySnapshot!!.documentChanges) {
                    val change = MatchResult.matchResultFromSnapshot(docChange.document)
                    if (change.code == mr!!.code && change.challenger.isNotEmpty() && change.winner.isEmpty()) {
                        when (docChange.type) {
                            DocumentChange.Type.MODIFIED -> {
                                waitingMsg.text = "Found Player, Loading Your Game Selection Page"
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_waiting, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onWaitingFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnWaitingFragmentInteractionListener) {
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
    interface OnWaitingFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onWaitingFragmentInteraction(uri: Uri)
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
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(mr: MatchResult, isChalllenger:Boolean) =
            WaitingFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, mr)
                    putBoolean(ARG_ISCHALLENGER, isChalllenger)
                }
            }
    }
}
