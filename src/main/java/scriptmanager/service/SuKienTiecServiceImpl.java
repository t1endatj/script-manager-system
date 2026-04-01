package scriptmanager.service;
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
    public void save(SuKienTiec item) { dao.save(item); }
    @Override
    public void update(SuKienTiec item) { dao.update(item); }
    @Override
    public void delete(int id) {
        SuKienTiec item = dao.findById(id);
        if (item != null) dao.delete(item);
    }
}