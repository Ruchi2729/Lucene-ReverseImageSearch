package com.projectWork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FloatField;

import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
 
public class LuceneWriteIndexFromFile
{
    public static void main(String[] args)
    {
        //Input folder
        String docsPath = "inputFiles2";
         
        //Output folder
        String indexPath = "indexedFiles";
 
        //Input Path Variable
        final Path docDir = Paths.get(docsPath);
 
        try
        {
            //org.apache.lucene.store.Directory instance
            Directory dir = FSDirectory.open( Paths.get(indexPath) );
             
            //analyzer with the default stop words
            Analyzer analyzer = new StandardAnalyzer();
             
            //IndexWriter Configuration
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
             
            //IndexWriter writes new index files to the directory
            IndexWriter writer = new IndexWriter(dir, iwc);
             
            //Its recursive method to iterate all files and directories
            indexDocs(writer, docDir);
 
            writer.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
     
    static void indexDocs(final IndexWriter writer, Path path) throws IOException 
    {
        //Directory?
        if (Files.isDirectory(path)) 
        {
            //Iterate directory
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() 
            {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException 
                {
                    try
                    {
                        //Index this file
                        indexDoc1(writer, file, attrs.lastModifiedTime().toMillis());
                    } 
                    catch (IOException ioe) 
                    {
                        ioe.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } 
        else
        {
            //Index this file
            indexDoc1(writer, path, Files.getLastModifiedTime(path).toMillis());
        }
    }
 
    
    
    static void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException 
    {
        try (InputStream stream = Files.newInputStream(file)) 
        {
            //Create lucene Document
            Document doc = new Document();
             System.out.print("File Info"+file.toString());
         
     		
     	
             
             
             //Calculating Image descriptor
             String histogram=findProperty("RGBHistogram",file.toString());
             String histDist=findProperty("colorHistogram",file.toString());
             
             
             System.out.println(histDist);
           
             
             if(histogram!=null&&histDist!=null)
             {
             
          doc.add(new TextField("rgbhistogram", histogram, Field.Store.YES));
         // doc.add(new TextField("shapeProperty", shapeProperty, Field.Store.YES));
           
            doc.add(new StringField("path", file.toString(), Field.Store.YES));
          //  doc.add(new LongPoint("modified", lastModified));
            doc.add(new TextField("contents", new String(Files.readAllBytes(file)), Store.YES));
             
            //Updates a document by first deleting the document(s) 
            //containing <code>term</code> and then adding the new
            //document.  The delete and then add are atomic as seen
            //by a reader on the same index
            writer.updateDocument(new Term("path", file.toString()), doc);
             }
        }
    }
    
    
    static void indexDoc1(IndexWriter writer, Path file, long lastModified) throws IOException 
    {
        try (InputStream stream = Files.newInputStream(file)) 
        {
            //Create lucene Document
            Document doc = new Document();
             System.out.println("File Info"+file.toString());
         
     		
     	
             
             
             //Calculating Image descriptor
            // String histogram=findProperty("RGBHistogram",file.toString());
             String histProperty=findProperty("colorHistogram",file.toString());
             String shapeString=findProperty("shape",file.toString());
             float similarityIndex=Float.parseFloat(findProperty("similarity",file.toString()));
           
             histProperty=histProperty.replaceAll("[\\[\\](){}]","");
             
             System.out.println(histProperty);
             
             String[] colors=histProperty.trim().split("\\s+");
             String[] shapes=shapeString.trim().split("\\s+");
             System.out.println(histProperty);
             System.out.println(colors);
      
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
             else
             {
            	 flag1=false;
            	 
             }
             
             
             //System.out.print("see: "+Float.valueOf(colors[1]));
            
             if(flag1)
             {
             
         // doc.add(new TextField("rgbhistogram", histogram, Field.Store.YES));
          doc.add(new FloatField("shapeA", shapeA , Field.Store.NO));
          doc.add(new FloatField("shapeB", shapeB , Field.Store.NO));
          doc.add(new FloatField("shapeC", shapeC , Field.Store.NO));
          doc.add(new FloatField("green",green,Field.Store.NO));
        doc.add(new FloatField("red",red,Field.Store.NO));
        doc.add(new FloatField("blue",blue,Field.Store.NO));
        
        
        doc.add(new FloatField("similarityIndex",similarityIndex,Field.Store.YES));
        doc.add(new StringField("path", file.toString(), Field.Store.YES));
            //doc.add(new LongPoint("modified", lastModified));
           // doc.add(new TextField("contents", new String(Files.readAllBytes(file)), Store.YES));
             
            //Updates a document by first deleting the document(s) 
            //containing <code>term</code> and then adding the new
            //document.  The delete and then add are atomic as seen
            //by a reader on the same index
            writer.updateDocument(new Term("path", file.toString()), doc);
             }
        }
        catch(Exception e)
        {
        	
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
}
