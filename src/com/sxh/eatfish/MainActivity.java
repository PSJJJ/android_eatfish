package com.sxh.eatfish;
import android.app.Activity;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends  Activity implements View.OnClickListener, GameView.OnGameEventListener
{

    TextView mScore;
    TextView mStart;
    LinearLayout mGameOver;
    TextView mStop;
    GameView mGameView;
    TextView mLife;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);
        findViewById(R.id.restart).setOnClickListener(this);

        mScore = (TextView) findViewById(R.id.score);
        mStart = (TextView) findViewById(R.id.start);
        mStop = (TextView) findViewById(R.id.stop);
        mGameOver = (LinearLayout) findViewById(R.id.gameover);
        mGameView = (GameView) findViewById(R.id.fishView);
        mLife = (TextView) findViewById(R.id.life);

        mGameView.setListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.start:
                onGameStart();
                break;
            case R.id.stop:
                onGameStop();
                break;
            case R.id.restart:
                onGameRestart();
                break;
        }
    }

    private void onGameRestart()
    {
        mGameView.stop();
        mGameView.start();
        mStop.setVisibility(View.VISIBLE);
        mGameOver.setVisibility(View.GONE);
        mScore.setText("score:0");
        mScore.setVisibility(View.VISIBLE);
        mLife.setText("life:7");
        mLife.setVisibility(View.VISIBLE);
    }

    private void onGameStop()
    {
        mGameView.stop();
        mStart.setVisibility(View.VISIBLE);
        mStop.setVisibility(View.GONE);
        mScore.setVisibility(View.GONE);
        mLife.setVisibility(View.GONE);
    }

    private void onGameStart()
    {
        mGameView.stop();
        mGameView.start();
        mStop.setVisibility(View.VISIBLE);
        mStart.setVisibility(View.GONE);
        mScore.setText("score:0");
        mScore.setVisibility(View.VISIBLE);
        mLife.setText("life:7");
        mLife.setVisibility(View.VISIBLE);
    }

    @Override
    public void onUpdateScore(int score)
    {
        mScore.setText("score:"+score);
    }

    @Override
    public void onGameOver()
    {
        mGameOver.setVisibility(View.VISIBLE);
        mStop.setVisibility(View.GONE);
        mLife.setVisibility(View.GONE);
    }

    @Override
    public void onUpdateLife(int life)
    {
        mLife.setText("life:"+life);
    }
}
