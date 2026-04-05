package scriptmanager.service;

import scriptmanager.dao.DanhSachNhacDao;
import scriptmanager.dao.DanhSachNhacDaoImpl;
import scriptmanager.entity.asset.DanhSachNhac;
import scriptmanager.exception.ValidationException;

import java.util.List;
import java.util.Locale;

public class DanhSachNhacService {
    private final DanhSachNhacDao danhSachNhacDao;

    public DanhSachNhacService() {
        this.danhSachNhacDao = new DanhSachNhacDaoImpl();
    }

    public List<DanhSachNhac> findAll() {
        return danhSachNhacDao.findAll();
    }

    public DanhSachNhac findById(int id) {
        return danhSachNhacDao.findById(id);
    }

    public void save(DanhSachNhac item) {
        AuthorizationService.requireManagerOrAdmin();
        validate(item);
        danhSachNhacDao.save(item);
    }

    public void update(DanhSachNhac item) {
        AuthorizationService.requireManagerOrAdmin();
        validate(item);
        danhSachNhacDao.update(item);
    }

    public void delete(int id) {
        AuthorizationService.requireManagerOrAdmin();
        danhSachNhacDao.deleteById(id);
    }

    private void validate(DanhSachNhac item) {
        if (item == null) {
            throw new ValidationException("Dữ liệu bài hát không hợp lệ.");
        }

        String fileNhac = item.getFileNhac() != null ? item.getFileNhac().trim() : "";
        String tenFile = item.getTenFileGoc() != null ? item.getTenFileGoc().trim() : "";
        String loaiFile = item.getLoaiFile() != null ? item.getLoaiFile().trim() : "";
        byte[] noiDungFile = item.getNoiDungFile();

        boolean hasBinary = noiDungFile != null && noiDungFile.length > 0;
        boolean hasLegacyFile = !fileNhac.isEmpty();
        if (!hasBinary && !hasLegacyFile) {
            throw new ValidationException("Vui lòng chọn file nhạc.");
        }

        if (item.getMaBaiHat() == 0 && !hasBinary) {
            throw new ValidationException("Bài hát mới phải lưu kèm file .mp3 hoặc .mp4.");
        }

        // Rule: chỉ nhận file nhạc/video mp3, mp4.
        String extSource = !tenFile.isEmpty() ? tenFile : fileNhac;
        if (!isSupportedExt(extSource)) {
            throw new ValidationException("File nhạc chỉ hỗ trợ định dạng .mp3 hoặc .mp4.");
        }

        item.setFileNhac(fileNhac);
        item.setTenFileGoc(tenFile);
        item.setLoaiFile(loaiFile);
    }

    private boolean isSupportedExt(String fileName) {
        String normalized = fileName.toLowerCase(Locale.ROOT);
        return normalized.endsWith(".mp3") || normalized.endsWith(".mp4");
    }
}
