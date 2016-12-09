package com.mygdx.game.Screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Utilities.Controller;
import com.mygdx.game.Utilities.GameHistory;
import com.mygdx.game.Utilities.HUD;
import com.mygdx.game.Utilities.MapHandler;
import com.mygdx.game.characters.Angler;
import com.mygdx.game.characters.GoldFish;
import com.mygdx.game.characters.Sharky;
import com.mygdx.game.characters.Steak;

import java.util.ArrayList;

public class PlayScreen implements Screen, InputProcessor
{

    public static class GameDescriptor
    {
        public Vector2 mLocation;
        public ArrayList<Angler> anglers;
        public ArrayList<GoldFish> goldfish;
    }

    private static PlayScreen instance;

    public static PlayScreen getInstance()
    {
        if (instance == null)
        {
            return new PlayScreen();
        }
        return instance;
    }

    float xstart, ystart, xend, yend;

    OrthographicCamera gamecam;
    Viewport gameport;
    Texture img;
    World w;
    Sharky sharky;
    Box2DDebugRenderer b2dr;
    MyGdxGame game;
    float x = -4.0f;
    private float speed = 100;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private float stateTime;
    private TextureRegion currentFrame;

    private Animation bgAnimation;
    private TextureRegion[][] bgTmp;
    private TextureRegion[] bgAnimationFrames;

    public final int anglerCount = 40;
    public final int goldfishCount = 4;
    public final int steakCount = 20;

    Array<Angler> anglers = new Array<Angler>();
    Array<GoldFish> goldfish = new Array<GoldFish>();
    Array<Steak> steaks = new Array<Steak>();
    InputMultiplexer iMultiplexer;
    private Music bgmusic;

    Controller controller;
    HUD hud;

    private java.util.Random random;

    public void Init(MyGdxGame game)
    {
        this.game = game;

        random = new java.util.Random();

        bgTmp = new TextureRegion(new Texture("graphics/def.png")).split(192, 320);
        bgAnimationFrames = new TextureRegion[9];

        bgmusic = Gdx.audio.newMusic(Gdx.files.internal("music/background.mp3"));
        bgmusic.setLooping(true);
        bgmusic.play();
        bgmusic.setVolume(0.1f);

        int index = 0;
        for (int i = 0; i < bgTmp.length; i++)
        {
            for (int j = 0; j < bgTmp[0].length; j++)
            {
                bgAnimationFrames[index++] = bgTmp[i][j];
            }
        }
        bgAnimation = new Animation(1 / 6f, bgAnimationFrames);

        img = new Texture("badlogic.jpg");
        gamecam = new OrthographicCamera(MyGdxGame.WIDTH * MyGdxGame.PPM, MyGdxGame.HEIGHT * MyGdxGame.PPM);
        gameport = new FitViewport(MyGdxGame.WIDTH * MyGdxGame.PPM, MyGdxGame.HEIGHT * MyGdxGame.PPM, gamecam);
        gamecam.position.set((MyGdxGame.WIDTH / 2f) * MyGdxGame.PPM, (MyGdxGame.HEIGHT / 2f) * MyGdxGame.PPM, 0f);
        w = new World(new Vector2(0, 0f), true);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Levels/map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, MyGdxGame.PPM);

        new MapHandler(w, map);
        sharky = new Sharky(w, game);
        b2dr = new Box2DDebugRenderer();

        controller = new Controller(game);
        hud = new HUD(game);
        iMultiplexer = new InputMultiplexer();
        iMultiplexer.addProcessor(this);
        iMultiplexer.addProcessor(controller.stage);

        Gdx.input.setInputProcessor(iMultiplexer);

