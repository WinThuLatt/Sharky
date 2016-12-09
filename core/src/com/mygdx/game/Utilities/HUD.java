package com.mygdx.game.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;

public class HUD
{
    MyGdxGame game;
    Viewport viewport;
    OrthographicCamera camera;
    private BitmapFont font;
    private float timepassed = 0;
    public TextureAtlas.AtlasRegion border;
    public TextureAtlas.AtlasRegion health;

    private float healthBar = 1f;

    public void setHealthBar(float percentage)
    {
        this.healthBar = percentage;
    }


    public HUD(MyGdxGame game)
    {

        camera = new OrthographicCamera(MyGdxGame.WIDTH * MyGdxGame.PPM, MyGdxGame.HEIGHT * MyGdxGame.PPM);
        viewport = new FitViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, camera);
        this.game = game;
        border = game.atlas.findRegion("border");
        health = game.atlas.findRegion("health");


        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Black.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 24;
        font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose();
    }

    public void draw(float delta)
    {
        GlyphLayout layout = new GlyphLayout(font, "Time : " + String.valueOf((int) timepassed));

        float fontX = (layout.width / 2f);
        float fontY = (camera.viewportHeight * 0.45f);
        this.game.batch.setProjectionMatrix(this.camera.combined);

        float leftTopX = -camera.viewportWidth / 2.2f;
        float leftTopY = camera.viewportHeight * 0.4f;

        float healthleftTopX = -(camera.viewportWidth / 2.2f) + 1f;
        float healthleftTopY = (camera.viewportHeight * 0.4f) + 1f;

        this.game.batch.begin();
        this.font.setColor(Color.GREEN);
        this.font.draw(this.game.batch, "Time : " + String.valueOf((int) timepassed), fontX, fontY);

        game.batch.draw(border, leftTopX, leftTopY, 0, 0, border.getRegionWidth(), border.getRegionHeight(), 1f, 1f, 0f);
        game.batch.draw(health, healthleftTopX, healthleftTopY, 0, 0, health.getRegionWidth(), health.getRegionHeight(), healthBar, 1f, 0f);


        this.game.batch.end();
        timepassed += delta;
    }

    public void resize(int width, int height)
    {
        viewport.update(width, height);
    }

}
