package com.ezam.playground.customviews

import android.app.DatePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.getResourceIdOrThrow
import com.google.android.material.textfield.TextInputLayout
import com.ezam.playground.R
import com.google.android.material.textfield.TextInputEditText


class InputDatePicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.inputDatePickerStyle
) : ConstraintLayout(context, attrs, defStyleAttr){

    val style: Int
    val textInputLayout: TextInputLayout

    var datePickerDialogStyle = 0
    init {


        with( context.obtainStyledAttributes(null, intArrayOf(R.attr.inputDatePickerStyle) ) ){
            style = getResourceIdOrThrow(0)
            recycle()
        }

        View.inflate( ContextThemeWrapper(context, style), R.layout.view_input_date_picker, this )
        //View.inflate(context, R.layout.view_input_date_picker, this)
        //LayoutInflater.from( ContextThemeWrapper(context, style) ).inflate( R.layout.view_input_date_picker, this, true)

        textInputLayout = findViewById(R.id.textInputLayout)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it,
                R.styleable.InputDatePicker,
                R.attr.inputDatePickerStyle,
                R.style.Widget_AppTheme_InputDatePicker
            )

            datePickerDialogStyle = typedArray.getResourceId( R.styleable.InputDatePicker_inputDateDialogStyle , 0)

            typedArray.recycle()
        }

        textInputLayout.editText?.setOnClickListener {
            openDialog()
        }
    }

    fun openDialog(){
        val dialog = DatePickerDialog(context, datePickerDialogStyle, onDateSetCallback,1 ,1, 1)
        dialog.show()
    }

    val onDateSetCallback = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

    }
}