package com.bangkit.synco.helper

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(inputDate: String): String {
    val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())

    val date: LocalDate = LocalDate.parse(inputDate, inputFormat)
    return outputFormat.format(date)
}
fun convertStringToDate(dob: String): Date? {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return format.parse(dob)
}