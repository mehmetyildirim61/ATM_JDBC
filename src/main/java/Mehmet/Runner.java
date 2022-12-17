package Mehmet;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;


public class Runner {

    static Scanner scan = new Scanner(System.in);
    static Scanner scan2 = new Scanner(System.in);
    static Scanner scan3 = new Scanner(System.in);
    static Scanner input = new Scanner(System.in);
    static ArrayList<MusteriBilgileri> musteriler = new ArrayList<>();

    static String sql1 = "CREATE TABLE bankaMusterileri (Musteri_Adi varchar (20), Kart_no char (16), sifre char (4), Hesaptaki_Tutar_TL numeric)";
    static String sql2 = "INSERT INTO bankaMusterileri VALUES (?, ?, ?, ?)";
    static String sql3 = "UPDATE bankaMusterileri SET Hesaptaki_Tutar_TL=? WHERE Kart_no=?";
    static String sql4 = "SELECT Hesaptaki_Tutar_TL FROM bankaMusterileri WHERE Kart_no=?";
    static String sql5 = "SELECT sifre FROM bankaMusterileri WHERE Kart_no=?";
    static String sql6 = "UPDATE bankaMusterileri SET sifre=? WHERE Kart_no=?";
    static String sql7 = "SELECT Musteri_Adi FROM bankaMusterileri WHERE Kart_no=?";
    static Connection connection;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/????", "????", "????");
        Statement st = connection.createStatement();
        PreparedStatement pst1 = connection.prepareStatement(sql2);

        MusteriBilgileri musteri1 = new MusteriBilgileri("Mehmet Yildirim ", "1025468954203687", "1234", 0);
        MusteriBilgileri musteri2 = new MusteriBilgileri("Mustafa Dag", "9512346852100567", "1981", 0);
        MusteriBilgileri musteri3 = new MusteriBilgileri("Kamil Hirkali", "4562803191000782", "8026", 0);
        MusteriBilgileri musteri4 = new MusteriBilgileri("Nuran Albayrak", "2001564228339471", "3346", 0);
        MusteriBilgileri musteri5 = new MusteriBilgileri("Nurefsan Aydemir", "6456823100458990", "5910", 0);

        musteriler.add(musteri1);
        musteriler.add(musteri2);
        musteriler.add(musteri3);
        musteriler.add(musteri4);
        musteriler.add(musteri5);

//        st.execute(sql1);
//        for(int i=0; i<musteriler.size(); i++){
//            String ad_soyad=musteriler.get(i).getAdSoyad();
//            String kartNo=musteriler.get(i).getKartNo();
//            String sifre=musteriler.get(i).getSifre();
//            double bakiye=musteriler.get(i).getBakiye();
//            pst1.setString(1,ad_soyad);
//            pst1.setString(2,kartNo);
//            pst1.setString(3,sifre);
//            pst1.setDouble(4,bakiye);
//            pst1.executeUpdate();
//        }
        String kartNo = kartNumarasiVeSifreGir();
        System.out.println("kartNo = " + kartNo);

        islemler();

