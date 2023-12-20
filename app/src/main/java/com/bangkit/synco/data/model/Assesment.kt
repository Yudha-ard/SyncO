package com.bangkit.synco.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Assesment(
    @field:SerializedName("message")
    val message: String,

    @SerializedName("nama_penyakit")
    val namaPenyakit: String,

    @SerializedName("deskripsi")
    val deskripsi: String,

    @SerializedName("pencegahan")
    val pencegahan: String,
): Parcelable

data class AssesmentRequest(
    @SerializedName("symptoms")
    val symptoms: List<String>
)