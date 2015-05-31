package nl.arts.mark.betty;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class Bet {
    private long id;
    private String name;
    private Date date;
    private ArrayList<Gamble> gambles;
    private Boolean won;
    private String winner;

    public Bet(){
        won = false;
        winner = "";
    }

    public void solve(Context x, Date date){
        this.setDate(date);
        this.setWinner(calcWinner(getDate()).getPersonName());
        this.setWon(true);
        this.save(x);
    }

    public Gamble calcWinner(Date date){

        long ts = date.getTime();

        Gamble closest = gambles.get(0);
        for(Gamble gamble : gambles){
            if(Math.abs(ts - gamble.getDate().getTime()) < Math.abs(ts - closest.getDate().getTime())){
                closest = gamble;
            }
        }
        return closest;
    }

    public void save(Context x){
        BetsDataSource bs = new BetsDataSource(x);
        bs.open();
        bs.saveBet(this);
        bs.close();
    }

    public Boolean getWon() {return won; }
    public void setWon(Boolean won) {this.won = won; }

    public String getWinner() {return winner; }
    public void setWinner(String name){ this.winner = name; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getDate() { return this.date; }
    public void setDate(Date date) {
        this.date = date;
    }
    public void setDate(String d){
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        try {
            this.date = format.parse(d);
        } catch (ParseException ex){
            this.date = new Date();
        }
    }

    public ArrayList<Gamble> getGambles() { return gambles; }
    public void setGambles(ArrayList<Gamble> gambles) { this.gambles = gambles;}

    @Override
    public String toString() {
        return this.name;
    }
}