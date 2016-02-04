# Utilisation de l'API

La méthode `goOAuth` présentée fonctionne en deux étapes. 

Premièrement, une URL est générée et permet de générer un code à partir des variables propres à l'application: le *client_id*, le *client_secret* et le *redirect_uri*. Ainsi au démarrage de l'application, un message apparaît en console, présentant un lien. Il est nécessaire de cliquer sur ce lien pour donner les droits à l'application de lire des informations en tant qu'utilisateur de la plate-forme Linkedin. Le navigateur doit alors rediriger vers une page bidon dans laquelle on trouve, en fouillant dans les requêtes, une réponse json contenant une valeur `code` qui peut alors être copié dans la console qui attend un input utilisateur.

Ce code est dans un second temps utilisé par l'application pour générer un token. Ce token est valable 60 jours et permet ainsi à l'application d'agir en tant que l'utilisateur qui a donné les droits. Ce token sera écrit dans un fichier dans `src/main/resources/token`. Il pourra alors être utilisé pour toutes les requêtes vers Linkedin.