/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tipakeharkka;

import static com.mycompany.tipakeharkka.SQLdriver.suorita;
import java.sql.*;
import static com.mycompany.tipakeharkka.Komennot.*;
import java.util.*;

/**
 *
 * @author riikoro
 */
public class Kayttoliittyma {
    
    static void launch(Connection c) {
        try {
            Statement s = c.createStatement();
            Scanner lukija = new Scanner(System.in);
            
            while (true) {
                
                System.out.print("Valitse toiminto (1-10), 0 = ohje: ");
                String toiminto = lukija.nextLine();
                
                if (toiminto.equals("0")) {
                    k0(false);
                } else if (toiminto.equals("1")) {
                    k1(s,false);
                } else if (toiminto.equals("2")) {
                    String paikka = lukija.nextLine();
                    k2(paikka, s, false);
                    
                } else if (toiminto.equals("3")) {
                    System.out.print("Anna asiakkaan nimi: ");
                    String nimi = lukija.nextLine();
                    //lisätään asiakas
                    k3(nimi, s, false);
                    
                } else if (toiminto.equals("4")) {
                    System.out.print("Anna paketin seurantakoodi: ");
                    String paku = lukija.nextLine();
                    System.out.print("Anna asiakkaan nimi: ");
                    String nimi = lukija.nextLine();
                    //lisätään paikka
                    k4(paku, nimi, s, false);
                    
                } else if (toiminto.equals("5")) {
                    System.out.print("Anna paketin seurantakoodi: ");
                    String seurantakoodi = lukija.nextLine();
                    System.out.print("Anna tapahtuman paikka: ");
                    String paikka = lukija.nextLine();
                    System.out.print("Anna tapahtuman kuvaus: ");
                    String kuvaus = lukija.nextLine();
                    //lisätään paketti
                    k5(seurantakoodi, paikka, kuvaus, s, false);
                    
                } else if (toiminto.equals("6")) {
                    System.out.print("Anna paketin seurantakoodi: ");
                    String paku = lukija.nextLine();
                    String[] kentat = {"date", "time", "nimi", "kuvaus"};
                    //haetaan paketin tiedot
                    k6(paku, kentat, s, false);
                    
                } else if (toiminto.equals("7")) {
                    System.out.print("Anna asiakkaan nimi: ");
                    String name = lukija.nextLine();
                    String[] kentat = {"seurantakoodi", "COUNT(kuvaus)"};
                    //haetaan asiakkaan tiedot
                    k7(name, kentat, s, false);
                    
                } else if (toiminto.equals("8")) {
                    System.out.print("Anna paikan nimi: ");
                    String paikka = lukija.nextLine();
                    System.out.print("Anna päivämäärä (muodossa vvvv/kk/pp): ");
                    String päivä = lukija.nextLine();
                    String[] kentät = {"lkm"};      
                    //haetaan paikan tiedot em. päivänä
                    k8(päivä, paikka, kentät, s, false);
                    
                } else if (toiminto.equals("9")) {
                    tehokkuustesti(s);                    
                } else if (toiminto.equals("10")) {
                    break;
                } else {
                    System.out.println("virhe");
                }
            }
        } catch (Exception e) {
            System.err.print(e);
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }
    
    private static void tehokkuustesti(Statement s) {
        //tyhjennetään tietokanta
        suorita("DELETE FROM Asiakkaat", s, "", true);
        suorita("DELETE FROM Paketit", s, "", true);
        suorita("DELETE FROM Tapahtmat", s, "", true);
        suorita("DELETE FROM Paikat", s, "", true);
        
        Random r = new Random();

        System.out.println("Aloitetaan vaihe 1 (lisätään tuhat paikkaa nimillä P1, P2, P3, jne)");
        long alku1 = System.nanoTime();
        for (int i = 0; i < 1e3; i++) {
            k2("P"+(i+1),s,true);
        }
        long loppu1 = System.nanoTime();
        System.out.println("Aikaa kului " + ((loppu1 - alku1) / 1e9) + " s" + "\n");

        System.out.println("Aloitetaan vaihe 2 (lisätään tuhat asiakasta nimillä A1, A2, A3, jne)");
        long alku2 = System.nanoTime();
        for (int i = 0; i < 1e3; i++) {
            k3("A"+(i+1),s,true);
        }
        long loppu2 = System.nanoTime();
        System.out.println("Aikaa kului " + ((loppu2 - alku2) / 1e9) + " s" + "\n");

        System.out.println("Aloitetaan vaihe 3 (lisätään tuhat pakettia, jokaiselle jokin asiakas)");
        long alku3 = System.nanoTime();
        String defKoodi = "K00000";       
        for (int i = 0; i < 1e3; i++) {
            String seurantakoodi = defKoodi + (1000 - (1000 - i));
            int asiakas_id = r.nextInt(1000);
            k4(seurantakoodi,"A"+(asiakas_id+1),s, true);
        }
        long loppu3 = System.nanoTime();
        System.out.println("Aikaa kului " + ((loppu3 - alku3) / 1e9) + " s" + "\n");

        System.out.println("Aloitetaan vaihe 4 (lisätään miljoona tapahtumaa, jokaiselle jokin paketti)");
        long alku4 = System.nanoTime();
        for (int i = 0; i < 1e6; i++) {
            int paketti_id = r.nextInt(1000);
            String seurantakoodi = defKoodi + (1000 - (1000 - paketti_id));
            int paikka_id = r.nextInt(1000);
            String kuvaus = "testi, tapahtuma no." + i;
            k5(seurantakoodi, "P"+(paikka_id+1) ,kuvaus,s,true);
            if(i%100000==0) System.out.println("ollaan indeksissä " + i);
        }
        long loppu4 = System.nanoTime();
        System.out.println("Aikaa kului " + ((loppu4 - alku4) / 1e9) + " s" + "\n");

        //5
        System.out.println("Aloitetaan vaihe 5 (suoritetaan tuhat kyselyä, joista jokaisessa haetaan jonkin asiakkaan pakettien määrä)");
        long alku5 = System.nanoTime();
        for (int i = 0; i < 1e3; i++) {
            
        }
        long loppu5 = System.nanoTime();
        System.out.println("Aikaa kului " + ((loppu5 - alku5) / 1e9) + " s" + "\n");

        //6
        System.out.println("Aloitetaan vaihe 6 (Suoritetaan tuhat kyselyä, joista jokaisessa haetaan jonkin paketin tapahtumien määrä)");
        long alku6 = System.nanoTime();
        for (int i = 0; i < 1e3; i++) {
            
        }
        long loppu6 = System.nanoTime();
        System.out.println("Aikaa kului " + ((loppu6 - alku6) / 1e9) + " s" + "\n");
    }
}
