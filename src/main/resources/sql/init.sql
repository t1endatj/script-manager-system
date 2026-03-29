CREATE DATABASE ScriptCoordinationDB
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 1. NguoiDung
CREATE TABLE NguoiDung (
    MaND INT AUTO_INCREMENT PRIMARY KEY,
    TenDangNhap VARCHAR(50) UNIQUE NOT NULL,
    MatKhau VARCHAR(100) NOT NULL,
    QuyenHan VARCHAR(50)
);

-- 2. SuKienTiec
CREATE TABLE SuKienTiec (
    MaSK INT AUTO_INCREMENT PRIMARY KEY,
    TenSuKien VARCHAR(100),
    ThoiGianToChuc DATETIME,
    DiaDiem VARCHAR(255),
    MaND INT,
    FOREIGN KEY (MaND) REFERENCES NguoiDung(MaND)
);

-- 3. HangMucKichBan
CREATE TABLE HangMucKichBan (
    MaHM INT AUTO_INCREMENT PRIMARY KEY,
    TenHM VARCHAR(100),
    TgBatDau DATETIME,
    TgKetThuc DATETIME,
    NoiDung TEXT,
    MaSK INT,
    FOREIGN KEY (MaSK) REFERENCES SuKienTiec(MaSK)
);

-- 4. DaoCu
CREATE TABLE DaoCu (
    MaDaoCu INT AUTO_INCREMENT PRIMARY KEY,
    TenDaoCu VARCHAR(100),
    SoLuong INT,
    TrangThai VARCHAR(50)
);

-- 5. SuDungDaoCu (bảng trung gian)
CREATE TABLE SuDungDaoCu (
    MaHM INT,
    MaDaoCu INT,
    SoLuongSuDung INT,
    PRIMARY KEY (MaHM, MaDaoCu),
    FOREIGN KEY (MaHM) REFERENCES HangMucKichBan(MaHM),
    FOREIGN KEY (MaDaoCu) REFERENCES DaoCu(MaDaoCu)
);

-- 6. HieuUng
CREATE TABLE HieuUng (
    MaHU INT AUTO_INCREMENT PRIMARY KEY,
    TenHU VARCHAR(100)
);

-- 7. SuDungHieuUng (bảng trung gian)
CREATE TABLE SuDungHieuUng (
    MaHM INT,
    MaHU INT,
    ThoiDiemKichHoat DATETIME,
    PRIMARY KEY (MaHM, MaHU),
    FOREIGN KEY (MaHM) REFERENCES HangMucKichBan(MaHM),
    FOREIGN KEY (MaHU) REFERENCES HieuUng(MaHU)
);

-- 8. ThietBi
CREATE TABLE ThietBi (
    MaTB INT AUTO_INCREMENT PRIMARY KEY,
    TenTB VARCHAR(100),
    SoLuong INT,
    TinhTrang VARCHAR(50)
);

-- 9. PhanCongThietBi
CREATE TABLE PhanCongThietBi (
    MaHM INT,
    MaTB INT,
    SoLuongSuDung INT,
    PRIMARY KEY (MaHM, MaTB),
    FOREIGN KEY (MaHM) REFERENCES HangMucKichBan(MaHM),
    FOREIGN KEY (MaTB) REFERENCES ThietBi(MaTB)
);

-- 10. NhanSu
CREATE TABLE NhanSu (
    MaNS INT AUTO_INCREMENT PRIMARY KEY,
    TenNS VARCHAR(100),
    SDT VARCHAR(15),
    VaiTro VARCHAR(50)
);

-- 11. PhanCongNhanSu
CREATE TABLE PhanCongNhanSu (
    MaHM INT,
    MaNS INT,
    NhiemVu VARCHAR(255),
    PRIMARY KEY (MaHM, MaNS),
    FOREIGN KEY (MaHM) REFERENCES HangMucKichBan(MaHM),
    FOREIGN KEY (MaNS) REFERENCES NhanSu(MaNS)
);

-- 12. DoiTac
CREATE TABLE DoiTac (
    MaDT INT AUTO_INCREMENT PRIMARY KEY,
    TenDonVi VARCHAR(100),
    LinhVuc VARCHAR(100),
    SDT VARCHAR(15)
);

-- 13. DanhSachNhac 
CREATE TABLE DanhSachNhac (
    MaBaiHat INT AUTO_INCREMENT PRIMARY KEY,
    TenBaiHat VARCHAR(100),
    CaSi VARCHAR(100),
    ThoiLuong INT,
    FileNhac VARCHAR(255),
    MaHM INT,
    FOREIGN KEY (MaHM) REFERENCES HangMucKichBan(MaHM)
);

-- 14. LichTongDuyet
CREATE TABLE LichTongDuyet (
    MaTongDuyet INT AUTO_INCREMENT PRIMARY KEY,
    ThoiGianDuyet DATETIME,
    NoiDungDuyet TEXT,
    TrangThai VARCHAR(50),
    MaSK INT,
    FOREIGN KEY (MaSK) REFERENCES SuKienTiec(MaSK)
);
