package kue;

import java.util.Map;

public abstract class TransferBank extends Pembayaran {

    @Override
    public PaymentResult pay(double totalBayar, Map<String, String> info)
            throws PaymentException {

        // VALIDASI: nomor rekening toko HARUS ada (dari rekening.txt)
        if (!info.containsKey("rekeningToko")) {
            throw new PaymentException("Nomor rekening toko tidak ditemukan!");
        }

        // VALIDASI: customer harus memilih bank
        if (!info.containsKey("bank")) {
            throw new PaymentException("Metode bank belum dipilih!");
        }

        // Tidak ada setLunas() → admin yang menentukan setelah verifikasi

        return new PaymentResult(true,
                "Transfer ke Bank" + getBankName() + " diajukan. Menunggu admin memverifikasi.");
    }

    @Override // <-- TAMBAHKAN INI
    public String getMethodName() {
        return "Transfer Bank"; // Nama metode pembayaran untuk semua bank transfer
    }

    public abstract String getBankName();
}