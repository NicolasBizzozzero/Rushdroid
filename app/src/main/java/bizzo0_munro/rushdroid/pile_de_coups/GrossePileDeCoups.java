package bizzo0_munro.rushdroid.pile_de_coups;

import java.util.ArrayList;

public class GrossePileDeCoups {
    private ArrayList<PileDeCoups> grossePileDeCoups;

    public GrossePileDeCoups(){
        grossePileDeCoups = new ArrayList<>();
    }

    public PileDeCoups retirerDernierCoupJoue(){
        int tailleListe = grossePileDeCoups.size();

        if (tailleListe > 0){
            return grossePileDeCoups.remove(tailleListe-1);
        }

        return null;
    }

    public void ajouterCoup(PileDeCoups coup){
        grossePileDeCoups.add(coup);
    }

    public String toString(){
        int taillePile = grossePileDeCoups.size();
        if (taillePile <= 0)
            return "[]";

        String s = "[";
        for (int i=0; i<taillePile-1; i++){
            s += grossePileDeCoups.get(i);
            s += ", ";
        }
        s += grossePileDeCoups.get(taillePile-1);
        s += "]";

        return s;
    }
}
