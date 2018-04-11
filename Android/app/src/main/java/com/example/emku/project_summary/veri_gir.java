package com.example.emku.project_summary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import static android.app.Activity.RESULT_OK;


public class veri_gir extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    private static final int Phone_CHOOSER = 6;
    Button btn_summary;
    ImageView imageView_enterdata_info;
    EditText editTextEnterData;
    AlertDialog.Builder dialog_summary;
    View alert_summary;
    ProgressDialog barProgressDialog;
    EditText editText_fullDataSave;
    EditText editText_summary_rate;
    Spinner spinner_method;
    String write_path;
    String method_name;
    String show_info;
    String input_data;
    String output_data;
    String rate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ThreadPolicy permission
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view_veri_gir = inflater.inflate(R.layout.fragment_veri_gir, null, false);

        //ATTRIBUTION
        editTextEnterData = (EditText) view_veri_gir.findViewById(R.id.editText_enter_data);
        editText_summary_rate = (EditText)view_veri_gir.findViewById(R.id.editText_summary_rate);
        editText_summary_rate.setSelection(1);
        imageView_enterdata_info = (ImageView)view_veri_gir.findViewById(R.id.imageView_enterdata_info);
        spinner_method = (Spinner)view_veri_gir.findViewById(R.id.spinner_enter_data_method);
        btn_summary = (Button) view_veri_gir.findViewById(R.id.img_summary_enter);

        ArrayAdapter<String> adap_method = new ArrayAdapter<String>(getContext(),R.layout.spinner_config,R.id.textView_spinner,dosya_sec.methods);

        //LISTENERR

        btn_summary.setOnClickListener(this);
        spinner_method.setAdapter(adap_method);
        spinner_method.setOnItemSelectedListener(this);
        imageView_enterdata_info.setOnClickListener(this);
        return view_veri_gir;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.img_summary_enter:
                try {
                    if (editTextEnterData.getText().toString().trim().length() != 0)
                    {
                        if (!editText_summary_rate.getText().toString().equals("") && !editText_summary_rate.getText().toString().equals("0") && !editText_summary_rate.getText().toString().equals("00"))
                            summaryEnterData();
                        else
                            Toast.makeText(getContext(), "Lütfen özet oranını belirleyiniz...", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getContext(), "Lütfen veri giriniz...", Toast.LENGTH_SHORT).show();
                }

                catch (Exception e) {  Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show(); }
                break;
            case R.id.imageView_enterdata_info:
                View view_info = View.inflate(getContext(),R.layout.layout_info,null);
                TextView textView_info = view_info.findViewById(R.id.textView_info);
                AlertDialog.Builder dialog_info = new AlertDialog.Builder(getContext());
                textView_info.setText(show_info);
                dialog_info.setView(view_info);
                dialog_info.show();
                break;

            case R.id.alert_img_phone:
                intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, Phone_CHOOSER);
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
    //Dosya kaydetme penceresinden dönen sonuçların işlendiği metot
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == Phone_CHOOSER) {
                write_path = dosya_sec.get_Path(data);
                FileWrite.text_Write(write_path, editText_fullDataSave.getText().toString());
                File atik_dosya = new File(write_path);
                atik_dosya.delete();
                Toast.makeText(getContext(), "Dosya kaydedildi.", Toast.LENGTH_SHORT).show();

                editText_summary_rate.setText("1");
                editTextEnterData.setText("");

            }
        }
    }
    // Özetle butonuna tıklandığında çağrılan metot
    public void summaryEnterData() throws Exception {

        barProgressDialog = new ProgressDialog(getContext());
        barProgressDialog.setTitle("Özetleniyor...");
        barProgressDialog.setMessage("Lütfen bekleyiniz ...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);

        input_data = FileRead.formatCode(editTextEnterData.getText().toString());
        rate = editText_summary_rate.getText().toString();

        new MyTask().execute();

        dialog_summary = new AlertDialog.Builder(getContext());
        alert_summary = View.inflate(getContext(), R.layout.alert_summary, null);
        editText_fullDataSave = (EditText) alert_summary.findViewById(R.id.txt_summary_content);
        ImageView img_save_phone = (ImageView) alert_summary.findViewById(R.id.alert_img_phone);
        ImageView img_save_mail = (ImageView) alert_summary.findViewById(R.id.alert_img_mail);
        ImageView img_save_onedrive = (ImageView) alert_summary.findViewById(R.id.alert_img_onedrive);
        ImageView img_save_gmail = (ImageView) alert_summary.findViewById(R.id.alert_img_gdrive);
        img_save_phone.setOnClickListener(this);
        img_save_mail.setOnClickListener(this);
        img_save_onedrive.setOnClickListener(this);
        img_save_gmail.setOnClickListener(this);
        dialog_summary.setView(alert_summary);
        }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        method_name = dosya_sec.methods[i].replace("ü","u");
        show_info = dosya_sec.information[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public interface OnFragmentInteractionListener {
    }
    class MyTask extends AsyncTask<Integer, Integer, String> {
        @Override
        public String doInBackground(Integer... params) {  //Arkaplanda yapılan işlem
            String JSON = "{\"method\":\" " + method_name.toLowerCase() + " \",\"content\":\" " + input_data + " \" ,\"rate\": \" " + rate + " \" }";

            try {
                output_data = SendPost.sendPost(JSON);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return output_data;
        }
        @Override
        protected void onPostExecute(String result) { // Uyuma bittikten sonra yapılacak işlem
            barProgressDialog.dismiss();
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


