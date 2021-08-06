package com.jcp.stickyheader

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val mItemList: List<Item>) : RecyclerView.Adapter<MyAdapter.ParentViewHolder>(),StickyHeaderHandler {

    enum class ListStyle(val type: Int){
        HeaderType(0),
        NormalType(1)
    }

    // ViewHolder機構（親クラス）
    open class ParentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    // ViewHolder機構 Header Type（子クラス）
    class HeaderViewHolder(view: View) : MyAdapter.ParentViewHolder(view) {
         val headerDate: TextView = view.findViewById(R.id.headerDate)
    }

    // ViewHolder機構 Normal type（子クラス）
    class NormalViewHolder(view: View) : MyAdapter.ParentViewHolder(view) {
         val date: TextView = view.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
         return when(viewType){
            // HeaderType
            ListStyle.HeaderType.type -> {
                val view =  LayoutInflater.from(parent.context).inflate(R.layout.header_layout, parent,false)
                HeaderViewHolder(view)
            }
            // NormalType
            else -> {
                val view =  LayoutInflater.from(parent.context).inflate(R.layout.normal_layout, parent,false)
                NormalViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        val item = mItemList[position]
        when(holder){
            // HeaderType
            is HeaderViewHolder -> {
                holder.headerDate.text =  item.date
            }
            // NormalType
            is  NormalViewHolder -> {
                holder.date.text = item.date
            }
        }
    }

    override fun getItemCount(): Int = mItemList.size

    override fun getItemViewType(position: Int): Int {
        val item = mItemList[position]
        return if(item.isHeader){
            ListStyle.HeaderType.type
        }else{
            ListStyle.NormalType.type
        }
    }

    // 表示中の一番上のセルからリストの先頭(一番上)まで遡り、ヘッダーがあればそのインデックスを、なければ -1を返す
    override fun getHeaderPosition(itemPosition: Int): Int {
        var headerPosition = StickyHeaderHandler.HEADER_POSITION_NOT_FOUND

        var targetItemPosition = itemPosition
        do {
            if (isHeader(targetItemPosition)) {
                headerPosition = targetItemPosition
                break
            }
            targetItemPosition -= 1
        } while (targetItemPosition >= 0)
        return headerPosition
    }

    // Header用のレイアウトを返す
    override fun getHeaderLayout(headerPosition: Int): Int {
        return R.layout.header_layout
    }

    // Headerセルにてデータをbindする
    override fun bindHeaderData(header: View?, headerPosition: Int) {
        header?:return

        val headerItem = mItemList[headerPosition]
        if (headerItem.isHeader) {
            val headerDate = header.findViewById(R.id.headerDate) as TextView
            headerDate.text = headerItem.date
        }
    }

    // Headerかどうかの判定
    override fun isHeader(itemPosition: Int): Boolean {
        val item = mItemList[itemPosition]
        return item.isHeader
    }
}