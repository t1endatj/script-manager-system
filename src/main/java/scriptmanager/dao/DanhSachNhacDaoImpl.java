package scriptmanager.dao;

import scriptmanager.entity.asset.DanhSachNhac;

public class DanhSachNhacDaoImpl extends GenericDaoImpl<DanhSachNhac, Integer> implements DanhSachNhacDao {
    public DanhSachNhacDaoImpl() {
        super(DanhSachNhac.class);
    }
}

