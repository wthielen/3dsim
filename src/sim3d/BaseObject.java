package sim3d;

import java.awt.Color;
import java.awt.Graphics;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

public abstract class BaseObject {
    // このオブジェクトが存在している世界のプロパティ
    // 例えば focus と fps をアクセスするために
    // ここに世界を保存する
    protected World w;

    // 3Dのベクトルクラスを使用して位置を設定
    protected Vector3d p;

    // このオブジェクトにとっての自分のXYZ軸
    protected Vector3d[] axis;

    // 色
    protected Color c = Color.BLACK;

    // 頂点の数と収集
    protected int nVertex;
    protected Vector3d[] vertex;

    // 軸のベクトルを作成する
    private void initAxis() {
        this.axis = new Vector3d[3];
        this.axis[0] = new Vector3d(1, 0, 0);
        this.axis[1] = new Vector3d(0, 1, 0);
        this.axis[2] = new Vector3d(0, 0, 1);
    }

    // 描くための必要な頂点を作成する
    protected void initVertices() {
        int nVertex = this.getVertexCount();

        if (null == this.vertex || this.vertex.length != nVertex) {
            this.vertex = new Vector3d[nVertex];
            for(int i = 0; i < nVertex; i++) {
                this.vertex[i] = new Vector3d();
            }
        }
    }

    // クラスメンバーがoverrideされていないので
    // このメソッドに通じてクラスの頂点数をアクセスする
    protected int getVertexCount() {
        return this.nVertex;
    }

    // 計算された3Dの頂点全てから
    // 一点透視図法によって2D座標を計算する
    protected Vector2d[] mapVertices() {
        int nVertex = this.getVertexCount();

        Vector2d xy[] = new Vector2d[nVertex];
        Vector3d v = new Vector3d();
        for(int i = 0; i < nVertex; i++) {
            // 0, 0, 0 から始まる
            v.set(0, 0, 0);

            // オブジェクトの位置へ移動
            v.add(this.p);

            // 各頂点の座標はオブジェクトのXYZ軸に対して計算されたので
            // ドット積を利用して各頂点の座標を計算して加算
            v.add(new Vector3d(
                    this.axis[0].dot(this.vertex[i]),
                    this.axis[1].dot(this.vertex[i]),
                    this.axis[2].dot(this.vertex[i])
                ));

            xy[i] = new Vector2d(
                    this.w.width/2.0 + v.x * this.w.focus / v.z,
                    this.w.height/2.0 - v.y * this.w.focus / v.z
                );
        }

        return xy;
    }

    // デフォルトコンストラクタ
    public BaseObject() {
        this.p = new Vector3d();
        this.initAxis();
    }

    // ベクトルを使用コンストラクタ
    public BaseObject(Vector3d p) {
        this.p = p;
        this.initAxis();
    }

    // 座標を使用コンストラクタ
    public BaseObject(double x, double y, double z) {
        this.p = new Vector3d(x, y, z);
        this.initAxis();
    }

    // 位置のゲッター
    public Vector3d getPosition() {
        return this.p;
    }

    // 色のゲッター
    public Color getColor() {
        return this.c;
    }

    // 世界のセッター
    public BaseObject setWorld(World w) {
        this.w = w;
        return this;
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

    // 色のセッター
    public BaseObject setColor(Color c) {
        this.c = c;
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
        Vector3d real_omega = new Vector3d(omega);
        real_omega.scale(1.0/this.w.fps);

        // X軸を回転させる
        delta.cross(this.axis[0], real_omega);
        this.axis[0].add(delta);
        this.axis[0].normalize();

        // Y軸を回転させる
        delta.cross(this.axis[1], real_omega);
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
        Vector3d real_omega = new Vector3d(omega);
        Vector3d p_omega = new Vector3d();

        // 1秒あたり |omega| のアングルで回転するので
        // 世界のFPSによる除算して今度のフレームのomegaを計算する
        real_omega.scale(1.0/this.w.fps);

        // 回転ベクトルに対してオブジェクトの位置ベクトルを計算
        p_omega.sub(this.p, base);

        // このp_omegaのベクトルを回転させるためのデルタを計算
        delta.cross(real_omega, p_omega);

        // p_omegaのサイズを保持するためにサイズを覚える
        double len = p_omega.length();

        // p_omegaを移動して、最初のサイズにもどすと
        // 位置ベクトルが回転したというわけです
        p_omega.add(delta);
        p_omega.scale(len/p_omega.length());

        // p_omegaから自分の位置を計算する
        // p = base + p_omega
        this.p.add(base, p_omega);

        // XYZ軸も回転させる場合はrotate(omega)を呼び出す
        if (includeAxis) this.rotate(omega);

        return this;
    }

    // オブジェクトを描くが
    // 図形がないのでアブストラクト
    abstract void draw(Graphics g);
}
