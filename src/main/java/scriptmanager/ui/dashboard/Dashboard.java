package scriptmanager.ui.dashboard;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import scriptmanager.dto.DashboardResourceAlertDTO;
import scriptmanager.dto.DashboardStatsDTO;
import scriptmanager.dto.DashboardTaskItemDTO;
import scriptmanager.dto.DashboardTimelineItemDTO;
import scriptmanager.service.DashboardService;
import scriptmanager.ui.main.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Dashboard extends JPanel {

    private static final Color TONE_900 = new Color(17, 17, 17);
    private static final Color TONE_800 = new Color(35, 35, 35);
    private static final Color TONE_700 = new Color(82, 82, 82);
    private static final Color TONE_500 = new Color(120, 120, 120);
    private static final Color TONE_200 = new Color(224, 224, 224);
    private static final Color BG_SOFT = new Color(245, 247, 250);
    private static final Color TEXT_DARK = new Color(24, 24, 24);

    private final MainFrame mainFrame;
    private final DashboardService dashboardService;
    private DashboardStatsDTO stats;
    private List<DashboardTimelineItemDTO> timelineItems;
    private List<DashboardResourceAlertDTO> resourceAlerts;
    private List<DashboardTaskItemDTO> taskItems;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter TASK_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM HH:mm");

    public Dashboard(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.dashboardService = new DashboardService();
        this.stats = new DashboardStatsDTO(0, 0, 0, 0);
        this.timelineItems = new ArrayList<>();
        this.resourceAlerts = new ArrayList<>();
        this.taskItems = new ArrayList<>();
        loadDashboardData();
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBackground(BG_SOFT);
        setBorder(new EmptyBorder(22, 24, 22, 24));

        JPanel container = new JPanel(new MigLayout("fillx,wrap,insets 0,gap 14", "[grow]", "[]"));
        container.setOpaque(false);

        container.add(createHeader(), "growx");
        container.add(createMetricsRow(), "growx");
        container.add(createMiddleRow(), "growx");
        container.add(createTaskPanel(), "growx,pushy,growy");

        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BG_SOFT);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new MigLayout("fillx,insets 16 20 16 20", "[grow][]", "[]"));
        panel.setBackground(Color.WHITE);
        panel.putClientProperty(FlatClientProperties.STYLE,
                "arc:24;" +
                        "border:1,1,1,1,#D1D5DB");

        JPanel left = new JPanel(new MigLayout("wrap,insets 0,gap 2", "[]", "[]"));
        left.setOpaque(false);

        JLabel title = new JLabel("Dashboard Điều Phối Sự Kiện");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(TEXT_DARK);

        JLabel subtitle = new JLabel("Theo dõi tổng quan tiến độ, lịch tổng duyệt và cảnh báo tài nguyên");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TONE_700);

        left.add(title);
        left.add(subtitle);

        JPanel right = new JPanel(new MigLayout("wrap,insets 0,align right", "[]", "[]"));
        right.setOpaque(false);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        JLabel date = new JLabel("Ngày " + LocalDate.now().format(formatter));
        date.setFont(new Font("Segoe UI", Font.BOLD, 13));
        date.setForeground(TEXT_DARK);

        JButton hmManager = new JButton("QL Kịch Bản");
        hmManager.putClientProperty(FlatClientProperties.STYLE,
                "arc:12;" +
                "background:#F3F4F6;" +
                "foreground:#111111;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
        hmManager.addActionListener(e -> mainFrame.showHangMucManager());

        JButton eventManager = new JButton("QL Sự Kiện");
        eventManager.putClientProperty(FlatClientProperties.STYLE,
                "arc:12;" +
                "background:#F3F4F6;" +
                "foreground:#111111;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
        eventManager.addActionListener(e -> mainFrame.showSuKienManager());

        JButton userManager = new JButton("QL Người Dùng");
        userManager.putClientProperty(FlatClientProperties.STYLE,
                "arc:12;" +
                "background:#F3F4F6;" +
                "foreground:#111111;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
        userManager.addActionListener(e -> mainFrame.showUserManager());

        JButton coordinationBoard = new JButton("Bảng điều phối");
        coordinationBoard.putClientProperty(FlatClientProperties.STYLE,
                "arc:12;" +
                "background:#F3F4F6;" +
                "foreground:#111111;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
        coordinationBoard.addActionListener(e -> mainFrame.showCoordinationManager());

        JButton extendedModules = new JButton("Phân hệ tài nguyên");
        extendedModules.putClientProperty(FlatClientProperties.STYLE,
                "arc:12;" +
                "background:#F3F4F6;" +
                "foreground:#111111;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
        extendedModules.addActionListener(e -> mainFrame.showExtendedModules());

        JButton logout = new JButton("Đăng xuất");
        logout.putClientProperty(FlatClientProperties.STYLE,
                "arc:12;" +
                "background:#F3F4F6;" +
                "foreground:#111111;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
        logout.addActionListener(e -> mainFrame.logout());

        right.add(date, "align right");
        
        JPanel btnPanel = new JPanel(new MigLayout("insets 0, gap 8"));
        btnPanel.setOpaque(false);
        btnPanel.add(hmManager, "h 34!");
        btnPanel.add(eventManager, "h 34!");
        btnPanel.add(userManager, "h 34!");
        btnPanel.add(coordinationBoard, "h 34!");
        btnPanel.add(extendedModules, "h 34!");
        btnPanel.add(logout, "w 110!,h 34!");
        
        right.add(btnPanel, "gapy 10 0,align right");

        panel.add(left, "growx");
        panel.add(right, "align right");

        return panel;
    }

    private JPanel createMetricsRow() {
        JPanel row = new JPanel(new MigLayout("fillx,insets 0,gap 12", "[grow][grow][grow][grow]", "[]"));
        row.setOpaque(false);

        row.add(createMetricCard("Sự kiện sắp tới", String.valueOf(stats.getSuKienSapToi()), "Theo thời gian hiện tại", TONE_900), "growx");
        row.add(createMetricCard("Tổng duyệt chưa xong", String.valueOf(stats.getTongDuyetChuaXong()), "Cần ưu tiên hôm nay", TONE_800), "growx");
        row.add(createMetricCard("Thiết bị nguy cơ thiếu", String.valueOf(stats.getThietBiNguyCoThieu()), "Ngưỡng kiểm tra: < 5", TONE_700), "growx");
        row.add(createMetricCard("Nhân sự đã phân công", String.valueOf(stats.getNhanSuDaPhanCong()), "Đếm theo nhân sự phân công", TONE_500), "growx");

        return row;
    }

    private void loadDashboardData() {
        try {
            DashboardStatsDTO data = dashboardService.getDashboardStats();
            if (data != null) {
                this.stats = data;
            }
            this.timelineItems = dashboardService.getLatestRehearsals(6);
            this.resourceAlerts = dashboardService.getResourceAlerts(4);
            this.taskItems = dashboardService.getPendingTasks(8);
        } catch (Exception ignored) {
            this.timelineItems = new ArrayList<>();
            this.resourceAlerts = new ArrayList<>();
            this.taskItems = new ArrayList<>();
        }
    }

    private JPanel createMetricCard(String titleText, String valueText, String noteText, Color accent) {
        JPanel card = new JPanel(new MigLayout("wrap,fillx,insets 14 16 14 16", "[grow]", "[]"));
        card.setBackground(Color.WHITE);
        card.putClientProperty(FlatClientProperties.STYLE, "arc:18");

        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        title.setForeground(new Color(75, 85, 99));

        JLabel value = new JLabel(valueText);
        value.setFont(new Font("Segoe UI", Font.BOLD, 30));
        value.setForeground(accent);

        JLabel note = new JLabel(noteText);
        note.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        note.setForeground(new Color(107, 114, 128));

        card.add(title);
        card.add(value, "gapy 2 0");
        card.add(note, "gapy 4 0");

        return card;
    }

    private JPanel createMiddleRow() {
        JPanel row = new JPanel(new MigLayout("fillx,insets 0,gap 12", "[grow][grow]", "[grow]"));
        row.setOpaque(false);

        row.add(createTimelinePanel(), "grow,pushy");
        row.add(createWarningPanel(), "grow,pushy");

        return row;
    }

    private JPanel createTimelinePanel() {
        JPanel panel = createSectionPanel("Lịch tổng duyệt gần nhất", "Ưu tiên xử lý theo khung giờ");

        DefaultListModel<String> model = new DefaultListModel<>();
        if (timelineItems.isEmpty()) {
            model.addElement("Chưa có dữ liệu lịch tổng duyệt");
        } else {
            for (DashboardTimelineItemDTO item : timelineItems) {
                String timeText = item.getThoiGianDuyet() == null
                        ? "--:--"
                        : item.getThoiGianDuyet().format(TIME_FORMATTER);
                String eventText = safe(item.getTenSuKien(), "N/A");
                String contentText = safe(item.getNoiDung(), "Chưa có nội dung");
                model.addElement(timeText + "  | " + eventText + " - " + contentText);
            }
        }

        JList<String> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFixedCellHeight(38);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        list.setBackground(new Color(255, 255, 255));
        list.setForeground(TEXT_DARK);
        list.setBorder(new EmptyBorder(10, 10, 10, 10));
        if (!model.isEmpty()) {
            list.setSelectedIndex(0);
        }
        list.putClientProperty(FlatClientProperties.STYLE,
            "selectionBackground:#E5E7EB;" +
                "selectionForeground:#111111");

        list.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = list.locationToIndex(e.getPoint());
                    if (index >= 0 && index < timelineItems.size()) {
                        try {
                            Integer hmId = (Integer) timelineItems.get(index).getClass().getMethod("getMaHM").invoke(timelineItems.get(index));
                            mainFrame.showHangMucManager(hmId);
                        } catch (Exception ex) {
                            mainFrame.showHangMucManager();
                        }
                    }
                }
            }
        });

        list.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = list.locationToIndex(e.getPoint());
                    if (index >= 0 && index < timelineItems.size()) {
                        try {
                            Integer hmId = (Integer) timelineItems.get(index).getClass().getMethod("getMaHM").invoke(timelineItems.get(index));
                            mainFrame.showHangMucManager(hmId);
                        } catch (Exception ex) {
                            mainFrame.showHangMucManager();
                        }
                    }
                }
            }
        });

        JScrollPane sp = new JScrollPane(list);
        sp.setBorder(new EmptyBorder(0, 0, 0, 0));
        sp.getViewport().setBackground(Color.WHITE);

        panel.add(sp, "grow,push");
        return panel;
    }

    private JPanel createWarningPanel() {
        JPanel panel = createSectionPanel("Cảnh báo tài nguyên", "Kiểm soát tồn kho và phân công");

        if (resourceAlerts.isEmpty()) {
            panel.add(createWarningItem("Chưa có cảnh báo", "Dữ liệu thiết bị chưa sẵn sàng", 0, TONE_500), "growx");
        } else {
            for (int i = 0; i < resourceAlerts.size(); i++) {
                DashboardResourceAlertDTO alert = resourceAlerts.get(i);
                String status = "Đã đặt " + alert.getDaDat() + "/" + alert.getTongSo();
                Color accent = pickAlertColor(alert.getPhanTram());
                String gap = i == 0 ? "growx" : "growx,gapy 8 0";
                panel.add(createWarningItem(alert.getTenTaiNguyen(), status, alert.getPhanTram(), accent), gap);
            }
        }

        JPanel actions = new JPanel(new MigLayout("insets 10 0 0 0,gap 8", "[grow][grow]", "[]"));
        actions.setOpaque(false);

        JButton assignBtn = new JButton("Mở màn phân công");
        assignBtn.putClientProperty(FlatClientProperties.STYLE,
                "arc:12;" +
                "background:#111111;" +
                        "foreground:#FFFFFF;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
        assignBtn.addActionListener(e -> mainFrame.showCoordinationManager());

        JButton importBtn = new JButton("Cập nhật tồn kho");
        importBtn.putClientProperty(FlatClientProperties.STYLE,
                "arc:12;" +
                "background:#F3F4F6;" +
                "foreground:#111111;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
        importBtn.addActionListener(e -> mainFrame.showExtendedModules());

        actions.add(assignBtn, "h 34!");
        actions.add(importBtn, "h 34!");

        panel.add(actions, "growx,pushy,aligny bottom");
        return panel;
    }

    private JPanel createWarningItem(String name, String status, int value, Color accent) {
        JPanel item = new JPanel(new MigLayout("fillx,wrap,insets 8 10 8 10", "[grow][]", "[]"));
        item.setBackground(new Color(249, 250, 251));
        item.putClientProperty(FlatClientProperties.STYLE, "arc:12");

        JLabel lbName = new JLabel(name);
        lbName.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbName.setForeground(TEXT_DARK);

        JLabel lbStatus = new JLabel(status);
        lbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbStatus.setForeground(new Color(107, 114, 128));

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(value);
        progressBar.setStringPainted(true);
        progressBar.setString(value + "%");
        progressBar.setForeground(accent);
        progressBar.setBackground(new Color(229, 231, 235));
        progressBar.setBorderPainted(false);

        item.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mainFrame.showExtendedModules();
            }
        });

        item.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mainFrame.showExtendedModules();
            }
        });

        item.add(lbName, "split 2");
        item.add(lbStatus, "align right");
        item.add(progressBar, "growx");

        return item;
    }

    private JPanel createTaskPanel() {
        JPanel panel = createSectionPanel("Hạng mục cần xử lý", "Tổng hợp công việc trong ngày");

        String[] columns = {"Hạng mục", "Sự kiện", "Người phụ trách", "Mốc giờ", "Trạng thái"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if (taskItems.isEmpty()) {
            tableModel.addRow(new Object[]{"Chưa có hạng mục", "-", "-", "-", "-"});
        } else {
            for (DashboardTaskItemDTO item : taskItems) {
                String timeText = item.getMocGio() == null ? "--" : item.getMocGio().format(TASK_TIME_FORMATTER);
                tableModel.addRow(new Object[]{
                        safe(item.getHangMuc(), "N/A"),
                        safe(item.getSuKien(), "N/A"),
                        safe(item.getNguoiPhuTrach(), "Chưa phân công"),
                        timeText,
                        safe(item.getTrangThai(), "N/A")
                });
            }
        }

        JTable table = new JTable(tableModel);

        table.setRowHeight(34);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(229, 231, 235));
        table.setSelectionForeground(TONE_900);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(243, 244, 246));
        table.getTableHeader().setForeground(TONE_900);
        table.setGridColor(new Color(229, 231, 235));
        table.setShowVerticalLines(false);

        table.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0 && row < taskItems.size()) {
                        try {
                            Integer hmId = (Integer) taskItems.get(row).getClass().getMethod("getMaHM").invoke(taskItems.get(row));
                            mainFrame.showHangMucManager(hmId);
                        } catch (Exception ex) {
                            mainFrame.showHangMucManager();
                        }
                    }
                }
            }
        });

        table.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0 && row < taskItems.size()) {
                        try {
                            Integer hmId = (Integer) taskItems.get(row).getClass().getMethod("getMaHM").invoke(taskItems.get(row));
                            mainFrame.showHangMucManager(hmId);
                        } catch (Exception ex) {
                            mainFrame.showHangMucManager();
                        }
                    }
                }
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new EmptyBorder(0, 0, 0, 0));
        sp.getViewport().setBackground(Color.WHITE);

        panel.add(sp, "grow,push");

        return panel;
    }

    private JPanel createSectionPanel(String title, String description) {
        JPanel panel = new JPanel(new MigLayout("fill,wrap,insets 14 16 14 16,gap 8", "[grow]", "[]"));
        panel.setBackground(Color.WHITE);
        panel.putClientProperty(FlatClientProperties.STYLE,
                "arc:18;" +
                "border:1,1,1,1,#D1D5DB");

        JLabel lbTitle = new JLabel(title);
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbTitle.setForeground(TONE_900);

        JLabel lbDescription = new JLabel(description);
        lbDescription.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbDescription.setForeground(TONE_700);

        panel.add(lbTitle);
        panel.add(lbDescription, "gapy 0 6");

        return panel;
    }

    private Color pickAlertColor(int percent) {
        if (percent >= 85) {
            return TONE_900;
        }
        if (percent >= 70) {
            return TONE_800;
        }
        if (percent >= 55) {
            return TONE_700;
        }
        return TONE_500;
    }

    private String safe(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}