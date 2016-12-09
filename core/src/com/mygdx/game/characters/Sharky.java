
package com.mygdx.game.characters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;

/**
 * Created by my on 8/17/2016.
 */
public class Sharky extends Sprite
{
    private int health = 100;

    public World world;
    public Body b2Body;
    private Circle boundary;
    private float circleRadius = 32f * MyGdxGame.PPM;


    private Animation sharkAnimation;

    public Animation getSharkAnimation()
    {
        return sharkAnimation;
    }

    public Animation getSharkShockAnimation()
    {
        return sharkShockAnimation;
    }

    private Animation sharkShockAnimation;
    private TextureRegion[][] sharkShockTmp;
    private TextureRegion[] sharkShockAnimationFrames;

    private TextureRegion[][] sharkTmp;
    private TextureRegion[] sharkAnimationFrames;

    public Sharky(World world, MyGdxGame game)
    {
        this.world = world;
        defineMainPlayer();

        sharkShockTmp = game.atlas.findRegion("s_plyr_elec_strip2").split(
                game.atlas.findRegion("s_plyr_elec_strip2").getRegionWidth() / 2,
                game.atlas.findRegion("s_plyr_elec_strip2").getRegionHeight());
        sharkShockAnimationFrames = new TextureRegion[2];


        int index = 0;
        for (int i = 0; i < 1; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                sharkShockAnimationFrames[index++] = sharkShockTmp[i][j];
            }
        }

        sharkShockAnimation = new Animation(0.25f, sharkShockAnimationFrames);


        sharkTmp = game.atlas.findRegion("s_plyr_swim_strip10").split(
                game.atlas.findRegion("s_plyr_swim_strip10").getRegionWidth() / 10,
                game.atlas.findRegion("s_plyr_swim_strip10").getRegionHeight());
        sharkAnimationFrames = new TextureRegion[10];

        index = 0;
        for (int i = 0; i < sharkTmp.length; i++)
        {
            for (int j = 0; j < sharkTmp[0].length; j++)
            {
                sharkAnimationFrames[index++] = sharkTmp[i][j];
            }
        }
        sharkAnimation = new Animation(0.05f, sharkAnimationFrames);
    }

    public void defineMainPlayer()
    {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32f * MyGdxGame.PPM, 1.5f * 64F * MyGdxGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2Body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        fdef.restitution = 0.05f;
        fdef.density = 1f;

        setBoundary(new Circle(1f, 1f, circleRadius));

        shape.setRadius(circleRadius);

        fdef.shape = shape;
        b2Body.createFixture(fdef);
    }

    public void Update()
    {
        this.boundary.setPosition(this.b2Body.getPosition().x, this.b2Body.getPosition().y);
    }

    public Circle getBoundary()
    {
        return boundary;
    }

    public void setBoundary(Circle boundary)
    {
        this.boundary = boundary;
    }

    public int getHealth()
    {
        return health;
    }

    public void setHealth(int health)
    {
        this.health = health;
    }
}