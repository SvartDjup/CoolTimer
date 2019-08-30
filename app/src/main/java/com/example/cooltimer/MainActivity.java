  package com.example.cooltimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

  public class MainActivity extends AppCompatActivity {

      private TextView textView;
      private SeekBar seekBar;
      private boolean isTimerOn;
      private Button button;
      private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("Runnable: ", "2 seconds are passed");
                handler.postDelayed(this, 2000);
            }
        };

        handler.post(runnable);*/

        textView = findViewById(R.id.textView);
        seekBar = findViewById(R.id.seekBar);
        button = findViewById(R.id.button);

        seekBar.setMax(600);
        seekBar.setProgress(30);
        isTimerOn = false;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                long progressInMillis = progress * 1000;
                updateTimer(progressInMillis);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

      public void start(View view) {

        if (!isTimerOn){
            button.setText("Stop");
            seekBar.setEnabled(false);
            isTimerOn = true;

            countDownTimer = new CountDownTimer(seekBar.getProgress()*1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    updateTimer(millisUntilFinished);

                }

                @Override
                public void onFinish() {


                    SharedPreferences sharedPreferences  =
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    if (sharedPreferences.getBoolean("enable_sound",true)){
                        MediaPlayer mediaPlayer;

                        String melodyName = sharedPreferences.getString("timer_melody", "bell");
                        switch (melodyName){
                            case "bell":
                                 mediaPlayer= MediaPlayer.create(getApplicationContext(),
                                        R.raw.bell_sound);
                                mediaPlayer.start();
                                break;
                            case "alarm_siren":
                                mediaPlayer = MediaPlayer.create(getApplicationContext(),
                                        R.raw.alarm_siren_sound);
                                mediaPlayer.start();
                                break;
                            case "bip":
                                mediaPlayer = MediaPlayer.create(getApplicationContext(),
                                        R.raw.bip_sound);
                                mediaPlayer.start();
                                break;

                        }


                    }
                    resetTimer();

                }
            }.start();


        }else {
            resetTimer();
        }


      }

      private void  updateTimer(long millisUntilFinished){
          int minutes = (int)millisUntilFinished/1000/60;
          int seconds = (int)millisUntilFinished/1000 - (minutes * 60);

          String minutesStr = "";
          String secondsStr = "";

          if (minutes<10){
              minutesStr = "0" + minutes;

          }else {
              minutesStr = String.valueOf(minutes);
          }

          if (seconds<10){
              secondsStr = "0" + seconds;
          }else {
              secondsStr = String.valueOf(seconds);
          }

          textView.setText(minutesStr + ":" + secondsStr);

      }

      private  void resetTimer(){


          countDownTimer.cancel();
          textView.setText("00:30");
          button.setText("Start");
          seekBar.setEnabled(true);
          seekBar.setProgress(30);
          isTimerOn = false;

      }

      @Override
      public boolean onCreateOptionsMenu(Menu menu) {
          MenuInflater menuInflater = getMenuInflater();
          menuInflater.inflate(R.menu.timer_menu, menu);
          return true;
      }

      @Override
      public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings){
            Intent openSettings = new Intent(this, SettingsActivity.class);
            startActivity(openSettings);
            return true;
        }else if (id == R.id.action_about){
            Intent openAbout = new Intent(this, AboutActivity.class);
            startActivity(openAbout);
            return true;
        }
          return super.onOptionsItemSelected(item);
      }
  }
