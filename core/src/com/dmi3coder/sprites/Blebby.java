package com.dmi3coder.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


/**
 * Created by dmi3coder on 28/05/16;22:15.
 */
public class Blebby extends Sprite {

    public Blebby(Texture texture){
        super(texture);
        setColor(new Color((int)(Math.random() * 0x1000000)));
    }

}
