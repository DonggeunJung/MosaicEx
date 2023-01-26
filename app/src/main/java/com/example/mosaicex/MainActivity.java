package com.example.mosaicex;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity implements Mosaic.GameEvent {
    Mosaic mosaic = null;
    Mosaic.Card gameBackground;
    Mosaic.Card cardColor;
    Mosaic.Card cardHeart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mosaic = findViewById(R.id.mosaic);
        initGame();
    }

    @Override
    protected void onDestroy() {
        if(mosaic != null)
            mosaic.clearMemory();
        super.onDestroy();
    }

    void initGame() {
        mosaic.setScreenGrid(100,140);
        mosaic.listener(this);
        gameBackground = mosaic.addCard(R.drawable.anipang_standby);
        gameBackground.addImage(R.drawable.flappybird_back);
        cardColor = mosaic.addCardColor(Color.rgb(255,240,240), 80,110,20,20);
        cardHeart = mosaic.addCard(R.drawable.icon_heart1, 34, 12, 9, 6);
        cardHeart.addImage(R.drawable.explosion01);
        cardHeart.addImage(R.drawable.explosion02);
        cardHeart.addImage(R.drawable.explosion03);
        cardHeart.addImage(R.drawable.explosion04);
        cardHeart.addImage(R.drawable.explosion05);
        cardHeart.addImage(R.drawable.explosion06);

        mosaic.playBGM(R.raw.motivational);
        mosaic.startSensorAccelerometer();
    }

    void scrollBackground() {
        gameBackground.sourceRect(0, 0, 30, 100);
        gameBackground.sourceRectIng(100, 0, 8);
    }

    // User Event start ====================================

    public void onBtn1(View v) {
        cardHeart.moving(45, 95, 1.0);
    }

    public void onBtn2(View v) {
        if(gameBackground.isSourceRectIng()) {
            gameBackground.stopSourceRectIng();
        } else {
            gameBackground.imageChange(1);
            scrollBackground();
        }
    }

    // User Event end ====================================

    // Game Event start ====================================

    @Override
    public void onGameWorkEnded(Mosaic.Card card, Mosaic.WorkType workType) {
        switch(workType) {
            case AUDIO_PLAY: {
                break;
            }
            case MOVE: {
                if(card == cardHeart) {
                    cardHeart.resizing(25, 25, 0.8);
                    mosaic.playAudioBeep(R.raw.fireworks_fire);
                }
                break;
            }
            case RESIZE: {
                if(card == cardHeart) {
                    cardHeart.imageChanging(1, 6, 1);
                    mosaic.playAudioBeep(R.raw.fireworks_boom);
                }
                break;
            }
            case IMAGE_CHANGE: {
                if(card == cardHeart) {
                    cardHeart.imageChange(0);
                    cardHeart.resize(9, 6);
                    cardHeart.move(34, 12);
                }
                break;
            }
            case SOURCE_RECT: {
                if(card == gameBackground) {
                    scrollBackground();
                }
                break;
            }
        }
    }

    @Override
    public void onGameTouchEvent(Mosaic.Card card, int action, float blockX, float blockY) {
        if(card == cardHeart && action == MotionEvent.ACTION_MOVE) {
            card.moveGap(blockX, blockY);
        }
    }

    @Override
    public void onGameSensor(int sensorType, float x, float y, float z) {
        if(sensorType == Sensor.TYPE_ACCELEROMETER) {
            float v1 = 0, v2 = 0, cut = 10, rate = 0.2f;
            if(Math.abs(x) > cut)
                v1 = (cut - Math.abs(x)) * rate;
            if(Math.abs(y) > cut)
                v2 = (cut - Math.abs(y)) * rate;
            cardColor.moveGap(v1, v2);
        }
    }

    @Override
    public void onGameCollision(Mosaic.Card card1, Mosaic.Card card2) {}

    @Override
    public void onGameTimer() {}

    // Game Event end ====================================

}