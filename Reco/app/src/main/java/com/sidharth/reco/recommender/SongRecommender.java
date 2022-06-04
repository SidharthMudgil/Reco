package com.sidharth.reco.recommender;

import android.app.ProgressDialog;
import android.content.Context;

import com.arasthel.asyncjob.AsyncJob;
import com.sidharth.reco.chat.model.SongModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

public class SongRecommender {
    private static JSONArray songsData;
    private static final int MOOD_HAPPY = 0;
    private static final int MOOD_CALM = 1;
    private static final int MOOD_ANXIOUS = 2;
    private static final int MOOD_ENERGETIC = 3;

    public static void initializeSongData(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("Initializing Reco");
        dialog.setMessage("Please wait while reco is getting ready");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        new AsyncJob.AsyncJobBuilder<Boolean>()
                .doInBackground(() -> {
                    try {
                        songsData = loadJSONFromAssets(context);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                })
                .doWhenFinished(o -> {
                    if (songsData == null) {
                        throw new RuntimeException("Array List Empty");
                    } else {
                        dialog.dismiss();
                    }
                }).create().start();
    }

    private static JSONArray loadJSONFromAssets(Context context) {
        try {
            InputStream is = context.getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            //noinspection ResultOfMethodCallIgnored
            is.read(buffer);
            is.close();
            String data = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(data);
            return jsonObject.getJSONArray("results");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getMoodSong(int mood) {
        switch (mood) {
            case MOOD_HAPPY:
                return "785P76EJxDydMu7a2IZRHR";
            case MOOD_CALM:
                return "1FOEQiszPh2XuMuaY4ZIUq";
            case MOOD_ANXIOUS:
                return "3DgviHZKJ8Xf385NqwwwK8";
            case MOOD_ENERGETIC:
                return "2lda3NIlkKYkZsU5eai2Ve";
            default:
                return getNewSong();
        }
    }

    public static String getNewSong() {
        int totalSongs = 50014;
        try {
            JSONObject song = songsData.getJSONObject(new Random().nextInt(totalSongs));
            return song.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<String> getSimilarSongs(SongModel songModel) {
        ArrayList<String> ids = new ArrayList<>();
        ids.add(songModel.getImgID());
        ids.add("0kmOFBPszGiU5UiERMN9ph");
        ids.add("749FLa24QNwTmCF7MRsZ4m");
        ids.add("75AMWT7xTcueDzIuX6p0Nt");
        ids.add("4dPVmeisPfQrLcjx0Wz1KW");
        return ids;
    }
}
