package nl.arts.mark.betty;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class BetActivity extends AppCompatActivity {

    private Bet bet;

    private TextView winV;
    private TextView winL;
    private TextView dateV;
    private TextView dateL;
    private Button solve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bet);

        Intent intent = getIntent();
        Long id = intent.getLongExtra("betId", 0);
        BetsDataSource bs = new BetsDataSource(this);
        bs.open();
        this.bet = bs.getBet(id);
        bs.close();

        solve = (Button)findViewById(R.id.b_bet_solve);

        winV = (TextView)findViewById(R.id.bet_winnerText);
        winL = (TextView)findViewById(R.id.bet_winnerLabel);
        dateV = (TextView)findViewById(R.id.bet_winDateLabel);
        dateL = (TextView)findViewById(R.id.bet_winDateText);

        if(bet.getWon()){
            winV.setText(bet.getWinner());
            solve.setVisibility(View.GONE);
            SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
            dateV.setText(f.format(bet.getDate()));
        }else{
            winV.setVisibility(View.GONE);
            winL.setVisibility(View.GONE);
            dateV.setVisibility(View.GONE);
            dateL.setVisibility(View.GONE);
        }

        TextView tv = (TextView)findViewById(R.id.bet_name);
        tv.setText(this.bet.getName());
        setTitle(this.bet.getName());

        final ListView ls = (ListView)findViewById(R.id.bet_list);
        ArrayAdapter<Gamble> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                this.bet.getGambles()
        );
        ls.setAdapter(adapter);

    }

    public void b_solve(View view) {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        final Context cont = this;
        DatePickerDialog dpd = new DatePickerDialog(this,
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar c = Calendar.getInstance();
                    c.set(year, monthOfYear - 1, dayOfMonth, 0, 0);

                    bet.solve(cont, c.getTime());

                    winV.setText(bet.getWinner());
                    SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
                    dateV.setText(f.format(bet.getDate()));
                    
                    winV.setVisibility(View.VISIBLE);
                    winL.setVisibility(View.VISIBLE);
                    dateL.setVisibility(View.VISIBLE);
                    dateV.setVisibility(View.VISIBLE);
                    solve.setVisibility(View.GONE);

                }
            }, mYear, mMonth, mDay);
        dpd.show();
    }

    public void b_bet_delete(View view){
        BetsDataSource bs = new BetsDataSource(this);
        bs.open();
        bs.deleteBet(this.bet);
        bs.close();

        setResult(RESULT_OK);
        finish();
    }

}
