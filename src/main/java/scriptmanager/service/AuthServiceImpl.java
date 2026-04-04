package scriptmanager.service;

import scriptmanager.dao.NguoiDungDao;
import scriptmanager.dao.NguoiDungDaoImpl;
import scriptmanager.entity.user.NguoiDung;
import scriptmanager.enums.UserRole;

public class AuthServiceImpl implements AuthService {
    private final NguoiDungDao nguoiDungDao;

    public AuthServiceImpl() {
        this.nguoiDungDao = new NguoiDungDaoImpl();
    }

    @Override
    public NguoiDung login(String username, String password) {
        NguoiDung user = nguoiDungDao.findByUsername(username);
        if (user != null && user.getMatKhau() != null && user.getMatKhau().equals(password)) {
            String normalizedRole = UserRole.fromDb(user.getQuyenHan()).name();
            if (!normalizedRole.equalsIgnoreCase(user.getQuyenHan())) {
                user.setQuyenHan(normalizedRole);
                nguoiDungDao.update(user);
            }
            return user;
        }
        return null;
    }
}
