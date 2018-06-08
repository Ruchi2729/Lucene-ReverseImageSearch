#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Tue Aug 15 22:35:44 2017

@author: ruchirapatil
"""
import numpy as np
import cv2
from matplotlib import pyplot as plt
import sys



img1 = cv2.imread(sys.argv[1],-1)          # queryImage
img2 = cv2.imread(sys.argv[2],-1)          # trainImage

# Initiate SIFT detector
sift =cv2.xfeatures2d.SIFT_create()

# find the keypoints and descriptors with SIFT
kp1, des1 = sift.detectAndCompute(img1,None)
kp2, des2 = sift.detectAndCompute(img2,None)

# BFMatcher with default params
bf = cv2.BFMatcher()
matches = bf.knnMatch(des1,des2, k=2)

# Apply ratio test
good = []
for m,n in matches:
    if m.distance < 0.75*n.distance:
        good.append([m])

# cv2.drawMatchesKnn expects list of lists as matches.


print(len(good))