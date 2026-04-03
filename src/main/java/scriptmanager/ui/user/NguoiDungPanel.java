package scriptmanager.ui.user;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import scriptmanager.entity.user.NguoiDung;
import scriptmanager.service.NguoiDungService;
import scriptmanager.ui.main.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NguoiDungPanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(NguoiDungPanel.class.getName());

    private static final Color TONE_900 = new Color(17, 17, 17);
    private static final Color BG_SOFT = new Color(245, 247, 250);

    private final MainFrame mainFrame;
    private final NguoiDungService nguoiDungService;

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtTenDangNhap;
    private JTextField txtMatKhau;
    private JComboBox<String> cbQuyenHan;
    private Integer currentId = null;

    public NguoiDungPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.nguoiDungService = new NguoiDungService();
        init();
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

        JLabel title = new JLabel("Quản Lý Người Dùng");
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

        String[] cols = {"ID", "Tên Đăng Nhập", "Quyền Hạn"};
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
        JPanel panel = new JPanel(new MigLayout("fillx,wrap,insets 20 16 20 16", "[grow]", "[]12[]12[]12[]24[]12[]"));
        panel.setBackground(Color.WHITE);
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:18;border:1,1,1,1,#D1D5DB");

        JLabel lblTitle = new JLabel("Thông Tin Người Dùng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(TONE_900);
        panel.add(lblTitle, "wrap");

        txtTenDangNhap = new JTextField();
        txtTenDangNhap.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tên đăng nhập");

        txtMatKhau = new JTextField();
        txtMatKhau.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập mật khẩu");

        cbQuyenHan = new JComboBox<>(new String[]{"ADMIN", "MANAGER", "USER"});

        JLabel lblUser = new JLabel("Tên đăng nhập:");
        lblUser.setForeground(Color.BLACK);
        JLabel lblPass = new JLabel("Mật khẩu:");
        lblPass.setForeground(Color.BLACK);
        JLabel lblRole = new JLabel("Quyền hạn:");
        lblRole.setForeground(Color.BLACK);

        panel.add(lblUser);
        panel.add(txtTenDangNhap, "growx, h 34!");
        panel.add(lblPass);
        panel.add(txtMatKhau, "growx, h 34!");
        panel.add(lblRole);
        panel.add(cbQuyenHan, "growx, h 34!");

        JButton btnSave = new JButton("Lưu Mới (Thêm)");
        btnSave.putClientProperty(FlatClientProperties.STYLE, "background:#22C55E;foreground:#FFFFFF;arc:12;focusWidth:0");

        JButton btnUpdate = new JButton("Cập Nhật");
        btnUpdate.putClientProperty(FlatClientProperties.STYLE, "background:#3B82F6;foreground:#FFFFFF;arc:12;focusWidth:0");

        JButton btnDelete = new JButton("Xóa");
        btnDelete.putClientProperty(FlatClientProperties.STYLE, "background:#FF4C4C;foreground:#FFFFFF;arc:12;focusWidth:0");
        
        JButton btnClear = new JButton("Làm Mới");
        btnClear.putClientProperty(FlatClientProperties.STYLE, "background:#F3F4F6;foreground:#111111;arc:12;focusWidth:0");

        btnSave.addActionListener(e -> saveUser());
        btnUpdate.addActionListener(e -> updateUser());
        btnDelete.addActionListener(e -> deleteUser());
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

    private void loadData() {
        new SwingWorker<List<NguoiDung>, Void>() {
            @Override
            protected List<NguoiDung> doInBackground() throws Exception {
                return nguoiDungService.getAllNguoiDung();
            }

            @Override
            protected void done() {
                try {
                    List<NguoiDung> list = get();
                    tableModel.setRowCount(0);
                    for (NguoiDung nd : list) {
                        tableModel.addRow(new Object[]{nd.getMaND(), nd.getTenDangNhap(), nd.getQuyenHan()});
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.WARNING, "Lỗi tải dữ liệu người dùng", ex);
                    JOptionPane.showMessageDialog(NguoiDungPanel.this, "Lỗi khi tải dữ liệu: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void fillForm(int row) {
        currentId = (Integer) table.getValueAt(row, 0);
        String ten = (String) table.getValueAt(row, 1);
        String quyen = (String) table.getValueAt(row, 2);

        txtTenDangNhap.setText(ten);
        txtMatKhau.setText(""); // Avoid showing real passwords, or you can implement logic to fetch it if needed.
        cbQuyenHan.setSelectedItem(quyen);
    }

    private void clearForm() {
        currentId = null;
        txtTenDangNhap.setText("");
        txtMatKhau.setText("");
        cbQuyenHan.setSelectedIndex(0);
        table.clearSelection();
    }

    private void saveUser() {
        String td = txtTenDangNhap.getText().trim();
        String mk = txtMatKhau.getText().trim();
        String qh = cbQuyenHan.getSelectedItem().toString();

        if (td.isEmpty() || mk.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        NguoiDung nd = new NguoiDung(td, mk, qh);
        
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                nguoiDungService.save(nd);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(NguoiDungPanel.this, "Thêm thành công!");
                    loadData();
                    clearForm();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(NguoiDungPanel.this, "Lỗi: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void updateUser() {
        if (currentId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn người dùng để cập nhật!");
            return;
        }

        String td = txtTenDangNhap.getText().trim();
        String mk = txtMatKhau.getText().trim();
        String qh = cbQuyenHan.getSelectedItem().toString();

        if (td.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập không được trống!");
            return;
        }

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                NguoiDung nd = nguoiDungService.getById(currentId);
                if (nd != null) {
                    nd.setTenDangNhap(td);
                    if (!mk.isEmpty()) {
                        nd.setMatKhau(mk);
                    }
                    nd.setQuyenHan(qh);
                    nguoiDungService.update(nd);
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(NguoiDungPanel.this, "Cập nhật thành công!");
                    loadData();
                    clearForm();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(NguoiDungPanel.this, "Lỗi: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void deleteUser() {
        if (currentId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn người dùng để xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    nguoiDungService.delete(currentId);
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        get();
                        JOptionPane.showMessageDialog(NguoiDungPanel.this, "Xóa thành công!");
                        loadData();
                        clearForm();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(NguoiDungPanel.this, "Lỗi: " + ex.getMessage());
                    }
                }
            }.execute();
        }
    }
}
