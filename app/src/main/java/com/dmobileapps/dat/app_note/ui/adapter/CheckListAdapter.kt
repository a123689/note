package com.dmobileapps.dat.app_note.ui.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dmobileapps.dat.app_note.R
import com.dmobileapps.dat.app_note.model.CheckList
import com.dmobileapps.dat.app_note.ui.fragment.checkList.adapter.AdapterImageCheckList
import com.dmobileapps.dat.app_note.ui.fragment.checkList.adapter.AdapterRecord
import com.dmobileapps.dat.app_note.utils.Common
import kotlinx.android.synthetic.main.item_check_list.view.*

class CheckListAdapter(
    private val arrCheckList: ArrayList<CheckList>,
    private val onFocusEditText: (position: Int, text: String) -> Unit,
    private val onSetText: (position: Int, text: String) -> Unit,
    private val onClickImage: (position: Int,positionImage:Int) -> Unit,
    private val onClickRecord: (positionItem: Int, oldPositionPlay: Int, positionPlay: Int) -> Unit,
    private val onDeleteItem: (position: Int) -> Unit,
    private val onDeleteImage: (positionCheckList: Int, positionImage: Int) -> Unit,
    private val onDeleteRecord: (positionCheckList: Int, positionRecord: Int) -> Unit
) : RecyclerView.Adapter<CheckListAdapter.ViewHolder>() {
    lateinit var adapterRecord: AdapterRecord
    var positionFocus = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_check_list, parent, false)
        return ViewHolder(view,onFocusEditText, onSetText)
    }

    //   try {
//                        if (itemView.edtTitle.hasFocus()){
//                            onSetText(position, itemView.edtTitle.text.toString())
//                        }
//                    } catch (e: Exception) {
//                    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int = arrCheckList.size
    inner class ViewHolder(
        itemView: View,
        private val onFocusEditTextVH: (position: Int, text: String) -> Unit,
        private val onSetTextVH: (position: Int, text: String) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val edt = itemView.edtTitle

        init {
            edt.setOnFocusChangeListener { v, hasFocus ->
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onFocusEditTextVH(adapterPosition, itemView.edtTitle.text.toString())
                }
            }
            edt.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onSetTextVH(adapterPosition, s.toString())
                    }
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

        fun onBind(position: Int) {
            val checkListObj = arrCheckList[position]

            if (Common.checkInterface) {
                itemView.layoutNote.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.colorBlack
                    )
                )
                itemView.edtTitle.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.colorBlack
                    )
                )
                itemView.edtTitle.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.colorWhite
                    )
                )

            } else {
                itemView.layoutNote.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.colorWhite
                    )
                )
                itemView.edtTitle.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.colorWhite
                    )
                )
                itemView.edtTitle.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.colorBlack
                    )
                )

            }

            edt.setText(checkListObj.title)
            if (position == positionFocus){
                edt.requestFocus()
            }

            val adapterImage = AdapterImageCheckList(checkListObj.images,
                {
                    onClickImage(position,it)
                },
                {
//                on delete
                    onDeleteImage(position, it)
                })

            itemView.rcvImage.layoutManager =
                GridLayoutManager(itemView.context, 3, RecyclerView.VERTICAL, false)

            itemView.rcvImage.adapter = adapterImage

            adapterRecord = AdapterRecord(checkListObj.audios, { oldPositionPlay, positionPlay ->
                onClickRecord(position, oldPositionPlay, positionPlay)
            }, {
//                on delete
                onDeleteRecord(position, it)
            })

            itemView.rcvRecord.layoutManager =
                LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)

            itemView.rcvRecord.adapter = adapterRecord

            itemView.btnDelete.setOnClickListener { onDeleteItem(position) }


        }
    }
}