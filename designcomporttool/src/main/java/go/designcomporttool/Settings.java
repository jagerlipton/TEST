package go.designcomporttool;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class Settings extends AppCompatActivity {
    String[] names = { "Скорость", "биты данных", "четность", "стоп биты", "flow control"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ListView lvMain = (ListView) findViewById(R.id.lvMain);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        lvMain.setAdapter(adapter);
          }
}
