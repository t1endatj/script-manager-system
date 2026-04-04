package scriptmanager.ui.main;

import scriptmanager.config.RememberMeStore;
import scriptmanager.config.UserSession;
import scriptmanager.entity.user.NguoiDung;
import scriptmanager.service.NguoiDungService;
import scriptmanager.ui.dashboard.Dashboard;
import scriptmanager.ui.hangmuc.HangMucKichBanPanel;
import scriptmanager.ui.login.Login;
import scriptmanager.ui.sukien.SuKienTiecPanel;
import scriptmanager.ui.user.NguoiDungPanel;
import scriptmanager.ui.coordination.CoordinationPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final NguoiDungService nguoiDungService = new NguoiDungService();
    private NguoiDung currentUser;

    public MainFrame() {
        init();
    }

    private void init() {
        setTitle("Script Manager System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1200, 700));
        setLocationRelativeTo(null);

        if (RememberMeStore.isRemembered()) {
            String rememberedUsername = RememberMeStore.getRememberedUsername();
            NguoiDung rememberedUser = nguoiDungService.getByUsername(rememberedUsername);
            if (rememberedUser != null) {
                setCurrentUser(rememberedUser);
                showDashboard();
                return;
            }
            RememberMeStore.clearRememberedUser();
        }
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

    public NguoiDung getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(NguoiDung currentUser) {
        this.currentUser = currentUser;
        UserSession.setCurrentUser(currentUser);
    }

    public void showUserManager() {
        setContentPane(new NguoiDungPanel(this));
        revalidate();
        repaint();
    }

    public void showSuKienManager() {
        setContentPane(new SuKienTiecPanel(this));
        revalidate();
        repaint();
    }

    public void showHangMucManagerForEvent(Integer eventId) {
        scriptmanager.ui.hangmuc.HangMucKichBanPanel panel = new scriptmanager.ui.hangmuc.HangMucKichBanPanel(this, eventId);
        setContentPane(panel);
        revalidate();
        repaint();
        if (eventId != null) {
            panel.selectEvent(eventId);
        }
    }

    public void showHangMucManager() {
        showHangMucManager(null);
    }

    public void showHangMucManager(Integer hangMucId) {
        scriptmanager.ui.hangmuc.HangMucKichBanPanel panel = new scriptmanager.ui.hangmuc.HangMucKichBanPanel(this, null);
        setContentPane(panel);
        revalidate();
        repaint();
        if (hangMucId != null) {
            panel.selectAndEdit(hangMucId);
        }
    }

    public void showCoordinationManager() {
        setContentPane(new CoordinationPanel(this));
        revalidate();
        repaint();
    }

    public void showExtendedModules() {
        setContentPane(new ExtendedModulePanel(this));
        revalidate();
        repaint();
    }

    public void logout() {
        RememberMeStore.clearRememberedUser();
        currentUser = null;
        UserSession.clear();
        showLogin();
    }
}
