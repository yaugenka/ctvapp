package com.example.ctv

import android.cultraview.tv.data.CtvDeviceHardwareInfo

data class LogEntry(
    val phone: String,
    val mac: String,
    val info: CtvDeviceHardwareInfo
)
