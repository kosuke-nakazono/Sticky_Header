package com.jcp.stickyheader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
    const val STICKY_HEADER_SAMPLE = "STICKY_HEADER_SAMPLE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // adapterのセット
        val adapter =  MyAdapter(makeItems())
        recyclerView.adapter = adapter

        // itemDecorationのセット
        val itemDecoration = MyItemDecoration(adapter)
        recyclerView.addItemDecoration(itemDecoration)
    }


    // 2021年5月1日から今日までの日付を格納したリストを作成
    // 10の倍数をStickyHeaderとする
    private fun makeItems(): MutableList<Item>{
        val list = mutableListOf<Item>()

        // 現在の日付を元にカレンダーのインスタンスを取得
        val calender = Calendar.getInstance()

        // 2021年5月1日
        val pastCalender = Calendar.getInstance().apply {
            set(2021,5,1)
        }

        while (calender.after(pastCalender)){
            val day = calender.get(Calendar.DATE)
            val month = calender.get(Calendar.MONTH).plus(1)
            val date = "${month}月${day}日"

            // 10の倍数であればSticky Headerとする
            val isHeader = (day % 10) == 0

            val item = Item(date = date, isHeader = isHeader)
            list.add(item)

            calender.add(Calendar.DATE,-1)
        }

        return list
    }
}