package com.dmobileapps.dat.app_note.ui.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dmobileapps.dat.app_note.R
import com.dmobileapps.dat.app_note.model.CheckList
import com.dmobileapps.dat.app_note.ui.fragment.checkList.adapter.AdapterImageCheckList
import kotlinx.android.synthetic.main.item_check_list.view.*

class CheckListAdapter(
    private val arrCheckList: ArrayList<CheckList>,
    private val onFocusEditText: (position: Int, text: String) -> Unit,
    private val onDeleteItem: (position: Int) -> Unit,
    private val onDeleteImage: (positionCheckList: Int, positionImage: Int) -> Unit,
    private val onDeleteRecord: (positionCheckList: Int, positionRecord: Int) -> Unit
) : RecyclerView.Adapter<CheckListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_check_list, parent, false)
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
            val adapterImage = AdapterImageCheckList(checkListObj.images, {}, {
//                on delete
                onDeleteImage(position, it)
            })
            itemView.rcvImage.layoutManager =  GridLayoutManager(itemView.context, 3, RecyclerView.VERTICAL, false)
            itemView.rcvImage.adapter = adapterImage
            itemView.btnDelete.setOnClickListener { onDeleteItem(position)  }
            itemView.edtTitle.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    onFocusEditText(position, itemView.edtTitle.text.toString())
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }
    }

}