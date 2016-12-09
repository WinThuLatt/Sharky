package com.mygdx.game.Utilities;

import com.badlogic.gdx.utils.Json;
import com.mygdx.game.Screens.PlayScreen.GameDescriptor;

public class GameHistory
{
    private String name;
    
    
    
    
    public void save(String name, GameDescriptor descriptor )
    {
	this.name = name;
	Json json = new Json();
	System.out.println(json.toJson(descriptor.mLocation));
    }
}
