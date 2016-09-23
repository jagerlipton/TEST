package go.designcomporttool;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    TextView status;
    final String LOG_TAG = "myLogs";

    final String FILENAME = "file";

    final String DIR_SD = "MyFiles";
    final String FILENAME_SD = "fileSD";
    final ArrayList<String> logstrings = new ArrayList<String>();
    static final SimpleDateFormat DATA_FORMAT = new SimpleDateFormat("dd_MM_yyyy");
    static final String LOG_EXTENTION = ".txt";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
             return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                return true;
            case R.id.action_exit:
                this.finish();
                return true;
            case R.id.action_saveas:
                saveas();
                return true;
            case R.id.action_writephone:
                writeFileSD();
                return true;
            case R.id.action_readsd:
                readFileSD();
                return true;
            case R.id.action_readphone:
                readphone();
                return true;
                   default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Read_Comport_settings();




       // status = (TextView) findViewById(R.id.textView);
       // status.setText(Global_data.Gd_comport_baudrate);
    }
 public void Read_Comport_settings(){
     String downloadType;
     SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
     downloadType = SP.getString("comport_bauderate","9600");
     Global_data.Gd_comport_baudrate=downloadType;
     downloadType = SP.getString("comport_databit","8");
     Global_data.Gd_comport_databit=downloadType;
     downloadType = SP.getString("comport_chet","Нет");
     Global_data.Gd_comport_chet=downloadType;
     downloadType = SP.getString("comport_stopbits","1");
     Global_data.Gd_comport_stopbits=downloadType;
     downloadType = SP.getString("comport_flowcontrol","Нет");
     Global_data.Gd_comport_flowcontrol=downloadType;

    }

    public void saveas() {
    //==
// вызов папкодиалога
        Intent intent = new Intent(this, opendir.class);
        intent.putExtra("EXTRA_KEY", "fromMainForm");
        startActivityForResult(intent, 1);

    }
    public void writephone() {
     //gg
    }
    public void readsd() {
        //==
    }
    public void readphone() {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (data == null) {return;}
        String url = data.getStringExtra("url");
        status = (TextView) findViewById(R.id.textView);
       // status.setText(url);
    //    if data.getStringExtra("EXTRA_KEY");
        status.setText(data.getStringExtra("EXTRA_KEY"));
       // WriteFile_from_listview(url);
    }
    //========================

    //==========================================
    void WriteFile_from_listview(String filepath) {
          if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        String dateString = DATA_FORMAT.format(new Date());
        String fileName = "Log_"+dateString;

       File sdFile = new File(filepath, fileName+LOG_EXTENTION);
              int i = 0;
       while (sdFile.exists()) {
           i += 1;
           sdFile = new File(filepath, fileName+"_"+(i)+LOG_EXTENTION);
        }

       try {

        BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            ListView list = (ListView) findViewById(R.id.listView);
            ArrayAdapter<String> adapter = new    ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, logstrings);
            list.setAdapter(adapter);
           Integer list_lines_count=adapter.getCount();

           for (int  k = 0; k < list_lines_count; ++k){
               String str=logstrings.get(k);
               bw.write(str);
               bw.newLine();
                      }
             bw.close();
           Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());
      } catch (IOException e) {
           e.printStackTrace();
       }

    }
    //========================================================================
    void writeFileSD() {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        Log.d(LOG_TAG, sdPath.getAbsolutePath() + "/" + DIR_SD);
        // создаем каталог
        sdPath.mkdirs();
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FILENAME_SD);
        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            bw.write("ggdhgfhgfhfghgfhgfhgfdhfhddfghgfhgfh");
           bw.newLine();
            bw.write("867876867876867876876");

            // закрываем поток
            bw.close();
            Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void readFileSD() {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FILENAME_SD);
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            String str = "";
            // читаем содержимое

            ListView list = (ListView) findViewById(R.id.listView);
                     ArrayAdapter<String> adapter = new    ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, logstrings);
                     list.setAdapter(adapter);

            while ((str = br.readLine()) != null) {

                Log.d(LOG_TAG, str);
                logstrings.add(str);

                adapter.notifyDataSetChanged();
            }
            str=Integer.toString(adapter.getCount());
            Log.d(LOG_TAG, str); ;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

