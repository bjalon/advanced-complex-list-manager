Créer dans le projet Studio un schéma erdf_notes avec 2 champs de liste de complexes note_non_securisee et note_securisee. Par exemple, un sous-champ note, auteur, date_creation, ...
Créer ensuite un type documentaire HiddenNote avec le nom de schéma associé erdf_note créable nulle part. Dans le schéma, donner les mêmes nom et les mêmes type documentaire associés que dans le champ complexe. (les schéma xsd sont donnés dans les sources à la racine du projet.

Définir le layout d'édition qui sera utilisé de formulaire de créeation d'une nouvelle note

Créer un layout Note (que vous pourrez ensuite dans tous vos type documentaire pour peu que celui-ci implémente le schéma erdf_notes. Dans ce layout créer deux champs de saisie pour les champs avec le widget list mais mettre les sous-widget en read only et retirer la possibiilité d'ajouter et activer la possibilité de réordonner. Dans le même layout ajouter un widget template avec le xhtml donné dans la racine de ce projet.

Et voilà.

Cela donne la copie d'écran également à la racine de ce projet.

*Résumé :*

Donc pour ajouter la notion de notes sécurisées, il faut
* Ajouter le schéma erdf_notes (avec un s) sur le ou les types documentaire
* Ajouter un tab ou ajouter dans les layout du/des documents en s’appuyant sur le layout Note

Pour faire évoluer le schéma :
* La définition du formulaire de création d’une seule note => tout est dans ERDFNote + l’event handler de mise à jour automatique de la date/créateur
Pour faire évoluer la vue et la gestion de la liste des notes :
* Modifier le schéma erdf_note (Sans s)
* Modifier le layout Note


Deux groupes définissent les droits sur les notes :
* vue des notes privées
* l’édition des notes
Par défaut les groupes affectés sont administrators.
Pour changer, créer un fichier linky.properties dans nxserver/config/linky.note.properties et dedans saisir les bons nombres de groupes:
org.nuxeo.erdf.linky.secured.notes.group.access=nomDuGroupe
org.nuxeo.erdf.linky.secured.notes.group.manage=nomDuGroupe
