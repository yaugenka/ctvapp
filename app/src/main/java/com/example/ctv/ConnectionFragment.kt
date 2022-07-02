package com.example.ctv

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class ConnectionFragment : Fragment() {
    companion object {
        const val TAG = "ConnectionFragment"
    }

    private var listener: ConnectionFragmentListener? = null
    interface ConnectionFragmentListener {
        fun onConnect()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ConnectionFragmentListener) listener = context
        else throw RuntimeException("$context must implement ConnectionFragmentListener")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_connection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.connectButton).apply {
            requestFocus()
            setOnClickListener {
                listener?.onConnect()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}