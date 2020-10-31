package com.dmobileapps.dat.app_note.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dmobileapps.dat.app_note.R
import com.dmobileapps.dat.app_note.model.Folder
import com.dmobileapps.dat.app_note.utils.Common
import kotlinx.android.synthetic.main.item_folder.view.*

class FolderAdapter(
    val context: Context,
    private val onClick: (Folder) -> Unit,
    private val onLongClick: (Folder) -> Unit
) : RecyclerView.Adapter<FolderAdapter.ViewHolder>() {

    private var noste:MutableList<Folder> = mutableListOf()
    private var listBackup:MutableList<Folder> = mutableListOf()
    var key = ""
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.tvName
        private val tvCount: TextView = itemView.tvCountNote
        private val layout:ConstraintLayout = itemView.layout
        private val view:View = itemView.view

        fun onBind(note: Folder, position: Int){
            if(Common.checkInterface){
                tvTitle.setTextColor(ContextCompat.getColor(context,R.color.colorWhite))
            }

            tvTitle.text = note.name
            tvCount.text = note.noteCount.toString()
            if(position == noste.size -1){
                view.visibility = View.INVISIBLE
            }else{
                view.visibility = View.VISIBLE
            }
            layout.setOnLongClickListener {
                onLongClick(note)
               true
            }

            layout.setOnClickListener {
                onClick(note)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_folder, parent, false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(noste[position], position)
    }

    override fun getItemCount(): Int = noste.size

    fun setFolder(note: MutableList<Folder>){
        this.noste = note
        this.listBackup.addAll(note)
         notifyDataSetChanged()
    }

    fun searchFolder(stringSearch:String){
        noste.clear()
        key = stringSearch.toLowerCase()
        if (key.length == 0) {
            noste.addAll(listBackup)
        } else {
            for (item in listBackup) {
                if (item.name.toLowerCase().contains(key)) {
                    noste.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }
}