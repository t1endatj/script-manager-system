package scriptmanager.dao;

import scriptmanager.entity.core.LichTongDuyet;

public class LichTongDuyetDaoImpl extends GenericDaoImpl<LichTongDuyet, Integer> implements LichTongDuyetDao {
    public LichTongDuyetDaoImpl() {
        super(LichTongDuyet.class);
    }
}

