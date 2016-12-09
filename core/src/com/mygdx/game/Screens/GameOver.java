package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;

/**
 * Created by my on 10/18/2016.
 */
public class GameOver implements Screen
{

    class TextureButton extends Actor
    {

        TextureAtlas.AtlasRegion texture;
        float x = 0, y = 0;

        public TextureButton(TextureAtlas.AtlasRegion texture, float x, float y)
        {
            this.texture = texture;
            this.x = x;
            this.y = y;
            setBounds(x, y, texture.getRegionWidth(), texture.getRegionHeight());
        }


        @Override
        public void draw(Batch batch, float parentAlpha)
        {
            batch.draw(texture, x, y);
        }
    }

    MyGdxGame game;

    private OrthographicCamera camera;
    private Stage stage;
    private boolean restart = false;

    private Viewport viewport;

    private static GameOver instance;

    public static GameOver getInstance()
    {
        if (instance == null)
        {
            return new GameOver();
        }
        return instance;
    }

    public void Init(final MyGdxGame game)
    {
        camera = new OrthographicCamera(MyGdxGame.WIDTH * MyGdxGame.PPM, MyGdxGame.HEIGHT * MyGdxGame.PPM);
        viewport = new FitViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, camera);

        this.game = game;

        stage = new Stage(viewport);

        TextureButton restartButton = new TextureButton(game.atlas.findRegion("restart"), (MyGdxGame.WIDTH / 2f) - (game.atlas.findRegion("restart").getRegionWidth() / 2f), MyGdxGame.HEIGHT * 0.5f);

        restartButton.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                restart = true;
                return true;
            }
        });



        TextureButton quitButton = new TextureButton(game.atlas.findRegion("quit"), (MyGdxGame.WIDTH / 2f) - (game.atlas.findRegion("quit").getRegionWidth() / 2f), MyGdxGame.HEIGHT * 0.2f);

        quitButton.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                Gdx.app.exit();
                return true;
            }
        });


        stage.addActor(quitButton);
        stage.addActor(restartButton);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show()
    {
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(.1f, .2f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
        if(restart)
        {
            restart = false;
            game.playScreen.restart();
            game.setScreen(game.playScreen);
        }
    }

    @Override
    public void resize(int width, int height)
    {
        viewport.update(width, height);
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

    }
}
