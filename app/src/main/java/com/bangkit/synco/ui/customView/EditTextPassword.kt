package com.bangkit.synco.ui.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.bangkit.synco.R

class EditTextPassword : AppCompatEditText {

    private lateinit var passwordIconDrawable: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        transformationMethod = PasswordTransformationMethod.getInstance()
    }

    private fun init() {
        passwordIconDrawable =
            ContextCompat.getDrawable(context, R.drawable.baseline_key_24) as Drawable
        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        compoundDrawablePadding = 16

        setDrawableWithStartMargin(passwordIconDrawable, startMargin = 16)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && s.length < 8)
                    error = context.getString(R.string.password_error_message)
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