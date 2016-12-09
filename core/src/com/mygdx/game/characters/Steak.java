
package com.mygdx.game.characters;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;

/**
 * Created by my on 8/17/2016.
 */
public class Steak
{
    public Vector2 location;
    private MyGdxGame game;

    public TextureAtlas.AtlasRegion image;

    public Steak(Vector2 location, MyGdxGame game)
    {
        this.game = game;
        this.location = location;
        image = game.atlas.findRegion("steak");
    }

    public void draw()
    {
        ApplyGravity();
        game.batch.begin();
        game.batch.draw(image, this.location.x, this.location.y, 0,0,image.getRegionWidth() * MyGdxGame.PPM, image.getRegionHeight() * MyGdxGame.PPM,0.5f,0.5f,0);
        game.batch.end();
    }

    public void ApplyGravity()
    {
        if(this.location.y > 2f)
        {
            this.location.y -= 0.1f;
        }
    }

    public Rectangle getBoundary()
    {
        return new Rectangle(this.location.x, this.location.y, 1.6f, 1f);
    }

}