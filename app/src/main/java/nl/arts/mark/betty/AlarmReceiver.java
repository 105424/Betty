package nl.arts.mark.betty;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import java.util.Date;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        if(prefs.getBoolean("notify_winners", true)) {
            check(context);
        }
    }

    private void check(Context context){
        BetsDataSource bs = new BetsDataSource(context);

        bs.open();
        List<Bet> bets = bs.getAllBets();
        bs.close();

        for(Bet bet : bets){
            if(!bet.getWon()){
                Gamble win =  bet.calcWinner(new Date());
                if(!bet.getLastWinner().equals(win.getPersonName()) ){
                    bet.setLastWinner(win.getPersonName());
                    bet.save(context);

                    createNotification(context, bet, win.getPersonName());
                }
            }
        }
    }


    private void createNotification(Context context, Bet bet, String winner){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_stat_action_alarm)
                        .setContentTitle("A new person might win a bet")
                        .setContentText(winner+" will win: "+bet.getName());
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, BetActivity.class);
        resultIntent.putExtra("betId", bet.getId());

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(BetActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(500094, mBuilder.build());
    }

}
