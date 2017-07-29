package bizzo0_munro.rushdroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;

public class MenuPrincipal extends MonAppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
    }

    public void onClickGoToSelectionDesNiveaux(View v) {
        Intent intent = new Intent(this, SelectionDesNiveaux.class);
        startActivity(intent);
        finish();
    }

    public void onClickGoToStatistiques(View v) {
        Intent intent = new Intent(this, Statistiques.class);
        startActivity(intent);
        finish();
    }

    public void onClickGoToOptions(View v) {
        Intent intent = new Intent(this, Options.class);
        startActivity(intent);
        finish();
    }

    public void onClickGoToMeilleursScores(View v) {
        int duration = Toast.LENGTH_LONG;
        String texte = getString(R.string.fonctionindisponible);

        Toast toast = Toast.makeText(app, texte, duration);
        toast.show();

        /*Intent intent = new Intent(this, MeilleursScores.class);
        startActivity(intent);
        finish();*/
    }

    public void onClickGoToSucces(View v) {
        int duration = Toast.LENGTH_LONG;
        String texte = getString(R.string.fonctionindisponible);

        Toast toast = Toast.makeText(app, texte, duration);
        toast.show();

        /*Intent intent = new Intent(this, Succes.class);
        startActivity(intent);
        finish();*/
    }

    public void onClickExit(View v){
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            super.onDestroy();
        }

        return super.onKeyDown(keyCode, event);
    }
}
