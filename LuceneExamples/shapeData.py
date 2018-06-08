#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Aug 13 19:09:56 2017

@author: ruchirapatil
"""



import cv2
import numpy as np
from matplotlib import pyplot as plt
import sys



img = cv2.imread(sys.argv[1], -1)

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
    
print(shapeString)


