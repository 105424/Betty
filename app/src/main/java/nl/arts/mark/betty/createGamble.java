package nl.arts.mark.betty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;


public class createGamble extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gamble);

        setTitle("Create Gamble");
    }

    public void createGamble_create(View view){
        Gamble gamble = new Gamble();

        TextView name = (TextView)findViewById(R.id.createGamble_name);
        gamble.setPersonName(name.getText().toString());

        DatePicker date = (DatePicker)findViewById(R.id.createGamble_datePicker);

        int day = date.getDayOfMonth();
        int month = date.getMonth();
        int year =  date.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        gamble.setDate(calendar.getTime());

        Intent resultData = new Intent();
        resultData.putExtra("gamble", gamble);
        setResult(Activity.RESULT_OK, resultData);
        finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    public void createGamble_cancel(View view){
        Intent resultData = new Intent();
        setResult(Activity.RESULT_CANCELED, resultData);
        finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

}
