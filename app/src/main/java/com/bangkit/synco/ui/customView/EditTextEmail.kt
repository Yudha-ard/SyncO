package com.bangkit.synco.ui.customView


import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.bangkit.synco.R

class EditTextEmail : AppCompatEditText {
    private lateinit var emailButtonImage: Drawable

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        emailButtonImage = ContextCompat.getDrawable(context, R.drawable.baseline_email_24) as Drawable
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        compoundDrawablePadding = 16

        setDrawableWithStartMargin(emailButtonImage, startMargin = 16)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && !Patterns.EMAIL_ADDRESS.matcher(s).matches())
                    error = context.getString(R.string.email_error_message)
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        val paddingStart = paddingLeft // Current padding
        val additionalPadding = 40 // Additional padding
        setPadding(paddingStart + additionalPadding, paddingTop, paddingRight, paddingBottom)
    }
    private fun setDrawableWithStartMargin(
        start: Drawable? = null,
        top: Drawable? = null,
        end: Drawable? = null,
        bottom: Drawable? = null,
        startMargin: Int = 0
    ) {
        val startDrawable = start?.apply {
            setBounds(startMargin, 0, intrinsicWidth + startMargin, intrinsicHeight)
        }
        val topDrawable = top?.apply { setBounds(0, 0, intrinsicWidth, intrinsicHeight) }
        val endDrawable = end?.apply { setBounds(0, 0, intrinsicWidth, intrinsicHeight) }
        val bottomDrawable = bottom?.apply { setBounds(0, 0, intrinsicWidth, intrinsicHeight) }

        setCompoundDrawablesRelativeWithIntrinsicBounds(
            startDrawable, topDrawable, endDrawable, bottomDrawable
        )
    }

}