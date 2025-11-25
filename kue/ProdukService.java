package kue;

import java.util.List;
import java.util.ArrayList;

public class ProdukService {

    private ListProduk listProduk; // struktur data penyimpan objek Produk
    private FileManager fileManager; // bertanggung jawab baca/tulis file

    public ProdukService(ListProduk listProduk, FileManager fileManager) {
        this.listProduk = listProduk;
        this.fileManager = fileManager;
    }

    // ==========================================================
    // LOAD DATA PRODUK DARI FILE → masukkan ke struktur ListProduk
    // ==========================================================
    public void loadProduk() {
        List<Produk> data = fileManager.readProduk(); // baca list dari file
        for (Produk p : data) {
            listProduk.addProduk(p); // masukkan ke memory
        }
    }

    // ==========================================================
    // SIMPAN PRODUK KE FILE → dipanggil setelah ada perubahan
    // ==========================================================
    public void saveProduk() {
        fileManager.writeProducts(listProduk.getAll());
    }

    // ==========================================================
    // TAMBAH PRODUK BARU
    // ==========================================================
    public boolean addProduk(Produk p) {
        // cek apakah ID sudah dipakai
        if (findById(p.getId()) != null) {
            return false; // gagal — ID sudah ada
        }

        listProduk.addProduk(p); // tambahkan ke list memory
        saveProduk(); // simpan ke file
        return true; // sukses
    }

    // ==========================================================
    // EDIT PRODUK (berdasarkan ID) → update data lama
    // ==========================================================
    public void editProduk(Produk newP) {
        Produk old = listProduk.findById(newP.getId()); // cari produk lama berdasarkan ID

        if (old != null) {
            // update field produk
            old.setName(newP.getName());
            old.setPrice(newP.getPrice());
            old.setDeskripsi(newP.getDeskripsi());
            old.setStok(newP.getStok());

            saveProduk(); // simpan perubahan ke file
        }
    }

    // ==========================================================
    // HAPUS PRODUK
    // ==========================================================
    public void removeProduk(String id) {
        listProduk.removeProduk(id); // hapus dari list memory
        saveProduk(); // simpan ke file
    }

    // ==========================================================
    // CARI PRODUK BERDASARKAN ID
    // ==========================================================
    public Produk findById(String id) {
        return listProduk.findById(id);
    }

    // ==========================================================
    // CARI PRODUK BERDASARKAN NAMA (search)
    // tidak case-sensitive supaya user friendly
    // ==========================================================
    public List<Produk> searchByNama(String nama) {
        List<Produk> hasil = new ArrayList<>();

        for (Produk p : listProduk.getAll()) {
            if (p.getName().toLowerCase().contains(nama.toLowerCase())) {
                hasil.add(p);
            }
        }

        return hasil;
    }
    
    public List<Produk> getAll() {
        return listProduk.getAll();
    }

    public void printAllProduk() {
        for (Produk p : listProduk.getAll()) {
            System.out.println(p.getId() + " - " + p.getName() +
                    " - Rp" + p.getPrice() +
                    " - stok: " + p.getStok());
        }
    }
}
