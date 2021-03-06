package com.example.myapplication;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.modifier.MoveYModifier;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class EnemySpawn extends Entity {

    public static EnemySpawn instance;
    public Camera mCamera;
    public int width;
    private LinkedList<Enemy> enemies;

    public EnemySpawn() {
        enemies = new LinkedList<>();
        instance = this;
    }

    public static EnemySpawn getSharedInstance() {
        return instance;
    }

    public static Iterator getIterator() {
        return instance.enemies.iterator();
    }

    public static void purgeAndSpawn() {
        instance.purge();
        instance.spawn();
    }

    public void spawn() {
        mCamera = MainActivity.getSharedInstance().mCamera;
        width = (int) mCamera.getWidth();

        TimerHandler spriteMoveHandler = new TimerHandler(1.5f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                Random rand = new Random();
                Enemy e = EnemyPool.sharedEnemyPool().obtainPoolItem();
                int low = 290;
                int high = (int) mCamera.getWidth() - 290;
                int result = rand.nextInt(high - low) + low;
                e.sprite.setPosition(result, -20);
                e.sprite.setVisible(true);
                attachChild(e.sprite);
                enemies.add(e);
                setVisible(true);
                MoveYModifier moveDown = new MoveYModifier(5, mCamera.getHeight(), -100);
                e.sprite.registerEntityModifier(moveDown);
            }
        });
        registerUpdateHandler(spriteMoveHandler);
    }

    public void purge() {
        detachChildren();
        for (Enemy e : enemies) {
            EnemyPool.sharedEnemyPool().recyclePoolItem(e);
        }
        enemies.clear();
    }

    @Override
    public void onDetached() {
        purge();
        clearUpdateHandlers();
        super.onDetached();
    }

}
