package com.example.settle_down

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.settle_down.Models.MatchResult
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), HomeFragment.OnFragmentInteractionListener {
    private val auth = FirebaseAuth.getInstance()
    lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private val RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)
        if(savedInstanceState == null) {
            val startingFragment = StartingFragment()
            val ft = supportFragmentManager.beginTransaction()
            ft.add(R.id.fragment_container, startingFragment)
            ft.commit()
        }
        initializeListeners()
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener (authStateListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authStateListener)
    }

    private fun initializeListeners() {
        // TODO: Create an AuthStateListener that passes the UID
        // to the MovieQuoteFragment if the user is logged in
        // and goes back to the Splash fragment otherwise.
        // See https://firebase.google.com/docs/auth/users#the_user_lifecycle
        authStateListener = FirebaseAuth.AuthStateListener {auth: FirebaseAuth ->
            val user = auth.currentUser
            Log.d(Constants.TAG, "In anth listener, user = $user")
            if(user != null){
                Log.d(Constants.TAG, "UID: ${user.uid}")
                Log.d(Constants.TAG, "Name: ${user.displayName}")
                Log.d(Constants.TAG, "Email: ${user.email}")
                Log.d(Constants.TAG, "Phone: ${user.phoneNumber}")
                Log.d(Constants.TAG, "Photo URL: ${user.photoUrl}")
                switchToHomeFragment(user.uid, user.displayName, user.photoUrl)
            }else{
//                switchToFragment()
            }
        }

    }

    private fun switchToHomeFragment(uid: String, uname:String?, uphoto: Uri?) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, HomeFragment.newInstance(uid, uname, uphoto))
        ft.commit()
    }

    override fun onFragmentInteraction(mr: MatchResult) {
        Log.d("mr", mr.toString())
    }

}
