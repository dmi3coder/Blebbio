package com.dmi3coder.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by dmi3coder on 28/05/16;15:55.
 */
public class Bubble extends Sprite {
    private Vector2 previousPosition;
    private float size = 1f;

    public Bubble(Texture texture){
        super(texture);
        previousPosition = new Vector2(getX(),getY());
    }

    public boolean hasMoved(){
        if(previousPosition.x != getX() || previousPosition.y != getY()){
            previousPosition.x = getX();
            previousPosition.y = getY();
            return true;
        }
        return false;
    }

    public void setSize(float size){
        this.setSize(getWidth()/this.size*size,getHeight()/this.size*size);
        this.size = size;
    }
    public boolean contains(float x, float y) {
        return (x > getX() && y > getY() && x < getX() + getWidth() && y < getY() + getHeight());
    }
    public float getSize(){
        return size;
    }
}
