package scriptmanager.service;

import scriptmanager.entity.assignment.PhanCongNhanSu;
import scriptmanager.entity.assignment.PhanCongThietBi;
import scriptmanager.entity.assignment.SuDungHieuUng;
import scriptmanager.entity.core.HangMucKichBan;

import java.util.List;

public interface AssignmentService {
    void assignNhanSu(PhanCongNhanSu pcns);
    void assignThietBi(PhanCongThietBi pctb);
    void assignHieuUng(SuDungHieuUng sdhu);
    List<String> checkConflicts(HangMucKichBan hangMuc);
}
