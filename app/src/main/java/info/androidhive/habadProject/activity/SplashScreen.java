package info.androidhive.habadProject.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import javax.net.ssl.HttpsURLConnection;
import com.az.igrot.hakodesh.R;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new SplashScreen.DateAsyncTask().execute("https://www.hebcal.com/converter/?cfg=json&gy=" + year + "&gm=" + month + "&gd=" + day + "&g2h=1");

    }


    Calendar cal = Calendar.getInstance();
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int year = Calendar.getInstance().get(Calendar.YEAR);
    int month = Calendar.getInstance().get(Calendar.MONTH) + 1;


    public class DateAsyncTask extends AsyncTask<String, String, String> {
        ProgressDialog PD;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PD = new ProgressDialog(SplashScreen.this);
            PD.setTitle("Please wait");
            PD.setMessage("מחפש חיבור רשת");
            PD.setCancelable(false);
            PD.show();

        }

        @Override
        protected String doInBackground(String... Url) {
            try {

                HttpsURLConnection con = null;
                try {
                    URL u = new URL(Url[0]);
                    con = (HttpsURLConnection) u.openConnection();

                    con.connect();


                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();

                    JSONObject reader = new JSONObject(sb.toString());
                    String date = reader.getString("hebrew");
                    MainClass.currentDate =date;


                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    if (con != null) {
                        try {
                            con.disconnect();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }


            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String args) {

                PD.dismiss();
                Intent in = new Intent(getApplicationContext(), MainClass.class);
                startActivity(in);
            }


        }


    }

