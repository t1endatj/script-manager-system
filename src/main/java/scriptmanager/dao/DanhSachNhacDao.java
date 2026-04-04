package scriptmanager.dao;

import scriptmanager.entity.asset.DanhSachNhac;

public interface DanhSachNhacDao extends GenericDao<DanhSachNhac, Integer> {
	void deleteById(int id);
}

