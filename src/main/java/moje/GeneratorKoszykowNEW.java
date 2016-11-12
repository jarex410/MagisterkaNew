package moje;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by JaroLP on 2015-10-30.
 * <p>
 * <p>
 * KLASA DZIALA POPRAWNIE
 */
public class GeneratorKoszykowNEW {


    public final static String PATH_TO_RZESZOW_DATABASE = "D:\\MAGISTERKA\\BazyZdjec\\Rzeszow";

    public final static String PATH_TO_RZESZOW_DATABASE_DESC = PATH_TO_RZESZOW_DATABASE + "\\desc";

    public final static int SUFR = 64;

    public final static int SIFT = 128;


    /**
     * Metoda generuje koszyki dla odpowiedniej bazy zdjec
     *
     * @param pathToDataBase
     * @param numberOfBaskets
     * @param listaDESCGenerowanychPrzezAlgorytm
     * @throws IOException
     */

    public void generujKoszyki(String pathToDataBase, int numberOfBaskets, int listaDESCGenerowanychPrzezAlgorytm) throws IOException {

        List<String> calaBazaPkt = new ArrayList<>();
        List<String> pktBazowe = new ArrayList<>();

        Random r = new Random();
        int randomik = 0;

        File folder = new File(pathToDataBase + "\\desc");
        File[] listOfFiles = folder.listFiles();

        //zbiera pkt z okreslonej listy obrazow i losuje koszyki
        //w tym przypadku 100 losowych obrazow z bazy

        for (int i = 0; i < 100; i++) {
            File file = listOfFiles[r.nextInt(listOfFiles.length)];
            if (file.isFile()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    calaBazaPkt.add(scanner.nextLine() + "\n");
                    for (int ii = 0; ii < listaDESCGenerowanychPrzezAlgorytm; ii++) {
                        calaBazaPkt.add(scanner.nextLine() + "\n");
                    }
                }
            }
        }

        //generuje koszyki
        for (int i = 0; i < numberOfBaskets; i++) {
            randomik = r.nextInt(calaBazaPkt.size() / (listaDESCGenerowanychPrzezAlgorytm + 1));
            pktBazowe.add(calaBazaPkt.get(randomik * (listaDESCGenerowanychPrzezAlgorytm + 1)));
            for (int k = 1; k < listaDESCGenerowanychPrzezAlgorytm + 1; k++) {
                pktBazowe.add(calaBazaPkt.get(randomik * (listaDESCGenerowanychPrzezAlgorytm + 1) + k));
            }
        }
        File fileOut = new File(pathToDataBase + "\\baskets\\SrodkiPrzedzialow.txt");
        FileWriter zapis = new FileWriter(fileOut, true);
        zapis.write(pktBazowe.toString().replaceAll(",", ""));
        zapis.close();
    }

    /**
     * dla testu
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        GeneratorKoszykowNEW generatorKoszykowNEW = new GeneratorKoszykowNEW();

        generatorKoszykowNEW.generujKoszyki(PATH_TO_RZESZOW_DATABASE, 200, SUFR);

    }

}
