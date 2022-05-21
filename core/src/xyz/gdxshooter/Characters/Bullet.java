package xyz.gdxshooter.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends Character {
    private float airResistAccel = 300;

    public Bullet(Vector2 position, float width, float height, int maxHp, Direction direction, Texture texture) {
        super(position, width, height, maxHp, texture, texture);
        this.maxSpeedX = 2000;

        this.direction = direction;
        if (direction == Direction.RIGHT) {
            this.velocity = new Vector2(maxSpeedX, 0);
            this.accel = new Vector2(-airResistAccel, 0);
        } else {
            this.velocity = new Vector2(-maxSpeedX, 0);
            this.accel = new Vector2(airResistAccel, 0);
        }
    }
}
