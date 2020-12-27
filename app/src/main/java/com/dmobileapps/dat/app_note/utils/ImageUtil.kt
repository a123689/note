package com.dmobileapps.dat.app_note.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.dmobileapps.dat.app_note.R
import java.io.*

object ImageUtil {

    fun setImage(image: ImageView, drawable_image: Int) {
        Glide.with(image.context).load(drawable_image).into(image)
    }

    fun setImage(image: ImageView, url_image: String?) {
        Glide.with(image.context).load(url_image).
        placeholder(R.mipmap.ic_launcher4)
            .error(R.mipmap.ic_launcher4).into(image)
    }

    fun setImage(image: ImageView, url_image: Uri?) {
        Glide.with(image.context).load(url_image).
        placeholder(R.mipmap.ic_launcher4)
            .error(R.mipmap.ic_launcher4).into(image)
    }

    fun setImageByte(image: ImageView, url_image: ByteArray?) {
        Glide.with(image.context).load(url_image).placeholder(R.mipmap.ic_launcher4)
            .error(R.mipmap.ic_launcher4).load(url_image).into(image)
    }

    fun convertBitmapFromDrawable(res: Resources?, resId: Int): Bitmap {
        return BitmapFactory.decodeResource(res, resId)
    }

      fun imageToBitmap(res: Resources?, resId: Int): ByteArray {
        val bitmap = convertBitmapFromDrawable(res,resId)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)

        return stream.toByteArray()
    }
    fun convertBitmaptoFile(
        context: Context,
        bitmap: Bitmap,
        filename: String?
    ): File { //create a file to write bitmap data
        val f = File(context.cacheDir, filename)
        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos)
        val bitmapdata = bos.toByteArray()

//write the bytes in file
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(f)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos!!.write(bitmapdata)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return f
    }
}