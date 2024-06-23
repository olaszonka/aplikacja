package com.example.kalendarzyk

import java.util.Calendar
import java.util.Date

open class Data(
    val today: Date,
    val mood: Double,
    val secretion: Double,
    val temperature: Double,
    val bleeding: Boolean? = null
)

