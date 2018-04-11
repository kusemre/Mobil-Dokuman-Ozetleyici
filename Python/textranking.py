# -*- coding: utf-8 -*-
import sys
import os
from nltk.tokenize import sent_tokenize, word_tokenize
from nltk import pos_tag
from nltk.corpus import stopwords
import numpy as np
import math
import pandas as pd
from sklearn.feature_extraction.text import CountVectorizer
from openpyxl import load_workbook
cv = CountVectorizer()

def text_rank_sent(graph,node_weights,d=.85,iter=10):
    weight_sum = np.sum(graph,axis=0)
    while iter >0:
        for i in range(len(node_weights)):
            temp = 0.0
            for j in range(len(node_weights)):
                temp += (graph[i,j]*node_weights[j]+1)/(weight_sum[j]+1)
            node_weights[i] = 1-d+(d*(temp-1))
        iter-=1
    return node_weights
def trsim(i,j,haber):
    if haber[i] =='' or haber[j] =='':
        return 0
    elif len(set(haber[i].lower().split())) == 1 and len(set(haber[j].lower().split())) == 1:
        return 0
    else:
        pay =  float(len(set(haber[i].lower().split())&set(haber[j].lower().split())))
        payda = math.log(len(set(haber[i].lower().split()))) + math.log(len(set(haber[j].lower().split())))
        return pay/payda
    
def getSisozet(haber,so):
    #Kaç tane cümele varsa aslında uzunluğunu al #
    haber = [haber[k].lower() for k in range(len(haber))]
    num_nodes = len(haber);
    
    uzunluk = round(num_nodes * (int(so)/100))
    
    if uzunluk <1:
        uzunluk = 1
    # N*N boyutta cümle sayısı kadar graf oluştur : 
    graph = np.zeros((num_nodes,num_nodes));
    
    # Her cümlenin diğer cümleye göre ORTAK KELİME / TOPLAM KELİME HESAPLAYIP diziye ekle #
    for i in range(num_nodes):
        for j in range(num_nodes):
            if i==j:
                graph[i,j]=1
            else:
                graph[i,j] = trsim(i,j,haber)
    
    node_weights = np.ones(num_nodes)
    
    # Grapf ile elimizdeki ağırlıkları PR aloritmasına gönder : 
    node_weights = text_rank_sent(graph,node_weights)
    
    skordizi =[]
    for k in range(len(node_weights)):
        skordizi.append((node_weights[k],k))
    #3 tane en yüksek skora sahip node'nin numarasını al :
    skordizi.sort(key=lambda x: x[0],reverse=True)
    skordizi  = skordizi[:uzunluk]
    skordizi.sort(key=lambda x: x[1],reverse=False)
    #Bu numaralar ile en önemli cümleleri çıkar : 
    sis_ozet = [haber[skordizi[k][1]] for k in range(len(skordizi))]
    
    return sis_ozet


    
   