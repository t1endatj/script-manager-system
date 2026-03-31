package com.scriptmanager.service;

import com.scriptmanager.dao.NguoiDungDao;
import com.scriptmanager.dao.NguoiDungDaoImpl;
import scriptmanager.entity.user.NguoiDung;

public class AuthServiceImpl implements AuthService {
    private final NguoiDungDao nguoiDungDao;

    public AuthServiceImpl() {
        this.nguoiDungDao = new NguoiDungDaoImpl();
    }

    @Override
    public NguoiDung login(String username, String password) {
        NguoiDung user = nguoiDungDao.findByUsername(username);
        if (user != null && user.getMatKhau().equals(password)) {
            return user;
        }
        return null;
    }
}
