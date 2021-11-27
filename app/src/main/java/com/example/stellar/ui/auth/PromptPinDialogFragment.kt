package com.example.stellar.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import com.example.stellar.R
import com.example.stellar.databinding.FragmentPromptPinBinding

class PromptPinDialogFragment(private val onSuccess: (pin: String) -> Unit) : DialogFragment() {

    private lateinit var binding: FragmentPromptPinBinding
    private lateinit var etEnterPin: EditText

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

}