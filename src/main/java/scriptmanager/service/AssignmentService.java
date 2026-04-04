package scriptmanager.service;

import scriptmanager.entity.assignment.PhanCongNhanSu;
import scriptmanager.entity.assignment.PhanCongThietBi;
import scriptmanager.entity.assignment.SuDungDaoCu;
import scriptmanager.entity.assignment.SuDungHieuUng;
import scriptmanager.entity.assignment.pk.PhanCongNhanSuId;
import scriptmanager.entity.assignment.pk.PhanCongThietBiId;
import scriptmanager.entity.assignment.pk.SuDungDaoCuId;
import scriptmanager.entity.assignment.pk.SuDungHieuUngId;
import scriptmanager.entity.core.HangMucKichBan;

import java.util.List;

public interface AssignmentService {
    void assignNhanSu(PhanCongNhanSu pcns);
    void assignThietBi(PhanCongThietBi pctb);
    void assignDaoCu(SuDungDaoCu sddc);
    void assignHieuUng(SuDungHieuUng sdhu);
    
    void removeNhanSu(PhanCongNhanSuId id);
    void removeThietBi(PhanCongThietBiId id);
    void removeDaoCu(SuDungDaoCuId id);
    void removeHieuUng(SuDungHieuUngId id);
    
    List<PhanCongNhanSu> getNhanSuByHangMuc(int maHM);
    List<PhanCongThietBi> getThietBiByHangMuc(int maHM);
    List<SuDungDaoCu> getDaoCuByHangMuc(int maHM);
    List<SuDungHieuUng> getHieuUngByHangMuc(int maHM);

    List<String> checkConflicts(HangMucKichBan hangMuc);
}
