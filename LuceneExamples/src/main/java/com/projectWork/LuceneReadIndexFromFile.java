package com.projectWork;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
 
public  class  LuceneReadIndexFromFile
{
    //directory contains the lucene indexes
    private static final String INDEX_DIR = "indexedFiles";
 
    public static void main(String[] args) throws Exception 
    {
        //Create lucene searcher. It search over a single IndexReader.
        IndexSearcher searcher = createSearcher();
         
        //Search indexed contents using search term
String inarg="inputFiles/002_0001.jpg";
        
	    ProcessBuilder pb = new ProcessBuilder("/Users/ruchirapatil/anaconda/bin/python","/Users/ruchirapatil/Documents/Workspace/ImageSearch/shapeData.py",inarg);
	    Process p = pb.start();
	    
	   // ProcessBuilder("/Users/ruchirapatil/anaconda/bin/python","sentiment.py");
	   // Process p = Runtime.getRuntime().exec("/Users/ruchirapatil/anaconda/bin/python", "Python histRGB.py "+"/IMG_8179 2.jpg");
	    //Process p =  Runtime.getRuntime().exec("Python histRGB.py "+"/IMG_8179 2.jpg");  
	    BufferedReader stdInput = new BufferedReader(new 
                 InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(p.getErrorStream()));
            String histString =findProperty("RGBHistogram",inarg);
            System.out.println(histString);
        
        TopDocs foundDocs = searchInRGB(histString, searcher);
         
        //Total found documents
        System.out.println("Total Results :: " + foundDocs.totalHits);
         
        //Let's print out the path of files which have searched term
        for (ScoreDoc sd : foundDocs.scoreDocs) 
        {
            Document d = searcher.doc(sd.doc);
            System.out.println("Path : "+ d.get("path") + ", Score : " + sd.score);
        }
    }
    
    
    private static String findProperty(String propertyName,String fileLoc) throws IOException,IllegalArgumentException {
		// TODO Auto-generated method stub
		
		String fileName="";
		switch(propertyName)
		{
		
		case "similarity":fileName="similarity.py";
        break;
case "shape":fileName="shapeData.py";
        break;  
case "colorHistogram":fileName="averageColour.py";
        break;
                   
		
		}
		
		  
		    ProcessBuilder pb = new ProcessBuilder("/Users/ruchirapatil/anaconda/bin/python","/Users/ruchirapatil/Downloads/LuceneExamples/"+fileName,fileLoc);
		    Process p = pb.start();
		    
		   // ProcessBuilder("/Users/ruchirapatil/anaconda/bin/python","sentiment.py");
		   // Process p = Runtime.getRuntime().exec("/Users/ruchirapatil/anaconda/bin/python", "Python histRGB.py "+"/IMG_8179 2.jpg");
		    //Process p =  Runtime.getRuntime().exec("Python histRGB.py "+"/IMG_8179 2.jpg");  
		    BufferedReader stdInput = new BufferedReader(new 
	                 InputStreamReader(p.getInputStream()));

	            BufferedReader stdError = new BufferedReader(new 
	                 InputStreamReader(p.getErrorStream()));
	           String histString = stdInput.readLine();
	            System.out.println(histString);
	            
	          
	            p.destroy();  
		return histString;
	}
    
    

    
    public ArrayList<Result> searchImageForRGB(String imagePath) throws Exception
    {
    	ArrayList listOfPath=new ArrayList();
    	
    	//Create lucene searcher. It search over a single IndexReader.
        IndexSearcher searcher = createSearcher();
         
        //Search indexed contents using search term
String inarg=imagePath;
        
//	    ProcessBuilder pb = new ProcessBuilder("/Users/ruchirapatil/anaconda/bin/python","/Users/ruchirapatil/Documents/Workspace/ImageSearch/shapeData.py",inarg);
//	    Process p = pb.start();
//	    
//	   // ProcessBuilder("/Users/ruchirapatil/anaconda/bin/python","sentiment.py");
//	   // Process p = Runtime.getRuntime().exec("/Users/ruchirapatil/anaconda/bin/python", "Python histRGB.py "+"/IMG_8179 2.jpg");
//	    //Process p =  Runtime.getRuntime().exec("Python histRGB.py "+"/IMG_8179 2.jpg");  
//	    BufferedReader stdInput = new BufferedReader(new 
//                 InputStreamReader(p.getInputStream()));
//
//            BufferedReader stdError = new BufferedReader(new 
//                 InputStreamReader(p.getErrorStream()));
//Calculating Image descriptor
// String histogram=findProperty("RGBHistogram",file.toString());
 String histProperty=findProperty("colorHistogram",imagePath);
 String shapeString=findProperty("shape",imagePath);
 Float similarityIndex=Float.parseFloat(findProperty("similarity",imagePath));

 histProperty=histProperty.replaceAll("[\\[\\](){}]","");
 
 System.out.println(histProperty);
 
 String[] colors=histProperty.trim().split("\\s+");
 String[] shapes=shapeString.trim().split("\\s+");



//           String histString =findProperty("colorHistogram",imagePath);
//            System.out.println(histString);
//            
//            histString=histString.replaceAll("[\\[\\](){}]","");
//            
//            System.out.println(histString);
//            
//            String[] colors=histString.trim().split("\\s+");
//            System.out.println(histString);
//            System.out.println(colors);
//
//            float red=0;
//            float green = 0;
//            float blue=0;
//           		 boolean flag1=false;
//            if(colors.length==3)
//            {
//           	 flag1=true;
//           	
//             red=Float.valueOf(colors[2]);
//             green=Float.valueOf(colors[1]);
//             blue=Float.valueOf(colors[0]);
//            }
//            
            
            
        
        TopDocs foundDocs = searchAll(colors, shapes, similarityIndex, searcher);
         
        //Total found documents
        System.out.println("Total Results :: " + foundDocs.totalHits);
         
        //Let's print out the path of files which have searched term
        for (ScoreDoc sd : foundDocs.scoreDocs) 
        {
            Document d = searcher.doc(sd.doc);
            Result rs=new Result();
            rs.setPath(d.get("path"));
            rs.setScore(Float.toString(sd.score));
            listOfPath.add(rs);
            System.out.println("Path : "+ d.get("path") + ", Score : " + sd.score);
        }
    	
		return listOfPath;
    	
    }
    
    
    
    
    public ArrayList<Result> searchImage(String imagePath) throws Exception
    {
    	ArrayList listOfPath=new ArrayList();
    	
    	//Create lucene searcher. It search over a single IndexReader.
        IndexSearcher searcher = createSearcher();
         
        //Search indexed contents using search term
String inarg=imagePath;
        
//	    ProcessBuilder pb = new ProcessBuilder("/Users/ruchirapatil/anaconda/bin/python","/Users/ruchirapatil/Documents/Workspace/ImageSearch/shapeData.py",inarg);
//	    Process p = pb.start();
//	    
//	   // ProcessBuilder("/Users/ruchirapatil/anaconda/bin/python","sentiment.py");
//	   // Process p = Runtime.getRuntime().exec("/Users/ruchirapatil/anaconda/bin/python", "Python histRGB.py "+"/IMG_8179 2.jpg");
//	    //Process p =  Runtime.getRuntime().exec("Python histRGB.py "+"/IMG_8179 2.jpg");  
//	    BufferedReader stdInput = new BufferedReader(new 
//                 InputStreamReader(p.getInputStream()));

//            BufferedReader stdError = new BufferedReader(new 
//                 InputStreamReader(p.getErrorStream()));
           String histString =findProperty("RGBHistogram",imagePath);
            System.out.println(histString);
        
        TopDocs foundDocs = searchInRGB(histString, searcher);
         
        //Total found documents
        System.out.println("Total Results :: " + foundDocs.totalHits);
         
        //Let's print out the path of files which have searched term
        for (ScoreDoc sd : foundDocs.scoreDocs) 
        {
            Document d = searcher.doc(sd.doc);
            Result rs=new Result();
            rs.setPath(d.get("path"));
            rs.setScore(Float.toString(sd.score));
            listOfPath.add(rs);
            System.out.println("Path : "+ d.get("path") + ", Score : " + sd.score);
        }
    	
		return listOfPath;
    	
    }
    
   
     
