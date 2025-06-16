package com.example.personalityquiz

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.os.SystemClock
import android.os.SystemClock.elapsedRealtime
import android.provider.MediaStore.Audio.Radio
import android.widget.Button
import android.widget.CheckBox
import android.widget.Checkable
import android.widget.Chronometer
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalityquiz.R.id.mainLayout

@Suppress("RedundantIf")
class MainActivity : AppCompatActivity() {
    private lateinit var quizDurationChronometer: Chronometer
    private lateinit var timeLeftCountDownTimer: TextView
    private lateinit var showDatePickerAndTimePickerButton: Button
    private lateinit var spendingTimeRadioGroup: RadioGroup
    private lateinit var appleCheckbox: CheckBox
    private lateinit var bananaCheckbox: CheckBox
    private lateinit var pearCheckbox: CheckBox
    private lateinit var dateResultTextView: TextView
    private lateinit var timeResultTextView: TextView
    private lateinit var submitDataButton: Button

    private var pauseOffset: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(mainLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        quizDurationChronometer = findViewById(R.id.quiz_duration_chronometer)
        quizDurationChronometer.format = "%m:%s"
        timeLeftCountDownTimer = findViewById(R.id.time_left_countdown_timer)

        spendingTimeRadioGroup = findViewById(R.id.spendingTime_radioGroup)
        var preferredTime = ""

        appleCheckbox = findViewById(R.id.apple_checkbox)
        var appleCheckboxIsChecked = false
        bananaCheckbox = findViewById(R.id.banana_checkbox)
        var bananaCheckboxIsChecked = false
        pearCheckbox = findViewById(R.id.pear_checkbox)
        var pearCheckboxIsChecked = false

        showDatePickerAndTimePickerButton = findViewById(R.id.open_timestamp_dialog)
        dateResultTextView = findViewById(R.id.date_result)
        timeResultTextView = findViewById(R.id.time_result)
        submitDataButton = findViewById(R.id.submit_button)


        spendingTimeRadioGroup.setOnCheckedChangeListener { _, _ ->
            getSelectedRadioButton()
        }


        appleCheckbox.setOnCheckedChangeListener { _, _ ->
            appleCheckboxIsChecked = if(appleCheckbox.isChecked){
                true
            } else {
                false
            }
        }
        bananaCheckbox.setOnCheckedChangeListener { _, _ ->
            bananaCheckboxIsChecked = if(bananaCheckbox.isChecked){
                true
            } else {
                false
            }
        }
        pearCheckbox.setOnCheckedChangeListener { _, _ ->
            pearCheckboxIsChecked = if(pearCheckbox.isChecked){
                true
            } else {
                false
            }
        }

        showDatePickerAndTimePickerButton.setOnClickListener {
            showDatePickerAndTimePicker()
        }

        submitDataButton.setOnClickListener {
            submitData()
        }
        startChronometer()
    }

    private fun getSelectedRadioButton(): String {
        val selectedID = spendingTimeRadioGroup.checkedRadioButtonId
        val selectedRadioButton = findViewById<RadioButton>(selectedID)
        return selectedRadioButton.text.toString()
    }

    private var chronometerIsRunning: Boolean = false
    private fun startChronometer() {
        if (!chronometerIsRunning) {
            quizDurationChronometer.base = elapsedRealtime()
            quizDurationChronometer.start()
            chronometerIsRunning = true
        }
    }
    private fun stopChronometer() {
        if (chronometerIsRunning) {
            quizDurationChronometer.stop()
            chronometerIsRunning = false
        }
    }
    private fun resetChronometer() {
        quizDurationChronometer.base = elapsedRealtime()
    }
    private fun getChronometerTime(): Long {
        return (elapsedRealtime() - quizDurationChronometer.base) / 1000
    }

    private fun getSelectedCheckboxes(): MutableList<String> {
        val selectedCheckboxes = mutableListOf<String>()
        if(appleCheckbox.isChecked) {
            selectedCheckboxes.add("jabłka")
        }
        if(bananaCheckbox.isChecked) {
            selectedCheckboxes.add("banany")
        }
        if(pearCheckbox.isChecked) {
            selectedCheckboxes.add("gruszki")
        }
        return selectedCheckboxes
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePickerAndTimePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val createdDatePickerDialog = DatePickerDialog(this,{_, selectedDay, selectedMonth, selectedYear ->
            dateResultTextView.text = "$selectedDay/$selectedMonth/$selectedYear"
        }, year, month, day)

        val createdTimePickerDialog = TimePickerDialog(this, {_ : TimePicker, selectedHour: Int, selectedMinute: Int ->
            timeResultTextView.text = "$selectedHour:$selectedMinute"
        }, hour, minute, true)

        createdDatePickerDialog.show()
        createdTimePickerDialog.show()
    }

    private fun submitData() {
        stopChronometer()

        Toast.makeText(this@MainActivity, "czas quizu: "+getChronometerTime()+" sekund", Toast.LENGTH_SHORT).show()
        Toast.makeText(this@MainActivity, "użytkownik woli spędzać czas "+getSelectedRadioButton(), Toast.LENGTH_SHORT).show()
        Toast.makeText(this@MainActivity, "lubiane owoce: "+getSelectedCheckboxes(), Toast.LENGTH_SHORT).show()
    }
}