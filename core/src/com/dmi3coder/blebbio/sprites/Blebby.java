package com.dmi3coder.blebbio.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


/**
 * Created by dmi3coder on 28/05/16;22:15.
 */
public class Blebby extends Sprite {
    private boolean visible = true;
    private float capacity = 0.01f;

    public Blebby(Texture texture){
        super(texture);
    }

    public void setVisible(boolean b){
        visible = b;
    }

    public boolean isVisibile(){
        return visible;
    }


}