    private static TopDocs searchShape(String textToFind, IndexSearcher searcher) throws ParseException, IOException {
		// TODO Auto-generated method stub
    	//Calculating Image descriptor
        
            //Create search query
            QueryParser qp = new QueryParser("shapeProperty", new StandardAnalyzer());
            Query query = qp.parse(textToFind);
             
            //search the index
            TopDocs hits = searcher.search(query, 10);
            return hits;
        
	}
    
    private static TopDocs searchInRGBIndividually(Float sred,Float sgreen,Float sblue, IndexSearcher searcher) throws ParseException, IOException {
    	//  StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
    	  // red query
    	 // QueryParser redQP = new QueryParser("red", new StandardAnalyzer());
    	  //Query redQuery = redQP.parse(sred);
    	  
    	  Query redQuery = NumericRangeQuery.newFloatRange("red", sred-5.03f, sred+5.10f, true, true);
    	  Query greenQuery = NumericRangeQuery.newFloatRange("green", sgreen-5.03f, sgreen+5.10f, true, true);
    	  Query blueQuery = NumericRangeQuery.newFloatRange("blue", sblue-5.03f, sblue+5.10f, true, true);

    
    	  // final query
    	  BooleanQuery finalQuery = new BooleanQuery();
    	  finalQuery.add(redQuery, Occur.MUST); // MUST implies that the keyword must occur.
    	  finalQuery.add(greenQuery, Occur.MUST); // Using all "MUST" occurs is equivalent to "AND" operator.
    	  finalQuery.add(blueQuery, Occur.MUST);
    	  //search the index
          TopDocs hits = searcher.search(finalQuery, 100);
          return hits;
          
  	}
    
    
    private static TopDocs searchAll(String[] colors,String[] shapes,float similarity, IndexSearcher searcher) throws ParseException, IOException {
    	//  StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
    	  // red query
    	 // QueryParser redQP = new QueryParser("red", new StandardAnalyzer());
    	  //Query redQuery = redQP.parse(sred);
    	
    	  float red=0;
          float green = 0;
          float blue=0;
         		 boolean flag1=false;
          if(colors.length==3)
          {
         	 flag1=true;
         	
           red=Float.valueOf(colors[2]);
           green=Float.valueOf(colors[1]);
           blue=Float.valueOf(colors[0]);
          }
          
          float shapeA=0;
          float shapeB = 0;
          float shapeC=0;
         		// boolean flag1=false;
          if(colors.length==3)
          {
         	 flag1=true;
         	
         	 shapeA=Float.valueOf(shapes[0]);
         	 shapeB=Float.valueOf(shapes[1]);
         	 shapeC=Float.valueOf(shapes[2]);
          }
    	
    	
    	  
    	  Query redQuery = NumericRangeQuery.newFloatRange("red", red-15.03f, red+15.10f, true, true);
    	  Query greenQuery = NumericRangeQuery.newFloatRange("green", green-15.03f, green+15.10f, true, true);
    	  Query blueQuery = NumericRangeQuery.newFloatRange("blue", blue-15.03f, blue+15.10f, true, true);

    	  Query shapeAQuery = NumericRangeQuery.newFloatRange("shapeA", shapeA-20.03f, shapeA+20.10f, true, true);
    	  Query shapeBQuery = NumericRangeQuery.newFloatRange("shapeB", shapeB-20.03f, shapeB+20.10f, true, true);
    	  Query shapeCQuery = NumericRangeQuery.newFloatRange("shapeC", shapeC-20.03f, shapeC+20.10f, true, true);
    	  
    	 // Query similarityQuery = NumericRangeQuery.newFloatRange("similarityIndex", similarity-28.03f, similarity+28.10f, true, true);
    
    	  // final query
    	  BooleanQuery finalQuery = new BooleanQuery();
    	  finalQuery.add(redQuery, Occur.MUST); // MUST implies that the keyword must occur.
    	  finalQuery.add(greenQuery, Occur.MUST); // Using all "MUST" occurs is equivalent to "AND" operator.
    	  finalQuery.add(blueQuery, Occur.MUST);
    	  
    	  finalQuery.add(shapeAQuery, Occur.SHOULD); // MUST implies that the keyword must occur.
    	  finalQuery.add(shapeBQuery, Occur.SHOULD); // Using all "MUST" occurs is equivalent to "AND" operator.
    	  finalQuery.add(shapeCQuery, Occur.SHOULD);
    	  
    	 // finalQuery.add(similarityQuery, Occur.MUST);
    	  //search the index
          TopDocs hits = searcher.search(finalQuery, 200);
          return hits;
          
  	}
    
