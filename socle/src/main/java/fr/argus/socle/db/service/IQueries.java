/**
 * 
 */
package fr.argus.socle.db.service;

/**
 * @author mamadou.dansoko
 * Cette interface doit contenir uniquement des requêtes SQL pas d'autres constantes
 */
public interface IQueries {
	public static final String SQL_SELECT_RESOURCES = "SELECT id, nom, contenu, date,id_type_module FROM referentiel.ressources";
	public static final String SQL_SELECT_SIGNALEMENTS = "SELECT id, code, libelle, description, type, date FROM referentiel.signalements";
	public static final String SQL_SELECT_TYPE_MODULE = "SELECT id, nom, libelle, description, date, id_script_entree, id_script_sortie, id_type_module_suivant, id_ressource_autotest_entree, id_ressource_autotest_sortie,  actif, parametres FROM referentiel.type_module";
	public static final String SQL_SELECT_LOGS = "select date_log,description from production.logs order by date_log desc limit ?";
	public static final String SQL_SELECT_AUTO_TEST = "select  a.date_debut, a.date_fin,s.libelle from  production.autotest a , referentiel.signalements s ,referentiel.type_module tm where a.id_status=s.id and a.id_type_module=tm.id  order by a.date desc limit 1";
	public static final String SQL_INSERT_TICKET_WS = "INSERT INTO production.encours(id_ocr, id_produit, type, priorite, contenu_ticket, login_utilisateur, machine,id_type_module, date,mode_approvisonnement,id_livraison_numerique) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
	public static final String SQL_INSERT_TICKET_BANETTE = "INSERT INTO production.encours( type,priorite,  contenu_ticket, login_utilisateur,   machine, id_type_module, date,mode_approvisonnement,id_livraison_numerique)  VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String SQL_SLECT_FILE_PATH = "SELECT (xpath('/TICKET/FILES/FILE/text()', contenu_ticket))[1]::varchar as file_path  from production.encours  where ((xpath('/TICKET/FILES/FILE/text()', contenu_ticket))[1]::varchar) = ? ";
	public static final String SQL_INSERT_MODULE = "INSERT INTO production.modules(id_type_module, machine, login_utilisateur, port_ws, port_supervision, etat_mise_a_jour_autotest,id_status,date_dernier_traitement,date) VALUES ( ?,?, ?, ?,?,1,1, ?, ?)";
	public static final String SQL_SELECT_TICKETS_EN_COURS = "SELECT contenu_ticket FROM production.encours WHERE machine = ?";
	public static final String SQL_INSERT_TICKET_BANETTE_BATCH = "COPY production.encours( contenu_ticket,type,priorite,  login_utilisateur, machine, id_type_module, date,mode_approvisonnement)  FROM STDIN WITH CSV   ";
	public static final String SQL_SELECT_MODULE = "SELECT id, id_type_module, machine, login_utilisateur, port_ws, port_supervision, etat_mise_a_jour_autotest, id_status, date_dernier_traitement, date  FROM production.modules WHERE id_type_module=? and machine=? and login_utilisateur=? and  port_ws=?";
	public static final String SQL_DELETE_MODULE = " DELETE FROM production.modules WHERE id_type_module=? and machine=? and login_utilisateur=? and port_supervision=? ";
	public static final String SQL_SELECT_TYPE_MODULE_ID_BY_NAME = "SELECT id FROM referentiel.type_module WHERE nom = ? ";
	public static final String SQL_SELECT_MODULE_MACHINE_NAME = "SELECT  machine FROM production.modules WHERE id_type_module=? and machine=? and login_utilisateur=? and  port_ws=? or port_supervision=?";
	
