package scriptmanager.dao;
import scriptmanager.entity.core.HangMucKichBan;
public class HangMucKichBanDaoImpl extends GenericDaoImpl<HangMucKichBan, Integer> implements HangMucKichBanDao {
    public HangMucKichBanDaoImpl() {
        super(HangMucKichBan.class);
    }
}