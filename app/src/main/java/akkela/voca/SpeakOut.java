package akkela.voca;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;


public class SpeakOut extends ActionBarActivity {
    int id;
    ArrayList arrAsk=new ArrayList();
    ArrayList arrAnsw=new ArrayList();
    InputStream input;
    Intent intent;
    String modTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_speak_out, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
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
    public void onBtnClick(View view){
        switch (view.getId()){
            case R.id.Speakout6:
                input = getResources().openRawResource(R.raw.speakout6);
                modTitle="Speakоut 6.";
                startTest();
                break;
            case R.id.Speakout9:
                input = getResources().openRawResource(R.raw.speakout9);
                modTitle="Speakоut 9.";
                startTest();
                break;
            case R.id.Speakout10:
                input = getResources().openRawResource(R.raw.speakout10);
                modTitle="Speakоut 10.";
                startTest();
                break;
            case R.id.Speakout1:
                input = getResources().openRawResource(R.raw.speakout1);
                modTitle="Speakоut 1.";
                startTest();
                break;
            case R.id.Speakout2:
                input = getResources().openRawResource(R.raw.speakout2);
                modTitle="Speakоut 2.";
                startTest();
                break;
            case R.id.Speakout3:
                input = getResources().openRawResource(R.raw.speakout3);
                modTitle="Speakоut 3.";
                startTest();
                break;
            case R.id.Speakout4:
                input = getResources().openRawResource(R.raw.speakout4);
                modTitle="Speakоut 4.";
                startTest();
                break;
            case R.id.Speakout5:
                input = getResources().openRawResource(R.raw.speakout5);
                modTitle="Speakоut 5.";
                startTest();
                break;
            case R.id.Speakout7:
                input = getResources().openRawResource(R.raw.speakout7);
                modTitle="Speakоut 7.";
                startTest();
                break;
        }
    }
    public void startTest(){
        Scanner scanner = new Scanner(input).useDelimiter("\\A");
        String string = scanner.hasNext() ? scanner.next() : null;
        scanner.close();
        ArrayList<String> arrline = new ArrayList<String>(Arrays.asList(string.split("[\r\n]+")));
        Collections.shuffle(arrline);
        if (arrline.size()>0){
            String split[];
            for (int i=0; i<arrline.size();i++){
                split=arrline.get(i).split(":");
                if(split.length==2){
                    arrAsk.add(split[0]);
                    arrAnsw.add(split[1]);
                }
            }
        }
        intent = new Intent(this, test.class);
        intent.putExtra("ask",arrAsk);
        intent.putExtra("answ",arrAnsw);
        intent.putExtra("modTitle",modTitle);
        startActivity(intent);
    }
}
