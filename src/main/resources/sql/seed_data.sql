USE ScriptCoordinationDB;

-- 1. NguoiDung
INSERT INTO NguoiDung (TenDangNhap, MatKhau, QuyenHan) VALUES
('admin', '123456', 'QuanTri'),
('thuydev', '123456', 'NhanVien'),
('linhtran', '123456', 'NhanVien');

-- 2. SuKienTiec
INSERT INTO SuKienTiec (TenSuKien, ThoiGianToChuc, DiaDiem, MaND) VALUES
('Tiệc cưới Anh Tuấn', '2026-05-10 18:00:00', 'Nhà hàng Riverside', 1),
('Sinh nhật bé Na', '2026-04-20 17:00:00', 'Quận 7', 2);

-- 3. HangMucKichBan
INSERT INTO HangMucKichBan (TenHM, TgBatDau, TgKetThuc, NoiDung, MaSK) VALUES
('Đón khách', '2026-05-10 17:30:00', '2026-05-10 18:00:00', 'Khách vào sảnh', 1),
('Khai tiệc', '2026-05-10 18:00:00', '2026-05-10 18:30:00', 'MC giới thiệu', 1),
('Cắt bánh', '2026-05-10 19:00:00', '2026-05-10 19:30:00', 'Nghi thức chính', 1),
('Mở quà', '2026-04-20 18:00:00', '2026-04-20 18:30:00', 'Bé nhận quà', 2);

-- 4. DaoCu
INSERT INTO DaoCu (TenDaoCu, SoLuong, TrangThai) VALUES
('Bàn tiệc', 20, 'Tốt'),
('Ghế', 200, 'Tốt'),
('Bánh cưới', 1, 'Chuẩn bị'),
('Hoa trang trí', 50, 'Tốt');

-- 5. HieuUng
INSERT INTO HieuUng (TenHU) VALUES
('Pháo giấy'),
('Khói sân khấu'),
('Đèn LED');

-- 6. ThietBi
INSERT INTO ThietBi (TenTB, SoLuong, TinhTrang) VALUES
('Loa', 4, 'Tốt'),
('Micro', 6, 'Tốt'),
('Đèn sân khấu', 10, 'Tốt');

-- 7. NhanSu
INSERT INTO NhanSu (TenNS, SDT, VaiTro) VALUES
('Nguyễn Văn A', '0901234567', 'MC'),
('Trần Thị B', '0912345678', 'Phục vụ'),
('Lê Văn C', '0923456789', 'Kỹ thuật');

-- 8. DoiTac
INSERT INTO DoiTac (TenDonVi, LinhVuc, SDT) VALUES
('Công ty Âm Thanh Ánh Sáng', 'Thiết bị', '0901111111'),
('Shop Hoa Tươi Hạnh Phúc', 'Trang trí', '0902222222');

-- 9. SuDungDaoCu
INSERT INTO SuDungDaoCu (MaHM, MaDaoCu, SoLuongSuDung) VALUES
(1, 1, 10),
(1, 2, 100),
(2, 3, 1),
(3, 4, 20);

-- 10. SuDungHieuUng
INSERT INTO SuDungHieuUng (MaHM, MaHU, ThoiDiemKichHoat) VALUES
(2, 1, '2026-05-10 18:05:00'),
(3, 2, '2026-05-10 19:05:00');

-- 11. PhanCongThietBi
INSERT INTO PhanCongThietBi (MaHM, MaTB, SoLuongSuDung) VALUES
(2, 1, 2),
(2, 2, 2),
(3, 3, 5);

-- 12. PhanCongNhanSu
INSERT INTO PhanCongNhanSu (MaHM, MaNS, NhiemVu) VALUES
(2, 1, 'Dẫn chương trình'),
(1, 2, 'Đón khách'),
(2, 3, 'Setup âm thanh');

-- 13. DanhSachNhac
INSERT INTO DanhSachNhac (TenBaiHat, CaSi, ThoiLuong, FileNhac, MaHM) VALUES
('Ngày Chung Đôi', 'Văn Mai Hương', 240, 'ngaychungdoi.mp3', 2),
('Happy Birthday', 'Traditional', 180, 'happybirthday.mp3', 4);

-- 14. LichTongDuyet
INSERT INTO LichTongDuyet (ThoiGianDuyet, NoiDungDuyet, TrangThai, MaSK) VALUES
('2026-05-09 18:00:00', 'Tổng duyệt sân khấu cưới', 'Hoàn thành', 1),
('2026-04-19 17:00:00', 'Test chương trình sinh nhật', 'Hoàn thành', 2);