package daynight.daynnight.engine;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import daynight.daynnight.engine.Model.Model;
import daynight.daynnight.engine.math.Vec2;
import daynight.daynnight.engine.math.Vec3;

/**
 * Created by zelovini on 2018-02-05.
 */

class ObjParser {
    /**
     * Charger un fichier .obj en Modèle
     * @param context Contexte de l'application (ou de l'activité). Utilisé pour ouvrir le dossier "assets"
     * @param file Fichier .obj qui se trouve dans le dossier "assets"
     * @return List of models from the the object file
     * @throws IOException If the .obj file couldn't be read
     */
    @SuppressWarnings("all")
    static List<Model> Parse(Context context, String file) throws IOException{
        List<Model> list = new ArrayList<>();
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open(file)));

            final List<Vec3> tmp_vertices = new ArrayList<>(), tmp_normals = new ArrayList<>();
            final List<Vec2> tmp_uvs = new ArrayList<>();
            Model tmp_model = null;

            String mtl_lib="";

            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(" ");

                switch(parts[0]){
                    case "o":
                        if(tmp_model != null) {
                            list.add(tmp_model);
                        }

                        tmp_model = new Model();
                        break;
                    case "v":
                        tmp_vertices.add(new Vec3(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3])));
                        break;
                    case "vt":
                        tmp_uvs.add(new Vec2(Float.parseFloat(parts[1]), Float.parseFloat(parts[2])));
                        break;
                    case "vn":
                        tmp_normals.add(new Vec3(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3])));
                        break;
                    case "f":
                        //Create the VBO
                        final int vboSize = (((tmp_vertices.size() + tmp_normals.size()) * 3) + (tmp_uvs.size() * 2)) * Float.SIZE;// 3 floats per vertex, 3 floats per normal, 2 float per uv (texture coordinates)
                        final FloatBuffer vbo = ByteBuffer.allocateDirect(vboSize).order(ByteOrder.nativeOrder()).asFloatBuffer();


                        //Set the offsets of the VBO
                        if(!tmp_vertices.isEmpty())
                            tmp_model.setVerticesOffset(0);

                        if(!tmp_uvs.isEmpty())
                            tmp_model.setVerticesOffset(3);

                        if(!tmp_normals.isEmpty())
                            tmp_model.setNormalsOffset(5);


                        //3 vertices per face (a face is a triangle)
                        for(byte i=1; i < parts.length-1; ++i){
                            String[] corner = parts[i].split("/");
                            Vec3 v, vn;
                            Vec2 vt;

                            //---- Add the items to the vbo -----\\

                            //Vertices
                            if(!tmp_vertices.isEmpty()) {
                                v = tmp_vertices.get(Integer.parseInt(corner[0]));
                                vbo.put(v.x());     vbo.put(v.y());     vbo.put(v.z());
                            }

                            //Texture UVs
                            if(!tmp_uvs.isEmpty()) {
                                vt = tmp_uvs.get(Integer.parseInt(corner[1]));
                                vbo.put(vt.x());    vbo.put(vt.y());
                            }

                            //Normals
                            if(!tmp_normals.isEmpty()) {
                                vn = tmp_normals.get(Integer.parseInt(corner[2]));
                                vbo.put(vn.x());    vbo.put(vn.y());    vbo.put(vn.z());
                            }
                        }

                        tmp_model.setVBO(vbo);
                        break;
                    case "usemtl"://Material of the model
                        tmp_model.setTexture(getTexture(context, mtl_lib, parts[1]));
                        break;
                    case "mtllib": //Once per file. At the beginning of the file
                        mtl_lib = parts[1];
                        break;
                }
            }

        }catch(NullPointerException ex) {
            Log.e("Object Parser", "Failed to parse .obj file. File's format is wrong.");
        }finally {
            if(reader != null)
                reader.close();
        }
        return list;
    }

    private static String getTexture(Context context, String mtl_file, String mtl_name) throws IOException{
        String texture = "";

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open(mtl_file)));

            boolean found_mtl = false;
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(" ");

                if(!found_mtl) {
                    if (parts[0].equals("newmtl") && parts[1].equals(mtl_name))
                        found_mtl = true;
                }else{
                    if(parts[0].equals("map_Kd"))
                        texture = parts[1];
                }
            }
        }finally {
            if(reader != null)
                reader.close();
        }

        return texture;
    }
}
