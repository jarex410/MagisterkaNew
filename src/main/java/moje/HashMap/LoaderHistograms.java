package moje.HashMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by JaroLP on 2015-11-12.
 */
public class LoaderHistograms {
    /*
        Metoda ?aduj?ca pkt wraz z desc do listy;
        Parametry to sciezka do plik oraz numer katalogowy obrazka
     */
    public HashMap fileToHasMap(File fileWithHistogarm) throws IOException {
        HashMap<Integer, Integer> map = new HashMap<>();
        String[] bazaPkt;

        String pom;
        String pom3;

        FileReader fr = new FileReader(fileWithHistogarm);
        BufferedReader bfr = new BufferedReader(fr);

        pom = bfr.readLine().replace("{", " ").replace("}", ""); // PIERWSZY
        bazaPkt = pom.split(",");

        bfr.close();
        fr.close();

        for (Integer i = 0; i < bazaPkt.length; i++) {
            if (i < 9) {
                pom3 = bazaPkt[i].substring(3);
                map.put(i, Integer.valueOf(pom3));
            } else if (i < 99) {
                pom3 = bazaPkt[i].substring(4);
                map.put(i, Integer.valueOf(pom3));
            } else {
                pom3 = bazaPkt[i].substring(5);
                map.put(i, Integer.valueOf(pom3));
            }
        }

        return map;
    }
}