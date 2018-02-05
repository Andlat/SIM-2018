package daynight.daynnight.engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by andlat on 2018-02-02.
 */

class Geometry {
    static class Triangle {
        private FloatBuffer vertexBuffer;

        private static float coords[] = {
                .5f, 0.f, 0.f, //Top middle
                -.5f, .5f, 0.f, //Bottom right
                -.5f, -.5f, 0.f //Bottom Left
        };

        private float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.f};

        Triangle() {
            ByteBuffer bb = ByteBuffer.allocateDirect(coords.length * 4);//4 bytes per float
            bb.order(ByteOrder.nativeOrder());//Set endianness

            vertexBuffer = bb.asFloatBuffer();
            vertexBuffer.put(coords);
            vertexBuffer.position(0);
        }

        int getVertexCount(){ return coords.length; }
        int getVertexSize(){ return coords.length * 4; }
        FloatBuffer getVertexBuffer(){ return vertexBuffer; }
    }

    static class Square {
        private FloatBuffer vertexBuffer;
        private ShortBuffer drawOrderBuffer;

        private static float coords[] = {
                .5f, -.5f, 0.f, //Top left
                -.5f, -.5f, 0.f, //Bottom Left
                -.5f, .5f, 0.f, //Bottom right
                .5f, .5f, 0.f//Top right
        };
        private short drawOrder[] = {0, 1, 2, 0, 2, 3};

        private float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.f};


        Square() {
            //Vertex buffer
            ByteBuffer bb = ByteBuffer.allocateDirect(coords.length * 4);//4 bytes per float
            bb.order(ByteOrder.nativeOrder());//Set endianness
            vertexBuffer = bb.asFloatBuffer();
            vertexBuffer.put(coords);
            vertexBuffer.position(0);

            //Draw order (indexing) buffer
            ByteBuffer dob = ByteBuffer.allocateDirect(drawOrder.length * 2);
            dob.order(ByteOrder.nativeOrder());
            drawOrderBuffer = dob.asShortBuffer();
            drawOrderBuffer.put(drawOrder);
            drawOrderBuffer.position(0);
        }

        int getVertexCount(){ return coords.length; }
        int getVertexSize(){ return coords.length * 4; }
        FloatBuffer getVertexBuffer(){ return vertexBuffer; }
        ShortBuffer getDOBuffer(){ return drawOrderBuffer; }
    }
}
