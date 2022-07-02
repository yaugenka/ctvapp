package com.example.ctv

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

class PhoneFragment : Fragment() {
    companion object {
        const val TAG = "PhoneFragment"
    }

    private var resetInput = false
    private lateinit var inputEditText: EditText

    private var listener: PhoneFragmentListener? = null
    interface PhoneFragmentListener {
        fun onPhoneInput(phone: String, subs: Boolean)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is PhoneFragmentListener) listener = context
        else throw RuntimeException("$context must implement PhoneFragmentListener")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_phone, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        inputEditText = view.findViewById(R.id.inputEditText)
        inputEditText.apply {
            setSelection(text.length)
            showKeyboard()
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val text = text.toString().replace(Regex("""[\s-]"""),"")
                    val pattern = Regex("""\+7\d{3}""")
                    if (!text.matches(pattern))
                        error = getString(R.string.error_phone)
                    else {
                        hideKeyboard()
                        listener?.onPhoneInput(text,
                            when (radioGroup.checkedRadioButtonId) {
                                R.id.radioButton1 -> true
                                else -> false
                            }
                        )
                    }
                    true
                } else  false
            }
        }
        view.findViewById<ImageButton>(R.id.subsButton).setOnClickListener {
            radioGroup.apply {
                isVisible = !isVisible
            }
        }
    }

    fun resetInput() {
        resetInput = true
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (resetInput) {
            inputEditText.setText(R.string.value_phone_prefix)
            resetInput = false
        }
    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}