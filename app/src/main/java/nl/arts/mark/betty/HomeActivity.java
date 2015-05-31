package nl.arts.mark.betty;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ListView ls;
    private ArrayAdapter<Bet> a;
    private List<Bet> bets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setService(this);

        BetsDataSource bs = new BetsDataSource(this);

        bs.open();
        bets = bs.getAllBets();
        bs.close();

        ls = (ListView)findViewById(R.id.l_bets);
        final Context context = this;

        a = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                bets
        );
        ls.setClickable(true);
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Bet bet = (Bet)ls.getItemAtPosition(position);
                Intent intent = new Intent(context, BetActivity.class);
                intent.putExtra("betId", bet.getId());
                startActivityForResult(intent, 9001);
            }
        });
        ls.setAdapter(a);

    }

    public static void setService(Context context){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);

        if(prefs.getBoolean("notify_winners", true)){
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000, pendingIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.betty_settings) {
            this.closeOptionsMenu();
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void refreshList(View view){
        BetsDataSource bs = new BetsDataSource(this);
        bs.open();
        bets = bs.getAllBets();
        bs.close();

        a = new ArrayAdapter<Bet>(
                this,
                android.R.layout.simple_list_item_1,
                bets
        );
        ls.setAdapter(a);
    }

    public void b_addBetClick(View view){
        Intent intent = new Intent(this, addBetActivity.class);
        startActivityForResult(intent, 666);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){
            refreshList(null);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