        populate();
    }

    @Override
    public void show()
    {
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        updateMethod();

        stateTime += delta;
        game.batch.setProjectionMatrix(gamecam.combined);

        game.batch.begin();
        // background
        currentFrame = bgAnimation.getKeyFrame(stateTime, true);
        game.batch.draw(currentFrame, gamecam.position.x - (currentFrame.getRegionWidth() * MyGdxGame.PPM),
                gamecam.position.y - (currentFrame.getRegionHeight() * MyGdxGame.PPM), 0, 0, 2, 2, 192f * MyGdxGame.PPM,
                320f * MyGdxGame.PPM, 0);


        // shark
        currentFrame = sharky.getSharkAnimation().getKeyFrame(stateTime, true);
        game.batch.draw(currentFrame,
                (sharky.b2Body.getLinearVelocity().x < 0) ? sharky.b2Body.getPosition().x + 1f
                        : sharky.b2Body.getPosition().x - 1f,
                sharky.b2Body.getPosition().y - 1f, 0, 0, (sharky.b2Body.getLinearVelocity().x < 0) ? -1 : 1, 1,
                32f * MyGdxGame.PPM * 2f, 32f * MyGdxGame.PPM * 2f, 0);

        // goldfish
        for (GoldFish fish : goldfish)
        {
            currentFrame = fish.getGoldfishAnimation().getKeyFrame(stateTime, true);
            game.batch.draw(currentFrame, fish.getPos().x, fish.getPos().y, 0, 0, fish.right ? 1 : -1, -1,
                    32f * MyGdxGame.PPM * 2f, -32f * MyGdxGame.PPM * 2f, 0);
            if (Intersector.overlaps(sharky.getBoundary(), fish.getBorder()))
            {
                System.out.println("goldfish collides");
                System.out.println("goldfish location" + fish.getPos().toString());
                System.out.println("mplayer location" + sharky.b2Body.getPosition().toString());
            }
        }


        // angler fish
        float tempx = 0, tempy = 0;
        for (Angler angler : anglers)
        {
            currentFrame = angler.getAnglerfishAnimation().getKeyFrame(stateTime, true);
            game.batch.draw(currentFrame, angler.getPos().x, angler.getPos().y, 0, 0, angler.right ? -1 : 1, -1,
                    MyGdxGame.PPM * 100f, MyGdxGame.PPM * -102f, 0);

            Circle collider = angler.right ? new Circle(angler.getPos().x - 1.5f, angler.getPos().y + 1f, 1f) : new Circle(angler.getPos().x, angler.getPos().y, 1f);
            if (Intersector.overlaps(sharky.getBoundary(), collider))
            {
                System.out.println("angler collides");
                System.out.println("angler location" + angler.getPos().toString());
                System.out.println("mplayer location" + sharky.b2Body.getPosition().toString());
                currentFrame = sharky.getSharkShockAnimation().getKeyFrame(stateTime);
                game.batch.draw(currentFrame,
                        (sharky.b2Body.getLinearVelocity().x < 0) ? sharky.b2Body.getPosition().x + 1f
                                : sharky.b2Body.getPosition().x - 1f,
                        sharky.b2Body.getPosition().y - 1f, 0, 0, (sharky.b2Body.getLinearVelocity().x < 0) ? -1 : 1, 1,
                        32f * MyGdxGame.PPM * 2f, 32f * MyGdxGame.PPM * 2f, 0);

                float health = (sharky.getHealth() - 1f) / 100f;
                sharky.setHealth(sharky.getHealth() - 1);
                hud.setHealthBar(health);
            }
        }
        game.batch.end();


        for (Steak steak : steaks)
        {
            steak.draw();

            if (Intersector.overlaps(sharky.getBoundary(), steak.getBoundary()))
            {
                float x = xend * 0.8f * (float) Math.random();
                float y = ystart + yend * 0.8f * MyGdxGame.PPM * (float) Math.random();
                steak.location = new Vector2(x, y);
                float newHealth = sharky.getHealth() + 5;
                float health = MathUtils.clamp(newHealth, 0, 100);
                sharky.setHealth((int) (health));
                hud.setHealthBar(health / 100f);
            }
        }


        game.batch.setProjectionMatrix(controller.stage.getCamera().combined);
        hud.draw(delta);
        mapRenderer.render();
//        if (Gdx.app.getType() == ApplicationType.Android)
//        {
            controller.draw();
//        }

    }

    private void populate()
    {
        MapObject object = map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class).first();
        Rectangle rect = ((RectangleMapObject) object).getRectangle();

        xstart = rect.getX() * MyGdxGame.PPM;
        ystart = rect.getY() * MyGdxGame.PPM;
        xend = xstart + (rect.getWidth() * MyGdxGame.PPM);
        yend = ystart + (rect.getHeight() * MyGdxGame.PPM);

        System.out.println(xstart);
        System.out.println(xend);
        System.out.println(ystart);
        System.out.println(yend);

        if (anglers.size < anglerCount)
        {
            for (int i = 0; i < anglerCount; i++)
            {
                float x = xstart + (rect.getWidth() * 0.8f * MyGdxGame.PPM) * (float) Math.random();
                float y = ystart + (rect.getHeight() * 0.8f * MyGdxGame.PPM) * (float) Math.random();

                anglers.add(new Angler(game, w, x, y, xstart, ystart, xend, yend));
            }
        }

        if (goldfish.size < goldfishCount)
        {
            for (int i = 0; i < goldfishCount; i++)
            {
                float x = xstart + (rect.getWidth() * 0.8f * MyGdxGame.PPM) * (float) Math.random();
                float y = ystart + (rect.getHeight() * 0.8f * MyGdxGame.PPM) * (float) Math.random();

                goldfish.add(new GoldFish(game, w, x, y, xstart, ystart, xend, yend));
            }
        }

        if (steaks.size < steakCount)
        {
            for (int i = 0; i < steakCount; i++)
            {
                float x = xstart + (rect.getWidth() * 0.8f * MyGdxGame.PPM) * (float) Math.random();
                float y = ystart + (rect.getHeight() * 0.8f * MyGdxGame.PPM) * (float) Math.random();

                steaks.add(new Steak(new Vector2(x, y), game));
            }
        }
    }

    private void updateMethod()
    {
        if (Gdx.input.isKeyPressed(Keys.Z))
        {
            GameDescriptor gameDescriptor = new GameDescriptor();
            gameDescriptor.mLocation = this.sharky.b2Body.getPosition();
            new GameHistory().save("test", gameDescriptor);
        }
        if (Gdx.input.isKeyPressed(Keys.A))
        {
            sharky.b2Body.applyForce(new Vector2(-speed * 10, 0), sharky.b2Body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Keys.D))
        {
            sharky.b2Body.applyForce(new Vector2(speed * 10, 0), sharky.b2Body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Keys.W))
        {
            sharky.b2Body.applyForce(new Vector2(0, 3 * 10), sharky.b2Body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Keys.S))
        {
            sharky.b2Body.applyForce(new Vector2(0, -3 * 10), sharky.b2Body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Keys.G))
        {
            sharky.b2Body.setLinearVelocity(0, 0);
        }
        if (controller.downPressed)
        {
            sharky.b2Body.applyForce(new Vector2(0, -3 * 10), sharky.b2Body.getWorldCenter(), true);
        }
        if (controller.upPressed)
        {
            sharky.b2Body.applyForce(new Vector2(0, 3 * 10), sharky.b2Body.getWorldCenter(), true);
        }
        if (controller.leftPressed)
        {
            sharky.b2Body.applyForce(new Vector2(-speed * 10, 0), sharky.b2Body.getWorldCenter(), true);
        }
        if (controller.rightPressed)
        {
            sharky.b2Body.applyForce(new Vector2(speed * 10, 0), sharky.b2Body.getWorldCenter(), true);
        }

        float xv = sharky.b2Body.getLinearVelocity().x;
        float yv = sharky.b2Body.getLinearVelocity().y;
        xv = MathUtils.clamp(xv, -5, 5);
        yv = MathUtils.clamp(yv, -5, 5);
        sharky.b2Body.setLinearVelocity(xv, yv);
        w.step(1 / 60f, 6, 2);

        float x = MathUtils.clamp(sharky.b2Body.getPosition().x, 0, 200);
        float y = MathUtils.clamp(sharky.b2Body.getPosition().y, 0, 19);

        sharky.b2Body.setTransform(x, y, 0);

        gamecam.position.x = sharky.b2Body.getPosition().x;

        gamecam.update();
        mapRenderer.setView(gamecam);
        sharky.Update();

        for (Angler angler : anglers)
        {
            angler.move(angler.getXstart() * random.nextFloat(), angler.getXend(), 0.05f, 10f, 15f, 0.1f, true);
        }

        for (GoldFish goldFish : goldfish)
        {
            goldFish.move(goldFish.getXstart() * random.nextFloat(), goldFish.getXend(), 0.05f, 6f, 15f, 0.1f, true);
        }
        if (sharky.getHealth() <= 0)
        {
            game.setScreen(game.gameOverScreen);
        }
    }

    @Override
    public void resize(int width, int height)
    {
        gamecam.position.x = sharky.b2Body.getPosition().x;
        gameport.update(width, height);
        game.batch.setProjectionMatrix(gameport.getCamera().combined);
        controller.resize(width, height);
        hud.resize(width, height);
    }

    @Override
    public void pause()
    {
    }

    @Override
    public void resume()
    {
    }

    @Override
    public void hide()
    {
    }

    @Override
    public void dispose()
    {
        bgmusic.dispose();
    }

    @Override
    public boolean keyDown(int keycode)
    {
        return true;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        return true;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        System.out.println("touchdown");
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;
    }

    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }

    public void restart()
    {
        sharky.setHealth(100);
        hud.setHealthBar(1);
        sharky.b2Body.setTransform(32f * MyGdxGame.PPM, 1.5f * 64F * MyGdxGame.PPM, 0);
        stateTime = 0;
        bgmusic.play();
        Gdx.input.setInputProcessor(iMultiplexer);
    }

}
