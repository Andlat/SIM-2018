package daynight.daynnight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ListeBadges extends AppCompatActivity
{
    //Créer
    Button retour;
    static ArrayList<Badge> badges = new ArrayList<>();
    static AdapteurArrayBadge adapteur;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_badges);


        adapteur = new AdapteurArrayBadge(this, 0, badges);

        ListView listView = (ListView) findViewById(R.id.listeBadges);
        listView.setAdapter(adapteur);


        //Attribuer
        retour = (Button) findViewById(R.id.retour);

        //Ajout de Badges manuellement
        Date c = Calendar.getInstance().getTime();
        badges.add(new Badge("badge_montagnes","Quillorama, Marieville, Qc, Ca",c,"Divertissement"));
        badges.add(new Badge("badge_feu_foret","Quillorama, Marieville, Qc, Ca",c,"Divertissement"));
        badges.add(new Badge("badge_montagnes","Quillorama, Marieville, Qc, Ca",c,"Divertissement"));
        badges.add(new Badge("badge_feu_foret","Quillorama, Marieville, Qc, Ca",c,"Divertissement"));
        badges.add(new Badge("badge_montagnes","Quillorama, Marieville, Qc, Ca",c,"Divertissement"));
        badges.add(new Badge("badge_feu_foret","Quillorama, Marieville, Qc, Ca",c,"Divertissement"));
        badges.add(new Badge("badge_montagnes","Quillorama, Marieville, Qc, Ca",c,"Divertissement"));

        retour.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //TODO
            }
        });
    }

    //custom ArrayAdapter
    class AdapteurArrayBadge extends ArrayAdapter<Badge>
    {
        private Context context;
        private int layout;
        private List<Badge> badges;

        public AdapteurArrayBadge(Context context, int resource, ArrayList<Badge> objects)
        {
            super(context, resource, objects);

            this.context = context;
            this.layout = resource;
            this.badges = objects;
        }

        public View getView(final int position, View convertView, ViewGroup parent)
        {
            Badge badge = badges.get(position);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_badge, null);

            ImageView logo = (ImageView) view.findViewById(R.id.logo);
            TextView locationCapture = (TextView) view.findViewById(R.id.locationCapture);
            TextView dateCapture = (TextView) view.findViewById(R.id.dateCapture);
            TextView type = (TextView) view.findViewById(R.id.type);

            int imageID = context.getResources().getIdentifier(badge.getLogo(), "drawable", context.getPackageName());
            logo.setImageResource(imageID);
            locationCapture.setText(badge.getLocationCapture());
            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            String date = df.format(badge.getDateCapture());
            dateCapture.setText(date);
            type.setText(badge.getType());

            return view;
        }
    }
}
