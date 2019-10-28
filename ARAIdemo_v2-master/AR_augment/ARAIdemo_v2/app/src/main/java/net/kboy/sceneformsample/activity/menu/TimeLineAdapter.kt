package com.example.lookin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TimeLineAdapter(private val context: Context, private val dataList: List<TimeLineData>) :
    RecyclerView.Adapter<TimeLineAdapter.ListViewHolder>() {

    class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userIconImageView: ImageView = view.findViewById(R.id.userIcon)
        val userNameTextView: TextView = view.findViewById(R.id.userName)
        val contentTextView: TextView = view.findViewById(R.id.content)
        val dateTextView: TextView = view.findViewById(R.id.date)
        val contentPictureView: ImageView = view.findViewById(R.id.contentPicture)
        val _tagTextView: TextView = view.findViewById(R.id._tag)
        val heartView: ImageView = view.findViewById(R.id.heart)
        var LikeNum: TextView = view.findViewById(R.id.likeCount)
        var countLike: Int = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder =
        ListViewHolder(LayoutInflater.from(context).inflate(R.layout.timeline_list, parent, false))

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.userIconImageView.setImageResource(R.mipmap.ic_launcher_round)
        holder.userNameTextView.text = dataList[position].username
        holder.dateTextView.text = dataList[position].date
        holder.contentTextView.text = dataList[position].content
        holder.contentPictureView.setImageResource(R.color.colorPrimary)
        holder.heartView.setImageResource(R.mipmap.heart1)
        holder.LikeNum.text="0"
        holder.heartView.setOnClickListener {
            holder.heartView.setImageResource(R.mipmap.heart2)
            holder.countLike++

            holder.LikeNum.text=holder.countLike.toString()
        }


    }

}