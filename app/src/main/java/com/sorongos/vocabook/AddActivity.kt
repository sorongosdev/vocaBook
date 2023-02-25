package com.sorongos.vocabook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.chip.Chip
import com.sorongos.vocabook.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_add)

        initViews()
    }

    private fun initViews(){
        val types = listOf(
            "명사","동사","대명사","형용사",
            "부사","감탄사","전치사","점속사"
        )
        binding.typeChipGroup.apply{
            //리스트의 아이템을 칩으로 변환
            types.forEach{ text ->
                addView(createChip(text))
            }
        }
    }

    /**칩 생성, 칩 그룹 안에*/
    private fun createChip(text: String) : Chip{
        return Chip(this).apply {
            setText(text)
            isCheckable = true
            isClickable = true
        }
    }
}