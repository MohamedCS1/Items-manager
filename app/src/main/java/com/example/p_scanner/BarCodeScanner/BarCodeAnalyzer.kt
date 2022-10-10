package com.example.p_scanner.BarCodeScanner

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.p_scanner.AddProductActivity
import com.example.p_scanner.Interfaces.BarCodeInterfaces
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class BarCodeAnalyzer(
    private val context: Context
) : ImageAnalysis.Analyzer {
    var barCodeInterfaces:BarCodeInterfaces? = null
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
                            Log.d("CurrentBarCode" ,barcodes[0].rawValue.toString())
                            val intent = Intent(context , AddProductActivity::class.java)
                            intent.putExtra("ProductID" ,barcodes[0].rawValue.toString())
                            context.startActivity(intent)
                        return@addOnSuccessListener
                    }
                }
                .addOnFailureListener { }

                .addOnCompleteListener{
                    image.close()
                }
        }
    }

    fun onBarCodeDetection(barCodeInterfaces: BarCodeInterfaces)
    {
            this.barCodeInterfaces = barCodeInterfaces
    }


}