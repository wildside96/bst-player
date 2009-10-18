/*
 * Copyright 2009 Sikirulai Braheem
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.bramosystems.oss.player.core.client;

import com.google.gwt.user.client.Window;

/**
 * Represents a transformation matrix that determines how to map points from one coordinate space
 * to another. The matrix can be used to perform various two-dimensional graphical transformations
 * including translation (x and y repositioning), rotation, scaling, and skewing.
 *
 * <p>A transformation matrix object is a 3 x 3 matrix with the following contents:
 *
 * <pre>
 *      --         --
 *      | a   b   u |
 *      | c   d   v |
 *      | tx  ty  w |
 *      --         --
 * </pre>
 *
 * <b>Note:</b> Property values <code>u</code> and <code>v</code> are always 0.0,
 * while <code>w</code> is always 1.0.
 *
 * @since 1.1
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 */
public class TransformationMatrix implements Cloneable {

    private double a,  b,  c,  d,  tx,  ty,  u,  v,  w = 1.0;

    private TransformationMatrix(double a, double b, double c, double d, double tx, double ty,
            double u, double v, double w) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.tx = tx;
        this.ty = ty;
        this.u = u;
        this.v = v;
        this.w = w;
    }

    /**
     * Constructs a new identity TransformationMatrix object.
     */
    public TransformationMatrix() {
        a = 1.0;
        d = 1.0;
    }

    /**
     * Constructs a new TransformationMatrix object.
     *
     * @param a matrix element a
     * @param b matrix element b
     * @param c matrix element c
     * @param d matrix element d
     * @param tx matrix element tx
     * @param ty matrix element ty
     */
    public TransformationMatrix(double a, double b, double c, double d, double tx, double ty) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.tx = tx;
        this.ty = ty;
    }

    /**
     * Returns the matrix elements as a String
     *
     * @return the matrix in the form <code>a, b, u, c, d, v, tx, ty, w</code>
     */
    @Override
    public String toString() {
        return a + ", " + b + ", 0.0, " + c + ", " + d + ", 0.0, " + tx + ", " + ty + ", 1.0";
    }

    /**
     * Returns matrix element a
     *
     * @return the a
     */
    public double getA() {
        return a;
    }

    /**
     * Returns matrix element b
     *
     * @return the b
     */
    public double getB() {
        return b;
    }

    /**
     * Returns matrix element c
     *
     * @return the c
     */
    public double getC() {
        return c;
    }

    /**
     * Returns matrix element d
     *
     * @return the d
     */
    public double getD() {
        return d;
    }

    /**
     * Returns matrix element tx
     *
     * @return the tx
     */
    public double getTx() {
        return tx;
    }

    /**
     * Returns matrix element ty
     *
     * @return the ty
     */
    public double getTy() {
        return ty;
    }

    /**
     * Sets the matrix element a
     *
     * @param a the a to set
     */
    public void setA(double a) {
        this.a = a;
    }

    /**
     * Sets the matrix element b
     *
     * @param b the b to set
     */
    public void setB(double b) {
        this.b = b;
    }

    /**
     * Sets the matrix element c
     *
     * @param c the c to set
     */
    public void setC(double c) {
        this.c = c;
    }

    /**
     * Sets the matrix element d
     *
     * @param d the d to set
     */
    public void setD(double d) {
        this.d = d;
    }

    /**
     * Sets the matrix element tx
     *
     * @param tx the tx to set
     */
    public void setTx(double tx) {
        this.tx = tx;
    }

    /**
     * Sets the matrix element ty
     *
     * @param ty the ty to set
     */
    public void setTy(double ty) {
        this.ty = ty;
    }

    /**
     * Performs a displacement transformation on this matrix along the x and y axes
     *
     * @param x displacement along the x-axis (in pixels)
     * @param y displacement along the y-axis (in pixels)
     */
    public void translate(double x, double y) {
        multiply(new TransformationMatrix(1, 0, 0, 1, x, y));
    }

    /**
     * Performs a scalling transformation on this matrix along the x and y axes. The
     * x-axis is multiplied by <code>x</code> and the y-axis is multiplied by <code>y</code>.
     *
     * @param x multiplier used to scale the matrix along the x-axis
     * @param y multiplier used to scale the matrix along the y-axis
     */
    public void scale(double x, double y) {
        multiply(new TransformationMatrix(x, 0, 0, y, 0, 0));
    }

    /**
     * Applies a rotation transformation to this matrix.
     *
     * @param angle angle of rotation in radians
     */
    public void rotate(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        multiply(new TransformationMatrix(cos, sin, -1 * sin, cos, 0, 0));
    }

    /**
     * Applies a skewing transformation to this matrix
     *
     * @param ax skew angle along the x-axis
     * @param ay skew angle along the y-axis
     */
    public void skew(double ax, double ay) {
        multiply(new TransformationMatrix(0, Math.tan(ay), Math.tan(ax), 0, 0, 0));
    }

    /**
     * Performs an inversion on this matrix
     */
    public void invert() {
        double det = getDeterminant();
        Window.alert("Det : " + det);
        toCofactor();
        toTranspose();

        a /= det;
        b /= det;
        u /= det;
        c /= det;
        d /= det;
        v /= det;
        tx /= det;
        ty /= det;
        w /= det;
    }

