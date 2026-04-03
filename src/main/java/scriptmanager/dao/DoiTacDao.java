package scriptmanager.dao;

import scriptmanager.entity.user.DoiTac;

public interface DoiTacDao extends GenericDao<DoiTac, Integer> {
	boolean existsByTenDonVi(String tenDonVi, Integer excludeId);
}

