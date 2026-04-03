package scriptmanager.dao;

import scriptmanager.entity.user.DoiTac;

public class DoiTacDaoImpl extends GenericDaoImpl<DoiTac, Integer> implements DoiTacDao {
    public DoiTacDaoImpl() {
        super(DoiTac.class);
    }
}

