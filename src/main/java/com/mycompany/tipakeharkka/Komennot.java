/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tipakeharkka;

import static com.mycompany.tipakeharkka.SQLdriver.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 *
 * @author riikoro
 */
public class Komennot {

    static void k0(boolean testi) {
        if(testi) return;
        System.out.println("1 = luo tietokanta");
        System.out.println("2 = lisää paikka");
        System.out.println("3 = lisää asiakas");
        System.out.println("4 = lisää paketti");
        System.out.println("5 = lisää tapahtuma");
        System.out.println("6 = seuraa pakettia");
        System.out.println("7 = asiakkaan tiedot");
        System.out.println("8 = paikan tiedot");
        System.out.println("9 = suorita tehokkuustesti");
        System.out.println("10 = sulje ohjelma");
    }

    static void k1(Statement s, boolean testi) {
        suorita("CREATE TABLE Asiakkaat (id INTEGER PRIMARY KEY, nimi TEXT NOT NULL UNIQUE)", s, "", testi);
        suorita("CREATE TABLE Paketit (seurantakoodi TEXT PRIMARY KEY NOT NULL UNIQUE, asiakas_id INTEGER NOT NULL REFERENCES Asiakkaat)", s, "", testi);
        suorita("CREATE TABLE Tapahtumat (id INTEGER PRIMARY KEY, paketti_id INTEGER NOT NULL REFERENCES Paketit, paikka_id  NOT NULL INTEGER REFERENCES Paikat, kuvaus TEXT DEFAULT('ei kuvausta'), date DATE NOT NULL, time TEXT NOT NULL)", s, "", false);
        suorita("CREATE TABLE Paikat (id INTEGER PRIMARY KEY, nimi TEXT UNIQUE NOT NULL)", s, "Tietokanta luotu", testi);
    }

    static void k2(String paikannimi, Statement s, boolean testi) {
        suorita("INSERT INTO Paikat (nimi) VALUES ('" + paikannimi + "')", s, "Paikka lisätty", testi);
    }

    static void k3(String asiakkaannimi, Statement s,boolean testi) {
        suorita("INSERT INTO Asiakkaat (nimi) VALUES ('" + asiakkaannimi + "')", s, "Asiakas lisätty.", testi);
    }

    static void k4(String paku, String nimi, Statement s, boolean testi) {
        try {
            ResultSet r = s.executeQuery("SELECT * FROM Asiakkaat WHERE nimi='" + nimi + "';");
            int id = r.getInt("id");
            suorita("INSERT INTO Paketit(seurantakoodi,asiakas_id) VALUES ('" + paku + "'," + id + ")", s, "Paketti lisätty", testi);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static void k5(String seurantakoodi, String paikka, String kuvaus, Statement s, boolean testi) {
        String[] datentime = haeaika();
        String date = datentime[0];
        String time = datentime[1];

        try {
            ResultSet t = s.executeQuery("SELECT * FROM Paikat WHERE nimi='" + paikka + "';");
            int paikka_id = t.getInt("id");
            suorita("INSERT INTO Tapahtumat(paketti_id,paikka_id,kuvaus, date, time) VALUES ('" + seurantakoodi + "', " + paikka_id + ",'" + kuvaus + "', '" + date + "','" + time + "')", s, "Tapahtuma lisätty.", testi);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static void k6(String paku, String[] kentat, Statement s, boolean testi) {
        hae("SELECT * FROM Tapahtumat JOIN Paikat ON paikat.id=tapahtumat.paikka_id where paketti_id=" + paku, kentat, "", s, testi);
    }

    static void k7(String name, String[] kentat, Statement s, boolean testi) {
        hae("SELECT seurantakoodi, COUNT(kuvaus) FROM Paketit"
                            + " LEFT JOIN Asiakkaat ON asiakkaat.id=paketit.asiakas_id LEFT JOIN Tapahtumat ON tapahtumat.paketti_id=seurantakoodi WHERE nimi='" + name + "' GROUP BY seurantakoodi",
                            kentat, " tapahtumaa", s, false);
    }

    public static void k8(String päivä, String paikka, String[] kentät, Statement s, boolean testi) {
        hae("SELECT COUNT(*) lkm FROM Tapahtumat JOIN Paikat on Tapahtumat.paikka_id=Paikat.id WHERE date='" + päivä + "' AND nimi='" + paikka + "'", kentät, "Tapahtumien määrä: ", s, testi);
    }

    private static String[] haeaika() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String timendate = dtf.format(now);
        String[] datentime = timendate.split(" ");
        return datentime;
    }

}
