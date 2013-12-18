package sim3d;

import java.awt.Color;
import java.awt.Graphics;
import javax.vecmath.Vector3d;

public abstract class BaseObject {
    // 3Dのベクトルクラスを使用して位置を設定
    protected Vector3d p;

    // このオブジェクトにとっての自分のXYZ軸
    protected Vector3d[] axis;

    // 色
    protected Color c;

    // デフォルトコンストラクタ
    public BaseObject() {
        this.p = new Vector3d();
        this.c = Color.BLACK;

        this.axis = new Vector3d[3];
        this.axis[0] = new Vector3d(1, 0, 0);
        this.axis[1] = new Vector3d(0, 1, 0);
        this.axis[2] = new Vector3d(0, 0, 1);
    }

    public BaseObject(Vector3d p) {
        this.p = p;
        this.c = Color.BLACK;

        this.axis = new Vector3d[3];
        this.axis[0] = new Vector3d(1, 0, 0);
        this.axis[1] = new Vector3d(0, 1, 0);
        this.axis[2] = new Vector3d(0, 0, 1);
    }

    // 座標を使用コンストラクタ
    public BaseObject(double x, double y, double z) {
        this.p = new Vector3d(x, y, z);
        this.c = Color.BLACK;

        this.axis = new Vector3d[3];
        this.axis[0] = new Vector3d(1, 0, 0);
        this.axis[1] = new Vector3d(0, 1, 0);
        this.axis[2] = new Vector3d(0, 0, 1);
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

    // オブジェクトをベクトルに通じて移動させる
    public BaseObject translate(Vector3d v) {
        this.p.add(v);
        return this;
    }

    // オブジェクトをベクトルの座標に通じて移動させる
    public BaseObject translate(double x, double y, double z) {
        this.p.add(new Vector3d(x, y, z));
        return this;
    }

    // XYZ軸をomegaという回転ベクトルによって回転させる
    public BaseObject rotate(Vector3d omega) {
        Vector3d delta = new Vector3d();

        // X軸を回転させる
        delta.cross(this.axis[0], omega);
        this.axis[0].add(delta);
        this.axis[0].normalize();

        // Y軸を回転させる
        delta.cross(this.axis[1], omega);
        this.axis[1].add(delta);
        this.axis[1].normalize();

        // Z軸は新しいY軸とX軸のクロス積から計算する
        this.axis[2].cross(this.axis[1], this.axis[0]);

        // X軸とY軸が垂直であることを確保するため
        // Y軸をX軸とZ軸のクロス積から計算する
        this.axis[1].cross(this.axis[0], this.axis[2]);

        return this;
    }

    // オブジェクトの位置をbaseから出ているomegaの回転ベクトルによって回転させる
    // XYZ軸も回転させるかどうかというパラメーターあり
    public BaseObject rotate(Vector3d base, Vector3d omega, boolean includeAxis) {
        Vector3d delta = new Vector3d();
        Vector3d p_omega = new Vector3d();

        // 回転ベクトルに対してオブジェクトの位置ベクトルを計算
        p_omega.sub(this.p, base);

        // このp_omegaのベクトルを回転させるためのデルタを計算
        delta.cross(omega, p_omega);

        // p_omegaのサイズを保持するためにサイズを覚える
        double len = p_omega.length();

        // p_omegaを回転して、最初のサイズにもどす
        p_omega.add(delta);
        p_omega.scale(len/p_omega.length());

        // p_omegaから自分の位置を計算する
        this.p.add(base, p_omega);

        // XYZ軸も回転させる場合はrotate(omega)を呼び出す
        if (includeAxis) this.rotate(omega);

        return this;
    }

    // オブジェクトを描くが
    // 図形がないのでアブストラクト
    abstract void draw(World w, Graphics g);
}
