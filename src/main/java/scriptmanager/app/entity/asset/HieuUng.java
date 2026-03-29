package scriptmanager.app.entity.asset;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import scriptmanager.app.entity.assignment.SuDungHieuUng;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "HieuUng")
public class HieuUng {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaHU")
    private int maHU;

    @NotBlank
    @Size(max = 100)
    @Column(name = "TenHU", nullable = false, length = 100)
    private String tenHU;

    //Quan hệ 1-n với SuDungHieuUng
    @OneToMany(mappedBy = "hieuUng", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SuDungHieuUng> suDungHieuUngs = new HashSet<>();

    //Constructor
    public HieuUng() {
    }

    public HieuUng(String tenHU) {
        this.tenHU = tenHU;
    }

    //helper method SuDungHieuUng
    public void addSuDungHieuUng(SuDungHieuUng sd) {
        suDungHieuUngs.add(sd);
        sd.setHieuUng(this);
    }

    public void removeSuDungHieuUng(SuDungHieuUng sd) {
        suDungHieuUngs.remove(sd);
        sd.setHieuUng(null);
    }

    //Getter và setter
    public int getMaHU() {
        return maHU;
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
        this.suDungHieuUngs.clear();
        if (suDungHieuUngs != null) {
            suDungHieuUngs.forEach(this::addSuDungHieuUng);
        }
    }
}
