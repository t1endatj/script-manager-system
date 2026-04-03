package scriptmanager.ui.main;

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
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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

        JLabel title = new JLabel("Tài nguyên");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT_DARK);

        JButton back = new JButton("Quay lại Dashboard");
        back.putClientProperty(FlatClientProperties.STYLE,
                "arc:12;" +
                        "background:#F3F4F6;" +
                        "foreground:#111111;" +
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

        JButton reset = new JButton("Làm mới");
        reset.putClientProperty(FlatClientProperties.STYLE,
                "arc:10;" +
                        "background:#F3F4F6;" +
                        "foreground:#111111;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
        reset.addActionListener(e -> {
            ten.setText("");
            linhVuc.setText("");
            sdt.setText("");
            table.clearSelection();
            ten.requestFocusInWindow();
        });

        return wrapCrud(table, load, new String[]{"Tên đơn vị", "Lĩnh vực", "SĐT"}, new JComponent[]{ten, linhVuc, sdt}, add, delete, reset);
    }

    private JComponent createNhanSuTab() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã NS", "Tên nhân sự", "SĐT", "Vai trò"}, 0);
        JTable table = new JTable(model);
        JTextField ten = new JTextField();
        JTextField sdt = new JTextField();
        JTextField vaiTro = new JTextField();

        Runnable load = () -> {
            model.setRowCount(0);
            for (NhanSu item : nhanSuService.findAll()) {
                model.addRow(new Object[]{item.getMaNS(), item.getTenNS(), item.getSdt(), item.getVaiTro()});
            }
        };

        JButton add = new JButton("Thêm");
        styleAddButton(add);
        add.addActionListener(e -> {
            try {
                nhanSuService.save(new NhanSu(ten.getText().trim(), sdt.getText().trim(), vaiTro.getText().trim()));
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Thêm nhân sự thất bại: " + ex.getMessage());
            }
        });

        JButton delete = new JButton("Xóa dòng chọn");
        styleDeleteButton(delete);
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                nhanSuService.delete((int) model.getValueAt(row, 0));
                load.run();
            }
        });

        return wrapCrud(table, load, new String[]{"Tên nhân sự", "SĐT", "Vai trò"}, new JComponent[]{ten, sdt, vaiTro}, add, delete);
    }

    private JComponent createThietBiTab() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã TB", "Tên thiết bị", "Số lượng", "Tình trạng", "Mã đối tác"}, 0);
        JTable table = new JTable(model);
        JTextField ten = new JTextField();
        JTextField soLuong = new JTextField();
        JTextField tinhTrang = new JTextField();
        JTextField maDT = new JTextField();

        Runnable load = () -> {
            model.setRowCount(0);
            for (ThietBi item : thietBiService.findAll()) {
                Integer doiTacId = item.getDoiTac() != null ? item.getDoiTac().getMaDT() : null;
                model.addRow(new Object[]{item.getMaTB(), item.getTenTB(), item.getSoLuong(), item.getTinhTrang(), doiTacId});
            }
        };

        JButton add = new JButton("Thêm");
        styleAddButton(add);
        add.addActionListener(e -> {
            try {
                DoiTac doiTac = doiTacService.findById(Integer.parseInt(maDT.getText().trim()));
                if (doiTac == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy đối tác");
                    return;
                }
                ThietBi item = new ThietBi(ten.getText().trim(), Integer.parseInt(soLuong.getText().trim()), tinhTrang.getText().trim(), doiTac);
                thietBiService.save(item);
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Thêm thiết bị thất bại: " + ex.getMessage());
            }
        });

        JButton delete = new JButton("Xóa dòng chọn");
        styleDeleteButton(delete);
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                thietBiService.delete((int) model.getValueAt(row, 0));
                load.run();
            }
        });

        return wrapCrud(table, load,
                new String[]{"Tên thiết bị", "Số lượng", "Tình trạng", "Mã đối tác"},
                new JComponent[]{ten, soLuong, tinhTrang, maDT}, add, delete);
    }

    private JComponent createDaoCuTab() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã đạo cụ", "Tên đạo cụ", "Số lượng", "Trạng thái"}, 0);
        JTable table = new JTable(model);
        JTextField ten = new JTextField();
        JTextField soLuong = new JTextField();
        JTextField trangThai = new JTextField();

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

        return wrapCrud(table, load,
                new String[]{"Tên đạo cụ", "Số lượng", "Trạng thái"},
                new JComponent[]{ten, soLuong, trangThai}, add, delete);
    }

    private JComponent createHieuUngTab() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã hiệu ứng", "Tên hiệu ứng"}, 0);
        JTable table = new JTable(model);
        JTextField ten = new JTextField();

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

        return wrapCrud(table, load, new String[]{"Tên hiệu ứng"}, new JComponent[]{ten}, add, delete);
    }

    private JComponent createDanhSachNhacTab() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã bài hát", "Tên bài hát", "Ca sĩ", "Thời lượng", "File nhạc", "Mã hạng mục"}, 0);
        JTable table = new JTable(model);
        JTextField ten = new JTextField();
        JTextField caSi = new JTextField();
        JTextField thoiLuong = new JTextField();
        JTextField fileNhac = new JTextField();
        JTextField maHM = new JTextField();

        Runnable load = () -> {
            model.setRowCount(0);
            for (DanhSachNhac item : danhSachNhacService.findAll()) {
                Integer hangMucId = item.getHangMuc() != null ? item.getHangMuc().getMaHM() : null;
                model.addRow(new Object[]{item.getMaBaiHat(), item.getTenBaiHat(), item.getCaSi(), item.getThoiLuong(), item.getFileNhac(), hangMucId});
            }
        };

        JButton add = new JButton("Thêm");
        styleAddButton(add);
        add.addActionListener(e -> {
            try {
                HangMucKichBan hm = hangMucService.findById(Integer.parseInt(maHM.getText().trim()));
                if (hm == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy hạng mục");
                    return;
                }
                DanhSachNhac item = new DanhSachNhac(
                        ten.getText().trim(),
                        caSi.getText().trim(),
                        Integer.parseInt(thoiLuong.getText().trim()),
                        fileNhac.getText().trim(),
                        hm
                );
                danhSachNhacService.save(item);
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Thêm bài hát thất bại: " + ex.getMessage());
            }
        });

        JButton delete = new JButton("Xóa dòng chọn");
        styleDeleteButton(delete);
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                danhSachNhacService.delete((int) model.getValueAt(row, 0));
                load.run();
            }
        });

        return wrapCrud(table, load,
                new String[]{"Tên bài hát", "Ca sĩ", "Thời lượng", "File nhạc", "Mã hạng mục"},
                new JComponent[]{ten, caSi, thoiLuong, fileNhac, maHM}, add, delete);
    }

    private JComponent createLichTongDuyetTab() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã tổng duyệt", "Thời gian duyệt", "Nội dung duyệt", "Trạng thái", "Mã sự kiện"}, 0);
        JTable table = new JTable(model);
        JTextField thoiGian = new JTextField("2026-05-01 18:00");
        JTextField noiDung = new JTextField();
        JTextField trangThai = new JTextField("Chưa duyệt");
        JTextField maSK = new JTextField();

        Runnable load = () -> {
            model.setRowCount(0);
            for (LichTongDuyet item : lichTongDuyetService.findAll()) {
                Integer suKienId = item.getSuKienTiec() != null ? item.getSuKienTiec().getMaSK() : null;
                model.addRow(new Object[]{item.getMaTongDuyet(), item.getThoiGianDuyet(), item.getNoiDungDuyet(), item.getTrangThai(), suKienId});
            }
        };

        JButton add = new JButton("Thêm");
        styleAddButton(add);
        add.addActionListener(e -> {
            try {
                SuKienTiec sk = suKienService.findById(Integer.parseInt(maSK.getText().trim()));
                if (sk == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sự kiện");
                    return;
                }
                LichTongDuyet item = new LichTongDuyet(
                        LocalDateTime.parse(thoiGian.getText().trim(), DATE_TIME_FORMATTER),
                        noiDung.getText().trim(),
                        trangThai.getText().trim(),
                        sk
                );
                lichTongDuyetService.save(item);
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Thêm lịch tổng duyệt thất bại: " + ex.getMessage());
            }
        });

        JButton delete = new JButton("Xóa dòng chọn");
        styleDeleteButton(delete);
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                lichTongDuyetService.delete((int) model.getValueAt(row, 0));
                load.run();
            }
        });

        return wrapCrud(table, load,
                new String[]{"Thời gian (yyyy-MM-dd HH:mm)", "Nội dung duyệt", "Trạng thái", "Mã sự kiện"},
                new JComponent[]{thoiGian, noiDung, trangThai, maSK}, add, delete);
    }

    private JComponent createSuDungDaoCuTab() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã hạng mục", "Mã đạo cụ", "Số lượng sử dụng"}, 0);
        JTable table = new JTable(model);
        JTextField maHM = new JTextField();
        JTextField maDaoCu = new JTextField();
        JTextField soLuong = new JTextField("1");

        Runnable load = () -> {
            model.setRowCount(0);
            for (SuDungDaoCu item : suDungDaoCuService.findAll()) {
                model.addRow(new Object[]{item.getId().getMaHM(), item.getId().getMaDaoCu(), item.getSoLuongSuDung()});
            }
        };

        JButton addOrUpdate = new JButton("Lưu/Cập nhật");
        styleUpdateButton(addOrUpdate);
        addOrUpdate.addActionListener(e -> {
            try {
                int hangMucId = Integer.parseInt(maHM.getText().trim());
                int daoCuId = Integer.parseInt(maDaoCu.getText().trim());
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
                    item.setSoLuongSuDung(Integer.parseInt(soLuong.getText().trim()));
                    suDungDaoCuService.save(item);
                } else {
                    existing.setSoLuongSuDung(Integer.parseInt(soLuong.getText().trim()));
                    suDungDaoCuService.update(existing);
                }
                load.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lưu sử dụng đạo cụ thất bại: " + ex.getMessage());
            }
        });

        JButton delete = new JButton("Xóa dòng chọn");
        styleDeleteButton(delete);
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int hangMucId = (int) model.getValueAt(row, 0);
                int daoCuId = (int) model.getValueAt(row, 1);
                suDungDaoCuService.delete(new SuDungDaoCuId(hangMucId, daoCuId));
                load.run();
            }
        });

        return wrapCrud(table, load,
                new String[]{"Mã hạng mục", "Mã đạo cụ", "Số lượng sử dụng"},
                new JComponent[]{maHM, maDaoCu, soLuong}, addOrUpdate, delete);
    }

    private JComponent wrapCrud(
            JTable table,
            Runnable load,
            String[] labels,
            JComponent[] fields,
            JButton add,
            JButton delete
    ) {
        return wrapCrud(table, load, labels, fields, add, delete, null);
    }

    private JComponent wrapCrud(
            JTable table,
            Runnable load,
            String[] labels,
            JComponent[] fields,
            JButton add,
            JButton delete,
            JButton extra
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

        JButton refresh = new JButton("Tải lại");
        refresh.putClientProperty(FlatClientProperties.STYLE,
                "arc:10;" +
                        "background:#111111;" +
                        "foreground:#FFFFFF;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
        refresh.addActionListener(e -> load.run());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setBackground(CARD_BG);
        buttons.add(add);
        buttons.add(delete);
        if (extra != null) {
            buttons.add(extra);
        }
        buttons.add(refresh);

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


