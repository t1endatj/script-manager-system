package scriptmanager.service;

import scriptmanager.dao.HangMucKichBanDao;
import scriptmanager.dao.HangMucKichBanDaoImpl;
import scriptmanager.entity.core.HangMucKichBan;

import java.util.List;

public class HangMucKichBanServiceImpl implements HangMucKichBanService {
    private final HangMucKichBanDao dao;

    public HangMucKichBanServiceImpl() {
        this.dao = new HangMucKichBanDaoImpl();
    }

    @Override
    public List<HangMucKichBan> findAll() { return dao.findAll(); }

    @Override
    public HangMucKichBan findById(int id) { return dao.findById(id); }

    @Override
    public void save(HangMucKichBan item) {
        validate(item);
        dao.save(item);
    }

    @Override
    public void update(HangMucKichBan item) {
        validate(item);
        dao.update(item);
    }

    @Override
    public void delete(int id) {
        HangMucKichBan item = dao.findById(id);
        if (item != null) dao.delete(item);
    }

    private void validate(HangMucKichBan item) {
        if (item == null || item.getSuKienTiec() == null) {
            throw new IllegalArgumentException("Dữ liệu hạng mục không hợp lệ.");
        }

        if (item.getTenHM() == null || item.getTenHM().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên hạng mục không được để trống.");
        }

        if (item.getTgBatDau() == null || item.getTgKetThuc() == null) {
            throw new IllegalArgumentException("Thời gian bắt đầu và kết thúc không được để trống.");
        }

        if (!item.getTgBatDau().isBefore(item.getTgKetThuc())) {
            throw new IllegalArgumentException("Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc.");
        }

        item.setTenHM(item.getTenHM().trim());
        item.setNoiDung(item.getNoiDung() == null ? "" : item.getNoiDung().trim());
    }
}