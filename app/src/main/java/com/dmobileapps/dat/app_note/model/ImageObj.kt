package com.dmobileapps.dat.app_note.model

import android.net.Uri

data class ImageObj(
    var id:String ="",
    var title:String ="",
    var path: String? = null,
    var isSelected: Boolean = false
)