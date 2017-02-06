--Insert Module GenerationPdf
INSERT INTO referentiel.type_module(
            nom, libelle, description, id_script_entree, id_script_sortie, 
            id_type_module_suivant, id_ressource_autotest_entree, id_ressource_autotest_sortie, 
            actif,parametres)
    VALUES ('GENERATION_PDF', 'Génération de PDF','Process de Génération de PDF', null, null,null, null, 
            null, TRUE,'<parametres><nom_module>GENERATION_PDF</nom_module><ip_port_ws>8080</ip_port_ws><port_supervision>8080
  </port_supervision><banette_entree>C:\\banette_entree</banette_entree><banette_sortie>C:\\banette_sortie</banette_sortie><duration_supervision_seconds>3</duration_supervision_seconds>
  <scrutation_banette_delay_seconds>30</scrutation_banette_delay_seconds><scrutation_pile_delay_seconds>5</scrutation_pile_delay_seconds>
  <duration_process_ticket_minutes>3</duration_process_ticket_minutes><autotest_delay_seconds>5</autotest_delay_seconds>
  <nbtry_to_stop>5</nbtry_to_stop><mail_smtp_host>192.168.2.10</mail_smtp_host>
  <support_mail_to_address>Adresse_Mail_Support</support_mail_to_address><support_mail_from_address>Adresse_Mail_Support</support_mail_from_address>
  <support_mail_subject>Module en cours d exécution</support_mail_subject><max_thread_bannettes>5</max_thread_bannettes><max_thread_ws>5</max_thread_ws></parametres>');
  
--Insert Module TraitementOCR
INSERT INTO referentiel.type_module(
            nom, libelle, description, id_script_entree, id_script_sortie, 
            id_type_module_suivant, id_ressource_autotest_entree, id_ressource_autotest_sortie, 
            actif,parametres)
    VALUES ('TRAITEMENT_OCR', 'OCR','Process de traitement ocr',null, null,null, null, 
            null, TRUE,'<parametres><nom_module>TRAITEMENT_OCR</nom_module><ip_port_ws>8080</ip_port_ws><port_supervision>8080
  </port_supervision><banette_entree>C:\\banette_entree</banette_entree><banette_sortie>C:\\banette_sortie</banette_sortie><duration_supervision_seconds>3</duration_supervision_seconds>
  <scrutation_banette_delay_seconds>30</scrutation_banette_delay_seconds><scrutation_pile_delay_seconds>5</scrutation_pile_delay_seconds>
  <duration_process_ticket_minutes>3</duration_process_ticket_minutes><autotest_delay_seconds>5</autotest_delay_seconds>
  <nbtry_to_stop>5</nbtry_to_stop><mail_smtp_host>192.168.2.10</mail_smtp_host>
  <support_mail_to_address>Adresse_Mail_Support</support_mail_to_address><support_mail_from_address>Adresse_Mail_Support</support_mail_from_address>
  <support_mail_subject>Module en cours d exécution</support_mail_subject><max_thread_bannettes>5</max_thread_bannettes><max_thread_ws>5</max_thread_ws></parametres>');
            
--Insert Module TraitementImage
INSERT INTO referentiel.type_module(
            nom, libelle, description, id_script_entree, id_script_sortie, 
            id_type_module_suivant, id_ressource_autotest_entree, id_ressource_autotest_sortie, 
            actif,parametres)
    VALUES ('TRAITEMENT_IMAGE', 'Image','Process de traitement image',null, null,null, null, 
            null, TRUE,'<parametres><nom_module>TRAITEMENT_IMAGE</nom_module><ip_port_ws>8080</ip_port_ws><port_supervision>8080
  </port_supervision><banette_entree>C:\\banette_entree</banette_entree><banette_sortie>C:\\banette_sortie</banette_sortie><duration_supervision_seconds>3</duration_supervision_seconds>
  <scrutation_banette_delay_seconds>30</scrutation_banette_delay_seconds><scrutation_pile_delay_seconds>5</scrutation_pile_delay_seconds>
  <duration_process_ticket_minutes>3</duration_process_ticket_minutes><autotest_delay_seconds>5</autotest_delay_seconds>
  <nbtry_to_stop>5</nbtry_to_stop><mail_smtp_host>192.168.2.10</mail_smtp_host>
  <support_mail_to_address>Adresse_Mail_Support</support_mail_to_address><support_mail_from_address>Adresse_Mail_Support</support_mail_from_address>
  <support_mail_subject>Module en cours d exécution</support_mail_subject><max_thread_bannettes>5</max_thread_bannettes><max_thread_ws>5</max_thread_ws></parametres>');

--Insert Module Supervision
INSERT INTO referentiel.type_module(
            nom, libelle, description, id_script_entree, id_script_sortie, 
            id_type_module_suivant, id_ressource_autotest_entree, id_ressource_autotest_sortie, 
            actif,parametres)
    VALUES ('SUPERVISION', 'Supervision','Process de supervisopn',null, null,null, null, 
            null, TRUE,'<parametres><nom_module>SUPERVISION</nom_module><ip_port_ws>8080</ip_port_ws><port_supervision>8080
  </port_supervision><banette_entree>C:\\banette_entree</banette_entree><banette_sortie>C:\\banette_sortie</banette_sortie><duration_supervision_seconds>3</duration_supervision_seconds>
  <scrutation_banette_delay_seconds>30</scrutation_banette_delay_seconds><scrutation_pile_delay_seconds>5</scrutation_pile_delay_seconds>
  <duration_process_ticket_minutes>3</duration_process_ticket_minutes><autotest_delay_seconds>5</autotest_delay_seconds>
  <nbtry_to_stop>5</nbtry_to_stop><mail_smtp_host>192.168.2.10</mail_smtp_host>
  <support_mail_to_address>Adresse_Mail_Support</support_mail_to_address><support_mail_from_address>Adresse_Mail_Support</support_mail_from_address>
  <support_mail_subject>Module en cours d exécution</support_mail_subject><max_thread_bannettes>5</max_thread_bannettes><max_thread_ws>5</max_thread_ws></parametres>');

--Insert Module AutoTest
INSERT INTO referentiel.type_module(
            nom, libelle, description, id_script_entree, id_script_sortie, 
            id_type_module_suivant, id_ressource_autotest_entree, id_ressource_autotest_sortie, 
            actif,parametres)
    VALUES ('AUTOTEST', 'Auto test','Process autotest',null, null,null, null, 
            null, TRUE,'<parametres><nom_module>AUTOTEST</nom_module><ip_port_ws>8080</ip_port_ws><port_supervision>8080
  </port_supervision><banette_entree>C:\\banette_entree</banette_entree><banette_sortie>C:\\banette_sortie</banette_sortie><duration_supervision_seconds>3</duration_supervision_seconds>
  <scrutation_banette_delay_seconds>30</scrutation_banette_delay_seconds><scrutation_pile_delay_seconds>5</scrutation_pile_delay_seconds>
  <duration_process_ticket_minutes>3</duration_process_ticket_minutes><autotest_delay_seconds>5</autotest_delay_seconds>
  <nbtry_to_stop>5</nbtry_to_stop><mail_smtp_host>192.168.2.10</mail_smtp_host>
  <support_mail_to_address>Adresse_Mail_Support</support_mail_to_address><support_mail_from_address>Adresse_Mail_Support</support_mail_from_address>
  <support_mail_subject>Module en cours d exécution</support_mail_subject><max_thread_bannettes>5</max_thread_bannettes><max_thread_ws>5</max_thread_ws></parametres>');            

--Insert Module TraitementGeneral
INSERT INTO referentiel.type_module(
            nom, libelle, description, id_script_entree, id_script_sortie, 
            id_type_module_suivant, id_ressource_autotest_entree, id_ressource_autotest_sortie, 
            actif,parametres)
    VALUES ('FORMAT_PIVOT', 'Format Pivot','Process de Format Pivot',null, null,null, null, 
            null, TRUE,'<parametres><nom_module>FORMAT_PIVOT</nom_module><ip_port_ws>8080</ip_port_ws><port_supervision>8080
  </port_supervision><banette_entree>C:\\banette_entree</banette_entree><banette_sortie>C:\\banette_sortie</banette_sortie><duration_supervision_seconds>3</duration_supervision_seconds>
  <scrutation_banette_delay_seconds>30</scrutation_banette_delay_seconds><scrutation_pile_delay_seconds>5</scrutation_pile_delay_seconds>
  <duration_process_ticket_minutes>3</duration_process_ticket_minutes><autotest_delay_seconds>5</autotest_delay_seconds>
  <nbtry_to_stop>5</nbtry_to_stop><mail_smtp_host>192.168.2.10</mail_smtp_host>
  <support_mail_to_address>Adresse_Mail_Support</support_mail_to_address><support_mail_from_address>Adresse_Mail_Support</support_mail_from_address>
  <support_mail_subject>Module en cours d exécution</support_mail_subject><max_thread_bannettes>5</max_thread_bannettes><max_thread_ws>5</max_thread_ws></parametres>');


--Insert Module ATTRIBUTS
INSERT INTO referentiel.type_module(
            nom, libelle, description, id_script_entree, id_script_sortie, 
            id_type_module_suivant, id_ressource_autotest_entree, id_ressource_autotest_sortie, 
            actif,parametres)
    VALUES ('ATTRIBUTS', 'Attributs','Process de transformation Attributs',null, null,null, null, 
            null, TRUE,'<parametres><nom_module>ATTRIBUTS</nom_module><ip_port_ws>8080</ip_port_ws><port_supervision>8080
  </port_supervision><banette_entree>C:\\banette_entree</banette_entree><banette_sortie>C:\\banette_sortie</banette_sortie><duration_supervision_seconds>3</duration_supervision_seconds>
  <scrutation_banette_delay_seconds>30</scrutation_banette_delay_seconds><scrutation_pile_delay_seconds>5</scrutation_pile_delay_seconds>
  <duration_process_ticket_minutes>3</duration_process_ticket_minutes><autotest_delay_seconds>5</autotest_delay_seconds>
  <nbtry_to_stop>5</nbtry_to_stop><mail_smtp_host>192.168.2.10</mail_smtp_host>
  <support_mail_to_address>Adresse_Mail_Support</support_mail_to_address><support_mail_from_address>Adresse_Mail_Support</support_mail_from_address>
  <support_mail_subject>Module en cours d exécution</support_mail_subject><max_thread_bannettes>5</max_thread_bannettes><max_thread_ws>5</max_thread_ws></parametres>');

--Insert Module DEDOUBLONNEMENT
INSERT INTO referentiel.type_module(
            nom, libelle, description, id_script_entree, id_script_sortie, 
            id_type_module_suivant, id_ressource_autotest_entree, id_ressource_autotest_sortie, 
            actif,parametres)
    VALUES ('DEDOUBLONNEMENT', 'Dedoublonnage','Process de dedoublonnage',null, null,null, null, 
            null, TRUE,'<parametres><nom_module>DEDOUBLONNEMENT</nom_module><ip_port_ws>8080</ip_port_ws><port_supervision>8080
  </port_supervision><banette_entree>C:\\banette_entree</banette_entree><banette_sortie>C:\\banette_sortie</banette_sortie><duration_supervision_seconds>3</duration_supervision_seconds>
  <scrutation_banette_delay_seconds>30</scrutation_banette_delay_seconds><scrutation_pile_delay_seconds>5</scrutation_pile_delay_seconds>
  <duration_process_ticket_minutes>3</duration_process_ticket_minutes><autotest_delay_seconds>5</autotest_delay_seconds>
  <nbtry_to_stop>5</nbtry_to_stop><mail_smtp_host>192.168.2.10</mail_smtp_host>
  <support_mail_to_address>Adresse_Mail_Support</support_mail_to_address><support_mail_from_address>Adresse_Mail_Support</support_mail_from_address>
  <support_mail_subject>Module en cours d exécution</support_mail_subject><max_thread_bannettes>5</max_thread_bannettes><max_thread_ws>5</max_thread_ws></parametres>');

--Insert Module SOUS_LOTS
INSERT INTO referentiel.type_module(
            nom, libelle, description, id_script_entree, id_script_sortie, 
            id_type_module_suivant, id_ressource_autotest_entree, id_ressource_autotest_sortie, 
            actif,parametres)
    VALUES ('SOUS_LOTS', 'Constitution de sous lots"','Process de constitution de sous lots',null, null,null, null, 
            null, TRUE, '<parametres><nom_module>SOUS_LOTS</nom_module><ip_port_ws>8080</ip_port_ws><port_supervision>8080
  </port_supervision><banette_entree>C:\\banette_entree</banette_entree><banette_sortie>C:\\banette_sortie</banette_sortie><duration_supervision_seconds>3</duration_supervision_seconds>
  <scrutation_banette_delay_seconds>30</scrutation_banette_delay_seconds><scrutation_pile_delay_seconds>5</scrutation_pile_delay_seconds>
  <duration_process_ticket_minutes>3</duration_process_ticket_minutes><autotest_delay_seconds>5</autotest_delay_seconds>
  <nbtry_to_stop>5</nbtry_to_stop><mail_smtp_host>192.168.2.10</mail_smtp_host>
  <support_mail_to_address>Adresse_Mail_Support</support_mail_to_address><support_mail_from_address>Adresse_Mail_Support</support_mail_from_address>
  <support_mail_subject>Module en cours d exécution</support_mail_subject><max_thread_bannettes>5</max_thread_bannettes><max_thread_ws>5</max_thread_ws></parametres>');

--Insert Module COUVERTURE_PAGE
INSERT INTO referentiel.type_module(
            nom, libelle, description, id_script_entree, id_script_sortie, 
            id_type_module_suivant, id_ressource_autotest_entree, id_ressource_autotest_sortie, 
            actif,parametres)
    VALUES ('COUVERTURE_PAGE', 'Couverture Pages','Process de recuperation de couverture pages',null, null,null, null, 
            null, TRUE,'<parametres><nom_module>COUVERTURE_PAGE</nom_module><ip_port_ws>8080</ip_port_ws><port_supervision>8080
  </port_supervision><banette_entree>C:\\banette_entree</banette_entree><banette_sortie>C:\\banette_sortie</banette_sortie><duration_supervision_seconds>3</duration_supervision_seconds>
  <scrutation_banette_delay_seconds>30</scrutation_banette_delay_seconds><scrutation_pile_delay_seconds>5</scrutation_pile_delay_seconds>
  <duration_process_ticket_minutes>3</duration_process_ticket_minutes><autotest_delay_seconds>5</autotest_delay_seconds>
  <nbtry_to_stop>5</nbtry_to_stop><mail_smtp_host>192.168.2.10</mail_smtp_host>
  <support_mail_to_address>Adresse_Mail_Support</support_mail_to_address><support_mail_from_address>Adresse_Mail_Support</support_mail_from_address>
  <support_mail_subject>Module en cours d exécution</support_mail_subject><max_thread_bannettes>5</max_thread_bannettes><max_thread_ws>5</max_thread_ws></parametres>');

--Insert Module POINTAGE
INSERT INTO referentiel.type_module(
            nom, libelle, description, id_script_entree, id_script_sortie, 
            id_type_module_suivant, id_ressource_autotest_entree, id_ressource_autotest_sortie, 
            actif,parametres)
    VALUES ('POINTAGE', 'Impression feuille de poinatge','Process de feuille de pointage',null, null,null, null, 
            null, TRUE,'<parametres><nom_module>POINTAGE</nom_module><ip_port_ws>8080</ip_port_ws><port_supervision>8080
  </port_supervision><banette_entree>C:\\banette_entree</banette_entree><banette_sortie>C:\\banette_sortie</banette_sortie><duration_supervision_seconds>3</duration_supervision_seconds>
  <scrutation_banette_delay_seconds>30</scrutation_banette_delay_seconds><scrutation_pile_delay_seconds>5</scrutation_pile_delay_seconds>
  <duration_process_ticket_minutes>3</duration_process_ticket_minutes><autotest_delay_seconds>5</autotest_delay_seconds>
  <nbtry_to_stop>5</nbtry_to_stop><mail_smtp_host>192.168.2.10</mail_smtp_host>
  <support_mail_to_address>Adresse_Mail_Support</support_mail_to_address><support_mail_from_address>Adresse_Mail_Support</support_mail_from_address>
  <support_mail_subject>Module en cours d exécution</support_mail_subject><max_thread_bannettes>5</max_thread_bannettes><max_thread_ws>5</max_thread_ws></parametres>');
    
--Insert Module IDENTIFIANT_PRESSE
INSERT INTO referentiel.type_module(
            nom, libelle, description, id_script_entree, id_script_sortie, 
            id_type_module_suivant, id_ressource_autotest_entree, id_ressource_autotest_sortie, 
            actif,parametres)
    VALUES ('IDENTIFIANT_PRESSE', 'Identifiant Presse','Process de recuperation des identifiants presse',null, null,null, null, 
            null, TRUE,'<parametres><nom_module>IDENTIFIANT_PRESSE</nom_module><ip_port_ws>8080</ip_port_ws><port_supervision>8080
  </port_supervision><banette_entree>C:\\banette_entree</banette_entree><banette_sortie>C:\\banette_sortie</banette_sortie><duration_supervision_seconds>3</duration_supervision_seconds>
  <scrutation_banette_delay_seconds>30</scrutation_banette_delay_seconds><scrutation_pile_delay_seconds>5</scrutation_pile_delay_seconds>
  <duration_process_ticket_minutes>3</duration_process_ticket_minutes><autotest_delay_seconds>5</autotest_delay_seconds>
  <nbtry_to_stop>5</nbtry_to_stop><mail_smtp_host>192.168.2.10</mail_smtp_host>
  <support_mail_to_address>Adresse_Mail_Support</support_mail_to_address><support_mail_from_address>Adresse_Mail_Support</support_mail_from_address>
  <support_mail_subject>Module en cours d exécution</support_mail_subject><max_thread_bannettes>5</max_thread_bannettes><max_thread_ws>5</max_thread_ws></parametres>');
  
--Insert Module FICHIERS_IMAGES_POSITIONS
INSERT INTO referentiel.type_module(
            nom, libelle, description, id_script_entree, id_script_sortie, 
            id_type_module_suivant, id_ressource_autotest_entree, id_ressource_autotest_sortie, 
            actif,parametres)
    VALUES ('FICHIERS_IMAGES_POSITIONS', 'Génération fichiers images et fichiers positions','Process fichiers images et positions',null, null,null, null, 
            null, TRUE,'<parametres><nom_module>FICHIERS_IMAGES_POSITIONS</nom_module><ip_port_ws>8080</ip_port_ws><port_supervision>8080
  </port_supervision><banette_entree>C:\\banette_entree</banette_entree><banette_sortie>C:\\banette_sortie</banette_sortie><duration_supervision_seconds>3</duration_supervision_seconds>
  <scrutation_banette_delay_seconds>30</scrutation_banette_delay_seconds><scrutation_pile_delay_seconds>5</scrutation_pile_delay_seconds>
  <duration_process_ticket_minutes>3</duration_process_ticket_minutes><autotest_delay_seconds>5</autotest_delay_seconds>
  <nbtry_to_stop>5</nbtry_to_stop><mail_smtp_host>192.168.2.10</mail_smtp_host>
  <support_mail_to_address>Adresse_Mail_Support</support_mail_to_address><support_mail_from_address>Adresse_Mail_Support</support_mail_from_address>
  <support_mail_subject>Module en cours d exécution</support_mail_subject><max_thread_bannettes>5</max_thread_bannettes><max_thread_ws>5</max_thread_ws></parametres>');
  
  
--Insert Module ATTENTE POINTAGE
INSERT INTO referentiel.type_module(
            nom, libelle, description, id_script_entree, id_script_sortie, 
            id_type_module_suivant, id_ressource_autotest_entree, id_ressource_autotest_sortie, 
            actif,parametres)
    VALUES ('ATTENTE_POINTAGE', 'Attente pointage','Process Attente pointage',null, null,null, null, 
            null, TRUE,'<parametres><nom_module>ATTENTE_POINTAGE</nom_module><ip_port_ws>8080</ip_port_ws><port_supervision>8080
  </port_supervision><banette_entree>C:\\banette_entree</banette_entree><banette_sortie>C:\\banette_sortie</banette_sortie><duration_supervision_seconds>3</duration_supervision_seconds>
  <scrutation_banette_delay_seconds>30</scrutation_banette_delay_seconds><scrutation_pile_delay_seconds>5</scrutation_pile_delay_seconds>
  <duration_process_ticket_minutes>3</duration_process_ticket_minutes><autotest_delay_seconds>5</autotest_delay_seconds>
  <nbtry_to_stop>5</nbtry_to_stop><mail_smtp_host>192.168.2.10</mail_smtp_host>
  <support_mail_to_address>Adresse_Mail_Support</support_mail_to_address><support_mail_from_address>Adresse_Mail_Support</support_mail_from_address>
  <support_mail_subject>Module en cours d exécution</support_mail_subject><max_thread_bannettes>5</max_thread_bannettes><max_thread_ws>5</max_thread_ws></parametres>');


--Insert Module LIVRABLES
INSERT INTO referentiel.type_module(
            nom, libelle, description, id_script_entree, id_script_sortie, 
            id_type_module_suivant, id_ressource_autotest_entree, id_ressource_autotest_sortie, 
            actif,parametres)
    VALUES ('LIVRABLES', 'Livrables ','Process livrables finals',null, null,null, null, 
            null, TRUE,'<parametres><nom_module>LIVRABLES</nom_module><ip_port_ws>8080</ip_port_ws><port_supervision>8080
  </port_supervision><banette_entree>C:\\banette_entree</banette_entree><banette_sortie>C:\\banette_sortie</banette_sortie><duration_supervision_seconds>3</duration_supervision_seconds>
  <scrutation_banette_delay_seconds>30</scrutation_banette_delay_seconds><scrutation_pile_delay_seconds>5</scrutation_pile_delay_seconds>
  <duration_process_ticket_minutes>3</duration_process_ticket_minutes><autotest_delay_seconds>5</autotest_delay_seconds>
  <nbtry_to_stop>5</nbtry_to_stop><mail_smtp_host>192.168.2.10</mail_smtp_host>
  <support_mail_to_address>Adresse_Mail_Support</support_mail_to_address><support_mail_from_address>Adresse_Mail_Support</support_mail_from_address>
  <support_mail_subject>Module en cours d exécution</support_mail_subject><max_thread_bannettes>5</max_thread_bannettes><max_thread_ws>5</max_thread_ws></parametres>');

  
  