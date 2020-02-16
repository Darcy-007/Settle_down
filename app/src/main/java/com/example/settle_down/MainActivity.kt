package com.example.settle_down

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.example.settle_down.GameFragments.CodingGameFragment
import com.example.settle_down.GameFragments.DiceGameFragment
import com.example.settle_down.GameFragments.TypingGameFragment
import com.example.settle_down.Models.*
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity(),
    StartingFragment.OnLoginButtonPressedListener,
    HomeFragment.OnHomeFragmentInteractionListener,
    WaitingFragment.OnWaitingFragmentInteractionListener,
    GameDashBoardFragment.OnGameDashboardFragmentInteractionListener,
    CodingGameFragment.OnCodingGameFragmentInteractionListener,
    TypingGameFragment.OnTypingGameFragmentInteractionListener,
    DiceGameFragment.OnDiceGameFragmentInteractionListener,
    WaitingAnotherInGameFragment.OnWAIGFragmentInteractionListener,
    ScoreboardFragment.OnScoreboardFragmentInteractionListener,
    HistoryFragment.OnHistoryFragmentInteractionListener {


    private val auth = FirebaseAuth.getInstance()
    lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private val RC_SIGN_IN = 1
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)
        if (savedInstanceState == null) {
            val startingFragment = StartingFragment()
            val ft = supportFragmentManager.beginTransaction()
            ft.add(R.id.fragment_container, startingFragment)
            ft.commit()
        }
        initializeListeners()

    }

    override fun showSoftKeyboard(view: View) {
        Log.d("KEYBOARD", "Show")
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        view.setOnKeyListener { v, keyCode, event ->
            Log.d("CODE!!!!", keyCode.toString())
            true
        }
    }

    override fun showTestSoftKeyboard(view: View) {
        Log.d("KEYBOARD", "Show")
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        view.setOnKeyListener { v, keyCode, event ->
            //            Log.d("CODE!!!!", keyCode.toString())
            true
        }
    }


    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authStateListener)
    }

    private fun initializeListeners() {
        // to the MovieQuoteFragment if the user is logged in
        // and goes back to the Splash fragment otherwise.
        // See https://firebase.google.com/docs/auth/users#the_user_lifecycle
        authStateListener = FirebaseAuth.AuthStateListener { auth: FirebaseAuth ->

            if (auth.currentUser != null) {
                user = auth.currentUser!!
                Log.d(Constants.TAG, "UID: ${user.uid}")
                Log.d(Constants.TAG, "Name: ${user.displayName}")
                Log.d(Constants.TAG, "Email: ${user.email}")
                Log.d(Constants.TAG, "Phone: ${user.phoneNumber}")
                Log.d(Constants.TAG, "Photo URL: ${user.photoUrl}")
                switchToHomeFragment(user)
            } else {
                switchToStartingFragment()
            }
        }

    }

    private fun switchToStartingFragment() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, StartingFragment())
        ft.commit()
    }


    private fun switchToHomeFragment(user: FirebaseUser) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, HomeFragment.newInstance(user))
        ft.commit()
    }

    override fun onLoginButtonPressed() {
        launchLoginUI()
    }

    private fun launchLoginUI() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val loginIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        startActivityForResult(loginIntent, RC_SIGN_IN)
    }


    override fun onHomeFragmentInteraction(mr: MatchResult, isChallenger: Boolean) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, WaitingFragment.newInstance(mr, isChallenger))
        ft.commit()
    }

    override fun onWaitingFragmentInteraction(isChallenger: Boolean?, mr: MatchResult?) {
        if (isChallenger!!) {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.fragment_container, GameDashBoardFragment.newInstance(mr!!))
            ft.commit()
        } else {
            val ft = supportFragmentManager.beginTransaction()
            when (mr!!.gameType) {
                Constants.mathgame -> {
                    Log.d(Constants.TAG, "0")
                    ft.replace(R.id.fragment_container, CodingGameFragment.newInstance(mr!!, false))
                }
                Constants.codinggame -> {
                    Log.d(Constants.TAG, "1")
                    ft.replace(R.id.fragment_container, CodingGameFragment.newInstance(mr!!, false))
                }
                Constants.typegame -> {

                    Log.d(Constants.TAG, "2")
                    ft.replace(R.id.fragment_container, TypingGameFragment.newInstance(mr!!, false))
                }
                Constants.dicegame -> {
                    Log.d(Constants.TAG, "3")
                    ft.replace(R.id.fragment_container, DiceGameFragment.newInstance(mr!!, false))
                }
            }
            ft.commit()
        }
    }

    override fun onGameDashboardFragmentInteraction(mr: MatchResult?, game: Game) {
        val ft = supportFragmentManager.beginTransaction()

        if (game is MathGame) {
            ft.replace(R.id.fragment_container, CodingGameFragment.newInstance(mr!!, true))
        } else if (game is CodingGame) {
            ft.replace(R.id.fragment_container, CodingGameFragment.newInstance(mr!!, true))
        } else if (game is TypeGame) {
            ft.replace(R.id.fragment_container, TypingGameFragment.newInstance(mr!!, true))
        } else {
            ft.replace(R.id.fragment_container, DiceGameFragment.newInstance(mr!!, true))
        }
        ft.commit()
    }

    override fun onCodingGameFragmentInteraction(mr: MatchResult, isChallenger: Boolean) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(
            R.id.fragment_container,
            WaitingAnotherInGameFragment.newInstance(mr, isChallenger)
        )
        ft.commit()
    }

    override fun onTypingGameFragmentInteraction(mr: MatchResult, isWinner: Boolean) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, ScoreboardFragment.newInstance(mr, isWinner))
        ft.commit()
    }

    override fun onDiceGameFragmentInteraction(mr: MatchResult, isWinner: Boolean) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, ScoreboardFragment.newInstance(mr, isWinner))
        ft.commit()
    }

    override fun onScoreboardFragmentInteraction() {
        switchToHomeFragment(user!!)
    }

    override fun onWAIGFragmentInteraction(mr: MatchResult?, isChalllenger: Boolean?) {
        val ft = supportFragmentManager.beginTransaction()
        val myId = if (isChalllenger!!) mr!!.challenger else mr!!.receiver
        val myResult: Boolean = mr.winner == myId
        ft.replace(R.id.fragment_container, ScoreboardFragment.newInstance(mr!!, myResult))
        ft.commit()
    }

    override fun onFinishedInteraction(mr: MatchResult, result: Boolean) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, ScoreboardFragment.newInstance(mr!!, result))
        mr.numCompleted = -1
        FirestoreDataManager.matchresultRef.document(mr.id).set(mr!!)
        ft.commit()
    }


    override fun onHomeFragmentToHistory(user: FirebaseUser) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, HistoryFragment.newInstance(user))
        ft.addToBackStack("detail")
        ft.commit()
    }

    override fun onHistoryFragmentInteraction() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, HomeFragment.newInstance(user))
        while(supportFragmentManager.backStackEntryCount>0){
            supportFragmentManager.popBackStackImmediate()
        }
        ft.commit()
    }

}
