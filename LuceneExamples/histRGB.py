# -*- coding: utf-8 -*-
"""
Spyder Editor

This is a temporary script file.
"""


import numpy as np
import cv2
 
#class RGBHistogram:
#	def __init__(self, bins):
#		# store the number of bins the histogram will use
#		self.bins = bins
# 
#	def describe(self, image):
#		# compute a 3D histogram in the RGB colorspace,
#		# then normalize the histogram so that images
#		# with the same content, but either scaled larger
#		# or smaller will have (roughly) the same histogram
#		hist = cv2.calcHist([image], [0, 1, 2],
#			None, self.bins, [0, 256, 0, 256, 0, 256])
#		hist = cv2.normalize(hist)
#        pr=hist.flatten()
#		# return out 3D histogram as a flattened array
#		return hist.flatten()



import cv2
import numpy as np
from matplotlib import pyplot as plt
import sys



img = cv2.imread('/001_0001.jpg', -1)

shapeArray=img.shape
color = ('b','g','r')
for channel,col in enumerate(color):
    hist = cv2.calcHist([img],[channel],None,[256],[0,256])

    pr=hist.flatten()

#shapeString="".join(str(shapeArray))
#histString="".join(str(pr))
#
#mylist = [1, 2, 3]
#histString = ''.join(histString(x) for x in pr)

histString=""
for digit in pr:
    histString += str(digit)+" "
    
shapeString=""
for digit in shapeArray:
    shapeString += str(digit)+" "
    
print(histString)



#import exifread
## Open image file for reading (binary mode)
#f = open("/001_0001.jpg", 'rb')
#
## Return Exif tags
#tags = exifread.process_file(f)

#cv2.imshow('GoldenGate',img)
#color = ('b','g','r')
#for channel,col in enumerate(color):
#    histr = cv2.calcHist([img],[channel],None,[256],[0,256])
#    plt.plot(histr,color = col)
#    plt.xlim([0,256])
#plt.title('Histogram for color scale picture')
#plt.show()
#
#while True:
#    k = cv2.waitKey(0) & 0xFF     
#    if k == 27: break             # ESC key to exit 
#cv2.destroyAllWindows()

