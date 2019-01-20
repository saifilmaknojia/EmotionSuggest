package emotionapi.suggest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Suggestions extends AppCompatActivity {
    static  HashMap<String,String> musicMap = new HashMap<String, String>();

    private String url1;
    private String url2;
    private String url3;

    private String title1;
    private String title2;
    private String title3;

    TextView news1;
    TextView news2;
    TextView news3;

    static {
        musicMap.put("anger","https://open.spotify.com/playlist/37i9dQZF1DX1s9knjP51Oa");
        musicMap.put("contempt","https://open.spotify.com/playlist/37i9dQZF1DX1OY2Lp0bIPp");
        musicMap.put("disgust","https://open.spotify.com/playlist/37i9dQZF1DWUbycBFSWTh7");
        musicMap.put("fear","https://open.spotify.com/playlist/37i9dQZF1DX4fpCWaHOned");
        musicMap.put("happiness","https://open.spotify.com/playlist/37i9dQZF1DX7F6T2n2fegs");
        musicMap.put("neutral","https://open.spotify.com/playlist/37i9dQZF1DWXmlLSKkfdAk");
        musicMap.put("sadness","https://open.spotify.com/playlist/37i9dQZF1DXdxcBWuJkbcy");
        musicMap.put("surprise","https://open.spotify.com/playlist/37i9dQZEVXbLRQDuF5jeBp");
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggestions_layout);

        news1 = (TextView) findViewById(R.id.news1);

        news2 = (TextView) findViewById(R.id.news2);

        news3 = (TextView) findViewById(R.id.news3);
    }
    public void openMusic(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(musicMap.get(MainActivity.maxEmotion))));
    }

    public void getNews(View view) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://newsapi.org/v2/everything?" +
                "q=" + "trending " + MainActivity.emotionMap.get(MainActivity.maxEmotion) +
                "&sortBy=popularity&" +
                "apiKey="+R.string.news_api;

        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String myResponse = response.body().string();
                    Log.d("response",myResponse);

                    JSONObject obj = null;
                    // convert the string to JSONArray
                    try {
                        obj = new JSONObject(myResponse);
                        JSONArray articles = obj.getJSONArray("articles");
                        JSONObject article1 = articles.getJSONObject(0);
                        title1 = article1.getString("title");
                        url1 = article1.getString("url");

                        JSONObject article2 = articles.getJSONObject(1);
                        title2 = article2.getString("title");
                        url2 = article2.getString("url");

                        JSONObject article3 = articles.getJSONObject(2);
                        title3 = article3.getString("title");
                        url3 = article3.getString("url");

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                news1.setText(title1);

                                news2.setText(title2);

                                news3.setText(title3);

                            }
                        });



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void getNews1(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url1)));
    }

    public void getNews2(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url2)));
    }

    public void getNews3(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url3)));
    }

    public void openVideo(View view) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=" +
                "trending " + MainActivity.emotionMap.get(MainActivity.maxEmotion) + " videos" + " english" +
                "&type=playlist&key="+R.string.youtube_data_api;

        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String myResponse = response.body().string();
                    Log.d("response",myResponse);

                    JSONObject obj = null;
                    // convert the string to JSONArray
                    try {
                        obj = new JSONObject(myResponse);
                        JSONArray items = obj.getJSONArray("items");
                        JSONObject itemOb = items.getJSONObject(0);
                        JSONObject id = itemOb.getJSONObject("id");
                        String playListId = id.getString("playlistId");

                        String playlistLink = "https://www.youtube.com/playlist?list=" + playListId;
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(playlistLink)));



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }
}
