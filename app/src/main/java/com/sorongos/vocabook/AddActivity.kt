package com.sorongos.vocabook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.chip.Chip
import com.sorongos.vocabook.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        /**단어추가*/
        binding.addButton.setOnClickListener {
            add()
        }
    }

    private fun initViews() {
        val types = listOf(
            "명사", "동사", "대명사", "형용사",
            "부사", "감탄사", "전치사", "점속사"
        )
        binding.typeChipGroup.apply {
            //리스트의 아이템을 칩으로 변환
            types.forEach { text ->
                addView(createChip(text))
            }
        }
    }

    /**칩 생성, 칩 그룹 안에*/
    private fun createChip(text: String): Chip {
        return Chip(this).apply {
            setText(text)
            isCheckable = true
            isClickable = true
        }
    }

    /**addButton을 눌렀을 때, Word 인스턴스로 변환*/
    private fun add(){
        val text = binding.textInputEditText.text.toString()
        val mean = binding.meanTextInputEditText.text.toString()
        val type = findViewById<Chip>(binding.typeChipGroup.checkedChipId).text.toString() //check된 칩을 받아옴
        val word = Word(text, mean, type)

        Thread{ //데이터베이스는 워커 쓰레드에서
            AppDatabase.getInstance(this)?.wordDao()?.insert(word) // 있을 때만 작업
            runOnUiThread {
                Toast.makeText(this,"저장을 완료했습니다",Toast.LENGTH_SHORT).show()

            }
            finish()
        }.start()
    }
}