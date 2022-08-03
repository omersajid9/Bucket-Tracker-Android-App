package com.depauw.buckettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.depauw.buckettracker.databinding.ActivityMainBinding;

import java.net.SecureCacheResponse;

public class MainActivity extends AppCompatActivity
{

    private final static int DEFAULT_NUM_MINS = 20;
    private final static int MILLIS_PER_MIN = 60000;
    private final static int MILLIS_PER_SEC = 1000;
    private final static int SECS_PER_MIN = 60;

    private String cur_time;



    private CountDownTimer timer;

    private ActivityMainBinding binding;


    private final View.OnLongClickListener button_add_score_onlongclicklistener = new View.OnLongClickListener()
    {


        @Override
        public boolean onLongClick(View v)
        {

            int score = 0;
            if (binding.checkboxAddOne.isChecked())
            {
                score += Integer.valueOf(binding.checkboxAddOne.getText().toString());
            }
            if (binding.checkboxAddTwo.isChecked())
            {
                score += Integer.valueOf(binding.checkboxAddTwo.getText().toString());
            }
            if (binding.checkboxAddThree.isChecked())
            {
                score += Integer.valueOf(binding.checkboxAddThree.getText().toString());
            }

            if(score >0)
            {

                if (binding.toggleIsGuest.getText().toString().equalsIgnoreCase("home"))
                {
                    binding.textviewHomeScore.setText(String.valueOf(Integer.valueOf(binding.textviewHomeScore.getText().toString()) + score));
                } else
                {
                    binding.textviewGuestScore.setText(String.valueOf(Integer.valueOf(binding.textviewGuestScore.getText().toString()) + score));
                }


                binding.toggleIsGuest.toggle();
                changePossession();
                clearCheckboxes();
            }
            return true;
        }
    };

    private final View.OnClickListener compoundView_onclicklistener = new View.OnClickListener()
    {

        @Override
        public void onClick(View view)
        {switch(view.getId())
        {   case R.id.toggle_is_guest:
                changePossession();
                break;
            case R.id.switch_game_clock:
                if (binding.switchGameClock.isChecked()) {


                    long min = Long.valueOf(cur_time.split(":")[0]) * MILLIS_PER_MIN + Long.valueOf(cur_time.split(":")[1]) * MILLIS_PER_SEC;
                    Log.d("omer", String.valueOf(min));
                    timer = getNewTimer(min, MILLIS_PER_SEC);
                    timer.start();


                } else {
                    cur_time = binding.textviewTimeRemaining.getText().toString();
                    Log.d("omer", cur_time);
                    timer.cancel();

                }
                break;
        }
        }
    };

    boolean checkIfNotEmpty(EditText text)
    {
        return text.getText().toString().length() > 0;
    }


    private View.OnClickListener button_set_time_onclicklistener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if(checkIfNotEmpty(binding.edittextNumMins) && checkIfNotEmpty(binding.edittextNumSecs))
            {
                int secs = Integer.valueOf(binding.edittextNumSecs.getText().toString());
                int mins = Integer.valueOf(binding.edittextNumMins.getText().toString());


                if((secs >= 0 && secs < SECS_PER_MIN) && (mins >= 0  && mins < DEFAULT_NUM_MINS))
                {
                    timer.cancel();
                    cur_time = binding.edittextNumMins.getText().toString() + ":" + binding.edittextNumSecs.getText().toString();
                    setTime(cur_time);
                    binding.switchGameClock.setChecked(false);
                }
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cur_time = binding.textviewTimeRemaining.getText().toString();

        binding.toggleIsGuest.setOnClickListener(compoundView_onclicklistener);
        binding.buttonAddScore.setOnLongClickListener(button_add_score_onlongclicklistener);
        binding.switchGameClock.setOnClickListener(compoundView_onclicklistener);
        binding.buttonSetTime.setOnClickListener(button_set_time_onclicklistener);
        setScoreColor();


    }

    public void setScoreColor()
    {
        String text = binding.toggleIsGuest.getText().toString();
        Log.d("Omer", text);
        if(text.equalsIgnoreCase("Home")){
            binding.labelHome.setTextColor(getResources().getColor(R.color.red, getTheme()));
            binding.textviewHomeScore.setTextColor(getResources().getColor(R.color.red, getTheme()));
            binding.labelGuest.setTextColor(getResources().getColor(R.color.black, getTheme()));
            binding.textviewGuestScore.setTextColor(getResources().getColor(R.color.black, getTheme()));
        }
        else
        {
            binding.labelHome.setTextColor(getResources().getColor(R.color.black, getTheme()));
            binding.textviewHomeScore.setTextColor(getResources().getColor(R.color.black, getTheme()));
            binding.labelGuest.setTextColor(getResources().getColor(R.color.red, getTheme()));
            binding.textviewGuestScore.setTextColor(getResources().getColor(R.color.red, getTheme()));
        }
    }

    public void clearCheckboxes()
    {
        binding.checkboxAddOne.setChecked(false);
        binding.checkboxAddTwo.setChecked(false);
        binding.checkboxAddThree.setChecked(false);
    }

    public void changePossession()
    {
//        binding.toggleIsGuest.toggle(); //change the possession of the team
        setScoreColor();
        Log.d("Omer", "Before" + binding.toggleIsGuest.getText().toString());
    }


    public CountDownTimer getNewTimer(long length, long tick_length)
    {
        return new CountDownTimer(length, tick_length)
        {
            @Override
            public void onTick(long l)
            {
                long cur_min = (long) l/MILLIS_PER_MIN;
                long cur_sec = (l - cur_min*MILLIS_PER_MIN)/MILLIS_PER_SEC;
                cur_time = cur_min + ":" + cur_sec;
                Log.d("omer", "time changed" + cur_time);
                setTime(cur_time);
            }

            @Override
            public void onFinish() {
                Log.d("omer", "done");
            }
        };

    }

    public void setTime(String time)
    {
        binding.textviewTimeRemaining.setText(time);
    }


}