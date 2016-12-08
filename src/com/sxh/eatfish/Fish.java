package com.sxh.eatfish;


import android.graphics.Rect;

/**
 * Created by sxh on 2016/11/25.
 */
public class Fish
{
    private int mSpeed = 1; //鱼游动速度
    private boolean mHeadToRight= true; //鱼头朝右
    private int mX = 0; //坐标位置
    private int mY = 0;
    private int mWidth = 0;
    private int mHeight = 0;
    private int mWeight = 10; //个头大小
    private int mType = 0;//类型
    private int mLife = 0;





    public Fish()
    {

    }

    public int getSpeed()
    {
        return mSpeed;
    }

    public void setSpeed(int speed)
    {
        mSpeed = speed;
    }

    public boolean isHeadToRight()
    {
        return mHeadToRight;
    }

    public void setHeadToRight(boolean headToRight)
    {
        mHeadToRight = headToRight;
    }

    public int getX()
    {
        return mX;
    }

    public void setX(int x)
    {
        mX = x;
    }

    public int getY()
    {
        return mY;
    }

    public void setY(int y)
    {
        mY = y;
    }

    public int getWeight()
    {
        return mWeight;
    }

    public void setWeight(int weight)
    {
        mWeight = weight;
    }

    public int getType()
    {
        return mType;
    }

    public void setType(int type)
    {
        mType = type;
    }

    public int getWidth()
    {
        return mWidth;
    }

    public void setWidth(int width)
    {
        mWidth = width;
    }

    public int getHeight()
    {
        return mHeight;
    }

    public void setHeight(int height)
    {
        mHeight = height;
    }

    public int getLife()
    {
        return mLife;
    }

    public void setLife(int life)
    {
        mLife = life;
    }

    public boolean hitTest(int x, int y)
    {
        Rect rect = new Rect(mX,mY, mX +mWidth,mY+mHeight);
        return  rect.contains(x,y);
    }

    public void move(int x,int y)
    {
        int tx = x-mWidth/2;
        mY = y-mHeight/2;
        mHeadToRight = tx>mX;
        mX = tx;
    }
}

