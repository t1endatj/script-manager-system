package scriptmanager.dao;

import java.util.List;

import scriptmanager.entity.core.LichTongDuyet;

public interface LichTongDuyetDao extends GenericDao<LichTongDuyet, Integer> {
	List<LichTongDuyet> findByNguoiDungId(int maNguoiDung);
}

