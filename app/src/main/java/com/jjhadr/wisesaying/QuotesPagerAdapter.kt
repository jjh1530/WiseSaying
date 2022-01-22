package com.jjhadr.wisesaying

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuotesPagerAdapter(
    private val quotes: List<Quote>,
    private val isNameRevealed: Boolean) :
    RecyclerView.Adapter<QuotesPagerAdapter.QuoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        //layout, viewholder create
        return QuoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_quote, parent, false)
        )
    }
    // 생성된 뷰홀더에 데이터를 바인딩 해주는 함수
    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val actualPosition = position % quotes.size
        holder.bind(quotes[actualPosition], isNameRevealed)
        //holder.bind(quotes[position], isNameRevealed)
    }

    override fun getItemCount(): Int {
        //return quotes.size
        return Int.MAX_VALUE // 스와이프 루프
    }

    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //뷰홀더 : 현재 화면에 보이는 아이템 레이아웃 개수만큼 생성 그 후는 viewHodler를 재사용
        //맨 처음 화면에 보이는 아이템들을 기억함
        private val quoteTextView: TextView = itemView.findViewById(R.id.qouteTextView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)

        @SuppressLint("SetTextI18n")
        fun bind(quote:Quote, isNameRevealed: Boolean) {
            quoteTextView.text = "\"${quote.quote}\""

            if (isNameRevealed) {  // Firebase에서 false로 바꾸면 이름 보이지 않게 만듬
                nameTextView.text = "- ${quote.name}"
                nameTextView.visibility =View.VISIBLE
            }else {
                nameTextView.visibility = View.GONE
            }
        }
    }
}