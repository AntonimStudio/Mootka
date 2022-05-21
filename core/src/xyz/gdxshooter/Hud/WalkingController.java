package xyz.gdxshooter.Hud;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.viewport.Viewport;

public class WalkingController extends Touchpad
{
    // Размеры участков на атласе Контроллера
    public static final int frameWidth = 242;
    public static final int frameHeight = 242;
    public static final int circleWidth = 70;
    public static final int circleHeight = 70;

    private Texture texture;
    private TextureRegion frameActive;
    private TextureRegion circleActive;
    private TextureRegion frameInactive;
    private TextureRegion circleInactive;
    private Skin skin;

    private Viewport viewport;

    public WalkingController(Viewport viewport, Skin skin) {
        super(10, new TouchpadStyle(skin.getDrawable("frameActive"), skin.getDrawable("circleActive")));
        this.skin = skin;
        this.viewport = viewport;

        makeInactive();
        resetPosition();
    }

    public void makeActive()
    {
        getStyle().background = this.skin.getDrawable("frameActive");;
        getStyle().knob = this.skin.getDrawable("circleActive");
    }

    public void makeInactive()
    {
        getStyle().background = this.skin.getDrawable("frameInactive");;
        getStyle().knob = this.skin.getDrawable("circleInactive");
    }

    public void setCenterPosition(float x, float y)
    {
        this.setPosition(x - getWidth() / 2,y - getHeight() / 2);
    }

    public void resetPosition()
    {
        setBounds(0, 0, WalkingController.frameWidth, WalkingController.frameHeight);
        this.setSize(120,120);
    }

}
