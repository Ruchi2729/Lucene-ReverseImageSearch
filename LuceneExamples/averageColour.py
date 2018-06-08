#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Aug 13 20:17:21 2017

@author: ruchirapatil
"""


import cv2
import numpy as np
from matplotlib import pyplot as plt
import sys

img = cv2.imread(sys.argv[1], -1)
average_color_per_row = np.average(img, axis=0)
average_color = np.average(average_color_per_row, axis=0)
print(average_color)