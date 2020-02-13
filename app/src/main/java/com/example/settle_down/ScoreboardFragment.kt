package com.example.settle_down

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.settle_down.Models.MatchResult
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_scoreboard_lose.view.*
import kotlinx.android.synthetic.main.fragment_scoreboard_win.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ScoreboardFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ScoreboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScoreboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var mr: MatchResult? = null
    private var isWinner: Boolean? = null
    private var listener: OnScoreboardFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mr = it.getParcelable(ARG_PARAM1)
            isWinner = it.getBoolean(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        var view:View?
        if(isWinner!!) {
            view = inflater.inflate(R.layout.fragment_scoreboard_win, container, false)
            view!!.SB_win_user_score.text = mr!!.challengerScore.toString()
            view!!.SB_win_another_score.text = mr!!.receiverScore.toString()
            view!!.scoreboardNice.setOnClickListener {
                var event = view!!.scoreboardMakeDecision.text.toString()
                if(event.isEmpty()){
                    event = "Nothing Special"
                }
                mr!!.eventResolved = event
                FirestoreDataManager.matchresultRef.document(mr!!.id).set(mr!!).addOnCompleteListener {
                    listener!!.onScoreboardFragmentInteraction()
                }
            }
        } else {
            view = inflater.inflate(R.layout.fragment_scoreboard_lose, container, false)
            view!!.SB_lose_user_score.text = mr!!.receiverScore.toString()
            view!!.SB_lose_another_score.text = mr!!.challengerScore.toString()
            view!!.scoreboardCrap.setOnClickListener {
                listener!!.onScoreboardFragmentInteraction()
            }


            Picasso.get()
                .load("https://img9.doubanio.com/lpic/o591865.jpg")
                .resize(6000, 2000)
                .onlyScaleDown()
//            .fit()
                .into(view!!.scoreboardFunPic)
        }

        return view
    }

    // TODO: Rename method, update argument and hook method into UI event


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnScoreboardFragmentInteractionListener) {
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
    interface OnScoreboardFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onScoreboardFragmentInteraction()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScoreboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(mr: MatchResult, isWinner: Boolean) =
            ScoreboardFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, mr)
                    putBoolean(ARG_PARAM2, isWinner)
                }
            }
    }
}
