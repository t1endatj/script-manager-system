package scriptmanager.dao;

import scriptmanager.entity.asset.DaoCu;

public interface DaoCuDao extends GenericDao<DaoCu, Integer> {
	boolean hasUsages(int maDaoCu);
}

