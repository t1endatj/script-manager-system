package scriptmanager.service;

import scriptmanager.dao.LichTongDuyetDao;
import scriptmanager.dao.LichTongDuyetDaoImpl;
import scriptmanager.entity.core.LichTongDuyet;

import java.util.List;

public class LichTongDuyetService {
    private final LichTongDuyetDao lichTongDuyetDao;

    public LichTongDuyetService() {
        this.lichTongDuyetDao = new LichTongDuyetDaoImpl();
    }

    public List<LichTongDuyet> findAll() {
        return lichTongDuyetDao.findAll();
    }

    public LichTongDuyet findById(int id) {
        return lichTongDuyetDao.findById(id);
    }

    public void save(LichTongDuyet item) {
        validate(item);
        lichTongDuyetDao.save(item);
    }

    public void update(LichTongDuyet item) {
        validate(item);
        lichTongDuyetDao.update(item);
    }

    public void delete(int id) {
        LichTongDuyet item = lichTongDuyetDao.findById(id);
        if (item != null) {
            lichTongDuyetDao.delete(item);
        }
    }

    private void validate(LichTongDuyet item) {
        if (item == null || item.getSuKienTiec() == null) {
            throw new IllegalArgumentException("Dữ liệu lịch tổng duyệt không hợp lệ.");
        }

        if (item.getThoiGianDuyet() == null) {
            throw new IllegalArgumentException("Thời gian duyệt không được để trống.");
        }

        if (item.getSuKienTiec().getThoiGianToChuc() != null
                && item.getThoiGianDuyet().isAfter(item.getSuKienTiec().getThoiGianToChuc())) {
            throw new IllegalArgumentException("Thời gian tổng duyệt phải trước hoặc bằng thời gian tổ chức sự kiện.");
        }

        item.setNoiDungDuyet(item.getNoiDungDuyet() == null ? "" : item.getNoiDungDuyet().trim());
        item.setTrangThai(item.getTrangThai() == null ? "" : item.getTrangThai().trim());
    }
}

