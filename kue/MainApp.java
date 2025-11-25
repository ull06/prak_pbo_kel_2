package kue;

import java.util.*;

// import java.util.HashMap;
// import java.util.Map;

// public class MainKue{
//     public static void main(String[] args){

//         // //siapkan filemananger & produk
//         // FileManager fm = new FileManager();
//         // ListProduk lp = new ListProduk();

//         // ProdukService ps = new ProdukService(lp, fm);
//         // ps.loadProduk(); // load dari file

//         // System.out.println("=== PRODUK TERLOAD ===");
//         // for (Produk p : lp.getAll()) {
//         //     System.out.println(p.getId() + " - " + p.getName() + " stok:" + p.getStok());
//         // }


//         // //siapkan aauth & akun
//         // AuthService auth = new AuthService(fm);
//         // auth.loadAccounts();

//         // // Ambil salah satu customer dari file
//         // Akun acc = auth.login("user1", "123");
//         // if (!(acc instanceof Customer)) {
//         //     System.out.println("Tidak bisa mengambil customer untuk test");
//         //     return;
//         // }

//         // Customer c = (Customer) acc;

//         // //Test Produk & ListProduk
//         // System.out.println("Test Produk");
//         // Produk p1 = new Produk("p01", "Brownies", "kue coklat", 25000, 10);
//         // Produk p2 = new Produk("p02", "Donat", "kue coklat", 25000, 10);

//         // ListProduk lp = new ListProduk();
//         // lp.addProduk(p1);
//         // lp.addProduk(p2);
//         // lp.removeProduk("p01");
//         // //lp.tambahStok(10);

//         // lp.printListProduk();
//         // System.out.println("total Produk: " + lp.getTotal());


//         // //TEST USERPROFILE
//         // System.out.println("\n=== TEST USER PROFILE ===");
//         // UserProfile up = new UserProfile("Jl. Mawar", "08123", "test@gmail.com");
//         // System.out.println("Alamat: " + up.getAddress());
//         // System.out.println("Phone: " + up.getPhoneNumber());
//         // System.out.println("Email: " + up.getEmail());



//         // // //TEST customer + cart
//         // System.out.println("\n=== TEST CUSTOMER & CART ===");
//         // Customer c = new Customer("user1", "123", "Budi", up);

//         // try { 
//         //     c.getCart().addProduk(p1, 2); // 2 brownies
//         //     c.getCart().addProduk(p2, 3); // 3 donat
//         // } catch (Exception e) {
//         //     System.out.println(e.getMessage());
//         // }

//         // System.out.println("isi keranjang");
//         // for (CartItem item : c.getCart().getItems()) {
//         //     System.out.println(item.getProduk().getName() + " x" + item.getJumlah() +
//         //             " = Rp" + item.getSubtotal());
                   
//         // }
//         // System.out.println("Total keranjang = Rp" + c.getCart().totalHarga());


//         // //TEST ADMIN
//         // System.out.println("\n=== TEST ADMIN ===");
//         // Admin admin = new Admin("admin", "123", "Admin Satu");
//         // System.out.println("Role admin: " + admin.getRole());

//         // //TEST USERPROFILE - HISTORY
//         // System.out.println("\n=== TEST HISTORY ===");
//         // Transaksi dummy = new Transaksi("T001", c, c.getCart().getItems(), null);
//         // up.addTransaksi(dummy);

//         // System.out.println("Total transaksi user: " + up.getTotalTransaksi());
//         // System.out.println("Total uang yang dihabiskan: " + up.getTotalSpent());

//         // System.out.println("\n=== TEST SELESAI: BAGIAN A WORKING ===");


//         //  // =====================================================
//         // // B1. TEST PEMBAYARAN QRIS
//         // // =====================================================
//         // System.out.println("\n=== TEST PEMBAYARAN QRIS ===");
//         // Pembayaran qris = new QRISPayment();

//         // Map<String, String> infoQR = new HashMap<>();
//         // infoQR.put("qrisCode", "QR12345");

//         // try {
//         //     PaymentResult res = qris.pay(c.getCart().totalHarga(), infoQR);
//         //     System.out.println("Status: " + res.isSuccess());
//         //     System.out.println("Pesan : " + res.getMessage());
//         // } catch (PaymentException e) {
//         //     System.out.println("PAYMENT ERROR: " + e.getMessage());
//         // }

