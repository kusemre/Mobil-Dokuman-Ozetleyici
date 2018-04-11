# -*- coding: utf-8 -*-
"""
Created on Mon Jan  2 15:41:13 2017
# -*- coding: utf-8 -*-
@author: zer03
"""
import numpy as np
import pandas as pd
from collections import Counter
from nltk import tokenize
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from rippletagger.tagger import Tagger
from sumy.nlp.tokenizers import Tokenizer




def getsw():
    swdizin = r'turkcesw.txt'
    with open(swdizin) as f:
        sw = f.read().splitlines()
    return sw;

def getFeatures(gelen):
        
    
    yazi = list(filter(('').__ne__, gelen))
    
    # -----FEATURE 1 CÜMLE UZUNLUĞU -----#
    f1 = np.zeros(len(yazi))
    for i in  range(len(yazi)):
        cumleuzunluk = len(yazi[i].split())
        f1[i]=cumleuzunluk
    f1 = f1/max(f1)
    
    #----FEATURE 2 CÜMLE KONUMU ----#
    f2 = np.zeros(len(yazi))
    for i in  range(len(yazi)):
       f2[i] = ((len(yazi) - yazi.index(yazi[i]))/len(yazi))
        
    #----FEATURE 3 TERİM AĞIRLIĞI TF/ISF -----#
    
    f3 = np.zeros(len(yazi))
    tfidf = TfidfVectorizer().fit_transform(yazi)
    
    for i in  range(len(yazi)):
        f3[i] =(tfidf[i].sum())
    f3 = f3/max(f3)
    
    # FEATURE 4 ÖZEL İSİM (PROPER NOUR) ----#
    f5 = np.zeros(len(yazi))
    tagger = Tagger(language="english")
    for i in  range(len(yazi)):
        sayi = len([item for item in tagger.tag(yazi[i]) if item[1] == 'NOUN'])
        sayi = sayi / len(yazi[i].split())
        f5[i] = sayi
        
       
    #----FEATURE 5 TEMATİK KELİMELER ---#
    sw = getsw();
    c = Counter([i for i in ' '.join(yazi).lower().split() if i not in sw]).most_common(5)
    tematikler = [item[0] for item in c]
    f6 = np.zeros(len(yazi))
    for i in  range(len(yazi)):
        f6[i]=len(set(yazi[i].lower().split())&set(tematikler)) /len(yazi[i].split())
        
        
     #----FEATURE 6 numerik veriler ---#
    f7 = np.zeros(len(yazi))
    for i in  range(len(yazi)):
        f7[i] = len([int(s) for s in yazi[i].split() if s.isdigit()]) /len(yazi[i].split())
       
       
      #---- FEATURE 7 Cümle Benzerlik Skoru ---#
    f8 = np.zeros(len(yazi));
    tfidf = TfidfVectorizer().fit_transform(yazi)
    
    for i in range(len(yazi)):
        f8[i] = cosine_similarity(tfidf[i],tfidf).sum();
    f8 = f8 / max(f8)
    
    
    sutunlar= ['f1_uzunluk','f2_konum','f3_tfisf','f4_özelisim','f5_tematik','f6_numerik','f7_benzerlik']
    ind = [];
    for i in range(len(yazi)):
        ind.append(str(i));
    data = np.array([f1,f2,f3,f5,f6,f7,f8])
    
    Dframe = pd.DataFrame(data=data,index=sutunlar , columns=ind);
    dizi = Dframe.sum(axis=0).as_matrix()
    geridondur = []
    for t in range(len(dizi)):
        geridondur.append((dizi[t],t))
    #Dataframe ile doküman uzunluğu geri döndürülüyor !
    return geridondur

def getKlasikOzet(yazi,so):
    uzunluk = round(len(yazi) * (int(so)/100))
    if uzunluk<1:
        uzunluk=1
        
    gelendizi =  getFeatures(yazi)
    gelendizi.sort(key=lambda x: x[0],reverse=True)
    gelendizi = gelendizi[0:uzunluk]
    gelendizi.sort(key=lambda x: x[1])
    ozet = ([yazi[gelendizi[k][1]] for k in range(len(gelendizi))])
    
    return ozet