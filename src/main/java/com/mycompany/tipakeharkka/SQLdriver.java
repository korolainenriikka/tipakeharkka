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
            if (!testi) {
                System.out.println(okMessage);
            }
        } catch (SQLException e) {
            if (!testi) {
                System.out.println(e);
            }
        }
    }

    public static void hae(String kysely, String[] kentät, String lisa, Statement s, boolean testi) {
        try {
            ResultSet r = s.executeQuery(kysely);
            while (r.next()) {
                if (lisa.equals("Tapahtumia: ")) {
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
        } catch (Exception e) {
            if (!testi) {
                System.out.println(e);
            }
        }
    }
}