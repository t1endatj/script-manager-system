package scriptmanager.dao;

import scriptmanager.entity.assignment.SuDungDaoCu;
import scriptmanager.entity.assignment.pk.SuDungDaoCuId;

public class SuDungDaoCuDaoImpl extends GenericDaoImpl<SuDungDaoCu, SuDungDaoCuId> implements SuDungDaoCuDao {
    public SuDungDaoCuDaoImpl() {
        super(SuDungDaoCu.class);
    }
}

