Propositions d'implementations :
 * Ajouter une fonction de pause qui bloque la vue du puzzle mais arrête le temps.
 * Implementer des achievements et la possibilité de se connecter au Google play pour synchroniser ses achievements.
 * Ajouter des boutons pour nous suivre sur les reseaux sociaux (g+ facebook twitter).
 * Ajouter des boutons pour nous envoyer des dons.
 * Ajouter une animation de chargement et une animation de changement d'activité (avec un glissement).
* Convertir les scores en long int au lieu de int pour eviter un depassement de capacité.
* J'ai fait des recherches et commencé à ecrire plusieurs pseudo-algorithmes pour faire un solveur et pouvoir implementer une fonctionnalité de création de niveau ainsi que d'obtenir les coups minimaux pour chaque puzzle. 

Bugs à corriger :
 * Problème d'affichage que j'ai """resolu""" comme je le pouvais. Il se produit sur l'activité Jeu enn mode landscape sur les tablettes, lorsqu'on lance l'application en mode portrait puis qu'on passe en mode landscape sur le jeu. Le canvas etait coupé vers le bas, il est maintenant surelevé. Je suppose que ce problème d'affichage est du à le présence de Navbar mais je n'en suis pas sur.
 * Utiliser un service pour lancer/couper la musique ? Car elle se coupe un peu entre les activités
* J'ai ajouté le jour du rendu des boutons undo et restart. Restart fonctionne correctement, mais undo fait buguer le jeu. De plus, je ne suis pas satisfait de leur apparence.

Fonctionnalités qui peuvent vous echapper:
* L'application est par défaut, entierrement en anglais. Des traductions françaises, espagnoles et turques se chargeront automatiquement selon la langue du telephone de l'utilisateur.
* L'application se trouve en ligne sur le play store sous le nom de "RushDroid" avec pour nom de société "Bizzozzéro Nicolas". Sa page contient une traduction en anglais et en français ainsi que plusieurs captures d'ecrans. L'avis de certains utilisateurs m'a permis de me tenir au courant de plusieurs bugs d'affichage qui ont été corrigés par la suite, et j'ai ajouté une fonctionnalité supplementaire (le bouton suivant sur l'activité de fin de jeu) suite à la demande d'un utilisateur.
* L'application ne peut volontairement pas être installée sur les Android Watch (croyez-moi, c'était très moche).

Commentaires supplementaires :
* J'ai corrigé le plus de warnings (indiqués par AndroidStudio) possibles
* Je n'utilise aucune ressource trouvée sur internet sans en créditer son auteur et sans m'être assuré qu'elles étaient libres de droits
* Je n'ai pas réussi à utiliser l'API GooglePlay pour pouvoir permettre aux joueurs d'utiliser un classement et des achievements. Toutefois, les classements et achievements ont deja été créés sur mon compte Google Developer et j'ai recupéré leur ID.
