package info.androidhive.habadProject.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import com.az.igrot.hakodesh.R;
import info.androidhive.habadProject.activity.AsyncBook;
import info.androidhive.habadProject.activity.CreateList;
import info.androidhive.habadProject.activity.SavedArticle;

import static android.content.Context.MODE_PRIVATE;


public class OneFragment extends Fragment {
    private ArrayList<SavedArticle> articleList ;
    String [] book_type ;
    private ArrayList<String[]> articles;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    ArrayAdapter bookAdapter,igeretAdapter;
    Spinner bookSpinner,valueSpinner;
    TextView bookSpinnerTextView , valueSpinnerTextView , shareWhatsappTextVview, favoriteTextView;
    String currentBook = "";
    String currentArticle = "";
    private int bookOpen ;
    private int articleOpen ;
    private String igeretContent="";
    TextView igeretTextView;


    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CreateList booktitles=new CreateList();
        book_type=booktitles.getBook_type();
        articles=booktitles.getArticles();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_one, container, false);

        bookSpinner = (Spinner) v.findViewById(R.id.book_spinner);
        valueSpinnerTextView = (TextView) v.findViewById(R.id.valueSpinnerTextView);
        bookSpinnerTextView = (TextView) v.findViewById(R.id.bookSpinnerTextView);
        valueSpinner = (Spinner) v.findViewById(R.id.igeret_spinner);
        bookAdapter = new ArrayAdapter <String> (this.getActivity(),R.layout.spinner_item_color, book_type);
        bookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookSpinner.setAdapter(bookAdapter);
        bookSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if (adapterView.getSelectedItem().equals(book_type[i])) {

                    igeretAdapter = new ArrayAdapter  <String>(getActivity(),R.layout.spinner_item_color, articles.get(i));
                    igeretAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    valueSpinner.setAdapter(igeretAdapter);
                    bookOpen=i;
                    currentBook = book_type[i];


                }
                else onNothingSelected(adapterView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        valueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getSelectedItem().equals(articles.get(bookOpen)[i])) {
                    currentArticle = articles.get(bookOpen)[i];
                    articleOpen = i;
                    getByRequest(String.valueOf(bookOpen+1),String.valueOf(i+1));


                }
                else onNothingSelected(adapterView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        ImageButton myFav = (ImageButton)  v.findViewById(R.id.addTofavorites);

        myFav.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "האגרת נוספה ל-'האגרות שלי'",
                        Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                SharedPreferences preferences = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String savedArticles = preferences.getString("savedArticles", null);
                if(savedArticles!=null) {
                    Type listType = new TypeToken<ArrayList<SavedArticle>>() {
                    }.getType();
                    articleList = new Gson().fromJson(savedArticles, listType);

                }
                else{
                    articleList=new ArrayList<SavedArticle>();
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



    public void setText(String text){
        TextView igeretTextView = (TextView) getView().findViewById(R.id.allBooksText);
      //  Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/webfont.ttf");
        //textView.setTypeface(font);
        igeretTextView.setText(text);
    }

    public void getByRequest(String bookId,String volumeId){


        try {

            igeretContent =new AsyncBook().execute("https://www.drone-programming.online/api.php?" + "bookId=" + bookId + "&volumeId=" + volumeId).get();
            setText(igeretContent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

}
