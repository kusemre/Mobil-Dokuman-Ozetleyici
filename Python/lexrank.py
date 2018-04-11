# -*- coding: utf-8 -*-
"""
Created on Fri Dec 15 00:06:07 2017

@author: DELL PC
"""

from __future__ import absolute_import
from sumy.parsers.plaintext import PlaintextParser
from sumy.nlp.tokenizers import Tokenizer
from sumy.summarizers.lex_rank import LexRankSummarizer
tokendili="turkish"


def getLRozet(haber,so):
    haber = [haber[k].lower() for k in range(len(haber))]
    uzunluk = round(len(haber) * int(so)/100)
    if uzunluk<1:
        uzunluk=1
    parser = PlaintextParser.from_string(' . '.join(haber), Tokenizer(tokendili))
    summarizer = LexRankSummarizer()
    sis_ozet = [];
    for ss in summarizer(parser.document, uzunluk):
        sis_ozet.append(' '.join(ss.words))
        
    return sis_ozet