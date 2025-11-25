package kue;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.Locale;

public class InvoiceGUI extends JDialog { 

    private static final NumberFormat IDR_FORMAT = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
    private final Color palePink = new Color(255, 240, 245); 
    private final Color darkBrown = new Color(101, 67, 33);
    private final Color lightGray = new Color(250, 250, 250);

    public InvoiceGUI(Frame owner, Invoice invoice) { 
        super(owner, "INVOICE - " + invoice.getTransaksi().getId(), true);
        
        getContentPane().setBackground(palePink);
        setLayout(new BorderLayout(10, 10)); 

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(palePink);
        
        // 1. Header dengan Logo/Nama Toko
        mainPanel.add(createStoreHeader());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // 2. Judul Invoice
        JLabel title = new JLabel("INVOICE PEMBELIAN", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(darkBrown);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // 3. Panel Informasi Transaksi
        mainPanel.add(createTransactionInfoPanel(invoice));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // 4. Panel Informasi Customer
        mainPanel.add(createCustomerInfoPanel(invoice));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 5. Label Detail Produk
        JLabel productLabel = new JLabel("Detail Produk:", SwingConstants.LEFT);
        productLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        productLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(productLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // 6. Tabel Items
        mainPanel.add(createItemsTable(invoice));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 7. Panel Summary (Total dan Metode Pembayaran)
        mainPanel.add(createSummaryPanel(invoice));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // 8. Panel Informasi Pembayaran Detail
        mainPanel.add(createPaymentDetailPanel(invoice));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // 9. Footer dengan pesan terima kasih
        mainPanel.add(createFooterPanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // 10. Tombol OK
        JButton btnOk = new JButton("Tutup");
        btnOk.setBackground(darkBrown);
        btnOk.setForeground(Color.WHITE);
        btnOk.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnOk.setPreferredSize(new Dimension(120, 35));
        btnOk.addActionListener(e -> dispose());
        
        JPanel buttonWrapper = new JPanel();
        buttonWrapper.setBackground(palePink);
        buttonWrapper.add(btnOk);
        
        mainPanel.add(buttonWrapper); 
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(palePink);
        
        add(scrollPane, BorderLayout.CENTER);
        pack(); 
        setSize(600, Math.min(700, getHeight())); 
        setLocationRelativeTo(owner);
    }
    
    private JPanel createStoreHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(palePink);
        
        JLabel storeName = new JLabel("RASA.IN", SwingConstants.CENTER);
        storeName.setFont(new Font("SansSerif", Font.BOLD, 24));
        storeName.setForeground(darkBrown);
        
        JLabel storeTagline = new JLabel("Toko Kue & Roti Terpercaya", SwingConstants.CENTER);
        storeTagline.setFont(new Font("SansSerif", Font.ITALIC, 12));
        storeTagline.setForeground(new Color(100, 100, 100));
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(palePink);
        centerPanel.add(storeName);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        centerPanel.add(storeTagline);
        
        headerPanel.add(centerPanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createTransactionInfoPanel(Invoice invoice) {
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 8));
        infoPanel.setBackground(lightGray);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss");

        addInfoRow(infoPanel, "ID Transaksi:", 
            invoice.getTransaksi() != null && invoice.getTransaksi().getId() != null 
                ? invoice.getTransaksi().getId() : "-");
        
        addInfoRow(infoPanel, "Tanggal Transaksi:", 
            invoice.getTanggal() != null 
                ? invoice.getTanggal().format(formatter) : "-");
        
        String status = "PENDING";
        Color statusColor = Color.ORANGE.darker();
        if (invoice.getTransaksi() != null && invoice.getTransaksi().isAccepted()) {
            status = "DITERIMA";
            statusColor = new Color(0, 150, 0);
        }
        JLabel statusLabel = new JLabel(status);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        statusLabel.setForeground(statusColor);
        infoPanel.add(new JLabel("Status:"));
        infoPanel.add(statusLabel);

        return infoPanel;
    }
    
    private JPanel createCustomerInfoPanel(Invoice invoice) {
        JPanel customerPanel = new JPanel();
        customerPanel.setLayout(new BoxLayout(customerPanel, BoxLayout.Y_AXIS));
        customerPanel.setBackground(lightGray);
        customerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel customerTitle = new JLabel("Informasi Customer:");
        customerTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        customerTitle.setForeground(darkBrown);
        customerPanel.add(customerTitle);
        customerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        if (invoice.getTransaksi() != null && invoice.getTransaksi().getCustomer() != null) {
            var customer = invoice.getTransaksi().getCustomer();
            var profile = customer.getProfile();
            
            customerPanel.add(createInfoLabel("Nama:", customer.getFullName()));
            if (profile != null) {
                customerPanel.add(createInfoLabel("Alamat:", profile.getAddress()));
                customerPanel.add(createInfoLabel("Telepon:", profile.getPhoneNumber()));
                customerPanel.add(createInfoLabel("Email:", profile.getEmail()));
            }
        } else {
            customerPanel.add(new JLabel("Data customer tidak tersedia"));
        }
        
        return customerPanel;
    }
    
    private JLabel createInfoLabel(String label, String value) {
        JLabel infoLabel = new JLabel("<html><b>" + label + "</b> " + (value != null ? value : "-") + "</html>");
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        return infoLabel;
    }
    
    private void addInfoRow(JPanel panel, String label, String value) {
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("SansSerif", Font.BOLD, 11));
        panel.add(labelComp);
        
        JLabel valueComp = new JLabel(value != null ? value : "-");
        valueComp.setFont(new Font("SansSerif", Font.PLAIN, 11));
        panel.add(valueComp);
    }
    
    private JPanel createHeaderPanel(Invoice invoice) {
        JPanel headerPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        headerPanel.setBackground(palePink);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        headerPanel.add(new JLabel("Tanggal:"));
        if (invoice.getTanggal() != null) {
            headerPanel.add(new JLabel(invoice.getTanggal().format(formatter)));
        } else {
            headerPanel.add(new JLabel("-"));
        }

        headerPanel.add(new JLabel("ID Transaksi:"));
        if (invoice.getTransaksi() != null && invoice.getTransaksi().getId() != null) {
            headerPanel.add(new JLabel(invoice.getTransaksi().getId()));
        } else {
            headerPanel.add(new JLabel("-"));
        }

        headerPanel.add(new JLabel("Customer:"));
        if (invoice.getTransaksi() != null && invoice.getTransaksi().getCustomer() != null) {
            headerPanel.add(new JLabel(invoice.getTransaksi().getCustomer().getFullName()));
        } else {
            headerPanel.add(new JLabel("-"));
        }

        return headerPanel;
    }

    private JScrollPane createItemsTable(Invoice invoice) {
        String[] columnNames = {"ID Produk", "Nama Produk", "Harga Satuan", "Jumlah", "Subtotal"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Pastikan items tidak null dan tidak kosong
        if (invoice.getTransaksi().getItems() != null && !invoice.getTransaksi().getItems().isEmpty()) {
            for (CartItem item : invoice.getTransaksi().getItems()) {
                if (item != null && item.getProduk() != null) {
                    Produk produk = item.getProduk();
                    model.addRow(new Object[]{
                            produk.getId(),
                            produk.getName(),
                            IDR_FORMAT.format(produk.getPrice()),
                            item.getJumlah(),
                            IDR_FORMAT.format(item.getSubtotal())
                    });
                }
            }
        } else {
            // Jika tidak ada items, tampilkan pesan
            model.addRow(new Object[]{"Tidak ada data produk", "", "", "", ""});
        }

        JTable table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(550, 200));
        table.setFillsViewportHeight(true);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setBackground(darkBrown);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setFont(new Font("SansSerif", Font.PLAIN, 11));
        table.setRowHeight(25);
        table.setGridColor(new Color(220, 220, 220));
        
        // Set alignment untuk kolom numerik
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer); // Harga Satuan
        table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer); // Jumlah
        table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer); // Subtotal
        
        // Set lebar kolom
        table.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID Produk
        table.getColumnModel().getColumn(1).setPreferredWidth(200);  // Nama Produk
        table.getColumnModel().getColumn(2).setPreferredWidth(120);  // Harga Satuan
        table.getColumnModel().getColumn(3).setPreferredWidth(60);   // Jumlah
        table.getColumnModel().getColumn(4).setPreferredWidth(120);  // Subtotal

        return new JScrollPane(table);
    }

    private JPanel createSummaryPanel(Invoice invoice) {
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBackground(lightGray);
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Hitung subtotal items
        double subtotal = 0.0;
        if (invoice.getTransaksi().getItems() != null && !invoice.getTransaksi().getItems().isEmpty()) {
            for (CartItem item : invoice.getTransaksi().getItems()) {
                if (item != null) {
                    subtotal += item.getSubtotal();
                }
            }
        }
        
        // Panel untuk subtotal
        JPanel subtotalPanel = createSummaryRow("Subtotal:", IDR_FORMAT.format(subtotal), false);
        summaryPanel.add(subtotalPanel);
        
        // Panel untuk total (bold dan lebih besar)
        double total = 0.0;
        if (invoice.getTransaksi() != null && invoice.getTransaksi().getTotal() != null) {
            total = invoice.getTransaksi().getTotal();
        }
        JPanel totalPanel = createSummaryRow("TOTAL PEMBAYARAN:", IDR_FORMAT.format(total), true);
        summaryPanel.add(totalPanel);

        return summaryPanel;
    }
    
    private JPanel createSummaryRow(String label, String value, boolean isTotal) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBackground(lightGray);
        rowPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        JLabel labelComp = new JLabel(label);
        if (isTotal) {
            labelComp.setFont(new Font("SansSerif", Font.BOLD, 16));
            labelComp.setForeground(darkBrown);
        } else {
            labelComp.setFont(new Font("SansSerif", Font.PLAIN, 12));
        }
        
        JLabel valueComp = new JLabel(value);
        if (isTotal) {
            valueComp.setFont(new Font("SansSerif", Font.BOLD, 16));
            valueComp.setForeground(darkBrown);
        } else {
            valueComp.setFont(new Font("SansSerif", Font.PLAIN, 12));
        }
        valueComp.setHorizontalAlignment(SwingConstants.RIGHT);
        
        rowPanel.add(labelComp, BorderLayout.WEST);
        rowPanel.add(valueComp, BorderLayout.EAST);
        
        return rowPanel;
    }
    
    private JPanel createPaymentDetailPanel(Invoice invoice) {
        JPanel paymentPanel = new JPanel();
        paymentPanel.setLayout(new BoxLayout(paymentPanel, BoxLayout.Y_AXIS));
        paymentPanel.setBackground(lightGray);
        paymentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel paymentTitle = new JLabel("Informasi Pembayaran:");
        paymentTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        paymentTitle.setForeground(darkBrown);
        paymentPanel.add(paymentTitle);
        paymentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        if (invoice.getPembayaran() != null) {
            String methodName = invoice.getPembayaran().getMethodName();
            paymentPanel.add(createInfoLabel("Metode Pembayaran:", methodName));
            
            // Informasi tambahan berdasarkan jenis pembayaran
            if (invoice.getPembayaran() instanceof CODPayment) {
                CODPayment cod = (CODPayment) invoice.getPembayaran();
                String alamat = cod.getAlamatPengiriman();
                if (alamat != null && !alamat.isEmpty()) {
                    paymentPanel.add(createInfoLabel("Alamat Pengiriman:", alamat));
                }
                paymentPanel.add(createInfoLabel("Catatan:", "Pembayaran dilakukan saat barang diterima"));
            } else if (invoice.getPembayaran() instanceof TransferBank) {
                TransferBank transfer = (TransferBank) invoice.getPembayaran();
                paymentPanel.add(createInfoLabel("Bank:", transfer.getBankName()));
                paymentPanel.add(createInfoLabel("Catatan:", "Silakan transfer sesuai total pembayaran"));
            } else if (invoice.getPembayaran() instanceof QRISPayment) {
                paymentPanel.add(createInfoLabel("Catatan:", "Scan QRIS untuk melakukan pembayaran"));
            }
        } else {
            paymentPanel.add(new JLabel("Metode pembayaran tidak tersedia"));
        }
        
        return paymentPanel;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setBackground(palePink);
        footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 0, 0, 0)
        ));
        
        JLabel thankYouLabel = new JLabel("Terima kasih telah berbelanja di RASA.IN!", SwingConstants.CENTER);
        thankYouLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        thankYouLabel.setForeground(darkBrown);
        thankYouLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel infoLabel = new JLabel("Invoice ini adalah bukti pembayaran yang sah", SwingConstants.CENTER);
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        infoLabel.setForeground(new Color(100, 100, 100));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        footerPanel.add(thankYouLabel);
        footerPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        footerPanel.add(infoLabel);
        
        return footerPanel;
    }
}