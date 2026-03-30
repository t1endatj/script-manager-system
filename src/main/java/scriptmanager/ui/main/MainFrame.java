package scriptmanager.ui.main;

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

        // Trang đầu tiên
        setContentPane(new Login());

    }
}