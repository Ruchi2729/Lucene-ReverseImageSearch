Image Search Engine                                                                                                                                                                                 August 2017
Developed a Lucene based reverse image search engine and implemented image processing algorithms in Python.
Technologies used: Java, Lucene, Python, OpenCV

•	Steps to Run the Project :-

1)Unzip the jar and save java project on your machine directory.
2)Install Python 3.3 with Anaconda and know the python path with command ‘which python’
3)Install OpenCV by executing command pip install opencv-contrib-python
4)In a findProperty method in LuceneReadIndexFromFileExample class of project. In ProcessBuilder argument mention Python path as well as the path where the python          scripts are saved. Currently I have kept all scripts in project root folder.

ProcessBuilder("/Users/ruchirapatil/anaconda/bin/python","/Users/ruchirapatil/Downloads/LuceneExamples/"+fileName,fileLoc);

4)All image file in InputFiles2 folder are already indexed. So no need to index again.
6)Directly run Java swing application by running ImageSearchWindow class.
7)If wish to index all files all over again. Update the Python path and path of the folder where all Python scripts are in Process builder of findProperty method of WriteIndexFile class.
8)Images in folder inputFiles2 are indexed.
