package scriptmanager.ui.hangmuc;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import scriptmanager.entity.core.HangMucKichBan;
import scriptmanager.entity.core.SuKienTiec;
import scriptmanager.service.HangMucKichBanService;
import scriptmanager.service.HangMucKichBanServiceImpl;
import scriptmanager.service.SuKienTiecService;
import scriptmanager.service.SuKienTiecServiceImpl;
import scriptmanager.ui.main.MainFrame;

import com.github.lgooddatepicker.components.DateTimePicker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HangMucKichBanPanel extends JPanel {

    private static final Color TONE_900 = new Color(17, 17, 17);
    private static final Color BG_SOFT = new Color(245, 247, 250);

    private final MainFrame mainFrame;
    private final HangMucKichBanService hangMucService;
    private final SuKienTiecService suKienTiecService;

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtTenHM;
    private DateTimePicker dtTgBatDau;
    private DateTimePicker dtTgKetThuc;
    private JTextArea txtNoiDung;
    private JComboBox<SuKienItem> cbSuKien;
    private Integer currentId = null;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public HangMucKichBanPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.hangMucService = new HangMucKichBanServiceImpl();
        this.suKienTiecService = new SuKienTiecServiceImpl();
        init();
        loadSuKien();
        loadData();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBackground(BG_SOFT);
        setBorder(new EmptyBorder(22, 24, 22, 24));

        JPanel container = new JPanel(new MigLayout("fill,wrap,insets 0,gap 14", "[grow][350]", "[][grow]"));
        container.setOpaque(false);

        container.add(createHeader(), "span 2,growx");
        container.add(createTablePanel(), "grow,push");
        container.add(createFormPanel(), "growy");

        add(container, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new MigLayout("fillx,insets 16 20 16 20", "[grow][]", "[]"));
        panel.setBackground(TONE_900);
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:24");

        JLabel title = new JLabel("Quản Lý Hạng Mục Kịch Bản");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        panel.add(title, "growx");

        JButton btnBack = new JButton("Quay lại Dashboard");
        btnBack.putClientProperty(FlatClientProperties.STYLE,
                "arc:12;background:#F3F4F6;foreground:#111111;focusWidth:0;borderWidth:0");
        btnBack.addActionListener(e -> mainFrame.showDashboard());
        panel.add(btnBack, "w 150!,h 34!");

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new MigLayout("fill,wrap,insets 14 16 14 16", "[grow]", "[grow]"));
        panel.setBackground(Color.WHITE);
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:18;border:1,1,1,1,#D1D5DB");

        String[] cols = {"Mã HM", "Tên Hạng Mục", "Sự Kiện/Tiệc", "Bắt Đầu", "Kết Thúc"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(34);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(229, 231, 235));
        table.setSelectionForeground(TONE_900);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(243, 244, 246));
        table.getTableHeader().setForeground(TONE_900);
        table.setShowVerticalLines(false);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    fillForm(row);
                }
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        panel.add(sp, "grow,push");

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap,insets 20 16 20 16", "[grow]", "[]12[]12[]12[]12[]12[]24[]12[]"));
        panel.setBackground(Color.WHITE);
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:18;border:1,1,1,1,#D1D5DB");

        JLabel lblTitle = new JLabel("Chi Tiết Hạng Mục");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(TONE_900);
        panel.add(lblTitle, "wrap");

        txtTenHM = new JTextField();
        txtTenHM.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tên hạng mục kịch bản");

        cbSuKien = new JComboBox<>();

        dtTgBatDau = new DateTimePicker();
        dtTgBatDau.setBackground(Color.WHITE);
        dtTgBatDau.datePicker.setBackground(Color.WHITE);
        dtTgBatDau.timePicker.setBackground(Color.WHITE);

        dtTgKetThuc = new DateTimePicker();
        dtTgKetThuc.setBackground(Color.WHITE);
        dtTgKetThuc.datePicker.setBackground(Color.WHITE);
        dtTgKetThuc.timePicker.setBackground(Color.WHITE);

        txtNoiDung = new JTextArea(4, 20);
        txtNoiDung.setLineWrap(true);
        txtNoiDung.setWrapStyleWord(true);
        JScrollPane scrollNoiDung = new JScrollPane(txtNoiDung);

        JLabel lblTenHM = new JLabel("Tên HM:");
        lblTenHM.setForeground(Color.BLACK);
        JLabel lblSuKien = new JLabel("Thuộc sự kiện:");
        lblSuKien.setForeground(Color.BLACK);
        JLabel lblBatDau = new JLabel("Bắt đầu (dd/MM/yyyy HH:mm):");
        lblBatDau.setForeground(Color.BLACK);
        JLabel lblKetThuc = new JLabel("Kết thúc (dd/MM/yyyy HH:mm):");
        lblKetThuc.setForeground(Color.BLACK);
        JLabel lblNoiDung = new JLabel("Nội dung kịch bản:");
        lblNoiDung.setForeground(Color.BLACK);

        panel.add(lblTenHM);
        panel.add(txtTenHM, "growx, h 34!");
        panel.add(lblSuKien);
        panel.add(cbSuKien, "growx, h 34!");
        panel.add(lblBatDau);
        panel.add(dtTgBatDau, "growx, h 34!");
        panel.add(lblKetThuc);
        panel.add(dtTgKetThuc, "growx, h 34!");
        panel.add(lblNoiDung);
        panel.add(scrollNoiDung, "growx, h 100!");

        JButton btnSave = new JButton("Lưu Mới (Thêm)");
        btnSave.putClientProperty(FlatClientProperties.STYLE, "background:#22C55E;foreground:#FFFFFF;arc:12;focusWidth:0");

        JButton btnUpdate = new JButton("Cập Nhật");
        btnUpdate.putClientProperty(FlatClientProperties.STYLE, "background:#3B82F6;foreground:#FFFFFF;arc:12;focusWidth:0");

        JButton btnDelete = new JButton("Xóa");
        btnDelete.putClientProperty(FlatClientProperties.STYLE, "background:#FF4C4C;foreground:#FFFFFF;arc:12;focusWidth:0");
        
        JButton btnClear = new JButton("Làm Mới");
        btnClear.putClientProperty(FlatClientProperties.STYLE, "background:#F3F4F6;foreground:#111111;arc:12;focusWidth:0");

        btnSave.addActionListener(e -> saveHM());
        btnUpdate.addActionListener(e -> updateHM());
        btnDelete.addActionListener(e -> deleteHM());
        btnClear.addActionListener(e -> clearForm());

        JPanel buttons1 = new JPanel(new MigLayout("insets 0, gap 8", "[grow][grow]", "[]"));
        buttons1.setOpaque(false);
        buttons1.add(btnSave, "growx, h 34!");
        buttons1.add(btnUpdate, "growx, h 34!");

        JPanel buttons2 = new JPanel(new MigLayout("insets 0, gap 8", "[grow][grow]", "[]"));
        buttons2.setOpaque(false);
        buttons2.add(btnDelete, "growx, h 34!");
        buttons2.add(btnClear, "growx, h 34!");

        panel.add(buttons1, "growx");
        panel.add(buttons2, "growx");

        return panel;
    }

    private void loadSuKien() {
        new SwingWorker<List<SuKienTiec>, Void>() {
            @Override
            protected List<SuKienTiec> doInBackground() {
                return suKienTiecService.findAll();
            }

            @Override
            protected void done() {
                try {
                    List<SuKienTiec> list = get();
                    cbSuKien.removeAllItems();
                    for (SuKienTiec sk : list) {
                        cbSuKien.addItem(new SuKienItem(sk.getMaSK(), sk.getTenSuKien()));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.execute();
    }

    private void loadData() {
        new SwingWorker<List<HangMucKichBan>, Void>() {
            @Override
            protected List<HangMucKichBan> doInBackground() {
                return hangMucService.findAll();
            }

            @Override
            protected void done() {
                try {
                    List<HangMucKichBan> list = get();
                    tableModel.setRowCount(0);
                    for (HangMucKichBan hm : list) {
                        String skStr = (hm.getSuKienTiec() != null) ? hm.getSuKienTiec().getTenSuKien() + " (" + hm.getSuKienTiec().getMaSK() + ")" : "N/A";
                        String startStr = (hm.getTgBatDau() != null) ? hm.getTgBatDau().format(formatter) : "";
                        String endStr = (hm.getTgKetThuc() != null) ? hm.getTgKetThuc().format(formatter) : "";
                        tableModel.addRow(new Object[]{hm.getMaHM(), hm.getTenHM(), skStr, startStr, endStr});
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(HangMucKichBanPanel.this, "Lỗi tải Kịch Bản: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void fillForm(int row) {
        currentId = (Integer) table.getValueAt(row, 0);
        
        new SwingWorker<HangMucKichBan, Void>() {
            @Override
            protected HangMucKichBan doInBackground() {
                return hangMucService.findById(currentId);
            }
            @Override
            protected void done() {
                try {
                    HangMucKichBan hm = get();
                    if (hm != null) {
                        txtTenHM.setText(hm.getTenHM());

                        if (hm.getTgBatDau() != null) {
                            dtTgBatDau.setDateTimePermissive(hm.getTgBatDau());
                        } else {
                            dtTgBatDau.setDateTimePermissive(LocalDateTime.now());
                        }

                        if (hm.getTgKetThuc() != null) {
                            dtTgKetThuc.setDateTimePermissive(hm.getTgKetThuc());
                        } else {
                            dtTgKetThuc.setDateTimePermissive(LocalDateTime.now());
                        }

                        txtNoiDung.setText(hm.getNoiDung());
                        
                        if (hm.getSuKienTiec() != null) {
                            for (int i = 0; i < cbSuKien.getItemCount(); i++) {
                                if (cbSuKien.getItemAt(i).id == hm.getSuKienTiec().getMaSK()) {
                                    cbSuKien.setSelectedIndex(i);
                                    break;
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                   ex.printStackTrace();
                }
            }
        }.execute();
    }

    private void clearForm() {
        currentId = null;
        txtTenHM.setText("");
        dtTgBatDau.setDateTimePermissive(LocalDateTime.now());
        dtTgKetThuc.setDateTimePermissive(LocalDateTime.now());
        txtNoiDung.setText("");
        if (cbSuKien.getItemCount() > 0) cbSuKien.setSelectedIndex(0);
        table.clearSelection();
    }

    private void saveHM() {
        String ten = txtTenHM.getText().trim();
        SuKienItem skItem = (SuKienItem) cbSuKien.getSelectedItem();
        LocalDateTime tgbd = dtTgBatDau.getDateTimeStrict();
        LocalDateTime tgkt = dtTgKetThuc.getDateTimeStrict();
        String noiDung = txtNoiDung.getText().trim();

        if (ten.isEmpty() || skItem == null) {
            JOptionPane.showMessageDialog(this, "Tên hạng mục và sự kiện không được rỗng!");
            return;
        }

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                SuKienTiec sk = suKienTiecService.findById(skItem.id);
                HangMucKichBan hm = new HangMucKichBan(ten, sk);
                
                hm.setTgBatDau(tgbd);
                hm.setTgKetThuc(tgkt);
                hm.setNoiDung(noiDung);

                hangMucService.save(hm);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(HangMucKichBanPanel.this, "Thêm thành công!");
                    loadData();
                    clearForm();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(HangMucKichBanPanel.this, "Lỗi: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void updateHM() {
        if (currentId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hạng mục cần sửa!");
            return;
        }

        String ten = txtTenHM.getText().trim();
        SuKienItem skItem = (SuKienItem) cbSuKien.getSelectedItem();
        LocalDateTime tgbd = dtTgBatDau.getDateTimeStrict();
        LocalDateTime tgkt = dtTgKetThuc.getDateTimeStrict();
        String noiDung = txtNoiDung.getText().trim();

        if (ten.isEmpty() || skItem == null) {
            JOptionPane.showMessageDialog(this, "Tên và sự kiện không được rỗng!");
            return;
        }

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                HangMucKichBan hm = hangMucService.findById(currentId);
                if (hm != null) {
                    hm.setTenHM(ten);
                    hm.setTgBatDau(tgbd);
                    hm.setTgKetThuc(tgkt);
                    
                    hm.setNoiDung(noiDung);
                    hm.setSuKienTiec(suKienTiecService.findById(skItem.id));
                    hangMucService.update(hm);
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(HangMucKichBanPanel.this, "Cập nhật thành công!");
                    loadData();
                    clearForm();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(HangMucKichBanPanel.this, "Lỗi: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void deleteHM() {
        if (currentId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hạng mục để xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc xóa Hạng Mục Kịch Bản này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    hangMucService.delete(currentId);
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        get();
                        JOptionPane.showMessageDialog(HangMucKichBanPanel.this, "Xóa thành công!");
                        loadData();
                        clearForm();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(HangMucKichBanPanel.this, "Lỗi: " + ex.getMessage());
                    }
                }
            }.execute();
        }
    }

    // Lớp phụ dùng hiển thị SuKien combobox
    private static class SuKienItem {
        int id;
        String name;

        SuKienItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name + " (ID: " + id + ")";
        }
    }
}
