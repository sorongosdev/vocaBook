package com.sorongos.vocabook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sorongos.vocabook.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), WordAdapter.ItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var wordAdapter: WordAdapter

    /**addActivity의 result를 들어볼 수 있다
     * addActivity를 들여다보기 위해 사용*/
    private val updateAddWordResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val isUpdated = result.data?.getBooleanExtra("isUpdated", false) ?: false

        if (result.resultCode == RESULT_OK && isUpdated) { //add를 했을때만 결과값을 받아옴
            //추가되면 맨 위에 내용이 들어가야함
            //추가된 맨 위에 내용만 받아오자
            //update 되면 데이터를 가지고 오고, 어댑터에 알려줌
            updateAddWord()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

        //단어 추가 화면으로 넘어가는 플러스버튼
        binding.addButton.setOnClickListener {
            Intent(this, AddActivity::class.java).let {
                startActivity(it)
                updateAddWordResult.launch(it) // launch를 통해 실행
            }
        }
    }

    /**리사이클러뷰 초기화*/
    private fun initRecyclerView() {

        wordAdapter = WordAdapter(mutableListOf(), this)
        binding.wordRecyclerView.apply {
            adapter = wordAdapter
            layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            //디바이더 넣기
            val dividerItemDecoration =
                DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)
            addItemDecoration(dividerItemDecoration)
        }
        Thread {
            // db read
            val list = AppDatabase.getInstance(this)?.wordDao()?.getAll() ?: emptyList()
            wordAdapter.list.addAll(list) // 어댑터에 db로 받아온 정보를 넣음
            runOnUiThread {
                // 데이터가 바뀌었다는 것을 알려줌, UI가 변경
                // 부하가 많이 걸림
                wordAdapter.notifyDataSetChanged()
            }
        }.start()
    }

    private fun updateAddWord() {
        Thread { //데이터베이스를 열기 위해
            AppDatabase.getInstance(this)?.wordDao()?.getLatestWord()?.let { word ->
                wordAdapter.list.add(0, word)
                runOnUiThread { // 데이터 추가한걸 아려야함
                    wordAdapter.notifyDataSetChanged()
                }
            } //최근것을 갖고옴, null
            // safe
        }.start()
    }

    override fun onClick(word: Word) {
        Toast.makeText(this, "${word.text}가 클릭 됐습니다", Toast.LENGTH_SHORT).show()
    }

}