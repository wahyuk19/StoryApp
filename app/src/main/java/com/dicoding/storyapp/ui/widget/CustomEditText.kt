package com.dicoding.storyapp.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.storyapp.R
import com.dicoding.storyapp.ui.utils.Constants

class CustomEditText : AppCompatEditText {
    private val typeName = 1
    private val typeEmail = 33
    private val typePassword = 129
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()

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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    private fun init() {
        when (inputType) {
            typeName -> {
                hint = resources.getString(R.string.hint_name)
            }
            typeEmail -> {
                hint = resources.getString(R.string.hint_email)
            }
            typePassword -> {
                hint = resources.getString(R.string.hint_password)
            }
        }
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        when (inputType) {
            typeEmail -> {
                if (!text.toString().matches(emailPattern)) {
                    error = resources.getString(R.string.error_email)
                    Constants.EMAIL_STATUS = false
                } else {
                    Constants.EMAIL_STATUS = true
                }
            }
            typePassword -> {
                if (text.toString().length < 6 || text.toString().length < 6) {
                    error = resources.getString(R.string.error_password)
                    Constants.PASSWORD_STATUS = false
                } else {
                    Constants.PASSWORD_STATUS = true
                }
            }
            typeName -> {
                Constants.NAME_STATUS = text.toString() != ""
                if (!Constants.NAME_STATUS) {
                    error = resources.getString(R.string.error_name)
                }
            }
        }
    }
}