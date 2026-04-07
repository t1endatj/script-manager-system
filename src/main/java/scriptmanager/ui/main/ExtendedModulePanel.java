package scriptmanager.ui.main;

import com.github.lgooddatepicker.components.DateTimePicker;
import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import scriptmanager.entity.asset.DaoCu;
import scriptmanager.entity.asset.DanhSachNhac;
import scriptmanager.entity.asset.HieuUng;
import scriptmanager.entity.asset.ThietBi;
import scriptmanager.entity.assignment.SuDungDaoCu;
import scriptmanager.entity.assignment.pk.SuDungDaoCuId;
import scriptmanager.entity.core.HangMucKichBan;
import scriptmanager.entity.core.LichTongDuyet;
import scriptmanager.entity.core.SuKienTiec;
import scriptmanager.entity.user.DoiTac;
import scriptmanager.entity.user.NhanSu;
import scriptmanager.service.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

public class ExtendedModulePanel extends JPanel {

    private static final Color BG_SOFT = new Color(245, 247, 250);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_DARK = new Color(17, 17, 17);
    private static final Color INPUT_TEXT = new Color(46, 42, 39);

    private final MainFrame mainFrame;
    private final DoiTacService doiTacService = new DoiTacService();
    private final NhanSuService nhanSuService = new NhanSuService();
    private final ThietBiService thietBiService = new ThietBiService();
    private final DaoCuService daoCuService = new DaoCuService();
    private final HieuUngService hieuUngService = new HieuUngService();
    private final DanhSachNhacService danhSachNhacService = new DanhSachNhacService();
    private final LichTongDuyetService lichTongDuyetService = new LichTongDuyetService();
    private final SuDungDaoCuService suDungDaoCuService = new SuDungDaoCuService();
    private final HangMucKichBanService hangMucService = new HangMucKichBanServiceImpl();
    private final SuKienTiecService suKienService = new SuKienTiecServiceImpl();
    private final PhanCongNhanSuService phanCongNhanSuService = new PhanCongNhanSuService();

    public ExtendedModulePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBackground(BG_SOFT);

        JPanel header = new JPanel(new MigLayout("fillx,insets 12 16 12 16", "[grow][]", "[]"));
        header.setBackground(CARD_BG);
        header.putClientProperty(FlatClientProperties.STYLE,
                "arc:18;" +
                        "border:1,1,1,1,#D1D5DB");