   private static TopDocs searchInRGB(String textToFind, IndexSearcher searcher) throws ParseException, IOException {
		// TODO Auto-generated method stub
    	//Calculating Image descriptor
        
            //Create search query
            QueryParser qp = new QueryParser("rgbhistogram", new StandardAnalyzer());
            Query query = qp.parse(textToFind);
             
            //search the index
            TopDocs hits = searcher.search(query, 10);
            return hits;
        
	}

	private static TopDocs searchInContent(String textToFind, IndexSearcher searcher) throws Exception
    {
        //Create search query
        QueryParser qp = new QueryParser("contents", new StandardAnalyzer());
        Query query = qp.parse(textToFind);
         
        //search the index
        TopDocs hits = searcher.search(query, 10);
        return hits;
    }
 
    private static IndexSearcher createSearcher() throws IOException 
    {
        Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
         
        //It is an interface for accessing a point-in-time view of a lucene index
        IndexReader reader = DirectoryReader.open(dir);
         
        //Index searcher
        IndexSearcher searcher = new IndexSearcher(reader);
        return searcher;
    }


	public int scoreFilesForSimilarity(String candidateImagePath,String searchedImagePath) throws IOException {
		// TODO Auto-generated method stub
		System.out.println(candidateImagePath);
		System.out.println(searchedImagePath);
		 ProcessBuilder pb = new ProcessBuilder("/Users/ruchirapatil/anaconda/bin/python","/Users/ruchirapatil/Documents/workspace/ImageSearch/siftDetection.py",candidateImagePath,searchedImagePath);
		    Process p = pb.start();
		    
		   BufferedReader stdInput = new BufferedReader(new 
	                 InputStreamReader(p.getInputStream()));

	            BufferedReader stdError = new BufferedReader(new 
	                 InputStreamReader(p.getErrorStream()));
	           String similarityScore = stdInput.readLine();
	            System.out.println(similarityScore);
	            
	            p.destroy();    
		
		return Integer.parseInt(similarityScore);
		
	}
}