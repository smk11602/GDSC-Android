package com.myfirstandroidapp.emerg_med_info_app

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.myfirstandroidapp.emerg_med_info_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goEditActivityButton.setOnClickListener{
            val intent = Intent(this, EditActivity::class.java)

            startActivity(intent)

        }
        binding.deleteButton.setOnClickListener {
            deleteData()
        }
        binding.emergencyContactlayer.setOnClickListener{
            //암시적 인텐트 실행 -> 전화를 걸 수 있는 어떤 것..
            val intent = with(Intent(Intent.ACTION_VIEW)) {
                val phoneNumber = binding.PhoneTextView.text.toString()
                    .replace("-", "")
                data = Uri.parse("tel:$phoneNumber")
                startActivity(this)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        getDataUiUpdate() // 이 시점에서 불려야 메인액티비티로 돌아왔을 때 data와 ui가 업데이트 됨!
        deleteData()
    }

    private fun getDataUiUpdate() { // Data 값 가져오기
        with(getSharedPreferences(USER_INFORMATION, Context.MODE_PRIVATE)) {
            binding.nameValueTextView.text = getString(NAME, "미정")
            binding.BirthValueTextView.text = getString(BIRTHDATE, "미정")
            binding.BloodTypeValueTextView.text = getString(BLOODTYPE, "미정")
            binding.PhoneValueTextView.text = getString(EMERGENCY_CONTACT, "미정")
            val warning = getString(NOTICE, "미정")

            binding.NoticeTextView.isVisible = !warning.isNullOrEmpty().not()
            binding.NoticeValueTextView.isVisible = !warning.isNullOrEmpty().not()
            if(!warning.isNullOrEmpty()) {
               binding.NoticeValueTextView.text = warning
                }
            }
    }
    private fun deleteData() {
        with(getSharedPreferences(USER_INFORMATION, Context.MODE_PRIVATE).edit()) {
            clear()
            apply() //필수
            getDataUiUpdate()
        }
        Toast.makeText(this, "초기화를 완료했습니다.", Toast.LENGTH_SHORT).show()

    }
}
