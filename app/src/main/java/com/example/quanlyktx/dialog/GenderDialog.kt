package com.example.quanlyktx.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import com.example.quanlyktx.R

class GenderDialog(context: Context, private val listener: GenderDialogListener) : Dialog(context) {

    private lateinit var genderRadioGroup: RadioGroup
    private lateinit var gender: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gender_dialog)
        gender = "Nam"
        genderRadioGroup = findViewById(R.id.gender_radio_group)
        val saveButton = findViewById<Button>(R.id.save_btn)

        saveButton.setOnClickListener {
            val selectedGender =
                when (genderRadioGroup.checkedRadioButtonId) {
                    R.id.male_radio_btn -> "Nam"
                    R.id.female_radio_btn -> "Nữ"
                    else -> "Khác"
                }
            listener.onGenderSelected(selectedGender)
            gender = selectedGender
            dismiss()
        }
    }



    interface GenderDialogListener {
        fun onGenderSelected(gender: String)
    }
}