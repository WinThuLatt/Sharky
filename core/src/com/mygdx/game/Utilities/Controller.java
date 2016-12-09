package com.mygdx.game.Utilities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;

public class Controller
{
    public boolean isUpPressed()
    {
        return upPressed;
    }

    public void setUpPressed(boolean upPressed)
    {
        this.upPressed = upPressed;
    }

    public boolean isDownPressed()
    {
        return downPressed;
    }

    public void setDownPressed(boolean downPressed)
    {
        this.downPressed = downPressed;
    }

    public boolean isLeftPressed()
    {
        return leftPressed;
    }

    public void setLeftPressed(boolean leftPressed)
    {
        this.leftPressed = leftPressed;
    }

    public boolean isRightPressed()
    {
        return rightPressed;
    }

    public void setRightPressed(boolean rightPressed)
    {
        this.rightPressed = rightPressed;
    }

    public Viewport viewport;
    public Stage stage;
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    OrthographicCamera camera;

    public Controller(MyGdxGame game) {
	camera = new OrthographicCamera();
	viewport = new FitViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, camera);
	stage = new Stage(viewport, game.batch);	
	
	Table table = new Table();
	table.setFillParent(true);
	table.right().bottom();
	
	Image upImage = new Image(new Texture("graphics/up.png"));
	upImage.setSize(40, 40);
	upImage.addListener(new InputListener(){

	    @Override
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	    {
		upPressed = true;
		return true;
	    }

	    @Override
	    public void touchUp(InputEvent event, float x, float y, int pointer, int button)
	    {
		upPressed = false;
	    }});
	
	
	Image downImage = new Image(new Texture("graphics/down.png"));
	downImage.setSize(40, 40);
	downImage.addListener(new InputListener(){

	    @Override
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	    {
		downPressed = true;
		return true;
	    }

	    @Override
	    public void touchUp(InputEvent event, float x, float y, int pointer, int button)
	    {
		downPressed = false;
	    }});
	
	
	Image leftImage = new Image(new Texture("graphics/left.png"));
	leftImage.setSize(40, 40);
	leftImage.addListener(new InputListener(){

	    @Override
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	    {
		leftPressed = true;
		return true;
	    }

	    @Override
	    public void touchUp(InputEvent event, float x, float y, int pointer, int button)
	    {
		leftPressed = false;
	    }});
	
	
	Image rightImage = new Image(new Texture("graphics/right.png"));
	rightImage.setSize(40, 40);
	rightImage.addListener(new InputListener(){

	    @Override
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
	    {
		rightPressed = true;
		return true;
	    }

	    @Override
	    public void touchUp(InputEvent event, float x, float y, int pointer, int button)
	    {
		rightPressed = false;
	    }});

	
	table.add();
	table.add(upImage).size(upImage.getWidth(), upImage.getHeight());
	table.add();
	
	table.row().pad(5);
	table.add(leftImage).size(leftImage.getWidth(),leftImage.getHeight());
	table.add();
	table.add(rightImage).size(rightImage.getWidth(), rightImage.getHeight());
	
	table.row().padBottom(5);
	table.add();
	table.add(downImage).size(downImage.getWidth(), downImage.getHeight());
	table.add();
	
	stage.addActor(table);
    }
    
    public void draw()
    {
	stage.draw();
    }
    
    public void resize(int width, int height)
    {
	viewport.update(width, height);
    }
}
