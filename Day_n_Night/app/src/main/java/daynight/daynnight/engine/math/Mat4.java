package daynight.daynnight.engine.math;

import android.opengl.Matrix;

/**
 * Created by andlat on 2018-02-04.
 */

public class Mat4 {
    private float[] mMatrix = new float[16];

    /**
     * Créer une matrice 4x4 d'identité 1.f
     */
    public Mat4(){
        this(1.f);
    }

    /**
     * Créer une matrice d'identité 4x4.
     * @param id_val Valeur de l'identité
     */
    public Mat4(float id_val){
        this.clear(id_val);
    }

    /**
     * Créer une matrice 4x4 à partir d'un buffer
     * @param matrix Buffer contenant les valeurs de la matrice 4x4
     * @param offset Position de où lire les données du buffer
     */
    public Mat4(float[] matrix, int offset){
        System.arraycopy(matrix, offset, mMatrix, 0, 16);
    }

    public float[] toArray(){
        return mMatrix;
    }

    /**
     * Restaurer la matrice 4x4 à une matrice d'identité
     * @param id_val Valeur de l'identité
     */
    public void clear(float id_val){
        mMatrix[0] = id_val;
        mMatrix[5] = id_val;
        mMatrix[10] = id_val;
        mMatrix[15] = id_val;
    }

    /**
     * Multiplier 2 matrices 4x4 ensemble
     * @param mat Matrice à multiplier
     * @return "this": La matrice résultante
     */
    public Mat4 mult(Mat4 mat){
        Matrix.multiplyMM(mMatrix, 0, mMatrix, 0, mat.mMatrix, 0);
        return this;
    }

    /**
     * Multiplier la matrice "this" avec un vecteur à 4 composantes (Vec4)
     * @param vec Vecteur à multiplier
     * @return "this": La matrice résultante
     */
    public Vec4 mult(Vec4 vec){
        float[] result = new float[4];

        Matrix.multiplyMV(result, 0, mMatrix, 0, vec.components, 0);

        return (Vec4)Vector.make(Vector.VecSize.V4, result, 0);
    }
}
