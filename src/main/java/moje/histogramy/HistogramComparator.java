package moje.histogramy;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by JaroLP on 2015-10-31.
 * <p>
 * Klasa porównujaca histogramy;
 * Na HashMapach
 */
public class HistogramComparator {


    public final static String PATH_TO_RZESZOW_DATABASE = "D:\\MAGISTERKA\\BazyZdjec\\Rzeszow";
    public final static String PATH_FOR_COMPARING_FILES = "D:\\MAGISTERKA\\TESTY";

    public HashMap<String, String> comparator(File fileToCompare, String pathToBaseForSearching, File[] dataBaseDesc, int numberOfBaskets) throws IOException {

        LoaderHistograms um = new LoaderHistograms();

        int suma;
        int sumaPom = 0;
        String typ = "";

        HashMap<Integer, Integer> map1 = um.fileToHasMap(fileToCompare);
        //Wczytanie pierwszego obrazka

        HashMap<String, String> wynikPorownania = new HashMap<>();

        File[] listOfFiles;

        if (pathToBaseForSearching != null) {
            File folder = new File(pathToBaseForSearching + "\\Histogramy");
            listOfFiles = folder.listFiles();
        } else {
            listOfFiles = dataBaseDesc;
        }

        for (File file : listOfFiles) {
            if (file != null && file.isFile()) {

                suma = 0;

                HashMap<Integer, Integer> map2 = um.fileToHasMap(file); //wczytanie bazowej mapy

                for (Integer z = 0; z < numberOfBaskets; z++) {
                    if (map1.get(z) != 0 && map2.get(z) != 0) {
                        if (map1.get(z) < map2.get(z))
                            suma += map1.get(z);
                        else
                            suma += map2.get(z);
                    }
                }
                if (suma > sumaPom) {
                    sumaPom = suma;
                    typ = file.getName();
                }
            }
        }
        wynikPorownania.put(fileToCompare.getName(), typ);
        return wynikPorownania;
    }

    public static void main(String[] args) throws IOException {
        HistogramComparator histogramComparator = new HistogramComparator();
        File file = new File("D:\\MAGISTERKA\\BazyZdjec\\Rzeszow\\Histogramy\\Histogram_00_01_04.jpg.DESC.txt");
        System.out.println(histogramComparator.comparator(file, PATH_TO_RZESZOW_DATABASE, null, 200).toString());

    }
}