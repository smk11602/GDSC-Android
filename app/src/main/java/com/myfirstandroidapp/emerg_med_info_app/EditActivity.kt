package com.myfirstandroidapp.emerg_med_info_app

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import com.myfirstandroidapp.emerg_med_info_app.databinding.ActivityEditBinding
import com.myfirstandroidapp.emerg_med_info_app.databinding.ActivityMainBinding

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.BloodTypeSpinner.adapter = ArrayAdapter.createFromResource(
            //리스트. 배열이나 컬렉션에 있는 데이터들을 UI로 연결할 때 쓰는 adapter
            this,
            R.array.blood_types,
            android.R.layout.simple_list_item_1
        )
        binding.Birthlayer.setOnClickListener{
            val listener = OnDateSetListener{ _, year, month, dayOfMonth ->
                binding.BirthTextView.text = "$year-${month.inc()}-$dayOfMonth"
            } // 안드는 month.inc()를 해줘야 달이 정확하게 나옴
            DatePickerDialog( // 미리 지정한 날짜
                this,
                listener,
                2001,
                2,
                19
            ).show() // 보여줘야 함
        }

        binding.NoticeCheckbox.setOnCheckedChangeListener { _, isChecked ->
            binding.NoticeEditText.isVisible = isChecked
        }
        binding.NoticeEditText.isVisible = binding.NoticeCheckbox.isChecked

        binding.SaveButton.setOnClickListener {
            saveData()
            finish() // activity 종료
        }
    }

    private fun saveData(){
        //키-값 데이터 저장 (데이터가 비교적 작은 경우)
        with(getSharedPreferences("userInfromation", Context.MODE_PRIVATE).edit()) {
            putString(NAME, binding.nameEditText.text.toString())
            putString(BLOODTYPE,getBloodType())
            putString(EMERGENCY_CONTACT, binding.PhoneEditText.text.toString())
            putString(BIRTHDATE, binding.BirthTextView.text.toString())
            putString(NOTICE, getNotice())
            apply() //필수
        }
        Toast.makeText(this,"저장을 완료했습니다.", Toast.LENGTH_SHORT).show()

    }

    private fun getBloodType(): String {
        val bloodAlphabet = binding.BloodTypeSpinner.selectedItem.toString()
        val bloodSign = if(binding.BloodTypePlus.isChecked) "+" else "-"
        return "$bloodSign$bloodAlphabet"
    }
    private fun getNotice(): String {
        return if(binding.NoticeCheckbox.isChecked) binding.NoticeEditText.text.toString() else ""
    }
}