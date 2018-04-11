# -*- coding: utf-8 -*-
import re
import sys
import numpy as np
import nltk
import nltk.data
from textranking import getSisozet as textrank
from kumeleme import getsisozet as kumeleme
from lexrank import getLRozet as lexrank
from featurebased import getKlasikOzet as klasik
def oz(yontem,so,haber):
    
    if len(haber)<1:
        print('Lütfen Cümle Giriniz');
    elif so=="" or so==0 or so=='0' :
        print('Lütfen Özet Uzunluğu için Sıkıştırma Oranı Giriniz')
    elif len(haber)<2:
        return haber
    else:
        if yontem == "textrank":
            ozetcumleleri = textrank(haber,so)
        elif yontem== "kumeleme":
            ozetcumleleri = kumeleme(haber,so)
        elif yontem=="lexrank":
            ozetcumleleri = lexrank(haber,so)
        else:
            ozetcumleleri = klasik(haber,so)
        yeniozet = ' . '.join(ozetcumleleri) + ' . '
        return(yeniozet)

def Temizle(icerik):
    icerik.replace('â','a');
    #Gereksiz işaretleri temizle:
    
    icerik= re.sub(r'[^a-zA-Z0-9.:?!, \\nþÞýÝöÖçÇðÐüÜiİıIğĞşŞ]',' ',icerik);
   
    #Nokta Virgül hariç diğer işaretleri (! ? :) noktaya çevir:
    icerik= re.sub(r'[^a-zA-Z0-9., \\nþÞýÝöÖçÇðÐüÜiİıIğĞşŞ]',' . ',icerik);
    
    # Virgül Temizle:
    icerik = re.sub(r'[^a-zA-Z0-9. \\nþÞýÝöÖçÇðÐüÜiİıIğĞşŞ]',' ',icerik);
    
    #Cümle bölücü ile dokümanı cümlelere böl ve liste ekle:
    st = nltk.data.load('tokenizers/punkt/turkish.pickle')
    cumleler = st.tokenize(icerik)
    cumleler = [k.replace('.',' ') for k in cumleler]
    #gelen temizlemetürü stopwordsuz(s) ise cümlelerdeki stopwordleri temizle:
    
    return cumleler;
    
    
    
def main():
    yontem = sys.argv[1];
    so = sys.argv[2]
    gelen = sys.argv[3];
    temizgelen = Temizle(gelen);
    ozet = oz(yontem,so,temizgelen);
    print(ozet)
    return(ozet)

if (__name__ == '__main__'):
    main();
#    fonksiyon(gelen)
