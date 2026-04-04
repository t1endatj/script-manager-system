package scriptmanager.service;

import scriptmanager.dao.NhanSuDao;
import scriptmanager.dao.NhanSuDaoImpl;
import scriptmanager.entity.user.NhanSu;
import scriptmanager.exception.BusinessRuleException;
import scriptmanager.exception.ValidationException;

import java.util.List;
import java.util.Locale;

public class NhanSuService {
    private final NhanSuDao nhanSuDao;

    public NhanSuService() {
        this.nhanSuDao = new NhanSuDaoImpl();
    }

    public List<NhanSu> findAll() {
        return nhanSuDao.findAll();
    }

    public NhanSu findById(int id) {
        return nhanSuDao.findById(id);
    }

    public void save(NhanSu item) {
        AuthorizationService.requireManagerOrAdmin();
        validateForSave(item);
        nhanSuDao.save(item);
    }

    public void update(NhanSu item) {
        AuthorizationService.requireManagerOrAdmin();
        validateForUpdate(item);
        nhanSuDao.update(item);
    }

    public void delete(int id) {
        AuthorizationService.requireManagerOrAdmin();
        NhanSu item = nhanSuDao.findById(id);
        if (item != null) {
            // Không cho xóa nhân sự nếu vẫn đang được phân công trong sự kiện/hạng mục.
            if (nhanSuDao.hasAssignments(id)) {
                throw new BusinessRuleException("Không thể xóa nhân sự đang phục vụ sự kiện. Vui lòng gỡ phân công trước.");
            }
            nhanSuDao.delete(item);
        }
    }

    private void validateForSave(NhanSu item) {
        if (item == null || item.getTenNS() == null || item.getTenNS().trim().isEmpty()) {
            throw new ValidationException("Tên nhân sự không được để trống.");
        }

        normalize(item);
        if (isDuplicate(item, null)) {
            throw new BusinessRuleException("Nhân sự đã tồn tại.");
        }
    }

    private void validateForUpdate(NhanSu item) {
        if (item == null || item.getTenNS() == null || item.getTenNS().trim().isEmpty()) {
            throw new ValidationException("Tên nhân sự không được để trống.");
        }

        normalize(item);
        if (isDuplicate(item, item.getMaNS())) {
            throw new BusinessRuleException("Nhân sự đã tồn tại.");
        }
    }

    private void normalize(NhanSu item) {
        item.setTenNS(item.getTenNS().trim());
        item.setSdt(item.getSdt() == null ? "" : item.getSdt().trim());
        item.setVaiTro(item.getVaiTro() == null ? "" : item.getVaiTro().trim());
    }

    private boolean isDuplicate(NhanSu item, Integer excludeId) {
        String ten = normalizeCompareValue(item.getTenNS());
        String sdt = normalizeCompareValue(item.getSdt());

        for (NhanSu existing : nhanSuDao.findAll()) {
            if (excludeId != null && existing.getMaNS() == excludeId) {
                continue;
            }
            String existingTen = normalizeCompareValue(existing.getTenNS());
            String existingSdt = normalizeCompareValue(existing.getSdt());
            if (ten.equals(existingTen) && sdt.equals(existingSdt)) {
                return true;
            }
        }
        return false;
    }

    private String normalizeCompareValue(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }
}
