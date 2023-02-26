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
    private var selectedWord: Word? = null // 선택된 word를 Word 인스턴스로 저장

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

    /**데이터를 받고나면 업데이트가 될거다*/
    private val updateEditWordResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // word 자체를 받는다
        val editWord = result.data?.getParcelableExtra<Word>("editWord")

        if (result.resultCode == RESULT_OK && editWord != null) { //add를 했을때만 결과값을 받아옴
            updateEditWord(editWord)
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

        binding.deleteView.setOnClickListener {
            delete()
        }
        binding.editImageView.setOnClickListener {
            edit()
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

    private fun updateEditWord(word: Word) { // 어떤게 변경되어 있는지 모르니까 word를 받아야한다
        //값을 찾아야한다
        //데이터의 내용만 변경, 아이디는 변경x
        val index = wordAdapter.list.indexOfFirst { it.id == word.id }
        wordAdapter.list[index] = word
        runOnUiThread {
            selectedWord = word
            wordAdapter.notifyItemChanged(index)
            binding.textTextView.text = word.text
            binding.meanTextView.text = word.mean
        }

    }

    /**최상단 단어 삭제*/
    private fun delete() {
        //선택된 게 없다면
        if (selectedWord == null) return

        Thread {
            selectedWord?.let { word ->
                AppDatabase.getInstance(this)?.wordDao()?.delete(word)
                runOnUiThread {
                    wordAdapter.list.remove(word) // 어댑터에서 삭제
                    wordAdapter?.notifyDataSetChanged()
                    binding.textTextView.text = "" // 위에서 삭제
                    binding.meanTextView.text = ""
                    Toast.makeText(this, "삭제 완료", Toast.LENGTH_SHORT).show()
                }

            }
        }.start()
    }

    private fun edit() {
        if (selectedWord == null) return

        val intent = Intent(this, AddActivity::class.java).putExtra("originWord", selectedWord)
        updateEditWordResult.launch(intent) // 해줘야함
    }

    /**텍스트가 선택되면 최상단에 단어를 띄움*/
    override fun onClick(word: Word) {
        selectedWord = word
        binding.textTextView.text = word.text
        binding.meanTextView.text = word.mean
        Toast.makeText(this, "${word.text}가 클릭 됐습니다", Toast.LENGTH_SHORT).show()
    }

}