package scriptmanager.ui.hangmuc;

import net.miginfocom.swing.MigLayout;
import scriptmanager.entity.asset.DaoCu;
import scriptmanager.entity.asset.ThietBi;
import scriptmanager.entity.assignment.PhanCongNhanSu;
import scriptmanager.entity.assignment.PhanCongThietBi;
import scriptmanager.entity.assignment.SuDungDaoCu;
import scriptmanager.entity.assignment.pk.PhanCongNhanSuId;
import scriptmanager.entity.assignment.pk.PhanCongThietBiId;
import scriptmanager.entity.assignment.pk.SuDungDaoCuId;
import scriptmanager.entity.core.HangMucKichBan;
import scriptmanager.entity.user.NhanSu;
import scriptmanager.service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PhanCongDialog extends JDialog {
    private Integer hangMucId;
    private AssignmentService assignmentService;
    private HangMucKichBanService hangMucService;
    private NhanSuService nhanSuService;
    private ThietBiService thietBiService;
    private DaoCuService daoCuService;

    private DefaultTableModel modelNhanSu, modelThietBi, modelDaoCu;
    private JTable tblNhanSu, tblThietBi, tblDaoCu;
    private JComboBox<NhanSuItem> cbNhanSu;
    private JComboBox<ThietBiItem> cbThietBi;
    private JComboBox<DaoCuItem> cbDaoCu;
    private JTextField txtNhiemVu, txtSlThietBi, txtSlDaoCu;

    public PhanCongDialog(Window owner, Integer hangMucId) {
        super(owner, "Phân Công Nguồn Lực", Dialog.ModalityType.APPLICATION_MODAL);
        this.hangMucId = hangMucId;
        this.assignmentService = new AssignmentServiceImpl();
        this.hangMucService = new HangMucKichBanServiceImpl();
        this.nhanSuService = new NhanSuService();
        this.thietBiService = new ThietBiService();
        this.daoCuService = new DaoCuService();

        setSize(800, 600);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Nhân Sự", createNhanSuPanel());
        tabs.addTab("Thiết Bị", createThietBiPanel());
        tabs.addTab("Đạo Cụ", createDaoCuPanel());
        add(tabs, BorderLayout.CENTER);

        loadData();
    }

    private JPanel createNhanSuPanel() {
        JPanel panel = new JPanel(new MigLayout("fill,wrap", "[grow]", "[][grow][]"));
        panel.setBackground(Color.WHITE);

        JPanel form = new JPanel(new MigLayout("insets 0", "[][grow][][grow][]"));
        form.setOpaque(false);
        form.add(new JLabel("Nhân sự:"));
        cbNhanSu = new JComboBox<>();
        form.add(cbNhanSu, "growx");
        form.add(new JLabel("Nhiệm vụ:"));
        txtNhiemVu = new JTextField();
        form.add(txtNhiemVu, "growx");
        JButton btnAdd = new JButton("Thêm / Cập Nhật");
        btnAdd.addActionListener(e -> addNhanSu());
        form.add(btnAdd);
        panel.add(form, "growx");

        modelNhanSu = new DefaultTableModel(new String[]{"Mã NS", "Tên NS", "Nhiệm Vụ"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblNhanSu = new JTable(modelNhanSu);
        tblNhanSu.setRowHeight(30);
        panel.add(new JScrollPane(tblNhanSu), "grow, push");

        JButton btnDelete = new JButton("Gỡ Bỏ");
        btnDelete.addActionListener(e -> deleteNhanSu());
        panel.add(btnDelete, "right");

        return panel;
    }

    private JPanel createThietBiPanel() {
        JPanel panel = new JPanel(new MigLayout("fill,wrap", "[grow]", "[][grow][]"));
        panel.setBackground(Color.WHITE);

        JPanel form = new JPanel(new MigLayout("insets 0", "[][grow][][grow][]"));
        form.setOpaque(false);
        form.add(new JLabel("Thiết bị:"));
        cbThietBi = new JComboBox<>();
        form.add(cbThietBi, "growx");
        form.add(new JLabel("Số lượng:"));
        txtSlThietBi = new JTextField();
        form.add(txtSlThietBi, "growx");
        JButton btnAdd = new JButton("Thêm / Cập Nhật");
        btnAdd.addActionListener(e -> addThietBi());
        form.add(btnAdd);
        panel.add(form, "growx");

        modelThietBi = new DefaultTableModel(new String[]{"Mã TB", "Tên TB", "Số Lượng Dùng"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblThietBi = new JTable(modelThietBi);
        tblThietBi.setRowHeight(30);
        panel.add(new JScrollPane(tblThietBi), "grow, push");

        JButton btnDelete = new JButton("Gỡ Bỏ");
        btnDelete.addActionListener(e -> deleteThietBi());
        panel.add(btnDelete, "right");

        return panel;
    }

    private JPanel createDaoCuPanel() {
        JPanel panel = new JPanel(new MigLayout("fill,wrap", "[grow]", "[][grow][]"));
        panel.setBackground(Color.WHITE);

        JPanel form = new JPanel(new MigLayout("insets 0", "[][grow][][grow][]"));
        form.setOpaque(false);
        form.add(new JLabel("Đạo cụ:"));
        cbDaoCu = new JComboBox<>();
        form.add(cbDaoCu, "growx");
        form.add(new JLabel("Số lượng:"));
        txtSlDaoCu = new JTextField();
        form.add(txtSlDaoCu, "growx");
        JButton btnAdd = new JButton("Thêm / Cập Nhật");
        btnAdd.addActionListener(e -> addDaoCu());
        form.add(btnAdd);
        panel.add(form, "growx");

        modelDaoCu = new DefaultTableModel(new String[]{"Mã ĐC", "Tên ĐC", "Số Lượng Dùng"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblDaoCu = new JTable(modelDaoCu);
        tblDaoCu.setRowHeight(30);
        panel.add(new JScrollPane(tblDaoCu), "grow, push");

        JButton btnDelete = new JButton("Gỡ Bỏ");
        btnDelete.addActionListener(e -> deleteDaoCu());
        panel.add(btnDelete, "right");

        return panel;
    }

    private void loadData() {
        new SwingWorker<Void, Void>() {
            List<NhanSu> nss = new ArrayList<>();
            List<ThietBi> tbs = new ArrayList<>();
            List<DaoCu> dcs = new ArrayList<>();
            @Override protected Void doInBackground() {
                try {
                    nss = nhanSuService.findAll();
                    tbs = thietBiService.findAll();
                    dcs = daoCuService.findAll();
                } catch(Exception ignored){}
                return null;
            }
            @Override protected void done() {
                for (NhanSu n : nss) cbNhanSu.addItem(new NhanSuItem(n));
                for (ThietBi t : tbs) cbThietBi.addItem(new ThietBiItem(t));
                for (DaoCu d : dcs) cbDaoCu.addItem(new DaoCuItem(d));
                loadTables();
            }
        }.execute();
    }

    private void loadTables() {
        new SwingWorker<Void, Void>() {
            List<PhanCongNhanSu> listNS = new ArrayList<>();
            List<PhanCongThietBi> listTB = new ArrayList<>();
            List<SuDungDaoCu> listDC = new ArrayList<>();
            @Override protected Void doInBackground() {
                try {
                    listNS = assignmentService.getNhanSuByHangMuc(hangMucId);
                    listTB = assignmentService.getThietBiByHangMuc(hangMucId);
                    listDC = assignmentService.getDaoCuByHangMuc(hangMucId);
                } catch(Exception ignored){}
                return null;
            }
            @Override protected void done() {
                modelNhanSu.setRowCount(0);
                for (PhanCongNhanSu pc : listNS) {
                    modelNhanSu.addRow(new Object[]{pc.getNhanSu().getMaNS(), pc.getNhanSu().getTenNS(), pc.getNhiemVu()});
                }
                modelThietBi.setRowCount(0);
                for (PhanCongThietBi pc : listTB) {
                    modelThietBi.addRow(new Object[]{pc.getThietBi().getMaTB(), pc.getThietBi().getTenTB(), pc.getSoLuongSuDung()});
                }
                modelDaoCu.setRowCount(0);
                for (SuDungDaoCu pc : listDC) {
                    modelDaoCu.addRow(new Object[]{pc.getDaoCu().getMaDaoCu(), pc.getDaoCu().getTenDaoCu(), pc.getSoLuongSuDung()});
                }
            }
        }.execute();
    }

    private void addNhanSu() {
        NhanSuItem item = (NhanSuItem) cbNhanSu.getSelectedItem();
        if (item == null) return;
        String nv = txtNhiemVu.getText().trim();
        
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                PhanCongNhanSu pc = new PhanCongNhanSu();
                PhanCongNhanSuId id = new PhanCongNhanSuId();
                id.setMaHM(hangMucId);
                id.setMaNS(item.ns.getMaNS());
                
                try {
                    java.lang.reflect.Method m3 = pc.getClass().getMethod("setId", PhanCongNhanSuId.class);
                    m3.invoke(pc, id);
                } catch(Exception e) {
                    try {
                        java.lang.reflect.Field f3 = pc.getClass().getDeclaredField("id");
                        f3.setAccessible(true); f3.set(pc, id);
                    } catch (Exception ex) {}
                }

                try {
                    java.lang.reflect.Method mNv = pc.getClass().getMethod("setNhiemVu", String.class);
                    mNv.invoke(pc, nv);
                } catch (Exception e) {
                    try {
                        java.lang.reflect.Field fNv = pc.getClass().getDeclaredField("nhiemVu");
                        fNv.setAccessible(true); fNv.set(pc, nv);
                    } catch (Exception ex) {}
                }

                assignmentService.assignNhanSu(pc);
                return null;
            }
            @Override protected void done() {
                try {
                    get();
                    txtNhiemVu.setText("");
                    loadTables();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PhanCongDialog.this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void addThietBi() {
        ThietBiItem item = (ThietBiItem) cbThietBi.getSelectedItem();
        if (item == null) return;
        
        int sl;
        try { 
            sl = Integer.parseInt(txtSlThietBi.getText().trim()); 
        } catch (Exception ex) { 
            JOptionPane.showMessageDialog(this, "Số lượng phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return; 
        }
        
        if (sl <= 0) {
            JOptionPane.showMessageDialog(this, "Số lượng phải > 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Kiểm tra chặt tồn kho
        int tonKho = item.tb.getSoLuong();
        if (sl > tonKho) {
            JOptionPane.showMessageDialog(this, "Vượt quá tổng tồn kho (Tối đa: " + tonKho + ")!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                PhanCongThietBi pc = new PhanCongThietBi();
                PhanCongThietBiId id = new PhanCongThietBiId();
                id.setMaHM(hangMucId);
                id.setMaTB(item.tb.getMaTB());
                
                try {
                    java.lang.reflect.Method m3 = pc.getClass().getMethod("setId", PhanCongThietBiId.class);
                    m3.invoke(pc, id);
                } catch(Exception e) {
                    try {
                        java.lang.reflect.Field f3 = pc.getClass().getDeclaredField("id");
                        f3.setAccessible(true); f3.set(pc, id);
                    } catch (Exception ex) {}
                }

                try {
                    java.lang.reflect.Method mSl = pc.getClass().getMethod("setSoLuongSuDung", int.class);
                    mSl.invoke(pc, sl);
                } catch (Exception e) {
                    try {
                        java.lang.reflect.Method mSl = pc.getClass().getMethod("setSoLuongSuDung", Integer.class);
                        mSl.invoke(pc, sl);
                    } catch (Exception ex) {
                        try {
                            java.lang.reflect.Field fSl = pc.getClass().getDeclaredField("soLuongSuDung");
                            fSl.setAccessible(true); fSl.set(pc, sl);
                        } catch (Exception ex2) {}
                    }
                }

                assignmentService.assignThietBi(pc);
                return null;
            }
            @Override protected void done() {
                try {
                    get();
                    txtSlThietBi.setText("");
                    loadTables();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PhanCongDialog.this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void addDaoCu() {
        DaoCuItem item = (DaoCuItem) cbDaoCu.getSelectedItem();
        if (item == null) return;
        
        int sl;
        try { 
            sl = Integer.parseInt(txtSlDaoCu.getText().trim()); 
        } catch (Exception ex) { 
            JOptionPane.showMessageDialog(this, "Số lượng phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return; 
        }
        
        if (sl <= 0) {
            JOptionPane.showMessageDialog(this, "Số lượng phải > 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int tonKho = item.dc.getSoLuong();
        if (sl > tonKho) {
            JOptionPane.showMessageDialog(this, "Vượt quá tổng tồn kho (Tối đa: " + tonKho + ")!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                SuDungDaoCu pc = new SuDungDaoCu();
                SuDungDaoCuId id = new SuDungDaoCuId();
                id.setMaHM(hangMucId);
                id.setMaDaoCu(item.dc.getMaDaoCu());
                
                try {
                    java.lang.reflect.Method m3 = pc.getClass().getMethod("setId", SuDungDaoCuId.class);
                    m3.invoke(pc, id);
                } catch(Exception e) {
                    try {
                        java.lang.reflect.Field f3 = pc.getClass().getDeclaredField("id");
                        f3.setAccessible(true); f3.set(pc, id);
                    } catch (Exception ex) {}
                }

                try {
                    java.lang.reflect.Method mSl = pc.getClass().getMethod("setSoLuongSuDung", int.class);
                    mSl.invoke(pc, sl);
                } catch (Exception e) {
                    try {
                        java.lang.reflect.Method mSl = pc.getClass().getMethod("setSoLuongSuDung", Integer.class);
                        mSl.invoke(pc, sl);
                    } catch (Exception ex) {
                        try {
                            java.lang.reflect.Field fSl = pc.getClass().getDeclaredField("soLuongSuDung");
                            fSl.setAccessible(true); fSl.set(pc, sl);
                        } catch (Exception ex2) {}
                    }
                }

                assignmentService.assignDaoCu(pc);
                return null;
            }
            @Override protected void done() {
                try {
                    get();
                    txtSlDaoCu.setText("");
                    loadTables();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PhanCongDialog.this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void deleteNhanSu() {
        int r = tblNhanSu.getSelectedRow();
        if (r < 0) return;
        int maNS = (int) modelNhanSu.getValueAt(r, 0);
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                PhanCongNhanSuId id = new PhanCongNhanSuId();
                id.setMaHM(hangMucId);
                id.setMaNS(maNS);
                assignmentService.removeNhanSu(id);
                return null;
            }
            @Override protected void done() { 
                try { get(); loadTables(); } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PhanCongDialog.this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void deleteThietBi() {
        int r = tblThietBi.getSelectedRow();
        if (r < 0) return;
        int maTB = (int) modelThietBi.getValueAt(r, 0);
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                PhanCongThietBiId id = new PhanCongThietBiId();
                id.setMaHM(hangMucId);
                id.setMaTB(maTB);
                assignmentService.removeThietBi(id);
                return null;
            }
            @Override protected void done() { 
                try { get(); loadTables(); } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PhanCongDialog.this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void deleteDaoCu() {
        int r = tblDaoCu.getSelectedRow();
        if (r < 0) return;
        int maDC = (int) modelDaoCu.getValueAt(r, 0);
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                SuDungDaoCuId id = new SuDungDaoCuId();
                id.setMaHM(hangMucId);
                id.setMaDaoCu(maDC);
                assignmentService.removeDaoCu(id);
                return null;
            }
            @Override protected void done() { 
                try { get(); loadTables(); } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PhanCongDialog.this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    static class NhanSuItem {
        NhanSu ns;
        NhanSuItem(NhanSu ns) { this.ns = ns; }
        @Override public String toString() { return ns.getTenNS() + " (ID: " + ns.getMaNS() + ")"; }
    }
    static class ThietBiItem {
        ThietBi tb;
        ThietBiItem(ThietBi tb) { this.tb = tb; }
        @Override public String toString() { return tb.getTenTB() + " (ID: " + tb.getMaTB() + ")"; }
    }
    static class DaoCuItem {
        DaoCu dc;
        DaoCuItem(DaoCu dc) { this.dc = dc; }
        @Override public String toString() { return dc.getTenDaoCu() + " (ID: " + dc.getMaDaoCu() + ")"; }
    }
}
