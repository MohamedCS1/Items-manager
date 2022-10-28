package com.example.p_scanner.BarCodeScanner

import android.annotation.SuppressLint
import android.content.Context
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.p_scanner.Interfaces.BarCodeInterfaces
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

var barCodeInterfaces:BarCodeInterfaces? = null
class BarCodeAnalyzer(
    private val context: Context
) : ImageAnalysis.Analyzer {
    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {
        val img = image.image
        if (img != null) {

            val inputImage = InputImage.fromMediaImage(img, image.imageInfo.rotationDegrees)

            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .build()

            val scanner = BarcodeScanning.getClient(options)

            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        barCodeInterfaces!!.onBarCodeDetection(barcodes[0])
                        return@addOnSuccessListener
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