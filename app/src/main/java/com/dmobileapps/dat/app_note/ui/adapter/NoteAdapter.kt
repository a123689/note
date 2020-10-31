package com.dmobileapps.dat.app_note.ui.adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.dmobileapps.dat.app_note.R
import com.dmobileapps.dat.app_note.model.Note
import com.dmobileapps.dat.app_note.utils.Common
import kotlinx.android.synthetic.main.item_folder.view.view
import kotlinx.android.synthetic.main.item_note.view.*

class NoteAdapter(
    val context: Context,
    private val onClick: (Note) -> Unit,
    private val onLongClick: (Note) -> Unit,
    private val onMutilDetele:(MutableList<Note>) ->Unit,
    private val onDelete:(Note)->Unit,
    private val onLock:(Note)->Unit,
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private var notes: MutableList<Note> = mutableListOf()
    private var listBackup: MutableList<Note> = mutableListOf()
    private lateinit var sharedPreference : SharedPreferences
    var key = ""
    var check = false

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.tvTitle
        private val tvDate: TextView = itemView.tvDate
        private val tvContent: TextView = itemView.tvContent
        private val layout: ConstraintLayout = itemView.layoutNote
        private val view: View = itemView.view
        private val swipeLayout:SwipeLayout = itemView.swipe
        val radioButton:RadioButton = itemView.rbDelete
        val ivDelete:ImageView = itemView.Delete
        val ivLock:ImageView = itemView.Lock
        var isCheck = false

        fun onBind(note: Note, position: Int) {
            if(Common.checkInterface){
                tvTitle.setTextColor(ContextCompat.getColor(context,R.color.colorWhite))
            }

            var arr = note.content?.split("\n")

            if(arr != null && arr[0].isNotEmpty()){
                tvTitle.text = if(arr[0].length > 25) arr[0].substring(0, 25).plus("...") else arr[0]
            }else{
                tvTitle.text = "New note"
            }

            if(sharedPreference.getString(note.id.toString(),"")?.isEmpty()!!){
                ivLock.setImageResource(R.drawable.ic_baseline_lock_24)
            }else{
                ivLock.setImageResource(R.drawable.ic_baseline_lock_open_24)

            }

            if(arr?.size!! > 1 && arr[1]!!.isNotEmpty()){
                tvContent.text = if(arr[1].length > 20) arr[1].substring(0, 20).plus("...") else arr[1]
            }else{
                tvContent.text = "No additional text"
            }

            tvDate.text = note.date

            if (position == notes.size - 1) {
                view.visibility = View.INVISIBLE
            } else {
                view.visibility = View.VISIBLE
            }

            if(isCheck){
                radioButton.isChecked = true
                isCheck = false
            }else{
                radioButton.isChecked = false
            }


            layout.setOnLongClickListener {
                notes[position].isCheck = true
                check = true
                notifyDataSetChanged()
                isCheck = true
                notifyItemChanged(position)
                onLongClick(note)
                onMutilDetele(notes)
                true
            }

            layout.setOnClickListener {

                onClick(note)
            }

            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut)


            swipeLayout.addDrag(
                SwipeLayout.DragEdge.Right,
                swipeLayout.findViewById<View>(R.id.bottom_wraper)
            )

            radioButton.setOnClickListener {
                if(notes[position].isCheck){
                    onMutilDetele(notes)
                    radioButton.isChecked = false
                    notes[position].isCheck = false
                }else{
                    onMutilDetele(notes)
                    radioButton.isChecked = true
                    notes[position].isCheck = true
                }
            }

            ivDelete.setOnClickListener {
                onDelete(note)
            }

            ivLock.setOnClickListener {
                onLock(note)
            }

            swipeLayout.addSwipeListener(object : SwipeLayout.SwipeListener {
                override fun onStartOpen(layout: SwipeLayout) {}
                override fun onOpen(layout: SwipeLayout) {}
                override fun onStartClose(layout: SwipeLayout) {}
                override fun onClose(layout: SwipeLayout) {}
                override fun onUpdate(layout: SwipeLayout, leftOffset: Int, topOffset: Int) {}
                override fun onHandRelease(layout: SwipeLayout, xvel: Float, yvel: Float) {}
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        sharedPreference = context?.getSharedPreferences("NOTE", Context.MODE_PRIVATE)!!
        holder.onBind(notes[position], position)

        if(check){
            holder.radioButton.visibility = View.VISIBLE
        }else{
            holder.radioButton.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = notes.size

    fun setNote(note: MutableList<Note>) {
        this.notes = note
        this.listBackup.addAll(note)
        notifyDataSetChanged()
    }

    fun searchFolder(stringSearch: String){
        notes.clear()
        key = stringSearch.toLowerCase()
        if (key.length == 0) {
            notes.addAll(listBackup)
        } else {
            for (item in listBackup) {
                if (item.content?.toLowerCase()!!.contains(key)) {
                    notes.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }

    fun resetCheck(){
        for(item in notes){
            item.isCheck = false
        }
    }

}