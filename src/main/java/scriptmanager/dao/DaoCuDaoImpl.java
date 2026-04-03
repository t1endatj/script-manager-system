package scriptmanager.dao;

import scriptmanager.entity.asset.DaoCu;

public class DaoCuDaoImpl extends GenericDaoImpl<DaoCu, Integer> implements DaoCuDao {
    public DaoCuDaoImpl() {
        super(DaoCu.class);
    }
}

