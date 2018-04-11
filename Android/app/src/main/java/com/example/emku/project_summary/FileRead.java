package com.example.emku.project_summary;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.List;


/**
 * Created by EmKu on 18.12.2017.
 */

public class FileRead {

    public static String txt_Read(String path) {
        String text = "";
        try {
            FileInputStream fis = new FileInputStream(new File(path));
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in,"utf-8"));

            String strLine;
            while ((strLine = br.readLine()) != null) {
                text = text + strLine + " ";
                text = text  + "\n";
            }
            br.close();
            in.close();
            fis.close();
            text = formatCode(text);
            return text;
        } catch (Exception e) {
            return e.toString();

        }
    }

    public static String word_Read(String path) {
        String text = "";
        try {
            FileInputStream fis = new FileInputStream(new File(path));
            HWPFDocument doc = new HWPFDocument(fis);
            WordExtractor extractor = new WordExtractor(doc);

            String[] fileData = extractor.getParagraphText();
            for (int i = 0; i < fileData.length; i++) {

                text = text + fileData[i];
                text = text.replace("\r"," ");
            }
            fis.close();
            text = formatCode(text);

            return text;

        } catch (FileNotFoundException e) {
            return e.toString();
        } catch (IOException e) {
            return e.toString();
        }
    }

    public static String word_ReadDocx(String path) throws IOException, InvalidFormatException {
        String text="";
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);

            XWPFDocument document = new XWPFDocument(fis);

            List<XWPFParagraph> paragraphs = document.getParagraphs();

            for (XWPFParagraph para : paragraphs) {
                text = text + para.getText();
            }
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return text;
        }



    }
    public static String formatCode(String code){

        String[] codeArray = code.split("\n");
        String tempCode="";
        for(int i =0;i<codeArray.length;i++){
            tempCode+=codeArray[i] + " ";
        }
        codeArray = tempCode.split("\"");
        int  i;
        tempCode="";
        for(i =0;i<codeArray.length-1;i++){
            tempCode+=codeArray[i]+"\\\"";
        }
        tempCode+=codeArray[i];

        return tempCode;
    }
}

