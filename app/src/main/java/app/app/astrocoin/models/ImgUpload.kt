package app.app.astrocoin.models

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class ImgUpload {
    @SerializedName("path")
    @Expose
    var path: String? = null
}