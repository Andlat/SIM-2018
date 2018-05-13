package daynight.daynnight.engine.math;

/**
 * Created by Nikola Zelovic on 2018-02-02.
 */

public abstract class Vector {
    /**
     * Exception qui est jeté quand le vecteur passé en argument pour l'opération mathématique à plus de composantes que "this".
     */
    public static class TooLargeException extends IllegalArgumentException {
        TooLargeException() {
            super("TooLargeException: Vector argument is larger than this.");
        }

        TooLargeException(String msg) {
            super("TooLargeException: " + msg);
        }
    }

    /**
     * Initialize array size in constructor of Vector subclass
     */
    float[] components;

    public float x() {
        return this.components[0];
    }

    public float y() {
        return this.components[1];
    }

    public void x(float x) {
        this.components[0] = x;
    }

    public void y(float y) {
        this.components[1] = y;
    }

    public float[] toArray(){ return this.components; }

    /**
     * Specifies the size of a new Vector
     */
    public enum VecSize{
        V2(2), V3(3), V4(4);
        private int size;
        VecSize(int size){ this.size = size; }

        public int toInt(){ return this.size; }
    }
    /**
     * Create a new Vector from a float array with the number of specified components
     * @param buff Float array containing the components
     * @param offset Offset of the array. (Starting position of where to read in the array)
     * @param size Size of new Vector
     * @return The newly created Vector
     */
    public static Vector make(VecSize size, float[] buff, int offset){
        //Create new Vector
        Vector new_vec;
        switch(size){
            case V2: new_vec = new Vec2(); break;
            case V3: new_vec = new Vec3(); break;
            case V4: new_vec = new Vec4(); break;
            default: new_vec = new Vec4(); break;
        }

        //Copy components
        System.arraycopy(buff, offset, new_vec.components, 0, size.toInt());
        return new_vec;
    }


    public void clear(){
        for(byte i=0; i < this.Dimensions(); ++i){
            this.components[i] = 0;
        }
    }

    /**
     * Check if all components of the vector equal to 0
     * @return Is the vector empty
     */
    public boolean isEmpty(){
        boolean isEmpty = true;

        for(byte i=0; i < this.Dimensions(); ++i){
            if(this.components[i] != 0) {
                isEmpty = false;
                break;
            }
        }
        return isEmpty;
    }

    public void SwitchXY(){
        float tmp = this.components[0];
        this.components[0] = this.components[1];
        this.components[1] = tmp;
    }

    /**
     * Nombre de composantes (ou dimensions) du vecteur
     * @return Nombre de composantes du vecteur
     */
    final byte Dimensions(){ return (byte)components.length; }

    /**
     * Produit scalaire
     * @param vec Vecteur à multiplier
     * @return produit scalaire
     */
    public float dot(Vector vec){
        //Use the smallest number of components, because if one Vector has more than the other, the product would be 0 for that component.
        byte smallest_vec_dimens = (this.Dimensions() <= vec.Dimensions() ? this.Dimensions() : vec.Dimensions());

        float dot = 0.f;

        for(byte i = 0; i < smallest_vec_dimens; ++i){
            dot += this.components[i] * vec.components[i];
        }

        return dot;
    }

    /**
     * Produit des composantes du vecteur.
     * Si le vecteur passé en argument est plus petit, il est "casté" au même nombre de composantes avec une valeur de 1.f pour les nouvelles
     *
     * @param vec vecteur à multiplier qui à le même nombre de dimensions ou moins.
     * @return this, qui est le vecteur résultant.
     * @throws TooLargeException Si vec à plus de composantes que l'objet "this"
     */
    public Vector mult(Vector vec) throws TooLargeException {
        return arithmetic(vec, Arithmetic_Op.MULT);
    }

    /**
     * Multiplication du vecteur par un scalaire
     *
     * @param scalar Scalaire à multiplier
     * @return this, qui est le vecteur résultant.
     */
    public Vector mult(float scalar) {
        for (byte i = 0; i < this.Dimensions(); ++i) {
            this.components[i] *= scalar;
        }

        return this;
    }

    /**
     * Division des composantes du vecteur.
     * Si le vecteur passé en argument est plus petit, il est "casté" au même nombre de composantes avec une valeur de 1.f pour les nouvelles
     *
     * @param vec vecteur à diviser qui à le même nombre de dimensions ou moins.
     * @return this, qui est le vecteur résultant.
     * @throws TooLargeException Si vec à plus de composantes que l'objet "this"
     */
    public Vector divide(Vector vec) throws TooLargeException{
        return arithmetic(vec, Arithmetic_Op.DIVIDE);
    }

    /**
     * Additionne les composantes du vecteur.
     * Si le vecteur passé en argument est plus petit, il est "casté" au même nombre de composantes avec une valeur de 1.f pour les nouvelles
     *
     * @param vec vecteur à additionner qui à le même nombre de dimensions ou moins.
     * @return this, qui est le vecteur résultant.
     * @throws TooLargeException Si vec à plus de composantes que l'objet "this"
     */
    public Vector add(Vector vec) throws TooLargeException{
        return arithmetic(vec, Arithmetic_Op.ADD);
    }

    /**
     * Soustrait les composantes du vecteur.
     * Si le vecteur passé en argument est plus petit, il est "casté" au même nombre de composantes avec une valeur de 1.f pour les nouvelles
     *
     * @param vec vecteur à soustraire qui à le même nombre de dimensions ou moins.
     * @return this, qui est le vecteur résultant.
     * @throws TooLargeException Si vec à plus de composantes que l'objet "this"
     */
    public Vector substract(Vector vec) throws TooLargeException{
        return arithmetic(vec, Arithmetic_Op.SUB);
    }

    /** Operations arithmétiques de base **/
    private enum Arithmetic_Op{ MULT, DIVIDE, ADD, SUB }

    /**
     * Execute l'opération arithmétique spécifier sur les vecteurs
     * @param vec Vecteur à utiliser
     * @param op_type Type d'opération
     * @return this, qui est le vecteur résultant.
     * @throws TooLargeException
     */
    private Vector arithmetic(Vector vec, Arithmetic_Op op_type) throws TooLargeException{
        if(!isVecTooBig(vec)) {
            for (byte i = 0; i < vec.Dimensions(); ++i) {
                switch (op_type) {
                    case MULT:
                        this.components[i] *= vec.components[i];
                        break;
                    case DIVIDE:
                        this.components[i] /= vec.components[i];
                        break;
                    case ADD:
                        this.components[i] += vec.components[i];
                        break;
                    case SUB:
                        this.components[i] -= vec.components[i];
                        break;
                }
            }
        }
        return this;
    }

    /**
     * Vérifie si vec à égal ou moins de composantes que "this". Sinon, une exception est jetée.
     * @param vec Vecteur à vérifier
     * @return Si le vecteur à vérifier <= composantes que "this"
     * @throws TooLargeException Si vec > composantes que "this"
     */
    private boolean isVecTooBig(Vector vec) throws TooLargeException{
        if (this.Dimensions() >= vec.Dimensions()) {
            return false;
        } else {
            throw new TooLargeException();
        }
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder().append("Vector{");

        for(float v : components){
            b.append(v + ", ");
        }
        b.replace(b.length(), b.length()+1, "}");

        return b.toString();
    }
}
