package info.androidhive.habadProject.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;


import com.az.igrot.hakodesh.R;
import info.androidhive.habadProject.activity.SavedArticle;

import static android.content.Context.MODE_PRIVATE;


public class TwoFragment extends Fragment {

    private ArrayAdapter<SavedArticle> listAdapter;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private String articleJSON;
    ListView mainListView;
    static boolean clicked = false;
    private ArrayList<SavedArticle> articleList = new ArrayList<SavedArticle>();


    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {

            updateList();

        }
        if (!isVisibleToUser) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        mainListView = (ListView) view.findViewById(R.id.myArticlesListView);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item,
                                    int position, long id) {
                SavedArticle savedarticle = listAdapter.getItem(position);
                savedarticle.toggleChecked();
                articleViewHolder viewHolder = (articleViewHolder) item.getTag();
                viewHolder.getCheckBox().setChecked(savedarticle.isChecked());


            }
        });


//remove togled

        ImageButton myFab = (ImageButton) view.findViewById(R.id.removeFromList);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(getActivity(), "מחיקה מהרשימה בוצעה בצלחה",
                        Toast.LENGTH_LONG).show();
                Iterator<SavedArticle> iter = articleList.iterator();
                while (iter.hasNext()) {
                    SavedArticle SA = iter.next();
                    if (SA.isChecked()) {
                        iter.remove();
                    }
                }
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                Gson gson = new Gson();
                String articleJSON = gson.toJson(articleList);
                editor.putString("savedArticles", articleJSON);
                editor.commit();
                updateList();
            }

        });


        return view;
    }


    public void updateList() {

        SharedPreferences preferences = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        articleJSON = preferences.getString("savedArticles", null);
        Type listType = new TypeToken<ArrayList<SavedArticle>>() {
        }.getType();
        articleList = new Gson().fromJson(articleJSON, listType);
        // Set our custom array adapter as the ListView's adapter.
        listAdapter = new articleArrayAdapter(getActivity(), articleList);
        if (articleList != null)
            mainListView.setAdapter(listAdapter);
    }


    /**
     * Holds child views for one row.
     */
    private static class articleViewHolder {
        private CheckBox checkBox;
        private TextView textView;

        public articleViewHolder() {

        }

        public articleViewHolder(TextView textView, CheckBox checkBox) {
            this.checkBox = checkBox;
            this.textView = textView;

        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }

        public TextView getTextView() {
            return textView;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }
    }


    /**
     * Custom adapter for displaying an array of Planet objects.
     */
    private class articleArrayAdapter extends ArrayAdapter<SavedArticle> {
        //private static   TextView expandText;
        private LayoutInflater inflater;
        //private TextView expandButton;
        private ArrayList<SavedArticle> articleList = new ArrayList<SavedArticle>();


        public articleArrayAdapter(Context context, ArrayList<SavedArticle> articleList) {
            super(context, R.layout.my_book_simplerow, R.id.rowTextView, articleList);
            // Cache the LayoutInflate to avoid asking for a new one each time.
            this.articleList = articleList;

            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // savedarticle to display
            SavedArticle savedarticle = this.getItem(position);

            // The child views in each row.
            final CheckBox checkBox;
            final TextView textView;

            // Create a new row view
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.my_book_simplerow, null);

                // Find the child views.
                textView = (TextView) convertView.findViewById(R.id.rowTextView);
                checkBox = (CheckBox) convertView.findViewById(R.id.CheckBox01);


                // Optimization: Tag the row with it's child views, so we don't have to
                // call findViewById() later when we reuse the row.
                convertView.setTag(new articleViewHolder(textView, checkBox));
                textView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        TextView cb = (TextView) v;
                        //   System.out.println("Intent");

                    }
                });

                textView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        TextView tv = (TextView) v;

                        if (!clicked) {
                            tv.setText(articleList.get(position).getIgeretContent());
//                            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/webfont.ttf");
//                            tv.setTypeface(font);
                            clicked = true;

                        } else {
                            tv.setText(articleList.get(position).toString());
                            clicked = false;
                        }

                        // Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/webfont.ttf");
                        //  textView.setTypeface(font);



                    }

                });
                // If CheckBox is toggled, update the planet it is tagged with.
                checkBox.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        SavedArticle savedarticle = (SavedArticle) cb.getTag();
                        savedarticle.setChecked(cb.isChecked());
                    }
                });
            }
            // Reuse existing row view
            else {
                // Because we use a ViewHolder, we avoid having to call findViewById().
                articleViewHolder viewHolder = (articleViewHolder) convertView.getTag();
                checkBox = viewHolder.getCheckBox();
                textView = viewHolder.getTextView();
            }

            // Tag the CheckBox with the Planet it is displaying, so that we can
            // access the planet in onClick() when the CheckBox is toggled.
            checkBox.setTag(savedarticle);

            // Display article data
            checkBox.setChecked(savedarticle.isChecked());
            textView.setText(savedarticle.toString());

            return convertView;
        }


    }


}





