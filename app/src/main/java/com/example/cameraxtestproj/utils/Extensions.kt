package com.example.cameraxtestproj.utils

/**
 * An extension function to unite the type of barcodes into a common type.
 */
fun String.getCommonBarcodeType(): CommonBarcodeType {
    return when (this) {
        "EAN-128", "EAN128", "CODE 128", "CODE128", "LABEL-TYPE-CODE128" -> CommonBarcodeType.Code128
        "CODE 39", "CODE39", "LABEL-TYPE-CODE39" -> CommonBarcodeType.Code39
        "CODE 93", "CODE93", "LABEL-TYPE-CODE93" -> CommonBarcodeType.Code93
        "EAN-8", "EAN8", "LABEL-TYPE-EAN8" -> CommonBarcodeType.Ean8
        "PDF-417", "PDF417", "LABEL-TYPE-PDF417" -> CommonBarcodeType.Pdf417
        "QR CODE", "QRCODE", "LABEL-TYPE-QRCODE" -> CommonBarcodeType.QrCode
        else -> CommonBarcodeType.Unknown
    }
}
