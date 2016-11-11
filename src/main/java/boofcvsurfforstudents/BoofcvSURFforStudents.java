/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package boofcvsurfforstudents;

import boofcv.abst.feature.detect.extract.ConfigExtract;
import boofcv.abst.feature.detect.extract.NonMaxSuppression;
import boofcv.abst.feature.orientation.OrientationIntegral;
import boofcv.alg.feature.describe.DescribePointBrief;
import boofcv.alg.feature.describe.DescribePointSurf;
import boofcv.alg.feature.describe.brief.FactoryBriefDefinition;
import boofcv.alg.feature.detect.interest.FastHessianFeatureDetector;
import boofcv.alg.transform.ii.GIntegralImageOps;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.core.image.GeneralizedImageOps;
import boofcv.factory.feature.describe.FactoryDescribePointAlgs;
import boofcv.factory.feature.detect.extract.FactoryFeatureExtractor;
import boofcv.factory.feature.orientation.FactoryOrientationAlgs;
import boofcv.factory.filter.blur.FactoryBlurFilter;
import boofcv.gui.feature.FancyInterestPointRender;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.BoofDefaults;
import boofcv.struct.feature.ScalePoint;
import boofcv.struct.feature.SurfFeature;
import boofcv.struct.feature.TupleDesc_B;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageSingleBand;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Na podstawie http://boofcv.org/index.php?title=Example_SURF_Feature
 */
public class BoofcvSURFforStudents {

    /**
     * @param args the command line arguments
     */
    private static final String rzeszowBase = "D:\\MAGISTERKA\\BazyZdjec\\Rzeszow";
    private static final String zuBuDuBase = "D:\\MAGISTERKA\\BazyZdjec\\ZuBuD";
    private static final String oxfordBase = "D:\\MAGISTERKA\\BazyZdjec\\oxbuild_images";

    public void generujPktSurf(String pathToFile) throws IOException {
        // SURF generuje punkty na obrazie i opisuje każdy z nich za pomocą 64 wartości.
        int liczbaPunktowOktawa = 500;
        File folder = new File(pathToFile);
        File[] listOfFiles = folder.listFiles();


        for (File file : listOfFiles) {
            List<ScalePoint> punkty = new ArrayList<>();
            List<SurfFeature> deskryptory = new ArrayList<>();
            if (file.isFile()) {

                PrintWriter zapis2 = null;
                String dirDESC = folder + "\\desc\\";
                String dirPOINTS = folder + "\\points\\";
                String dirBRIEF = folder + "\\BRIEF\\" + file.getName() + ".txt";

                try {
                    generujPunktySurf(file.toString(), punkty, deskryptory, liczbaPunktowOktawa);
                    boofCV_points_2_PoI_BRIEF(punkty, file.toString(), dirBRIEF);
                } catch (Exception e) {
                    continue;
                }


                try {
                    zapis2 = new PrintWriter(dirPOINTS + file.getName() + ".PKT.txt");
                    FileWriter fw = new FileWriter(dirDESC + file.getName() + ".DESC.txt");
                    BufferedWriter bw = new BufferedWriter(fw);
                    String pom = "";

                    int j = 0;

                    for (SurfFeature it : deskryptory) {
                        try {
                            bw.write(punkty.get(j).toString());
                            bw.newLine();

                            for (int i = 0; i < it.value.length; i++) {
                                pom = it.value[i] + " ";
                                bw.write(pom);
                                bw.newLine();
                            }
                            j++;

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    zapis2.write(punkty.toString());

                    bw.close();
                    fw.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                zapis2.close();
            }
        }


    }

    public static void generujPunktySurf(String plik, List<ScalePoint> pkty, List<SurfFeature> descry, int liczbaPunktowOktawa) {      //   System.out.println( plik);
        ImageFloat32 image = UtilImageIO.loadImage(plik, ImageFloat32.class);

// SURF works off of integral images
        Class<ImageSingleBand> integralType = GIntegralImageOps.getIntegralType(ImageFloat32.class);

        // define the feature detection algorithm
        NonMaxSuppression extractor =
                FactoryFeatureExtractor.nonmax(new ConfigExtract(2, 0, 5, true));
        FastHessianFeatureDetector<ImageSingleBand> detector =
                new FastHessianFeatureDetector<>(extractor, liczbaPunktowOktawa, 2, 9, 4, 4);

        // estimate orientation
        OrientationIntegral<ImageSingleBand> orientation =
                FactoryOrientationAlgs.sliding_ii(null, integralType);

        DescribePointSurf<ImageSingleBand> descriptor = FactoryDescribePointAlgs.<ImageSingleBand>surfStability(null, integralType);

        // compute the integral image of 'image'
        ImageSingleBand integral = GeneralizedImageOps.createSingleBand(integralType, image.width, image.height);
        GIntegralImageOps.transform(image, integral);

        // detect fast hessian features
        detector.detect(integral);
        // tell algorithms which image to process
        orientation.setImage(integral);
        descriptor.setImage(integral);

        List<ScalePoint> points = detector.getFoundPoints();

        List<SurfFeature> descriptions = new ArrayList<>();

        for (ScalePoint p : points) {
            // estimate orientation
            orientation.setScale(p.scale);
            double angle = orientation.compute(p.x, p.y);

            // extract the SURF description for this region
            SurfFeature desc = descriptor.createDescription();
            descriptor.describe(p.x, p.y, angle, p.scale, desc);

            // save everything for processing later on
            descriptions.add(desc);
        }

        pkty.addAll(points);
        descry.addAll(descriptions);


    }


    private static void wyswietlRezultat(BufferedImage image,
                                         List<ScalePoint> points) {
        Graphics2D g2 = image.createGraphics();
        FancyInterestPointRender render = new FancyInterestPointRender();
        for (ScalePoint p : points)
            render.addCircle((int) p.x, (int) p.y, (int) (p.scale * BoofDefaults.SCALE_SPACE_CANONICAL_RADIUS));

        g2.setStroke(new BasicStroke(2));

        render.draw(g2);
        ShowImages.showWindow(image, "Liczba punktów=  " + points.size() + "");

    }

    public static void boofCV_points_2_PoI_BRIEF(List<ScalePoint> mojePunkty, String obrazNazwa, String briefPath) throws FileNotFoundException {

        List<String> wyjscie = new ArrayList<>();
        BufferedImage tmp = null;
        PrintWriter zapis = new PrintWriter(briefPath);
        try {
            tmp = ImageIO.read(new File(obrazNazwa));
        } catch (IOException e) {
            System.out.println(e + " | " + obrazNazwa + " UWAGA");
        }

        ImageFloat32 input = ConvertBufferedImage.convertFromSingle(tmp, null, ImageFloat32.class);


        DescribePointBrief<ImageFloat32> brief = FactoryDescribePointAlgs.brief(FactoryBriefDefinition.gaussian(new Random(123), 16, 512),
                FactoryBlurFilter.gaussian(ImageFloat32.class, 0, 4));


        brief.setImage(input);
        TupleDesc_B f = brief.createFeature();


        for (ScalePoint moj : mojePunkty) {

            brief.process(moj.x, moj.y, f);

            List<Integer> deskryptor = new ArrayList<>();

            for (int i = 0; i < f.numBits; i++) {
                if (deskryptor.size() <= 520) {
                    if (f.isBitTrue(i)) {
                        deskryptor.add(1);
                    } else
                        deskryptor.add(0);
                }
            }

            wyjscie.add(deskryptor.toString());


        }

        zapis.write(String.valueOf(wyjscie));

    }

}
