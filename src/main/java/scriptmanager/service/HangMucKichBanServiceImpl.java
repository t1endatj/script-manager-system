package scriptmanager.service;

import scriptmanager.config.UserSession;
import scriptmanager.dao.HangMucKichBanDao;
import scriptmanager.dao.HangMucKichBanDaoImpl;
import scriptmanager.entity.core.HangMucKichBan;
import scriptmanager.enums.UserRole;
import scriptmanager.exception.AuthorizationException;
import scriptmanager.exception.BusinessRuleException;
import scriptmanager.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<HangMucKichBan> findBySuKienId(int suKienId) {
        return findAll().stream()
                .filter(hm -> hm.getSuKienTiec() != null && hm.getSuKienTiec().getMaSK() == suKienId)
                .collect(Collectors.toList());
    }

    @Override
    public HangMucKichBan findById(int id) { return dao.findById(id); }

    @Override
    public void save(HangMucKichBan item) {
        enforceOwnership(item);
        // Bắt buộc đúng rule dữ liệu trước khi lưu.
        validate(item);
        dao.save(item);
    }

    @Override
    public void update(HangMucKichBan item) {
        enforceOwnership(item);
        // Áp lại rule khi cập nhật để tránh dữ liệu bẩn.
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
                throw new AuthorizationException("Bạn không có quyền xóa hạng mục này.");
            }
        }
        HangMucKichBan item = dao.findById(id);
        if (item != null) {
            dao.deleteByIdWithDependencies(id);
        }
    }

    private void enforceOwnership(HangMucKichBan item) {
        if (UserSession.getCurrentRole() != UserRole.USER) {
            return;
        }

        // USER chỉ được thao tác hạng mục thuộc sự kiện mình phụ trách.
        Integer currentUserId = UserSession.getCurrentUserId();
        if (currentUserId == null || item == null || item.getSuKienTiec() == null
                || item.getSuKienTiec().getNguoiDung() == null
                || item.getSuKienTiec().getNguoiDung().getMaND() != currentUserId) {
            throw new AuthorizationException("Bạn chỉ có thể thao tác hạng mục của sự kiện do mình phụ trách.");
        }
    }

    private void validate(HangMucKichBan item) {
        if (item == null || item.getSuKienTiec() == null) {
            throw new ValidationException("Dữ liệu hạng mục không hợp lệ.");
        }

        if (item.getTenHM() == null || item.getTenHM().trim().isEmpty()) {
            throw new ValidationException("Tên hạng mục không được để trống.");
        }

        if (item.getTgBatDau() == null || item.getTgKetThuc() == null) {
            throw new ValidationException("Thời gian bắt đầu và kết thúc không được để trống.");
        }

        // Mốc bắt đầu luôn phải nhỏ hơn mốc kết thúc.
        if (!item.getTgBatDau().isBefore(item.getTgKetThuc())) {
            throw new BusinessRuleException("Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc.");
        }

        // Hạng mục phải bám theo thời điểm tổ chức sự kiện để tránh lệch lịch.
        LocalDateTime eventTime = item.getSuKienTiec().getThoiGianToChuc();
        if (eventTime == null) {
            throw new ValidationException("Sự kiện chưa có thời gian tổ chức hợp lệ.");
        }

        if (!item.getTgBatDau().toLocalDate().equals(eventTime.toLocalDate())
                || !item.getTgKetThuc().toLocalDate().equals(eventTime.toLocalDate())) {
            throw new BusinessRuleException("Thời gian hạng mục phải cùng ngày với thời gian tổ chức sự kiện.");
        }

        if (item.getTgBatDau().isBefore(eventTime)) {
            throw new BusinessRuleException("Thời gian bắt đầu hạng mục không được trước thời gian tổ chức sự kiện.");
        }

        // Không cho phép tạo hạng mục trùng trong cùng một sự kiện.
        boolean duplicated = findBySuKienId(item.getSuKienTiec().getMaSK()).stream()
                .filter(existing -> item.getMaHM() <= 0 || existing.getMaHM() != item.getMaHM())
                .anyMatch(existing -> normalize(existing.getTenHM()).equals(normalize(item.getTenHM()))
                        && existing.getTgBatDau() != null
                        && existing.getTgKetThuc() != null
                        && existing.getTgBatDau().equals(item.getTgBatDau())
                        && existing.getTgKetThuc().equals(item.getTgKetThuc()));
        if (duplicated) {
            throw new BusinessRuleException("Hạng mục đã tồn tại trong sự kiện với cùng tên và khung thời gian.");
        }

        item.setTenHM(item.getTenHM().trim());
        item.setNoiDung(item.getNoiDung() == null ? "" : item.getNoiDung().trim());
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }
}
