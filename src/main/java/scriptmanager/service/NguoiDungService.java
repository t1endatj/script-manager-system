package scriptmanager.service;

import scriptmanager.dao.NguoiDungDao;
import scriptmanager.dao.NguoiDungDaoImpl;
import scriptmanager.entity.user.NguoiDung;

import java.util.List;

public class NguoiDungService {
    private final NguoiDungDao nguoiDungDao;

    public NguoiDungService() {
        this.nguoiDungDao = new NguoiDungDaoImpl();
    }

    public List<NguoiDung> getAllNguoiDung() {
        return nguoiDungDao.findAll();
    }

    public NguoiDung getByUsername(String username) {
        return nguoiDungDao.findByUsername(username);
    }

    public NguoiDung getById(Integer id) {
        return nguoiDungDao.findById(id);
    }

    public void save(NguoiDung nguoiDung) {
        AuthorizationService.requireAdmin();
        validateUsernameForSave(nguoiDung);
        nguoiDungDao.save(nguoiDung);
    }

    public void update(NguoiDung nguoiDung) {
        AuthorizationService.requireAdmin();
        validateUsernameForUpdate(nguoiDung);
        nguoiDungDao.update(nguoiDung);
    }

    public void delete(Integer id) {
        AuthorizationService.requireAdmin();
        NguoiDung nguoiDung = nguoiDungDao.findById(id);
        if (nguoiDung != null) {
            nguoiDungDao.delete(nguoiDung);
        }
    }

    private void validateUsernameForSave(NguoiDung nguoiDung) {
        if (nguoiDung == null || nguoiDung.getTenDangNhap() == null || nguoiDung.getTenDangNhap().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên đăng nhập không được để trống.");
        }

        String normalizedUsername = nguoiDung.getTenDangNhap().trim();
        NguoiDung existed = nguoiDungDao.findByUsername(normalizedUsername);
        if (existed != null) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại.");
        }

        nguoiDung.setTenDangNhap(normalizedUsername);
    }

    private void validateUsernameForUpdate(NguoiDung nguoiDung) {
        if (nguoiDung == null || nguoiDung.getTenDangNhap() == null || nguoiDung.getTenDangNhap().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên đăng nhập không được để trống.");
        }

        String normalizedUsername = nguoiDung.getTenDangNhap().trim();
        NguoiDung existed = nguoiDungDao.findByUsername(normalizedUsername);
        if (existed != null && existed.getMaND() != nguoiDung.getMaND()) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại.");
        }

        nguoiDung.setTenDangNhap(normalizedUsername);
    }
}
