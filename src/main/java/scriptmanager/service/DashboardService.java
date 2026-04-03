package scriptmanager.service;

import scriptmanager.dao.DashboardDao;
import scriptmanager.dto.DashboardResourceAlertDTO;
import scriptmanager.dto.DashboardStatsDTO;
import scriptmanager.dto.DashboardTaskItemDTO;
import scriptmanager.dto.DashboardTimelineItemDTO;

import java.util.List;

public class DashboardService {
    private final DashboardDao dashboardDao;

    public DashboardService() {
        this.dashboardDao = new DashboardDao();
    }

    public DashboardStatsDTO getDashboardStats() {
        return dashboardDao.getStats();
    }

    public List<DashboardTimelineItemDTO> getLatestRehearsals(int limit) {
        return dashboardDao.getLatestRehearsals(limit);
    }

    public List<DashboardResourceAlertDTO> getResourceAlerts(int limit) {
        return dashboardDao.getResourceAlerts(limit);
    }

    public List<DashboardTaskItemDTO> getPendingTasks(int limit) {
        return dashboardDao.getPendingTasks(limit);
    }
}
