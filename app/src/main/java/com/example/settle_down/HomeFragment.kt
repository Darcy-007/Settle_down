package com.example.settle_down

import android.app.ProgressDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import com.example.settle_down.Models.MatchResult
import com.example.settle_down.Models.MathGame
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.enter_room_dialog.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.QuerySnapshot
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_U = "U"

//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(){


    private var user: FirebaseUser? = null
    private var uname: String? = null
    //    private var param2: String? = null
    private var listener: OnHomeFragmentInteractionListener? = null

    private val ref = FirebaseFirestore
        .getInstance()
        .collection("MatchResult")


    private fun waitOrJoin(code:String){
        var challenger = true

        ref.get().addOnCompleteListener{
            Log.d("code", code)
            for (mr in it.result!!.documents) {
                var matchtemp = mr.toObject(MatchResult::class.java)
                if (matchtemp!!.code == code && !matchtemp.complete) {
                    Log.d("inhome", matchtemp.toString())
                    matchtemp.complete = true
                    matchtemp.receiver = user!!.uid
                    ref.document(mr.id).set(matchtemp)
                    listener?.onHomeFragmentInteraction(matchtemp, false)
                    challenger = false
                    break
                }
            }

            if(challenger){

                var match: MatchResult = MatchResult(user!!.uid, 0,
                    code, false, "", -1, "", 0, "", ArrayList())
                ref.add(match).addOnSuccessListener {
                    listener?.onHomeFragmentInteraction(match, true)
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getParcelable(ARG_U)
        }
        if(user!!.displayName != null) uname = user!!.displayName else uname = user!!.phoneNumber


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        if(user!!.photoUrl == null){
            Log.d(Constants.TAG, "!!!!!")
            view.imageView.setImageResource(R.drawable.ic_account_circle_black_24dp)
        }else{
            Picasso.get()
                .load(user!!.photoUrl)
//                .resize(30,30)
////                .onlyScaleDown()
//            .fit()
                .into(view.imageView)
        }
        view.username.text = uname
//        view.home_button.setOnKeyListener { v, keyCode, event ->
//            Log.d("CODE!!!!", keyCode.toString())
//            true
//        }
        view.imageView.setOnClickListener {
            listener!!.onHomeFragmentToHistory(user!!)
        }


//        view.imageView.setImageResource(R.drawable.ic_account_circle_black_24dp)
        view.home_button.setOnClickListener {
            Log.d("AFTER" ,"Fine")

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
//            builder.setOnClickListener {
//            }

            builder.show()
        }

        return view
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnHomeFragmentInteractionListener) {
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
    interface OnHomeFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onHomeFragmentInteraction(mr:MatchResult, isChallenger: Boolean)
        fun onHomeFragmentToHistory(user:FirebaseUser)
        fun showTestSoftKeyboard(view: View)
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
        fun newInstance(user:FirebaseUser) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_U, user)

                }
            }
    }
}
