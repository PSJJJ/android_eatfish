package com.sxh.eatfish;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by sxh on 2016/11/25.
 */
public class GameView extends View
{
    private Fish mMainRole = new Fish(); //游戏主角
    private ArrayList<Fish> mFishs = new ArrayList<Fish>(); //鱼列表
    private Bitmap mFinshBmp;
    private Rect mRect = new Rect();
    private boolean mIsPress = false;
    private SparseIntArray mFishRes = new SparseIntArray();
    private SparseIntArray mFishWeight = new SparseIntArray();
    private boolean mIsRun = false;
    private int mScore;

    private Thread mThread;


    public interface OnGameEventListener
    {
        void onUpdateScore(int score);
        void onGameOver();
        void onUpdateLife(int life);
    }

    OnGameEventListener mListener;

    public void setListener(OnGameEventListener listener)
    {
        mListener = listener;
    }

    public GameView(Context context)
    {
        super(context);
    }

    public GameView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public void start()
    {
        mIsRun = false;
        mFishRes.clear();
        mFishs.clear();
        mIsRun = true;
        init();
    }

    public void stop()
    {
        mIsRun = false;
        invalidate();
    }



    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            calcGameStatu();
            invalidate();
            super.handleMessage(msg);
        }
    };

    private void init()
    {
        mFishRes.append(1,R.drawable.clown_fish_24px);
        mFishRes.append(2,R.drawable.clown_fish_32px);
        mFishRes.append(3,R.drawable.clown_fish_48px);
        mFishRes.append(4,R.drawable.clown_fish_64px);
        mFishRes.append(5,R.drawable.clown_fish_72px);
        mFishRes.append(6,R.drawable.clown_fish_96px);
        mFishRes.append(7,R.drawable.clown_fish_128px);


        mScore = 0;
        //获取显示区域大小
        int width = getWidth();
        int height = getHeight();

        //创建主角鱼
        mMainRole = new Fish();
        mMainRole.setHeadToRight(true);
        mMainRole.setWeight(5);
        mMainRole.setType(5);
        mMainRole.setLife(5);
        if(mListener!=null)
        {
            mListener.onUpdateScore(0);
            mListener.onUpdateLife(5);
        }
        Bitmap bmp = getBitmapByType(mMainRole.getType());
        mFinshBmp = bmp;
        mMainRole.setWidth(bmp.getWidth());
        mMainRole.setHeight(bmp.getHeight());
        mMainRole.setX(width/2-mMainRole.getWidth()/2);
        mMainRole.setY(height/2-mMainRole.getHeight()/2);


        int size = mFishRes.size();


        for(int i=1; i<=size; ++i)
        {
            mFishWeight.append(i,i);
            mFishs.add( randomFish(width,height) ) ;
        }

        mThread = new Thread()
        {
            @Override
            public void run()
            {
                while(mIsRun)
                {
                    SystemClock.sleep(50);
                    updateFish();
                    mHandler.sendMessage(Message.obtain());

                }
            }
        };
        mThread.start();

    }

    private void updateFish()
    {
        int width = getWidth();
        int height = getHeight();
        ArrayList<Fish> goneFishs = new ArrayList<Fish>();
        synchronized (mFishs)
        {
            for (Fish fish : mFishs)
            {
                int x = fish.getX();
                if (fish.isHeadToRight())
                {
                    x += fish.getSpeed();
                    if (x > width)
                    {
                        goneFishs.add(fish);
                    }
                } else
                {
                    x -= fish.getSpeed();
                    if (x + fish.getWidth() < 0)
                    {
                        goneFishs.add(fish);
                    }
                }
                fish.setX(x);
            }
            for (Fish fish : goneFishs)
            {
                mFishs.remove(fish);
                fish = randomFish(width, height);
                mFishs.add(fish);
            }
        }
    }

    private Fish randomFish(int width, int height)
    {
        Fish fish = new Fish();
        //随机类型和大小
        int iType = (int)(Math.random()*1000);
        iType %= mFishRes.size();
        iType += 1;
        fish.setType(iType);
        fish.setWeight(iType);
        //随机方向
        fish.setHeadToRight( iType%2==1);

        Bitmap bmp = getBitmapByType(iType);
        fish.setWidth(bmp.getWidth());
        fish.setHeight(bmp.getHeight());



        //随机y方向位置
        iType = (int)(Math.random()*10000);
        fish.setY(iType%height);

        //随机鱼游动速度
        iType = (int)(Math.random()*1000);
        fish.setSpeed(iType%20+3);
        //随机鱼起始x方向位置
        fish.setX(fish.isHeadToRight()?(-fish.getWidth()): width);
        return fish;
    }



    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        drawAllFinsh(canvas);
    }


    void drawFish(Canvas canvas, Paint paint,Fish fish,Bitmap fishBmp)
    {

        paint.setTextSize(30);
        canvas.drawText(""+fish.getWeight(), fish.getX(),fish.getY(), paint);
        if(fish.isHeadToRight()==false)
        {
            Matrix matrix = new Matrix();
            matrix.postScale(-1f,1f, fish.getWidth()/2, fish.getHeight()/2);
            matrix.postTranslate(fish.getX(), fish.getY());
            canvas.drawBitmap(fishBmp, matrix,paint);
        }
        else
        {
            canvas.drawBitmap(fishBmp,fish.getX(),fish.getY(), paint);
        }
    }

    Bitmap getBitmapByType(int iType)
    {
        return BitmapFactory.decodeResource(getResources(), mFishRes.get(iType));
    }

    void drawAllFinsh(Canvas canvas)
    {
        if(mIsRun==false)
        {
            return;
        }
        Paint paint = new Paint();
        drawFish(canvas,paint, mMainRole, mFinshBmp);

        synchronized (mFishs)
        {
            for (Fish fish : mFishs)
            {
                Bitmap bmp = getBitmapByType(fish.getType());
                drawFish(canvas, paint, fish, bmp);
                bmp.recycle();
            }
        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        Log.i("sxh","acid:"+event.getAction());
        boolean bHandle = true;
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mIsPress = mMainRole.hitTest((int)event.getX(),(int)event.getY());
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsPress = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if(mIsPress)
                {
                    mMainRole.move((int)event.getX(),(int)event.getY());
                    calcGameStatu();
                    invalidate();
                }
                break;
            default:
                bHandle = false;
                break;
        }
        if(bHandle) return true;
        return super.onTouchEvent(event);
    }

    private boolean fishInArea(Rect rect, Fish fish)
    {
        int left = fish.getX();
        int top = fish.getY();
        int right = left+fish.getWidth();
        int bottom = top+fish.getHeight();
        return  rect.contains(left,top) || rect.contains(right,top)
                || rect.contains(right,bottom) || rect.contains(left,bottom);
    }

    private void calcGameStatu()
    {
        //if(true) return;
        Rect rect = new Rect(mMainRole.getX(),mMainRole.getY(), mMainRole.getX()+mMainRole.getWidth(),
                mMainRole.getY()+mMainRole.getHeight());
        ArrayList<Fish> eats = new ArrayList<Fish>();
        synchronized (mFishs)
        {


            for (Fish fish : mFishs)
            {
                if (fishInArea(rect, fish))//与主角发生碰撞
                {
                    int life = mMainRole.getLife();
                    if (fish.getWeight() <= mMainRole.getWeight()) //比主角小, 吃掉
                    {
                        eats.add(fish);
                        life += fish.getWeight();
                        mScore += fish.getWeight();
                        mMainRole.setLife(life);
                        if (mListener != null)
                        {
                            mListener.onUpdateLife(life);
                            mListener.onUpdateScore(mScore);

                        }
                    } else
                    {

                        //life -= fish.getWeight(); //这里可以控制只少血, 不直接死
                        //eats.add(fish);
                        life = 0; //碰大鱼,直接Game Over
                        if (life <= 0)
                        {
                            stop();
                            if (mListener != null)
                            {
                                mListener.onGameOver();
                            }
                        } else
                        {
                            mMainRole.setLife(life);
                            if (mListener != null)
                            {
                                mListener.onUpdateLife(life);
                            }
                        }
                    }
                }
            }
            int iWidth = getWidth();
            int iHeight = getHeight();
            for (Fish fish : eats)
            {
                mFishs.remove(fish);
                fish = randomFish(iWidth, iHeight);
                mFishs.add(fish);
            }
        }
    }
}
