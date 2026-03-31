package com.scriptmanager.service;

import scriptmanager.entity.user.NguoiDung;

public interface AuthService {
    NguoiDung login(String username, String password);
}
