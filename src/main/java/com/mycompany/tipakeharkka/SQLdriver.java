/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tipakeharkka;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author riikoro
 */
public class SQLdriver {

    public static void suorita(String komento, Statement s, String okMessage, boolean testi) {
        try {
            s.execute(komento);
            if (!testi && !okMessage.equals("")) {
                System.out.println(okMessage);
            }
        } catch (SQLException e) {
            if (testi) {
                System.out.println("VIRHE:");
                System.err.print(e);
            } else {
                //tulostetaan sopiva virheviesti
                if (e.getMessage().contains("SQL error or missing database (table Asiakkaat already exists)")) {
                    System.out.println("virhe: tietokanta on jo olemassa");
                }
                if (e.getMessage().contains("Abort due to constraint violation (column paikka is not unique)")) {
                    System.out.println("virhe: paikka on jo tietokannassa");
                }
                if (e.getMessage().contains("Abort due to constraint violation (column nimi is not unique)")) {
                    System.out.println("virhe: asiakas on jo tietokannassa");
                }
                if (e.getMessage().contains("Abort due to constraint violation (column seurantakoodi is not unique)")) {
                    System.out.println("virhe: paketti on jo tietokannassa");
                }
            }
        }
    }

    public static void hae(String kysely, String[] kentät, String lisa, Statement s, boolean testi) {
        try {
            ResultSet r = s.executeQuery(kysely);
            boolean tuloksia = false;
            
            while (r.next()&&r.getInt(kentät[0])!=0) {
                tuloksia = true;
                if (lisa.equals("Tapahtumien määrä: ")) {
                    System.out.print(lisa);
                }
                for (int i = 0; i < kentät.length; i++) {
                    if (!testi) {
                        System.out.print(r.getObject(kentät[i]));
                    }
                    if (i != kentät.length - 1 && !testi) {
                        System.out.print(", ");
                    }
                }
                if (lisa.equals(" tapahtumaa")) {
                    System.out.print(lisa);
                }
                if (!testi) {
                    System.out.println();
                }
            }
            
            if (!tuloksia) {
                System.out.println("virhe: ei hakutuloksia");
                return;
            }
        } catch (Exception e) {
            if (testi) {
                System.out.println("VIRHE:");
                System.err.print(e);
            } else {
                System.out.println(e);
            }
        }
    }

    public static int etsiIndeksi(String taulu, String sarake, String hakutermi, Statement s, String errormessage, boolean testi) {
        try {
            ResultSet t = s.executeQuery("SELECT * FROM " + taulu + " WHERE " + sarake + "='" + hakutermi + "';");
            int id = t.getInt("id");
            return id;
        } catch (Exception e) {
            if (testi) {
                System.out.println("VIRHE:");
                System.err.print(e);
            } else {
                System.out.println(errormessage);
            }
        }
        return -1;
        /*jos palautusarvo tämä, lisäystä ei tehdä*/
    }
}
