
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('201', 'PRESSE_ERREUR_DEFAUT', 'Erreur defaut', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('202', 'RESSE_FICHIER_ABSENT_IN', 'Fichier in absent', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('203', 'PRESSE_FICHIER_ABSENT_OUT', 'Fichier out absent', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('204', 'PRESSE_REPERTOIRE_ABSENT_IN', 'Répertoire in absent', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('205', 'PRESSE_REPERTOIRE_ABSENT_OUT', 'Répertoire out absent', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('206', 'PRESSE_NUMERO_PUBLICATION_ABSENT', 'numéro publication absent', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('207', 'PRESSE_NUMERO_PUBLICATION_INCORRECT', 'numéro publication incorrect', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('208', 'PRESSE_SCRIPT_ABSENT', 'Script absent', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('209', 'PRESSE_SCRIPT_ERREUR_COMPILATION', 'Erreur compilation script', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('210', 'AUTO_TEST_EN_ERREUR_CONTENU', 'Erreur contenu auto tes', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('211', 'AUTO_TEST_EN_ERREUR_TEMPS', 'Erreur temps auto tes', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('212', 'AUTO_TEST_OK', 'Auto test ok', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('250', 'PRESSE_ALERTE_GENERALE', 'Alerte générale', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('300', 'PRESSE_LOG_DEFAUT', 'Log par defaut', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('301', 'PRESSE_REMISE_EN_ATTENTE', 'Remise en attente', 'LOG');

INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('500', 'PF_TELECHARGEMENT_OK_', 'Fin du téléchargement', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('505', 'PF_LISTAGE_OK', 'Listage FTP OK', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('510', 'PF_TELECHARGEMENT_EN_COURS_', 'Téléchargement en cours', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('520', 'PF_TELECHARGEMENT_ERREUR_SITE_NON_TROUVE', 'Téléchargement site non trouvé', 'ERREUR');

INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('530', 'PF_TELECHARGEMENT_ERREUR_LOGIN_INCORRECT', 'Téléchargement login incorrect', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('540', 'PF_TELECHARGEMENT_ERREUR_TRANSFERT_INTERROMPU', 'Téléchargement transfert interrompu', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('550', 'PF_TELECHARGEMENT_ERREUR_REPERTOIRE_INTROUVABLE', 'Téléchargement répertoire introuvable', 'ERREUR');

INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('600', 'PF_IMPORT_PIVOT_OK_', 'Import pivot ok', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('601', 'PF_IMPORT_PIVOT_EN_COURS_', 'Import pivot en cours', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('602', 'PF_IMPORT_PIVOT_ERREUR_ARCHIVE_CORROMPUE', 'Import pivot archive corrompue', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('603', 'PF_IMPORT_PIVOT_ERREUR_REPERTOIRE_INTROUVABLE', 'Import pivot répertoire introuvable', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('604', 'PF_IMPORT_PIVOT_ERREUR_BASE_DE_DONNEES', '"Import pivot erreur base de données"', 'ERREUR');
INSERT INTO referentiel.signalements( code, libelle, description, type)VALUES ('610', 'PF_DEDOUBLONNEMENT_OK_', 'Dedoublonnage OK', 'LOG');
INSERT INTO referentiel.signalements( code, libelle, description, type)VALUES ('620', 'PF_DEDOUBLONNEMENT_EN_COURS_', 'Dedoublonnage en cours', 'LOG');
INSERT INTO referentiel.signalements( code, libelle, description, type)VALUES ('630', 'PF_DEDOUBLONNEMENT_ERREUR_BASE_DE_DONNEES', 'Dedoublonnage erreur base de donnée', 'ERREUR');
INSERT INTO referentiel.signalements( code, libelle, description, type)VALUES ('640', 'PF_DEDOUBLONNEMENT_ERREUR_TEMPS_DEPASSE', 'Dedoublonnage timeout', 'ERREUR');
INSERT INTO referentiel.signalements( code, libelle, description, type)VALUES ('650', 'PF_DEDOUBLONNEMENT_ERREUR_FORMAT_INVALIDE', 'Dedoublonnage format invalide', 'ERREUR');
INSERT INTO referentiel.signalements( code, libelle, description, type)VALUES ('660', 'PF_SOUS_LOTS_OK_', 'Sous lots OK', 'LOG');
INSERT INTO referentiel.signalements( code, libelle, description, type)VALUES ('670', 'PF_SOUS_LOTS_EN_COURS_', 'Sous lots en cours', 'LOG');
INSERT INTO referentiel.signalements( code, libelle, description, type)VALUES ('680', 'PF_SOUS_LOTS_ERREUR_BASE_DE_DONNEES', 'Sous lots erreur base de données', 'LOG');
INSERT INTO referentiel.signalements( code, libelle, description, type)VALUES ('690', 'PF_SOUS_LOTS_ERREUR_TEMPS_DEPASSE', 'Sous lots timeout', 'LOG');
INSERT INTO referentiel.signalements( code, libelle, description, type)VALUES ('700', 'PF_SOUS_LOTS_ERREUR_FORMAT_INVALIDE', 'Sous lots format invalide', 'ERREUR');
INSERT INTO referentiel.signalements( code, libelle, description, type)VALUES ('710', 'PF_COUVERTURE_OK_', 'Couverture OK', 'LOG');
INSERT INTO referentiel.signalements( code, libelle, description, type)VALUES ('720', 'PF_COUVERTURE_EN_COURS_', 'Couverture en cours', 'LOG');
INSERT INTO referentiel.signalements( code, libelle, description, type)VALUES ('730', 'PF_COUVERTURE_ERREUR_BASE_DE_DONNEES', 'Couverture errue base de données', 'ERREUR');
INSERT INTO referentiel.signalements( code, libelle, description, type)VALUES ('740', 'PF_COUVERTURE_ERREUR_TEMPS_DEPASSE', 'Couverture timeout', 'LOG');
INSERT INTO referentiel.signalements( code, libelle, description, type)VALUES ('750', 'PF_COUVERTURE_ERREUR_FORMAT_INVALIDE', 'Couverture format invalide', 'ERREUR');

INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('820','PF_IDENTIFICATION_OK_', 'Identification Ok', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('830','PF_IDENTIFICATION_EN_COURS_', 'Identification en cours', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('840','PF_IDENTIFICATION_ERREUR_FORMAT_INVALIDE', 'Erreur format invalide', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('850','PF_IDENTIFICATION_ERREUR_BASE_DE_DONNEES', 'Erreu Base de données', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('860','PF_IDENTIFICATION_ERREUR_TEMPS_DEPASSE', 'Erreur temps attente', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('870','PF_IDENTIFICATION_ERREUR_REPERTOIRE_INTROUVABLE', 'Répertoire introuvable', 'ERREUR');

INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('880','PF_POINTAGE_OK_', 'Attente pointage en cours', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('890','PF_POINTAGE_EN_COURS_', 'Pointage en attente en cours', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('900','PF_POINTAGE_ERREUR_BASE_DE_DONNEES', 'Erreur base de données', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('910','PF_POINTAGE_ERREUR_FORMAT_INVALIDE', 'Format introuvable', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('920','PF_POINTAGE_ERREUR_REPERTOIRE_INTROUVABLE', 'Répertoire introuvable', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('930','PF_POINTAGE_ERREUR_TEMPS_DEPASSE', 'Temps attente dépassé', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('940','PF_POINTAGE_ERREUR_WEBSERVICE', 'Web service de pointage introuvable', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('950','PF_CREATION_LIVRABLE_OK_', 'Création Livrable OK', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('960','PF_CREATION_LIVRABLE_EN_COURS_', 'création livrable en cours', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('970','PF_CREATION_LIVRABLE_ERREUR_BASE_DE_DONNEES', 'Répertoire introuvable', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('980','PF_CREATION_LIVRABLE_ERREUR_TEMPS_DEPASSE', 'Répertoire introuvable', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('990','PF_CREATION_LIVRABLE_ERREUR_FORMAT_INVALIDE', 'Répertoire introuvable', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('1000','PF_CREATION_LIVRABLE_ERREUR_REPERTOIRE_SOURCE_INTROUVABLE', 'Répertoire introuvable', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('1010','PF_CREATION_LIVRABLE_ERREUR_REPERTOIRE_DESTINATION_INTROUVABLE', 'Répertoire introuvable', 'ERREUR');

INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('760','PF_FEUILLE_POINTAGE_OK_', 'Impresseion feuille de pointage OK ', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('765','PF_FEUILLE_POINTAGE_EN_COURS_', 'Impresseion feuille de pointage en cours ', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('770','PF_FEUILLE_POINTAGE_ERREUR_BASE_DE_DONNEES', 'Erreur de base de données', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('775','PF_FEUILLE_POINTAGE_ERREUR_FORMAT_INVALIDE', 'Format feuillde de pointage invalide', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('780','PF_FEUILLE_POINTAGE_ERREUR_REPERTOIRE_INTROUVABLE', 'Répertoire introuvable', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('785','PF_FEUILLE_POINTAGE_ERREUR_TEMPS_DEPASSE', 'Temps dépassé', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('790','PF_ATTRIBUT_ENCOURS', 'Attribut en cours', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('795','PF_ATTRIBUT_OK', 'Attribut Ok', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('800','PF_ATTRIBUT_ERREUR_BASE_DE_DONNEES', 'Erreur base de données', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('871','PF_FICHIER_IMAGE_POSTION_ENCOURS', 'Génération image position en cours', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('872','PF_FICHIER_IMAGE_POSTION_OK', 'Génération image position OK', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('873','PF_FICHIER_IMAGE_POSTION_ERREUR_BASE_DE_DONNEES', 'Erreur base de données', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('874','PF_FICHIER_IMAGE_POSTION_ERREUR_REPERTOIRE_SOURCE_INTROUVABLE', 'Répertoire source introuvable', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('875','PF_FICHIER_IMAGE_POSTION_ERREUR_REPERTOIRE_DESTINATION_INTROUVABLE', 'Répertoire destination introuvable', 'ERREUR');


INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('251','PF_MODALITE_APPRO_ERREUR_NON_DEFINIE', 'Modalité appro non définie', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('606','PF_IMPORT_PIVOT_ERREUR_COPIE_FICHIER', 'Erreur copie fichier import pivot', 'ERREUR');

INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('252','PF_ERREUR_GENERATION_PIVOT', 'Erreur de génération du format pivot xml', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('253','PF_ERREUR_EXECUTION_XSL', 'Erreur exécution xsl sur le', 'ERREUR');

INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('876','PF_FICHIER_IMAGE_POSTION_ERREUR_ARCHIVE_CORROMPUE', 'Erreur archive corrompue', 'ERREUR');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('877','PF_ERREUR_FERMETURE_ARCHIVE', 'Erreur fermeture archive', 'ERREUR');

INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('651','PF_DEDOUBLONNEMENT_ERREUR_RESOURCE_INTROUVABLE', 'Erreur resource introuvable', 'ERREUR');

INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('607','PF_CONTENU_PIVOT_DOUBLON', 'Contenu format pivot en doublon', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('608','PF_CONTENU_PIVOT_ORUGINAL', 'Contenu original format pivot', 'LOG');
INSERT INTO referentiel.signalements(code, libelle, description, type)VALUES ('706','PF_SOUS_LOTS_ATTENTE_TRAITEMENT_AUTRE_INSTANCE', 'En attente de traitement par autre instance original format pivot', 'LOG');



 
    





