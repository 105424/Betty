package nl.arts.mark.betty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;


public class addBetActivity extends ActionBarActivity {

    private ArrayList<Gamble> gambles;
    private ListView lv;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bet);

        setTitle("Add Bet");

        this.gambles = new ArrayList<>();

        this.lv = (ListView)findViewById(R.id.addBet_listView);

        this.adapter = new ArrayAdapter<Gamble>(
                this,
                android.R.layout.simple_list_item_1,
                this.gambles
        );
        this.lv.setClickable(true);
        this.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Gamble gamble = (Gamble) lv.getItemAtPosition(position);
                gambles.remove(gamble);
                adapter.notifyDataSetChanged();
            }
        });
        lv.setAdapter(this.adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_bet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addGamble(View view){
        Intent intent = new Intent(this, createGamble.class);
        startActivityForResult(intent, 42);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    public void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == 42) {
            if (resultCode == RESULT_OK) {
                Gamble gamble = (Gamble)data.getSerializableExtra("gamble");
                gambles.add(gamble);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void addBet_add(View view){
        EditText eName = (EditText)findViewById(R.id.addBet_name);
        BetsDataSource bs = new BetsDataSource(this);
        bs.open();
        String s = eName.getText().toString();
        Date d = new Date();
        bs.createBet(s, d, this.gambles);
        bs.close();

        setResult(RESULT_OK);
        finish();
        overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
    }
}
