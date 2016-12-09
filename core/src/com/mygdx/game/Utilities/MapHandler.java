package com.mygdx.game.Utilities;

import java.util.ArrayList;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;

/**
 * Created by my on 8/18/2016.
 */
public class MapHandler
{
    private ArrayList<Rectangle> boxes = new ArrayList<Rectangle>();
    public ArrayList<Rectangle> getBoxes()
    {
        return boxes;
    }
    public MapHandler(World world, TiledMap map) {
	BodyDef bdef = new BodyDef();
	PolygonShape shape = new PolygonShape();
	FixtureDef fdef = new FixtureDef();
	Body body;

	for (MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class))
	{
	    Rectangle rect = ((RectangleMapObject) object).getRectangle();

	    bdef.type = BodyDef.BodyType.StaticBody;
	    bdef.position.set(rect.getX() * MyGdxGame.PPM + (rect.getWidth() * MyGdxGame.PPM * 0.5f) ,
		    rect.getY() * MyGdxGame.PPM + (rect.getHeight() * 0.5f * MyGdxGame.PPM));

	    body = world.createBody(bdef);
	    shape.setAsBox((rect.getWidth() * MyGdxGame.PPM * 0.5f) , (rect.getHeight()* MyGdxGame.PPM * 0.5f) );
	    fdef.shape = shape;
	    body.createFixture(fdef);
	    boxes.add(rect);
	}
    }
    
    

}
