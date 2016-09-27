package go.designcomporttool;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Environment;
import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;


public class MainActivity extends AppCompatActivity {
    TextView status;
    final String LOG_TAG = "myLogs";

    final ArrayList<String> logstrings = new ArrayList<String>();
    static final SimpleDateFormat DATA_FORMAT = new SimpleDateFormat("dd_MM_yyyy");
    static final String LOG_EXTENTION = ".txt";
    private static final int REQUEST_DIRECTORY = 0;
    private static final String TAG = "DirChooserSample";



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
            case R.id.action_settings_comport:
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                return true;
            case R.id.action_exit:
                this.finish();
                return true;
            case R.id.action_settings_directory_path:
              Start_chooser_dialog("settings_directory_path");
                return true;
            case R.id.action_saveas:
                Start_chooser_dialog("saveas");
                     return true;
            case R.id.action_save:
                WriteFile_from_listview(Global_data.Gd_Directory_path);
                return true;
            case R.id.action_readfile:
             openFileDialog();

                return true;
            case R.id.action_read:
                azaza();
                return true;
                    default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Read_Comport_settings();

    }
  public void Start_chooser_dialog(String extra)
  {
      final Intent chooserIntent = new Intent(
              MainActivity.this,
              DirectoryChooserActivity.class);

      final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
              .newDirectoryName("Logs")
              .allowReadOnlyDirectory(true)
              .allowNewDirectoryNameModification(true)
              .build();
      chooserIntent.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config);
      Global_data.Gd_Intent_data=extra;
      startActivityForResult(chooserIntent, REQUEST_DIRECTORY);
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
     downloadType = SP.getString("pref_directory_path","");
     Global_data.Gd_Directory_path=downloadType;

     Global_data.Gd_BAUDRATE=Integer.parseInt(Global_data.Gd_comport_baudrate);
     Global_data.Gd_DATABITS=Integer.parseInt(Global_data.Gd_comport_databit);

     switch(Global_data.Gd_comport_stopbits) {
         case "1":
             Global_data.Gd_STOPBITS=1;
             break;
         case "1,5":
             Global_data.Gd_STOPBITS=3;
             break;
         case  "2":
             Global_data.Gd_STOPBITS=2;
             break;
              }
     switch(Global_data.Gd_comport_flowcontrol) {
         case "Xon/Xoff In":
             Global_data.Gd_FLOWCONTROL=4;
             break;
         case "Xon/Xoff Out":
             Global_data.Gd_FLOWCONTROL=8;
             break;
         case  "RTS/CTS In":
             Global_data.Gd_FLOWCONTROL=1;
             break;
         case  "RTS/CTS Out":
             Global_data.Gd_FLOWCONTROL=2;
             break;
         case  "Нет":
             Global_data.Gd_FLOWCONTROL=0;
             break;
     }

    }


    public void writephone() {
     //gg
    }

    public void openFileDialog() {

        FileChooser filechooser = new FileChooser(this);
        filechooser.setFileListener(new FileChooser.FileSelectedListener() {
            @Override
            public void fileSelected(final File file) {
                String filename = file.getAbsolutePath();
                Log.d(LOG_TAG, "Открыть файл для чтения: " + filename);
                readfile(filename);
                         }
        });
        filechooser.setExtension("txt");
        filechooser.showDialog();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        if (requestCode == REQUEST_DIRECTORY) {
            Log.i(TAG, String.format("Return from DirChooser with result %d",  resultCode));
            status = (TextView) findViewById(R.id.textView);
            if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                if (Global_data.Gd_Intent_data == "settings_directory_path") {
                    Global_data.Gd_Directory_path=data.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR);
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("pref_directory_path",data.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR));
                    editor.commit();
                }
                if (Global_data.Gd_Intent_data == "saveas") {
                    WriteFile_from_listview(data.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR));
                }
            }
        }
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

    void readfile(String filename) {

        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }

        File sdFile = new File(filename);
        try {

            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            String str = "";


            ListView list = (ListView) findViewById(R.id.listView);
                     ArrayAdapter<String> adapter = new    ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, logstrings);
                     list.setAdapter(adapter);
logstrings.clear();
            while ((str = br.readLine()) != null) {
                       logstrings.add(str);
                adapter.notifyDataSetChanged();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public   void azaza()  {
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        if (availableDrivers.isEmpty()) {
            return;
        }

// Open a connection to the first available driver.
        UsbSerialDriver driver = availableDrivers.get(0);
        UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
        if (connection == null) {
            // You probably need to call UsbManager.requestPermission(driver.getDevice(), ..)
            return;
        }

// Read some data! Most have just one port (port 0).
        UsbSerialPort port = driver.getPorts().get(0);
        try {
            port.open(connection);
           port.setParameters(Global_data.Gd_BAUDRATE, Global_data.Gd_DATABITS, Global_data.Gd_STOPBITS, Global_data.Gd_PARITY);

            byte buffer[] = new byte[16];
            int numBytesRead = port.read(buffer, 1000);
            //  Log.d(TAG, "Read " + numBytesRead + " bytes.");
        } catch (IOException e) {
            // Deal with error.
        } finally {
            try {
                port.close();
            } catch(IOException ex) {
                ex.printStackTrace();
            }

        }

    }

    public void connect_click (View v){
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
               if (availableDrivers.isEmpty()) {
                   status = (TextView) findViewById(R.id.textView2);
                   status.setText("Портов не найдено");
                   Log.d(LOG_TAG, "Портов не найдено");
            return;
        }
   //  status = (TextView) findViewById(R.id.textView2);
     //   status.setText("Connected");
        Log.d(LOG_TAG, "Обнаружен порт");

        UsbSerialDriver driver = availableDrivers.get(0);
        UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
        if (connection == null) {
             status = (TextView) findViewById(R.id.textView2);
             status.setText("Connected");
            Log.d(LOG_TAG, "Connected");
            return;
        }

    }
}

