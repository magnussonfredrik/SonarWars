package com.example.myapplication;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.primitive.Rectangle;

public class Ship {
    public static Ship instance;
    public Rectangle sprite;
    Camera mCamera;
    private boolean movable;

    private Ship() {
        sprite = new Rectangle(0, 0, 280, 120, MainActivity.getSharedInstance()
                .getVertexBufferObjectManager());
        mCamera = MainActivity.getSharedInstance().mCamera;
        sprite.setPosition(mCamera.getCenterX(), 20);
        movable = true;
    }

    public static Ship getSharedInstance() {
        if (instance == null)
            instance = new Ship();
        return instance;
    }

    public void moveShip(float accelerometerSpeedX) {
        if (!movable)
            return;

        if (accelerometerSpeedX != 0) {
            int lL = 290;
            int rL = (int) mCamera.getWidth() - 290;
            float newX;

            // Calculate New X,Y Coordinates within Limits
            if (sprite.getX() >= lL) {
                newX = sprite.getX() + accelerometerSpeedX;
            } else {
                newX = lL;
            }
            if (newX <= rL) {
                newX = sprite.getX() + accelerometerSpeedX;
            } else {
                newX = rL;
            }

            // Double Check That New X,Y Coordinates are within Limits
            if (newX < lL)
                newX = lL;
            else if (newX > rL)
                newX = rL;
            sprite.setPosition(newX, sprite.getY());
        }
    }


    public void shoot() {
        if (!movable) {
            return;
        }
        GameScene scene = (GameScene) MainActivity.getSharedInstance().mCurrentScene;
        Bullet b = BulletPool.sharedBulletPool().obtainPoolItem();
        b.sprite.setPosition(sprite.getX(), sprite.getY() + 50);
        MoveYModifier mod = new MoveYModifier(1.5f, b.sprite.getY(),
                mCamera.getHeight() + 10);
        b.sprite.setVisible(true);
        b.sprite.detachSelf();
        scene.attachChild(b.sprite);
        scene.bulletList.add(b);
        b.sprite.registerEntityModifier(mod);
        scene.bulletCount++;
    }

    public void restart() {
        movable = false;
        Camera mCamera = MainActivity.getSharedInstance().mCamera;
        MoveXModifier mod = new MoveXModifier(0.2f, sprite.getX(),
                mCamera.getCenterX()) {
            @Override
            protected void onModifierFinished(IEntity pItem) {
                super.onModifierFinished(pItem);
                movable = true;
            }
        };
        sprite.registerEntityModifier(mod);
    }
}
