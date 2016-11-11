package moje;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by JaroLP on 2015-10-31.
 */
public class GeneratorHistogramowNEW {
    /**
     * GENERATOR HISTOGRAMÓW DLA WSZYSTKICH OBRAZÓW
     * @param args
     * @throws IOException
     */


    public final static String PATH_TO_RZESZOW_DATABASE = "D:\\MAGISTERKA\\BazyZdjec\\Rzeszow";

    public final static String PATH_TO_RZESZOW_DATABASE_DESC = PATH_TO_RZESZOW_DATABASE + "\\desc";

    public final static String PATH_TO_RZESZOW_DATABASE_POINTS = PATH_TO_RZESZOW_DATABASE + "\\points";

    public final static String PATH_TO_SAVE_HISTOGRAMS_FOR_RZESZOW = PATH_TO_RZESZOW_DATABASE + "\\histogramy";



    public static void generujHistogramyDlaBazy(String pathToDataBase, int liczbaPktGenerowanychPrzezAlgorytm, int liczbaKoszykow) throws IOException {

        GeneratorHistogramow2NEW um = new GeneratorHistogramow2NEW();

        File folder = new File(pathToDataBase + "\\desc");
        File[] listOfFiles = folder.listFiles();

        for (File fileWithDESC : listOfFiles){

            if (fileWithDESC.isFile()) {

                    ArrayList<String> lista = um.pktToArrayList(fileWithDESC,liczbaPktGenerowanychPrzezAlgorytm);
                    HashMap histogram = um.histogramGenerator(liczbaKoszykow, lista, liczbaPktGenerowanychPrzezAlgorytm);

                    File plik = new File(pathToDataBase + "\\Histogramy\\Histogram_" + fileWithDESC.getName());
                    FileWriter zapis = new FileWriter(plik, true);
                    zapis.write(histogram.toString());
                    zapis.close();

            }
        }
    }

    /**
     * DLA TESTU
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        generujHistogramyDlaBazy(PATH_TO_RZESZOW_DATABASE,64,200);

       // GeneratorKoszykowNEW generatorKoszykowNEW = new GeneratorKoszykowNEW();

       // generatorKoszykowNEW.generujKoszyki(PATH_TO_RZESZOW_DATABASE_DESC, 200, 64);

    }
}