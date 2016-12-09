package com.mygdx.game.characters;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;

public class Angler
{
    public boolean right = true;
    public boolean top = true;
    private float dd = 0.1f;
    private float yd = 0.0f;
    private float ytemp = 0.0f;
    private float xstart, ystart, xend, yend;
    private float borderX, borderY;

    public Vector2 position;
    public float getXstart()
    {
        return xstart;
    }

    public float getYstart()
    {
        return ystart;
    }

    public float getXend()
    {
        return xend;
    }

    public float getYend()
    {
        return yend;
    }

    private Animation anglerfishAnimation;

    public Animation getAnglerfishAnimation()
    {
        return anglerfishAnimation;
    }

    private TextureRegion[][] anglerfishTmp;
    private TextureRegion[] anglerfishAnimationFrames;

    public Vector2 getPos()
    {
        return this.position;
    }

    public void setPos(Vector2 pos)
    {
        this.position = pos;
    }

    public Angler(MyGdxGame game, World w, float x, float y, float xstart, float ystart, float xend, float yend)
    {
        this.position = new Vector2(x, y);

        this.right = MathUtils.randomBoolean();

        this.xstart = xstart;
        this.xend = xend;
        this.ystart = ystart;
        this.yend = yend;

        anglerfishTmp = game.atlas.findRegion("anglerfish_spritesheet").split(
                game.atlas.findRegion("anglerfish_spritesheet").getRegionWidth() / 10,
                game.atlas.findRegion("anglerfish_spritesheet").getRegionHeight());
        anglerfishAnimationFrames = new TextureRegion[10];


        borderX = game.atlas.findRegion("anglerfish_spritesheet").getRegionWidth() / 10;
        borderY = game.atlas.findRegion("anglerfish_spritesheet").getRegionHeight();
        int index = 0;
        for (int i = 0; i < anglerfishTmp.length; i++)
        {
            for (int j = 0; j < anglerfishTmp[0].length; j++)
            {
                anglerfishAnimationFrames[index++] = anglerfishTmp[i][j];
            }
        }
        anglerfishAnimation = new Animation(0.025f, anglerfishAnimationFrames);
    }

    public void move(float startx, float endx, float dx, boolean flip)
    {
        if (flip)
        {
            dx = dx * -1f;
        }
        if (this.right)
        {

            this.setPos(new Vector2(this.getPos().x + dx, this.getPos().y));
            if (this.getPos().x > endx)
            {
                this.right = false;
            }
        }
        if (!this.right)
        {
            this.setPos(new Vector2(this.getPos().x - dx, this.getPos().y));
            if (this.getPos().x < startx)
            {
                this.right = true;
            }
        }
    }

    public void move(float startx, float endx, float dx)
    {
        if (this.right)
        {
            this.setPos(new Vector2(this.getPos().x + dx, this.getPos().y));
            if (this.getPos().x > endx)
            {
                this.right = false;
            }
        }
        if (!this.right)
        {
            this.setPos(new Vector2(this.getPos().x - dx, this.getPos().y));
            if (this.getPos().x < startx)
            {
                this.right = true;
            }
        }
    }

    public void move(float startx, float endx, float dx, float starty, float endy, float dy, boolean randomY)
    {
        float toX = 0;
        float toY = 0;

        if (this.right)
        {
            toX = this.getPos().x + dx;
            if (this.getPos().x > endx)
            {
                this.right = false;
            }
        }
        if (!this.right)
        {
            toX = this.getPos().x - dx;
            if (this.getPos().x < startx)
            {
                this.right = true;
            }
        }

        if (randomY && yd == 0)
        {
            Random random = new Random();
            int temp = random.nextInt(2);

            yd = random.nextInt((int) (endy - starty));
            if (temp == 1)
            {
                top = true;
                ytemp = position.y;

            } else
            {
                top = false;
                ytemp = position.y;
            }
        }

        toY = ytemp + (top ? lerp(0, yd, dd) : -lerp(0, yd, dd));
        toY = MathUtils.clamp(toY, starty, endy);
        dd += 0.005f;
        if (dd > 1)
        {
            dd = 0f;
            yd = 0;
        }

        this.setPos(new Vector2(toX, toY));
    }

    float lerp(float a, float b, float f)
    {
        return a + f * (b - a);
    }
}
