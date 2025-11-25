package kue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Transaksi {
    // ATRIBUT
    private String id;
    private Customer customer;
    private List<CartItem> items;
    private double total;
    private LocalDateTime createdAt;
    private Boolean acceptedByAdmin;
    private Pembayaran pembayaran; // simpan metode pembayaran

    // CONSTRUCTOR
    public Transaksi(String id, Customer customer, List<CartItem> items, Pembayaran pembayaran) {
        this.id = id;
        this.customer = customer;
        this.items = items;
        this.pembayaran = pembayaran;
        this.createdAt = LocalDateTime.now();
        this.acceptedByAdmin = false; // belum diterima admin
        this.total = totalHarga(); // hitung total langsung
    }

    public Transaksi(String id,Customer customer,double total,LocalDateTime createdAt,boolean accepted,Pembayaran pembayaran) {

        this.id = id;
        this.customer = customer;
        this.items = new ArrayList<>();
        this.total = total;
        this.createdAt = createdAt;
        this.acceptedByAdmin = accepted;
        this.pembayaran = pembayaran;
}

    // Menghitung total harga semua CartItem
    public double totalHarga() {
        double sum = 0;
        for (CartItem c : items) {
            sum += c.getSubtotal();
        }
        return sum;
    }

    // Set Accepted by Admin
    public void setAccepted(boolean status) {
        this.acceptedByAdmin = status;
    }
    
    // Set Created At
    public void setCreatedAt(LocalDateTime dateTime) {
        this.createdAt = dateTime;
    }
    
    // Set Customer
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    // Set Items
    public void setItems(List<CartItem> items) {
        this.items = items;
        // Update total jika items berubah
        if (items != null && !items.isEmpty()) {
            this.total = totalHarga();
        }
    }

    // Getter
    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public Double getTotal() {
        return total;
    }

    public Pembayaran getPembayaran() {
        return pembayaran;
    }
    
    public Boolean getAcceptedByAdmin() {
        return acceptedByAdmin;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isAccepted() {
        return acceptedByAdmin;
    }
}
