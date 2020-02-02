package com.example.settle_down

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.settle_down.Models.MatchResult

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "CG"
private const val ARG_PARAM2 = "ISC"

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
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mr = it.getParcelable(ARG_PARAM1)
            isChallenger = it.getBoolean(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coding_game, container, false)
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
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
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
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
                    putParcelable(ARG_PARAM1, mr)
                    putBoolean(ARG_PARAM2, isChallenger)
                }
            }
    }
}
