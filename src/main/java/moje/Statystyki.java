package moje;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Statystyki {

    public static void main(String[] args) throws IOException {

        String line;
        String dir = "D:/INZYNIERKA/knn2/";
        String dir2 = "D:/INZYNIERKA/Statystyki/";
        String numerKatalogowy = "";
        String nazwa = "";
        String doZapisu = "";
        String nazwaWyjsciowa = "";

        int k;

        int L0, L1, L2, L3;

        for (Integer i = 0; i < 50; i++) {
            if (i < 10) {

                numerKatalogowy = "0" + i + "_00_00.jpg";

            } else {

                numerKatalogowy = i + "_00_00.jpg";


            }
            String typ0 = "KLASA = " + numerKatalogowy;
            String typ1 = "TYP1 = " + numerKatalogowy;
            String typ2 = "TYP2 = " + numerKatalogowy;
            String typ3 = "TYP3 = " + numerKatalogowy;

            System.out.println("NUMER KATALOGOOWY " + numerKatalogowy);
            for (int j = 0; j < 6; j++) {

                L0 = L1 = L2 = L3 = 0;

                for (k = 0; k < 10; k++) {
                    if (j == 0 && k == 0) {
                        k++;

                    }
                    if (i < 10) {

                        nazwa = "0" + i + "_0" + j + "_0" + k + ".jpg";

                    } else {

                        nazwa = i + "_0" + j + "_0" + k + ".jpg";

                    }
                    File file = new File(dir + " KNN  " + nazwa + ".txt");
                    Scanner scaner = new Scanner(file);

                    while (scaner.hasNextLine()) {
                        line = scaner.nextLine();
                        if (line.endsWith(typ0)) {
                            L0++;
                        }
                        if (line.endsWith(typ1)) {
                            L1++;
                        }
                        if (line.endsWith(typ2)) {
                            L2++;
                        }
                        if (line.endsWith(typ3)) {
                            L3++;
                        }

                    }


                }
/*                System.out.println("typ0 =" + L0);
                System.out.println("typ1 =" + L1);
                System.out.println("typ2 =" + L2);
                System.out.println("typ3 =" + L3);*/


                if (i < 10) {
                    nazwaWyjsciowa = "0" + i + ".txt";
                } else {
                    nazwaWyjsciowa = i + ".txt";
                }

                doZapisu += "PORA = " + j + "\ntyp0 =" + L0
                        + "\ntyp1 =" + L1
                        + "\ntyp2 =" + L2
                        + "\ntyp3 =" + L3 + "\n\n";


            }
            File fileOut = new File(dir2 + nazwaWyjsciowa);
            FileWriter zapis = new FileWriter(fileOut, true);
            zapis.write(doZapisu);
            doZapisu = "";
            zapis.close();
        }
    }

}
