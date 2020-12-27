package com.dmobileapps.dat.app_note.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dmobileapps.dat.app_note.R
import com.dmobileapps.dat.app_note.model.CheckList
import kotlinx.android.synthetic.main.item_check_list.view.*

class CheckListAdapter(
    private val arrCheckList: ArrayList<CheckList>,
    private val onFocusEditText: (position: Int) -> Unit
) : RecyclerView.Adapter<CheckListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_check_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int = arrCheckList.size
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(position: Int) {
            val checkListObj = arrCheckList[position]
            if (checkListObj.title.isNotEmpty()) {
                itemView.edtTitle.setText(checkListObj.title)
            }

        }
    }

}