//         // // =====================================================
//         // // B2. TEST PEMBAYARAN COD
//         // // =====================================================
//         // System.out.println("\n=== TEST PEMBAYARAN COD ===");
//         // Pembayaran cod = new CODPayment("aceh");

//         // Map<String, String> infoCOD = new HashMap<>();
//         // infoCOD.put("alamat", "Jl. Melati No. 20");

//         // try {
//         //     PaymentResult res = cod.pay(c.getCart().totalHarga(), infoCOD);
//         //     System.out.println("Status: " + res.isSuccess());
//         //     System.out.println("Pesan : " + res.getMessage());
//         // } catch (PaymentException e) {
//         //     System.out.println("PAYMENT ERROR: " + e.getMessage());
//         // }

//         // // =====================================================
//         // // B3. TEST TRANSFER BANK
//         // // =====================================================
//         // System.out.println("\n=== TEST TRANSFER BANK (BSI) ===");
//         // Pembayaran bsi = new BankBSI();

//         // Map<String, String> infoTF = new HashMap<>();
//         // infoTF.put("rekeningToko", "777888999");
//         // infoTF.put("bank", "BSI");

//         // try {
//         //     PaymentResult res = bsi.pay(c.getCart().totalHarga(), infoTF);
//         //     System.out.println("Status: " + res.isSuccess());
//         //     System.out.println("Pesan : " + res.getMessage());
//         // } catch (PaymentException e) {
//         //     System.out.println("PAYMENT ERROR: " + e.getMessage());
//         // }

//         // // =====================================================
//         // // B4. TEST TRANSAKSI & INVOICE
//         // // =====================================================
//         // System.out.println("\n=== TEST TRANSAKSI + INVOICE ===");
//         // Transaksi transaksiAkhir = new Transaksi("TRX02", c, c.getCart().getItems(), qris);
//         // Invoice inv = new Invoice(transaksiAkhir, qris);

//         // inv.printInvoice();

//         // System.out.println("\n=== SEMUA TEST A + B SELESAI ===");



//         // =============================
//         // 1. SIAPKAN FILEMANAGER & PRODUK
//         // =============================
//         FileManager fm = new FileManager();
//         ListProduk lp = new ListProduk();

//         ProdukService ps = new ProdukService(lp, fm);
//         ps.loadProduk(); // load dari file

//         System.out.println("=== PRODUK TERLOAD ===");
//         for (Produk p : lp.getAll()) {
//             System.out.println(p.getId() + " - " + p.getName() + " stok:" + p.getStok());
//         }

//         // =============================
//         // 2. SIAPKAN AUTH & AKUN
//         // =============================
//         AuthService auth = new AuthService(fm);
//         auth.loadAccounts();

//         // Ambil salah satu customer dari file
//         Akun acc = auth.login("user1", "123");
//         if (!(acc instanceof Customer)) {
//             System.out.println("Tidak bisa mengambil customer untuk test");
//             return;
//         }

//         Customer c = (Customer) acc;

//         // =============================
//         // 3. TEST KERANJANG
//         // =============================
//         Keranjang cart = c.getCart();
//         cart.clear();

//         Produk p = lp.getAll().get(0); // ambil produk pertama
//         try {
//             cart.addProduk(p, 2);
//         } catch (Exception e) {
//             System.out.println("Error add cart: " + e.getMessage());
//         }

//         System.out.println("\n=== ISI KERANJANG ===");
//         for (CartItem ci : cart.getItems()) {
//             System.out.println(ci.getProduk().getName() + " x" + ci.getJumlah());
//         }
//         System.out.println("TOTAL = " + cart.totalHarga());

//         // =============================
//         // 4. SIAPKAN TRANSAKSISERVICE
//         // =============================
//         TransactionService ts = new TransactionService(fm, ps);
//         ts.loadTransaksi();

//         // // =============================
//         // // 5. TEST PEMBAYARAN QRIS
//         // // =============================
//         Pembayaran bayar = new QRISPayment();
//         // Map<String, String> info = new HashMap<>();
//         // info.put("qr", "ok"); 

//         System.out.println("\n=== PROSES CHECKOUT ===");

//         try {
//             Transaksi t = ts.createTransaksi(c, cart, bayar);
//             System.out.println("Checkout sukses! ID: " + t.getId());
//         } catch (Exception e) {
//             System.out.println("Checkout gagal: " + e.getMessage());
//         }

