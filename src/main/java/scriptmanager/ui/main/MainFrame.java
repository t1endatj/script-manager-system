package scriptmanager.ui.main;

import scriptmanager.ui.dashboard.Dashboard;
import scriptmanager.ui.login.Login;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        init();
    }

    private void init() {
        setTitle("Script Manager System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1200, 700));
        setLocationRelativeTo(null);

        showLogin();
    }

    public void showLogin() {
        setContentPane(new Login(this));
        revalidate();
        repaint();
    }

    public void showDashboard() {
        setContentPane(new Dashboard(this));
        revalidate();
        repaint();
    }


}