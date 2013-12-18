package 3dsim;

import java.awt.Color;
import java.awt.Graphics;
import javax.vecmath.Vector3d;

public abstract class BaseObject {
    // 3Dのベクトルクラスを使用して位置を設定
    protected Vector3d p;

    // このオブジェクトにとっての基本の座標の原点
    protected Vector3d[] base;

    // 色
    protected Color c;

    // デフォルトコンストラクタ
    public BaseObject() {
        this.p = new Vector3d();
        this.c = Color.BLACK;

        this.base = new Vector3d[3];
        this.base[0] = new Vector3d(1, 0, 0);
        this.base[1] = new Vector3d(0, 1, 0);
        this.base[2] = new Vector3d(0, 0, 1);
    }

    public BaseObject(Vector3d p) {
        this.p = p;
        this.c = Color.BLACK;

        this.base = new Vector3d[3];
        this.base[0] = new Vector3d(1, 0, 0);
        this.base[1] = new Vector3d(0, 1, 0);
        this.base[2] = new Vector3d(0, 0, 1);
    }

    // 座標を使用コンストラクタ
    public BaseObject(double x, double y, double z) {
        this.p = new Vector3d(x, y, z);
        this.c = Color.BLACK;

        this.base = new Vector3d[3];
        this.base[0] = new Vector3d(1, 0, 0);
        this.base[1] = new Vector3d(0, 1, 0);
        this.base[2] = new Vector3d(0, 0, 1);
    }

    // 位置のゲッター
    public Vector3d getPosition() {
        return this.p;
    }

    // 色のゲッター
    public Color getColor() {
        return this.c;
    }

    // ベクトルを使用して位置のセッター
    public BaseObject setPosition(Vector3d p) {
        this.p.set(p);
        return this;
    }

    // 座標を使用して位置のセッター
    public BaseObject setPosition(double x, double y, double z) {
        this.p.set(x, y, z);
        return this;
    }

    // オブジェクトをベクトルに通じて移動する
    public BaseObject translate(Vector3d v) {
        this.p.add(v);
        return this;
    }

    // オブジェクトをベクトルの座標に通じて移動する
    public BaseObject translate(double x, double y, double z) {
        this.p.add(new Vector3d(x, y, z));
        return this;
    }

    // オブジェクトを描くけど
    // 図形がないのでアブストラクト
    abstract void draw(World w, Graphics g);
}
