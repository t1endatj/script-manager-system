package scriptmanager.dao;

import scriptmanager.entity.asset.ThietBi;

public interface ThietBiDao extends GenericDao<ThietBi, Integer> {
	boolean hasAssignments(int maThietBi);

	void deleteById(int id);
}

