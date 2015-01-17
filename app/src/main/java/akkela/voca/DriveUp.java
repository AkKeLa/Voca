package akkela.voca;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class DriveUp extends ActionBarActivity {
    int id;
    ArrayList arrAsk=new ArrayList();
    ArrayList arrAnsw=new ArrayList();
    InputStream input;
    Intent intent;
    String modTitle;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_up);
        preferences = getSharedPreferences("temp", getApplicationContext().MODE_PRIVATE);

        ViewGroup viewG = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_drive_up, null);

            for(int i=0; i < viewG.getChildCount(); i++)
            {
                View v = viewG.getChildAt(i);
                if (v instanceof Button) {
                    Button b = (Button) v;
                    b.setText(b.getText()+" " + preferences.getInt((String) b.getText(),0)+"%");
                }
            }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_drive_up, menu);
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
            case R.id.DriveUp5:
                input = getResources().openRawResource(R.raw.driveup5);
                modTitle="Drive Up 5.";
                startTest();
                break;
            case R.id.DriveUp6:
                input = getResources().openRawResource(R.raw.driveup6);
                modTitle="Drive Up 6.";
                startTest();
                break;
            case R.id.DriveUp7:
                input = getResources().openRawResource(R.raw.driveup7);
                modTitle="Drive Up 7.";
                startTest();
                break;
            case R.id.DriveUp8:
                input = getResources().openRawResource(R.raw.driveup8);
                modTitle="Drive Up 8.";
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
