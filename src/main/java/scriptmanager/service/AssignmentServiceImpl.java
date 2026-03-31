package scriptmanager.service;

import scriptmanager.config.HibernateUtil;
import scriptmanager.dao.GenericDao;
import scriptmanager.dao.GenericDaoImpl;
import scriptmanager.entity.assignment.PhanCongNhanSu;
import scriptmanager.entity.assignment.PhanCongThietBi;
import scriptmanager.entity.assignment.SuDungHieuUng;
import scriptmanager.entity.assignment.pk.PhanCongNhanSuId;
import scriptmanager.entity.assignment.pk.PhanCongThietBiId;
import scriptmanager.entity.assignment.pk.SuDungHieuUngId;
import scriptmanager.entity.core.HangMucKichBan;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class AssignmentServiceImpl implements AssignmentService {

    private final GenericDao<PhanCongNhanSu, PhanCongNhanSuId> nhanSuDao = 
            new GenericDaoImpl<PhanCongNhanSu, PhanCongNhanSuId>(PhanCongNhanSu.class) {};
            
    private final GenericDao<PhanCongThietBi, PhanCongThietBiId> thietBiDao = 
            new GenericDaoImpl<PhanCongThietBi, PhanCongThietBiId>(PhanCongThietBi.class) {};
            
    private final GenericDao<SuDungHieuUng, SuDungHieuUngId> hieuUngDao = 
            new GenericDaoImpl<SuDungHieuUng, SuDungHieuUngId>(SuDungHieuUng.class) {};

    @Override
    public void assignNhanSu(PhanCongNhanSu pcns) {
        nhanSuDao.save(pcns);
    }

    @Override
    public void assignThietBi(PhanCongThietBi pctb) {
        thietBiDao.save(pctb);
    }

    @Override
    public void assignHieuUng(SuDungHieuUng sdhu) {
        hieuUngDao.save(sdhu);
    }

    @Override
    public List<String> checkConflicts(HangMucKichBan hangMuc) {
        List<String> conflicts = new ArrayList<>();
        if (hangMuc.getTgBatDau() == null || hangMuc.getTgKetThuc() == null) return conflicts;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            for (PhanCongNhanSu pc : hangMuc.getPhanCongNhanSus()) {
                String hql = "SELECT hm FROM HangMucKichBan hm " +
                             "JOIN hm.phanCongNhanSus pcns " +
                             "WHERE pcns.nhanSu.maNS = :maNS " +
                             "AND hm.maHM != :currentHM " +
                             "AND (:start < hm.tgKetThuc AND :end > hm.tgBatDau)";
                
                Query<HangMucKichBan> query = session.createQuery(hql, HangMucKichBan.class);
                query.setParameter("maNS", pc.getNhanSu().getMaNS());
                query.setParameter("currentHM", hangMuc.getMaHM());
                query.setParameter("start", hangMuc.getTgBatDau());
                query.setParameter("end", hangMuc.getTgKetThuc());

                List<HangMucKichBan> overlapHMs = query.list();
                if (!overlapHMs.isEmpty()) {
                    conflicts.add("Nhân sự " + pc.getNhanSu().getTenNS() + 
                                  " bị trùng lịch với hạng mục: " + overlapHMs.get(0).getTenHM());
                }
            }

            for (PhanCongThietBi pc : hangMuc.getPhanCongThietBis()) {
                String hqlSum = "SELECT SUM(pctb.soLuongSuDung) FROM PhanCongThietBi pctb " +
                                "JOIN pctb.hangMuc hm " +
                                "WHERE pctb.thietBi.maTB = :maTB " +
                                "AND hm.maHM != :currentHM " +
                                "AND (:start < hm.tgKetThuc AND :end > hm.tgBatDau)";
                
                Query<Long> querySum = session.createQuery(hqlSum, Long.class);
                querySum.setParameter("maTB", pc.getThietBi().getMaTB());
                querySum.setParameter("currentHM", hangMuc.getMaHM());
                querySum.setParameter("start", hangMuc.getTgBatDau());
                querySum.setParameter("end", hangMuc.getTgKetThuc());

                Long usedQuantity = querySum.uniqueResult();
                if (usedQuantity == null) usedQuantity = 0L;
                
                int totalAvailable = pc.getThietBi().getSoLuong();
                if (usedQuantity + pc.getSoLuongSuDung() > totalAvailable) {
                    conflicts.add("Thiết bị " + pc.getThietBi().getTenTB() + 
                                  " không đủ số lượng (Cần: " + (usedQuantity + pc.getSoLuongSuDung()) + 
                                  ", Hiện có: " + totalAvailable + ")");
                }
            }
        }
        return conflicts;
    }
}
