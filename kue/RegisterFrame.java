package kue;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private AuthService auth;
    private ProdukService produkService;
    private TransactionService transactionService;

    // ************ DEFINISI WARNA ************
    private final Color palePink = new Color(255, 240, 245); // Pink sangat muda
    private final Color darkBrown = new Color(101, 67, 33); // Coklat tua
    // ****************************************

    // Komponen global
    private JPanel customerFieldsPanel;
    private JComboBox<String> comboRole;

    public RegisterFrame(AuthService auth, ProdukService ps, TransactionService ts) {
        this.auth = auth;
        this.produkService = ps;
        this.transactionService = ts;

        // ==========================================
        // SETTING WINDOW
        // ==========================================
        setTitle("Register Akun - RASA.IN");
        setSize(550, 550); 
        setMinimumSize(new Dimension(500, 500)); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(palePink); 

        // ==========================================
        // HEADER JUDUL (Dua Baris & Di Tengah Atas)
        // ==========================================
        
        JLabel headerLabel = new JLabel(
                "<html><div style='text-align: center;'>" +
                "<p style='font-size: 30pt; font-family: Serif; font-weight: bold; margin-bottom: 5px; color: rgb(101, 67, 33);'>RASA.IN</p>" +
                "<p style='font-size: 16pt; font-family: SansSerif;'>REGISTER AKUN</p>" +
                "</div></html>", 
                SwingConstants.CENTER);
        
        headerLabel.setBackground(palePink);
        headerLabel.setOpaque(true);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        add(headerLabel, BorderLayout.NORTH);

        // ==========================================
        // FORM INPUT WRAPPER (GridBagLayout: Pemusatan Horizontal)
        // ==========================================
        
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(palePink);
        add(wrapperPanel, BorderLayout.CENTER); 
        
        // --- GridBagConstraints untuk FormPanel ---
        GridBagConstraints gbcFormContainer = new GridBagConstraints();
        gbcFormContainer.weightx = 1.0; 
        gbcFormContainer.weighty = 0;   
        gbcFormContainer.anchor = GridBagConstraints.NORTH; 
        gbcFormContainer.fill = GridBagConstraints.HORIZONTAL; 

        // ==========================================
        // FORM INPUT (GridBagLayout - Konten Formulir)
        // ==========================================
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(palePink);
        
        wrapperPanel.add(formPanel, gbcFormContainer);


        // --- Konfigurasi GridBagLayout untuk Konten Form ---
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5); 

        // Inisialisasi Field
        comboRole = new JComboBox<>(new String[]{"Customer", "Admin"});
        JTextField txtUser = new JTextField();
        JPasswordField txtPass = new JPasswordField();
        JTextField txtFull = new JTextField();
        JTextField txtAlamat = new JTextField();
        JTextField txtPhone = new JTextField();
        JTextField txtEmail = new JTextField();
        
        // Lebar input field dipertahankan tetap agar rapi dan sejajar
        Dimension fixedFieldSize = new Dimension(280, 30); 
        comboRole.setPreferredSize(fixedFieldSize);
        txtUser.setPreferredSize(fixedFieldSize);
        txtPass.setPreferredSize(fixedFieldSize);
        txtFull.setPreferredSize(fixedFieldSize);
        txtAlamat.setPreferredSize(fixedFieldSize);
        txtPhone.setPreferredSize(fixedFieldSize);
        txtEmail.setPreferredSize(fixedFieldSize);


        // --- PENAMBAHAN SPACER HORIZONTAL (Untuk Pemusatan Internal formPanel) ---
        
        // Kolom 0: Spacer Kiri (Menyerap setengah ruang ekstra)
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.5; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(new JPanel() {{ setOpaque(false); }}, gbc);

        // Kolom 3: Spacer Kanan (Menyerap setengah ruang ekstra)
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.5; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(new JPanel() {{ setOpaque(false); }}, gbc);
        
        
        // --- PENGATURAN LAYOUT BARIS PER BARIS (Menggunakan Kolom 1 dan 2) ---
        int row = 0;
        
        // 1. Daftar Sebagai: (Kolom 1: Label, Kolom 2: Input)
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0; 
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.EAST; // Rata Kanan (Label)
        formPanel.add(createStyledLabel("Daftar sebagai:", darkBrown), gbc);
        
        gbc.gridx = 2; gbc.gridy = row++; gbc.weightx = 0; 
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.WEST; // Rata Kiri (Input)
        formPanel.add(comboRole, gbc);

        // 2. Username
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.EAST; 
        formPanel.add(createStyledLabel("Username:", darkBrown), gbc);
        
        gbc.gridx = 2; gbc.gridy = row++; gbc.weightx = 0; 
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.WEST; 
        formPanel.add(txtUser, gbc);

        // 3. Password
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.EAST; 
        formPanel.add(createStyledLabel("Password:", darkBrown), gbc);
        
        gbc.gridx = 2; gbc.gridy = row++; gbc.weightx = 0; 
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.WEST; 
        formPanel.add(txtPass, gbc);

        // 4. Nama Lengkap
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.EAST; 
        formPanel.add(createStyledLabel("Nama Lengkap:", darkBrown), gbc);
        
        gbc.gridx = 2; gbc.gridy = row++; gbc.weightx = 0; 
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.WEST; 
        formPanel.add(txtFull, gbc);
        
        // =========================================================
        // Panel Khusus Field Customer (Alamat, No. HP, Email)
        // =========================================================
        
        // *KOREKSI*: Menggunakan 4 kolom (3 kolom untuk konten + 1 spacer) agar sejajar dengan atas.
        customerFieldsPanel = new JPanel(new GridBagLayout());
        customerFieldsPanel.setBackground(palePink);
        
        // --- Konfigurasi GridBagLayout untuk Customer Fields ---
        GridBagConstraints gbcCustomer = new GridBagConstraints();
        gbcCustomer.insets = new Insets(8, 15, 8, 5); 
        
        int customerRow = 0;
        
        // 1. Spacer Kiri (Menjamin pemusatan internal)
        gbcCustomer.gridx = 1; gbcCustomer.gridy = 0; gbcCustomer.weightx = 0; gbcCustomer.fill = GridBagConstraints.HORIZONTAL;
        customerFieldsPanel.add(new JPanel() {{ setOpaque(false); }}, gbcCustomer);
        
        // 2. Spacer Kanan (Menjamin pemusatan internal)
        gbcCustomer.gridx = 2; gbcCustomer.gridy = 0; gbcCustomer.weightx = 0; gbcCustomer.fill = GridBagConstraints.HORIZONTAL;
        customerFieldsPanel.add(new JPanel() {{ setOpaque(false); }}, gbcCustomer);

        // Alamat (Kolom 1: Label, Kolom 2: Input)
        gbcCustomer.gridx = 1; gbcCustomer.gridy = customerRow; gbcCustomer.weightx = 0; gbcCustomer.fill = GridBagConstraints.NONE; gbcCustomer.anchor = GridBagConstraints.EAST;
        customerFieldsPanel.add(createStyledLabel("Alamat:", darkBrown), gbcCustomer);
        
        gbcCustomer.gridx = 2; gbcCustomer.gridy = customerRow++; gbcCustomer.weightx = 0; gbcCustomer.fill = GridBagConstraints.NONE; gbcCustomer.anchor = GridBagConstraints.WEST; 
        customerFieldsPanel.add(txtAlamat, gbcCustomer);

        // No. HP
        gbcCustomer.gridx = 1; gbcCustomer.gridy = customerRow; gbcCustomer.weightx = 0; gbcCustomer.fill = GridBagConstraints.NONE; gbcCustomer.anchor = GridBagConstraints.EAST;
        customerFieldsPanel.add(createStyledLabel("No. HP:", darkBrown), gbcCustomer);
        
        gbcCustomer.gridx = 2; gbcCustomer.gridy = customerRow++; gbcCustomer.weightx = 0; gbcCustomer.fill = GridBagConstraints.NONE; gbcCustomer.anchor = GridBagConstraints.WEST;
        customerFieldsPanel.add(txtPhone, gbcCustomer);

        // Email
        gbcCustomer.gridx = 1; gbcCustomer.gridy = customerRow; gbcCustomer.weightx = 0; gbcCustomer.fill = GridBagConstraints.NONE; gbcCustomer.anchor = GridBagConstraints.EAST;
        customerFieldsPanel.add(createStyledLabel("Email:", darkBrown), gbcCustomer);
        
        gbcCustomer.gridx = 2; gbcCustomer.gridy = customerRow++; gbcCustomer.weightx = 0; gbcCustomer.fill = GridBagConstraints.NONE; gbcCustomer.anchor = GridBagConstraints.WEST;
        customerFieldsPanel.add(txtEmail, gbcCustomer);
        
        // Tambahkan Panel Customer ke Form Utama (mengambil 4 kolom penuh)
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 4; // Mengambil empat kolom penuh
        gbc.weighty = 0; 
        gbc.fill = GridBagConstraints.BOTH; 
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(customerFieldsPanel, gbc);
        
        // Tambahkan spacer vertikal di bawah form (mengambil 4 kolom penuh)
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 4;
        gbc.weighty = 1.0; 
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(new JPanel() {{ setOpaque(false); }}, gbc);


        // ==========================================
        // BUTTON PANEL (Bawah) - Sudah di tengah oleh FlowLayout
        // ==========================================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(palePink);
        
        JButton btnRegister = new JButton("Register");
        JButton btnCancel = new JButton("Cancel");
        
        // Styling tombol
        btnRegister.setBackground(Color.WHITE); 
        btnRegister.setForeground(darkBrown);
        btnCancel.setBackground(Color.WHITE);
        btnCancel.setForeground(darkBrown);
        
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnCancel);
        
        add(buttonPanel, BorderLayout.SOUTH);

        // ===================================================
        // EVENT: COMBO BOX ROLE BERUBAH
        // ===================================================
        comboRole.addActionListener(e -> toggleCustomerFields());

        toggleCustomerFields(); 

        // ===================================================
        // EVENT: REGISTER (Logika tidak diubah)
        // ===================================================
        btnRegister.addActionListener(e -> {
            String role = (String) comboRole.getSelectedItem();
            String username = txtUser.getText().trim();
            String pw = new String(txtPass.getPassword()).trim();
            String full = txtFull.getText().trim();

            if (username.isEmpty() || pw.isEmpty() || full.isEmpty()) {
                showPinkMessageDialog("Username, password, dan nama lengkap harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean ok = false;

            if (role.equals("Customer")) {
                String alamat = txtAlamat.getText().trim();
                String phone = txtPhone.getText().trim();
                String email = txtEmail.getText().trim();

                if (alamat.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                    showPinkMessageDialog("Alamat, No. HP, dan Email harus diisi untuk Customer.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                UserProfile up = new UserProfile(alamat, phone, email);
                Customer c = new Customer(username, pw, full, up);
                ok = auth.registerCustomer(c);
            }
            else if (role.equals("Admin")) {
                Admin a = new Admin(username, pw, full);
                ok = auth.registerAdmin(a);
            }

            if (!ok) {
                showPinkMessageDialog("Username sudah dipakai.", "Gagal", JOptionPane.ERROR_MESSAGE);
                return;
            }

            showPinkMessageDialog("Register berhasil sebagai " + role + ". Silakan login.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginFrame(auth, produkService, transactionService).setVisible(true);
        });

        // ===================================================
        // EVENT: CANCEL
        // ===================================================
        btnCancel.addActionListener(e -> {
            dispose();
            new WelcomeWindow(auth, produkService, transactionService).setVisible(true);
        });
    }
    
    private void toggleCustomerFields() {
        String role = (String) comboRole.getSelectedItem();
        boolean isCustomer = role.equals("Customer");

        customerFieldsPanel.setVisible(isCustomer);
        
        revalidate();
        repaint();
    }

    private JLabel createStyledLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        return label;
    }
    
    private void showPinkMessageDialog(String message, String title, int messageType) {
        // Mengatur tema warna untuk JOptionPane
        UIManager.put("OptionPane.background", palePink);
        UIManager.put("Panel.background", palePink);
        UIManager.put("Button.background", Color.WHITE);
        UIManager.put("Button.foreground", darkBrown);
        UIManager.put("Label.foreground", darkBrown);
        
        JOptionPane.showMessageDialog(this, message, title, messageType);
        
        // Reset UIManager ke default setelah selesai
        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("Button.background", null);
        UIManager.put("Button.foreground", null);
        UIManager.put("Label.foreground", null);
    }
}