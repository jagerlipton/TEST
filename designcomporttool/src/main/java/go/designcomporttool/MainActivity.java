package go.designcomporttool;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    TextView status;
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


}
