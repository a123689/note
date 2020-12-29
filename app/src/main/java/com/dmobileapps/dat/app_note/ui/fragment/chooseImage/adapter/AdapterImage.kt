package  com.dmobileapps.dat.app_note.ui.fragment.chooseImage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dmobileapps.dat.app_note.R
import com.dmobileapps.dat.app_note.model.ImageObj
import com.dmobileapps.dat.app_note.utils.ImageUtil
import kotlinx.android.synthetic.main.item_image.view.*

class AdapterImage(
    private var arrImageObj: ArrayList<ImageObj>,
    private val onClickItemVideo: (position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return ItemSetting(view)
    }


    override fun getItemCount(): Int {
        return arrImageObj.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as ItemSetting).binDataRingTone(position)

    private inner class ItemSetting(view: View) : RecyclerView.ViewHolder(view) {

        fun binDataRingTone(position: Int) {
            val image = arrImageObj[position]
            ImageUtil.setImage(itemView.imgThumb, image.path)
            if (image.isSelected){
                itemView.rlSelectImg.visibility = View.VISIBLE
            }else{
                itemView.rlSelectImg.visibility = View.GONE
            }
            itemView.setOnClickListener {
                onClickItemVideo(position)
            }

        }
    }

}