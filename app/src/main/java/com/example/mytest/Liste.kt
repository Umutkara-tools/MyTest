package com.example.mytest

import java.io.Serializable

data class Liste(
    val kanalAdi: String,
    val kanalLogo: String,
    val kanalM3u: String
) : Serializable