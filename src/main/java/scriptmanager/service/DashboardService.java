package scriptmanager.service;

import scriptmanager.dao.DashboardDao;
import scriptmanager.dto.DashboardStatsDTO;

public class DashboardService {
    private final DashboardDao dashboardDao;

    public DashboardService() {
        this.dashboardDao = new DashboardDao();
    }

    public DashboardStatsDTO getDashboardStats() {
        return dashboardDao.getStats();
    }
}