	public static final String SQL_INSERT_LIVRAISON_NUMERIQUE = "INSERT INTO production.livraison_numerique(id, bp_index, id_source_appro, nom, nom_fichier, date_creation, id_status, contenu_pivot, chemin_livraison_hash, chemin_livraison_archivage, clef_index_pivot)  VALUES (?, ?, ?, ?, ?, ?,?, ?, ?, ?,?)";
	public static final String SQL_INSERT_LIVRAISON_CONTENU_NUMERIQUE = "INSERT INTO production.livraison_numerique_contenu(id, id_livraison_numerique, nom, status, id_livraison_numerique_contenu_doublon, contenu_pivot, type_fichier, type_contenu)VALUES (?, ?, ?, (SELECT id FROM referentiel.signalements WHERE code = ?), ?, ?,?, ?) ";
	public static final String SQL_SELECT_SEQUENCE_LIVRAISON_NUMERIQUE_PAR_BLOC ="select nextval('production.seq_livraison_numerique') as seq from generate_series(1,?)";
	public static final String SQL_SELECT_SEQUENCE_LIVRAISON_NUMERIQUE_CONTENU_PAR_BLOC ="select nextval('production.seq_livraison_numerique_contenu') as seq from generate_series(1,?)";
	public static final String SQL_INSERT_TRACE_LOGS = "INSERT INTO production.logs(id,id_ocr, id_produit, login_utilisateur, machine, description, date_debut,nom_module)VALUES (?,?, ?, ?, ?, ?, ?, ?)";
	public static final String SQL_UPDATE_TRACE_LOGS = "UPDATE production.logs SET date_fin=? WHERE id = ?";
	public static final String SQL_SEQ_LOGS = "select nextval('production.seq_logs') as seq ";
	public static final String SQL_INSERT_LIVRAISON_NUMERIQUE_BATCH = "COPY production.livraison_numerique(id_source_appro, nom, nom_fichier, date_creation)  FROM STDIN WITH CSV   ";
	
	public static final String SQL_INSERT_LIVRAISON_CONTENU_NUMERIQUE_BATCH = "COPY production.livraison_numerique_contenu(id, id_livraison_numerique, nom, status, id_livraison_numerique_contenu_doublon, contenu_pivot, type_fichier, type_contenu)  FROM STDIN WITH CSV QUOTE E'\b' DELIMITER '\t' ";
	
	public static final String SQL_UPDATE_LIVRAISON_NUMERIQUE = "UPDATE production.livraison_numerique SET bp_index=?,id_status= (SELECT id FROM referentiel.signalements where code = ?), contenu_pivot=?, chemin_livraison_hash=?, chemin_livraison_archivage=?, clef_index_pivot=? WHERE nom = ?";
	public static final String SQL_SELECT_ID_LIVRAISON_NUMERIQUE_BY_UNIQ_NAME ="SELECT id FROM production.livraison_numerique where nom = ?";
	public static final String SQL_UPDATE_STATUS_LIVRAISON_NUMERIQUE= "UPDATE production.livraison_numerique SET id_status = (SELECT id FROM referentiel.signalements WHERE code = ?) WHERE id = ? ";
	public static final String SQL_INSERT_TRACE_LOGS_DATE_FIN = "INSERT INTO production.logs(id_ocr, id_produit, login_utilisateur, machine, description, date_debut,date_fin,nom_module)VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

	public static final String SQL_SELECT_REQUETE_RESSOURCES = "SELECT ressources.id, ressources.nom, ressources.contenu, ressources.date, ressources.id_type_module FROM referentiel.ressources as ressources INNER JOIN referentiel.type_module AS type_module ON ressources.id_type_module = type_module.id WHERE ressources.nom like ? AND type_module.nom = ? ";
	public static final String SQL_SELECT_CONTENU_PIVOT_LIVRAISON_NUMERIQUE_BY_ID = "SELECT contenu_pivot FROM production.livraison_numerique WHERE id = ?";
	public static final String SQL_SELECT_CONTENU_PIVOT_LIVRAISON_NUMERIQUE_CONTENU_BY_IDLIVNUM = "SELECT contenu_pivot, status, type_contenu FROM production.livraison_numerique_contenu WHERE id_livraison_numerique = ? ";
	public static final String SQL_SELECT_LIBRAISON_NUMERIQUE_ATTRIBUTS = "SELECT livraison_numerique_attribut.nom, livraison_numerique_attribut.valeur FROM production.livraison_numerique_attribut WHERE id_livraison_numerique = ?";
	
