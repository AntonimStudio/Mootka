package xyz.gdxshooter;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import xyz.gdxshooter.Characters.Bullet;
import xyz.gdxshooter.Characters.Character;
import xyz.gdxshooter.Characters.Cow;
import xyz.gdxshooter.Characters.Player;
import xyz.gdxshooter.GameMain;

public class Level {

    private int width;
    private int height;
    private int worldGravity;
    private int levelIdx;
    private boolean completed;

    private Array<Rectangle> rectObjects;

    public Level(TiledMap map, int levelIdx)
    {
        MapObjects mapObjects = map.getLayers().get("ObjectLayer").getObjects();
        this.rectObjects = new Array<>();

        for (MapObject mapObject : mapObjects) {
            if (mapObject instanceof RectangleMapObject) {
                Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
                rectObjects.add(rectangle);
            }
        }

        this.height = GameMain.SCREEN_HEIGHT;
        this.worldGravity = 1500;
        this.width = (int) (rectObjects.get(0).width - 14);
        this.levelIdx = levelIdx;
        completed = false;
    }

    public int getWidth() {
        return width;
    }

    public float getSpawnY() {
        return rectObjects.get(0).y + rectObjects.get(0).height;
    }

    public int getWorldGravity() {
        return worldGravity;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void updateCollisions(float delta, Character character, boolean allCowsKilled) {
        character.setAccelY(-worldGravity);
        if (character.getPositionX() < 0)
            character.setPositionX(0);
        if (character instanceof Player && (levelIdx == 5 && character.getPositionX() > getWidth() - 100
                                            || character.getPositionX() > getWidth()))
        {
            if (allCowsKilled) {
                completed = true;
                character.setVelocityX(0);
                character.setControlled(false);
            }
            else
                character.setPositionX(getWidth() - character.getHitbox().width);
        }

        // обработка коллизий
        for (Rectangle rect : rectObjects) {
            // мёртвые коровы и игрок не взаимодействуют с уровнем (падают)
            if ((character instanceof Cow || character instanceof Player) && character.isDead)
                break;

            if (Intersector.overlaps(character.getHitbox(), rect)) {

                // столкновение с левой границей прямоугольника
                if ((character instanceof Player
                        && character.getPositionX() + character.getHitbox().width > rect.x
                        && character.getPositionX() < rect.x
                        && character.getPositionY() < rect.y + rect.height
                        && character.getPositionY() + character.getHitbox().height > rect.y
                        && character.getVelocityX() > 0)
                  || (character instanceof Bullet
                        && character.getPositionX() + character.getHitbox().width > rect.x
                        && character.getVelocityX() > 0
                        && !character.isDead()
                  )
                )
                {
                    character.setPositionX(rect.x - character.getHitbox().width);
                    if (character.getVelocityX() > 0)
                        character.setVelocityX(0);

                    if (character instanceof Bullet)
                        System.out.println("Bullet collided with LEFT border");
                }

                // столкновение с правой границей прямоугольника
                else if ((character instanceof Player
                        && character.getPositionX() < rect.x + rect.width
                        && character.getPositionX() + character.getHitbox().width > rect.x + rect.width
                        && character.getPositionY() < rect.y + rect.height
                        && character.getPositionY() + character.getHitbox().height > rect.y
                        && character.getVelocityX() < 0)
                    || (character instanceof Bullet
                        && character.getPositionX() < rect.x + rect.width
                        && character.getVelocityX() < 0
                        && !character.isDead()
                    )
                )
                {
                    character.setPositionX(rect.x + rect.width);
                    if (character.getVelocityX() < 0)
                        character.setVelocityX(0);

                    if (character instanceof Bullet) {
                        System.out.println("Bullet collided with RIGHT border");
                        character.setVelocityX(0);
                        character.setPositionX(rect.x + rect.width + 1);
                    }
                }

                // столкновение с верхней границей прямоугольника
                else if (
                        character.getPositionY() < rect.y + rect.height
                        && character.getPositionY() + character.getHitbox().height > rect.y + rect.height)
                {
                    character.setPositionY(rect.y + rect.height);
                    character.setVelocityY(0);
                    character.setCollisionDown(true);
                    if (character instanceof Bullet) {
                        character.setAccelX(0);
                        character.setVelocity(0, 0);
                    }

                    if (character instanceof Bullet && !character.isDead)
                        System.out.println("Bullet collided with TOP border");
                }

                // столкновение с нижней границей прямоугольника
                else if (!(character instanceof Bullet)
                        && character.getPositionY() + character.getHitbox().height > rect.y
                        && character.getPositionY() < rect.y
                        && character.getPositionY() + character.getHitbox().height - rect.y < character.getHitbox().height * 0.4)
                {
                    character.setPositionY(rect.y + rect.height + 1);
                    character.setVelocityY(0);
                    if (character instanceof Player)
                        character.setVelocityY(character.getJumpVelocityY());
                    else
                        // Убрать этот else, если надо, чтобы нижняя граница блока не подбрасывала коров

                        character.setVelocityY(300);

                }

                if (character instanceof Bullet && !character.isDead())
                    character.takeDamage(delta, 1);
            }
        }
    }
}

