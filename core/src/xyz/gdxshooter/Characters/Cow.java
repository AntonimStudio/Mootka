package xyz.gdxshooter.Characters;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public final class Cow extends Character {
    private Array<Float> routePointsX;
    private int currentPointIdx;
    private CowType type;
    private Music hurtSound;

    public Cow(Vector2 position, float width, float height, float maxSpeedX, int maxHp, CowType cowType,
               Texture texture, Texture textureDead, Animation<Texture> movingAnim, Array<Float> routePointsX,
               Music cowHurt) {
        super(position, width, height, maxHp, texture, textureDead, movingAnim);

        if (routePointsX.size < 2)
            throw new IllegalArgumentException("Size of routePointsX must be greater than 2");

        this.maxSpeedX = maxSpeedX;

        this.routePointsX = routePointsX;
        currentPointIdx = 0;

        if (this.getPositionX() < routePointsX.get(0))
            direction = Direction.RIGHT;
        else
            direction = Direction.LEFT;

        this.type = cowType;
        this.hurtSound = cowHurt;
    }

    public void control() {
        setControlled(true);
        float targetX = routePointsX.get(currentPointIdx);

        if (this.getPositionX() < targetX)
            direction = Direction.RIGHT;
        else
            direction = Direction.LEFT;

        if (Math.abs(getPositionX() - targetX) < 10f) {
            if (currentPointIdx == routePointsX.size - 1)
                currentPointIdx = 0;
            else
                currentPointIdx++;
        }
        else if (getPositionX() < targetX) {
            setVelocityX(maxSpeedX);
        }
        else {
            setVelocityX(-maxSpeedX);
        }
    }

    @Override
    public void takeDamage(float delta, int hits) {
        super.takeDamage(delta, hits);
        this.setVelocityY(300);
        hurtSound.setVolume(0.2f);
        hurtSound.play();
    }

    public CowType getType() {
        return this.type;
    }

}
