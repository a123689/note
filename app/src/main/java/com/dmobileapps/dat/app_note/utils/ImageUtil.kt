    package com.dmobileapps.dat.app_note.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.dmobileapps.dat.app_note.R
import com.dmobileapps.dat.app_note.model.ImageObj
import java.io.*
import java.nio.channels.FileChannel


object ImageUtil {

    fun setImage(image: ImageView, drawable_image: Int) {
        Glide.with(image.context).load(drawable_image).into(image)
    }

    fun setImage(image: ImageView, url_image: String?) {
        Glide.with(image.context).load(url_image).placeholder(R.mipmap.ic_launcher4)
            .error(R.mipmap.ic_launcher4).into(image)
    }

    fun setImage(image: ImageView, url_image: Uri?) {
        Glide.with(image.context).load(url_image).placeholder(R.mipmap.ic_launcher4)
            .error(R.mipmap.ic_launcher4).into(image)
    }

    fun setImage(image: ImageView, url_image: Bitmap?) {
        Glide.with(image.context).load(url_image).placeholder(R.mipmap.ic_launcher4)
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
        val bitmap = convertBitmapFromDrawable(res, resId)
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

    fun saveToInternalStorage(applicationContext: Context, image: ImageObj): String? {
        // path to /data/data/yourapp/app_data/imageDir
        val directory = getInternalPackage(applicationContext)

        // Create imageDir
        val mypath = File(directory, "${image.id}.png")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            image.bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return directory!!.absolutePath
    }

    private fun getInternalPackage(applicationContext: Context): File? {
        val cw = ContextWrapper(applicationContext)
        return cw.getDir("imageDir", Context.MODE_PRIVATE)
    }

    fun loadImageFromStorage(path: String): Bitmap? {
        try {
            val f = File(path)
            val b = BitmapFactory.decodeStream(FileInputStream(f))
            return b
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        }
    }

    fun deleteFileFromInternalStorage(imagePath: String): Boolean {
//        val dir = context.filesDir
        val file = File(imagePath)
        return file.delete()
    }

    fun copyFileToInternal(context: Context, inputPath: String, id: String): String? {
        val directory = getInternalPackage(context)
        val src = File(inputPath)
        val mypath = File(directory, "${id}.png")
        val inChannel: FileChannel? = FileInputStream(src).channel
        val outChannel: FileChannel? = FileOutputStream(mypath).channel
        try {
            inChannel!!.transferTo(0, inChannel.size(), outChannel)
        } catch (e: java.lang.Exception) {
            Log.e("TAG", "copyFile error: ", e)
        } finally {
            inChannel?.close()
            outChannel?.close()
        }
        return mypath.absolutePath
    }
}