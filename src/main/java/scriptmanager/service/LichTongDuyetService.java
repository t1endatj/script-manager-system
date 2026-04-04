package scriptmanager.service;

import scriptmanager.config.UserSession;
import scriptmanager.dao.LichTongDuyetDao;
import scriptmanager.dao.LichTongDuyetDaoImpl;
import scriptmanager.entity.core.LichTongDuyet;
import scriptmanager.enums.UserRole;
import scriptmanager.exception.AuthorizationException;
import scriptmanager.exception.BusinessRuleException;
import scriptmanager.exception.ValidationException;

import java.util.List;

public class LichTongDuyetService {
    private final LichTongDuyetDao lichTongDuyetDao;

    public LichTongDuyetService() {
        this.lichTongDuyetDao = new LichTongDuyetDaoImpl();
    }

    public List<LichTongDuyet> findAll() {
        Integer currentUserId = UserSession.getCurrentUserId();
        if (UserSession.getCurrentRole() == UserRole.USER && currentUserId != null) {
            return lichTongDuyetDao.findByNguoiDungId(currentUserId);
        }
        return lichTongDuyetDao.findAll();
    }

    public LichTongDuyet findById(int id) {
        return lichTongDuyetDao.findById(id);
    }

    public void save(LichTongDuyet item) {
        enforceOwnership(item);
        validate(item);
        lichTongDuyetDao.save(item);
    }

    public void update(LichTongDuyet item) {
        enforceOwnership(item);
        validate(item);
        lichTongDuyetDao.update(item);
    }

    public void delete(int id) {
        if (UserSession.getCurrentRole() == UserRole.USER) {
            LichTongDuyet existing = lichTongDuyetDao.findById(id);
            Integer currentUserId = UserSession.getCurrentUserId();
            if (existing == null || existing.getSuKienTiec() == null || existing.getSuKienTiec().getNguoiDung() == null
                    || currentUserId == null || existing.getSuKienTiec().getNguoiDung().getMaND() != currentUserId) {
                throw new AuthorizationException("Bạn không có quyền xóa lịch tổng duyệt này.");
            }
        }
        LichTongDuyet item = lichTongDuyetDao.findById(id);
        if (item != null) {
            lichTongDuyetDao.delete(item);
        }
    }

    private void enforceOwnership(LichTongDuyet item) {
        if (UserSession.getCurrentRole() != UserRole.USER) {
            return;
        }

        Integer currentUserId = UserSession.getCurrentUserId();
        if (currentUserId == null || item == null || item.getSuKienTiec() == null
                || item.getSuKienTiec().getNguoiDung() == null
                || item.getSuKienTiec().getNguoiDung().getMaND() != currentUserId) {
            throw new AuthorizationException("Bạn chỉ có thể thao tác lịch tổng duyệt của sự kiện do mình phụ trách.");
        }
    }

    private void validate(LichTongDuyet item) {
        if (item == null || item.getSuKienTiec() == null) {
            throw new ValidationException("Dữ liệu lịch tổng duyệt không hợp lệ.");
        }

        if (item.getThoiGianDuyet() == null) {
            throw new ValidationException("Thời gian duyệt không được để trống.");
        }

        if (item.getSuKienTiec().getThoiGianToChuc() != null
                && item.getThoiGianDuyet().isAfter(item.getSuKienTiec().getThoiGianToChuc())) {
            throw new BusinessRuleException("Thời gian tổng duyệt phải trước hoặc bằng thời gian tổ chức sự kiện.");
        }

        item.setNoiDungDuyet(item.getNoiDungDuyet() == null ? "" : item.getNoiDungDuyet().trim());
        item.setTrangThai(item.getTrangThai() == null ? "" : item.getTrangThai().trim());
    }
}
