package moje.badania;

import moje.histogramy.HistogramComparator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import static moje.histogramy.HistogramComparator.PATH_TO_RZESZOW_DATABASE;

/**
 * Created by JaroLP on 2016-11-12.
 */
public class Test1 {

    public final static String PATH_TO_TEST_FOLDER = "D:\\MAGISTERKA\\TESTY\\";

    /**
     * TEST LOSUJE LICZBE ZDJĘC DO ROZPOZNANIA Z BAZY I ZWRACA WYNIKI POROWNANIA W POSTACI HASH MAP.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Test1 test1 = new Test1();
        HashMap[] wynikiOstateczne;

        wynikiOstateczne = test1.test1(20, PATH_TO_RZESZOW_DATABASE);

        File fileOut = new File(PATH_TO_TEST_FOLDER + "\\test1Porownanie.txt");
        FileWriter zapis = new FileWriter(fileOut, true);
        zapis.write(wynikiOstateczne[0].toString());
        zapis.close();

        File fileOut2 = new File(PATH_TO_TEST_FOLDER + "\\test1SWyniki.txt");
        FileWriter zapis2 = new FileWriter(fileOut2, true);
        zapis2.write(wynikiOstateczne[1].toString());
        zapis2.close();
    }

    public HashMap[] test1(int liczbaObrazowDoLosowanychDoTestow, String bazaDanych) throws IOException {

        File folder = new File(bazaDanych + "\\Histogramy\\");
        File[] listOfFiles = folder.listFiles();
        Random random = new Random();

        HashMap<String, String> wyniki = new HashMap<>();
        HashMap<String, Integer> podsumowanie = new HashMap<>();

        HistogramComparator histogramComparator = new HistogramComparator();

        File[] wylosowaneObrazy = new File[liczbaObrazowDoLosowanychDoTestow];

        for (int i = 0; i < liczbaObrazowDoLosowanychDoTestow; i++) {
            int pozycja = random.nextInt(listOfFiles.length);
            wylosowaneObrazy[i] = listOfFiles[pozycja];
            listOfFiles[pozycja] = null;
        }

        int liczbaPoprawnych = 0;

        for (File fileToCompare : wylosowaneObrazy) {
            wyniki.putAll(histogramComparator.comparator(fileToCompare, null, listOfFiles, 200));
            podsumowanie.putAll(statystyka(fileToCompare.getName(), wyniki.get(fileToCompare.getName())));
            if(podsumowanie.get(fileToCompare.getName()) == 1){
                liczbaPoprawnych++;
            }
        }
        podsumowanie.put("POPRAWNYCH", liczbaPoprawnych);
        HashMap[] wynikiOstaeczne = new HashMap[2];
        wynikiOstaeczne[0] = wyniki;
        wynikiOstaeczne[1] = podsumowanie;

        return wynikiOstaeczne;
    }

    /**
     * TWORZY HASH MAPE KTÓREJ KLUCZEM JEST NAZWA ROZPOZNAWANEGO OBRAZU NATOMIAST KLUCZEM JEST WARTOŚĆ 1 LUB 0
     * @param nazwaPorownywanego
     * @param typ
     * @return
     */
    public HashMap<String, Integer> statystyka(String nazwaPorownywanego, String typ) {
        HashMap<String, Integer> podsumowanie = new HashMap<>();

        if (nazwaPorownywanego.contains(typ.substring(0, 13))) {
            podsumowanie.put(nazwaPorownywanego, 1);
        } else {
            podsumowanie.put(nazwaPorownywanego, 0);
        }

        return podsumowanie;
    }

}

