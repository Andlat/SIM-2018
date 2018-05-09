package daynight.daynnight.engine.physics;

import daynight.daynnight.engine.math.Vec3;

/**
 * Created by Nikola Zelovic on 2018-02-05.
 */

public class PhysicsAttributes {
    public static class StaticModelAttr {
        /**
         * Mass is in grams
         */
        private float mMass=0, mElasticity=0, mFricitonCoef=0;

        public StaticModelAttr(){}
        public StaticModelAttr(float mass, float elasticity, float fricitonCoef){
            mMass = mass;
            mElasticity = elasticity;
            mFricitonCoef = fricitonCoef;
        }

        public float getMass() {
            return mMass;
        }

        public void setMass(float mass) {
            mMass = mass;
        }

        public float getElasticity() {
            return mElasticity;
        }

        public void setElasticity(float elasticity) {
            mElasticity = elasticity;
        }

        public float getFricitonCoef() {
            return mFricitonCoef;
        }

        public void setFricitonCoef(float fricitonCoef) {
            mFricitonCoef = fricitonCoef;
        }

        public void CloneTo(StaticModelAttr attr){
            attr.mMass = this.mMass;
            attr.mElasticity = this.mElasticity;
            attr.mFricitonCoef = this.mFricitonCoef;
        }
    }
    public static class MovingModelAttr extends StaticModelAttr {
        public enum Movement{ SLIDING, BALL, WALKING }
        private Movement mMovement = Movement.SLIDING;

        private float mSpeed = 0.f;

        public MovingModelAttr(){
            super();
        }
        public MovingModelAttr(float mass, float elasticity, float fricitonCoef, float speed){
            super(mass, elasticity, fricitonCoef);
            mSpeed = speed;
        }
        public MovingModelAttr(float mass, float elasticity, float fricitonCoef, float speed, Movement movement){
            this(mass, elasticity, fricitonCoef, speed);
            mMovement = movement;
        }

        public float getSpeed() {
            return mSpeed;
        }
        public void setSpeed(float speed) {
            mSpeed = speed;
        }

        public Movement getMovementType() {
            return mMovement;
        }
        public void setMovementType(Movement movement) {
            mMovement = movement;
        }

        public void CloneTo(MovingModelAttr attr){
            super.CloneTo(attr);

            attr.mMovement = this.mMovement;
            attr.mSpeed = this.mSpeed;
        }
    }


    public static class WorldAttr{
        private float mGravity=0.f, mFluidCoeff=0.f;

        public WorldAttr(float gravity){
            mGravity = gravity;
        }

        public WorldAttr(float gravity, float fluidCoeff){
            mGravity = gravity;
            mFluidCoeff = fluidCoeff;
        }

        public float getGravity() {
            return mGravity;
        }
        public void setGravity(float gravity) {
            mGravity = gravity;
        }

        public float getFluidCoeff() {
            return mFluidCoeff;
        }

        public void setFluidCoeff(float fluidCoeff) {
            mFluidCoeff = fluidCoeff;
        }

        public void CloneTo(WorldAttr attr){
            attr.mGravity = this.mGravity;
            attr.mFluidCoeff = this.mFluidCoeff;
        }
    }
}
