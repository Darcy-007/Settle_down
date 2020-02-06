package com.example.settle_down

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.settle_down.Models.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_game_dash_board.view.*
import kotlin.random.Random

private const val ARG_PARAM1 = "gdb"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [GameDashBoardFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [GameDashBoardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameDashBoardFragment : Fragment() {
    private var mr: MatchResult? = null
    private var listener: OnGameDashboardFragmentInteractionListener? = null
    private val ref = FirebaseFirestore
        .getInstance()
        .collection("MatchResult")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mr = it.getParcelable(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_dash_board, container, false)
        view.game_dashboard_math.setOnClickListener {
            mr?.gameType = Constants.mathgame
            ref.document(mr!!.id).set(mr!!).addOnCompleteListener {
                var game = MathGame()
                listener?.onGameDashboardFragmentInteraction(mr, game)
            }
        }
        view.game_dashboard_computer.setOnClickListener {
            mr?.gameType = Constants.codinggame
            ref.document(mr!!.id).set(mr!!).addOnCompleteListener {
                var game = CodingGame()
                listener?.onGameDashboardFragmentInteraction(mr, game)
            }

        }
        view.game_dashboard_typing.setOnClickListener {
            FirestoreDataManager.typingGameRef.get().addOnSuccessListener {
                var game = it.documents[Random.nextInt(it.documents.size)].id
                Log.d("JSJSJJSJSJSJJSJSJSJJS", game)
                mr?.gameType = Constants.typegame
                mr!!.gameId = game
                ref.document(mr!!.id).set(mr!!)
                    .addOnSuccessListener {
                        listener?.onGameDashboardFragmentInteraction(mr, TypeGame())

                    }
            }
        }
        view.game_dashboard_dice.setOnClickListener {
            mr?.gameType = Constants.dicegame
            ref.document(mr!!.id).set(mr!!).addOnCompleteListener {
                var game = DiceGame()
                listener?.onGameDashboardFragmentInteraction(mr, game)
            }
        }

        return view

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnGameDashboardFragmentInteractionListener) {
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
    interface OnGameDashboardFragmentInteractionListener {
        fun onGameDashboardFragmentInteraction(mr: MatchResult?, game: Game)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GameDashBoardFragment.
         */
        @JvmStatic
        fun newInstance(mr: MatchResult) =
            GameDashBoardFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, mr)
                }
            }
    }
}
