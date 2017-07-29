package bizzo0_munro.rushdroid;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class MaProgressionDeJeu {
    private Set<String> achievementsDebloquesMaisPasSynchronises;
    private SharedPreferences preferencesAchievements;

    public MaProgressionDeJeu(Context activityContext){
        preferencesAchievements = activityContext.getSharedPreferences("Achievements", Context.MODE_PRIVATE);
        achievementsDebloquesMaisPasSynchronises = preferencesAchievements.getStringSet("AchievementsDebloquesMaisPasSynchronises", null);
    }

    public boolean ilResteDesAchievementsASynchroniser(){
        if (achievementsDebloquesMaisPasSynchronises == null)
            return false;
        else if (achievementsDebloquesMaisPasSynchronises.size() == 0)
            return false;

        return true;
    }

    public void sauvegarderAchievementPourLeSynchroniserPlusTard(String achievementID){
        achievementsDebloquesMaisPasSynchronises.add(achievementID);
        SharedPreferences.Editor editeurPreferencesAchievements = preferencesAchievements.edit();
        editeurPreferencesAchievements.putStringSet("AchievementsDebloquesMaisPasSynchronises", achievementsDebloquesMaisPasSynchronises);
    }

    public Set<String> getAchievementsDebloquesMaisPasSynchronises() {
        return achievementsDebloquesMaisPasSynchronises;
    }
}