	public static final String SQL_SELECT_CONTENU_PIVOT_LIVRAISON_NUMERIQUE_NON_DOUBLON_SUR_30_JOURS_LIMIT_200_BY_ID = "SELECT contenu_pivot FROM production.livraison_numerique WHERE id = ? AND  limit 200 ";
	
	public static final String SQL_INSERT_LIVRAISON_NUMERIQUE_ATTRIBUT = "INSERT INTO production.livraison_numerique_attribut(id,id_livraison_numerique, nom, valeur, date_creation)VALUES (nextval('production.seq_livraison_numerique_attribut'),?,?,?,?)";
	public static final String SQL_SELECT_LIVRAISON_NUMERIQUE_ATTRIBUT = "SELECT id FROM production.livraison_numerique_attribut where id_livraison_numerique = ? AND nom = ?";
	public static final String SQL_UPDATE_LIVRAISON_NUMERIQUE_ATTRIBUT = "UPDATE production.livraison_numerique_attribut SET valeur = ?, date_creation = ? WHERE id_livraison_numerique = ? AND nom = ?";
	
	public static final String SQL_UPDATE_CLE_INDEX_PIVOT_LIVRAISON_NUMERIQUE = "UPDATE production.livraison_numerique SET clef_index_pivot=? WHERE id = ?";
	public static final String SQL_SELECT_REQUEST_RESSOURCE_BY_MODULE_NAME_ = "SELECT ressources.id, ressources.nom, ressources.contenu, ressources.date, ressources.id_type_module FROM referentiel.ressources as ressources INNER JOIN referentiel.type_module AS type_module ON ressources.id_type_module = type_module.id WHERE ressources.nom = ? AND type_module.nom = ? ";
	public static final String SQL_SELECT_PROCESS_TRADEXPRESS = "SELECT ressources.contenu FROM referentiel.ressources AS ressources INNER JOIN referentiel.type_module AS module ON module.id_type_module_suivant = ressources.id_type_module where ressources.nom = ? and module.nom = ?";
	
	public static final String SQL_SELECT_CLE_INDEX_PIVOT_LIVRAISON_NUMERIQUE = "SELECT clef_index_pivot FROM production.livraison_numerique WHERE clef_index_pivot = ? AND  ((to_date(((xpath('/RECEPTION/@date_reception', contenu_pivot))[1]::varchar),'YYYYMMDDHH24:MI:SS') - date((now()::date - INTERVAL '30 DAY'))) < 30) limit 200 ";
	
	public static final String SQL_UPDATE_STATUS_ORIGINAL_OU_DOUBLON_LIVRAISON_NUMERIQUE_CONTENU = "UPDATE production.livraison_numerique_contenu SET status= (SELECT id FROM referentiel.signalements WHERE code = ?) WHERE id_livraison_numerique = ?";
	
	public static final String SQL_SELECT_CONTENU_RESSOURCE_BY_RESSOURCE_NAME ="SELECT ressources.contenu FROM referentiel.ressources as ressources WHERE ressources.nom = ? ";
	public static final String SQL_BEST_RESSOURCE_BY_NOM_CASE_IGNORE = "select coalesce(vals[array_position(keys, lower(:key||'.'||:machine))], vals[array_position(keys, lower(:key))]) as contenu from ( SELECT array_agg(lower(nom)) keys, array_agg(contenu) vals FROM referentiel.ressources where lower(nom)  IN (lower(:key||'.'||:machine), lower(:key)) ) t";
	public static final String SQL_SOUS_LOTS_INSERT_LIVRAISON_NUMERIQUE = "INSERT INTO production.livraison_numerique(id,bp_index, id_source_appro, nom, nom_fichier, date_creation, id_status, contenu_pivot, chemin_livraison_hash, chemin_livraison_archivage, clef_index_pivot,id_pere)  VALUES (?,?, ?, ?, ?, ?,(SELECT id FROM referentiel.signalements where code = ?), ?, ?, ?,?,?)";
	public static final String SQL_SELECT_LIVRAISON_NUMERIQUE_BY_ID = "SELECT id, id_source_appro, contenu_pivot, clef_index_pivot, date_creation, id_pere FROM production.livraison_numerique WHERE  id = ? ";
	
