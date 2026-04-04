package scriptmanager.service;

import java.time.LocalDateTime;

import scriptmanager.config.UserSession;
import scriptmanager.dao.SuKienTiecDao;
import scriptmanager.dao.SuKienTiecDaoImpl;
import scriptmanager.enums.UserRole;
import scriptmanager.entity.core.LichTongDuyet;
import scriptmanager.entity.core.SuKienTiec;
import scriptmanager.exception.AuthorizationException;
import scriptmanager.exception.BusinessRuleException;
import scriptmanager.exception.ValidationException;

import java.util.List;

public class SuKienTiecServiceImpl implements SuKienTiecService {
    private final SuKienTiecDao dao;

    public SuKienTiecServiceImpl() {
        this.dao = new SuKienTiecDaoImpl();
    }

    @Override
    public List<SuKienTiec> findAll() {
        Integer currentUserId = UserSession.getCurrentUserId();
        if (UserSession.getCurrentRole() == UserRole.USER && currentUserId != null) {
            return dao.findByNguoiDungId(currentUserId);
        }
        return dao.findAll();
    }

    @Override
    public SuKienTiec findById(int id) { return dao.findById(id); }

    @Override
    public boolean isDuplicate(int excludeId, String tenSuKien, LocalDateTime thoiGianToChuc, String diaDiem, int maNguoiDung) {
        Integer excluded = excludeId > 0 ? excludeId : null;
        return dao.existsDuplicate(tenSuKien, thoiGianToChuc, diaDiem, maNguoiDung, excluded);
    }

    @Override
    public void save(SuKienTiec item) {
        enforceOwnership(item);
        // Luôn kiểm tra rule trước khi lưu mới sự kiện.
        validateBusinessRules(item, 0);
        addDefaultRehearsal(item);
        dao.save(item);
    }

    @Override
    public void update(SuKienTiec item) {
        enforceOwnership(item);
        // Cập nhật vẫn phải qua cùng bộ rule nghiệp vụ.
        validateBusinessRules(item, item.getMaSK());
        dao.update(item);
    }

    @Override
    public void delete(int id) {
        if (UserSession.getCurrentRole() == UserRole.USER) {
            SuKienTiec existing = dao.findById(id);
            Integer currentUserId = UserSession.getCurrentUserId();
            if (existing == null || currentUserId == null || existing.getNguoiDung() == null
                    || existing.getNguoiDung().getMaND() != currentUserId) {
                throw new AuthorizationException("Bạn không có quyền xóa sự kiện này.");
            }
        }
        dao.deleteById(id);
    }

    private void enforceOwnership(SuKienTiec item) {
        if (UserSession.getCurrentRole() != UserRole.USER) {
            return;
        }

        Integer currentUserId = UserSession.getCurrentUserId();
        if (currentUserId == null || item == null || item.getNguoiDung() == null
                || item.getNguoiDung().getMaND() != currentUserId) {
            throw new AuthorizationException("Bạn chỉ có thể thao tác với sự kiện của chính mình.");
        }
    }

    private void validateBusinessRules(SuKienTiec item, int excludeId) {
        validateTime(item);
        validateDuplicate(item, excludeId);
    }

    private void validateTime(SuKienTiec item) {
        if (item == null) {
            throw new ValidationException("Dữ liệu sự kiện không hợp lệ.");
        }

        LocalDateTime thoiGian = item.getThoiGianToChuc();
        if (thoiGian == null) {
            throw new ValidationException("Thời gian tổ chức không được để trống.");
        }

        // Không cho phép tạo/sửa sự kiện lùi về quá khứ.
        if (thoiGian.isBefore(LocalDateTime.now())) {
            throw new BusinessRuleException("Không thể thêm/cập nhật sự kiện có thời gian trong quá khứ.");
        }
    }

    private void validateDuplicate(SuKienTiec item, int excludeId) {
        if (item == null || item.getNguoiDung() == null) {
            throw new ValidationException("Dữ liệu sự kiện không hợp lệ.");
        }

        boolean duplicated = isDuplicate(
                excludeId,
                item.getTenSuKien(),
                item.getThoiGianToChuc(),
                item.getDiaDiem(),
                item.getNguoiDung().getMaND()
        );

        // Chặn trùng đầy đủ theo tên + thời gian + địa điểm + phụ trách.
        if (duplicated) {
            throw new BusinessRuleException("Sự kiện đã tồn tại (trùng tên, thời gian, địa điểm, người phụ trách).");
        }
    }

    private void addDefaultRehearsal(SuKienTiec item) {
        if (item == null || item.getThoiGianToChuc() == null || !item.getLichTongDuyets().isEmpty()) {
            return;
        }

        // Tạo lịch tổng duyệt mặc định khi thêm sự kiện mới.
        LocalDateTime rehearsalTime = item.getThoiGianToChuc().minusDays(1);
        if (rehearsalTime.isBefore(LocalDateTime.now())) {
            rehearsalTime = LocalDateTime.now().plusHours(1);
        }

        LichTongDuyet draft = new LichTongDuyet();
        draft.setThoiGianDuyet(rehearsalTime);
        draft.setNoiDungDuyet("Tổng duyệt mặc định cho sự kiện mới.");
        draft.setTrangThai("Chưa duyệt");
        item.addLichTongDuyet(draft);
    }
}