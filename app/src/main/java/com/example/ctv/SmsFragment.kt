package com.example.ctv

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.Fragment

class SmsFragment : Fragment() {
    companion object {
        const val TAG = "SmsFragment"
    }

    private var listener: SmsFragmentListener? = null
    interface SmsFragmentListener {
        fun onSmsInput()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SmsFragmentListener) listener = context
        else throw RuntimeException("$context must implement SmsFragmentListener")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_sms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<EditText>(R.id.inputEditText).apply {
            showKeyboard()
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (this.text.toString() != "1234")
                        error = getString(R.string.error_sms)
                    else {
                        hideKeyboard()
                        listener?.onSmsInput()
                    }
                    true
                } else false
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}