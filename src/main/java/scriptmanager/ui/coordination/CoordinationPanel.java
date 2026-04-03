package scriptmanager.ui.coordination;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import scriptmanager.service.CoordinationService;
import scriptmanager.service.CoordinationServiceImpl;
import scriptmanager.ui.main.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class CoordinationPanel extends JPanel {

    private static final Color TONE_900 = new Color(17, 17, 17);
    private static final Color BG_SOFT = new Color(245, 247, 250);

    private final MainFrame mainFrame;
    private final CoordinationService coordinationService;

    private DefaultTableModel scheduleModel;
    private DefaultTableModel usageModel;

    public CoordinationPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.coordinationService = new CoordinationServiceImpl();
        init();
        loadData();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBackground(BG_SOFT);
        setBorder(new EmptyBorder(22, 24, 22, 24));

        JPanel container = new JPanel(new MigLayout("fill,wrap,insets 0,gap 14", "[grow]", "[][grow]"));
        container.setOpaque(false);

        container.add(createHeader(), "growx");
        container.add(createContent(), "grow,push");

        add(container, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new MigLayout("fillx,insets 16 20 16 20", "[grow][][]", "[]"));
        panel.setBackground(TONE_900);
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:24");

        JLabel title = new JLabel("Điều phối sự kiện - Nhân lực");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        panel.add(title, "growx");

        JButton btnReload = new JButton("Tải lại");
        btnReload.putClientProperty(FlatClientProperties.STYLE,
                "arc:12;background:#262626;foreground:#FFFFFF;focusWidth:0;borderWidth:0");
        btnReload.addActionListener(e -> loadData());

        JButton btnBack = new JButton("Quay lại Dashboard");
        btnBack.putClientProperty(FlatClientProperties.STYLE,
                "arc:12;background:#F3F4F6;foreground:#111111;focusWidth:0;borderWidth:0");
        btnBack.addActionListener(e -> mainFrame.showDashboard());

        panel.add(btnReload, "h 34!");
        panel.add(btnBack, "w 150!,h 34!");

        return panel;
    }

    private JComponent createContent() {
        JTabbedPane tabs = new JTabbedPane();

        scheduleModel = readonlyModel(new String[]{"Hạng mục", "Sự kiện", "Bắt đầu", "Kết thúc", "Số nhân sự"});
        usageModel = readonlyModel(new String[]{"Nhân sự", "Nhiệm vụ", "Hạng mục", "Sự kiện", "Khung thời gian"});

        tabs.addTab("Lịch hạng mục sự kiện", wrapTable(buildTable(scheduleModel)));
        tabs.addTab("Phân bổ nhân lực", wrapTable(buildTable(usageModel)));

        return tabs;
    }

    private DefaultTableModel readonlyModel(String[] columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private JTable buildTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(34);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(229, 231, 235));
        table.setSelectionForeground(TONE_900);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(243, 244, 246));
        table.getTableHeader().setForeground(TONE_900);
        table.setShowVerticalLines(false);
        return table;
    }

    private JComponent wrapTable(JTable table) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        panel.add(sp, BorderLayout.CENTER);

        return panel;
    }

    private void loadData() {
        new SwingWorker<Void, Void>() {
            private List<Map<String, Object>> fullSchedule;
            private List<Map<String, Object>> resourceUsage;

            @Override
            protected Void doInBackground() {
                fullSchedule = coordinationService.getFullSchedule();
                resourceUsage = coordinationService.getResourceUsageByTime();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();

                    scheduleModel.setRowCount(0);
                    for (Map<String, Object> item : fullSchedule) {
                        scheduleModel.addRow(new Object[]{
                                item.get("tenHangMuc"),
                                item.get("tenSuKien"),
                                item.get("batDau"),
                                item.get("ketThuc"),
                                item.get("soNhanSu")
                        });
                    }

                    usageModel.setRowCount(0);
                    for (Map<String, Object> item : resourceUsage) {
                        usageModel.addRow(new Object[]{
                                item.get("tenNhanSu"),
                                item.get("nhiemVu"),
                                item.get("tenHangMuc"),
                                item.get("tenSuKien"),
                                item.get("thoiGian")
                        });
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(CoordinationPanel.this,
                            "Lỗi khi tải dữ liệu điều phối: " + ex.getMessage());
                }
            }
        }.execute();
    }
}
