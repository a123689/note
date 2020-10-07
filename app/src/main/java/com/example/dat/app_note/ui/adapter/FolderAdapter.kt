package com.example.dat.app_note.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.dat.app_note.R
import com.example.dat.app_note.model.Folder
import kotlinx.android.synthetic.main.item_folder.view.*

class FolderAdapter(val context: Context,
                  private val onClick:(Folder)->Unit,
                  private val onDelete:(Folder)->Unit
) : RecyclerView.Adapter<FolderAdapter.ViewHolder>() {

    private var noste:List<Folder> = listOf()
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.tvName
        private val layout:ConstraintLayout = itemView.layout

        fun onBind(note: Folder){
            tvTitle.text = note.name


            layout.setOnClickListener {
                onClick(note)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_folder,parent,false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(noste[position])
    }

    override fun getItemCount(): Int = noste.size

    fun setFolder(note: List<Folder>){
        this.noste = note
         notifyDataSetChanged()
    }
}