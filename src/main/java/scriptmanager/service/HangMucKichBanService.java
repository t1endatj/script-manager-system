package scriptmanager.service;
import scriptmanager.entity.core.HangMucKichBan;
import java.util.List;
public interface HangMucKichBanService {
    List<HangMucKichBan> findAll();
    HangMucKichBan findById(int id);
    void save(HangMucKichBan item);
    void update(HangMucKichBan item);
    void delete(int id);
}