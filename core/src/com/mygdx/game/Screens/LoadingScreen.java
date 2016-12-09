package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;

public class LoadingScreen implements Screen
{
    TextureAtlas atlas;
    MyGdxGame game;

    private Animation loadingAnimation;
    private TextureRegion[][] loadingTmp;
    private TextureRegion[] loadingAnimationFrames;
    private TextureRegion currentFrame;
    private float time = 0f;
    private BitmapFont font;
    private float fontX = 0, fontY = 0;
    private String message = "Sharky Sharky";

    private Animation sharkAnimation;
    private TextureRegion[][] sharkTmp;
    private TextureRegion[] sharkAnimationFrames;

    private OrthographicCamera camera;

    private Viewport viewport;

    public LoadingScreen(MyGdxGame game)
    {
        camera = new OrthographicCamera(MyGdxGame.WIDTH * MyGdxGame.PPM, MyGdxGame.HEIGHT * MyGdxGame.PPM);
        viewport = new FitViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, camera);

        this.game = game;
        game.manager.load("graphics/pack.atlas", TextureAtlas.class);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Black.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 12;
        font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose();

        loadingTmp = TextureRegion.split(new Texture(Gdx.files.internal("graphics/loadingCircle.png")), 100, 100);
        loadingAnimationFrames = new TextureRegion[5 * 6];
        int index = 0;
        for (int i = 0; i < 6; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                loadingAnimationFrames[index++] = loadingTmp[i][j];
            }
        }

        loadingAnimation = new Animation(0.025f, loadingAnimationFrames);

        sharkTmp = TextureRegion.split(new Texture(Gdx.files.internal("graphics/sharkElectric.png")), 144, 88);
        sharkAnimationFrames = new TextureRegion[2];
        index = 0;
        for (int i = 0; i < 1; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                sharkAnimationFrames[index++] = sharkTmp[i][j];
            }
        }

        sharkAnimation = new Animation(0.25f, sharkAnimationFrames);

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

        if (this.game.manager.update() && this.time > 2f)
        {
            game.atlas = game.manager.get("graphics/pack.atlas");
            game.playScreen = PlayScreen.getInstance();
            game.gameOverScreen = GameOver.getInstance();
            game.playScreen.Init(game);
            game.gameOverScreen.Init(game);
            game.setScreen(game.playScreen);
        }
        this.time += delta;

        this.game.batch.setProjectionMatrix(this.camera.combined);

        this.game.batch.begin();

        GlyphLayout layout = new GlyphLayout(font, message);

        fontX = -(layout.width / 2f);
        fontY = -(camera.viewportHeight * 0.2f);
        // circle
        currentFrame = this.loadingAnimation.getKeyFrame(this.time, true);
        this.game.batch.draw(currentFrame,
                camera.viewportWidth * MyGdxGame.PPM / 2f - currentFrame.getRegionWidth() / 2f,
                camera.viewportHeight * MyGdxGame.PPM / 2f - currentFrame.getRegionHeight() / 2f);

        // shark
        currentFrame = this.sharkAnimation.getKeyFrame(this.time, true);
        this.game.batch.draw(currentFrame,
                camera.viewportWidth * MyGdxGame.PPM / 2f - currentFrame.getRegionWidth() / 2f,
                camera.viewportHeight * MyGdxGame.PPM / 2f + currentFrame.getRegionHeight());

        this.font.setColor(Color.WHITE);
        this.font.draw(this.game.batch, message, fontX, fontY);

        this.game.batch.end();
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
        game.dispose();
    }

}