//         // =============================
//         // 6. TEST PENDING TRANSAKSI
//         // =============================
//         System.out.println("\n=== PENDING TRANSAKSI ===");
//         for (Transaksi t : ts.getPendingTransaksi()) {
//             System.out.println(t.getId() + " | " + t.getTotal() + " | accepted = " + t.isAccepted());
//         }

//         // =============================
//         // 7. TEST ADMIN MENERIMA
//         // =============================
//         if (!ts.getPendingTransaksi().isEmpty()) {
//             String id = ts.getPendingTransaksi().get(0).getId();
//             ts.acceptTransaction(id);

//             System.out.println("\nAdmin menerima transaksi: " + id);
//         }

//         // =============================
//         // 8. CONFIRM STATUS
//         // =============================
//         System.out.println("\n=== STATUS SETELAH ADMIN TERIMA ===");
//         for (Transaksi t : ts.getHistory(c)) {
//             System.out.println(t.getId() + " | accepted = " + t.isAccepted());
//         }

//         System.out.println("\n=== ALL TEST C SELESAI ===");

//     }


    
// } 


//TEST MAIN BENAR BENAR


public class MainApp {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        FileManager fm = new FileManager();
        ListProduk lp = new ListProduk();
        ProdukService ps = new ProdukService(lp, fm);
        ps.loadProduk();

        AuthService auth = new AuthService(fm);
        auth.loadAccounts();

        TransactionService ts = new TransactionService(fm, ps);
        ts.loadTransaksi();

        boolean running = true;
        while (running) {
            System.out.println("\n=== APLIKASI TOKO KUE ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Pilih: ");
            String pilih = input.nextLine().trim();

            switch (pilih) {
                case "1" -> {
                    System.out.print("Username: ");
                    String u = input.nextLine().trim();
                    System.out.print("Password: ");
                    String p = input.nextLine().trim();
                    Akun a = auth.login(u, p);
                    if (a == null) {
                        System.out.println("Login gagal");
                        break;
                    }

                    if (a.getRole().equalsIgnoreCase("ADMIN")) {
                        Admin admin = (Admin) a;
                        AdminDriver ad = new AdminDriver(admin, ps, ts);
                        ad.showMenu();
                    } else {
                        Customer cust = (Customer) a;
                        CustomerDriver cd = new CustomerDriver(cust, ps, ts);
                        cd.showMenu();
                    }
                }
                case "2" -> {
                    System.out.println("\n=== REGISTER ===");
                    System.out.println("1. Customer");
                    System.out.println("2. Admin");
                    System.out.print("Daftar sebagai: ");
                    String tipe = input.nextLine().trim();


                    System.out.print("Username: ");
                    String u = input.nextLine().trim();
                    System.out.print("Password: ");
                    String p = input.nextLine().trim();
                    System.out.print("Nama Lengkap: ");
                    String f = input.nextLine().trim();
                    // System.out.print("Alamat: ");
                    // String alamat = input.nextLine().trim();
                    // System.out.print("No HP: ");
                    // String phone = input.nextLine().trim();
                    // System.out.print("Email: ");
                    // String email = input.nextLine().trim();

                    if(tipe.equals("1")){
                        //CUSTOMER
                        System.out.print("Alamat: ");
                        String alamat = input.nextLine().trim();
                        System.out.print("No HP: ");
                        String phone = input.nextLine().trim();
                        System.out.print("Email: ");
                        String email = input.nextLine().trim();

                        UserProfile prof = new UserProfile(alamat, phone, email);
                        Customer newCust = new Customer(u, p, f, prof);

                        boolean ok = auth.registerCustomer(newCust);
                        //System.out.println(ok ? "Register customer berhasil!" : "Username sudah dipakai.");
                        if (ok)
                            System.out.println("Register berhasil, silahkan login.");
                        else
                            System.out.println("username sudah dipakai.");
                    }
                    else if (tipe.equals("2")){
                        //ADMIN
                        Admin newAdmin = new Admin(u, p, f);
                        boolean ok = auth.registerAdmin(newAdmin);
                        //System.out.println(ok ? "Register admin berhasil!" : "Username sudah dipakai.");
                        if(ok)
                            System.out.println("Register berhasil, silahkan login.");
                        else
                            System.out.println("username sudah dipakai.");
                    }
                }
                case "3" -> {
                    running = false;
                }
                default -> System.out.println("Pilihan tidak dikenal");
            }
        }

        System.out.println("Keluar. Terima kasih.");

        input.close();
    }
}
