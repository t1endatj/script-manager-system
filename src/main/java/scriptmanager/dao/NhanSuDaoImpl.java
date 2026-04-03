package scriptmanager.dao;

import scriptmanager.entity.user.NhanSu;

public class NhanSuDaoImpl extends GenericDaoImpl<NhanSu, Integer> implements NhanSuDao {
    public NhanSuDaoImpl() {
        super(NhanSu.class);
    }
}

