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

import daynight.daynnight.R;
import daynight.daynnight.engine.Model.Model;
import daynight.daynnight.engine.Model.Texture;
import daynight.daynnight.engine.math.Vec2;
import daynight.daynnight.engine.math.Vec3;
import daynight.daynnight.engine.util.Util;

/**
 * Created by zelovini on 2018-02-05.
 */

//TODO This parser is made for glDrawArrays. For faster processing and drawing, it should parse for glDrawElements and the World class should use glDrawElements
public class ObjParser {
    /**
     * Charger un fichier .obj en Modèle
     * @param context Contexte de l'application (ou de l'activité). Utilisé pour ouvrir le dossier "assets"
     * @param file Fichier .obj qui se trouve dans le dossier "assets"
     * @return List of models from the the object file
     * @throws IOException If the .obj file couldn't be read
     */
    @SuppressWarnings("all")
    public static List<Model> Parse(Context context, String directory, String file) throws IOException{
        List<Model> list = new ArrayList<>();
        BufferedReader reader = null;


        try{
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open(directory + '/' + file)));

            final List<Vec3> tmp_vertices = new ArrayList<>(), tmp_normals = new ArrayList<>();
            final List<Vec2> tmp_uvs = new ArrayList<>();
            final List<Float> facesVbo = new ArrayList<>();

            Model tmp_model = null;

            String mtl_lib="";

            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(" ");

                switch(parts[0]){
                    case "o":
                        if(tmp_model != null) {
                            //Allocate the real VBO
                            final int vboSize = facesVbo.size() * Util.FLOAT_SIZE;// 3 floats per vertex, 3 floats per normal, 2 float per uv (texture coordinates)
                            FloatBuffer vbo = ByteBuffer.allocateDirect(vboSize).order(ByteOrder.nativeOrder()).asFloatBuffer();
                            vbo.put(Util.FloatListToArray(facesVbo));
                            vbo.position(0);

                            tmp_model.setVBO(vbo);
                            list.add(tmp_model);
                        }

                        tmp_model = new Model();

                        tmp_vertices.clear();
                        tmp_uvs.clear();
                        tmp_normals.clear();
                        facesVbo.clear();
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
                            //3 vertices per face (a face is a triangle)
                            for (byte i = 1; i < parts.length; ++i) {
                                String[] vertex = parts[i].split("/");
                                Vec3 v, vn;
                                Vec2 vt;

                                //---- Add the items to the vbo -----\\

                                //Vertices
                                if (!tmp_vertices.isEmpty()) {
                                    v = tmp_vertices.get(Integer.parseInt(vertex[0]) - 1);
                                    facesVbo.add(v.x());
                                    facesVbo.add(v.y());
                                    facesVbo.add(v.z());
                                }

                                //Texture UVs
                                if (!tmp_uvs.isEmpty()) {
                                    vt = tmp_uvs.get(Integer.parseInt(vertex[1]) - 1);
                                    facesVbo.add(vt.x());
                                    facesVbo.add(1-vt.y());
                                }

                                //Normals
                                if (!tmp_normals.isEmpty()) {
                                    vn = tmp_normals.get(Integer.parseInt(vertex[2]) - 1);
                                    facesVbo.add(vn.x());
                                    facesVbo.add(vn.y());
                                    facesVbo.add(vn.z());
                                }
                            }

                        break;
                    case "usemtl"://Material of the model
                        String texSource = ObjParser.getTexture(context, directory + '/' + mtl_lib, parts[1]).split("\\.")[0];//Delete the extension
                        tmp_model.setTextureSource(texSource);

                        try {
                            tmp_model.setTexture(Texture.Load(context, context.getResources().getIdentifier(texSource, "drawable", context.getPackageName())));
                            Log.e("SET", tmp_model.getTexture().toString());
                        }catch(IOException | RuntimeException ex){
                            Log.e("OBJ Parser", "Failed to load texture");
                            Log.e("Loading tex exception", ex.getMessage());
                        }
                        break;
                    case "mtllib": //Once per file. At the beginning of the file
                        mtl_lib = parts[1];
                        break;
                }
            }
            //Add last model. Exactly the same code as case "o". //TODO Not urgent. Find a way to reuse this code and not have it at 2 different places.
            if(tmp_model != null) {
                //Allocate the real VBO
                final int vboSize = facesVbo.size() * Util.FLOAT_SIZE;// 3 floats per vertex, 3 floats per normal, 2 float per uv (texture coordinates)
                FloatBuffer vbo = ByteBuffer.allocateDirect(vboSize).order(ByteOrder.nativeOrder()).asFloatBuffer();
                vbo.put(Util.FloatListToArray(facesVbo));
                vbo.position(0);

                tmp_model.setVBO(vbo);
                list.add(tmp_model);
            }

            /*-------- Set the offsets of the VBO --------*/
            int offset = 0;
            if(!tmp_vertices.isEmpty()) {
                tmp_model.setVerticesOffset(offset);
                offset += 3;
            }

            if(!tmp_uvs.isEmpty()) {
                tmp_model.setTexOffset(offset);
                offset += 2;
            }

            if(!tmp_normals.isEmpty())
                tmp_model.setNormalsOffset(offset);

            /*---------------------------------------------*/

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
