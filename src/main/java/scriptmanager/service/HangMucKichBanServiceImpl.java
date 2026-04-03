package scriptmanager.service;

import scriptmanager.config.UserSession;
import scriptmanager.dao.HangMucKichBanDao;
import scriptmanager.dao.HangMucKichBanDaoImpl;
import scriptmanager.enums.UserRole;
import scriptmanager.entity.core.HangMucKichBan;

import java.util.List;

public class HangMucKichBanServiceImpl implements HangMucKichBanService {
    private final HangMucKichBanDao dao;

    public HangMucKichBanServiceImpl() {
        this.dao = new HangMucKichBanDaoImpl();
    }

    @Override
    public List<HangMucKichBan> findAll() {
        Integer currentUserId = UserSession.getCurrentUserId();
        if (UserSession.getCurrentRole() == UserRole.USER && currentUserId != null) {
            return dao.findByNguoiDungId(currentUserId);
        }
        return dao.findAll();
    }

    @Override
    public HangMucKichBan findById(int id) { return dao.findById(id); }

    @Override
    public void save(HangMucKichBan item) {
        enforceOwnership(item);
        validate(item);
        dao.save(item);
    }

    @Override
    public void update(HangMucKichBan item) {
        enforceOwnership(item);
        validate(item);
        dao.update(item);
    }

    @Override
    public void delete(int id) {
        if (UserSession.getCurrentRole() == UserRole.USER) {
            HangMucKichBan existing = dao.findById(id);
            Integer currentUserId = UserSession.getCurrentUserId();
            if (existing == null || existing.getSuKienTiec() == null || existing.getSuKienTiec().getNguoiDung() == null
                    || currentUserId == null || existing.getSuKienTiec().getNguoiDung().getMaND() != currentUserId) {
                throw new SecurityException("Bạn không có quyền xóa hạng mục này.");
            }
        }
        HangMucKichBan item = dao.findById(id);
        if (item != null) dao.delete(item);
    }

    private void enforceOwnership(HangMucKichBan item) {
        if (UserSession.getCurrentRole() != UserRole.USER) {
            return;
        }

        Integer currentUserId = UserSession.getCurrentUserId();
        if (currentUserId == null || item == null || item.getSuKienTiec() == null
                || item.getSuKienTiec().getNguoiDung() == null
                || item.getSuKienTiec().getNguoiDung().getMaND() != currentUserId) {
            throw new SecurityException("Bạn chỉ có thể thao tác hạng mục của sự kiện do mình phụ trách.");
        }
    }

    private void validate(HangMucKichBan item) {
        if (item == null || item.getSuKienTiec() == null) {
            throw new IllegalArgumentException("Dữ liệu hạng mục không hợp lệ.");
        }

        if (item.getTenHM() == null || item.getTenHM().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên hạng mục không được để trống.");
        }

        if (item.getTgBatDau() == null || item.getTgKetThuc() == null) {
            throw new IllegalArgumentException("Thời gian bắt đầu và kết thúc không được để trống.");
        }

        if (!item.getTgBatDau().isBefore(item.getTgKetThuc())) {
            throw new IllegalArgumentException("Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc.");
        }

        item.setTenHM(item.getTenHM().trim());
        item.setNoiDung(item.getNoiDung() == null ? "" : item.getNoiDung().trim());
    }
}