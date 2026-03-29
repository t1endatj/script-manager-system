package scriptmanager.app.entity.asset;

import jakarta.persistence.*;
import scriptmanager.app.entity.assignment.SuDungHieuUng;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "HieuUng")
public class HieuUng {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maHU;

    private String tenHU;

    //Quan hệ 1-n với SuDungHieuUng
    @OneToMany(mappedBy = "hieuUng")
    private Set<SuDungHieuUng> suDungHieuUngs = new HashSet<>();

    //Constructor
    public HieuUng() {
    }

    public HieuUng(int maHU, String tenHU, Set<SuDungHieuUng> suDungHieuUngs) {
        this.maHU = maHU;
        this.tenHU = tenHU;
        this.suDungHieuUngs = suDungHieuUngs;
    }

    //Getter và setter
    public int getMaHU() {
        return maHU;
    }

    public void setMaHU(int maHU) {
        this.maHU = maHU;
    }

    public String getTenHU() {
        return tenHU;
    }

    public void setTenHU(String tenHU) {
        this.tenHU = tenHU;
    }

    public Set<SuDungHieuUng> getSuDungHieuUngs() {
        return suDungHieuUngs;
    }

    public void setSuDungHieuUngs(Set<SuDungHieuUng> suDungHieuUngs) {
        this.suDungHieuUngs = suDungHieuUngs;
    }
}