        JLabel title = new JLabel("Phân phối tài nguyên");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT_DARK);

        JButton back = new JButton("Quay lại Dashboard");
        back.putClientProperty(FlatClientProperties.STYLE,
                "arc:12;" +
                        "background:#2563EB;" +
                        "foreground:#FFFFFF;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
        back.addActionListener(e -> mainFrame.showDashboard());

        header.add(title, "growx");
        header.add(back, "h 34!");

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(CARD_BG);
        tabs.setForeground(TEXT_DARK);
        tabs.putClientProperty(FlatClientProperties.STYLE,
                "foreground:#111111;" +
                        "selectedForeground:#111111;");
        tabs.addTab("Đối tác", createDoiTacTab());
        tabs.addTab("Nhân sự", createNhanSuTab());
        tabs.addTab("Phân công nhân sự", createPhanCongNhanSuTheoSuKienTab());
        tabs.addTab("Thiết bị", createThietBiTab());
        tabs.addTab("Đạo cụ", createDaoCuTab());
        tabs.addTab("Hiệu ứng", createHieuUngTab());
        tabs.addTab("Danh sách nhạc", createDanhSachNhacTab());
        tabs.addTab("Lịch tổng duyệt", createLichTongDuyetTab());
        tabs.addTab("Sử dụng đạo cụ", createSuDungDaoCuTab());

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JComponent createDoiTacTab() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã ĐT", "Tên đơn vị", "Lĩnh vực", "SĐT"}, 0);
        JTable table = new JTable(model);
        JTextField ten = new JTextField();
        JTextField linhVuc = new JTextField();
        JTextField sdt = new JTextField();
        final int[] selectedId = {-1};

        Runnable load = () -> {
            model.setRowCount(0);
            List<DoiTac> items = doiTacService.findAll();
            for (DoiTac item : items) {
                model.addRow(new Object[]{item.getMaDT(), item.getTenDonVi(), item.getLinhVuc(), item.getSdt()});
            }
        };

        JButton add = new JButton("Thêm");
        styleAddButton(add);
        add.addActionListener(e -> {
            try {
                doiTacService.save(new DoiTac(ten.getText().trim(), linhVuc.getText().trim(), sdt.getText().trim()));
                ten.setText("");
                linhVuc.setText("");
                sdt.setText("");
                table.clearSelection();
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Thêm đối tác thất bại: " + ex.getMessage());
            }
        });

        JButton delete = new JButton("Xóa dòng chọn");
        styleDeleteButton(delete);
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                doiTacService.delete((int) model.getValueAt(row, 0));
                load.run();
            }
        });

        JButton update = new JButton("Cập nhật dòng chọn");
        styleUpdateButton(update);
        update.addActionListener(e -> {
            if (selectedId[0] < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đối tác để cập nhật.");
                return;
            }
            try {
                DoiTac item = doiTacService.findById(selectedId[0]);
                if (item == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy đối tác.");
                    return;
                }
                item.setTenDonVi(ten.getText().trim());
                item.setLinhVuc(linhVuc.getText().trim());
                item.setSdt(sdt.getText().trim());
                doiTacService.update(item);
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cập nhật đối tác thất bại: " + ex.getMessage());
            }
        });

        JButton reset = new JButton("Làm mới");
        reset.putClientProperty(FlatClientProperties.STYLE,
                "arc:10;" +
                        "background:#F3F4F6;" +
                        "foreground:#111111;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
        reset.addActionListener(e -> {
            selectedId[0] = -1;
            ten.setText("");
            linhVuc.setText("");
            sdt.setText("");
            table.clearSelection();
            ten.requestFocusInWindow();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    selectedId[0] = (int) model.getValueAt(row, 0);
                    ten.setText(String.valueOf(model.getValueAt(row, 1)));
                    linhVuc.setText(String.valueOf(model.getValueAt(row, 2)));
                    sdt.setText(String.valueOf(model.getValueAt(row, 3)));
                }
            }
        });

        return wrapCrud(table, load, new String[]{"Tên đơn vị", "Lĩnh vực", "SĐT"}, new JComponent[]{ten, linhVuc, sdt}, add, delete, update, reset);
    }

    private JComponent createNhanSuTab() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã NS", "Tên nhân sự", "SĐT", "Vai trò", "Có ảnh"}, 0);
        JTable table = new JTable(model);
        JTextField ten = new JTextField();
        JTextField sdt = new JTextField();
        JTextField vaiTro = new JTextField();

        JLabel lblImagePreview = new JLabel("Chưa có ảnh", SwingConstants.CENTER);
        lblImagePreview.setPreferredSize(new Dimension(60, 60));
        lblImagePreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JButton btnChooseImage = new JButton("Chọn ảnh");
        btnChooseImage.putClientProperty(FlatClientProperties.STYLE,
                "arc:10;" +
                        "background:#111827;" +
                        "foreground:#FFFFFF;" +
                        "focusWidth:0;" +
                        "borderWidth:0");

        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        imagePanel.setOpaque(false);
        imagePanel.add(lblImagePreview);
        imagePanel.add(btnChooseImage);

        final byte[][] selectedImageBytes = {null};
        final int[] selectedId = {-1};

        btnChooseImage.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("Hình ảnh (JPG, PNG)", "jpg", "jpeg", "png"));
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = chooser.getSelectedFile();
                    selectedImageBytes[0] = Files.readAllBytes(file.toPath());
                    ImageIcon icon = new ImageIcon(selectedImageBytes[0]);
                    Image img = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                    lblImagePreview.setIcon(new ImageIcon(img));
                    lblImagePreview.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Không thể đọc file ảnh: " + ex.getMessage());
                }
            }
        });

        Runnable resetForm = () -> {
            selectedId[0] = -1;
            ten.setText("");
            sdt.setText("");
            vaiTro.setText("");
            selectedImageBytes[0] = null;
            lblImagePreview.setIcon(null);
            lblImagePreview.setText("Chưa có ảnh");
            table.clearSelection();
            ten.requestFocusInWindow();
        };

        Runnable load = () -> {
            model.setRowCount(0);
            for (NhanSu item : nhanSuService.findAll()) {
                String coAnh = (item.getAnhDaiDien() != null && item.getAnhDaiDien().length > 0) ? "Có" : "Không";
                model.addRow(new Object[]{item.getMaNS(), item.getTenNS(), item.getSdt(), item.getVaiTro(), coAnh});
            }
        };

        JButton add = new JButton("Thêm");
        styleAddButton(add);
        add.addActionListener(e -> {
            try {
                NhanSu ns = new NhanSu(ten.getText().trim(), sdt.getText().trim(), vaiTro.getText().trim());
                ns.setAnhDaiDien(selectedImageBytes[0]);
                nhanSuService.save(ns);
                resetForm.run();
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Thêm nhân sự thất bại: " + ex.getMessage());
            }
        });

        JButton delete = new JButton("Xóa dòng chọn");
        styleDeleteButton(delete);
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa.");
                return;
            }
            try {
                nhanSuService.delete((int) model.getValueAt(row, 0));
                JOptionPane.showMessageDialog(this, "Xóa nhân sự thành công.");
                resetForm.run();
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Xóa nhân sự thất bại: " + ex.getMessage());
            }
        });

        JButton update = new JButton("Cập nhật dòng chọn");
        styleUpdateButton(update);
        update.addActionListener(e -> {
            if (selectedId[0] < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân sự để cập nhật.");
                return;
            }
            try {
                NhanSu item = nhanSuService.findById(selectedId[0]);
                if (item == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy nhân sự.");
                    return;
                }
                item.setTenNS(ten.getText().trim());
                item.setSdt(sdt.getText().trim());
                item.setVaiTro(vaiTro.getText().trim());
                item.setAnhDaiDien(selectedImageBytes[0]);
                nhanSuService.update(item);
                resetForm.run();
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cập nhật nhân sự thất bại: " + ex.getMessage());
            }
        });

        JButton reset = new JButton("Làm mới");
        reset.putClientProperty(FlatClientProperties.STYLE,
                "arc:10;" +
                        "background:#F3F4F6;" +
                        "foreground:#111111;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
        reset.addActionListener(e -> {
            resetForm.run();
            load.run();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    selectedId[0] = (int) model.getValueAt(row, 0);
                    ten.setText(String.valueOf(model.getValueAt(row, 1)));
                    sdt.setText(String.valueOf(model.getValueAt(row, 2)));
                    vaiTro.setText(String.valueOf(model.getValueAt(row, 3)));

                    NhanSu ns = nhanSuService.findById(selectedId[0]);
                    if (ns != null && ns.getAnhDaiDien() != null && ns.getAnhDaiDien().length > 0) {
                        selectedImageBytes[0] = ns.getAnhDaiDien();
                        ImageIcon icon = new ImageIcon(selectedImageBytes[0]);
                        Image img = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                        lblImagePreview.setIcon(new ImageIcon(img));
                        lblImagePreview.setText("");
                    } else {
                        selectedImageBytes[0] = null;
                        lblImagePreview.setIcon(null);
                        lblImagePreview.setText("Chưa có ảnh");
                    }
                }
            }
        });

        return wrapCrud(table, load,
                new String[]{"Tên nhân sự", "SĐT", "Vai trò", "Ảnh đại diện"},
                new JComponent[]{ten, sdt, vaiTro, imagePanel}, add, delete, update, reset);
    }

    private JComponent createPhanCongNhanSuTheoSuKienTab() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Mã SK", "Sự kiện", "Mã HM", "Hạng mục", "Mã NS", "Nhân sự", "Nhiệm vụ"}, 0);
        JTable table = new JTable(model);

        JComboBox<IdNameItem> cbSuKien = new JComboBox<>();
        JComboBox<IdNameItem> cbHangMuc = new JComboBox<>();
        JComboBox<IdNameItem> cbNhanSu = new JComboBox<>();
        JTextField nhiemVu = new JTextField();
        final int[] selectedHangMucId = {-1};
        final int[] selectedNhanSuId = {-1};

        Runnable loadHangMucByEvent = () -> {
            cbHangMuc.removeAllItems();
            IdNameItem suKienItem = (IdNameItem) cbSuKien.getSelectedItem();
            if (suKienItem == null) {
                return;
            }
            for (HangMucKichBan hm : hangMucService.findBySuKienId(suKienItem.id)) {
                cbHangMuc.addItem(new IdNameItem(hm.getMaHM(), hm.getTenHM()));
            }
        };

        Runnable resetForm = () -> {
            selectedHangMucId[0] = -1;
            selectedNhanSuId[0] = -1;
            table.clearSelection();
            nhiemVu.setText("");
            if (cbSuKien.getItemCount() > 0 && cbSuKien.getSelectedIndex() < 0) {
                cbSuKien.setSelectedIndex(0);
            }
            loadHangMucByEvent.run();
            if (cbHangMuc.getItemCount() > 0) {
                cbHangMuc.setSelectedIndex(0);
            }
            if (cbNhanSu.getItemCount() > 0) {
                cbNhanSu.setSelectedIndex(0);
            }
        };

        cbSuKien.addActionListener(e -> loadHangMucByEvent.run());

        Runnable load = () -> {
            model.setRowCount(0);
            for (java.util.Map<String, Object> item : phanCongNhanSuService.findAllView()) {
                model.addRow(new Object[]{
                        item.get("maSK"),
                        item.get("tenSuKien"),
                        item.get("maHM"),
                        item.get("tenHangMuc"),
                        item.get("maNS"),
                        item.get("tenNhanSu"),
                        item.get("nhiemVu")
                });
            }

            Object selectedEvent = cbSuKien.getSelectedItem();
            Object selectedHangMuc = cbHangMuc.getSelectedItem();
            Object selectedNhanSu = cbNhanSu.getSelectedItem();

            cbSuKien.removeAllItems();
            for (SuKienTiec sk : suKienService.findAll()) {
                cbSuKien.addItem(new IdNameItem(sk.getMaSK(), sk.getTenSuKien()));
            }

            if (selectedEvent instanceof IdNameItem selectedEventItem) {
                selectComboById(cbSuKien, selectedEventItem.id);
            }
            if (cbSuKien.getSelectedItem() == null && cbSuKien.getItemCount() > 0) {
                cbSuKien.setSelectedIndex(0);
            }

            loadHangMucByEvent.run();
            if (selectedHangMuc instanceof IdNameItem selectedHangMucItem) {
                selectComboById(cbHangMuc, selectedHangMucItem.id);
            }
            if (cbHangMuc.getSelectedItem() == null && cbHangMuc.getItemCount() > 0) {
                cbHangMuc.setSelectedIndex(0);
            }

            cbNhanSu.removeAllItems();
            for (NhanSu ns : nhanSuService.findAll()) {
                cbNhanSu.addItem(new IdNameItem(ns.getMaNS(), ns.getTenNS()));
            }
            if (selectedNhanSu instanceof IdNameItem selectedNhanSuItem) {
                selectComboById(cbNhanSu, selectedNhanSuItem.id);
            }
            if (cbNhanSu.getSelectedItem() == null && cbNhanSu.getItemCount() > 0) {
                cbNhanSu.setSelectedIndex(0);
            }
        };

        JButton assign = new JButton("Phân công theo sự kiện");
        styleUpdateButton(assign);
        assign.addActionListener(e -> {
            try {
                IdNameItem suKienItem = (IdNameItem) cbSuKien.getSelectedItem();
                IdNameItem hangMucItem = (IdNameItem) cbHangMuc.getSelectedItem();
                IdNameItem nhanSuItem = (IdNameItem) cbNhanSu.getSelectedItem();
                if (suKienItem == null || hangMucItem == null || nhanSuItem == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ sự kiện, hạng mục và nhân sự.");
                    return;
                }

                if (existsAssignment(model, hangMucItem.id, nhanSuItem.id, -1)) {
                    JOptionPane.showMessageDialog(this, "Phân công đã tồn tại. Vui lòng dùng nút cập nhật.");
                    return;
                }

                phanCongNhanSuService.assignNhanSuToHangMuc(
                        suKienItem.id,
                        hangMucItem.id,
                        nhanSuItem.id,
                        nhiemVu.getText()
                );
                JOptionPane.showMessageDialog(this, "Phân công thành công.");
                resetForm.run();
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Phân công nhân sự thất bại: " + ex.getMessage());
            }
        });

        JButton update = new JButton("Cập nhật phân công");
        styleUpdateButton(update);
        update.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0 || selectedHangMucId[0] < 0 || selectedNhanSuId[0] < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng phân công để cập nhật.");
                return;
            }

            try {
                IdNameItem suKienItem = (IdNameItem) cbSuKien.getSelectedItem();
                IdNameItem hangMucItem = (IdNameItem) cbHangMuc.getSelectedItem();
                IdNameItem nhanSuItem = (IdNameItem) cbNhanSu.getSelectedItem();
                if (suKienItem == null || hangMucItem == null || nhanSuItem == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ sự kiện, hạng mục và nhân sự.");
                    return;
                }

                if (existsAssignment(model, hangMucItem.id, nhanSuItem.id, selectedRow)) {
                    JOptionPane.showMessageDialog(this, "Phân công đích đã tồn tại, không thể cập nhật trùng.");
                    return;
                }

                phanCongNhanSuService.deleteByHangMucAndNhanSu(selectedHangMucId[0], selectedNhanSuId[0]);
                phanCongNhanSuService.assignNhanSuToHangMuc(
                        suKienItem.id,
                        hangMucItem.id,
                        nhanSuItem.id,
                        nhiemVu.getText()
                );

                JOptionPane.showMessageDialog(this, "Cập nhật phân công thành công.");
                resetForm.run();
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cập nhật phân công thất bại: " + ex.getMessage());
            }
        });

        JButton delete = new JButton("Xóa dòng chọn");
        styleDeleteButton(delete);
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa.");
                return;
            }
            try {
                int hangMucId = (int) model.getValueAt(row, 2);
                int staffId = (int) model.getValueAt(row, 4);
                phanCongNhanSuService.deleteByHangMucAndNhanSu(hangMucId, staffId);
                JOptionPane.showMessageDialog(this, "Xóa phân công thành công.");
                resetForm.run();
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Xóa phân công thất bại: " + ex.getMessage());
            }
        });

        JButton reset = new JButton("Làm mới");
        reset.putClientProperty(FlatClientProperties.STYLE,
                "arc:10;" +
                        "background:#F3F4F6;" +
                        "foreground:#111111;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
        reset.addActionListener(e -> resetForm.run());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    int maSK = (int) model.getValueAt(row, 0);
                    int maHM = (int) model.getValueAt(row, 2);
                    int maNS = (int) model.getValueAt(row, 4);
                    selectedHangMucId[0] = maHM;
                    selectedNhanSuId[0] = maNS;
                    selectComboById(cbSuKien, maSK);
                    loadHangMucByEvent.run();
                    selectComboById(cbHangMuc, maHM);
                    selectComboById(cbNhanSu, maNS);
                    Object task = model.getValueAt(row, 6);
                    nhiemVu.setText(task == null ? "" : task.toString());
                }
            }
        });

        JComponent panel = wrapCrud(table, load,
                new String[]{"Sự kiện", "Hạng mục thuộc sự kiện", "Nhân sự", "Nhiệm vụ"},
                new JComponent[]{cbSuKien, cbHangMuc, cbNhanSu, nhiemVu}, assign, delete, update, reset);

        panel.addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && panel.isShowing()) {
                load.run();
            }
        });
        return panel;
    }

    private JComponent createThietBiTab() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã TB", "Tên thiết bị", "Số lượng", "Tình trạng", "Đối tác"}, 0);
        JTable table = new JTable(model);
        JTextField ten = new JTextField();
        JTextField soLuong = new JTextField();
        JTextField tinhTrang = new JTextField();
        JComboBox<IdNameItem> cbDoiTac = new JComboBox<>();
        final int[] selectedId = {-1};

        Runnable loadDoiTacOptions = () -> {
            Object old = cbDoiTac.getSelectedItem();
            cbDoiTac.removeAllItems();
            for (DoiTac doiTac : doiTacService.findAll()) {
                cbDoiTac.addItem(new IdNameItem(doiTac.getMaDT(), doiTac.getTenDonVi()));
            }
            if (old instanceof IdNameItem oldItem) {
                selectComboById(cbDoiTac, oldItem.id);
            }
            if (cbDoiTac.getSelectedItem() == null && cbDoiTac.getItemCount() > 0) {
                cbDoiTac.setSelectedIndex(0);
            }
        };

        Runnable resetForm = () -> {
            selectedId[0] = -1;
            ten.setText("");
            soLuong.setText("");
            tinhTrang.setText("");
            table.clearSelection();
            if (cbDoiTac.getItemCount() > 0) {
                cbDoiTac.setSelectedIndex(0);
            }
            ten.requestFocusInWindow();
        };

        Runnable load = () -> {
            model.setRowCount(0);
            for (ThietBi item : thietBiService.findAll()) {
                String doiTacName = item.getDoiTac() != null ? item.getDoiTac().getTenDonVi() : "N/A";
                model.addRow(new Object[]{item.getMaTB(), item.getTenTB(), item.getSoLuong(), item.getTinhTrang(), doiTacName});
            }
            loadDoiTacOptions.run();
        };

        JButton add = new JButton("Thêm");
        styleAddButton(add);
        add.addActionListener(e -> {
            try {
                IdNameItem doiTacItem = (IdNameItem) cbDoiTac.getSelectedItem();
                if (doiTacItem == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn đối tác.");
                    return;
                }
                int soLuongInt = Integer.parseInt(soLuong.getText().trim());
                if (soLuongInt < 0) {
                    JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn hoặc bằng 0.");
                    return;
                }

                DoiTac doiTac = doiTacService.findById(doiTacItem.id);
                if (doiTac == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy đối tác");
                    return;
                }
                ThietBi item = new ThietBi(ten.getText().trim(), soLuongInt, tinhTrang.getText().trim(), doiTac);
                thietBiService.save(item);
                JOptionPane.showMessageDialog(this, "Thêm thiết bị thành công.");
                resetForm.run();
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Thêm thiết bị thất bại: " + ex.getMessage());
            }
        });

        JButton delete = new JButton("Xóa dòng chọn");
        styleDeleteButton(delete);
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa.");
                return;
            }
            try {
                thietBiService.delete((int) model.getValueAt(row, 0));
                JOptionPane.showMessageDialog(this, "Xóa thiết bị thành công.");
                resetForm.run();
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Xóa thiết bị thất bại: " + ex.getMessage());
            }
        });

        JButton update = new JButton("Cập nhật dòng chọn");
        styleUpdateButton(update);
        update.addActionListener(e -> {
            if (selectedId[0] < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn thiết bị để cập nhật.");
                return;
            }
            try {
                ThietBi item = thietBiService.findById(selectedId[0]);
                IdNameItem doiTacItem = (IdNameItem) cbDoiTac.getSelectedItem();
                if (doiTacItem == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn đối tác.");
                    return;
                }
                int soLuongInt = Integer.parseInt(soLuong.getText().trim());
                if (soLuongInt < 0) {
                    JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn hoặc bằng 0.");
                    return;
                }

                DoiTac doiTac = doiTacService.findById(doiTacItem.id);
                if (item == null || doiTac == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy thiết bị hoặc đối tác.");
                    return;
                }
                item.setTenTB(ten.getText().trim());
                item.setSoLuong(soLuongInt);
                item.setTinhTrang(tinhTrang.getText().trim());
                item.setDoiTac(doiTac);
                thietBiService.update(item);
                JOptionPane.showMessageDialog(this, "Cập nhật thiết bị thành công.");
                resetForm.run();
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cập nhật thiết bị thất bại: " + ex.getMessage());
            }
        });

        JButton reset = new JButton("Làm mới");
        reset.putClientProperty(FlatClientProperties.STYLE,
                "arc:10;" +
                        "background:#F3F4F6;" +
                        "foreground:#111111;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
        reset.addActionListener(e -> resetForm.run());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    selectedId[0] = (int) model.getValueAt(row, 0);
                    ten.setText(String.valueOf(model.getValueAt(row, 1)));
                    soLuong.setText(String.valueOf(model.getValueAt(row, 2)));
                    tinhTrang.setText(String.valueOf(model.getValueAt(row, 3)));
                    ThietBi selected = thietBiService.findById(selectedId[0]);
                    if (selected != null && selected.getDoiTac() != null) {
                        selectComboById(cbDoiTac, selected.getDoiTac().getMaDT());
                    }
                }
            }
        });

        return wrapCrud(table, load,
                new String[]{"Tên thiết bị", "Số lượng", "Tình trạng", "Đối tác"},
                new JComponent[]{ten, soLuong, tinhTrang, cbDoiTac}, add, delete, update, reset);
    }

    private JComponent createDaoCuTab() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã đạo cụ", "Tên đạo cụ", "Số lượng", "Trạng thái"}, 0);
        JTable table = new JTable(model);
        JTextField ten = new JTextField();
        JTextField soLuong = new JTextField();
        JTextField trangThai = new JTextField();
        final int[] selectedId = {-1};

        Runnable resetForm = () -> {
            selectedId[0] = -1;
            ten.setText("");
            soLuong.setText("");
            trangThai.setText("");
            table.clearSelection();
            ten.requestFocusInWindow();
        };

        Runnable load = () -> {
            model.setRowCount(0);
            for (DaoCu item : daoCuService.findAll()) {
                model.addRow(new Object[]{item.getMaDaoCu(), item.getTenDaoCu(), item.getSoLuong(), item.getTrangThai()});
            }
        };

        JButton add = new JButton("Thêm");
        styleAddButton(add);
        add.addActionListener(e -> {
            try {
                daoCuService.save(new DaoCu(ten.getText().trim(), Integer.parseInt(soLuong.getText().trim()), trangThai.getText().trim()));
                resetForm.run();
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Thêm đạo cụ thất bại: " + ex.getMessage());
            }
        });

        JButton delete = new JButton("Xóa dòng chọn");
        styleDeleteButton(delete);
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                daoCuService.delete((int) model.getValueAt(row, 0));
                load.run();
            }
        });

        JButton update = new JButton("Cập nhật dòng chọn");
        styleUpdateButton(update);
        update.addActionListener(e -> {
            if (selectedId[0] < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đạo cụ để cập nhật.");
                return;
            }
            try {
                DaoCu item = daoCuService.findById(selectedId[0]);
                if (item == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy đạo cụ.");
                    return;
                }
                item.setTenDaoCu(ten.getText().trim());
                item.setSoLuong(Integer.parseInt(soLuong.getText().trim()));
                item.setTrangThai(trangThai.getText().trim());
                daoCuService.update(item);
                resetForm.run();
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cập nhật đạo cụ thất bại: " + ex.getMessage());
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    selectedId[0] = (int) model.getValueAt(row, 0);
                    ten.setText(String.valueOf(model.getValueAt(row, 1)));
                    soLuong.setText(String.valueOf(model.getValueAt(row, 2)));
                    trangThai.setText(String.valueOf(model.getValueAt(row, 3)));
                }
            }
        });

        JButton reset = new JButton("Làm mới");
        reset.putClientProperty(FlatClientProperties.STYLE,
                "arc:10;" +
                        "background:#F3F4F6;" +
                        "foreground:#111111;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
        reset.addActionListener(e -> resetForm.run());

        return wrapCrud(table, load,
                new String[]{"Tên đạo cụ", "Số lượng", "Trạng thái"},
                new JComponent[]{ten, soLuong, trangThai}, add, delete, update, reset);
    }

    private JComponent createHieuUngTab() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã hiệu ứng", "Tên hiệu ứng"}, 0);
        JTable table = new JTable(model);
        JTextField ten = new JTextField();
        final int[] selectedId = {-1};

        Runnable resetForm = () -> {
            selectedId[0] = -1;
            ten.setText("");
            table.clearSelection();
            ten.requestFocusInWindow();
        };

        Runnable load = () -> {
            model.setRowCount(0);
            for (HieuUng item : hieuUngService.findAll()) {
                model.addRow(new Object[]{item.getMaHU(), item.getTenHU()});
            }
        };

        JButton add = new JButton("Thêm");
        styleAddButton(add);
        add.addActionListener(e -> {
            try {
                hieuUngService.save(new HieuUng(ten.getText().trim()));
                resetForm.run();
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Thêm hiệu ứng thất bại: " + ex.getMessage());
            }
        });

        JButton delete = new JButton("Xóa dòng chọn");
        styleDeleteButton(delete);
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                hieuUngService.delete((int) model.getValueAt(row, 0));
                load.run();
            }
        });

        JButton update = new JButton("Cập nhật dòng chọn");
        styleUpdateButton(update);
        update.addActionListener(e -> {
            if (selectedId[0] < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hiệu ứng để cập nhật.");
                return;
            }
            try {
                HieuUng item = hieuUngService.findById(selectedId[0]);
                if (item == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy hiệu ứng.");
                    return;
                }
                item.setTenHU(ten.getText().trim());
                hieuUngService.update(item);
                resetForm.run();
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cập nhật hiệu ứng thất bại: " + ex.getMessage());
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    selectedId[0] = (int) model.getValueAt(row, 0);
                    ten.setText(String.valueOf(model.getValueAt(row, 1)));
                }
            }
        });

        JButton reset = new JButton("Làm mới");
        reset.putClientProperty(FlatClientProperties.STYLE,
                "arc:10;" +
                        "background:#F3F4F6;" +
                        "foreground:#111111;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
        reset.addActionListener(e -> resetForm.run());

        return wrapCrud(table, load, new String[]{"Tên hiệu ứng"}, new JComponent[]{ten}, add, delete, update, reset);
    }

    private JComponent createDanhSachNhacTab() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã bài hát", "Tên bài hát", "Ca sĩ", "Thời lượng (phút)", "File nhạc", "Hạng mục"}, 0);
        JTable table = new JTable(model);
        JTextField ten = new JTextField();
        JTextField caSi = new JTextField();
        JTextField thoiLuong = new JTextField();
        JTextField fileNhac = new JTextField();
        JButton chooseFile = new JButton("Chọn file nhạc");
        JButton downloadFile = new JButton("Tải file");
        JComboBox<IdNameItem> cbHangMuc = new JComboBox<>();
        final int[] selectedId = {-1};
        final byte[][] selectedFileBytes = {null};
        final String[] selectedFileName = {""};
        final String[] selectedMimeType = {""};

        fileNhac.setEditable(false);
        fileNhac.setToolTipText("Chọn file .mp3 hoặc .mp4 từ máy để lưu vào cơ sở dữ liệu");

        JPanel fileNhacPanel = new JPanel(new BorderLayout(8, 0));
        fileNhacPanel.setOpaque(false);
        fileNhacPanel.add(fileNhac, BorderLayout.CENTER);
        fileNhacPanel.add(chooseFile, BorderLayout.EAST);

        Runnable loadHangMucOptions = () -> {
            Object old = cbHangMuc.getSelectedItem();
            cbHangMuc.removeAllItems();
            for (HangMucKichBan hm : hangMucService.findAll()) {
                String name = hm.getTenHM();
                if (hm.getSuKienTiec() != null) {
                    name += " - " + hm.getSuKienTiec().getTenSuKien();
                }
                cbHangMuc.addItem(new IdNameItem(hm.getMaHM(), name));
            }
            if (old instanceof IdNameItem oldItem) {
                selectComboById(cbHangMuc, oldItem.id);
            }
            if (cbHangMuc.getSelectedItem() == null && cbHangMuc.getItemCount() > 0) {
                cbHangMuc.setSelectedIndex(0);
            }
        };

        Runnable resetForm = () -> {
            selectedId[0] = -1;
            selectedFileBytes[0] = null;
            selectedFileName[0] = "";
            selectedMimeType[0] = "";
            ten.setText("");
            caSi.setText("");
            thoiLuong.setText("");
            fileNhac.setText("");
            table.clearSelection();
            if (cbHangMuc.getItemCount() > 0) {
                cbHangMuc.setSelectedIndex(0);
            }
            ten.requestFocusInWindow();
        };

        Runnable load = () -> {
            model.setRowCount(0);
            for (DanhSachNhac item : danhSachNhacService.findAll()) {
                String hangMucName = item.getHangMuc() != null ? item.getHangMuc().getTenHM() : "N/A";
                model.addRow(new Object[]{
                        item.getMaBaiHat(),
                        item.getTenBaiHat(),
                        item.getCaSi(),
                        item.getThoiLuong(),
                        getDisplayMusicFileName(item),
                        hangMucName
                });
            }
            loadHangMucOptions.run();
        };

        chooseFile.putClientProperty(FlatClientProperties.STYLE,
                "arc:10;" +
                        "background:#111827;" +
                        "foreground:#FFFFFF;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
        downloadFile.putClientProperty(FlatClientProperties.STYLE,
                "arc:10;" +
                        "background:#374151;" +
                        "foreground:#FFFFFF;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
        chooseFile.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Chọn file nhạc");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setFileFilter(new FileNameExtensionFilter("Audio/Video (*.mp3, *.mp4)", "mp3", "mp4"));
            String currentPath = fileNhac.getText().trim();
            if (!currentPath.isEmpty()) {
                File current = new File(currentPath);
                if (current.exists()) {
                    chooser.setSelectedFile(current);
                }
            }
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile() != null) {
                try {
                    File selected = chooser.getSelectedFile();
                    if (!isSupportedMediaFile(selected.getName())) {
                        JOptionPane.showMessageDialog(this, "Chỉ hỗ trợ file .mp3 hoặc .mp4.");
                        return;
                    }

                    selectedFileBytes[0] = Files.readAllBytes(selected.toPath());
                    selectedFileName[0] = selected.getName();
                    selectedMimeType[0] = selected.getName().toLowerCase().endsWith(".mp3")
                            ? "audio/mpeg"
                            : "video/mp4";
                    fileNhac.setText(selectedFileName[0]);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Không thể đọc file đã chọn: " + ex.getMessage());
                }
            }
        });

        downloadFile.addActionListener(e -> {
            if (selectedId[0] < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bài hát cần tải file.");
                return;
            }

            try {
                DanhSachNhac item = danhSachNhacService.findById(selectedId[0]);
                if (item == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy bài hát.");
                    return;
                }

                byte[] data = item.getNoiDungFile();
                if (data == null || data.length == 0) {
                    JOptionPane.showMessageDialog(this,
                            "Bản ghi này chưa lưu nội dung file trong cơ sở dữ liệu. Vui lòng chọn lại file và cập nhật.");
                    return;
                }

                String defaultName = buildDownloadFileName(item);
                JFileChooser saveChooser = new JFileChooser();
                saveChooser.setDialogTitle("Chọn nơi lưu file nhạc");
                saveChooser.setSelectedFile(new File(defaultName));
                saveChooser.setFileFilter(new FileNameExtensionFilter("Audio/Video (*.mp3, *.mp4)", "mp3", "mp4"));
                int result = saveChooser.showSaveDialog(this);
                if (result != JFileChooser.APPROVE_OPTION || saveChooser.getSelectedFile() == null) {
                    return;
                }

                File outFile = saveChooser.getSelectedFile();
                if (!isSupportedMediaFile(outFile.getName())) {
                    JOptionPane.showMessageDialog(this, "Tên file lưu phải có đuôi .mp3 hoặc .mp4.");
                    return;
                }

                if (outFile.exists()) {
                    int overwrite = JOptionPane.showConfirmDialog(
                            this,
                            "File đã tồn tại. Bạn có muốn ghi đè không?",
                            "Xác nhận ghi đè",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (overwrite != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                Files.write(outFile.toPath(), data);
                JOptionPane.showMessageDialog(this, "Tải file thành công: " + outFile.getAbsolutePath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Tải file thất bại: " + extractRootMessage(ex));
            }
        });

        JButton add = new JButton("Thêm");
        styleAddButton(add);
        add.addActionListener(e -> {
            try {
                IdNameItem hangMucItem = (IdNameItem) cbHangMuc.getSelectedItem();
                if (hangMucItem == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn hạng mục.");
                    return;
                }
                int duration = Integer.parseInt(thoiLuong.getText().trim());
                if (duration <= 0) {
                    JOptionPane.showMessageDialog(this, "Thời lượng phải lớn hơn 0 phút.");
                    return;
                }

                HangMucKichBan hm = new HangMucKichBan();
                hm.setMaHM(hangMucItem.id);
                if (selectedFileBytes[0] == null || selectedFileBytes[0].length == 0) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn file nhạc .mp3 hoặc .mp4 để lưu.");
                    return;
                }
                DanhSachNhac item = new DanhSachNhac(
                        ten.getText().trim(),
                        caSi.getText().trim(),
                        duration,
                        selectedFileName[0],
                        hm
                );
                item.setTenFileGoc(selectedFileName[0]);
                item.setLoaiFile(selectedMimeType[0]);
                item.setNoiDungFile(selectedFileBytes[0]);
                danhSachNhacService.save(item);
                JOptionPane.showMessageDialog(this, "Thêm bài hát thành công.");
                resetForm.run();
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Thêm bài hát thất bại: " + extractRootMessage(ex));
            }
        });

        JButton delete = new JButton("Xóa dòng chọn");
        styleDeleteButton(delete);
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa.");
                return;
            }
            try {
                danhSachNhacService.delete((int) model.getValueAt(row, 0));
                JOptionPane.showMessageDialog(this, "Xóa bài hát thành công.");
                resetForm.run();
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Xóa bài hát thất bại: " + ex.getMessage());
            }
        });

        JButton update = new JButton("Cập nhật dòng chọn");
        styleUpdateButton(update);
        update.addActionListener(e -> {
            if (selectedId[0] < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bài hát để cập nhật.");
                return;
            }
            try {
                DanhSachNhac item = danhSachNhacService.findById(selectedId[0]);
                IdNameItem hangMucItem = (IdNameItem) cbHangMuc.getSelectedItem();
                if (hangMucItem == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn hạng mục.");
                    return;
                }
                int duration = Integer.parseInt(thoiLuong.getText().trim());
                if (duration <= 0) {
                    JOptionPane.showMessageDialog(this, "Thời lượng phải lớn hơn 0 phút.");
                    return;
                }

                if (item == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy bài hát.");
                    return;
                }
                HangMucKichBan hm = new HangMucKichBan();
                hm.setMaHM(hangMucItem.id);
                item.setTenBaiHat(ten.getText().trim());
                item.setCaSi(caSi.getText().trim());
                item.setThoiLuong(duration);
                if (selectedFileBytes[0] != null && selectedFileBytes[0].length > 0) {
                    item.setFileNhac(selectedFileName[0]);
                    item.setTenFileGoc(selectedFileName[0]);
                    item.setLoaiFile(selectedMimeType[0]);
                    item.setNoiDungFile(selectedFileBytes[0]);
                }
                item.setHangMuc(hm);
                danhSachNhacService.update(item);
                JOptionPane.showMessageDialog(this, "Cập nhật bài hát thành công.");
                resetForm.run();
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cập nhật bài hát thất bại: " + extractRootMessage(ex));
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    selectedId[0] = (int) model.getValueAt(row, 0);
                    selectedFileBytes[0] = null;
                    selectedFileName[0] = "";
                    selectedMimeType[0] = "";
                    ten.setText(String.valueOf(model.getValueAt(row, 1)));
                    caSi.setText(String.valueOf(model.getValueAt(row, 2)));
                    thoiLuong.setText(String.valueOf(model.getValueAt(row, 3)));
                    fileNhac.setText(String.valueOf(model.getValueAt(row, 4)));
                    DanhSachNhac selected = danhSachNhacService.findById(selectedId[0]);
                    if (selected != null && selected.getHangMuc() != null) {
                        selectComboById(cbHangMuc, selected.getHangMuc().getMaHM());
                    }
                }
            }
        });

        JButton reset = new JButton("Làm mới");
        reset.putClientProperty(FlatClientProperties.STYLE,
                "arc:10;" +
                        "background:#F3F4F6;" +
                        "foreground:#111111;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
        reset.addActionListener(e -> resetForm.run());

        return wrapCrud(table, load,
                new String[]{"Tên bài hát", "Ca sĩ", "Thời lượng (phút)", "File nhạc", "Hạng mục"},
                new JComponent[]{ten, caSi, thoiLuong, fileNhacPanel, cbHangMuc}, add, delete, update, downloadFile, reset);
    }

    private JComponent createLichTongDuyetTab() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã tổng duyệt", "Thời gian duyệt", "Nội dung duyệt", "Trạng thái", "Sự kiện"}, 0);
        JTable table = new JTable(model);
        DateTimePicker thoiGian = new DateTimePicker();
        JTextField noiDung = new JTextField();
        JComboBox<String> cbTrangThai = new JComboBox<>(new String[]{"Chưa duyệt", "Đang duyệt", "Hoàn thành"});
        JComboBox<IdNameItem> cbSuKien = new JComboBox<>();
        final int[] selectedId = {-1};

        Runnable loadSuKienOptions = () -> {
            Object old = cbSuKien.getSelectedItem();
            cbSuKien.removeAllItems();
            for (SuKienTiec sk : suKienService.findAll()) {
                cbSuKien.addItem(new IdNameItem(sk.getMaSK(), sk.getTenSuKien()));
            }
            if (old instanceof IdNameItem oldItem) {
                selectComboById(cbSuKien, oldItem.id);
            }
            if (cbSuKien.getSelectedItem() == null && cbSuKien.getItemCount() > 0) {
                cbSuKien.setSelectedIndex(0);
            }
        };

        Runnable resetForm = () -> {
            selectedId[0] = -1;
            thoiGian.setDateTimePermissive(LocalDateTime.now());
            noiDung.setText("");
            cbTrangThai.setSelectedIndex(0);
            table.clearSelection();
            if (cbSuKien.getItemCount() > 0) {
                cbSuKien.setSelectedIndex(0);
            }
            noiDung.requestFocusInWindow();
        };

        Runnable load = () -> {
            model.setRowCount(0);
            for (LichTongDuyet item : lichTongDuyetService.findAll()) {
                String suKienName = item.getSuKienTiec() != null ? item.getSuKienTiec().getTenSuKien() : "N/A";
                model.addRow(new Object[]{item.getMaTongDuyet(), item.getThoiGianDuyet(), item.getNoiDungDuyet(), item.getTrangThai(), suKienName});
            }
            loadSuKienOptions.run();
        };

        JButton add = new JButton("Thêm");
        styleAddButton(add);
        add.addActionListener(e -> {
            try {
                IdNameItem suKienItem = (IdNameItem) cbSuKien.getSelectedItem();
                if (suKienItem == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn sự kiện.");
                    return;
                }
                LocalDateTime tgDuyet = thoiGian.getDateTimeStrict();
                if (tgDuyet == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn thời gian tổng duyệt hợp lệ.");
                    return;
                }

                SuKienTiec sk = suKienService.findById(suKienItem.id);
                if (sk == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sự kiện");
                    return;
                }
                LichTongDuyet item = new LichTongDuyet(
                        tgDuyet,
                        noiDung.getText().trim(),
                        String.valueOf(cbTrangThai.getSelectedItem()),
                        sk
                );
                lichTongDuyetService.save(item);
                JOptionPane.showMessageDialog(this, "Thêm lịch tổng duyệt thành công.");
                resetForm.run();
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Thêm lịch tổng duyệt thất bại: " + ex.getMessage());
            }
        });

        JButton delete = new JButton("Xóa dòng chọn");
        styleDeleteButton(delete);
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa.");
                return;
            }
            try {
                lichTongDuyetService.delete((int) model.getValueAt(row, 0));
                JOptionPane.showMessageDialog(this, "Xóa lịch tổng duyệt thành công.");
                resetForm.run();
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Xóa lịch tổng duyệt thất bại: " + ex.getMessage());
            }
        });

        JButton update = new JButton("Cập nhật dòng chọn");
        styleUpdateButton(update);
        update.addActionListener(e -> {
            if (selectedId[0] < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn lịch tổng duyệt để cập nhật.");
                return;
            }
            try {
                LichTongDuyet item = lichTongDuyetService.findById(selectedId[0]);
                IdNameItem suKienItem = (IdNameItem) cbSuKien.getSelectedItem();
                if (suKienItem == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn sự kiện.");
                    return;
                }
                LocalDateTime tgDuyet = thoiGian.getDateTimeStrict();
                if (tgDuyet == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn thời gian tổng duyệt hợp lệ.");
                    return;
                }

                SuKienTiec sk = suKienService.findById(suKienItem.id);
                if (item == null || sk == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy lịch tổng duyệt hoặc sự kiện.");
                    return;
                }
                item.setThoiGianDuyet(tgDuyet);
                item.setNoiDungDuyet(noiDung.getText().trim());
                item.setTrangThai(String.valueOf(cbTrangThai.getSelectedItem()));
                item.setSuKienTiec(sk);
                lichTongDuyetService.update(item);
                JOptionPane.showMessageDialog(this, "Cập nhật lịch tổng duyệt thành công.");
                resetForm.run();
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cập nhật lịch tổng duyệt thất bại: " + ex.getMessage());
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    selectedId[0] = (int) model.getValueAt(row, 0);
                    LichTongDuyet selected = lichTongDuyetService.findById(selectedId[0]);
                    if (selected != null && selected.getThoiGianDuyet() != null) {
                        thoiGian.setDateTimePermissive(selected.getThoiGianDuyet());
                    }
                    noiDung.setText(String.valueOf(model.getValueAt(row, 2)));
                    String trangThai = String.valueOf(model.getValueAt(row, 3));
                    cbTrangThai.setSelectedItem(trangThai);
                    if (selected != null && selected.getSuKienTiec() != null) {
                        selectComboById(cbSuKien, selected.getSuKienTiec().getMaSK());
                    }
                }
            }
        });

        JButton reset = new JButton("Làm mới");
        reset.putClientProperty(FlatClientProperties.STYLE,
                "arc:10;" +
                        "background:#F3F4F6;" +
                        "foreground:#111111;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
        reset.addActionListener(e -> resetForm.run());

        return wrapCrud(table, load,
                new String[]{"Thời gian tổng duyệt", "Nội dung duyệt", "Trạng thái", "Sự kiện"},
                new JComponent[]{thoiGian, noiDung, cbTrangThai, cbSuKien}, add, delete, update, reset);
    }

    private JComponent createSuDungDaoCuTab() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã HM", "Hạng mục", "Mã ĐC", "Đạo cụ", "Số lượng sử dụng"}, 0);
        JTable table = new JTable(model);
        JComboBox<IdNameItem> cbHangMuc = new JComboBox<>();
        JComboBox<IdNameItem> cbDaoCu = new JComboBox<>();
        JTextField soLuong = new JTextField("1");

        Runnable loadOptions = () -> {
            Object oldHM = cbHangMuc.getSelectedItem();
            Object oldDC = cbDaoCu.getSelectedItem();

            cbHangMuc.removeAllItems();
            for (HangMucKichBan hm : hangMucService.findAll()) {
                String name = hm.getTenHM();
                if (hm.getSuKienTiec() != null) {
                    name += " - " + hm.getSuKienTiec().getTenSuKien();
                }
                cbHangMuc.addItem(new IdNameItem(hm.getMaHM(), name));
            }

            cbDaoCu.removeAllItems();
            for (DaoCu dc : daoCuService.findAll()) {
                cbDaoCu.addItem(new IdNameItem(dc.getMaDaoCu(), dc.getTenDaoCu()));
            }

            if (oldHM instanceof IdNameItem hmItem) {
                selectComboById(cbHangMuc, hmItem.id);
            }
            if (oldDC instanceof IdNameItem dcItem) {
                selectComboById(cbDaoCu, dcItem.id);
            }
            if (cbHangMuc.getSelectedItem() == null && cbHangMuc.getItemCount() > 0) {
                cbHangMuc.setSelectedIndex(0);
            }
            if (cbDaoCu.getSelectedItem() == null && cbDaoCu.getItemCount() > 0) {
                cbDaoCu.setSelectedIndex(0);
            }
        };

        Runnable resetForm = () -> {
            table.clearSelection();
            soLuong.setText("1");
            if (cbHangMuc.getItemCount() > 0) {
                cbHangMuc.setSelectedIndex(0);
            }
            if (cbDaoCu.getItemCount() > 0) {
                cbDaoCu.setSelectedIndex(0);
            }
            soLuong.requestFocusInWindow();
        };

        Runnable load = () -> {
            model.setRowCount(0);
            for (SuDungDaoCu item : suDungDaoCuService.findAll()) {
                int maHM = item.getId() != null ? item.getId().getMaHM() : 0;
                int maDaoCu = item.getId() != null ? item.getId().getMaDaoCu() : 0;
                String hm = item.getHangMuc() != null ? item.getHangMuc().getTenHM() : "N/A";
                String dc = item.getDaoCu() != null ? item.getDaoCu().getTenDaoCu() : "N/A";
                model.addRow(new Object[]{maHM, hm, maDaoCu, dc, item.getSoLuongSuDung()});
            }
            loadOptions.run();
        };

        JButton addOrUpdate = new JButton("Lưu/Cập nhật");
        styleUpdateButton(addOrUpdate);
        addOrUpdate.addActionListener(e -> {
            try {
                IdNameItem hangMucItem = (IdNameItem) cbHangMuc.getSelectedItem();
                IdNameItem daoCuItem = (IdNameItem) cbDaoCu.getSelectedItem();
                if (hangMucItem == null || daoCuItem == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn hạng mục và đạo cụ.");
                    return;
                }
                int hangMucId = hangMucItem.id;
                int daoCuId = daoCuItem.id;
                int soLuongInt = Integer.parseInt(soLuong.getText().trim());
                if (soLuongInt <= 0) {
                    JOptionPane.showMessageDialog(this, "Số lượng sử dụng phải lớn hơn 0.");
                    return;
                }

                HangMucKichBan hm = hangMucService.findById(hangMucId);
                DaoCu dc = daoCuService.findById(daoCuId);
                if (hm == null || dc == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy hạng mục hoặc đạo cụ");
                    return;
                }
                SuDungDaoCuId id = new SuDungDaoCuId(hangMucId, daoCuId);
                SuDungDaoCu existing = suDungDaoCuService.findById(id);
                if (existing == null) {
                    SuDungDaoCu item = new SuDungDaoCu();
                    item.setHangMuc(hm);
                    item.setDaoCu(dc);
                    item.setSoLuongSuDung(soLuongInt);
                    suDungDaoCuService.save(item);
                } else {
                    existing.setSoLuongSuDung(soLuongInt);
                    suDungDaoCuService.update(existing);
                }
                JOptionPane.showMessageDialog(this, "Lưu sử dụng đạo cụ thành công.");
                resetForm.run();
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lưu sử dụng đạo cụ thất bại: " + ex.getMessage());
            }
        });

        JButton delete = new JButton("Xóa dòng chọn");
        styleDeleteButton(delete);
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa.");
                return;
            }
            try {
                int hangMucId = (int) model.getValueAt(row, 0);
                int daoCuId = (int) model.getValueAt(row, 2);
                suDungDaoCuService.delete(new SuDungDaoCuId(hangMucId, daoCuId));
                JOptionPane.showMessageDialog(this, "Xóa sử dụng đạo cụ thành công.");
                resetForm.run();
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Xóa sử dụng đạo cụ thất bại: " + ex.getMessage());
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    int maHM = (int) model.getValueAt(row, 0);
                    int maDaoCu = (int) model.getValueAt(row, 2);
                    selectComboById(cbHangMuc, maHM);
                    selectComboById(cbDaoCu, maDaoCu);
                    soLuong.setText(String.valueOf(model.getValueAt(row, 4)));
                }
            }
        });

        JButton reset = new JButton("Làm mới");
        reset.putClientProperty(FlatClientProperties.STYLE,
                "arc:10;" +
                        "background:#F3F4F6;" +
                        "foreground:#111111;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
        reset.addActionListener(e -> resetForm.run());

        return wrapCrud(table, load,
                new String[]{"Hạng mục", "Đạo cụ", "Số lượng sử dụng"},
                new JComponent[]{cbHangMuc, cbDaoCu, soLuong}, addOrUpdate, delete, reset);
    }

    private JComponent wrapCrud(
            JTable table,
            Runnable load,
            String[] labels,
            JComponent[] fields,
            JButton add,
            JButton delete
    ) {
        return wrapCrud(table, load, labels, fields, add, delete, new JButton[0]);
    }

    private JComponent wrapCrud(
            JTable table,
            Runnable load,
            String[] labels,
            JComponent[] fields,
            JButton add,
            JButton delete,
            JButton... extras
    ) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_SOFT);

        JPanel form = new JPanel(new MigLayout("wrap 2,insets 8", "[180][grow]", "[]"));
        form.setBackground(CARD_BG);
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i] + ":");
            label.setForeground(TEXT_DARK);
            if (fields[i] instanceof JTextComponent textComponent) {
                textComponent.setForeground(INPUT_TEXT);
                textComponent.setCaretColor(INPUT_TEXT);
            }
            form.add(label);
            form.add(fields[i], "growx");
        }

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setBackground(CARD_BG);
        buttons.add(add);
        buttons.add(delete);
        for (JButton extra : extras) {
            if (extra != null) {
                buttons.add(extra);
            }
        }

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(CARD_BG);
        top.putClientProperty(FlatClientProperties.STYLE,
                "arc:16;" +
                        "border:1,1,1,1,#D1D5DB");
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);

        table.setRowHeight(30);
        table.setSelectionBackground(new Color(229, 231, 235));
        table.setSelectionForeground(TEXT_DARK);
        table.getTableHeader().setBackground(new Color(243, 244, 246));
        table.getTableHeader().setForeground(TEXT_DARK);
        table.setGridColor(new Color(229, 231, 235));

        panel.add(top, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(209, 213, 219)));
        panel.add(scrollPane, BorderLayout.CENTER);

        load.run();
        return panel;
    }

    private void selectComboById(JComboBox<IdNameItem> comboBox, int id) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            IdNameItem item = comboBox.getItemAt(i);
            if (item != null && item.id == id) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
    }

    private boolean existsAssignment(DefaultTableModel model, int maHM, int maNS, int excludeRow) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if (i == excludeRow) {
                continue;
            }
            int existingHM = (int) model.getValueAt(i, 2);
            int existingNS = (int) model.getValueAt(i, 4);
            if (existingHM == maHM && existingNS == maNS) {
                return true;
            }
        }
        return false;
    }

    private String getDisplayMusicFileName(DanhSachNhac item) {
        if (item.getTenFileGoc() != null && !item.getTenFileGoc().trim().isEmpty()) {
            return item.getTenFileGoc();
        }
        return item.getFileNhac();
    }

    private String buildDownloadFileName(DanhSachNhac item) {
        String name = getDisplayMusicFileName(item);
        if (name == null || name.trim().isEmpty()) {
            return "baihat-" + item.getMaBaiHat() + ".mp3";
        }
        return name;
    }

    private boolean isSupportedMediaFile(String fileName) {
        String normalized = fileName == null ? "" : fileName.toLowerCase();
        return normalized.endsWith(".mp3") || normalized.endsWith(".mp4");
    }

    private String extractRootMessage(Throwable throwable) {
        Throwable root = throwable;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }
        String message = root.getMessage();
        return (message == null || message.trim().isEmpty()) ? throwable.toString() : message;
    }

    private static class IdNameItem {
        private final int id;
        private final String name;

        private IdNameItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name + " (ID: " + id + ")";
        }
    }

    private void styleAddButton(JButton button) {
        button.putClientProperty(FlatClientProperties.STYLE,
                "arc:10;" +
                        "background:#16A34A;" +
                        "foreground:#FFFFFF;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
    }

    private void styleUpdateButton(JButton button) {
        button.putClientProperty(FlatClientProperties.STYLE,
                "arc:10;" +
                        "background:#2563EB;" +
                        "foreground:#FFFFFF;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
    }

    private void styleDeleteButton(JButton button) {
        button.putClientProperty(FlatClientProperties.STYLE,
                "arc:10;" +
                        "background:#DC2626;" +
                        "foreground:#FFFFFF;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
    }
}
