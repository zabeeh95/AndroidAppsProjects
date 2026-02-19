package com.example.simpleasynctask;

import android.os.AsyncTask;
import android.text.PrecomputedText;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Random;

public class SimpleAsyncTask extends AsyncTask<Void, Void, String> {

    private WeakReference<TextView> textViewWeakReference;

    public SimpleAsyncTask(TextView tv) {
        textViewWeakReference = new WeakReference<>(tv);
    }


    @Override
    protected String doInBackground(Void... Voids) {
        int s = 2 * 2000;
        try {
            Thread.sleep(s);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return "awake after sleeping for" + s + "  seconds";
    }



    protected void onPostExecute(String result){
        textViewWeakReference.get().setText(result);
    }

}
