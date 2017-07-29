package bizzo0_munro.rushdroid.pile_de_coups;

public class PileDeCoups {
    private String pile;
    private int id;

    public PileDeCoups(int id){
        this.id = id;
        pile = "";
    }

    public void ajouterCoup(char c){
        int index = 0;
        boolean onADejaRemplaceUneLettre = false;

        switch(c){
            case 'f':
                for (char lettre : pile.toCharArray()) {
                    if (lettre == 'b') {
                        pile = pile.substring(0, index) + pile.substring(index+1);
                        onADejaRemplaceUneLettre = true;
                        break;
                    }
                    index++;
                }
                break;
            case 'b':
                for (char lettre : pile.toCharArray()) {
                    if (lettre == 'f') {
                        pile = pile.substring(0, index) + pile.substring(index + 1);
                        onADejaRemplaceUneLettre = true;
                        break;
                    }
                    index++;
                }
                break;
        }

        if (!onADejaRemplaceUneLettre)
            pile += c;
    }

    public int getId(){
        return id;
    }

    public String getPile(){
        return pile;
    }

    public String toString(){
        return String.format("%d-%s", id, pile);
    }
}
