package com.scriptmanager.dao;

import com.scriptmanager.config.HibernateUtil;
import com.scriptmanager.dto.DashboardStatsDTO;
import org.hibernate.Session;

public class DashboardDao {
    public DashboardStatsDTO getStats() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            long suKienSapToi = (long) session.createQuery("SELECT COUNT(s) FROM SuKienTiec s WHERE s.thoiGianToChuc >= CURRENT_TIMESTAMP").uniqueResult();
            long tongDuyetChuaXong = (long) session.createQuery("SELECT COUNT(l) FROM LichTongDuyet l WHERE l.trangThai != 'Hoàn thành'").uniqueResult();
            long thietBiNguyCoThieu = (long) session.createQuery("SELECT COUNT(t) FROM ThietBi t WHERE t.soLuong < 5").uniqueResult();
            long nhanSuDaPhanCong = (long) session.createQuery("SELECT COUNT(DISTINCT p.id.maNS) FROM PhanCongNhanSu p").uniqueResult();
            return new DashboardStatsDTO(suKienSapToi, tongDuyetChuaXong, thietBiNguyCoThieu, nhanSuDaPhanCong);
        }
    }
}
