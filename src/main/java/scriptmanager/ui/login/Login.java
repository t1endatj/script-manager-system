package scriptmanager.ui.login;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Objects;

public class Login extends JPanel {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JCheckBox chRememberMe;
    private JButton cmdLogin;

    public Login() {
        init();
    }

    private void init() {
        setLayout(new GridLayout(1, 2));
        setBackground(new Color(245, 247, 250));

        JPanel leftPanel = new JPanel() {
            private final Image image = new ImageIcon(
                    Objects.requireNonNull(getClass().getResource("/imgs/login-banner.jpg"))
            ).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                g2.dispose();
            }
        };
        leftPanel.setLayout(new MigLayout("fill,insets 0", "[grow]", "[grow]"));

        JPanel rightPanel = new JPanel(new MigLayout("fill,insets 40", "[grow,center]", "[grow]"));
        rightPanel.setBackground(new Color(245, 247, 250));

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        chRememberMe = new JCheckBox("Remember me");
        cmdLogin = new JButton("Login");

        JPanel shadowPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(0, 0, 0, 22));
                g2.fillRoundRect(8, 8, getWidth() - 16, getHeight() - 16, 28, 28);

                g2.dispose();
            }
        };
        shadowPanel.setOpaque(false);
        shadowPanel.setBorder(new EmptyBorder(6, 6, 12, 12));

        JPanel formPanel = new JPanel(new MigLayout("wrap,fillx,insets 30 38 30 38", "fill,320:340"));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(225, 230, 235), 1, true),
                new EmptyBorder(8, 8, 8, 8)
        ));
        formPanel.putClientProperty(FlatClientProperties.STYLE,
                "arc:24;" +
                        "background:#FFFFFF;");

        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tên đăng nhập");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Mật khẩu");

        txtUsername.setCaretColor(new Color(17, 24, 39));
        txtPassword.setCaretColor(new Color(17, 24, 39));

        txtUsername.putClientProperty(FlatClientProperties.STYLE,
                "arc:12;" +
                        "margin:8,20,8,14;" +
                        "background:#FFFFFF;" +
                        "foreground:#111827;" +
                        "borderWidth:1;" +
                        "borderColor:#D1D5DB;" +
                        "focusedBorderColor:#111111;" +
                        "focusWidth:0");

        txtPassword.putClientProperty(FlatClientProperties.STYLE,
                "arc:12;" +
                        "margin:8,20,8,14;" +
                        "background:#FFFFFF;" +
                        "foreground:#111827;" +
                        "borderWidth:1;" +
                        "borderColor:#D1D5DB;" +
                        "focusedBorderColor:#111111;" +
                        "focusWidth:0");

        txtPassword.putClientProperty("JTextField.trailingComponent", createPasswordToggle(txtPassword));

        chRememberMe.setOpaque(true);
        chRememberMe.setBackground(Color.WHITE);
        chRememberMe.setForeground(new Color(55, 65, 81));
        chRememberMe.setFocusPainted(false);
        chRememberMe.putClientProperty(FlatClientProperties.STYLE,
                "icon.borderWidth:1;" +
                        "icon.borderColor:#C7CDD4;" +
                        "icon.background:#FFFFFF;" +
                        "icon.selectedBackground:#FFFFFF;" +
                        "icon.checkmarkColor:#111111;");

        cmdLogin.putClientProperty(FlatClientProperties.STYLE,
                "arc:12;" +
                        "background:#111111;" +
                        "foreground:#FFFFFF;" +
                        "borderWidth:0;" +
                        "focusWidth:0;" +
                        "innerFocusWidth:0;");

        JLabel lbTitle = new JLabel("Chào mừng!");
        lbTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lbTitle.setForeground(new Color(17, 24, 39));

        JLabel description = new JLabel("Vui lòng đăng nhập để vào hệ thống!");
        description.setFont(new Font("SansSerif", Font.PLAIN, 14));
        description.setForeground(new Color(107, 114, 128));

        JLabel lbUsername = new JLabel("Tên đăng nhập");
        lbUsername.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbUsername.setForeground(new Color(31, 41, 55));

        JLabel lbPassword = new JLabel("Mật khẩu");
        lbPassword.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbPassword.setForeground(new Color(31, 41, 55));

        cmdLogin.addActionListener(e -> handleLogin());

        formPanel.add(lbTitle, "gapy 0 4");
        formPanel.add(description, "gapy 0 16");

        formPanel.add(lbUsername, "gapy 0 6");
        formPanel.add(txtUsername, "h 44!");

        formPanel.add(lbPassword, "gapy 8 6");
        formPanel.add(txtPassword, "h 44!");

        formPanel.add(chRememberMe, "gapy 8 0");
        formPanel.add(cmdLogin, "gapy 18 0, h 44!");

        shadowPanel.add(formPanel, BorderLayout.CENTER);
        rightPanel.add(shadowPanel, "center");

        add(leftPanel);
        add(rightPanel);
    }

    private JComponent createPasswordToggle(JPasswordField field) {
        char defaultEcho = field.getEchoChar();

        JToggleButton btn = new JToggleButton();
        btn.setFocusable(false);
        btn.setCursor(Cursor.getDefaultCursor());
        btn.setPreferredSize(new Dimension(34, 28));
        btn.setMinimumSize(new Dimension(34, 28));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.putClientProperty("JButton.buttonType", "roundRect");
        btn.putClientProperty(FlatClientProperties.STYLE,
                "arc:999;" +
                        "margin:0,0,0,0;" +
                        "minimumWidth:34;" +
                        "minimumHeight:28;" +
                        "background:#F9FAFB;" +
                        "foreground:#6B7280;" +
                        "hoverBackground:#F3F4F6;" +
                        "pressedBackground:#E5E7EB;" +
                        "borderWidth:0;" +
                        "focusWidth:0;" +
                        "innerFocusWidth:0;");

        Icon eyeIcon = new EyeToggleIcon();
        btn.setIcon(eyeIcon);
        btn.setSelectedIcon(eyeIcon);

        btn.addActionListener(e -> {
            boolean show = btn.isSelected();
            field.setEchoChar(show ? (char) 0 : defaultEcho);
        });

        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);
        wrap.setBorder(new EmptyBorder(0, 0, 0, 6));
        wrap.add(btn);

        return wrap;
    }

    private static class EyeToggleIcon implements Icon {
        private static final int SIZE = 16;

        @Override
        public int getIconWidth() {
            return SIZE;
        }

        @Override
        public int getIconHeight() {
            return SIZE;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            boolean selected = c instanceof AbstractButton && ((AbstractButton) c).isSelected();

            // giu 1 mau co dinh, khong doi sang trang khi click
            g2.setColor(new Color(107, 114, 128));
            g2.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            int cx = x + SIZE / 2;
            int cy = y + SIZE / 2;

            g2.drawArc(cx - 7, cy - 5, 14, 10, 0, 180);
            g2.drawArc(cx - 7, cy - 5, 14, 10, 180, 180);
            g2.fillOval(cx - 2, cy - 2, 4, 4);

            // dang an mat khau thi ve gach cheo
            if (!selected) {
                g2.drawLine(x + 3, y + 13, x + 13, y + 3);
            }

            g2.dispose();
        }
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = String.valueOf(txtPassword.getPassword());

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập");
            txtUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu");
            txtPassword.requestFocus();
            return;
        }

        JOptionPane.showMessageDialog(this, "Đăng nhập thành công (demo)");
    }
}