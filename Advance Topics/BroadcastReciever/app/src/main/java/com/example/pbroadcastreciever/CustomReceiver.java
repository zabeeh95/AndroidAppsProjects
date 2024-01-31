package com.example.pbroadcastreciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CustomReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String Action = intent.getAction();

        /*
            akela chalao chlta ha switch mai dalnay ka tareeqa amloom krna ha

           String recievedtext = intent.getStringExtra("main_activity");
           Toast.makeText(context, "custom vala chal gya ha ub", Toast.LENGTH_SHORT).show();
         */

       if (Action != null) {
            String toastMessage = context.getString(R.string.unknown_action);
            switch (Action){
                case Intent.ACTION_POWER_CONNECTED:
                    toastMessage = context.getString(R.string.power_connected);
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    toastMessage =context.getString(R.string.power_disconnected);
                    break;
            }
       }


    }

}
