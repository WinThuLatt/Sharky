package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.Screens.GameOver;
import com.mygdx.game.Screens.LoadingScreen;
import com.mygdx.game.Screens.PlayScreen;

public class MyGdxGame extends Game
{
    public SpriteBatch batch;

    public static int WIDTH = 384;
    public static int HEIGHT = 640;
    public static float PPM = 1 / 32f;

    public AssetManager manager = new AssetManager();
    public TextureAtlas atlas;

    public PlayScreen playScreen;
    public GameOver gameOverScreen;

    @Override
    public void create()
    {
	batch = new SpriteBatch();
	this.setScreen(new LoadingScreen(this));
    }

    @Override
    public void render()
    {
	super.render();
    }

    @Override
    public void dispose()
    {
	batch.dispose();
        this.gameOverScreen.dispose();

    }

}
