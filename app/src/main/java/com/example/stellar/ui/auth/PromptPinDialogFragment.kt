package com.example.stellar.ui.auth

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import com.example.stellar.R
import com.example.stellar.databinding.FragmentPromptPinBinding
import android.content.pm.ActivityInfo




/**
 * Fragment for the pin code
 * User is required to give in 4 numbers which are then returned to other fragments in the application
 */

class PromptPinDialogFragment : DialogFragment {

    private var onSuccess: (String) -> Unit?

    constructor(onSuccess: (pin: String) -> Unit) {
        this.onSuccess = onSuccess
    }

    constructor(onSuccess: (pin: String) -> Unit, message: String?) {
        this.onSuccess = onSuccess
        this.message = message
    }

    private lateinit var binding: FragmentPromptPinBinding
    private lateinit var etEnterPin: EditText
    private var message: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPromptPinBinding.inflate(inflater, container, false)

        val i1 = binding.ivCircle1
        val i2 = binding.ivCircle2
        val i3 = binding.ivCircle3
        val i4 = binding.ivCircle4

        message?.let {
            val tvMessage = binding.tvMessage
            tvMessage.visibility = TextView.VISIBLE
            tvMessage.text = it
        }

        etEnterPin = binding.etEnterPin

        val manager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        etEnterPin.postDelayed( {
            etEnterPin.requestFocus()
            manager.showSoftInput(etEnterPin, 0)
        }, 0)

        binding.llPinPad.setOnClickListener {
            etEnterPin.requestFocus()
            manager.showSoftInput(etEnterPin, 0)
        }

        etEnterPin.doAfterTextChanged {
            when (it?.length) {
                4 -> {
                    i4.setImageResource(R.drawable.circle2)
                    onSuccess(etEnterPin.text.toString())
                    dismiss()
                }
                3 -> {
                    i3.setImageResource(R.drawable.circle2)
                    i4.setImageResource(R.drawable.circle)
                }
                2 -> {
                    i2.setImageResource(R.drawable.circle2)
                    i3.setImageResource(R.drawable.circle)
                }
                1 -> {
                    i1.setImageResource(R.drawable.circle2)
                    i2.setImageResource(R.drawable.circle)
                }
                else -> i1.setImageResource(R.drawable.circle)
            }
        }

        return binding.root
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onResume() {
        super.onResume()
        //lock screen
        requireActivity().requestedOrientation = resources.configuration.orientation
    }

    override fun onPause() {
        super.onPause()
        //set rotation to sensor dependent
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
    }

}