package com.example.gservices.utils

import android.R
import android.text.Spanned
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.example.gservices.models.categories.Categories

class CommonBindingAdapter {

    companion object {

        @BindingAdapter("htmlTxt")
        @JvmStatic
        fun htmlTxt(textView: TextView, text: Spanned) {
            textView.text = text
        }







    }
}