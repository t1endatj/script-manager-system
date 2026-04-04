package scriptmanager.ui.sukien;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import scriptmanager.entity.core.SuKienTiec;
import scriptmanager.entity.user.NguoiDung;
import scriptmanager.service.NguoiDungService;
import scriptmanager.service.SuKienTiecService;
import scriptmanager.service.SuKienTiecServiceImpl;
import scriptmanager.ui.main.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import com.github.lgooddatepicker.components.DateTimePicker;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SuKienTiecPanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(SuKienTiecPanel.class.getName());

    private static final Color TONE_900 = new Color(17, 17, 17);
    private static final Color BG_SOFT = new Color(245, 247, 250);

    private final MainFrame mainFrame;
    private final SuKienTiecService suKienTiecService;
    private final NguoiDungService nguoiDungService;

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtTenSuKien;
    private DateTimePicker dtThoiGian;
    private JTextField txtDiaDiem;
    private JComboBox<NguoiDungItem> cbNguoiDung;
    private Integer currentId = null;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public SuKienTiecPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.suKienTiecService = new SuKienTiecServiceImpl();
        this.nguoiDungService = new NguoiDungService();
        init();
        loadUsers();
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

        JLabel title = new JLabel("Quản Lý Sự Kiện / Tiệc");
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

        String[] cols = {"Mã SK", "Tên Sự Kiện", "Thời Gian", "Địa Điểm", "Người Tạo(ID)"};
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
        JPanel panel = new JPanel(new MigLayout("fillx,wrap,insets 20 16 20 16", "[grow]", "[]12[]12[]12[]12[]24[]12[]"));
        panel.setBackground(Color.WHITE);
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:18;border:1,1,1,1,#D1D5DB");

        JLabel lblTitle = new JLabel("Thông Tin Sự Kiện");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(TONE_900);
        panel.add(lblTitle, "wrap");

        txtTenSuKien = new JTextField();
        txtTenSuKien.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tên Sự Kiện");

        dtThoiGian = new DateTimePicker();
        dtThoiGian.setBackground(Color.WHITE);
        dtThoiGian.datePicker.setBackground(Color.WHITE);
        dtThoiGian.timePicker.setBackground(Color.WHITE);

        txtDiaDiem = new JTextField();
        txtDiaDiem.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Địa điểm");

        cbNguoiDung = new JComboBox<>();

        JLabel lblTen = new JLabel("Tên:");
        lblTen.setForeground(Color.BLACK);
        JLabel lblThoiGian = new JLabel("Thời gian (dd/MM/yyyy HH:mm):");
        lblThoiGian.setForeground(Color.BLACK);
        JLabel lblDiaDiem = new JLabel("Địa điểm:");
        lblDiaDiem.setForeground(Color.BLACK);
        JLabel lblPhuTrach = new JLabel("Người Phụ Trách:");
        lblPhuTrach.setForeground(Color.BLACK);

        panel.add(lblTen);
        panel.add(txtTenSuKien, "growx, h 34!");
        panel.add(lblThoiGian);
        panel.add(dtThoiGian, "growx, h 34!");
        panel.add(lblDiaDiem);
        panel.add(txtDiaDiem, "growx, h 34!");
        panel.add(lblPhuTrach);
        panel.add(cbNguoiDung, "growx, h 34!");

        JButton btnSave = new JButton("Lưu Mới (Thêm)");
        btnSave.putClientProperty(FlatClientProperties.STYLE, "background:#22C55E;foreground:#FFFFFF;arc:12;focusWidth:0");

        JButton btnUpdate = new JButton("Cập Nhật");
        btnUpdate.putClientProperty(FlatClientProperties.STYLE, "background:#3B82F6;foreground:#FFFFFF;arc:12;focusWidth:0");

        JButton btnDelete = new JButton("Xóa");
        btnDelete.putClientProperty(FlatClientProperties.STYLE, "background:#FF4C4C;foreground:#FFFFFF;arc:12;focusWidth:0");
        
        JButton btnClear = new JButton("Làm Mới");
        btnClear.putClientProperty(FlatClientProperties.STYLE, "background:#F3F4F6;foreground:#111111;arc:12;focusWidth:0");

        JButton btnKichBan = new JButton("Kịch Bản");
        btnKichBan.putClientProperty(FlatClientProperties.STYLE, "background:#10B981;foreground:#FFFFFF;arc:12;focusWidth:0");
        btnKichBan.addActionListener(e -> {
            if (currentId == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một sự kiện để quản lý kịch bản!");
                return;
            }
            mainFrame.showHangMucManagerForEvent(currentId);
        });

        btnSave.addActionListener(e -> saveEvent());
        btnUpdate.addActionListener(e -> updateEvent());
        btnDelete.addActionListener(e -> deleteEvent());
        btnClear.addActionListener(e -> clearForm());

        JPanel buttons1 = new JPanel(new MigLayout("insets 0, gap 8", "[grow][grow]", "[]"));
        buttons1.setOpaque(false);
        buttons1.add(btnSave, "growx, h 34!");
        buttons1.add(btnUpdate, "growx, h 34!");

        JPanel buttons2 = new JPanel(new MigLayout("insets 0, gap 8", "[grow][grow][grow]", "[]"));
        buttons2.setOpaque(false);
        buttons2.add(btnKichBan, "growx, h 34!");
        buttons2.add(btnDelete, "growx, h 34!");
        buttons2.add(btnClear, "growx, h 34!");

        panel.add(buttons1, "growx");
        panel.add(buttons2, "growx");

        return panel;
    }

    private void loadUsers() {
        new SwingWorker<List<NguoiDung>, Void>() {
            @Override
            protected List<NguoiDung> doInBackground() {
                return nguoiDungService.getAllNguoiDung();
            }

            @Override
            protected void done() {
                try {
                    List<NguoiDung> list = get();
                    cbNguoiDung.removeAllItems();
                    for (NguoiDung nd : list) {
                        cbNguoiDung.addItem(new NguoiDungItem(nd.getMaND(), nd.getTenDangNhap()));
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.WARNING, "Lỗi tải danh sách người dùng cho combobox sự kiện", ex);
                }
            }
        }.execute();
    }

    private void loadData() {
        new SwingWorker<List<SuKienTiec>, Void>() {
            @Override
            protected List<SuKienTiec> doInBackground() {
                return suKienTiecService.findAll();
            }

            @Override
            protected void done() {
                try {
                    List<SuKienTiec> list = get();
                    tableModel.setRowCount(0);
                    for (SuKienTiec sk : list) {
                        String timeStr = (sk.getThoiGianToChuc() != null) ? sk.getThoiGianToChuc().format(formatter) : "";
                        String userStr = (sk.getNguoiDung() != null) ? sk.getNguoiDung().getTenDangNhap() + " (" + sk.getNguoiDung().getMaND() + ")" : "N/A";
                        tableModel.addRow(new Object[]{sk.getMaSK(), sk.getTenSuKien(), timeStr, sk.getDiaDiem(), userStr});
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.WARNING, "Lỗi tải dữ liệu sự kiện", ex);
                    JOptionPane.showMessageDialog(SuKienTiecPanel.this, "Lỗi tải dữ liệu Sự Kiện: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void fillForm(int row) {
        currentId = (Integer) table.getValueAt(row, 0);
        
        new SwingWorker<SuKienTiec, Void>() {
            @Override
            protected SuKienTiec doInBackground() {
                return suKienTiecService.findById(currentId);
            }
            @Override
            protected void done() {
                try {
                    SuKienTiec sk = get();
                    if (sk != null) {
                        txtTenSuKien.setText(sk.getTenSuKien());
                        if (sk.getThoiGianToChuc() != null) {
                            dtThoiGian.setDateTimePermissive(sk.getThoiGianToChuc());
                        } else {
                            dtThoiGian.setDateTimePermissive(LocalDateTime.now());
                        }
                        txtDiaDiem.setText(sk.getDiaDiem());
                        
                        if (sk.getNguoiDung() != null) {
                            for (int i = 0; i < cbNguoiDung.getItemCount(); i++) {
                                if (cbNguoiDung.getItemAt(i).id == sk.getNguoiDung().getMaND()) {
                                    cbNguoiDung.setSelectedIndex(i);
                                    break;
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.WARNING, "Lỗi đổ dữ liệu sự kiện lên form", ex);
                }
            }
        }.execute();
    }

    private void clearForm() {
        currentId = null;
        txtTenSuKien.setText("");
        dtThoiGian.setDateTimePermissive(LocalDateTime.now());
        txtDiaDiem.setText("");
        if (cbNguoiDung.getItemCount() > 0) cbNguoiDung.setSelectedIndex(0);
        table.clearSelection();
    }

   private void saveEvent() {
        String ten = txtTenSuKien.getText().trim();
        LocalDateTime time = dtThoiGian.getDateTimePermissive();
        String dia = txtDiaDiem.getText().trim();
        NguoiDungItem ndItem = (NguoiDungItem) cbNguoiDung.getSelectedItem();

        if (ten.isEmpty() || ndItem == null) {
            JOptionPane.showMessageDialog(this, "Tên sự kiện và người phụ trách không được rỗng!");
            return;
        }

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                NguoiDung nd = nguoiDungService.getById(ndItem.id);
                SuKienTiec sk = new SuKienTiec();
                sk.setTenSuKien(ten);
                sk.setThoiGianToChuc(time);
                sk.setDiaDiem(dia);
                sk.setNguoiDung(nd);
                suKienTiecService.save(sk);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(SuKienTiecPanel.this, "Thêm thành công!");
                    loadData();
                    clearForm();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(SuKienTiecPanel.this, "Lỗi: " + ex.getMessage());
                    JOptionPane.showMessageDialog(SuKienTiecPanel.this, resolveErrorMessage(ex));
                }
            }
        }.execute();
    }

    private void updateEvent() {
        if (currentId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sự kiện sửa!");
            return;
        }

        String ten = txtTenSuKien.getText().trim();
        LocalDateTime time = dtThoiGian.getDateTimePermissive();
        String dia = txtDiaDiem.getText().trim();
        NguoiDungItem ndItem = (NguoiDungItem) cbNguoiDung.getSelectedItem();

        if (ten.isEmpty() || ndItem == null) {
            JOptionPane.showMessageDialog(this, "Tên sự kiện và người phụ trách rỗng!");
            return;
        }

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                SuKienTiec sk = suKienTiecService.findById(currentId);
                if (sk != null) {
                    sk.setTenSuKien(ten);
                    sk.setThoiGianToChuc(time);
                    sk.setDiaDiem(dia);
                    sk.setNguoiDung(nguoiDungService.getById(ndItem.id));
                    suKienTiecService.update(sk);
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(SuKienTiecPanel.this, "Cập nhật thành công!");
                    loadData();
                    clearForm();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(SuKienTiecPanel.this, resolveErrorMessage(ex));
                }
            }
        }.execute();
    }

    private String resolveErrorMessage(Exception ex) {
        Throwable root = ex;
        if (ex instanceof ExecutionException && ex.getCause() != null) {
            root = ex.getCause();
        }

        String message = root.getMessage();
        if (message == null || message.isBlank()) {
            return "Thao tác thất bại. Vui lòng kiểm tra dữ liệu và thử lại.";
        }
        return message;
    }

    private void deleteEvent() {
        if (currentId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sự kiện xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc xóa Sự Kiện này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    suKienTiecService.delete(currentId);
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        get();
                        JOptionPane.showMessageDialog(SuKienTiecPanel.this, "Xóa thành công!");
                        loadData();
                        clearForm();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(SuKienTiecPanel.this, "Lỗi: " + ex.getMessage());
                    }
                }
            }.execute();
        }
    }

    // Lớp phụ dùng hiển thị NguoiDung combobox
    private static class NguoiDungItem {
        int id;
        String name;

        NguoiDungItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name + " (ID: " + id + ")";
        }
    }
}
