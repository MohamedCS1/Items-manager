package com.example.p_scanner.Interfaces

import com.google.mlkit.vision.barcode.common.Barcode

interface BarCodeInterfaces {
    fun onBarCodeDetection(barcode: Barcode)
}