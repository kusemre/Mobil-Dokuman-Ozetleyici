# -*- coding: utf-8 -*-
import math
import numpy as np
from nltk import tokenize
from nltk.stem.snowball import SnowballStemmer
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.cluster import Birch, MiniBatchKMeans, KMeans
from openpyxl import load_workbook

def get_cluster_kmeans(tfidf_matrix, num_clusters):
    
    km = KMeans(n_clusters = num_clusters,n_init=200,max_iter=5000,algorithm="full")
    km.fit(tfidf_matrix)
    cumlevektor = km.transform(tfidf_matrix);
    centers = km.cluster_centers_;
    cluster_list = km.labels_.tolist();
    listeler1 =[]
    for tt in range(len(list(set(cluster_list)))):
        ttlist=[]
        k = list(set(cluster_list))[tt]
        for ll in range(len(cluster_list)):
            if cluster_list[ll] == k:
                uzaklik = okliddis(cumlevektor[ll],centers[cluster_list[k]]);
                ttlist.append((ll,k,uzaklik))
        listeler1.append(ttlist)
    ozetcumleleri=[]  
    for kume in listeler1:
        kume.sort(key=lambda x: x[2],reverse=False)
        ozetcumleleri.append(kume[0][0])
    ozetcumleleri.sort();
    return ozetcumleleri

def okliddis(X,Y):
    toplam = 0;
    for k in range(len(X)):
        sayi = X[k] - Y[k];
        sayi = sayi**2;
        sayi = math.sqrt(sayi);
        toplam = toplam+sayi;
    return toplam;

def getsisozet(haber,so):
    haber = [haber[k].lower() for k in range(len(haber))]
    uzunluk = round(len(haber) * int(so)/100)
    if uzunluk <1:
        uzunluk = 1
    tfidf = TfidfVectorizer()
    matris = tfidf.fit_transform(haber);
    ozetcumleleri = get_cluster_kmeans(matris,uzunluk)
    yeniozet = [haber[k] for k in ozetcumleleri]
    
    return yeniozet
