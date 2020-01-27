package com.example.settle_down

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.settle_down.Models.MatchResult
import com.example.settle_down.Models.MathGame
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.enter_room_dialog.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_UID = "UID"
private const val ARG_UNAME = "UNAME"
private const val ARG_UPHOTO = "UPHOTO"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private var uid: String? = null
    private var uname: String? = null
    private var uphoto: Uri? = null
    //    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private val ref = FirebaseFirestore
        .getInstance()
        .collection("MatchResult")


    private fun waitOrJoin(code:String){
        ref.get().addOnSuccessListener {
            for(mr in it){
                var matchtemp = mr.toObject(MatchResult::class.java)
                if(matchtemp.Code==code&&!matchtemp.isComplete) {
                    matchtemp.isComplete = true
                    matchtemp.Receiver=uname!!
                    listener?.onFragmentInteraction(matchtemp)
                }
            }
            var match: MatchResult = MatchResult(uname!!, 0,
                code, "", MathGame(), "", 0, "", false)
            ref.add(match)
            ref.addSnapshotListener{ querySnapshot, firebaseFirestoreException ->
                if(firebaseFirestoreException!=null){
                    Log.e("error", "Listen error: $firebaseFirestoreException")
                }
                for(docChange in querySnapshot!!.documentChanges){
                    val change = MatchResult.matchResultFromSnapshot(docChange.document)
                    if(change.Code==code&&change.Challenger==uname!!&&change.Winner.isEmpty()) {
                        when (docChange.type) {
                            DocumentChange.Type.MODIFIED -> {
                                listener?.onFragmentInteraction(change)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uid = it.getString(ARG_UID)
            uname = it.getString(ARG_UNAME)
            uphoto = it.getParcelable(ARG_UPHOTO)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        view.imageView.setImageURI(uphoto)
        view.home_head.text=uname
        view.home_button.setOnClickListener {
            val builder = AlertDialog.Builder(context!!)
            builder.setTitle("Enter Your Code Here")
            val view = LayoutInflater.from(context).inflate(
                R.layout.enter_room_dialog, null, false
            )
            builder.setView(view)
            builder.setIcon(android.R.drawable.ic_input_add)
            builder.setPositiveButton(android.R.string.ok) { _, _ ->
                val code = view.roomcode.text.toString()
                waitOrJoin(code)
            }
            builder.setNegativeButton(android.R.string.cancel, null)
            builder.show()
        }
        return view
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
        // TODO: Update argument type and name
        fun onFragmentInteraction(mr:MatchResult)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(uid: String, uname:String?, uphoto:Uri?) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_UID, uid)
                    putString(ARG_UNAME, uname)
                    putParcelable(ARG_UPHOTO, uphoto)
                }
            }
    }
}
