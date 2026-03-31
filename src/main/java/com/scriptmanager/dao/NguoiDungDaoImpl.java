package com.scriptmanager.dao;

import com.scriptmanager.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import scriptmanager.entity.user.NguoiDung;

public class NguoiDungDaoImpl extends GenericDaoImpl<NguoiDung, Integer> implements NguoiDungDao {
    public NguoiDungDaoImpl() {
        super(NguoiDung.class);
    }

    @Override
    public NguoiDung findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<NguoiDung> query = session.createQuery("FROM NguoiDung WHERE tenDangNhap = :username", NguoiDung.class);
            query.setParameter("username", username);
            return query.uniqueResult();
        }
    }
}
