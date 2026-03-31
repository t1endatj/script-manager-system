package scriptmanager.ui.dashboard;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import scriptmanager.ui.main.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Dashboard extends JPanel {

    private static final Color TONE_900 = new Color(29, 36, 68);
    private static final Color TONE_800 = new Color(47, 57, 94);
    private static final Color TONE_700 = new Color(95, 102, 132);
    private static final Color TONE_500 = new Color(136, 141, 167);
    private static final Color TONE_200 = new Color(193, 196, 213);
    private static final Color BG_SOFT = new Color(232, 235, 245);
    private static final Color TEXT_DARK = new Color(35, 42, 64);

    private final MainFrame mainFrame;

    public Dashboard(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
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
        panel.setBackground(TONE_900);
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:24");

        JPanel left = new JPanel(new MigLayout("wrap,insets 0,gap 2", "[]", "[]"));
        left.setOpaque(false);

        JLabel title = new JLabel("Dashboard Điều Phối Sự Kiện");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Theo dõi tổng quan tiến độ, lịch tổng duyệt và cảnh báo tài nguyên");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TONE_200);

        left.add(title);
        left.add(subtitle);

        JPanel right = new JPanel(new MigLayout("wrap,insets 0,align right", "[]", "[]"));
        right.setOpaque(false);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        JLabel date = new JLabel("Ngày " + LocalDate.now().format(formatter));
        date.setFont(new Font("Segoe UI", Font.BOLD, 13));
        date.setForeground(new Color(224, 228, 242));

        JButton logout = new JButton("Đăng xuất");
        logout.putClientProperty(FlatClientProperties.STYLE,
                "arc:12;" +
                "background:#C1C4D5;" +
                "foreground:#1D2444;" +
                        "focusWidth:0;" +
                        "borderWidth:0");
        logout.addActionListener(e -> mainFrame.showLogin());

        right.add(date, "align right");
        right.add(logout, "w 110!,h 34!,gapy 10 0,align right");

        panel.add(left, "growx");
        panel.add(right, "align right");

        return panel;
    }

    private JPanel createMetricsRow() {
        JPanel row = new JPanel(new MigLayout("fillx,insets 0,gap 12", "[grow][grow][grow][grow]", "[]"));
        row.setOpaque(false);

        row.add(createMetricCard("Sự kiện sắp tới", "08", "+2 tuần này", TONE_900, new Color(219, 223, 238)), "growx");
        row.add(createMetricCard("Tổng duyệt chưa xong", "03", "Cần ưu tiên hôm nay", TONE_800, new Color(212, 217, 234)), "growx");
        row.add(createMetricCard("Thiết bị nguy cơ thiếu", "05", "2 mục dưới ngưỡng", TONE_700, new Color(223, 226, 238)), "growx");
        row.add(createMetricCard("Nhân sự đã phân công", "27", "Trong 5 sự kiện", TONE_500, new Color(227, 230, 241)), "growx");

        return row;
    }

    private JPanel createMetricCard(String titleText, String valueText, String noteText, Color accent, Color surface) {
        JPanel card = new JPanel(new MigLayout("wrap,fillx,insets 14 16 14 16", "[grow]", "[]"));
        card.setBackground(surface);
        card.putClientProperty(FlatClientProperties.STYLE, "arc:18");

        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        title.setForeground(new Color(67, 74, 96));

        JLabel value = new JLabel(valueText);
        value.setFont(new Font("Segoe UI", Font.BOLD, 30));
        value.setForeground(accent);

        JLabel note = new JLabel(noteText);
        note.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        note.setForeground(new Color(88, 95, 118));

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
        model.addElement("09:00  | Tiệc Cưới Nhật Minh - Tổng duyệt âm thanh");
        model.addElement("10:30  | Lễ Ra Mắt Sản Phẩm - Chạy thử hiệu ứng");
        model.addElement("14:00  | Sinh Nhật Công Ty A2 - Tổng duyệt MC");
        model.addElement("16:00  | Gala Cuối Năm - Kiểm tra đạo cụ sân khấu");
        model.addElement("19:30  | Event VIP - Chốt playlist và thiết bị");

        JList<String> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFixedCellHeight(38);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        list.setBackground(new Color(255, 255, 255));
        list.setForeground(TEXT_DARK);
        list.setBorder(new EmptyBorder(10, 10, 10, 10));
        list.setSelectedIndex(0);
        list.putClientProperty(FlatClientProperties.STYLE,
            "selectionBackground:#DCE0F1;" +
                "selectionForeground:#1D2444");

        JScrollPane sp = new JScrollPane(list);
        sp.setBorder(new EmptyBorder(0, 0, 0, 0));
        sp.getViewport().setBackground(Color.WHITE);

        panel.add(sp, "grow,push");
        return panel;
    }

    private JPanel createWarningPanel() {
        JPanel panel = createSectionPanel("Cảnh báo tài nguyên", "Kiểm soát tồn kho và phân công");

        panel.add(createWarningItem("Đèn follow 200W", "Đã đặt 9/10", 90, TONE_900), "growx");
        panel.add(createWarningItem("Loa monitor", "Đã đặt 6/8", 75, TONE_800), "growx,gapy 8 0");
        panel.add(createWarningItem("Đạo cụ backdrop", "Đã đặt 5/6", 84, TONE_700), "growx,gapy 8 0");
        panel.add(createWarningItem("Nhân sự âm thanh", "Đã đặt 3/5", 60, TONE_500), "growx,gapy 8 0");

        JPanel actions = new JPanel(new MigLayout("insets 10 0 0 0,gap 8", "[grow][grow]", "[]"));
        actions.setOpaque(false);

        JButton assignBtn = new JButton("Mở màn phân công");
        assignBtn.putClientProperty(FlatClientProperties.STYLE,
                "arc:12;" +
                "background:#1D2444;" +
                        "foreground:#FFFFFF;" +
                        "focusWidth:0;" +
                        "borderWidth:0");

        JButton importBtn = new JButton("Cập nhật tồn kho");
        importBtn.putClientProperty(FlatClientProperties.STYLE,
                "arc:12;" +
                "background:#C1C4D5;" +
                "foreground:#1D2444;" +
                        "focusWidth:0;" +
                        "borderWidth:0");

        actions.add(assignBtn, "h 34!");
        actions.add(importBtn, "h 34!");

        panel.add(actions, "growx,pushy,aligny bottom");
        return panel;
    }

    private JPanel createWarningItem(String name, String status, int value, Color accent) {
        JPanel item = new JPanel(new MigLayout("fillx,wrap,insets 8 10 8 10", "[grow][]", "[]"));
        item.setBackground(new Color(242, 244, 250));
        item.putClientProperty(FlatClientProperties.STYLE, "arc:12");

        JLabel lbName = new JLabel(name);
        lbName.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbName.setForeground(TEXT_DARK);

        JLabel lbStatus = new JLabel(status);
        lbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbStatus.setForeground(new Color(96, 102, 126));

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(value);
        progressBar.setStringPainted(true);
        progressBar.setString(value + "%");
        progressBar.setForeground(accent);
        progressBar.setBackground(new Color(214, 219, 235));
        progressBar.setBorderPainted(false);

        item.add(lbName, "split 2");
        item.add(lbStatus, "align right");
        item.add(progressBar, "growx");

        return item;
    }

    private JPanel createTaskPanel() {
        JPanel panel = createSectionPanel("Hạng mục cần xử lý", "Tổng hợp công việc trong ngày");

        String[] columns = {"Hạng mục", "Sự kiện", "Người phụ trách", "Mốc giờ", "Trạng thái"};
        Object[][] rows = {
                {"Căn chỉnh đèn sân khấu", "Gala Cuối Năm", "Trần Đức", "09:30", "Đang thực hiện"},
                {"Kiểm tra playlist mở màn", "Lễ Ra Mắt Sản Phẩm", "Ngọc Anh", "10:15", "Chờ duyệt"},
                {"Bố trí đạo cụ backdrop", "Sinh Nhật Công Ty A2", "Bảo Châu", "13:45", "Cần bổ sung"},
                {"Test micro khách mời", "Event VIP", "Minh Khang", "18:40", "Sẵn sàng"}
        };

        JTable table = new JTable(new DefaultTableModel(rows, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        table.setRowHeight(34);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(221, 225, 241));
        table.setSelectionForeground(TONE_900);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(223, 226, 238));
        table.getTableHeader().setForeground(TONE_900);
        table.setGridColor(new Color(217, 221, 235));
        table.setShowVerticalLines(false);

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
                "border:1,1,1,1,#D5DAEA");

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
}