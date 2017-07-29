package bizzo0_munro.rushdroid;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;

public class MonBuilderDeFenetreDeDialogue extends AlertDialog.Builder {

    public MonBuilderDeFenetreDeDialogue(Context context) {
        super(new ContextThemeWrapper(context, R.style.MonAlertDialog));

        this.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                ClassePrincipale app = (ClassePrincipale) (getContext().getApplicationContext());

                /* On utilise ce booleen car lors d'une rotation de l'ecran, la fenetre de dialogue
                   est automatiquement detruite. Il permet alors une simple verification pour
                   la réafficher si besoin est. */
                app.setUneFenetreDeMessageEstAffichee(false);
                dialog.dismiss();
            }
        });
    }
}
