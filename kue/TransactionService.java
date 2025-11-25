package kue;

import java.util.*;
import java.time.LocalDateTime;

public class TransactionService {

    private List<Transaksi> transaksiList; // semua transaksi yang tercatat
    private FileManager fileManager; // baca & simpan transaksi
    private ProdukService produkService; // update stok setelah checkout

    public TransactionService(FileManager fm, ProdukService ps) {
        this.fileManager = fm;
        this.produkService = ps;
        this.transaksiList = new ArrayList<>();
    }
    
    // Getter untuk FileManager (untuk akses dari CustomerFrame)
    public FileManager getFileManager() {
        return fileManager;
    }
    
    // Method untuk load items dari invoice (public agar bisa dipanggil dari CustomerFrame)
    public void loadItemsFromInvoice(Transaksi transaksi, Customer customer) {
        String invoiceLine = fileManager.readInvoiceLine(transaksi.getId());
        if (invoiceLine == null) {
            return;
        }
        
        try {
            // Format: id;tanggal;metodePembayaran;item1|item2|...
            String[] parts = invoiceLine.split(";");
            if (parts.length < 4 || parts[3].isEmpty()) {
                return;
            }
            
            // Parse items
            List<CartItem> items = new ArrayList<>();
            String[] itemStrings = parts[3].split("\\|");
            for (String itemStr : itemStrings) {
                if (itemStr == null || itemStr.trim().isEmpty()) {
                    continue;
                }
                // Format: produkId,jumlah,subtotal
                String[] itemParts = itemStr.split(",");
                if (itemParts.length >= 3) {
                    try {
                        String produkId = itemParts[0].trim();
                        int jumlah = Integer.parseInt(itemParts[1].trim());
                        
                        // Cari produk berdasarkan ID
                        Produk produk = produkService.findById(produkId);
                        if (produk != null) {
                            items.add(new CartItem(produk, jumlah));
                        } else {
                            System.out.println("Produk dengan ID " + produkId + " tidak ditemukan untuk transaksi " + transaksi.getId());
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing item: " + itemStr + " - " + e.getMessage());
                    }
                }
            }
            
            // Set items ke transaksi
            if (!items.isEmpty()) {
                transaksi.setItems(items);
            } else {
                System.out.println("Tidak ada items yang valid untuk transaksi " + transaksi.getId());
            }
            
            // Set pembayaran jika null
            if (transaksi.getPembayaran() == null && parts.length >= 3) {
                String metodePembayaran = parts[2];
                Pembayaran pembayaran = createPembayaranFromString(metodePembayaran, customer);
                if (pembayaran != null) {
                    // Note: Transaksi tidak punya setPembayaran, jadi kita skip
                    // Tapi invoice akan dibuat dengan pembayaran yang benar
                }
            }
            
        } catch (Exception e) {
            System.out.println("Gagal memuat items dari invoice: " + e.getMessage());
        }
    }
    
    // Helper method untuk membuat Pembayaran dari string (public agar bisa dipanggil dari CustomerFrame)
    public Pembayaran createPembayaranFromString(String metodePembayaran, Customer customer) {
        String metodeUpper = metodePembayaran.toUpperCase();
        if (metodeUpper.contains("QRIS")) {
            return new QRISPayment();
        } else if (metodeUpper.contains("COD")) {
            return new CODPayment(customer.getProfile().getAddress());
        } else if (metodeUpper.contains("TRANSFER")) {
            if (metodeUpper.contains("BSI")) {
                return new BankBSI();
            } else if (metodeUpper.contains("MANDIRI")) {
                return new BankMandiri();
            } else if (metodeUpper.contains("ACEH")) {
                return new BankAceh();
            } else {
                return new BankBSI();
            }
        }
        return new QRISPayment();
    }

    // ==========================================================
    // LOAD TRANSAKSI DARI FILE → untuk history dan admin
    // ==========================================================
    public void loadTransaksi() {
        transaksiList = fileManager.readTransactions();
    }

    // ==========================================================
    // SAVE KE FILE → setelah transaksi baru atau admin accept
    // ==========================================================
    public void saveTransaksi() {
        fileManager.writeTransactions(transaksiList);
    }

    // ==========================================================
    // CREATE TRANSAKSI BARU → proses checkout
    // ==========================================================
    public Transaksi createTransaksi(
            Customer c,
            Keranjang cart,
            Pembayaran pembayaran) throws PaymentException {

        double total = cart.totalHarga();

        // -----------------------------
        // Info tambahan untuk pembayaran
        // -----------------------------
        Map<String, String> info = new HashMap<>();

        // Jika metode TransferBank → ambil nomor rekening toko
        if (pembayaran instanceof TransferBank tf) {

            // baca file rekening.txt
            Map<String, String> rekMap = fileManager.readRekeningToko();

            String bank = tf.getBankName().toUpperCase();

            if (!rekMap.containsKey(bank)) {
                throw new PaymentException(
                        "Nomor rekening toko untuk bank " + bank + " tidak ditemukan!");
            }

            info.put("bank", bank);
            info.put("rekeningToko", rekMap.get(bank));
        }

        // -----------------------------
        // Jalankan logika pembayaran
        // -----------------------------
        PaymentResult hasil = pembayaran.pay(total, info);

        if (!hasil.success) {
            throw new PaymentException("Pembayaran gagal: " + hasil.message);
        }

        // -----------------------------
        // Buat ID transaksi
        // -----------------------------
        String id = "T" + (transaksiList.size() + 1);

        // -----------------------------
        // Buat transaksi baru
        // -----------------------------
        Transaksi t = new Transaksi(id, c, cart.getItems(), pembayaran);

        transaksiList.add(t);

        // -----------------------------
        // BUAT INVOICE & SIMPAN
        Invoice inv = new Invoice(t, pembayaran);
        fileManager.appendInvoice(inv);

        // -----------------------------
        // Update stok produk
        // -----------------------------
        for (CartItem item : cart.getItems()) {
            try {
                item.getProduk().kurangiStok(item.getJumlah());
            } catch (Exception e) {
                System.out.println("Stok gagal dikurangi: " + e.getMessage());
            }
        }

        // simpan perubahan
        produkService.saveProduk();
        saveTransaksi();

        // update history user
        c.getProfile().addTransaksi(t);

        // kosongkan keranjang
        cart.clear();

        return t;
    }

    // ==========================================================
    // AMBIL DAFTAR TRANSAKSI YANG BELUM DITERIMA ADMIN
    // ==========================================================
    public List<Transaksi> getPendingTransaksi() {
        List<Transaksi> pending = new ArrayList<>();
        for (Transaksi t : transaksiList) {
            if (!t.isAccepted()) {
                pending.add(t);
            }
        }
        return pending;
    }

    // ==========================================================
    // ADMIN MENERIMA TRANSAKSI → set accepted = true
    // ==========================================================
    public void acceptTransaction(String transaksiId) {

        for (Transaksi t : transaksiList) {
            if (t.getId().equals(transaksiId)) {
                t.setAccepted(true);
                saveTransaksi();
                return;
            }
        }
    }

    // ==========================================================
    // RIWAYAT TRANSAKSI CUSTOMER
    // ==========================================================
    public List<Transaksi> getHistory(Customer c) {
        List<Transaksi> hasil = new ArrayList<>();

        for (Transaksi t : transaksiList) {
            if (t.getCustomer().getUsername().equals(c.getUsername())) {
                // Ganti customer dummy dengan customer yang benar
                t.setCustomer(c);
                
                // Jika transaksi tidak punya items, coba load dari invoice
                if (t.getItems() == null || t.getItems().isEmpty()) {
                    loadItemsFromInvoice(t, c);
                }
                
                hasil.add(t);
            }
        }

        return hasil;
    }
    


    // ==========================================================
    // AMBIL SEMUA TRANSAKSI YANG SUDAH DITERIMA → untuk riwayat admin
    // ==========================================================
    public List<Transaksi> getCompletedTransaksi() {
        List<Transaksi> selesai = new ArrayList<>();
        for (Transaksi t : transaksiList) {
            if (t.isAccepted()) {
                selesai.add(t);
            }
        }
        return selesai;
    }

    // =========================================================
    // BACA INVOICE DARI FILE DAN BUAT INVOICE OBJECT
    // =========================================================
    public Invoice loadInvoiceFromFile(String transaksiId, Customer customer) {
        String invoiceLine = fileManager.readInvoiceLine(transaksiId);
        if (invoiceLine == null) {
            return null;
        }
        
        try {
            // Format: id;tanggal;metodePembayaran;item1|item2|...
            String[] parts = invoiceLine.split(";");
            if (parts.length < 3) {
                return null;
            }
            
            String id = parts[0];
            LocalDateTime tanggal = LocalDateTime.parse(parts[1]);
            String metodePembayaran = parts[2];
            
            // Buat Pembayaran berdasarkan metode
            Pembayaran pembayaran = null;
            String metodeUpper = metodePembayaran.toUpperCase();
            if (metodeUpper.contains("QRIS")) {
                pembayaran = new QRISPayment();
            } else if (metodeUpper.contains("COD")) {
                pembayaran = new CODPayment(customer.getProfile().getAddress());
            } else if (metodeUpper.contains("TRANSFER")) {
                // Untuk transfer bank, coba deteksi bank spesifik
                if (metodeUpper.contains("BSI")) {
                    pembayaran = new BankBSI();
                } else if (metodeUpper.contains("MANDIRI")) {
                    pembayaran = new BankMandiri();
                } else if (metodeUpper.contains("ACEH")) {
                    pembayaran = new BankAceh();
                } else {
                    pembayaran = new BankBSI(); // Default bank transfer
                }
            } else {
                pembayaran = new QRISPayment(); // Default
            }
            
            // Parse items jika ada
            List<CartItem> items = new ArrayList<>();
            if (parts.length > 3 && !parts[3].isEmpty()) {
                String[] itemStrings = parts[3].split("\\|");
                for (String itemStr : itemStrings) {
                    if (itemStr == null || itemStr.trim().isEmpty()) {
                        continue;
                    }
                    // Format: produkId,jumlah,subtotal
                    String[] itemParts = itemStr.split(",");
                    if (itemParts.length >= 3) {
                        try {
                            String produkId = itemParts[0].trim();
                            int jumlah = Integer.parseInt(itemParts[1].trim());
                            double subtotal = Double.parseDouble(itemParts[2].trim());
                            
                            // Cari produk berdasarkan ID
                            Produk produk = produkService.findById(produkId);
                            if (produk != null) {
                                items.add(new CartItem(produk, jumlah));
                            } else {
                                System.out.println("Produk dengan ID " + produkId + " tidak ditemukan untuk invoice " + id);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Error parsing item: " + itemStr + " - " + e.getMessage());
                        }
                    }
                }
            }
            
            // Validasi: pastikan ada items
            if (items.isEmpty()) {
                System.out.println("Invoice " + id + " tidak memiliki items yang valid");
                return null;
            }
            
            // Cari transaksi yang sudah ada di transaksiList untuk mendapatkan status yang benar
            Transaksi transaksiAsli = null;
            for (Transaksi t : transaksiList) {
                if (t.getId().equals(id)) {
                    transaksiAsli = t;
                    break;
                }
            }
            
            // Buat Transaksi dengan items yang lengkap
            Transaksi transaksi = new Transaksi(id, customer, items, pembayaran);
            transaksi.setCreatedAt(tanggal);
            
            // Set status dari transaksi asli jika ada
            if (transaksiAsli != null) {
                transaksi.setAccepted(transaksiAsli.isAccepted());
            } else {
                // Jika tidak ditemukan, default ke pending
                transaksi.setAccepted(false);
            }
            
            // Buat Invoice
            Invoice invoice = new Invoice(transaksi, pembayaran);
            return invoice;
            
        } catch (Exception e) {
            System.out.println("Gagal memuat invoice: " + e.getMessage());
            return null;
        }
    }

}
