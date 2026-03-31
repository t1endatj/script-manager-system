package scriptmanager.service;
import scriptmanager.entity.core.SuKienTiec;
import java.util.List;
public interface SuKienTiecService {
    List<SuKienTiec> findAll();
    SuKienTiec findById(int id);
    void save(SuKienTiec item);
    void update(SuKienTiec item);
    void delete(int id);
}