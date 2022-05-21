package xyz.gdxshooter.Characters;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

public class Player extends Character {
    private Texture bulletTexture;

    /** Cooldown of the shoot */
    private float shootCD; // Fire sound длительность 0.57 секунды
    private float shootRemainCD;

    /** Amount of time when character is immune after taking damage */
    private float takeDamageCD;
    private float takeDamageRemainCD;

    private Music hurtSound;

    public Player(Vector2 position, float width, float height, int maxHp,
                  Texture texture, Texture playerTextureDead, Animation<Texture> movingAnim, Texture bulletTexture,
                  Music playerHurt) {
        super(position, width, height, maxHp, texture, playerTextureDead, movingAnim);
        this.maxSpeedX = 170;
        this.jumpVelocityY = 450;
        this.bulletTexture = bulletTexture;

        shootCD = 0.5f;
        shootRemainCD = 0f;

        takeDamageCD = 0.7f;
        takeDamageRemainCD = 0f;
        this.hurtSound = playerHurt;
    }

    public void controlWithJoystick(float knobPercentX, float knobPercentY) {
        isControlled = Math.abs(knobPercentX) > 0;

        setVelocityX(getMaxVelocityX() * knobPercentX);
        if (knobPercentX > 0)
            this.direction = Direction.RIGHT;
        else
            this.direction = Direction.LEFT;
    }

    public Bullet shoot(float delta) {
        if (shootRemainCD <= 0) {
            Vector2 bulletPos = new Vector2(getPositionX() + getHitbox().width -2,
                    getPositionY() + getHitbox().height / 2f - 4);
            shootRemainCD = shootCD;
            return new Bullet(bulletPos, 7, 4, 1, direction, bulletTexture);
        }
        else {
            shootRemainCD -= delta;
            return null;
        }
    }

    public void takeDamage(float delta, int hits) {
        if (takeDamageRemainCD > 0) {
            takeDamageRemainCD -= delta;
        }
        else {
            super.takeDamage(delta, hits);
            if (hits == 1) {
                hurtSound.setVolume(0.4f);
                this.setVelocityY(600);
            }
            else if (hits == 2) {
                hurtSound.setVolume(0.8f);
                this.setVelocityY(1000);
            }
            hurtSound.play();
            takeDamageRemainCD = takeDamageCD;
        }

    }

}
