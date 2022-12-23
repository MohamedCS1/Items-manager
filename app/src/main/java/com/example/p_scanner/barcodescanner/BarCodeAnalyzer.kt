package com.example.p_scanner.barcodescanner

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.p_scanner.interfaces.BarCodeInterfaces
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

var barCodeInterfaces:BarCodeInterfaces? = null
class BarCodeAnalyzer() : ImageAnalysis.Analyzer {


    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
        .build()
    val scanner = BarcodeScanning.getClient(options)

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {
        val img = image.image
        if (img != null) {

            val inputImage = InputImage.fromMediaImage(img, image.imageInfo.rotationDegrees)

            scanner.process(inputImage)
                .addOnSuccessListener {
                    barcodes ->
                    if (barcodes.isNotEmpty()) {
                        scanner.close()
                        barCodeInterfaces!!.onBarCodeDetection(barcodes[0])
                        Log.d("CurrentBarCode" ,barcodes[0].toString())
                    }
                }
                .addOnFailureListener { }

                .addOnCompleteListener{
                    image.close()
                }
        }
    }

    fun onBarCodeDetection(BarCodeInterfaces: BarCodeInterfaces)
    {
        barCodeInterfaces = BarCodeInterfaces
    }

}