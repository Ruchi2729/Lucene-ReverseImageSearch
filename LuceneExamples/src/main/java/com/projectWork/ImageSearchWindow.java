package com.projectWork;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.TextField;
import javax.swing.JLabel;
import java.awt.Label;
import java.awt.RenderingHints;
import javax.swing.BoxLayout;

public class ImageSearchWindow extends JFrame {

	private String imagePath;
	private int previousResult=0;
	 
	private int category1=98;
	private int category13=98;
	private int category2=97;
	private int category24=112;
	private int category56=102;
	private int category159=209;
	private int category251=800;
	private int totalRetrivedInstances=0;
	private int totalRetrivedRelevantInstances=0;
	private int totalRelevantInstances=0;
	float precision;
	float recall;
	float f1Score;
	ArrayList<Result> docs;
	 //directory contains the lucene indexes
	LuceneReadIndexFromFile indexFile=new LuceneReadIndexFromFile();
	private final JPanel mainPanel = new JPanel();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImageSearchWindow frame = new ImageSearchWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public String selectFile() {
        JFileChooser chooser = new JFileChooser();
        // optionally set chooser options ...
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            return f.getPath().toString();
            // read  and/or display the file somehow. ....
        } else {
            // user changed their mind
        	
        	return "No File Chosen....";
        }
    }
	/**
	 * Create the frame.
	 */
	public ImageSearchWindow() {
		getContentPane().setBackground(Color.PINK);
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(null);
		
	
		
		TextField filePath = new TextField();
		filePath.setBounds(147, 29, 228, 39);
		mainPanel.add(filePath);
		
		JLabel imageWindow = new JLabel("Upload Image....");
		imageWindow.setBounds(6, 125, 200, 200);
		mainPanel.add(imageWindow);
		
		
		
		Button Browse = new Button("BrowseFile....");
		Browse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// selectFile() ;
				String imgPath=selectFile();
				imagePath=imgPath;
				 filePath.setText(imgPath);
				 try {
			         BufferedImage img = ImageIO.read(new File(imgPath));
			         BufferedImage resizedImage=resizeImage(img,200,200);
			         System.out.println("Load image into frame...");
			         ImageIcon   icon=new ImageIcon(resizedImage);
			        // ImageIcon icon = new ImageIcon(img);
			         //JLabel label = new JLabel(icon);
			         imageWindow.setIcon(icon);
			         
			         
			         //JOptionPane.showMessageDialog(null, label);
			      } catch (IOException e1) {
			         e1.printStackTrace();
			      }
				 
			
				
			}
		});
		Browse.setBackground(Color.ORANGE);
		Browse.setBounds(6, 10, 117, 66);
		mainPanel.add(Browse);
		
		JPanel panel1 = new JPanel();
		panel1.setBounds(390, 12, 450, 420);
		mainPanel.add(panel1);
		
		JPanel panel2 = new JPanel();
		panel2.setBounds(6, 444, 2000, 147);
		mainPanel.add(panel2);
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
		
		Button searchImages = new Button("Average Coloured Images");
		searchImages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				setCalculationVarables();
				
				try {
					
							
//					
					
					long start = System.currentTimeMillis();
			
					
						
					docs = indexFile.searchImageForRGB(imagePath);
					long time = System.currentTimeMillis() - start;
					
					
					int totalDocumentResults=docs.size();
					totalRetrivedInstances=totalDocumentResults;
					Double numberOfSquaresInOneRow=Math.sqrt(totalDocumentResults);
					int absoluteNumberOfSquaresInOneRow = 1+(int) Math.abs(numberOfSquaresInOneRow);
					
					int height=400/absoluteNumberOfSquaresInOneRow;
					int k=0;
					for(int i=0;i<absoluteNumberOfSquaresInOneRow;i++)
					{
						for(int j=0;j<absoluteNumberOfSquaresInOneRow;j++)
						{
							if(k<totalDocumentResults)
							{
								
							JLabel label = new JLabel();
							label.setBounds(387+(i*height), 49+(j*height), height, height);
							getContentPane().add(label);
							 System.out.println("Load image into frame..."+docs.get(k).getPath());
							 if(isRelevantImage(docs.get(k).getPath()))
							 {
								 totalRetrivedRelevantInstances=totalRetrivedRelevantInstances+1;
							 }
							BufferedImage img = ImageIO.read(new File(docs.get(k).getPath()));
							
							// BufferedImage img = ImageIO.read(new File(imgPath));
					         BufferedImage resizedImage=resizeImage(img,height,height);
					         System.out.println("Load image into frame...");
					         ImageIcon   icon=new ImageIcon(resizedImage);
					        // ImageIcon icon = new ImageIcon(img);
					         //JLabel label = new JLabel(icon);
					         label.setIcon(icon);
					         k++;
					         panel1.add(label);
							}
							
							
						}
					}
					
					calculatePrecisionRecall(time);
					
				
			        
			        
			       
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			private void calculatePrecisionRecall(long time) {
				
				System.out.print("totalRetrivedRelevantInstances "+totalRetrivedRelevantInstances+" totalRetrivedInstances "+totalRetrivedInstances+" totalRelevantInstances "+totalRelevantInstances+" Time"+time);
				precision=((float)totalRetrivedRelevantInstances)/((float)totalRetrivedInstances);
				recall=(float)totalRetrivedRelevantInstances/(float)totalRelevantInstances;
				f1Score=(precision*recall)/(precision+recall);
				
				System.out.print("Precision "+precision+" Recall "+recall+" F1Score "+f1Score+" Time"+time);
				
				
				
			}

			private boolean isRelevantImage(String apath) {
				// TODO Auto-generated method stub
				if(imagePath.contains("/001_"))
				{
					return(apath.contains("/001_"));
					
				}
				else if(imagePath.contains("/002_"))
				{
					return(apath.contains("/002_"));
				}
				if(imagePath.contains("/013_"))
				{
					return(apath.contains("/013_"));
				}
				if(imagePath.contains("/024_"))
				{
					return(apath.contains("/024_"));
				}
				if(imagePath.contains("/056_"))
				{
					return(apath.contains("/056_"));
				}
				if(imagePath.contains("/159_"))
				{
					return(apath.contains("/159_"));
				}
				if(imagePath.contains("/251_"))
				{
					return(apath.contains("/251_"));
				}
				return false;
			}

			private void setCalculationVarables() {
				if(imagePath.contains("/001_"))
				{
					totalRelevantInstances=98;
					
				}
				else if(imagePath.contains("/002_"))
				{
					totalRelevantInstances=98;
				}
				if(imagePath.contains("/013_"))
				{
					totalRelevantInstances=97;
				}
				if(imagePath.contains("/024_"))
				{
					totalRelevantInstances=112;
				}
				if(imagePath.contains("/056_"))
				{
					totalRelevantInstances=102;
				}
				if(imagePath.contains("/159_"))
				{
					totalRelevantInstances=209;
				}
				if(imagePath.contains("/251_"))
				{
					totalRelevantInstances=800;
				}
				
			}

		
		});
		searchImages.setBounds(243, 132, 117, 53);
		mainPanel.add(searchImages);
		
		Button findMostSimilar = new Button("Find Most Similar");
		findMostSimilar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try{
				HashMap<String,Integer> results=new HashMap<>();
				//indexFile.scoreFilesForSimilarity();
				 
				 //Let's print out the path of files which have searched term
				int i=0;
		        for (Result sd : docs) 
		        {
		        	
		        	int scoreOfSimilarity=indexFile.scoreFilesForSimilarity(imagePath.trim(),"/Users/ruchirapatil/Downloads/LuceneExamples/"+sd.getPath().trim());
		        	results.put(sd.getPath(), scoreOfSimilarity);
		        	
		        	

		        }
		        
		        
		        sortResults(results);
				
				
			}
			catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
			
				private void sortResults(HashMap<String, Integer> aMap) throws IOException {
				Set<Entry<String,Integer>> mapEntries = aMap.entrySet();
		        
		        System.out.println("Values and Keys before sorting ");
		        for(Entry<String,Integer> entry : mapEntries) {
		            System.out.println(entry.getValue() + " - "+ entry.getKey());
		        }
		        
		        // used linked list to sort, because insertion of elements in linked list is faster than an array list. 
		        List<Entry<String,Integer>> aList = new LinkedList<Entry<String,Integer>>(mapEntries);

		        // sorting the List
		        Collections.sort(aList, new Comparator<Entry<String,Integer>>() {

		            @Override
		            public int compare(Entry<String, Integer> ele1,
		                    Entry<String, Integer> ele2) {
		                
		                return ele1.getValue().compareTo(ele2.getValue());
		            }
		        });
		        Collections.reverse(aList);
		        // Storing the list into Linked HashMap to preserve the order of insertion. 
		        Map<String,Integer> aMap2 = new LinkedHashMap<String, Integer>();
		        for(Entry<String,Integer> entry: aList) {
		            aMap2.put(entry.getKey(), entry.getValue());
		        }
		        
		        // printing values after soring of map
		        System.out.println("Value " + " - " + "Key");
		        int p=0;
		        for(Entry<String,Integer> entry : aMap2.entrySet()) {
		            System.out.println(entry.getValue() + " - " + entry.getKey());
		            JLabel label = new JLabel();
					label.setBounds(6+(p*120),444, 120, 120);
					getContentPane().add(label);
					 System.out.println("Load image into frame..."+entry.getKey());
					BufferedImage img = ImageIO.read(new File("/Users/ruchirapatil/Downloads/LuceneExamples/"+entry.getKey()));
					
			         BufferedImage resizedImage=resizeImage(img,120,120);
			         System.out.println("Load image into frame...");
			         ImageIcon   icon=new ImageIcon(resizedImage);
			        
			         label.setIcon(icon);
			         p++;
			         panel2.add(label);
			         if(p>=10)
			         {
			        	 break;
			         }
			        
		            
		        }
				
			}
		});
		findMostSimilar.setBounds(243, 226, 117, 53);
		mainPanel.add(findMostSimilar);
		
		Button refresh = new Button("Reset");
		refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel1.removeAll();
				
				panel1.revalidate();
				panel1.repaint(); 
				panel2.removeAll();
				panel2.revalidate();
				panel2.repaint(); 
				
		totalRetrivedInstances=0;
			totalRetrivedRelevantInstances=0;
			totalRelevantInstances=0;
			 precision=0;
			 recall=0;
				 f1Score=0;
			}
		});
		refresh.setBounds(243, 324, 117, 60);
		mainPanel.add(refresh);
		
		
		
		
	
		
		
		
		
		
	}
	public BufferedImage resizeImage(BufferedImage image, int width, int height) {
	    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
	    Graphics2D g2d = (Graphics2D) bi.createGraphics();
	    g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
	    g2d.drawImage(image, 0, 0, width, height, null);
	    g2d.dispose();
	    return bi;
	}
}
