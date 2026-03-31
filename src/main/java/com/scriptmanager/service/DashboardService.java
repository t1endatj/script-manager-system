package com.scriptmanager.service;

import com.scriptmanager.dao.DashboardDao;
import com.scriptmanager.dto.DashboardStatsDTO;

public class DashboardService {
    private final DashboardDao dashboardDao;

    public DashboardService() {
        this.dashboardDao = new DashboardDao();
    }

    public DashboardStatsDTO getDashboardStats() {
        return dashboardDao.getStats();
    }
}
