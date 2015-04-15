/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package boofcvsurfforstudents;

import boofcv.abst.feature.detect.extract.ConfigExtract;
import boofcv.abst.feature.detect.extract.NonMaxSuppression;
import boofcv.abst.feature.orientation.OrientationIntegral;
import boofcv.alg.feature.describe.DescribePointSurf;
import boofcv.alg.feature.detect.interest.FastHessianFeatureDetector;
import boofcv.alg.transform.ii.GIntegralImageOps;
import boofcv.core.image.GeneralizedImageOps;
import boofcv.factory.feature.describe.FactoryDescribePointAlgs;
import boofcv.factory.feature.detect.extract.FactoryFeatureExtractor;
import boofcv.factory.feature.orientation.FactoryOrientationAlgs;
import boofcv.gui.feature.FancyInterestPointRender;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.BoofDefaults;
import boofcv.struct.feature.ScalePoint;
import boofcv.struct.feature.SurfFeature;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageSingleBand;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.io.PrintWriter;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

/**
 *
 * Na podstawie http://boofcv.org/index.php?title=Example_SURF_Feature
 */
public class BoofcvSURFforStudents {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)   throws IOException
    {
        // SURF generuje punkty na obrazie i opisuje każdy z nich za pomocą 64 wartości.
        
    String obrazek="img2.jpg";
    // PrintWriter zapis = new PrintWriter("G:/testy/img1PKT.txt");
     PrintWriter zapis2 = new PrintWriter("G:/testy/img2DESC.txt");
     FileWriter fw = new FileWriter("G:/testy/img2PKT.txt");
     BufferedWriter bw = new BufferedWriter(fw);
     
    int liczbaPunktowOktawa=5;
    
    List<ScalePoint> punkty= new ArrayList<>();
    List<SurfFeature> deskryptory = new ArrayList<>();
           String pom="";
    String pom3="";  

    
    generujPunktySurf(obrazek, punkty, deskryptory, liczbaPunktowOktawa);                
    System.out.println("Odnalezione cechy: "+punkty.size());

    String wartosciPrzykladowe="";

    
    int j=0;
    int k=0;
    
   // for(ScalePoint it2: punkty){
    
               

               for(SurfFeature it : deskryptory)
               {
                   bw.write(punkty.get(j).toString());
                   bw.newLine();
                   //System.out.println("PETLA1");
                       for(int i=0;i<deskryptory.get(j).value.length;i++)//zerowy zobaczmy
                       {//System.out.println(i+ "  POM  = "+pom);
                            // wartosciPrzykladowe+="\n"+ i +" |  " +deskryptory.get(1).value[i];
                              pom=deskryptory.get(j).value[i] + " ";
                               bw.write(pom);
                               bw.newLine();
                              //System.out.println(" POM = "+);
                       }
                      // pom+="\n\n\nKOLEJNY PKT \n\n\n";
                     //  pom+= "\n";
                       j++;
                      // System.out.println(" JJJJJJJJJ =  " + j);

               }
               j=0;
               
               //pom3+=pom;
             //  pom="";
                //System.out.println(" kkkk =  " + k);
          k++;      
    //}
   
    //System.out.println("Wartości w deskryptorze 1= "+wartosciPrzykladowe);   
       
    wyswietlRezultat( UtilImageIO.loadImage(obrazek) ,punkty);
    // bw.write(pom3);
   //  bw.newLine();
     zapis2.write(punkty.toString());
    //zapis.write(deskryptory.get(j).value.toString() );
     
        bw.close();
        fw.close();
      //zapis.close();
      zapis2.close();
    }

 public static  void generujPunktySurf (String plik,List<ScalePoint> pkty,  List<SurfFeature> descry, int liczbaPunktowOktawa)  
 {      //   System.out.println( plik);
          ImageFloat32 image = UtilImageIO.loadImage(plik,ImageFloat32.class);
        
// SURF works off of integral images
		Class<ImageSingleBand> integralType = GIntegralImageOps.getIntegralType(ImageFloat32.class);
 
		// define the feature detection algorithm
		NonMaxSuppression extractor =
				FactoryFeatureExtractor.nonmax(new ConfigExtract(2, 0, 5, true));
		FastHessianFeatureDetector<ImageSingleBand> detector = 
				new FastHessianFeatureDetector<>(extractor,liczbaPunktowOktawa,2, 9,4,4);
 
		// estimate orientation
		OrientationIntegral<ImageSingleBand> orientation = 
				FactoryOrientationAlgs.sliding_ii(null, integralType);
 
		DescribePointSurf<ImageSingleBand> descriptor = FactoryDescribePointAlgs.<ImageSingleBand>surfStability(null,integralType);
 
		// compute the integral image of 'image'
		ImageSingleBand integral = GeneralizedImageOps.createSingleBand(integralType,image.width,image.height);
		GIntegralImageOps.transform(image, integral);
 
		// detect fast hessian features
		detector.detect(integral);
		// tell algorithms which image to process
		orientation.setImage(integral);
		descriptor.setImage(integral);
 
		List<ScalePoint> points = detector.getFoundPoints();
 
		List<SurfFeature> descriptions = new ArrayList<>();
 
		for( ScalePoint p : points ) 
                {
			// estimate orientation
			orientation.setScale(p.scale);
			double angle = orientation.compute(p.x,p.y);
 
			// extract the SURF description for this region
			SurfFeature desc = descriptor.createDescription();
			descriptor.describe(p.x,p.y,angle,p.scale,desc);
  
			// save everything for processing later on
			descriptions.add(desc);
		}
 
                pkty.addAll(points);
                descry.addAll(descriptions);
                
                
                
		 
     
 } 


private static   void wyswietlRezultat(BufferedImage image,
	  List<ScalePoint> points)
	{
		Graphics2D g2 = image.createGraphics();
		FancyInterestPointRender render = new FancyInterestPointRender();
                for( ScalePoint p : points )
                     render.addCircle((int)p.x,(int)p.y, (int)(p.scale* BoofDefaults.SCALE_SPACE_CANONICAL_RADIUS));
                  
		g2.setStroke(new BasicStroke(2)); 
		 
		render.draw(g2);
		ShowImages.showWindow(image, "Liczba punktów=  "+points.size() + "");
                
	}
}
