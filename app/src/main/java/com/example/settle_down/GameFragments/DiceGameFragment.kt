package com.example.settle_down.GameFragments

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.example.settle_down.Models.MatchResult
import com.example.settle_down.R
import com.google.firebase.firestore.DocumentChange
import kotlinx.android.synthetic.main.fragment_dice_game.view.*
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DiceGameFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DiceGameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiceGameFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var mr: MatchResult? = null
    private var isChallenger: Boolean? = null
    private var listener: OnDiceGameFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mr = it.getParcelable(ARG_PARAM1)
            isChallenger = it.getBoolean(ARG_PARAM2)
        }

        FirestoreDataManager.matchresultRef.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                Log.e("error", "Listen error: $firebaseFirestoreException")
            }
            for (docChange in querySnapshot!!.documentChanges) {
                val change = MatchResult.matchResultFromSnapshot(docChange.document)
                if (change.id == mr!!.id) {

                }
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_dice_game, container, false)
        var currentValue = rollRandomDie()
        var currentIndex = 1
        val total = 21
        view.Dice1.setImageResource(randomDieImg(currentValue))
        view.DiceGameValue.text = "Your Current Value Is: $currentValue"
        var idArray = arrayListOf<ImageView>(view.Dice1,view.Dice2, view.Dice3, view.Dice4, view.Dice5, view.Dice6, view.Dice7, view.Dice8, view.Dice9, view.Dice10, view.Dice11,
            view.Dice12, view.Dice13, view.Dice14, view.Dice15, view.Dice16, view.Dice17, view.Dice18, view.Dice19, view.Dice20, view.Dice21)
        view.DiceGameAddOne.setOnClickListener {
            var imgView = idArray[currentIndex]
            var new = rollRandomDie()
            imgView.setImageResource(randomDieImg(new))
            currentValue+=new
            currentIndex++
            view.DiceGameValue.text = "Your Current Value Is: $currentValue"
            //if all go over 21, the first one goes over
            if(currentValue >21){
                view.DiceGameValue.setTextColor(ContextCompat.getColor(context!!, R.color.colorSdRed))
                if(isChallenger!!){
                    mr!!.challengerScore = 0
                    mr!!.winner = mr!!.challenger
                }else{
                    mr!!.receiverScore = 0
                    mr!!.winner = mr!!.receiver
                }
                FirestoreDataManager.matchresultRef.document(mr!!.id).set(mr!!).addOnSuccessListener {
                    if(mr!!.winner.isNotEmpty()){
                        listener!!.onDiceGameFragmentInteraction(mr!!, true)
                    }else{

                    }
                }
            }else if(currentValue in 18..21){
                view.DiceGameValue.setTextColor(ContextCompat.getColor(context!!, R.color.colorSdGreen))
            }
        }

        view.DiceGameSubmit.setOnClickListener {
            if(isChallenger!!){
                mr!!.challengerScore = currentValue
                if(mr!!.winner.isNotEmpty()){
                    if(mr!!.receiverScore<mr!!.challengerScore){
                        mr!!.winner = mr!!.challenger
                    }
                }else{
                    mr!!.winner = mr!!.challenger
                }
            }else{
                mr!!.receiverScore = currentValue
                if(mr!!.winner.isNotEmpty()){
                    if(mr!!.receiverScore>mr!!.challengerScore){
                        mr!!.winner = mr!!.receiver
                    }
                }else{
                    mr!!.winner = mr!!.receiver
                }
            }
            FirestoreDataManager.matchresultRef.document(mr!!.id).set(mr!!).addOnSuccessListener {
                if(mr!!.winner.isNotEmpty()){
                    listener!!.onDiceGameFragmentInteraction(mr!!, true)
                }else{

                }
            }



        }

        return view
    }

    private fun rollRandomDie():Int{
        return Random.nextInt(1,7)
    }
    private fun randomDieImg(die:Int): Int{
        return when(die){
            1 -> R.drawable.dice1
            2 -> R.drawable.dice2
            3 -> R.drawable.dice3
            4 -> R.drawable.dice4
            5 -> R.drawable.dice5
            6 -> R.drawable.dice6
            else -> 0
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDiceGameFragmentInteractionListener) {
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
    interface OnDiceGameFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onDiceGameFragmentInteraction(mr:MatchResult, isWinner: Boolean)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DiceGameFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(mr: MatchResult, isChallenger: Boolean) =
            DiceGameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, mr)
                    putBoolean(ARG_PARAM2, isChallenger)
                }
            }
    }
}
