package com.sorongos.vocabook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sorongos.vocabook.databinding.ItemWordBinding

/**사용할 데이터는 데이터 클래스를 담고 있는 컬렉션 타입*/
class WordAdapter(
    val list: MutableList<Word>,
    private val itemClickListener: ItemClickListener? = null,
) : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemWordBinding.inflate(inflater, parent, false)
        return WordViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    /**데이터 연결*/
    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(list[position]) //좀더 깔끔한 코드 구현
//        holder.binding.apply {
//            val word = list[position]
//            textTextView.text = word.text
//            meanTextView.text = word.mean
//            typeChip.text = word.type
//        }
        val word = list[position]
        holder.itemView.setOnClickListener{
            itemClickListener?.onClick(word)
        }
    }
    class WordViewHolder(val binding : ItemWordBinding) : RecyclerView.ViewHolder(binding.root){ //itemview가 아닌 binding.root를 통째로 넘겨줌
        fun bind(word: Word){
            binding.apply{
                textTextView.text = word.text
                meanTextView.text = word.mean
                typeChip.text = word.type
            }
        }
    }

    interface ItemClickListener{
        fun onClick(word: Word){

        }
    }
}