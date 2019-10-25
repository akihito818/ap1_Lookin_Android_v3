package com.example.lookin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.second.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second)
        val date = "1hour"
        val dataList = mutableListOf<TimeLineData>()
        repeat((0..13).count()) {
            dataList.add(
                TimeLineData(
                    "ユーザー名",
                    "テキストてきすとテキストてきすとテキストてきすとテキストてきすと",
                    "1 hours ago"
                )
            )
        }

        //RecyclerViewにAdapterとLayoutManagerを設定
        findViewById<RecyclerView>(R.id.TimeLineRecyclerView).also { recyclerView: RecyclerView ->
            recyclerView.adapter = TimeLineAdapter(this, dataList)
            recyclerView.layoutManager = LinearLayoutManager(this)
        }

        closeButton.setOnClickListener {
            val intentback = Intent(application, ImageActivity::class.java)
            startActivity(intentback)
        }

    }
}
