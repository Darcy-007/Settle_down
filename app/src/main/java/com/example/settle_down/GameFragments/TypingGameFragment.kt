package com.example.settle_down.GameFragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.settle_down.Models.TypeGame
import com.example.settle_down.R
import kotlinx.android.synthetic.main.fragment_typing_game.view.*
import kotlin.random.Random

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
    // TODO: Rename and change types of parameters
    private var mr: MatchResult? = null
    private var isChallenger: Boolean? = null
    private var listener: OnTypingGameFragmentInteractionListener? = null
    private var question: TypeGame? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_typing_game, container, false)
        FirestoreDataManager.typingGameRef.get().addOnSuccessListener {
            question = it.documents[Random.nextInt(it.documents.size)].toObject(TypeGame::class.java)
            view.problem.text = question!!.Problem
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnTypingGameFragmentInteractionListener) {
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
    interface OnTypingGameFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onTypingGameFragmentInteraction(uri: Uri)
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
        fun newInstance(mr: com.example.settle_down.Models.MatchResult, isChallenger: Boolean) =
            TypingGameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(MR, mr)
                    putBoolean(IC, isChallenger)
                }
            }
    }
}