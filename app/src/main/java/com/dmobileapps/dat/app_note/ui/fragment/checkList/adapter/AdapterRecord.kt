package  com.dmobileapps.dat.app_note.ui.fragment.checkList.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dmobileapps.dat.app_note.R
import com.dmobileapps.dat.app_note.model.RecordObj
import com.dmobileapps.dat.app_note.utils.ImageUtil
import kotlinx.android.synthetic.main.item_record.view.*


class AdapterRecord(
    private var arrRecord: ArrayList<RecordObj>,
    private val onClickIteRecord: (oldPosition: Int, position: Int) -> Unit,
    private val onDeleteIteRecord: (position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_record, parent, false)
        return ItemRecord(view)
    }

    private var oldPositionPlay = 0
    private var positionPlay = 0

    override fun getItemCount(): Int {
        return arrRecord.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as ItemRecord).binDataRecord(position)

    private inner class ItemRecord(view: View) : RecyclerView.ViewHolder(view) {

        fun binDataRecord(position: Int) {
            val record = arrRecord[position]
            if (record.isPlay) {
                ImageUtil.setImage(itemView.btnPlay, R.drawable.ic_play)
            } else {
                ImageUtil.setImage(itemView.btnPlay, R.drawable.ic_pause)
            }
            itemView.setOnClickListener {
                positionPlay = position
                onClickIteRecord(oldPositionPlay, positionPlay)
                oldPositionPlay = position
            }
            itemView.waveformSeekBar.setOnTouchListener(OnTouchListener { v, event -> true })
            itemView.btnDeleteRecord.setOnClickListener {
                onDeleteIteRecord(position)
            }

        }
    }

}