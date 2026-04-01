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
    public void save(HangMucKichBan item) { dao.save(item); }
    @Override
    public void update(HangMucKichBan item) { dao.update(item); }
    @Override
    public void delete(int id) {
        HangMucKichBan item = dao.findById(id);
        if (item != null) dao.delete(item);
    }
}