package com.dmobileapps.dat.app_note.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dmobileapps.dat.app_note.R
import com.dmobileapps.dat.app_note.model.ImageObj
import com.dmobileapps.dat.app_note.utils.ImageUtil
import kotlinx.android.synthetic.main.item_view_image.view.*

class ViewPagerAdapter(private var arrImage : ArrayList<ImageObj>) : RecyclerView.Adapter<PagerVH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
        PagerVH(LayoutInflater.from(parent.context).inflate(R.layout.item_view_image, parent, false))

    override fun getItemCount(): Int = arrImage.size

    override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {
        val image = arrImage[position]
        if (image.bitmap !=null){
            ImageUtil.setImage(imgView,image.bitmap)
        }else{
            ImageUtil.setImage(imgView,image.path)
        }
    }
}

class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)