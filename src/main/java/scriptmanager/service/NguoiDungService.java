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
        nguoiDungDao.save(nguoiDung);
    }

    public void update(NguoiDung nguoiDung) {
        nguoiDungDao.update(nguoiDung);
    }

    public void delete(Integer id) {
        NguoiDung nguoiDung = nguoiDungDao.findById(id);
        if (nguoiDung != null) {
            nguoiDungDao.delete(nguoiDung);
        }
    }
}