//    @Override
    public TransformationMatrix clone() {
        return new TransformationMatrix(a, b, c, d, tx, ty, u, v, w);
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        final TransformationMatrix other = (TransformationMatrix) obj;
        if ((this.a != other.a) || (this.b != other.b) || (this.c != other.c) ||
                (this.d != other.d) || (this.tx != other.tx) || (this.ty != other.ty) ||
                (this.u != other.u) || (this.v != other.v) || (this.w != other.w)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + (int) ((long) a ^ ((long) a >>> 32));
        hash = 83 * hash + (int) ((long) (this.b) ^ ((long) (this.b) >>> 32));
        hash = 83 * hash + (int) ((long) (this.c) ^ ((long) (this.c) >>> 32));
        hash = 83 * hash + (int) ((long) (this.d) ^ ((long) (this.d) >>> 32));
        hash = 83 * hash + (int) ((long) (this.tx) ^ ((long) (this.tx) >>> 32));
        hash = 83 * hash + (int) ((long) (this.ty) ^ ((long) (this.ty) >>> 32));
        hash = 83 * hash + (int) ((long) (this.u) ^ ((long) (this.u) >>> 32));
        hash = 83 * hash + (int) ((long) (this.v) ^ ((long) (this.v) >>> 32));
        hash = 83 * hash + (int) ((long) (this.w) ^ ((long) (this.w) >>> 32));
        return hash;
    }

    /**
     * Performs matrix multiplication on this matrix with the specified transformation matrix and
     * keeps the result in this matrix
     *
     * @param m the matrix to multipy with this matrix
     */
    public void multiply(TransformationMatrix m) {
//        Window.alert("[Multiply] this : " + toString() + ", other : " + m);
        /*                  ---->
         * | |a  b  u|   |a  b  u|
         * | |c  d  v| x |c  d  v|
         * | |tx ty w|   |tx ty w|
         * v
         */
        double a11 = (a * m.a) + (c * m.b) + (tx * m.u);
        double a21 = (a * m.c) + (c * m.d) + (tx * m.v);
        double a31 = (a * m.tx) + (c * m.ty) + (tx * m.w);

        double a12 = (b * m.a) + (d * m.b) + (ty * m.u);
        double a22 = (b * m.c) + (d * m.d) + (ty * m.v);
        double a32 = (b * m.tx) + (d * m.ty) + (ty * m.w);

        double a13 = (u * m.a) + (v * m.b) + (w * m.u);
        double a23 = (u * m.c) + (v * m.d) + (w * m.v);
        double a33 = (u * m.tx) + (v * m.ty) + (w * m.w);

        /*    ---->
         *  |a  b  u|   | |a  b  u|
         *  |c  d  v| x | |c  d  v|
         *  |tx ty w|   | |tx ty w|
         *              v
         */
//        double a11 = (a * m.a) + (b * m.c) + (u * m.tx);
//        double a21 = (a * m.b) + (b * m.d) + (u * m.ty);
//        double a31 = (a * m.u) + (b * m.v) + (u * m.w);

//        double a12 = (c * m.a) + (d * m.c) + (v * m.tx);
//        double a22 = (c * m.b) + (d * m.d) + (v * m.ty);
//        double a32 = (c * m.u) + (d * m.v) + (v * m.w);

//        double a13 = (tx * m.a) + (ty * m.c) + (w * m.tx);
//        double a23 = (tx * m.b) + (ty * m.d) + (w * m.ty);
//        double a33 = (tx * m.u) + (ty * m.v) + (w * m.w);

        a = a11;
        b = a12;
        u = a13;
        c = a21;
        d = a22;
        v = a23;
        tx = a31;
        ty = a32;
        w = a33;
    }

    /**
     * Converts this matrix to its transpose
     */
    private void toTranspose() {
        double a12 = c;
        double a13 = tx;
        double a21 = b;
        double a23 = ty;
        double a31 = u;
        double a32 = v;
        b = a12;
        u = a13;
        c = a21;
        v = a23;
        tx = a31;
        ty = a32;
    }

    /**
     * Converts this matrix to a matrix of its cofactors
     */
    private void toCofactor() {
        double a11 = (d * w) - (v * ty);
        double a12 = (c * w) - (v * tx);
        double a13 = (c * ty) - (d * tx);
        double a21 = (b * w) - (u * ty);
        double a22 = (a * w) - (u * tx);
        double a23 = (a * ty) - (b * tx);
        double a31 = (b * v) - (u * d);
        double a32 = (a * v) - (u * c);
        double a33 = (a * d) - (b * c);
        a = a11;
        b = -1 * a12;
        u = a13;
        c = -1 * a21;
        d = a22;
        v = -1 * a23;
        tx = a31;
        ty = -1 * a32;
        w = a33;
    }

    /**
     * Returns the determinant of this marix
     * @return
     */
    private double getDeterminant() {
        double a1 = (d * w) - (v * ty);
        double a2 = (c * w) - (v * tx);
        double a3 = (c * ty) - (d * tx);
        return (a * a1) - (b * a2) + (u * a3);
    }
}