        boolean islemDevamMi = true;
        while (islemDevamMi == true) {
            Scanner input = new Scanner(System.in);
            char islemTercih = input.next().charAt(0);

            switch (islemTercih) {
                case '1':
                    System.out.println("Guncel Bakiyeniz: " + bakiyeSorgula(kartNo) + " TL");
                    System.out.println("Ana menuye donmek icin bir tusa basin");
                    input.next();
                    islemler();
                    break;
                case '2':
                    paraYatir(kartNo);
                    break;
                case '3':
                    paraCek(kartNo);
                    break;
                case '4':
                    paraGonder(kartNo);
                    break;
                case '5':
                    sifreDegistir(kartNo);
                    break;
                case '6':
                    islemDevamMi = false;
                    break;
                default:
                    System.out.println("Yanlis giris yaptiniz.\nLutfen 1 ile 6 arasi secim yapiniz");
            }
        }
        System.out.println("Bankamizi kullandiginiz icin tesekkur ederiz.\n!!!Kartinizi almayi unutmayiniz!!!");
    }

    static void islemler() {
        System.out.println("1- Bakiye sorgulama\n2- Para yatirma\n3- Para çekme\n4- Para gönderme\n5- Sifre değiştirme\n6- Cikis");
    }

    static double bakiyeSorgula(String kartNo) throws SQLException {

        double bakiye = 0;
        PreparedStatement pst1 = connection.prepareStatement(sql4);
        pst1.setString(1, kartNo);
        ResultSet resultSet1 = pst1.executeQuery();
        while (resultSet1.next()) {
            bakiye += resultSet1.getDouble(1);
        }
        return bakiye;
    }

    static void paraYatir(String kartNo) throws SQLException {
        double guncelBakiye = bakiyeSorgula(kartNo);
        System.out.println("Guncel Bakiyeniz: " + guncelBakiye + " TL");

        while (true) {
            System.out.println("Lutfen yatiracaginiz miktari giriniz ve parayi acilan bolmeye koyunuz...");
            double hesabaYatirilanMiktar = input.nextDouble();
            System.out.println("Paraniz tanimlaniyor...\nYatirilan miktar: " + hesabaYatirilanMiktar);
            guncelBakiye += hesabaYatirilanMiktar;
            PreparedStatement pst1 = connection.prepareStatement(sql3);
            pst1.setString(2, kartNo);
            pst1.setDouble(1, guncelBakiye);
            pst1.executeUpdate();

            System.out.println("Guncel Bakiyeniz: " + guncelBakiye + " TL");
            System.out.println("Ekstra para yatirmak icin 1'e, ana menuye donmek icin herhangi bir tusa basiniz");
            char cevap = scan.next().charAt(0);
            if (cevap != '1') {
                break;
            }
        }
        islemler();
    }

    static void paraCek(String kartNo) throws SQLException {
        double guncelBakiye = bakiyeSorgula(kartNo);
        System.out.println("Guncel Bakiyeniz: " + guncelBakiye + " TL");

        while (true) {
            System.out.println("Cekmek istediginiz miktari giriniz...");

            double cekilenMiktar = input.nextDouble();
            if (cekilenMiktar > guncelBakiye) {
                System.out.println("Bakiye yetersiz, lutfen hesabinizi kontrol edin");
                break;
            }
            System.out.println("Paraniz hazirlaniyor...\nLutfen paranizi aliniz.\nCekilen miktar: " + cekilenMiktar + " TL");
            guncelBakiye -= cekilenMiktar;

            PreparedStatement pst1 = connection.prepareStatement(sql3);
            pst1.setString(2, kartNo);
            pst1.setDouble(1, guncelBakiye);
            pst1.executeUpdate();
            System.out.println("Guncel Bakiyeniz: " + guncelBakiye + " TL");
            System.out.println("Ekstra para cekmek icin 1'e, ana menuye donmek icin herhangi bir tusa basiniz");
            char cevap = scan.next().charAt(0);
            if (cevap != '1') {
                break;
            }
        }
        islemler();
    }

    static void paraGonder(String kartNo) throws SQLException {
        double guncelBakiye = bakiyeSorgula(kartNo);
        System.out.println("Guncel Bakiyeniz: " + guncelBakiye + " TL");

        System.out.println("Lutfen para gondereceginiz IBAN numarasini giriniz");
        String IBAN = scan3.nextLine();
        IBAN = IBAN.trim().replaceAll(" ", "");

        while (true) {
            if (IBAN.length() == 26 && IBAN.substring(0, 2).equalsIgnoreCase("TR")) {
                System.out.println("Lutfen gondereceginiz miktari yaziniz");
                double gonderilecekTutar = input.nextDouble();
                if (guncelBakiye < gonderilecekTutar) {
                    System.out.println("Bakiye yetersiz, lutfen hesabinizi kontrol edin");
                    islemler();
                    break;
                } else {
                    System.out.println("Gonderilecek Tutar: " + gonderilecekTutar + " TL\nPara Gonderme Islemi Basariyla Gerceklesti");
                    guncelBakiye -= gonderilecekTutar;
                    PreparedStatement pst1 = connection.prepareStatement(sql3);
                    pst1.setString(2, kartNo);
                    pst1.setDouble(1, guncelBakiye);
                    pst1.executeUpdate();
                    System.out.println("Guncel Bakiyeniz: " + guncelBakiye + " TL");
                    System.out.println("Ana menuye donmek icin herhangi bir tusa basin");
                    scan.next();
                    islemler();
                    break;
                }
            } else {
                System.out.println("Lutfen girdiginiz IBAN numarasini kontrol ediniz.\nIBAN numarasi TR ile baslamali ve toplam 26 karakterden olusmalidir");
                paraGonder(kartNo);
            }
        }
    }

    static void sifreDegistir(String kartNo) throws SQLException {
        PreparedStatement pst1 = connection.prepareStatement(sql5);
        pst1.setString(1, kartNo);
        ResultSet resultSet1 = pst1.executeQuery();
        String guncelSifre = "";
        while (resultSet1.next()) {
            guncelSifre += resultSet1.getString(1);
        }
        while (true) {
            System.out.println("Lutfen mevcut sifrenizi giriniz.");
            String sifre = scan2.next();

            if (sifre.equals(guncelSifre)) {
                System.out.println("Lutfen yeni sifrenizi giriniz");
                String yeniSifre = scan2.next();

                if (yeniSifre.replaceAll("[0-9]", "").length() == 0 && yeniSifre.length() == 4) {
                    PreparedStatement pst2 = connection.prepareStatement(sql6);
                    pst2.setString(2, kartNo);
                    pst2.setString(1, yeniSifre);
                    pst2.executeUpdate();
                    System.out.println("Yeni sifreniz " + yeniSifre + " olarak tanimlanmistir\nAna menuye donmek icin herhangi bir tusa basin");
                    scan.next();
                    break;
                } else {
                    System.out.println("Gecersiz sifre girisi. Sifreniz 4 basamakli olmali ve rakamlardan olusmalidir");
                }
            } else {
                System.out.println("yanlis sifre girdiniz");
            }
        }
        islemler();
    }

    static String kartNumarasiVeSifreGir() throws SQLException {

        Scanner scan = new Scanner(System.in);
        Scanner input = new Scanner(System.in);
        String guncelSifre = "";

        System.out.println("Lutfen 16 haneli kart numaranizi giriniz");
        String kartNo = input.nextLine().trim().replaceAll(" ", "");

        System.out.println("Lutfen 4 basamakli sifrenizi giriniz");
        String sifre = input.next();
        PreparedStatement pst1 = connection.prepareStatement(sql5);
        pst1.setString(1, kartNo);
        ResultSet resultSet1 = pst1.executeQuery();
        while (resultSet1.next()) {
            guncelSifre += resultSet1.getString(1);
            if (sifre.equals(guncelSifre)) {
                System.out.println("sifre girisi basarili, isleminiz devam ediyor...");
                PreparedStatement pst2 = connection.prepareStatement(sql7);
                pst2.setString(1, kartNo);
                ResultSet resultSet2 = pst2.executeQuery();
                while (resultSet2.next()) {
                    System.out.println("Sayin " + resultSet2.getString(1) + ", bankamiza hosgeldiniz.\nLutfen yapmak istediginiz islemi seciniz");
                    break;
                }
            } else {
                System.out.println("yanlis kart numarasi veya sifre, lutfen tekrar deneyiniz");
                kartNumarasiVeSifreGir();
            }
        }
        return kartNo;
    }
}


