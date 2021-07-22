package info.androidhive.habadProject.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import com.az.igrot.hakodesh.R;
import info.androidhive.habadProject.activity.AsyncBook;
import info.androidhive.habadProject.activity.CreateList;
import info.androidhive.habadProject.activity.SavedArticle;

import static android.content.Context.MODE_PRIVATE;


public class ThreeFragment extends Fragment {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private ArrayList<SavedArticle> articleList;
    String currentBook = "";
    String currentArticle = "";
    private int bookOpen;
    private int articleOpen;
    private ArrayList<String[]> articles;
    String[] book_type;
    String igeretContent="";

    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CreateList booktytles = new CreateList();
        book_type = booktytles.getBook_type();
        articles = booktytles.getArticles();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_three, container, false);


        ImageButton random = (ImageButton) v.findViewById(R.id.randomIgeret);
        random.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "אגרת רנדומלית",
                        Toast.LENGTH_LONG).show();
                Random r = new Random();
                bookOpen = r.nextInt(book_type.length);
                currentBook = book_type[bookOpen];
                articleOpen = r.nextInt(articles.get(bookOpen).length);
                currentArticle = articles.get(bookOpen)[articleOpen];
//                System.out.println("random book " + bookOpen + "random article " + articleOpen);
                getByRequest(String.valueOf(bookOpen + 1), String.valueOf(articleOpen + 1));
            }
        });


        ImageButton saveIgeret = (ImageButton) v.findViewById(R.id.saveIgeret);
        saveIgeret.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "האגרת נוספה ל-'האגרות שלי",
                        Toast.LENGTH_LONG).show();

                SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                SharedPreferences preferences = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String savedArticles = preferences.getString("savedArticles", null);
                if (savedArticles != null) {
                    Type listType = new TypeToken<ArrayList<SavedArticle>>() {
                    }.getType();
                    articleList = new Gson().fromJson(savedArticles, listType);

                } else {
                    articleList = new ArrayList<SavedArticle>();
                }
                articleList.add(new SavedArticle(currentBook, currentArticle,igeretContent, bookOpen, articleOpen));
                Gson gson = new Gson();
                String articleJSON = gson.toJson(articleList);
                editor.putString("savedArticles", articleJSON);
                editor.commit();
            }
        });

        ImageButton addToWhatsApp = (ImageButton)  v.findViewById(R.id.shareWhatsApp);

        addToWhatsApp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, igeretContent);
                try {
                    getActivity().startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "Whatsapp have not been installed.",
                            Toast.LENGTH_LONG).show();

                }
            }
        });

        return v;
    }

    public void setText(String text) {
        TextView textView = (TextView) getView().findViewById(R.id.allBooksTextFragment3);
//        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/webfont.ttf");
//        textView.setTypeface(font);
        textView.setText(text);
    }

    public void getByRequest(String bookId, String volumeId) {


        try {

            igeretContent = new AsyncBook().execute("HTTP Request to my server").get();

            setText(igeretContent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


}
