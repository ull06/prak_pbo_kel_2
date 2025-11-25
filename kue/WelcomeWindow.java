package kue;

import javax.swing.*;
import java.awt.*;

public class WelcomeWindow extends JFrame {

    // **DEFINISI WARNA**
    private final Color palePink = new Color(255, 240, 245); 
    private final Color darkBrown = new Color(101, 67, 33);

    public WelcomeWindow(AuthService auth, ProdukService ps, TransactionService ts) {

        setTitle("RASA.IN - TOKO KUE");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ===== Tema =====
        getContentPane().setBackground(palePink); 

        // ===== Konten Teks di Tengah (DIUBAH) =====
        
        // Gabungkan Judul besar dan Tagline ke dalam satu JLabel multi-baris
        JLabel contentLabel = new JLabel(
                "<html><div style='text-align: center;'>" +
                // Judul Besar "RASA.IN"
                "<p style='font-size: 36pt; font-family: Serif; font-weight: bold; margin-bottom: 5px; color: rgb(101, 67, 33);'>RASA.IN</p>" + 
                // Tagline "Selamat datang..."
                "<p style='font-size: 16pt; font-family: SansSerif;'>Selamat datang di Toko Kue RASA.IN</p>" +
                "</div></html>", 
                SwingConstants.CENTER);
        
        // Warna teks keseluruhan sudah diatur di HTML, tapi kita set default untuk amannya.
        contentLabel.setForeground(darkBrown);

        // ===== Tombol =====
        JButton btnLogin = new JButton("Login");
        JButton btnRegister = new JButton("Register");

        btnLogin.setPreferredSize(new Dimension(120, 35));
        btnLogin.setBackground(Color.WHITE); 
        btnLogin.setForeground(darkBrown);

        btnRegister.setPreferredSize(new Dimension(120, 35));
        btnRegister.setBackground(Color.WHITE);
        btnRegister.setForeground(darkBrown);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(palePink); 
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0)); 
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);

        // ==========================
        // LAYOUT UTAMA (Memastikan pemusatan konten)
        // ==========================
        
        setLayout(new BorderLayout());
        
        // Panel Tengah menggunakan GridBagLayout untuk Pemusatan Vertikal dan Horizontal
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(palePink);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // Wajib: Memberikan bobot agar label terdorong ke tengah vertikal
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Tambahkan label multi-baris ke panel tengah
        centerPanel.add(contentLabel, gbc);

        // Tambahkan panel ke JFrame
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // ===== EVENT =====
        btnLogin.addActionListener(e -> {
            dispose();
            new LoginFrame(auth, ps, ts).setVisible(true);
        });

        btnRegister.addActionListener(e -> {
            dispose();
            new RegisterFrame(auth, ps, ts).setVisible(true);
        });
    }
}