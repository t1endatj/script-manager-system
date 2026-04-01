package scriptmanager.dao;
import scriptmanager.entity.core.SuKienTiec;
public class SuKienTiecDaoImpl extends GenericDaoImpl<SuKienTiec, Integer> implements SuKienTiecDao {
    public SuKienTiecDaoImpl() {
        super(SuKienTiec.class);
    }
}