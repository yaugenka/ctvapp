package com.example.ctv

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
    activity?.currentFocus?.let {
        context?.imm()?.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

fun View.showKeyboard() {
    requestFocus()
    context.imm().showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun Context.imm(): InputMethodManager =
    getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager