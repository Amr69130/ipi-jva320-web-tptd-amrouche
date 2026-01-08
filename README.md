# TD et TP IPI JVA320 - Application web Java (Servlet, Spring MVC, Thymeleaf)

Ce projet est un début d'application web de gestion de salariés aide à domiciles.
Il permet de réaliser les exercices en séance (TD) et exercices de l'évaluation (TP) du cours.
L'énoncé de ces exercices est dans la page web accessible à http://localhost:8080/home.html
une fois l'application démarrée ou sinon [src/main/resources/templates/home.html].
L'énoncé d'un exercice de TP est préfixé par "TP". Les exercices optionnels sont aussi préfixés par "BONUS".

## Pré-requis

- Java JDK (et non JRE, il faut le compilateur !)
  - préférer la version Open Source la plus répandue à savoir Temurin, celle de la fondation Eclipse : https://adoptium.net/ (celle d'Oracle a désormais une licence payante en production...)
- Avoir installé un IDE :
    - IntelliJ Ultimate, avec votre adresse IPI sur Jetbrains Student à https://www.jetbrains.com/student/
    - sinon Eclipse, à https://www.eclipse.org/downloads/packages/release/2022-09/r/eclipse-ide-java-developers
- Savoir utiliser Git et les branches (utilisez les capacités Git de votre IDE ou installez-le séparément depuis
  https://git-scm.com/download/win ). Quelques liens :
    - https://learngitbranching.js.org/
    - https://git-scm.com/book/en/v2/Git-Branching-Basic-Branching-and-Merging
- Avoir un compte Github. Voici comment y configurer l'authentification de git par clé SSH :
    - https://docs.github.com/en/authentication/connecting-to-github-with-ssh
    - https://docs.github.com/en/authentication/connecting-to-github-with-ssh/adding-a-new-ssh-key-to-your-github-account
- Connaître les bases du développement Java avec Maven (la persistence avec JPA est également utilisée ponctuellement),
  et au moins comment importer et compiler un projet Java dans l'IDE :
    - IntelliJ :
        - Installation de Git : Git > git not installed > Donwload and install
        - Cloner un projet Github : Git > Clone
        - Configuration d'un projet Maven : clic droit sur pom.xml > Add as Maven project ou bien voir IV-B-2 à https://damienrieu.developpez.com/tutoriel/java/nouveautes-intellij-12/?page=page_1
        - Installation de Java : par exemple
            - erreur ne trouve pas le symbol "java" : clic droit sur pom.xml > Build > sur Setup DSK choisir Configure > choisir Download et install (par exemple de la JDK Eclipse Temurin)
            - "Error running..." : Project JDK is not specified > Configure... > no SDK > Add SDK > Download
            - erreur "Cannot find JRE" : File > Project Structure (ou clic droit dans l'arborescence sur la racine d'un projet Maven), et de là dans Platform Settings > SDKs faire + > Download JDK (ou bien dans Project Settings > Project faire Add SDK > Download SDK)
            - Pour mettre à jour Java : File > Project Structure puis SDKs puis Download...
        - lancer un build maven complet : Run > Edit configurations > Maven > Create configuration > mettre Working directory au dossier du projet et dans Command line, écrire : clean install
        - problème de sécurisation de connexion car proxy :
          - unable to access 'https://github.com/mdutoo/ipi-jva350-tptd.git/': SSL certificate problem: unable to get local issuer certificate
           Dans C:\Program Files\Git\etc\gitconfig, passer sous [http] la valeur sslBackend à schannel (et non openssl), comme dit à TODO
          - Maven error : unable to find valid certification path to requested target
          mvn clean package -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true
          ou dans IntelliJ Run > Edit Configurations > Java Options (sans -D) : maven.wagon.http.ssl.insecure=true maven.wagon.http.ssl.allowall=true
          comme dit à https://stackoverflow.com/questions/45612814/maven-error-pkix-path-building-failed-unable-to-find-valid-certification-path
          - unable to find valid certification path to requested target
          Aller sur les sites nodejs.org, (npmjs. ?) et registry.npmjs.com et télécharger leurs certificats (chaînes .pem) puis importer chacune dans le cacert de votre jdk (et non jre !) :
          "C:\Users\your_user\.jdks\temurin-20.0.1\bin\keytool" -import -v -trustcacerts -alias nodejs -file "C:\Users\your_user\dev\nodejs-org-chain.pem" -keystore "C:\Users\your_user\.jdks\temurin-20.0.1\lib\security\cacerts" -keypass changeit -storepass changeit
          "C:\Users\your_user\.jdks\temurin-20.0.1\bin\keytool" -import -v -trustcacerts -alias npmjs -file "C:\Users\your_user\dev\npmjs-com-chain.pem" -keystore "C:\Users\your_user\.jdks\temurin-20.0.1\lib\security\cacerts" -keypass changeit -storepass changeit
          "C:\Users\your_user\.jdks\temurin-20.0.1\bin\keytool" -import -v -trustcacerts -alias registry-npmjs -file "C:\Users\your_user\dev\registry-npmjs-com-chain.pem" -keystore "C:\Users\your_user\.jdks\temurin-20.0.1\lib\security\cacerts" -keypass changeit -storepass changeit
            "C:\Users\your_user\.jdks\temurin-20.0.1\bin\keytool" -import -v -trustcacerts -alias nodejs -file "C:\Users\your_user\dev\nodejs-org-chain.pem" -keystore "C:\Users\your_user\dev\ideaIC-2023.1.win\jbr\lib\security\cacerts" -keypass changeit -storepass changeit
            "C:\Users\your_user\.jdks\temurin-20.0.1\bin\keytool" -import -v -trustcacerts -alias npmjs -file "C:\Users\your_user\dev\npmjs-com-chain.pem" -keystore "C:\Users\your_user\dev\ideaIC-2023.1.win\jbr\lib\security\cacerts" -keypass changeit -storepass changeit
            "C:\Users\your_user\.jdks\temurin-20.0.1\bin\keytool" -import -v -trustcacerts -alias registry-npmjs -file "C:\Users\your_user\dev\registry-npmjs-com-chain.pem" -keystore "C:\Users\your_user\dev\ideaIC-2023.1.win\jbr\lib\security\cacerts" -keypass changeit -storepass changeit
          ET rajouter les propriétés maven -Djavax.net.ssl.trustStore="C:\Users\your_user\.jdks\temurin-20.0.1\lib\security\cacerts" -Djavax.net.ssl.trustStorePassword=changeit (ou dans IntelliJ Run > Edit Configurations > Java Options, mais là aussi sans -D)
          comme dit à https://stackoverflow.com/questions/54515921/cannot-install-node-through-front-end-maven-plugin-due-to-certificate-error
          - npm ERR! code UNABLE_TO_VERIFY_LEAF_SIGNATURE
          export NODE_EXTRA_CA_CERTS="C:\Users\marc.dutoo\dev\registry-npmjs-com-chain.pem"
          PUIS exécuter maven en ligne de commande au moins la première fois, par exemple avec git bash : MinGW64 (clic droit > git bash dans l'explorateur de fichiers ici)
          export JAVA_HOME="C:\Users\marc.dutoo\.jdks\temurin-20.0.1"
          "C:\Users\marc.dutoo\dev\ideaIC-2023.1.win\plugins\maven\lib\maven3\bin\mvn" clean install -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Djavax.net.ssl.trustStore="C:\Users\your_user\.jdks\temurin-20.0.1\lib\security\cacerts" -Djavax.net.ssl.trustStorePassword=changeit -e
          comme dit à https://stackoverflow.com/questions/13913941/how-to-fix-ssl-certificate-error-when-running-npm-on-windows
          NB. par contre, configurer cafile dans ./npmrc ne marche pas car https obligé depuis October 4, 2021 comme le disent les logs

    - sinon Eclipse : voir https://thierry-leriche-dessirier.developpez.com/tutoriels/java/importer-projet-maven-dans-eclipse-5-min/
      - mettre à jour java : télécharger la dernière distribution OpenJDK par exemple d'Eclipse (Temurin) à https://www.adoptium.net , puis le sélectionner comme défaut dans Windows > Preferences, puis si nécesssaire le mettre à jour
- Avoir installé postgresql (ou mysql) : https://www.postgresql.org/download/

## Créer la base de données

### H2 (par défaut)

Par défaut, l'application se lance avec la base de données embarquée en mémoire H2.
Comme il n'y a rien à faire pour cela, il est conseillé de commencer comme cela.
L'inconvénient, outre que cela est moins réaliste, est que les données rajoutées
disparaissent à chaque démarrage, à part quelques données de test rajoutées
à l'initialisation.

### PostgreSQL par image Docker

Pour lancer PostgreSQL dans une image Docker à l'aider du docker-compose.yml fourni :

    docker-compose up -d

NB. il est nécessaire de commenter les services autres que "db" si on souhaite démarrer les différents microservices directement en Java plutôt que dans Docker.

### PostgreSQL (base de données à créer)

Installer PostgreSQL : https://dbeaver.io/download/

Exécuter les lignes de commande plus bas,
- soit dans un outil à installer tel DBeaver : https://dbeaver.io/download/
- soit dans un terminal.

Créer l'utilisateur "ipi" :

en tant qu'administrateur (sous Windows : recherche "cmd" dans les applications et dessus clic droit > "Run as admin", sous linux : sudo su - postgres) :

    $> psql
    $postgresql> create user ipi with password 'ipi' createdb;
    $postgresql> \q

Créer la base de données "ipi_jva320_web" :

	$> psql -U ipi postgres -h localhost
	$postgresql> create database ipi_jva320_web encoding 'UTF8';
    $postgresql> \q

Vérifier que l'utilisateur créé peut bien se connecter à cette base :

	$> psql -U ipi ipi_jva320_web -h localhost

Configurer l'application pour s'en servir :
- dans ```main/resources/application.properties```, décommenter les lignes sous "postgresql - clean setup" et commenter les lignes à propos de H2.
- dans ```pom.xml```, commenter la dépendance à H2 (ou la passer en ```<scope>test</scope>```).


## Exécution

lancer la classe com.ipi.jva320.Jva320Application
- dans l'IDE
  - IntelliJ : l'ouvrir et cliquer sur la flèche verte sur sa gauche
  - Eclipse : clic droit > Run as application),
- avec maven (IDE ou ligne de commande) : ```mvn spring-boot:run```

Puis pointer le navigateur web à http://localhost:8080/ , ou pour afficher une page sans rendu Thymeleaf par exemple http://localhost:8080/home.html (qui contient l'énoncé des exercices à réaliser).

FAQ :
- erreur au démarrage "Cannot find template location: classpath:/templates/" => dans IntelliJ : clic droit sur projet > Add as Maven project


## Développement

Voici l'organisation du code source de l'application :
- code Java de l'application : dans le package com.ipi.jva320
- Controllers Spring MVC (à développer) : dans le sous-package web
- template Thymeleaf (à compléter) : dans main/resources/templates
- configuration : main/resources/application.properties