package com.example.p_scanner

import com.google.mlkit.vision.barcode.common.Barcode

interface BarCodeInterfaces {
    fun onBarCodeDetection(barcode: Barcode)
}