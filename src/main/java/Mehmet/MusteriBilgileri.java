package Mehmet;

public class MusteriBilgileri {

    private String adSoyad;

    private String kartNo;

    private String sifre;

    private double bakiye;

    int musNo;


    MusteriBilgileri(String adSoyad, String kartNo, String sifre, double bakiye){
        this.adSoyad=adSoyad;
        this.kartNo=kartNo;
        this.sifre=sifre;
        this.bakiye=bakiye;

    }

    public String getKartNo() {
        return kartNo;
    }

    public void setKartNo(String kartNo) {
        this.kartNo = kartNo;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public String getAdSoyad() {
        return adSoyad;
    }

    public void setAdSoyad(String adSoyad) {
        this.adSoyad = adSoyad;
    }

    public double getBakiye() {
        return bakiye;
    }

    public void setBakiye(double bakiye) {
        this.bakiye = bakiye;
    }

    @Override
    public String toString() {
        return "MusteriBilgileri{" +
                "adSoyad='" + adSoyad + '\'' +
                ", kartNo='" + kartNo + '\'' +
                ", sifre='" + sifre + '\'' +
                '}';
    }
}
