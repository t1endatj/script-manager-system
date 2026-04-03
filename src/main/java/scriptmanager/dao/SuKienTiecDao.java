package scriptmanager.dao;

import java.time.LocalDateTime;

import scriptmanager.entity.core.SuKienTiec;

public interface SuKienTiecDao extends GenericDao<SuKienTiec, Integer> {
	void deleteById(int id);

	boolean existsDuplicate(String tenSuKien, LocalDateTime thoiGianToChuc, String diaDiem, int maNguoiDung, Integer excludeId);
}