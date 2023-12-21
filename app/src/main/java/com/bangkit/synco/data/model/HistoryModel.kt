package com.bangkit.synco.data.model

data class History(
    val history: List<HistoryItem>
)

data class HistoryItem(
    val history_tanggal: String,
    val nama_penyakit: String
)