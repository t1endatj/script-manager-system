package scriptmanager.dao;

import scriptmanager.entity.asset.ThietBi;

public class ThietBiDaoImpl extends GenericDaoImpl<ThietBi, Integer> implements ThietBiDao {
    public ThietBiDaoImpl() {
        super(ThietBi.class);
    }
}

