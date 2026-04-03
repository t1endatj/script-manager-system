package scriptmanager.app;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import scriptmanager.ui.main.MainFrame;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("themes");
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));


        UIManager.put("Panel.background", new Color(244, 241, 237));
        UIManager.put("TextField.background", new Color(251, 250, 248));
        UIManager.put("TextField.foreground", new Color(46, 42, 39));
        UIManager.put("TextField.caretForeground", new Color(46, 42, 39));
        UIManager.put("Component.focusColor", new Color(111, 134, 166));
        UIManager.put("Component.borderColor", new Color(216, 211, 204));
        UIManager.put("Button.background", new Color(233, 228, 222));
        UIManager.put("Button.foreground", new Color(46, 42, 39));
        UIManager.put("Table.selectionBackground", new Color(231, 225, 218));
        UIManager.put("Table.selectionForeground", new Color(46, 42, 39));

        FlatMacLightLaf.setup();

        EventQueue.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}