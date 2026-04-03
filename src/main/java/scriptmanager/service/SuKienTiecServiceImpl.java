package scriptmanager.service;

import java.time.LocalDateTime;

import scriptmanager.dao.SuKienTiecDao;
import scriptmanager.dao.SuKienTiecDaoImpl;
import scriptmanager.entity.core.SuKienTiec;

import java.util.List;

public class SuKienTiecServiceImpl implements SuKienTiecService {
    private final SuKienTiecDao dao;

    public SuKienTiecServiceImpl() {
        this.dao = new SuKienTiecDaoImpl();
    }

    @Override
    public List<SuKienTiec> findAll() { return dao.findAll(); }

    @Override
    public SuKienTiec findById(int id) { return dao.findById(id); }

    @Override
    public boolean isDuplicate(int excludeId, String tenSuKien, LocalDateTime thoiGianToChuc, String diaDiem, int maNguoiDung) {
        Integer excluded = excludeId > 0 ? excludeId : null;
        return dao.existsDuplicate(tenSuKien, thoiGianToChuc, diaDiem, maNguoiDung, excluded);
    }

    @Override
    public void save(SuKienTiec item) {
        validateBusinessRules(item, 0);
        dao.save(item);
    }

    @Override
    public void update(SuKienTiec item) {
        validateBusinessRules(item, item.getMaSK());
        dao.update(item);
    }

    @Override
    public void delete(int id) { dao.deleteById(id); }

    private void validateBusinessRules(SuKienTiec item, int excludeId) {
        validateTime(item);
        validateDuplicate(item, excludeId);
    }

    private void validateTime(SuKienTiec item) {
        if (item == null) {
            throw new IllegalArgumentException("Dữ liệu sự kiện không hợp lệ.");
        }

        LocalDateTime thoiGian = item.getThoiGianToChuc();
        if (thoiGian == null) {
            throw new IllegalArgumentException("Thời gian tổ chức không được để trống.");
        }

        if (thoiGian.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Không thể thêm/cập nhật sự kiện có thời gian trong quá khứ.");
        }
    }

    private void validateDuplicate(SuKienTiec item, int excludeId) {
        if (item == null || item.getNguoiDung() == null) {
            throw new IllegalArgumentException("Dữ liệu sự kiện không hợp lệ.");
        }

        boolean duplicated = isDuplicate(
                excludeId,
                item.getTenSuKien(),
                item.getThoiGianToChuc(),
                item.getDiaDiem(),
                item.getNguoiDung().getMaND()
        );

        if (duplicated) {
            throw new IllegalArgumentException("Sự kiện đã tồn tại (trùng tên, thời gian, địa điểm, người phụ trách).");
        }
    }
}