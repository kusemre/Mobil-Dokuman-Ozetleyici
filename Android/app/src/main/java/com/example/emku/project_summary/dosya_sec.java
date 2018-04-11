package com.example.emku.project_summary;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;

import static android.content.Context.WINDOW_SERVICE;


public class dosya_sec extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    //GLOBALS VARIABLE
    long baslangicZamani;
    private static final int Pdf_CHOOSER = 1;
    private static final int Word_CHOOSER = 2;
    private static final int Txt_CHOOSER = 3;
    private static final int Phone_CHOOSER = 4;
    private static final int PERMISSION_EXTERNAL_STORAGE = 5;

    public static String textrank = "TextRank algoritması Google'ın linkleri önemine göre sıralamak için kullandığı PageRank algoritmasından esinlenilerek oluşturulan bir özetleme yöntemidir.  Bu yöntem kullanılarak her bir cümle diğerlerine benzerliklerine göre skor alır. Bir cümle diğerleriyle ne kadar benzerse o derecede dokümanı temsil ettiği kabul edilmektedir. Benzerlik skorları son adımda PageRank algoritmasından geçirilerek cümlenin nihai skoru hesaplanır. Bu skorlara göre istenilen uzunlukta seçilen cümleler birleştirilerek özet oluşturulur.";
    public static String lexrank = "LexRank algoritması Google'ın linkleri önemine göre sıralamak için kullandığı PageRank algoritmasından esinlenilerek oluşturulan bir diğer özetleme yöntemidir. TextRanktan farklı olarak her cümlenin önemini gösteren merkezilik skoru hesaplanır. Merkezilik skoru için her cümlenin diğer cümlelerle olan kosinüs benzerliği kullanılır. Ortaya çıkan cümle * cümle matrisinden belirli bir eşik üstünde olanlara 1 diğerlerine 0 atanarak benzerliği az olan değerler temizlenir. Son aşamada cümlenin merkezilik değerleri toplanarak cümlenin asıl skoru ortaya çıkartılır. Bu skorlara göre sıralanan cümleler istenilen uzunlukta birleştirilerek özet oluşturulur.";
    public static String kumeleme = "Kümeleme analizi, verilerin özniteliklerini kullanarak birbirleriyle benzer olanları alt kümelere ayıran ve verinin bütünü hakkında tahmin yapılmasını sağlayan veri madenciliği yaklaşımlarından birisidir. Başka bir deyişle kümeleme, verileri doğal gruplarına ayırmaktadır. Otomatik özetleme sistemlerinde oluşturulacak özetin dokümanın tamamını kapsaması amacıyla farklı anlamlardaki cümlelerin seçilmesini sağlamak için kullanılır. Bu amaçla dokümanın cümleleri kümelenerek her kümeden bir cümle seçilir, böylelikle oluşturulan özet dokümanın tamamını yansıtabilir hale gelir.";
    public static String ozellik_tabanli = "Klasik yöntem otomatik özetleme sistemleri tarihinde ilk kullanılan yaklaşımdır. Bu yaklaşımda her bir cümle belirli bazı yapısal ve içeriksel özelliklerine göre skor alır. Cümlenin uzunluğu, dokümandaki yeri, içerdiği özel isim veya numerik verinin cümlenin önemi hakkında bilgi taşıdığı kabul edilir. Her cümleye ait bu öznitelikler çıkartılarak toplanır ve cümlenin puanı hesaplanır. Özetleme adımında ise skoru en yüksek cümleler seçilir, birleştirilir ve özet oluşturma işlemi tamamlanır.";

    public static String[] methods = {"Özellik Tabanlı","Kümeleme","TextRank","LexRank"};
    public static String[] information = {ozellik_tabanli,kumeleme, textrank,lexrank};

    public  String method_name = "";
    public  String input_data = "";
    public  String output_data = "";

    String show_info = "";
    String file_extension = "";
    String read_path = "";
    String write_path = "";
    String rate = "";


    public AlertDialog.Builder dialog_summary ;
    public View view_alert;
    public EditText editText_fullDataSave;
    public EditText editText_summary_rate;
    ProgressDialog barProgressDialog;

    TextView summary_time;
    Button btn_summary;
    ImageView imageView_info;
    Spinner spinner_method;

    RelativeLayout layoutPdf;
    RelativeLayout layoutWord;
    RelativeLayout layoutTxt;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        //RUNTIME PERMISSION
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_EXTERNAL_STORAGE);
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_EXTERNAL_STORAGE);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view_dosya_sec = inflater.inflate(R.layout.fragment_dosya_sec, null, false);



        //ATTRIBUTION
        layoutPdf = (RelativeLayout) view_dosya_sec.findViewById(R.id.layout_pdf);
        layoutWord = (RelativeLayout) view_dosya_sec.findViewById(R.id.layout_word);
        layoutTxt = (RelativeLayout) view_dosya_sec.findViewById(R.id.layout_txt);
        imageView_info = (ImageView)view_dosya_sec.findViewById(R.id.imageView_fileselect_info);
        spinner_method = (Spinner)view_dosya_sec.findViewById(R.id.spinner_file_select_method);
        editText_summary_rate = (EditText)view_dosya_sec.findViewById(R.id.editText_rate);
        editText_summary_rate.setSelection(1);
        btn_summary = (Button)view_dosya_sec.findViewById(R.id.img_summary_load);

        ArrayAdapter<String> adap_method = new ArrayAdapter<String>(getContext(),R.layout.spinner_config,R.id.textView_spinner,methods);

        //LISTENER
        layoutWord.setOnClickListener(this);
        layoutPdf.setOnClickListener(this);
        layoutTxt.setOnClickListener(this);
        imageView_info.setOnClickListener(this);
        spinner_method.setAdapter(adap_method);
        spinner_method.setOnItemSelectedListener(this);
        btn_summary.setOnClickListener(this);

        return view_dosya_sec;
    }
    // Pdf , word, txt resimlerine ve diğer butonlara tıklandığında işleyen metot
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.layout_pdf:
                //intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setType("*/*");
                //startActivityForResult(intent, Pdf_CHOOSER);
                Toast.makeText(getContext(), "Bu özellik şuan aktif değildir.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_word:
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");
                String[] typeWord = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document","application/msword"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES,typeWord);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                dosya_sec.this.startActivityForResult(intent, Word_CHOOSER);
                break;
            case R.id.layout_txt:
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");
                String[] typeText = {"text/plain"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES,typeText);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,Txt_CHOOSER);
                break;
            case R.id.imageView_fileselect_info:
                View view_info = View.inflate(getContext(),R.layout.layout_info,null);
                TextView textView_info = view_info.findViewById(R.id.textView_info);
                AlertDialog.Builder dialog_info = new AlertDialog.Builder(getContext());
                textView_info.setText(show_info);
                dialog_info.setView(view_info);
                dialog_info.show();


                break;
            case R.id.img_summary_load:
                baslangicZamani = SystemClock.uptimeMillis();
                if (!read_path.equals("")) {
                    if (!editText_summary_rate.getText().toString().equals("") && !editText_summary_rate.getText().toString().equals("0") && !editText_summary_rate.getText().toString().equals("00"))
                        summaryFileLoad(file_extension);
                    else
                        Toast.makeText(getContext(), "Lütfen özet oranını belirleyiniz...", Toast.LENGTH_SHORT).show();
                }
                else  Toast.makeText(getContext(), "Lütfen dosya seçiniz...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.alert_img_phone:
                intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,Phone_CHOOSER);
                break;
            case R.id.alert_img_mail:
                Toast.makeText(getContext(), "Bu özellik şuan aktif değildir.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.alert_img_onedrive:
                Toast.makeText(getContext(), "Bu özellik şuan aktif değildir.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.alert_img_gdrive:
                Toast.makeText(getContext(), "Bu özellik şuan aktif değildir.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //Dosya seçme veya kaydetme penceresinden dönen sonuçların işlendiği metot
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
        {
            if(requestCode == Word_CHOOSER || requestCode == Txt_CHOOSER) {
                read_path = get_Path(data);
                file_extension = getExtension(read_path);
                Toast.makeText(getContext(), "Dosya seçildi.", Toast.LENGTH_SHORT).show();
            }

            if(requestCode == Phone_CHOOSER)
            {
                write_path = get_Path(data);
                FileWrite.text_Write(write_path,editText_fullDataSave.getText().toString());
                File atik_dosya = new File(write_path);
                atik_dosya.delete();
                Toast.makeText(getContext(), "Dosya kaydedildi.", Toast.LENGTH_SHORT).show();
                read_path = "";
                write_path = "";
                editText_summary_rate.setText("1");

            }
        }
        else
            Toast.makeText(getContext(), "Dosya seçilmedi!", Toast.LENGTH_SHORT).show();
    }

    // Okunacak dosyanın yolu
    public static String get_Path(Intent data) {
        String stable_Path = Environment.getExternalStorageDirectory().getPath().toString() + "/";
        Uri uri = data.getData();
        String Path = uri.getPath();
        if (Path.contains(":")) {
            String[] dynamic_Path = Path.split(":");
            Path = stable_Path + dynamic_Path[1].toString();
        }
        return Path;

    }

    //Okunacak dosyanın uzantısını bulan metot
    public String getExtension(String uri) {
        if (uri == null) {
            return null;
        }
        int nok = uri.lastIndexOf(".");
        if (nok >= 0)
            return uri.substring(nok);
        else
            return "";
    }

    // Özetle butonuna tıklandığında çağrılan metot
    public void summaryFileLoad(String extension)
    {
        boolean status_word_doc=false;
        boolean status_word_docx=false;
        boolean status_txt=false;

        try
        {
            if (extension.equals(".txt"))
                status_txt = true;
            else if (extension.equals(".doc"))
                status_word_doc = true;
            //else if (extension.equals(".docx"))
               // status_word_docx = true;
            else
                Toast.makeText(getContext(), "Dosya uzantısı çözümlenemedi...", Toast.LENGTH_SHORT).show();

            if ((status_txt || status_word_doc || status_word_docx))
            {
                if (extension.equals(".doc"))
                    input_data = FileRead.word_Read(read_path);
                else if (extension.equals(".docx"))
                { String x = FileRead.word_ReadDocx(read_path);
                    Toast.makeText(getContext(), x, Toast.LENGTH_SHORT).show();}
                else if (extension.equals(".txt"))
                    input_data = FileRead.txt_Read(read_path);


                rate = editText_summary_rate.getText().toString();

                barProgressDialog = new ProgressDialog(getContext());
                barProgressDialog.setTitle("Özetleniyor...");
                barProgressDialog.setMessage("Lütfen bekleyiniz ...");
                barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                barProgressDialog.setCanceledOnTouchOutside(false);
                barProgressDialog.setCancelable(false);

                new MyTask().execute();

                view_alert = View.inflate(getContext(), R.layout.alert_summary, null);
                dialog_summary = new AlertDialog.Builder(getContext());
                summary_time = (TextView) view_alert.findViewById(R.id.txt_summary_time);
                editText_fullDataSave = (EditText) view_alert.findViewById(R.id.txt_summary_content);
                ImageView img_save_phone = (ImageView) view_alert.findViewById(R.id.alert_img_phone);
                ImageView img_save_mail = (ImageView) view_alert.findViewById(R.id.alert_img_mail);
                ImageView img_save_onedrive = (ImageView) view_alert.findViewById(R.id.alert_img_onedrive);
                ImageView img_save_gmail = (ImageView) view_alert.findViewById(R.id.alert_img_gdrive);
                img_save_phone.setOnClickListener(this);
                img_save_mail.setOnClickListener(this);
                img_save_onedrive.setOnClickListener(this);
                img_save_gmail.setOnClickListener(this);
                dialog_summary.setView(view_alert);
            }
        } catch (Exception e) { Toast.makeText(getContext(), "Sunucuyu kontrol ediniz..." + e.toString(), Toast.LENGTH_SHORT).show(); }
    }

    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        method_name = methods[i].indexOf('ü') != -1 ? methods[i].replace('ü','u') : methods[i];
        method_name = methods[i].indexOf('Ö') != -1 ? methods[i].replace('Ö','o') : methods[i];
        method_name = method_name.replace(' ','_');
        show_info = information[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }
    class MyTask extends AsyncTask<Integer, Integer, String> {
        @Override
        public String doInBackground(Integer... params) {  //Arkaplanda yapılan işlem

            String JSON = "{\"method\":\" " + method_name.toLowerCase() + " \",\"content\":\" " + input_data + " \" ,\"rate\": \" " + rate + " \" }";

            try {
                output_data = SendPost.sendPost(JSON);

            } catch (Exception e) {
                Toast.makeText(getContext(), "sunucuyu kontrol ediniz...", Toast.LENGTH_SHORT).show();
                barProgressDialog.dismiss();
                e.printStackTrace();
            }
            return output_data;
        }
        @Override
        protected void onPostExecute(String result) { // Uyuma bittikten sonra yapılacak işlem
            long sure = (SystemClock.uptimeMillis() - baslangicZamani)/1000;
            barProgressDialog.dismiss();
            summary_time.setText(String.valueOf(sure) + " saniyede özetlendi.");
            editText_fullDataSave.setText(output_data);
            editText_fullDataSave.setSelection(editText_fullDataSave.length());
            dialog_summary.show();
        }
        @Override
        protected void onPreExecute() {     // Uyurken önplanda tutulan işlem
            barProgressDialog.show();
        }
    }
}

