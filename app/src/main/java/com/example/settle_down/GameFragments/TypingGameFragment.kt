package com.example.settle_down.GameFragments

import FirestoreDataManager
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.settle_down.Constants
import com.example.settle_down.Models.MatchResult
import com.example.settle_down.Models.TypeGame
import com.example.settle_down.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_typing_game.view.*
import kotlin.random.Random
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import com.google.firebase.firestore.DocumentChange
import kotlinx.android.synthetic.main.fragment_waiting.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val MR = "MR"
private const val IC = "IC"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TypingGameFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TypingGameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TypingGameFragment : Fragment() {


    private val ref = FirebaseFirestore
        .getInstance()
        .collection("MatchResult")
    // TODO: Rename and change types of parameters
    private var mr: MatchResult? = null
    private var isChallenger: Boolean? = null
    private var listener: OnTypingGameFragmentInteractionListener? = null
    private var question: TypeGame? = null
    private var vie: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            mr = it.getParcelable(MR)
            isChallenger = it.getBoolean(IC)
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }

        ref.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                Log.e("error", "Listen error: $firebaseFirestoreException")
            }
            for (docChange in querySnapshot!!.documentChanges) {
                val change = MatchResult.matchResultFromSnapshot(docChange.document)
                if (change.id == mr!!.id) {
                    when (docChange.type) {
                        DocumentChange.Type.MODIFIED -> {
                           mr = change
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

        vie = inflater.inflate(R.layout.fragment_typing_game, container, false)
        Log.d("LLLLOOOOOGGGG", mr.toString())
        FirestoreDataManager.typingGameRef
            .document(mr!!.gameId!![0])
            .get()
            .addOnSuccessListener {
                val game = it.toObject(TypeGame::class.java)
                vie!!.TypingProblem.text = game!!.Problem
                var problem = game!!.Problem
                var currentProgress = ""
                var currentIndex = 0
                val filterTextWatcher = object : TextWatcher {
                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        Log.d("EDITTEXTTTTTT", s.toString())
                        currentProgress = s.toString()
                        val spannable = SpannableString(problem)
                        if (currentProgress.equals(problem)) {
                            Log.d("YES!!!!!", "DONE!!!!!")
                            if(isChallenger!!){
                                mr!!.challengerScore=1
                                mr!!.winner = mr!!.challenger
                            }else{
                                mr!!.receiverScore=1
                                mr!!.winner = mr!!.receiver
                            }

                            Log.d("LOG", mr!!.id)
                            Log.d("LOG", mr!!.toString())

                            FirestoreDataManager.matchresultRef.document(mr!!.id).set(mr!!).addOnSuccessListener {
                                Log.d("LOG", listener.toString())
                                listener!!.onTypingGameFragmentInteraction(mr!!, true)
                            }
                            return
                        }
                        for (i in currentProgress.indices) {
                            if (problem.get(i) != currentProgress.get(i)) {
                                spannable.setSpan(
                                    ForegroundColorSpan(Color.RED),
                                    i, i + 1,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                            } else {
                                spannable.setSpan(
                                    ForegroundColorSpan(Color.rgb(103, 161, 98)),
                                    i, i + 1,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                            }
                        }
                        vie!!.TypingProblem.text = spannable
                    }

                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable) {}
                }
                vie!!.TypingGameUserInput.addTextChangedListener(filterTextWatcher)

            }

        return vie
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnTypingGameFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
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
    interface OnTypingGameFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onTypingGameFragmentInteraction(mr:MatchResult, isWinner:Boolean)

        fun showSoftKeyboard(view: View)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TypingGameFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(mr: MatchResult, isChallenger: Boolean) =
            TypingGameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(MR, mr)
                    putBoolean(IC, isChallenger)
                }
            }
    }
}
