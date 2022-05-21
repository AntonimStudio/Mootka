package xyz.gdxshooter.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Character {


    protected Vector2 position;
    protected Vector2 velocity;
    protected Vector2 accel;
    protected float maxSpeedX;
    protected float jumpVelocityY;
    protected Rectangle hitbox;
    protected boolean isCollisionDown;
    protected boolean isCollisionUp;
    protected Direction direction;
    protected boolean isControlled;

    protected int hp;
    protected int maxHp;
    public boolean isDead;

    protected Texture texture;
    protected Animation<Texture> movingAnim;
    protected Texture textureDead;
    protected Texture currentTexture;

    private Character(Vector2 position, float width, float height, int maxHp) {
        this.position = position;
        this.velocity = new Vector2(0, 0);
        this.accel = new Vector2(0, 0);
        this.maxSpeedX = 200;
        this.jumpVelocityY = 1200;
        this.isCollisionDown = false;
        this.isCollisionUp = false;

        this.hitbox = new Rectangle(position.x, position.y, width, height);
        this.direction = Direction.RIGHT;

        this.maxHp = maxHp;
        this.hp = maxHp;
        this.isDead = false;
        this.isControlled = false;
    }

    public Character(Vector2 position, float width, float height, int maxHp,
                     Texture texture, Texture textureDead) {
        this(position, width, height, maxHp);
        this.texture = texture;
        this.textureDead = textureDead;
    }

    public Character(Vector2 position, float width, float height, int maxHp,
                     Texture texture, Texture textureDead, Animation<Texture> movingAnim) {
        this(position, width, height, maxHp, texture, textureDead);
        this.movingAnim = movingAnim;
    }

    public void updatePosition(float delta) {
        velocity.add(accel.x * delta, accel.y * delta);
        position.add(velocity.x * delta, velocity.y * delta);
        hitbox.x = position.x;
        hitbox.y = position.y;
    }

    public void draw(SpriteBatch batch, float runTime) {
        if (isDead())
            currentTexture = textureDead;
        else {
            if (!isControlled)
                currentTexture = texture;
            else if (movingAnim != null)
                currentTexture = movingAnim.getKeyFrame(runTime);
            else
                currentTexture = texture;
        }

        batch.draw(
                currentTexture,
                position.x, position.y,
                Math.round(position.x), Math.round(position.y),
                currentTexture.getWidth(),
                currentTexture.getHeight(),
                1, 1, 0,
                0, 0,
                currentTexture.getWidth(),
                currentTexture.getHeight(),
                direction == Direction.LEFT, false);
    }

    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
    }

    public float getPositionX() {
        return position.x;
    }

    public void setPositionX(float x) {
        position.x = x;
    }

    public float getPositionY() {
        return position.y;
    }

    public void setPositionY(float y) {
        position.y = y;
    }

    public void setVelocity(float x, float y) {
        velocity.x = x;
        velocity.y = y;
    }

    public float getVelocityX() {
        return velocity.x;
    }

    public void setVelocityX(float x) {
        velocity.x = x;
    }

    public float getVelocityY() {
        return velocity.y;
    }

    public void setVelocityY(float y) {
        velocity.y = y;
    }

    public float getAccelX() {
        return accel.x;
    }

    public float getAccelY() {
        return accel.y;
    }

    public void setAccelX(float accelX) {
        accel.x = accelX;
    }

    public void setAccelY(float accelY) {
        accel.y = accelY;
    }

    /**
     * Returns hitbox of a player. The object must not be changed.
     * @return Hitbox of a player
     */
    public Rectangle getHitbox() {
        return hitbox;
    }

    public float getMaxVelocityX() {
        return maxSpeedX;
    }

    public boolean isCollisionDown() {
        return isCollisionDown;
    }

    public void setCollisionDown(boolean isOnFloor) {
        isCollisionDown = isOnFloor;
    }

    public boolean isCollisionUp() {
        return isCollisionUp;
    }

    public void setCollisionUp(boolean collisionUp) {
        isCollisionUp = collisionUp;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isControlled() {
        return isControlled;
    }

    public void setControlled(boolean controlled) {
        isControlled = controlled;
    }

    public boolean isDead() {
        return isDead;
    }

    public int getHp() {
        return hp;
    }


    public void takeDamage(float delta, int hits) {
        if (hp - hits <= 0) {
            hp = 0;
            isDead = true;
            setControlled(false);
        }
        else {
            hp -= hits;
        }
    }

    public void jump() {
        if (isCollisionDown()) {
            setVelocityY(this.jumpVelocityY);
            setCollisionDown(false);
        }
    }

}
