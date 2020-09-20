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
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.textfield.TextInputLayout
import com.ezam.playground.R
import com.ezam.playground.commons.*
import com.google.android.material.textfield.TextInputEditText
import java.text.DateFormat
import java.util.*


class InputDatePicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.inputDatePickerStyle
) : ConstraintLayout(context, attrs, defStyleAttr){

    private lateinit var dateFormat: DateFormat

    private val style: Int
    private val textInputLayout: TextInputLayout

    private var datePicked: Date? = null
    private var datePickedCallback: (Date) -> Unit = {}
    private var datePickerDialogStyle = 0

    private var text: String?
        set(value) { textInputLayout.editText?.setText( value ) }
        get() = textInputLayout.editText?.text?.toString() ?: ""

    init {
        DateFormat.LONG

        with( context.obtainStyledAttributes(null, intArrayOf(R.attr.inputDatePickerStyle) ) ){
            style = getResourceIdOrThrow(0)
            recycle()
        }

        View.inflate( ContextThemeWrapper(context, style), R.layout.view_input_date_picker, this )
        //View.inflate(context, R.layout.view_input_date_picker, this)
        //LayoutInflater.from( ContextThemeWrapper(context, style) ).inflate( R.layout.view_input_date_picker, this, true)

        textInputLayout = findViewById(R.id.textInputLayout)

        var stringFormat: String? = null

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it,
                R.styleable.InputDatePicker,
                R.attr.inputDatePickerStyle,
                R.style.Widget_AppTheme_InputDatePicker
            )

            datePickerDialogStyle = typedArray.getResourceId( R.styleable.InputDatePicker_inputDateDialogStyle , 0)

            stringFormat = typedArray.getString(R.styleable.InputDatePicker_format).nullIfBlank()


            typedArray.recycle()
        }


        setFormat( stringFormat )

        textInputLayout.editText?.setOnClickListener {
            openDialog()
        }
    }

    fun setDatePickedListener( handler: (Date) -> Unit ){
        datePickedCallback = handler
    }

    fun openDialog(){

        val showDate = Calendar.getInstance( context.locale )

        datePicked?.let { showDate.time = it }

        val dialog = DatePickerDialog(
            context, datePickerDialogStyle, onDateSetCallback,
            showDate.get( Calendar.YEAR ) ,showDate.get( Calendar.MONTH), showDate.get( Calendar.DAY_OF_MONTH ) )

        dialog.show()
    }

    private var onDateSetCallback = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        val date = Calendar.getInstance().apply { set(year, month, dayOfMonth) }.time
        setDate( date )
    }

    fun setFormat(stringFormat: String?) {
        if( stringFormat == null ) {
            dateFormat = dateFormat()
        } else {
            dateFormat = simpleDateFormatOf(stringFormat, resources.configuration)
        }

        datePicked?.let { text = dateFormat.formatOrNull(it) }
    }

    fun setDate(date: Date?){
        if( date?.time == datePicked?.time ) return
        datePicked = date
        text = dateFormat.formatOrNull( date )
        date?.let{ datePickedCallback( date ) }
    }


    fun getDate() : Date? = datePicked?.let { Date( it.time ) }
}

@BindingAdapter("dateAttrChanged")
fun InputDatePicker.setListener( attrChange: InverseBindingListener?){
    setDatePickedListener {
        attrChange?.onChange()
    }
}

@InverseBindingAdapter(attribute = "date")
fun InputDatePicker.getDateValue() : Date ? {
    return this.getDate()
}