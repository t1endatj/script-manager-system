package scriptmanager.service;

import scriptmanager.dao.SuDungDaoCuDao;
import scriptmanager.dao.SuDungDaoCuDaoImpl;
import scriptmanager.entity.asset.DaoCu;
import scriptmanager.entity.assignment.SuDungDaoCu;
import scriptmanager.entity.assignment.pk.SuDungDaoCuId;
import scriptmanager.exception.BusinessRuleException;
import scriptmanager.exception.ValidationException;

import java.util.List;

public class SuDungDaoCuService {
    private final SuDungDaoCuDao suDungDaoCuDao;
    private final DaoCuService daoCuService;

    public SuDungDaoCuService() {
        this.suDungDaoCuDao = new SuDungDaoCuDaoImpl();
        this.daoCuService = new DaoCuService();
    }

    public List<SuDungDaoCu> findAll() {
        return suDungDaoCuDao.findAll();
    }

    public SuDungDaoCu findById(SuDungDaoCuId id) {
        return suDungDaoCuDao.findById(id);
    }

    public void save(SuDungDaoCu item) {
        AuthorizationService.requireManagerOrAdmin();
        validate(item);
        suDungDaoCuDao.save(item);
    }

    public void update(SuDungDaoCu item) {
        AuthorizationService.requireManagerOrAdmin();
        validate(item);
        suDungDaoCuDao.update(item);
    }

    public void delete(SuDungDaoCuId id) {
        AuthorizationService.requireManagerOrAdmin();
        suDungDaoCuDao.deleteById(id);
    }

    private void validate(SuDungDaoCu item) {
        if (item == null || item.getDaoCu() == null) {
            throw new ValidationException("Dữ liệu sử dụng đạo cụ không hợp lệ.");
        }
        if (item.getSoLuongSuDung() <= 0) {
            throw new ValidationException("Số lượng sử dụng phải lớn hơn 0.");
        }

        DaoCu daoCu = daoCuService.findById(item.getDaoCu().getMaDaoCu());
        if (daoCu == null) {
            throw new ValidationException("Không tìm thấy đạo cụ.");
        }
        if (item.getSoLuongSuDung() > daoCu.getSoLuong()) {
            throw new BusinessRuleException("Số lượng sử dụng không được vượt quá tồn kho hiện có.");
        }
    }
}

