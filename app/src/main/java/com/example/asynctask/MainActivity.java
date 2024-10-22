package com.example.asynctask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView tvStatus;
    private Button btnStart, btnCancel;
    private MyAsyncTask myAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        tvStatus = findViewById(R.id.tvStatus);
        btnStart = findViewById(R.id.btnStart);
        btnCancel = findViewById(R.id.btnCancel);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute(100);  // 100 итераций
                btnCancel.setEnabled(true);
                btnStart.setEnabled(false);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myAsyncTask != null) {
                    myAsyncTask.cancel(true);
                }
            }
        });
    }

    private class MyAsyncTask extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected void onPreExecute() {
            tvStatus.setText("Status: Running");
            progressBar.setProgress(0);
        }

        @Override
        protected Void doInBackground(Integer... params) {
            int maxIterations = params[0];
            try {
                for (int i = 0; i < maxIterations; i++) {
                    if (isCancelled()) break;
                    Thread.sleep(300);
                    publishProgress((i + 1) * 100 / maxIterations);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            tvStatus.setText("Status: Finished");
            btnStart.setEnabled(true);
            btnCancel.setEnabled(false);
        }

        @Override
        protected void onCancelled() {
            tvStatus.setText("Status: Cancelled");
            progressBar.setProgress(0);
            btnStart.setEnabled(true);
            btnCancel.setEnabled(false);
        }
    }
}
