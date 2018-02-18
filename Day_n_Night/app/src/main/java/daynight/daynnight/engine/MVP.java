package daynight.daynnight.engine;

import android.opengl.Matrix;

import daynight.daynnight.engine.math.Mat4;
import daynight.daynnight.engine.math.Vec3;

/**
 * Created by andlat on 2018-02-04.
 */

class MVP {
    private Mat4 mProjection;
    private final Camera mCamera = new Camera();

    private float mViewportRatio;

    /**
     * Créer un MVP avec une projection perspective par défault de 45 degres
     */
    MVP(float viewportRatio){
        mViewportRatio = viewportRatio;

        //Make a default perspective projection of 45 degrees
        float[] projection = new float[16];
        Matrix.perspectiveM(projection, 0, 45, viewportRatio, 0.1f, 100.f);
        mProjection = new Mat4(projection, 0);
    }

    /**
     * Calcule la matrice de vue dans l'espace. Model View Projection matrix
     * @param modelMat4 Position du modèle. Utiliser une matrice d'identité pour un objet à l'origine.
     * @return La matrice MVP
     */
    Mat4 get(Mat4 modelMat4){
        modelMat4.mult(mCamera.lookAt()).mult(mProjection);
        return modelMat4;
    }

    Camera getCamera(){ return mCamera; }

    static class Camera{
        private Vec3 mEye, mCenter, mUp;

        /**
         * Créer une caméra avec des vecteurs de 0.f
         */
        Camera(){
            mEye = new Vec3();
            mCenter = new Vec3();
            mUp = new Vec3();
        }

        /**
         * Créer une caméra
         * @param eye Position de la caméra dans l'espace
         * @param center  Vers où regarde la caméra (son objectif)
         * @param up Direction de la tête de la caméra. Vers le haut: (0, 0, 1); Vers le bas: (0, 0, -1);
         */
        Camera(Vec3 eye, Vec3 center, Vec3 up){
            mEye = eye;
            mCenter = center;
            mUp = up;
        }

        Vec3 getEye() { return mEye; }
        Camera setEye(Vec3 pos) { mEye = pos; return this; }

        Vec3 getCenter() { return mCenter; }
        Camera setCenter(Vec3 eye) { mCenter = eye; return this; }


        Vec3 getUp() { return mUp; }
        Camera setUp(Vec3 head) { mUp = head; return this; }

        /**
         * Calcule la matrice de ce que la caméra voit
         * @return La matrice spécifiant ce que la caméra voit
         */
        Mat4 lookAt(){
            float[] lookat = new float[16];

            Matrix.setLookAtM(lookat, 0, mEye.x(), mEye.y(), mEye.z(), mCenter.x(), mCenter.y(), mCenter.z(), mUp.x(), mUp.y(), mUp.z());
            return new Mat4(lookat, 0);
        }
    }
}