	public static final String SQL_SELECT_COUVERTURE_BY_ID_LIVRAISON = "SELECT recuperation_couverture AS link_cover FROM referentiel.fournisseur AS fournisseur INNER JOIN referentiel.source_appro AS source ON source.id_fournisseur = fournisseur.id INNER JOIN production.livraison_numerique AS livraison ON livraison.id_source_appro = source.id WHERE livraison.id = ? ";
		
	public static final String SQL_UPDATE_COUVERTURE_TYPE_CONTENU_LIVRAISON_CONTENU = "UPDATE production.livraison_numerique_contenu SET type_contenu =  ? WHERE id = ? ";
	public static final String SQL_SELECT_LIVRAISON_NUMERIQUE_BY_NOM_FICHIER = "SELECT id_source_appro, contenu_pivot, clef_index_pivot,date_creation FROM production.livraison_numerique WHERE  nom_fichier = ? ";
	
	public static final String SQL_SELECT_COUVERTURE_ID_LIVRAISON_NUMERIQUE_CONTENU_BY_ID_LIVRAISON_NUMERIQUE = "SELECT id, id_source_appro, contenu_pivot from production.livraison_numerique  where id = ? ";
	public static final String SQL_SELECT_REPERTOIRE_COUVERTURE_LOT_BY_ID_LIVRAISON_NUMERIQUE = "SELECT (xpath('/RECEPTION/@cheminLivraisonHash', contenu_pivot))[1]::varchar as cheminHash  from production.livraison_numerique  where nom_fichier = ? ";
	
	public static final String SQL_SELECT_IDENTIFIANT_OCR = "SELECT id_ocr FROM production.identifiant_ocr WHERE disponible = ? ORDER BY date_creation DESC LIMIT ? ";
	public static final String SQL_INSERT_IDENTIFIANT_OCR_BATCH = "COPY production.identifiant_ocr(id_ocr,disponible,commentaire,date_creation)  FROM STDIN WITH CSV   ";
	public static final String SQL_UPDATE_IDENTIFIANT_OCR_INDISPONIBLE = "UPDATE production.identifiant_ocr SET disponible = ? WHERE id_ocr = ? ";
	public static final String SQL_INSERT_ATTRIBUTS_IDENTIFIANT_OCR_BATCH = "COPY production.livraison_numerique_attribut(id_livraison_numerique,nom,valeur,date_creation)  FROM STDIN WITH CSV   ";

	public static final String SQL_SELECT_POINTAGE_LIVRAISON_NUMERIQUE_BY_ID_LIVRAISON_NUMERIQUE = "SELECT contenu_pivot from production.livraison_numerique AS livarison WHERE livarison.id = ? ";
	
	public static final String SQL_SELECT_LIVRAISON_NUMERIQUE_ATTRIBUTS_BY_NAME = "SELECT livraison_numerique_attribut.nom, livraison_numerique_attribut.valeur FROM production.livraison_numerique_attribut WHERE id_livraison_numerique = ? AND livraison_numerique_attribut.nom = ?";
	
	public static final String SQL_SELECT_ID_GPS_BY_ID_LIVRAISON_NUMERIQUE ="SELECT mod.id_gps FROM referentiel.modalite_appro AS mod INNER JOIN production.livraison_numerique AS livraison ON livraison.id_source_appro = mod.id_source WHERE livraison.id = ? ";

	public static final String SQL_SELECT_MODALITE_APPRO_BY_ID_SOURCE ="SELECT modalite_appro.* FROM referentiel.modalite_appro WHERE modalite_appro.id_source = :id_source LIMIT 1";

	public static final String SQL_SELECT_FOURNISSEUR_LIVRAISON_NUMERIQUE = "SELECT fournisseur.nom AS nom FROM production.livraison_numerique AS livraison INNER JOIN referentiel.source_appro AS source ON livraison.id_source_appro = source.id INNER JOIN referentiel.fournisseur AS fournisseur ON fournisseur.id = source.id_fournisseur WHERE livraison.id = ? ";
	
