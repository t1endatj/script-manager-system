package scriptmanager.dao;

import java.util.List;

import scriptmanager.entity.core.HangMucKichBan;

public interface HangMucKichBanDao extends GenericDao<HangMucKichBan, Integer> {
	List<HangMucKichBan> findByNguoiDungId(int maNguoiDung);

	void deleteByIdWithDependencies(int maHM);
}