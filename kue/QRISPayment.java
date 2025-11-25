package kue;

import java.util.Map;

public class QRISPayment extends Pembayaran {

    @Override
    public PaymentResult pay(double totalBayar, Map<String, String> info) {
        // QRIS langsung berhasil, tapi status tetap pending sampai admin menerima
        return new PaymentResult(true, "QRIS sukses, menunggu verifikasi admin");
    }

    // setLunas();
    // return new PaymentResult(true, "Pembayaran QR berhasil");

    @Override
    public String getMethodName() {
        return "QRIS";
    }
}