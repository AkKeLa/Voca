package akkela.voca;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


public class test extends ActionBarActivity implements
        TextToSpeech.OnInitListener{
    int myId=0;
    ArrayList <String> arrAsk=new ArrayList();
    ArrayList arrAnsw=new ArrayList();
    String modTitle;
    Intent intent;
    Locale locUk=Locale.UK;
    ProgressBar progressBar;

    private TextToSpeech ttsUk;
    private TextToSpeech ttsRu;
    private Button btnSpeak;
    private EditText txtText;
    private boolean checkLang=true;
    String ask="";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("temp", getApplicationContext().MODE_PRIVATE);
        editor = preferences.edit();
        Bundle extra = getIntent().getExtras();

        arrAsk=(ArrayList)extra.get("ask");
        arrAnsw=(ArrayList)extra.get("answ");
        modTitle=(String)extra.get("modTitle");
        setTitle(modTitle);
        setContentView(R.layout.activity_test);
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                speakOut();
            }
        });
        final CheckBox check=(CheckBox)findViewById(R.id.autoplay);
        check.setChecked(preferences.getBoolean("autoplay",false));
        check.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editor.putBoolean("autoplay",check.isChecked());
                editor.commit();
            }
        });
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setMax(arrAnsw.size());
        ttsUk = new TextToSpeech(this, this);
 //       ttsUk.setLanguage(locUk);
        setParam();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_En_Ru:
                ArrayList change = arrAsk;
                arrAsk=arrAnsw;
                arrAnsw=change;
                //get check + imgbtn
                ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
                CheckBox checkBox = (CheckBox)findViewById(R.id.autoplay);
                if(imageButton.getVisibility()!=View.VISIBLE){
                    imageButton.setVisibility(View.VISIBLE);
                    checkBox.setVisibility(View.VISIBLE);
                } else {
                    imageButton.setVisibility(View.INVISIBLE);
                    checkBox.setVisibility(View.INVISIBLE);
                }
                checkLang=!checkLang;
                setParam();
                break;
            case R.id.menu_DriveUp:
                intent = new Intent(this, DriveUp.class);
                startActivity(intent);
                break;
            case R.id.menu_SpeakOut:
                intent = new Intent(this, SpeakOut.class);
                startActivity(intent);
                break;
            case R.id.menu_exit:
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_HOME);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList get4answ(){
        ArrayList draft=new ArrayList();
        draft.add(arrAnsw.get(myId));

        ArrayList copy = (ArrayList)arrAnsw.clone();
        Collections.shuffle(copy);
        int ii=0;
        for (int i=0;i<3;i++){
            if (copy.get(ii)!= arrAnsw.get(myId)) {
                draft.add(copy.get(ii));
            }
            else i--;
            ii++;
        }
        Collections.shuffle(draft);
        return draft;
    }
    public void setParam(){
        if (myId<arrAnsw.size()){
            ArrayList neu = get4answ();
            TextView txtView = (TextView) findViewById(R.id.ask);
            txtView.setText((String) arrAsk.get(myId));
            Button btn1 = (Button) findViewById(R.id.answ1);
            btn1.setText((String) neu.get(0));
            Button btn2 = (Button) findViewById(R.id.answ2);
            btn2.setText((String) neu.get(1));
            Button btn3 = (Button) findViewById(R.id.answ3);
            btn3.setText((String) neu.get(2));
            Button btn4 = (Button) findViewById(R.id.answ4);
            btn4.setText((String) neu.get(3));
            progressBar.setProgress(myId+1);
            CheckBox check=(CheckBox)findViewById(R.id.autoplay);
            if(check.isChecked()) {
                speakOut();
            }
        } else {
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.applause);
            mp.start();
            saveResults();
            new AlertDialog.Builder(this)
                    .setTitle("Done")
                    .setMessage("Test competed.")
                    .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            myId=0;
                            setParam();
                        }
                    })
                    .setNegativeButton("Choose another one", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            intent = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    private void saveResults() {
        editor.putInt((String)getTitle(),100);
        editor.commit();
    }

    public void onBtnClick(View view) throws InterruptedException {
        final Button checkBtn = (Button) findViewById(view.getId());
        final Drawable defa = checkBtn.getBackground();
        if(view.getId()==R.id.answ1 || view.getId()==R.id.answ2 || view.getId()==R.id.answ3 || view.getId()==R.id.answ4){
            if(checkBtn.getText()==arrAnsw.get(myId)){
                checkBtn.setBackgroundColor(Color.GREEN);
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        checkBtn.setBackground(defa);
                        myId++;
                        setParam();
                    }};
                Handler mHandler = new Handler();
                mHandler.postDelayed(r, 1000);
            } else{
                if(ask!=arrAsk.get(myId)){
                    ask=arrAsk.get(myId);
                    arrAsk.add(ask);
                    arrAnsw.add(arrAnsw.get(myId));
                    progressBar.setMax(arrAsk.size());
                }
                checkBtn.setBackgroundColor(Color.RED);
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        checkBtn.setBackground(defa);
                    }};
                Handler mHandler = new Handler();
                mHandler.postDelayed(r, 500);
            }
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = ttsUk.setLanguage(Locale.UK);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
               // speakOut();
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }
    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (ttsUk != null) {
            ttsUk.stop();
            ttsUk.shutdown();
        }

        super.onDestroy();
    }
    private void speakOut() {
            ttsUk.speak(arrAsk.get(myId), TextToSpeech.QUEUE_FLUSH, null);
    }
}