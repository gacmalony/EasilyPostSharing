package com.example.linstagram.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.linstagram.R
import com.example.linstagram.databinding.JournalCardBinding
import com.example.linstagram.model.Journal


class MyAdapter(val context: Context, val journal_list:List<Journal>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    lateinit var binding: JournalCardBinding


    inner class MyViewHolder(val binding: JournalCardBinding,val view: View) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(journal: Journal) {
            binding.assigner = journal
        }




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            binding = JournalCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val view = LayoutInflater.from(context).inflate(R.layout.journal_card, parent, false)
        return MyViewHolder(binding,view)
    }

    override fun getItemCount(): Int {
return journal_list.size    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        val journal: Journal = journal_list.get(position)
        holder.bind(journal)


            Glide.with(context)
                .load(journal.imageUrl.toString())
                .fitCenter()
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.postImageView)



    }
}