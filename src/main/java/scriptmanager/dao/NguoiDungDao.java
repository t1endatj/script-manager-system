package scriptmanager.dao;

import scriptmanager.entity.user.NguoiDung;

public interface NguoiDungDao extends GenericDao<NguoiDung, Integer> {
    NguoiDung findByUsername(String username);
}