	public static final String SQL_UPDATE_LIVRAISON_NUMERIQUE_XML= "UPDATE production.livraison_numerique SET contenu_pivot =  ? WHERE id = ? ";
	public static final String SQL_UPDATE_LIVRAISON_NUMERIQUE_XML_BY_UNIQUE_NAME= "UPDATE production.livraison_numerique SET contenu_pivot =  ? WHERE nom = ? ";
	
	public static final String SQL_SELECT_NBRE_SOUS_LOTS_BY_ID_LIVRAISON_NUMERIQUE ="SELECT count(*) AS nbre_sous_lots FROM production.livraison_numerique WHERE id_pere = ? ";
	public static final String SQL_SELECT_ALL_SOUS_LOTS_BY_ID_LIVRAISON_NUMERIQUE ="SELECT contenu_pivot,id FROM production.livraison_numerique WHERE id_pere = ? ";
	public static final String SQL_SELECT_LOT_PERE_BY_ID_SOUS_LOTS ="SELECT contenu_pivot,id FROM production.livraison_numerique WHERE id_pere = ? ";
	public static final String SQL_SELECT_ID_LOT_PERE = "SELECT id_pere FROM production.livraison_numerique WHERE id = ? ";
	public static final String SQL_SELECT_UNIQUE_NAME = "SELECT nom FROM production.livraison_numerique WHERE id = ? ";
	public static final String SQL_SELECT_PREMIERE_PAGE_LIVRAISON_NUMERIQUE =" SELECT id FROM production.livraison_numerique_contenu WHERE id_livraison_numerique = ? AND type_fichier != 'xml' GROUP BY id,nom ORDER BY id,nom limit 1 ";
	
	public static final String SQL_SELECT_WS_ADDRESS = "SELECT mod.machine||':'||port_ws AS adresse,COUNT(enc.id_type_module) AS cn FROM production.modules AS mod INNER JOIN referentiel.type_module AS typemod ON typemod.id = mod.id_type_module LEFT JOIN production.encours AS enc ON enc.id_type_module = mod.id_type_module WHERE typemod.nom =? GROUP BY mod.id_type_module,typemod.nom,mod.machine,port_ws ORDER BY cn ASC LIMIT 1";
	
	public static final String SQL_DELETE_TICKETS_EN_COURS_APRES_TRAITEMENT_PAR_MODULE = "DELETE FROM production.encours WHERE  id_livraison_numerique = ? AND id_type_module = (select id from referentiel.type_module where nom = ?)";
	
	public static final String SQL_INSERT_SUIVI_ETAPE= "INSERT INTO production.suivi_etape(nom,login_utilisateur,machine,id_livraison_numerique,id_parent) VALUES(?,?,?,?,?)";
	public static final String SQL_INSERT_SUIVI_ETAPE_ATTRIBUT= "INSERT INTO production.suivi_etape_attribut(id_suivi_etape,nom,valeur,quantite) VALUES((SELECT id FROM production.suivi_etape WHERE id_livraison_numerique = ? AND nom =?),?,?,?)";
	public static final String SQL_SELECT_ID_SUIVI_ETAPE_ID_PARENT= "SELECT id FROM referentiel.type_module m1 WHERE id_type_module_suivant = (SELECT id FROM referentiel.type_module WHERE nom = ? ) ";
	public static final String SQL_UPDATE_SUIVI_ETAPE = "UPDATE production.suivi_etape SET id_ocr=?,retour_traitement=?,date_fin=?,code_erreur=?,libelle_erreur=?,commentaire=? WHERE id_livraison_numerique = ? AND nom = ? ";
	
	public static final String SQL_SELECT_MODALITE_APPRO_BY_ID_SOURCE_EXISTS ="SELECT modalite_appro.* FROM referentiel.modalite_appro WHERE modalite_appro.id_source = ? LIMIT 1 ";
}
