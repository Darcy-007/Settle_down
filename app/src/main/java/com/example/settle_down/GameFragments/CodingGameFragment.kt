package com.example.settle_down.GameFragments

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import com.example.settle_down.Models.MatchResult
import com.example.settle_down.R
import kotlinx.android.synthetic.main.fragment_coding_game.view.*

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val CG = "CG"
private const val ISC = "ISC"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CodingGameFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CodingGameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CodingGameFragment : Fragment() {
    private var mr: MatchResult? = null
    private var isChallenger: Boolean? = null
    private var currentQuestion = 0
    private var currentCorrect = -1
    private var listener: OnCodingGameFragmentInteractionListener? = null
//    private var view: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mr = it.getParcelable(CG)
            isChallenger = it.getBoolean(ISC)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_coding_game, container, false)

        if(isChallenger!!){
            //red for challenger
            view.coding_len.setImageResource(R.drawable.ic_lens_red_24dp)
            view.coding_score.text = mr!!.challengerScore.toString()
            view.coding_score.setTextColor(ContextCompat.getColor(context!!, R.color.colorSdRed))
        }else{

        }
        setProblem(view)
        startAnimation()
        return view
    }

    private fun setProblem(view: View){
        FirestoreDataManager.codingingGameRef.document(mr!!.gameId!!.get(currentQuestion)).get().addOnSuccessListener {
            view.coding_question.text = (it["problem"]?:"")as String
            view.coding_button0.text = (it["choice0"]?:"")as String
            view.coding_button1.text = (it["choice1"]?:"")as String
            view.coding_button2.text = (it["choice2"]?:"")as String
            view.coding_button3.text = (it["choice3"]?:"")as String
            currentCorrect = (it["answer"]) as Int
            setOnClicks(view)
        }
    }

    private fun setOnClicks(view: View){
        view.coding_button0.setOnClickListener {

        }
    }

    private fun onClickHelper(buttonNum:Int, button: Button){
        if(buttonNum == currentCorrect){
            var docId = mr!!.gameId!!.get(currentQuestion)
            button.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorSdRed))
            FirestoreDataManager.codingingGameRef.document(docId).set(mr!!)
        }else{


        }
    }

    private fun startAnimation() {
        val mProgressBar = view!!.coding_progressBar as ProgressBar
        val progressAnimator = ObjectAnimator.ofInt(mProgressBar, "progress", 1000, 0)
        progressAnimator.setDuration(5000)
        progressAnimator.interpolator = LinearInterpolator()
        progressAnimator.doOnEnd {
        }
        progressAnimator.start()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCodingGameFragmentInteractionListener) {
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
    interface OnCodingGameFragmentInteractionListener {
        fun onCodingGameFragmentInteraction()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CodingGameFragment.
         */
        @JvmStatic
        fun newInstance(mr: MatchResult, isChallenger: Boolean) =
            CodingGameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CG, mr)
                    putBoolean(ISC, isChallenger)
                }
            }
    }
}
