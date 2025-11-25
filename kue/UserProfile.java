package kue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserProfile {

    private String address;
    private String phoneNumber;
    private String email;
    private ArrayList<Transaksi> transactionHistory;
    private LocalDateTime memberSince;

    public UserProfile(String address, String phone, String email) {
        this.address = address;
        this.phoneNumber = phone;
        this.email = email;
        this.transactionHistory = new ArrayList<>();
        this.memberSince = LocalDateTime.now();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phone) {
        this.phoneNumber = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addTransaksi(Transaksi t) {
        transactionHistory.add(t);
    }

    public List<Transaksi> getPurchaseHistory() {
        return transactionHistory;
    }

    public double getTotalSpent() {
        double total = 0;
        for (Transaksi t : transactionHistory)
            total += t.getTotal();
        return total;
    }

    public int getTotalTransaksi() {
        return transactionHistory.size();
    }
}
