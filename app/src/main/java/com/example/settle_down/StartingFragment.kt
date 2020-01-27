package com.example.settle_down


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_starting.view.*

/**
 * A simple [Fragment] subclass.
 */
class StartingFragment : Fragment() {
    var listener: OnLoginButtonPressedListener? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_starting, container, false)
        view.first_page_log_in_button.setOnClickListener {
            listener?.onLoginButtonPressed()
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnLoginButtonPressedListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnLoginButtonPressedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnLoginButtonPressedListener {
        fun onLoginButtonPressed()
    }


}
