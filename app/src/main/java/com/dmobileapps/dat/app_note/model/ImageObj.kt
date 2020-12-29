package com.dmobileapps.dat.app_note.model

import android.graphics.Bitmap
import android.net.Uri

data class ImageObj(
    var id:String ="",
    var title:String ="",
    var path: String? = null,
    var bitmap: Bitmap? = null,
    var isSelected: Boolean = false
)