CREATE DATABASE ScriptCoordinationDB
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE ScriptCoordinationDB;

-- 1. NguoiDung
CREATE TABLE NguoiDung (
     MaND INT AUTO_INCREMENT PRIMARY KEY,
     TenDangNhap VARCHAR(50) NOT NULL UNIQUE,
     MatKhau VARCHAR(100) NOT NULL,
     QuyenHan VARCHAR(50)
);

-- 2. DoiTac
CREATE TABLE DoiTac (
     MaDT INT AUTO_INCREMENT PRIMARY KEY,
     TenDonVi VARCHAR(100) NOT NULL,
     LinhVuc VARCHAR(100),
     SDT VARCHAR(15)
);

-- 3. SuKienTiec
CREATE TABLE SuKienTiec (
     MaSK INT AUTO_INCREMENT PRIMARY KEY,
     TenSuKien VARCHAR(100) NOT NULL,
     ThoiGianToChuc DATETIME,
     DiaDiem VARCHAR(255),
     MaND INT NOT NULL,
     CONSTRAINT FK_SK_ND FOREIGN KEY (MaND)
         REFERENCES NguoiDung(MaND)
         ON DELETE CASCADE
);

-- 4. HangMucKichBan
CREATE TABLE HangMucKichBan (
     MaHM INT AUTO_INCREMENT PRIMARY KEY,
     TenHM VARCHAR(100) NOT NULL,
     TgBatDau DATETIME,
     TgKetThuc DATETIME,
     NoiDung TEXT,
     MaSK INT NOT NULL,
     CONSTRAINT FK_HM_SK FOREIGN KEY (MaSK)
           REFERENCES SuKienTiec(MaSK)
           ON DELETE CASCADE
);

-- 5. DaoCu
CREATE TABLE DaoCu (
    MaDaoCu INT AUTO_INCREMENT PRIMARY KEY,
    TenDaoCu VARCHAR(100) NOT NULL,
    SoLuong INT DEFAULT 0,
    TrangThai VARCHAR(50),
    CONSTRAINT chk_daoCu_soluong CHECK (SoLuong >= 0)
);

-- 6. HieuUng
CREATE TABLE HieuUng (
    MaHU INT AUTO_INCREMENT PRIMARY KEY,
    TenHU VARCHAR(100) NOT NULL
);

-- 7. ThietBi
CREATE TABLE ThietBi (
     MaTB INT AUTO_INCREMENT PRIMARY KEY,
     TenTB VARCHAR(100) NOT NULL,
     SoLuong INT DEFAULT 0,
     TinhTrang VARCHAR(50),
     MaDT INT NOT NULL,
     CONSTRAINT FK_TB_DT FOREIGN KEY (MaDT)
          REFERENCES DoiTac(MaDT)
          ON DELETE CASCADE,
     CONSTRAINT chk_thietbi_soluong CHECK (SoLuong >= 0)
);

-- 8. NhanSu
CREATE TABLE NhanSu (
      MaNS INT AUTO_INCREMENT PRIMARY KEY,
      TenNS VARCHAR(100) NOT NULL,
      SDT VARCHAR(15),
      VaiTro VARCHAR(50)
);

-- 9. DanhSachNhac
CREATE TABLE DanhSachNhac (
      MaBaiHat INT AUTO_INCREMENT PRIMARY KEY,
      TenBaiHat VARCHAR(100) NOT NULL,
      CaSi VARCHAR(100),
      ThoiLuong INT,
      FileNhac VARCHAR(255),
      MaHM INT NOT NULL,
      CONSTRAINT FK_NHAC_HM FOREIGN KEY (MaHM)
            REFERENCES HangMucKichBan(MaHM)
            ON DELETE CASCADE
);

-- 10. LichTongDuyet
CREATE TABLE LichTongDuyet (
       MaTongDuyet INT AUTO_INCREMENT PRIMARY KEY,
       ThoiGianDuyet DATETIME,
       NoiDungDuyet TEXT,
       TrangThai VARCHAR(50),
       MaSK INT NOT NULL,
       CONSTRAINT FK_LTD_SK FOREIGN KEY (MaSK)
             REFERENCES SuKienTiec(MaSK)
             ON DELETE CASCADE
);

-- 11. SuDungDaoCu
CREATE TABLE SuDungDaoCu (
       MaHM INT,
       MaDaoCu INT,
       SoLuongSuDung INT DEFAULT 1,
       PRIMARY KEY (MaHM, MaDaoCu),
       CONSTRAINT FK_SDDC_HM FOREIGN KEY (MaHM)
               REFERENCES HangMucKichBan(MaHM)
               ON DELETE CASCADE,
       CONSTRAINT FK_SDDC_DC FOREIGN KEY (MaDaoCu)
               REFERENCES DaoCu(MaDaoCu)
               ON DELETE CASCADE,
       CONSTRAINT chk_sddc_soluong CHECK (SoLuongSuDung > 0)
);

-- 12. SuDungHieuUng
CREATE TABLE SuDungHieuUng (
       MaHM INT,
       MaHU INT,
       ThoiDiemKichHoat DATETIME,
       PRIMARY KEY (MaHM, MaHU),
       CONSTRAINT FK_SDHU_HM FOREIGN KEY (MaHM)
                REFERENCES HangMucKichBan(MaHM)
                ON DELETE CASCADE,
       CONSTRAINT FK_SDHU_HU FOREIGN KEY (MaHU)
                REFERENCES HieuUng(MaHU)
                ON DELETE CASCADE
);

-- 13. PhanCongThietBi
CREATE TABLE PhanCongThietBi (
      MaHM INT,
      MaTB INT,
      SoLuongSuDung INT DEFAULT 1,
      PRIMARY KEY (MaHM, MaTB),
      CONSTRAINT FK_PCTB_HM FOREIGN KEY (MaHM)
                 REFERENCES HangMucKichBan(MaHM)
                 ON DELETE CASCADE,
      CONSTRAINT FK_PCTB_TB FOREIGN KEY (MaTB)
                 REFERENCES ThietBi(MaTB)
                 ON DELETE CASCADE,
      CONSTRAINT chk_pctb_soluong CHECK (SoLuongSuDung > 0)
);

-- 14. PhanCongNhanSu
CREATE TABLE PhanCongNhanSu (
      MaHM INT,
      MaNS INT,
      NhiemVu VARCHAR(255),
      PRIMARY KEY (MaHM, MaNS),
      CONSTRAINT FK_PCNS_HM FOREIGN KEY (MaHM)
                  REFERENCES HangMucKichBan(MaHM)
                  ON DELETE CASCADE,
      CONSTRAINT FK_PCNS_NS FOREIGN KEY (MaNS)
                  REFERENCES NhanSu(MaNS)
                  ON DELETE CASCADE
);