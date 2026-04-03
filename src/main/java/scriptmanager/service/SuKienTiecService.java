package scriptmanager.service;

import java.time.LocalDateTime;

import scriptmanager.entity.core.SuKienTiec;
import java.util.List;

public interface SuKienTiecService {
    List<SuKienTiec> findAll();
    SuKienTiec findById(int id);
    boolean isDuplicate(int excludeId, String tenSuKien, LocalDateTime thoiGianToChuc, String diaDiem, int maNguoiDung);
    void save(SuKienTiec item);
    void update(SuKienTiec item);
    void delete(int id);
}