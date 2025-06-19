package com.example.tugas_1_dicoding.custom

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import com.example.tugas_1_dicoding.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CustomEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "CustomPasswordEditText"
    }

    enum class Mode {
        CREATE_ACCOUNT, LOGIN, CONFIRM, NAME, EMAIL
    }

    val editTextField = TextInputEditText(context)
    private var confirmTarget: CustomEditText? = null

    var mode: Mode = Mode.CREATE_ACCOUNT
        set(value) {
            field = value
            validate(editTextField.text)
        }

    init {
        addView(editTextField)
        setupAttributes(attrs)
        setupEndIcon()
        setupListeners()
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomPasswordEditText)
            hint = typedArray.getString(R.styleable.CustomPasswordEditText_android_hint)
            val inputType = typedArray.getInt(
                R.styleable.CustomPasswordEditText_android_inputType,
                InputType.TYPE_CLASS_TEXT
            )
            editTextField.inputType = inputType
            typedArray.recycle()
        } ?: run {
            editTextField.inputType = InputType.TYPE_CLASS_TEXT
        }
    }

    private fun setupEndIcon() {
        val inputType = editTextField.inputType
        val isPassword =
            inputType and InputType.TYPE_TEXT_VARIATION_PASSWORD == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
                    inputType and InputType.TYPE_NUMBER_VARIATION_PASSWORD == InputType.TYPE_NUMBER_VARIATION_PASSWORD ||
                    inputType and InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD == InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD

        endIconMode = if (isPassword) END_ICON_PASSWORD_TOGGLE else END_ICON_NONE
    }

    private fun setupListeners() {
        editTextField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = validate(s)
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        editTextField.setOnFocusChangeListener { _, hasFocus ->
            Log.d(TAG, if (hasFocus) "Focused" else "Lost focus")
        }

        editTextField.setOnClickListener {
            Log.d(TAG, "Clicked")
        }

        this.setOnClickListener {
            Log.d(TAG, "Container clicked")
            editTextField.requestFocus()
        }
    }

    fun setConfirmTarget(target: CustomEditText) {
        confirmTarget = target
        mode = Mode.CONFIRM
    }

    private fun validate(s: Editable?) {
        val input = s?.toString() ?: ""

        if (input.isEmpty()) {
            error = null
            return
        }

        when (mode) {

            Mode.NAME ->{
                error = null
            }

            Mode.EMAIL -> {
                val emailPattern = android.util.Patterns.EMAIL_ADDRESS
                error = if (!emailPattern.matcher(input).matches()) "Format email tidak valid" else null
            }

            Mode.CONFIRM -> {
                val targetText = confirmTarget?.editTextField?.text?.toString() ?: ""
                error = if (input != targetText) "Konfirmasi password tidak cocok" else null
            }

            Mode.LOGIN -> {
                error = if (input.length < 10) "Input harus minimal 10 karakter" else null
            }

            Mode.CREATE_ACCOUNT -> {
                val errorList = mutableListOf<String>()
                if (input.length < 10) errorList.add("minimal 10 karakter")
                if (!input.any { it.isUpperCase() }) errorList.add("ada huruf kapital")
                if (!input.any { it.isDigit() }) errorList.add("ada angka")
                if (!input.any { !it.isLetterOrDigit() }) errorList.add("ada simbol")

                error = if (errorList.isNotEmpty())
                    "Input harus memenuhi:\n" + errorList.joinToString("\n")
                else null
            }
        }
    }

    fun clearText() {
        editTextField.text?.clear()
        error = null // Also clear any error shown
    }

}
