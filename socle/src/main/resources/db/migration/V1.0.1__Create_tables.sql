-- Schema: production

-- DROP SCHEMA production;

CREATE SCHEMA production
  AUTHORIZATION postgres;

GRANT ALL ON SCHEMA production TO postgres;
GRANT ALL ON SCHEMA production TO public;
COMMENT ON SCHEMA production
  IS 'Schema de production - contient les tables des modules en cours d''execution, les tables de tracabilite ainsi que les tables de testes automatiques et de logs  ';

-- Table: production.modules

--DROP TABLE production.modules;

CREATE TABLE production.modules
(
  id integer NOT NULL DEFAULT nextval(('production.seq_modules'::text)::regclass), -- Identifiant de la table
  id_type_module integer NOT NULL, -- Type du module referencer ( traitement image, OCR, ...) issue de la table des types de modules
  machine character varying(50) NOT NULL,
  login_utilisateur character varying(60) NOT NULL, -- Login utilisateur
  port_ws integer NOT NULL, -- Port d'ecoute du web service de consommation des tickets
  port_supervision integer NOT NULL, -- Port de supervision
  etat_mise_a_jour_autotest integer NOT NULL, -- Identifiant du status de l'auto test du module
  id_status integer NOT NULL, -- Identifiant du status du module
  date_dernier_traitement timestamp with time zone NOT NULL, -- Date et heure du dernier retour test
  date timestamp with time zone NOT NULL DEFAULT now(), -- Date et heure de referencement (a la creation de la ligne)
  CONSTRAINT pk_modules PRIMARY KEY (id), -- Cle primaire de la table
  CONSTRAINT uk_type_module_machine_port_supervision UNIQUE (id_type_module, machine, port_supervision), -- Contrainte d'unicite sur le type du module, la machine et le port de supervision
  CONSTRAINT uk_type_module_machine_portws UNIQUE (id_type_module, machine, port_ws), -- Contrainte d'unicite sur le type du module, machine et le port d'ecoute du web service
  CONSTRAINT uk_type_module_machine_utilisateur UNIQUE (id_type_module, machine, login_utilisateur) -- Contrainte d'unicite sur le type du module, la machine et l'utilisateur
)
WITH (
  OIDS=FALSE
);
ALTER TABLE production.modules
  OWNER TO postgres;
COMMENT ON TABLE production.modules
  IS 'Table de referencement des modules';
COMMENT ON COLUMN production.modules.id IS 'Identifiant de la table';
COMMENT ON COLUMN production.modules.id_type_module IS 'Type du module referencer ( traitement image, OCR, ...) issue de la table des types de modules';
COMMENT ON COLUMN production.modules.login_utilisateur IS 'Login utilisateur';
COMMENT ON COLUMN production.modules.port_ws IS 'Port d''ecoute du web service de consommation des tickets ';
COMMENT ON COLUMN production.modules.port_supervision IS 'Port de supervision';
COMMENT ON COLUMN production.modules.etat_mise_a_jour_autotest IS 'Identifiant du status de l''auto test du module';
COMMENT ON COLUMN production.modules.id_status IS 'Identifiant du status du module';
COMMENT ON COLUMN production.modules.date_dernier_traitement IS 'Date et heure du dernier retour test';
COMMENT ON COLUMN production.modules.date IS 'Date et heure de referencement (a la creation de la ligne) ';

COMMENT ON CONSTRAINT pk_modules ON production.modules IS 'Cle primaire de la table';
COMMENT ON CONSTRAINT uk_type_module_machine_port_supervision ON production.modules IS 'Contrainte d''unicite sur le type du module, la machine et le port de supervision ';
COMMENT ON CONSTRAINT uk_type_module_machine_portws ON production.modules IS 'Contrainte d''unicite sur le type du module, machine et le port d''ecoute du web service';
COMMENT ON CONSTRAINT uk_type_module_machine_utilisateur ON production.modules IS 'Contrainte d''unicite sur le type du module, la machine et l''utilisateur';


-- Index: production.fki_etat_maj_autotest

-- DROP INDEX production.fki_etat_maj_autotest;

CREATE INDEX fki_etat_maj_autotest
  ON production.modules
  USING btree
  (etat_mise_a_jour_autotest);

-- Index: production.fki_id_type_module

-- DROP INDEX production.fki_id_type_module;

CREATE INDEX fki_id_type_module
  ON production.modules
  USING btree
  (id_type_module);

-- Index: production.fki_status

-- DROP INDEX production.fki_status;

CREATE INDEX fki_status
  ON production.modules
  USING btree
  (id_status);

-- Table: production.autotest

--DROP TABLE production.autotest;

CREATE TABLE production.autotest
(
  id integer NOT NULL DEFAULT nextval(('production.seq_autotest'::text)::regclass), -- Identifiant de la table
  id_module_reference integer, -- Identifiant du module de reference
  id_type_module integer NOT NULL, -- Type du module referencer ( traitement image, OCR, ...) issue de la table des types de modules
  machine character varying(50) NOT NULL, -- Machine d'execution du test
  login_utilisateur character varying(60) NOT NULL, -- Login utilisateur
  id_status integer NOT NULL, -- Identifiant du status
  retour xml, -- Champs XML contenant le retour xml du test
  date_debut timestamp with time zone NOT NULL, -- Dat et heure de debut du test
  date_fin timestamp with time zone, -- Date et heure de fin de test
  date timestamp with time zone NOT NULL DEFAULT now(), -- Dat et heure de creation de la ligne
  CONSTRAINT pk_autotest PRIMARY KEY (id), -- Cle primaire de la table
  CONSTRAINT fk_id_module_reference FOREIGN KEY (id_module_reference)
      REFERENCES production.modules (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION -- Cle etrangere vers l'identifiant du module de reference --> table modules
)
WITH (
  OIDS=FALSE
);
ALTER TABLE production.autotest
  OWNER TO postgres;
COMMENT ON TABLE production.autotest
  IS 'Table autotest';
COMMENT ON COLUMN production.autotest.id IS 'Identifiant de la table';
COMMENT ON COLUMN production.autotest.id_module_reference IS 'Identifiant du module de reference';
COMMENT ON COLUMN production.autotest.id_type_module IS 'Type du module referencer ( traitement image, OCR, ...) issue de la table des types de modules';
COMMENT ON COLUMN production.autotest.machine IS 'Machine d''execution du test';
COMMENT ON COLUMN production.autotest.login_utilisateur IS 'Login utilisateur';
COMMENT ON COLUMN production.autotest.id_status IS 'Identifiant du status';
COMMENT ON COLUMN production.autotest.retour IS 'Champs XML contenant le retour xml du test';
COMMENT ON COLUMN production.autotest.date_debut IS 'Dat et heure de debut du test';
COMMENT ON COLUMN production.autotest.date_fin IS 'Date et heure de fin de test';
COMMENT ON COLUMN production.autotest.date IS 'Dat et heure de creation de la ligne ';

COMMENT ON CONSTRAINT pk_autotest ON production.autotest IS 'Cle primaire de la table';
COMMENT ON CONSTRAINT fk_id_module_reference ON production.autotest IS 'Cle etrangere vers l''identifiant du module de reference --> table modules';


-- Index: production.fki_id_module_reference

-- DROP INDEX production.fki_id_module_reference;

CREATE INDEX fki_id_module_reference
  ON production.autotest
  USING btree
  (id_module_reference);

-- Index: production.fki_statut

-- DROP INDEX production.fki_statut;

CREATE INDEX fki_statut
  ON production.autotest
  USING btree
  (id_status);

-- Index: production.fki_type_module

-- DROP INDEX production.fki_type_module;

CREATE INDEX fki_type_module
  ON production.autotest
  USING btree
  (id_type_module);

-- Table: production.encours

--DROP TABLE production.encours;

CREATE TABLE production.encours
(
  id integer NOT NULL DEFAULT nextval(('production.seq_encours'::text)::regclass), -- Identifiant de la table
  id_ocr character varying(13), -- identifiant OCR /publication Presse
  id_produit character varying(13), -- Identifiant coupure presse
  type character varying(10), -- Type du ticket
  priorite integer NOT NULL, -- Priorite de la tache a realise ( sert pour le tri automatique de l'objet vecteur contenant les tickets)
  contenu_ticket xml NOT NULL, -- Contenu integrale du ticket a consommer
  login_utilisateur character varying(50) NOT NULL, -- Login de la session utilisateur ayant realise le traitement
  machine character varying(50) NOT NULL, -- Nom de poste sur lequel le traitement s'est realise
  id_type_module integer,
  date timestamp with time zone,
  mode_approvisonnement character varying(30), -- le mode d'approvisonnement
  id_livraison_numerique bigint NOT NULL, -- identifiant de la livraison numérique
  CONSTRAINT pk_encours PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE production.encours
  OWNER TO postgres;
COMMENT ON TABLE production.encours
  IS 'Table des modules/traitements en cours';
COMMENT ON COLUMN production.encours.id IS 'Identifiant de la table';
COMMENT ON COLUMN production.encours.id_ocr IS 'identifiant OCR /publication Presse';
COMMENT ON COLUMN production.encours.id_produit IS 'Identifiant coupure presse';
COMMENT ON COLUMN production.encours.type IS 'Type du ticket';
COMMENT ON COLUMN production.encours.priorite IS 'Priorite de la tache a realise ( sert pour le tri automatique de l''objet vecteur contenant les tickets)';
COMMENT ON COLUMN production.encours.contenu_ticket IS 'Contenu integrale du ticket a consommer';
COMMENT ON COLUMN production.encours.login_utilisateur IS 'Login de la session utilisateur ayant realise le traitement';
COMMENT ON COLUMN production.encours.machine IS 'Nom de poste sur lequel le traitement s''est realise';
COMMENT ON COLUMN production.encours.mode_approvisonnement IS 'le mode d''approvisonnement ';
COMMENT ON COLUMN production.encours.id_livraison_numerique IS 'identifiant de la livraison numérique';

-- Table: production.livraison_numerique

--DROP TABLE production.livraison_numerique;

CREATE TABLE production.livraison_numerique
(
  id bigint NOT NULL DEFAULT nextval(('production.seq_livraison_numerique'::text)::regclass), -- Identifiant unique de la livraison
  bp_index bigint, -- Valeur bp_index de TradExpress
  id_source_appro bigint NOT NULL, -- Identifiant de la source d'appro
  nom character varying(255) NOT NULL, -- Nom unique de la livraison
  nom_fichier character varying(255), -- Nom fichier recu
  date_creation timestamp with time zone NOT NULL DEFAULT now(), -- Identifiant du lot
  id_status integer NOT NULL, -- Identifiant du statut
  contenu_pivot xml,
  chemin_livraison_hash character varying(200), -- chemin complet du fichier sur hash temporaire
  chemin_livraison_archivage character varying(200), -- Chemin complet du fichier sur hash d'archivage
  clef_index_pivot text,
  id_pere bigint,
  CONSTRAINT pk_livraison_numerique PRIMARY KEY (id),
  CONSTRAINT uk_nom UNIQUE (nom)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE production.livraison_numerique
  OWNER TO postgres;
COMMENT ON TABLE production.livraison_numerique
  IS 'Table Livraison_numerique';
COMMENT ON COLUMN production.livraison_numerique.id IS 'Identifiant unique de la livraison';
COMMENT ON COLUMN production.livraison_numerique.bp_index IS 'Valeur bp_index de TradExpress';
COMMENT ON COLUMN production.livraison_numerique.id_source_appro IS 'Identifiant de la source d''appro';
COMMENT ON COLUMN production.livraison_numerique.nom IS 'Nom unique de la livraison';
COMMENT ON COLUMN production.livraison_numerique.nom_fichier IS 'Nom fichier recu';
COMMENT ON COLUMN production.livraison_numerique.date_creation IS 'Identifiant du lot';
COMMENT ON COLUMN production.livraison_numerique.id_status IS 'Identifiant du statut';
COMMENT ON COLUMN production.livraison_numerique.chemin_livraison_hash IS 'chemin complet du fichier sur hash temporaire';
COMMENT ON COLUMN production.livraison_numerique.chemin_livraison_archivage IS 'Chemin complet du fichier sur hash d''archivage ';


-- Table: production.livraison_numerique_attribut

--DROP TABLE production.livraison_numerique_attribut;

CREATE TABLE production.livraison_numerique_attribut
(
  id bigint NOT NULL DEFAULT nextval(('production.seq_livraison_numerique_attribut'::text)::regclass), -- Identifiant unique de la livraison valeur bpindex de tradexpress
  id_livraison_numerique bigint NOT NULL, -- identifient de la livraison numerique
  nom character varying(200), -- nom de l’attribut: “date_parution”,”date_faciale” a substituer dans les xml produit
  valeur text NOT NULL, -- Valeur de l'attribut
  date_creation timestamp with time zone NOT NULL -- Date de création de l'enregistrement
)
WITH (
  OIDS=FALSE
);
ALTER TABLE production.livraison_numerique_attribut
  OWNER TO postgres;
COMMENT ON COLUMN production.livraison_numerique_attribut.id IS 'Identifiant unique de la livraison valeur bpindex de tradexpress';
COMMENT ON COLUMN production.livraison_numerique_attribut.id_livraison_numerique IS 'identifient de la livraison numerique';
COMMENT ON COLUMN production.livraison_numerique_attribut.nom IS 'nom de l’attribut: “date_parution”,”date_faciale” a substituer dans les xml produit';
COMMENT ON COLUMN production.livraison_numerique_attribut.valeur IS 'Valeur de l''attribut';
COMMENT ON COLUMN production.livraison_numerique_attribut.date_creation IS 'Date de création de l''enregistrement';

-- Table: production.livraison_numerique_contenu

--DROP TABLE production.livraison_numerique_contenu;

CREATE TABLE production.livraison_numerique_contenu
(
  id bigint NOT NULL DEFAULT nextval(('production.seq_livraison_numerique_contenu'::text)::regclass), -- Identifiant unique de la livraison - valeur bpindex de TradExpress
  id_livraison_numerique bigint NOT NULL, -- Identifiant de la livraison numerique
  nom character varying(200), -- Nom du contenu
  date_creation timestamp with time zone NOT NULL DEFAULT now(), -- Date de creation de l'enregistrement
  status character varying(50), -- Status du contenu : doublon, original, ...
  id_livraison_numerique_contenu_doublon bigint, -- Si doublon identifiant de la livraison contenu l'enregistrement original
  contenu_pivot xml, -- Contenu au format pivot du contenu
  type_fichier character varying(10), -- xml, pdf, jpeg, ...
  type_contenu character varying(20), -- couverture, page, ...
  CONSTRAINT pk_livraison_numerique_contenu PRIMARY KEY (id),
  CONSTRAINT fk_livraison_numerique FOREIGN KEY (id_livraison_numerique)
      REFERENCES production.livraison_numerique (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_livraison_numerique_contenu FOREIGN KEY (id_livraison_numerique_contenu_doublon)
      REFERENCES production.livraison_numerique_contenu (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE production.livraison_numerique_contenu
  OWNER TO postgres;
COMMENT ON TABLE production.livraison_numerique_contenu
  IS 'Table livraison_numerique_contenu';
COMMENT ON COLUMN production.livraison_numerique_contenu.id IS 'Identifiant unique de la livraison - valeur bpindex de TradExpress';
COMMENT ON COLUMN production.livraison_numerique_contenu.id_livraison_numerique IS 'Identifiant de la livraison numerique';
COMMENT ON COLUMN production.livraison_numerique_contenu.nom IS 'Nom du contenu';
COMMENT ON COLUMN production.livraison_numerique_contenu.date_creation IS 'Date de creation de l''enregistrement';
COMMENT ON COLUMN production.livraison_numerique_contenu.status IS 'Status du contenu : doublon, original, ...';
COMMENT ON COLUMN production.livraison_numerique_contenu.id_livraison_numerique_contenu_doublon IS 'Si doublon identifiant de la livraison contenu l''enregistrement original';
COMMENT ON COLUMN production.livraison_numerique_contenu.contenu_pivot IS 'Contenu au format pivot du contenu';
COMMENT ON COLUMN production.livraison_numerique_contenu.type_fichier IS 'xml, pdf, jpeg, ...';
COMMENT ON COLUMN production.livraison_numerique_contenu.type_contenu IS 'couverture, page, ...';


-- Index: production.fki_livraison_numerique

-- DROP INDEX production.fki_livraison_numerique;

CREATE INDEX fki_livraison_numerique
  ON production.livraison_numerique_contenu
  USING btree
  (id_livraison_numerique);

-- Index: production.fki_livraison_numerique_contenu

-- DROP INDEX production.fki_livraison_numerique_contenu;

CREATE INDEX fki_livraison_numerique_contenu
  ON production.livraison_numerique_contenu
  USING btree
  (id_livraison_numerique_contenu_doublon);

-- Table: production.logs

--DROP TABLE production.logs;

CREATE TABLE production.logs
(
  id integer NOT NULL DEFAULT nextval(('production.seq_logs'::text)::regclass), -- Identifiant unique de la table
  id_ocr character varying(13), -- Identifiant OCR/Publication Presse
  id_produit character varying(13), -- Identifiant coupure Presse
  login_utilisateur character varying(50) NOT NULL, -- Login de la session utilisateur ayant ecrit le lignalement lie au traitement realise
  machine character varying(50) NOT NULL, -- Nom de la machine d'execution
  date_log timestamp with time zone NOT NULL DEFAULT now(), -- Date et heure de creation de la ligne
  description character varying(2000) NOT NULL, -- Description du contexte du signalement propre au traitement du module concerne
  date_debut timestamp with time zone NOT NULL, -- date debut du traitement
  date_fin timestamp with time zone, -- Dat de fin du traitement
  nom_module character varying(50), -- nom du module dans la table type_module
  CONSTRAINT pk_logs PRIMARY KEY (id) -- Cle primaire de la table
)
WITH (
  OIDS=FALSE
);
ALTER TABLE production.logs
  OWNER TO postgres;
COMMENT ON TABLE production.logs
  IS 'La table des logs';
COMMENT ON COLUMN production.logs.id IS 'Identifiant unique de la table';
COMMENT ON COLUMN production.logs.id_ocr IS 'Identifiant OCR/Publication Presse';
COMMENT ON COLUMN production.logs.id_produit IS 'Identifiant coupure Presse';
COMMENT ON COLUMN production.logs.login_utilisateur IS 'Login de la session utilisateur ayant ecrit le lignalement lie au traitement realise';
COMMENT ON COLUMN production.logs.machine IS 'Nom de la machine d''execution';
COMMENT ON COLUMN production.logs.date_log IS 'Date et heure de creation de la ligne';
COMMENT ON COLUMN production.logs.description IS 'Description du contexte du signalement propre au traitement du module concerne';
COMMENT ON COLUMN production.logs.date_debut IS 'date debut du traitement';
COMMENT ON COLUMN production.logs.date_fin IS 'Dat de fin du traitement';
COMMENT ON COLUMN production.logs.nom_module IS 'nom du module dans la table type_module';

COMMENT ON CONSTRAINT pk_logs ON production.logs IS 'Cle primaire de la table';

-- Table: production.suivi_etape

--DROP TABLE production.suivi_etape;

CREATE TABLE production.suivi_etape
(
  id integer NOT NULL DEFAULT nextval(('production.seq_suivi_etape'::text)::regclass), -- Identifiant unique pour la table
  pca integer NOT NULL DEFAULT 0, -- Ce champ permet de savoir si la donnee vient de PCA. L'objectif est de ne pas avoir de doublons d'identifiant lors de la reconciliation de donnees.
  id_ocr character varying(13), -- Identifiant OCR Presse
  id_produit character varying(13), -- Identifiant coupure Presse
  num_page integer NOT NULL, -- Numero de la page traitee
  nom character varying(50) NOT NULL, -- Nom de l'etape (utilise comme clef dans les requetes)
  login_utilisateur character varying(50) NOT NULL, -- Login de la session utilisateur ayant realise le traitement
  machine character varying(50) NOT NULL, -- Nom de poste sur lequel le traitement s'est realise.
  retour_traitement xml,
  date_creation timestamp with time zone NOT NULL DEFAULT now(), -- Date et heure de creation de la ligne
  date_debut timestamp with time zone NOT NULL DEFAULT now(), -- Date et heure de debut de l'etape
  date_fin timestamp with time zone, -- Date et heure de fin de l'etape
  code_erreur character varying(3), -- Code erreure si le traitement a genere une erreur
  libelle_erreur character varying(60), -- Libelle de l'erreur generee
  id_parent integer, -- Identifiant de l'etape parente pour reconstituer le chainage des etapes dans les requetes hierarchique
  commentaire character varying(50),
  pca_parent integer,
  CONSTRAINT pk_suivi_etape PRIMARY KEY (id, pca),
  CONSTRAINT fk_suivi_etape_parent FOREIGN KEY (id_parent, pca_parent)
      REFERENCES production.suivi_etape (id, pca) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE production.suivi_etape
  OWNER TO postgres;
COMMENT ON TABLE production.suivi_etape
  IS 'La table suivi_etape est une table de tracabilite et d''analyse, elle contient les les differentes etape d''execution d''un module/traitement ';
COMMENT ON COLUMN production.suivi_etape.id IS 'Identifiant unique pour la table';
COMMENT ON COLUMN production.suivi_etape.pca IS 'Ce champ permet de savoir si la donnee vient de PCA. L''objectif est de ne pas avoir de doublons d''identifiant lors de la reconciliation de donnees.';
COMMENT ON COLUMN production.suivi_etape.id_ocr IS 'Identifiant OCR Presse';
COMMENT ON COLUMN production.suivi_etape.id_produit IS 'Identifiant coupure Presse
';
COMMENT ON COLUMN production.suivi_etape.num_page IS 'Numero de la page traitee';
COMMENT ON COLUMN production.suivi_etape.nom IS 'Nom de l''etape (utilise comme clef dans les requetes)';
COMMENT ON COLUMN production.suivi_etape.login_utilisateur IS 'Login de la session utilisateur ayant realise le traitement';
COMMENT ON COLUMN production.suivi_etape.machine IS 'Nom de poste sur lequel le traitement s''est realise.';
COMMENT ON COLUMN production.suivi_etape.date_creation IS 'Date et heure de creation de la ligne';
COMMENT ON COLUMN production.suivi_etape.date_debut IS 'Date et heure de debut de l''etape';
COMMENT ON COLUMN production.suivi_etape.date_fin IS 'Date et heure de fin de l''etape';
COMMENT ON COLUMN production.suivi_etape.code_erreur IS 'Code erreure si le traitement a genere une erreur';
COMMENT ON COLUMN production.suivi_etape.libelle_erreur IS 'Libelle de l''erreur generee';
COMMENT ON COLUMN production.suivi_etape.id_parent IS 'Identifiant de l''etape parente pour reconstituer le chainage des etapes dans les requetes hierarchique';


-- Index: production.fki_suivi_etape_parent

-- DROP INDEX production.fki_suivi_etape_parent;

CREATE INDEX fki_suivi_etape_parent
  ON production.suivi_etape
  USING btree
  (id_parent, pca_parent);

-- Table: production.suivi_etape_attribut

--DROP TABLE production.suivi_etape_attribut;

CREATE TABLE production.suivi_etape_attribut
(
  id integer NOT NULL DEFAULT nextval(('production.seq_suivi_etape_attribut'::text)::regclass), -- Identifiant unique de la table
  pca integer NOT NULL DEFAULT 0,
  id_suivi_etape integer,
  nom character varying(50) NOT NULL,
  valeur text,
  quantite integer,
  pca_suivi_etape integer NOT NULL DEFAULT 0,
  CONSTRAINT pk_suivi_etape_attribut PRIMARY KEY (id, pca),
  CONSTRAINT fk_suivi_etape FOREIGN KEY (id_suivi_etape, pca_suivi_etape)
      REFERENCES production.suivi_etape (id, pca) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE production.suivi_etape_attribut
  OWNER TO postgres;
COMMENT ON TABLE production.suivi_etape_attribut
  IS 'La table suiv_etape_attribut contient les attributs lies a un suivi_etape';
COMMENT ON COLUMN production.suivi_etape_attribut.id IS 'Identifiant unique de la table';


-- Index: production.fki_suivi_etape

-- DROP INDEX production.fki_suivi_etape;

CREATE INDEX fki_suivi_etape
  ON production.suivi_etape_attribut
  USING btree
  (id_suivi_etape, pca_suivi_etape);

-- Table: production.identifiant_ocr

--DROP TABLE production.identifiant_ocr;

CREATE TABLE production.identifiant_ocr
(
  id integer NOT NULL DEFAULT nextval(('production.seq_identifiant_ocr'::text)::regclass), -- Identifiant de la table identifiant_ocr
  id_ocr character varying(20) NOT NULL, -- Identifiant OCR
  disponible boolean NOT NULL DEFAULT false, -- La valeur de ce champ indique si l'identifiant OCR est disponible ou pas :...
  commentaire character varying(200),
  date_creation timestamp with time zone, -- Date de creation
  CONSTRAINT pk_identifiant_ocr PRIMARY KEY (id), -- Cle primaire de la table identifiant OCR
  CONSTRAINT uk_id_ocr UNIQUE (id_ocr)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE production.identifiant_ocr
  OWNER TO postgres;
COMMENT ON COLUMN production.identifiant_ocr.id IS 'Identifiant de la table identifiant_ocr';
COMMENT ON COLUMN production.identifiant_ocr.id_ocr IS 'Identifiant OCR';
COMMENT ON COLUMN production.identifiant_ocr.disponible IS 'La valeur de ce champ indique si l''identifiant OCR est disponible ou pas :

1. TRUE : disponible
2. FALSE : non disponible';
COMMENT ON COLUMN production.identifiant_ocr.date_creation IS 'Date de creation';

COMMENT ON CONSTRAINT pk_identifiant_ocr ON production.identifiant_ocr IS 'Cle primaire de la table identifiant OCR';

-- Sequence: production.seq_autotest

-- DROP SEQUENCE production.seq_autotest;

CREATE SEQUENCE production.seq_autotest
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 180036
  CACHE 1;
ALTER TABLE production.seq_autotest
  OWNER TO postgres;

-- Sequence: production.seq_encours

-- DROP SEQUENCE production.seq_encours;

CREATE SEQUENCE production.seq_encours
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 15756
  CACHE 1;
ALTER TABLE production.seq_encours
  OWNER TO postgres;
  
  
-- Sequence: production.seq_livraison_numerique

-- DROP SEQUENCE production.seq_livraison_numerique;

CREATE SEQUENCE production.seq_livraison_numerique
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 21444
  CACHE 1;
ALTER TABLE production.seq_livraison_numerique
  OWNER TO postgres;

  -- Sequence: production.seq_livraison_numerique_attribut

-- DROP SEQUENCE production.seq_livraison_numerique_attribut;

CREATE SEQUENCE production.seq_livraison_numerique_attribut
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 23
  CACHE 1;
ALTER TABLE production.seq_livraison_numerique_attribut
  OWNER TO postgres;

  -- Sequence: production.seq_livraison_numerique_contenu

-- DROP SEQUENCE production.seq_livraison_numerique_contenu;

CREATE SEQUENCE production.seq_livraison_numerique_contenu
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 252696
  CACHE 1;
ALTER TABLE production.seq_livraison_numerique_contenu
  OWNER TO postgres;

  -- Sequence: production.seq_logs

-- DROP SEQUENCE production.seq_logs;

CREATE SEQUENCE production.seq_logs
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 259212
  CACHE 1;
ALTER TABLE production.seq_logs
  OWNER TO postgres;
  
  -- Sequence: production.seq_modules

-- DROP SEQUENCE production.seq_modules;

CREATE SEQUENCE production.seq_modules
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 830
  CACHE 1;
ALTER TABLE production.seq_modules
  OWNER TO postgres;

  -- Sequence: production.seq_suivi_etape

-- DROP SEQUENCE production.seq_suivi_etape;

CREATE SEQUENCE production.seq_suivi_etape
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 160001
  CACHE 1;
ALTER TABLE production.seq_suivi_etape
  OWNER TO postgres;
  
 -- Sequence: production.seq_suivi_etape_attribut

-- DROP SEQUENCE production.seq_suivi_etape_attribut;

CREATE SEQUENCE production.seq_suivi_etape_attribut
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 160001
  CACHE 1;
ALTER TABLE production.seq_suivi_etape_attribut
  OWNER TO postgres;
  
  
 -- Sequence: production.seq_identifiant_ocr

-- DROP SEQUENCE production.seq_identifiant_ocr;

CREATE SEQUENCE production.seq_identifiant_ocr
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 60
  CACHE 1;
ALTER TABLE production.seq_identifiant_ocr
  OWNER TO postgres;

-- Schema: historisation

-- DROP SCHEMA historisation;

CREATE SCHEMA historisation
  AUTHORIZATION postgres;

COMMENT ON SCHEMA historisation
  IS 'Schema d''historisation';

  
  -- Table: historisation.autotest

--DROP TABLE historisation.autotest;

CREATE TABLE historisation.autotest
(
  id integer NOT NULL, -- Identifiant unique de la table
  id_module_reference integer, -- Identifiant du module reference
  id_type_module integer NOT NULL, -- Type du module referencer ( traitement image, OCR, ...) issue de la table des types de modules
  machine character varying(50) NOT NULL, -- Machine d'execution du test
  login_utilisateur character varying(60) NOT NULL, -- Login utilisateur
  status integer NOT NULL, -- Identifiant du status
  retour xml, -- Champs XML contenant le retout xml
  date_debut timestamp with time zone NOT NULL,
  date_fin timestamp with time zone,
  date timestamp with time zone DEFAULT now(),
  CONSTRAINT pk_autotest PRIMARY KEY (id),
  CONSTRAINT fk_id_module_reference FOREIGN KEY (id_module_reference)
      REFERENCES production.modules (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE historisation.autotest
  OWNER TO postgres;
COMMENT ON TABLE historisation.autotest
  IS 'La table autotest du schema historisation';
COMMENT ON COLUMN historisation.autotest.id IS 'Identifiant unique de la table';
COMMENT ON COLUMN historisation.autotest.id_module_reference IS 'Identifiant du module reference';
COMMENT ON COLUMN historisation.autotest.id_type_module IS 'Type du module referencer ( traitement image, OCR, ...) issue de la table des types de modules';
COMMENT ON COLUMN historisation.autotest.machine IS 'Machine d''execution du test';
COMMENT ON COLUMN historisation.autotest.login_utilisateur IS 'Login utilisateur';
COMMENT ON COLUMN historisation.autotest.status IS 'Identifiant du status';
COMMENT ON COLUMN historisation.autotest.retour IS 'Champs XML contenant le retout xml';


-- Index: historisation.fki_id_module_reference

-- DROP INDEX historisation.fki_id_module_reference;

CREATE INDEX fki_id_module_reference
  ON historisation.autotest
  USING btree
  (id_module_reference);

-- Index: historisation.fki_type_module

-- DROP INDEX historisation.fki_type_module;

CREATE INDEX fki_type_module
  ON historisation.autotest
  USING btree
  (id_type_module);

-- Table: historisation.logs

--DROP TABLE historisation.logs;

CREATE TABLE historisation.logs
(
  id integer NOT NULL, -- Identifiant unique de la table
  id_ocr character varying(13), -- Identifiant OCR /publication Presse
  id_produit character varying(13), -- Identifiant coupure Presse
  machine character varying(50) NOT NULL, -- Machine d'execution
  login_utilisateur character varying(60) NOT NULL, -- Login utilisateur
  date_log timestamp with time zone NOT NULL DEFAULT now(), -- Date et heure de creation de la ligne
  description character varying(500) NOT NULL, -- Description du contexte du signalement propre au traitement du module concerne
  date_debut timestamp with time zone NOT NULL, -- Date de debut de traitement
  date_fin timestamp with time zone, -- Dat de fin de traitement
  CONSTRAINT pk_logs PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE historisation.logs
  OWNER TO postgres;
COMMENT ON TABLE historisation.logs
  IS 'La table des logs dans le schema historisation';
COMMENT ON COLUMN historisation.logs.id IS 'Identifiant unique de la table';
COMMENT ON COLUMN historisation.logs.id_ocr IS 'Identifiant OCR /publication Presse ';
COMMENT ON COLUMN historisation.logs.id_produit IS 'Identifiant coupure Presse';
COMMENT ON COLUMN historisation.logs.machine IS 'Machine d''execution';
COMMENT ON COLUMN historisation.logs.login_utilisateur IS 'Login utilisateur';
COMMENT ON COLUMN historisation.logs.date_log IS 'Date et heure de creation de la ligne';
COMMENT ON COLUMN historisation.logs.description IS 'Description du contexte du signalement propre au traitement du module concerne';
COMMENT ON COLUMN historisation.logs.date_debut IS 'Date de debut de traitement';
COMMENT ON COLUMN historisation.logs.date_fin IS 'Dat de fin de traitement';

-- Table: historisation.suivi_etape

--DROP TABLE historisation.suivi_etape;

CREATE TABLE historisation.suivi_etape
(
  id integer NOT NULL, -- Identifiant de la table
  pca integer NOT NULL, -- Permet de savoir si la donne vient de PCA est de ne pas avoir de doublon d'identifiant lors de la reconciliation de donnees
  id_ocr character varying(13), -- Identifiant OCR Presse
  id_produit character varying(13), -- Identifiant coupure Presse
  num_page integer NOT NULL, -- Numero de la page traitee
  nom character varying(50) NOT NULL, -- Nom de l'etape
  login_utilisateur character varying(50) NOT NULL, -- Login de la session utilisateur ayant realisee le traitement
  machine character varying(50) NOT NULL,
  retour_traitement xml, -- Contenu en XML du retour du traitment realise
  date_creation timestamp with time zone NOT NULL DEFAULT now(), -- Date et heure de creation de la ligne
  date_debut timestamp with time zone NOT NULL DEFAULT now(), -- Date et heure de debut de l'etape
  date_fin timestamp with time zone, -- Date et heure de fin de l'etape
  commentaire character varying(50), -- Commentaire porte par l'etape lors du traitement de la matiere
  code_erreur character varying(3), -- Code d'erreur si le traitement a genere une erreur
  libelle_erreur character varying(60), -- Libelle de l'erreur generee
  id_parent integer, -- Identifiant de l'etape parente pour reconstituer le chainage des etapes dans les requetes hierarchiques
  pca_parent integer,
  CONSTRAINT pk_suivi_etape PRIMARY KEY (id, pca),
  CONSTRAINT fk_suivi_etape_parent FOREIGN KEY (id_parent, pca_parent)
      REFERENCES historisation.suivi_etape (id, pca) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE historisation.suivi_etape
  OWNER TO postgres;
COMMENT ON TABLE historisation.suivi_etape
  IS 'Table suivi_etape dans le schema historisation';
COMMENT ON COLUMN historisation.suivi_etape.id IS 'Identifiant de la table';
COMMENT ON COLUMN historisation.suivi_etape.pca IS 'Permet de savoir si la donne vient de PCA est de ne pas avoir de doublon d''identifiant lors de la reconciliation de donnees';
COMMENT ON COLUMN historisation.suivi_etape.id_ocr IS 'Identifiant OCR Presse ';
COMMENT ON COLUMN historisation.suivi_etape.id_produit IS 'Identifiant coupure Presse';
COMMENT ON COLUMN historisation.suivi_etape.num_page IS 'Numero de la page traitee';
COMMENT ON COLUMN historisation.suivi_etape.nom IS 'Nom de l''etape';
COMMENT ON COLUMN historisation.suivi_etape.login_utilisateur IS 'Login de la session utilisateur ayant realisee le traitement';
COMMENT ON COLUMN historisation.suivi_etape.retour_traitement IS 'Contenu en XML du retour du traitment realise ';
COMMENT ON COLUMN historisation.suivi_etape.date_creation IS 'Date et heure de creation de la ligne';
COMMENT ON COLUMN historisation.suivi_etape.date_debut IS 'Date et heure de debut de l''etape ';
COMMENT ON COLUMN historisation.suivi_etape.date_fin IS 'Date et heure de fin de l''etape';
COMMENT ON COLUMN historisation.suivi_etape.commentaire IS 'Commentaire porte par l''etape lors du traitement de la matiere
';
COMMENT ON COLUMN historisation.suivi_etape.code_erreur IS 'Code d''erreur si le traitement a genere une erreur ';
COMMENT ON COLUMN historisation.suivi_etape.libelle_erreur IS 'Libelle de l''erreur generee';
COMMENT ON COLUMN historisation.suivi_etape.id_parent IS 'Identifiant de l''etape parente pour reconstituer le chainage des etapes dans les requetes hierarchiques';


-- Index: historisation.fki_suivi_etape_parent

-- DROP INDEX historisation.fki_suivi_etape_parent;

CREATE INDEX fki_suivi_etape_parent
  ON historisation.suivi_etape
  USING btree
  (id_parent, pca_parent);

-- Table: historisation.suivi_etape_attribut

--DROP TABLE historisation.suivi_etape_attribut;

CREATE TABLE historisation.suivi_etape_attribut
(
  id integer NOT NULL, -- Identifiant unique de la table
  pca integer NOT NULL, -- Permet de savoir si la donne vient de PCA est de ne pas avoir de doublon d'identifiant lors de la reconciliation de donnees
  id_suivi_etape integer, -- Cle etrangere vers la table suivi_etape
  nom character varying(50) NOT NULL, -- Nom de l'attribut
  valeur text, -- Valeur non numerique de l'attribut
  quantite integer, -- Quantite pour l'attribut
  pca_suivi_etape integer,
  CONSTRAINT pk_suivi_etape_attribut PRIMARY KEY (id),
  CONSTRAINT fk_suivi_etape FOREIGN KEY (id_suivi_etape, pca_suivi_etape)
      REFERENCES historisation.suivi_etape (id, pca) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE historisation.suivi_etape_attribut
  OWNER TO postgres;
COMMENT ON COLUMN historisation.suivi_etape_attribut.id IS 'Identifiant unique de la table';
COMMENT ON COLUMN historisation.suivi_etape_attribut.pca IS 'Permet de savoir si la donne vient de PCA est de ne pas avoir de doublon d''identifiant lors de la reconciliation de donnees
';
COMMENT ON COLUMN historisation.suivi_etape_attribut.id_suivi_etape IS 'Cle etrangere vers la table suivi_etape';
COMMENT ON COLUMN historisation.suivi_etape_attribut.nom IS 'Nom de l''attribut';
COMMENT ON COLUMN historisation.suivi_etape_attribut.valeur IS 'Valeur non numerique de l''attribut';
COMMENT ON COLUMN historisation.suivi_etape_attribut.quantite IS 'Quantite pour l''attribut';


-- Index: historisation.fki_suivi_etape

-- DROP INDEX historisation.fki_suivi_etape;

CREATE INDEX fki_suivi_etape
  ON historisation.suivi_etape_attribut
  USING btree
  (id_suivi_etape, pca_suivi_etape);

-- Table: historisation.encours

--DROP TABLE historisation.encours;

CREATE TABLE historisation.encours
(
  id integer NOT NULL, -- Identifiant de la table
  id_ocr character varying(13), -- identifiant OCR /publication Presse
  id_produit character varying(13), -- Identifiant coupure presse
  type character varying(10), -- Type du ticket
  priorite integer NOT NULL, -- Priorite de la tache a realise ( sert pour le tri automatique de l'objet vecteur contenant les tickets)
  contenu_ticket xml NOT NULL, -- Contenu integrale du ticket a consommer
  login_utilisateur character varying(50) NOT NULL, -- Login de la session utilisateur ayant realise le traitement
  machine character varying(50) NOT NULL, -- Nom de poste sur lequel le traitement s'est realise
  id_type_module integer,
  date timestamp with time zone,
  mode_approvisonnement character varying(30), -- le mode d'approvisonnement
  id_livraison_numerique bigint NOT NULL, -- identifiant de la livraison numérique
  CONSTRAINT pk_encours PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE historisation.encours
  OWNER TO postgres;
COMMENT ON TABLE historisation.encours
  IS 'Table des modules/traitements en cours';
COMMENT ON COLUMN historisation.encours.id IS 'Identifiant de la table';
COMMENT ON COLUMN historisation.encours.id_ocr IS 'identifiant OCR /publication Presse';
COMMENT ON COLUMN historisation.encours.id_produit IS 'Identifiant coupure presse';
COMMENT ON COLUMN historisation.encours.type IS 'Type du ticket';
COMMENT ON COLUMN historisation.encours.priorite IS 'Priorite de la tache a realise ( sert pour le tri automatique de l''objet vecteur contenant les tickets)';
COMMENT ON COLUMN historisation.encours.contenu_ticket IS 'Contenu integrale du ticket a consommer';
COMMENT ON COLUMN historisation.encours.login_utilisateur IS 'Login de la session utilisateur ayant realise le traitement';
COMMENT ON COLUMN historisation.encours.machine IS 'Nom de poste sur lequel le traitement s''est realise';
COMMENT ON COLUMN historisation.encours.mode_approvisonnement IS 'le mode d''approvisonnement ';
COMMENT ON COLUMN historisation.encours.id_livraison_numerique IS 'identifiant de la livraison numérique';

-- Table: historisation.identifiant_ocr

--DROP TABLE historisation.identifiant_ocr;

CREATE TABLE historisation.identifiant_ocr
(
  id integer NOT NULL, -- Identifiant de la table identifiant_ocr
  id_ocr character varying(20) NOT NULL, -- Identifiant OCR
  disponible boolean NOT NULL DEFAULT false, -- La valeur de ce champ indique si l'identifiant OCR est disponible ou pas :...
  commentaire character varying(200),
  date_creation timestamp with time zone, -- Date de creation
  CONSTRAINT pk_identifiant_ocr PRIMARY KEY (id), -- Cle primaire de la table identifiant OCR
  CONSTRAINT uk_id_ocr UNIQUE (id_ocr)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE historisation.identifiant_ocr
  OWNER TO postgres;
COMMENT ON COLUMN historisation.identifiant_ocr.id IS 'Identifiant de la table identifiant_ocr';
COMMENT ON COLUMN historisation.identifiant_ocr.id_ocr IS 'Identifiant OCR';
COMMENT ON COLUMN historisation.identifiant_ocr.disponible IS 'La valeur de ce champ indique si l''identifiant OCR est disponible ou pas :

1. TRUE : disponible
2. FALSE : non disponible';
COMMENT ON COLUMN historisation.identifiant_ocr.date_creation IS 'Date de creation';

COMMENT ON CONSTRAINT pk_identifiant_ocr ON historisation.identifiant_ocr IS 'Cle primaire de la table identifiant OCR';

-- Table: historisation.livraison_numerique

--DROP TABLE historisation.livraison_numerique;

CREATE TABLE historisation.livraison_numerique
(
  id bigint NOT NULL DEFAULT nextval(('historisation.seq_livraison_numerique'::text)::regclass), -- Identifiant unique de la livraison
  bp_index bigint, -- Valeur bp_index de TradExpress
  id_source_appro bigint NOT NULL, -- Identifiant de la source d'appro
  nom character varying(255) NOT NULL, -- Nom unique de la livraison
  nom_fichier character varying(255), -- Nom fichier recu
  date_creation timestamp with time zone NOT NULL DEFAULT now(), -- Identifiant du lot
  id_status integer NOT NULL, -- Identifiant du statut
  contenu_pivot xml,
  chemin_livraison_hash character varying(200), -- chemin complet du fichier sur hash temporaire
  chemin_livraison_archivage character varying(200), -- Chemin complet du fichier sur hash d'archivage
  clef_index_pivot text,
  id_pere bigint,
  CONSTRAINT pk_livraison_numerique PRIMARY KEY (id),
  CONSTRAINT uk_nom UNIQUE (nom)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE historisation.livraison_numerique
  OWNER TO postgres;
COMMENT ON TABLE historisation.livraison_numerique
  IS 'Table Livraison_numerique';
COMMENT ON COLUMN historisation.livraison_numerique.id IS 'Identifiant unique de la livraison';
COMMENT ON COLUMN historisation.livraison_numerique.bp_index IS 'Valeur bp_index de TradExpress';
COMMENT ON COLUMN historisation.livraison_numerique.id_source_appro IS 'Identifiant de la source d''appro';
COMMENT ON COLUMN historisation.livraison_numerique.nom IS 'Nom unique de la livraison';
COMMENT ON COLUMN historisation.livraison_numerique.nom_fichier IS 'Nom fichier recu';
COMMENT ON COLUMN historisation.livraison_numerique.date_creation IS 'Identifiant du lot';
COMMENT ON COLUMN historisation.livraison_numerique.id_status IS 'Identifiant du statut';
COMMENT ON COLUMN historisation.livraison_numerique.chemin_livraison_hash IS 'chemin complet du fichier sur hash temporaire';
COMMENT ON COLUMN historisation.livraison_numerique.chemin_livraison_archivage IS 'Chemin complet du fichier sur hash d''archivage ';



-- Table: historisation.livraison_numerique_attribut

--DROP TABLE historisation.livraison_numerique_attribut;

CREATE TABLE historisation.livraison_numerique_attribut
(
  id bigint NOT NULL, -- Identifiant unique de la livraison valeur bpindex de tradexpress
  id_livraison_numerique bigint NOT NULL, -- identifient de la livraison numerique
  nom character varying(200), -- nom de l’attribut: “date_parution”,”date_faciale” a substituer dans les xml produit
  valeur text NOT NULL, -- Valeur de l'attribut
  date_creation timestamp with time zone NOT NULL, -- Date de création de l'enregistrement
  CONSTRAINT pk_id_livraison_numerique_attribut PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE historisation.livraison_numerique_attribut
  OWNER TO postgres;
COMMENT ON COLUMN historisation.livraison_numerique_attribut.id IS 'Identifiant unique de la livraison valeur bpindex de tradexpress';
COMMENT ON COLUMN historisation.livraison_numerique_attribut.id_livraison_numerique IS 'identifient de la livraison numerique';
COMMENT ON COLUMN historisation.livraison_numerique_attribut.nom IS 'nom de l’attribut: “date_parution”,”date_faciale” a substituer dans les xml produit';
COMMENT ON COLUMN historisation.livraison_numerique_attribut.valeur IS 'Valeur de l''attribut';
COMMENT ON COLUMN historisation.livraison_numerique_attribut.date_creation IS 'Date de création de l''enregistrement';

-- Table: historisation.livraison_numerique_contenu

--DROP TABLE historisation.livraison_numerique_contenu;

CREATE TABLE historisation.livraison_numerique_contenu
(
  id bigint NOT NULL, -- Identifiant unique de la livraison - valeur bpindex de TradExpress
  id_livraison_numerique bigint NOT NULL, -- Identifiant de la livraison numerique
  nom character varying(200), -- Nom du contenu
  date_creation timestamp with time zone NOT NULL DEFAULT now(), -- Date de creation de l'enregistrement
  status character varying(50), -- Status du contenu : doublon, original, ...
  id_livraison_numerique_contenu_doublon bigint, -- Si doublon identifiant de la livraison contenu l'enregistrement original
  contenu_pivot xml, -- Contenu au format pivot du contenu
  type_fichier character varying(10), -- xml, pdf, jpeg, ...
  type_contenu character varying(20), -- couverture, page, ...
  CONSTRAINT pk_livraison_numerique_contenu PRIMARY KEY (id),
  CONSTRAINT fk_livraison_numerique FOREIGN KEY (id_livraison_numerique)
      REFERENCES historisation.livraison_numerique (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_livraison_numerique_contenu FOREIGN KEY (id_livraison_numerique_contenu_doublon)
      REFERENCES historisation.livraison_numerique_contenu (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE historisation.livraison_numerique_contenu
  OWNER TO postgres;
COMMENT ON TABLE historisation.livraison_numerique_contenu
  IS 'Table livraison_numerique_contenu';
COMMENT ON COLUMN historisation.livraison_numerique_contenu.id IS 'Identifiant unique de la livraison - valeur bpindex de TradExpress';
COMMENT ON COLUMN historisation.livraison_numerique_contenu.id_livraison_numerique IS 'Identifiant de la livraison numerique';
COMMENT ON COLUMN historisation.livraison_numerique_contenu.nom IS 'Nom du contenu';
COMMENT ON COLUMN historisation.livraison_numerique_contenu.date_creation IS 'Date de creation de l''enregistrement';
COMMENT ON COLUMN historisation.livraison_numerique_contenu.status IS 'Status du contenu : doublon, original, ...';
COMMENT ON COLUMN historisation.livraison_numerique_contenu.id_livraison_numerique_contenu_doublon IS 'Si doublon identifiant de la livraison contenu l''enregistrement original';
COMMENT ON COLUMN historisation.livraison_numerique_contenu.contenu_pivot IS 'Contenu au format pivot du contenu';
COMMENT ON COLUMN historisation.livraison_numerique_contenu.type_fichier IS 'xml, pdf, jpeg, ...';
COMMENT ON COLUMN historisation.livraison_numerique_contenu.type_contenu IS 'couverture, page, ...';


-- Index: historisation.fki_livraison_numerique

-- DROP INDEX historisation.fki_livraison_numerique;

CREATE INDEX fki_livraison_numerique
  ON historisation.livraison_numerique_contenu
  USING btree
  (id_livraison_numerique);

-- Index: historisation.fki_livraison_numerique_contenu

-- DROP INDEX historisation.fki_livraison_numerique_contenu;

CREATE INDEX fki_livraison_numerique_contenu
  ON historisation.livraison_numerique_contenu
  USING btree
  (id_livraison_numerique_contenu_doublon);

  
  -- Table: historisation.modules

--DROP TABLE historisation.modules;

CREATE TABLE historisation.modules
(
  id integer NOT NULL, -- Identifiant de la table
  id_type_module integer NOT NULL, -- Type du module referencer ( traitement image, OCR, ...) issue de la table des types de modules
  machine character varying(50) NOT NULL,
  login_utilisateur character varying(60) NOT NULL, -- Login utilisateur
  port_ws integer NOT NULL, -- Port d'ecoute du web service de consommation des tickets
  port_supervision integer NOT NULL, -- Port de supervision
  etat_mise_a_jour_autotest integer NOT NULL, -- Identifiant du status de l'auto test du module
  id_status integer NOT NULL, -- Identifiant du status du module
  date_dernier_traitement timestamp with time zone NOT NULL, -- Date et heure du dernier retour test
  date timestamp with time zone NOT NULL DEFAULT now(), -- Date et heure de referencement (a la creation de la ligne)
  CONSTRAINT pk_modules PRIMARY KEY (id), -- Cle primaire de la table
  CONSTRAINT uk_type_module_machine_port_supervision UNIQUE (id_type_module, machine, port_supervision), -- Contrainte d'unicite sur le type du module, la machine et le port de supervision
  CONSTRAINT uk_type_module_machine_portws UNIQUE (id_type_module, machine, port_ws), -- Contrainte d'unicite sur le type du module, machine et le port d'ecoute du web service
  CONSTRAINT uk_type_module_machine_utilisateur UNIQUE (id_type_module, machine, login_utilisateur) -- Contrainte d'unicite sur le type du module, la machine et l'utilisateur
)
WITH (
  OIDS=FALSE
);
ALTER TABLE historisation.modules
  OWNER TO postgres;
COMMENT ON TABLE historisation.modules
  IS 'Table de referencement des modules';
COMMENT ON COLUMN historisation.modules.id IS 'Identifiant de la table';
COMMENT ON COLUMN historisation.modules.id_type_module IS 'Type du module referencer ( traitement image, OCR, ...) issue de la table des types de modules';
COMMENT ON COLUMN historisation.modules.login_utilisateur IS 'Login utilisateur';
COMMENT ON COLUMN historisation.modules.port_ws IS 'Port d''ecoute du web service de consommation des tickets ';
COMMENT ON COLUMN historisation.modules.port_supervision IS 'Port de supervision';
COMMENT ON COLUMN historisation.modules.etat_mise_a_jour_autotest IS 'Identifiant du status de l''auto test du module';
COMMENT ON COLUMN historisation.modules.id_status IS 'Identifiant du status du module';
COMMENT ON COLUMN historisation.modules.date_dernier_traitement IS 'Date et heure du dernier retour test';
COMMENT ON COLUMN historisation.modules.date IS 'Date et heure de referencement (a la creation de la ligne) ';

COMMENT ON CONSTRAINT pk_modules ON historisation.modules IS 'Cle primaire de la table';
COMMENT ON CONSTRAINT uk_type_module_machine_port_supervision ON historisation.modules IS 'Contrainte d''unicite sur le type du module, la machine et le port de supervision ';
COMMENT ON CONSTRAINT uk_type_module_machine_portws ON historisation.modules IS 'Contrainte d''unicite sur le type du module, machine et le port d''ecoute du web service';
COMMENT ON CONSTRAINT uk_type_module_machine_utilisateur ON historisation.modules IS 'Contrainte d''unicite sur le type du module, la machine et l''utilisateur';


-- Index: historisation.fki_etat_maj_autotest

-- DROP INDEX historisation.fki_etat_maj_autotest;

CREATE INDEX fki_etat_maj_autotest
  ON historisation.modules
  USING btree
  (etat_mise_a_jour_autotest);

-- Index: historisation.fki_id_type_module

-- DROP INDEX historisation.fki_id_type_module;

CREATE INDEX fki_id_type_module
  ON historisation.modules
  USING btree
  (id_type_module);

-- Index: historisation.fki_status

-- DROP INDEX historisation.fki_status;

CREATE INDEX fki_status
  ON historisation.modules
  USING btree
  (id_status);


-- Schema: referentiel

-- DROP SCHEMA referentiel;

CREATE SCHEMA referentiel
  AUTHORIZATION postgres;
  
  -- Table: referentiel.fournisseur

--DROP TABLE referentiel.fournisseur;

CREATE TABLE referentiel.fournisseur
(
  id bigint NOT NULL DEFAULT nextval(('referentiel.seq_fournisseur'::text)::regclass), -- identifiant unique du fournisseur
  nom character varying(50) NOT NULL, -- Nom de la source d’alimentation (reprit de pour les logs)
  commentaire character varying(2000), -- Commentaire
  xsl bytea, -- feuille de composition de mail automatique
  contact_production character varying(2000) NOT NULL, -- Information du contact support chez le founisseur en cas de problème
  recuperation_couverture character varying(2000), -- Adresse du webservice pour la recuperation des premieres de couvertures. Si vide prendre la premiere page de l’archive mere
  CONSTRAINT pk_fournisseur PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE referentiel.fournisseur
  OWNER TO postgres;
COMMENT ON TABLE referentiel.fournisseur
  IS 'Table des fournisseurs';
COMMENT ON COLUMN referentiel.fournisseur.id IS 'identifiant unique du fournisseur';
COMMENT ON COLUMN referentiel.fournisseur.nom IS 'Nom de la source d’alimentation (reprit de pour les logs)';
COMMENT ON COLUMN referentiel.fournisseur.commentaire IS 'Commentaire';
COMMENT ON COLUMN referentiel.fournisseur.xsl IS 'feuille de composition de mail automatique';
COMMENT ON COLUMN referentiel.fournisseur.contact_production IS 'Information du contact support chez le founisseur en cas de problème';
COMMENT ON COLUMN referentiel.fournisseur.recuperation_couverture IS 'Adresse du webservice pour la recuperation des premieres de couvertures. Si vide prendre la premiere page de l’archive mere';



-- Table: referentiel.ressources

--DROP TABLE referentiel.ressources;

CREATE TABLE referentiel.ressources
(
  id integer NOT NULL DEFAULT nextval(('referentiel.seq_ressource'::text)::regclass), -- L'identifiant de la table
  nom character varying(150) NOT NULL, -- Nom de la ressource (utilise comme clef dans les requetes)
  contenu bytea NOT NULL, -- Contenu de la ressource
  date timestamp with time zone NOT NULL DEFAULT now(), -- date et heure de creation/mise a jour de la ressource
  id_type_module integer NOT NULL DEFAULT 0, -- Identifiant du type module
  CONSTRAINT pk_ressources PRIMARY KEY (id) -- Cle primair de la table
)
WITH (
  OIDS=FALSE
);
ALTER TABLE referentiel.ressources
  OWNER TO postgres;
COMMENT ON COLUMN referentiel.ressources.id IS 'L''identifiant de la table';
COMMENT ON COLUMN referentiel.ressources.nom IS 'Nom de la ressource (utilise comme clef dans les requetes)';
COMMENT ON COLUMN referentiel.ressources.contenu IS 'Contenu de la ressource';
COMMENT ON COLUMN referentiel.ressources.date IS 'date et heure de creation/mise a jour de la ressource';
COMMENT ON COLUMN referentiel.ressources.id_type_module IS 'Identifiant du type module';

COMMENT ON CONSTRAINT pk_ressources ON referentiel.ressources IS 'Cle primair de la table';


-- Index: referentiel.fki_type_module

-- DROP INDEX referentiel.fki_type_module;

CREATE INDEX fki_type_module
  ON referentiel.ressources
  USING btree
  (id_type_module);

-- Table: referentiel.type_module

--DROP TABLE referentiel.type_module;

CREATE TABLE referentiel.type_module
(
  id integer NOT NULL DEFAULT nextval(('referentiel.seq_type_module'::text)::regclass), -- L'identifiant de la table
  nom character varying(50) NOT NULL, -- Nom du module (utilise comme clef dans les requetes)
  libelle character varying(150) NOT NULL, -- Libelle du module (utilise pour l'affichage et les logs)
  description text NOT NULL, -- Description du module
  date timestamp with time zone NOT NULL DEFAULT now(), -- date et heure de creation de la ligne
  id_script_entree integer, -- identifiant de la ressource
  id_script_sortie integer, -- Identifiant de la ressource
  id_type_module_suivant integer, -- Identifiant de la ressource correspondant au type de module suivant utilise pour les appels webservices post traitement (valeur par defaut)
  id_ressource_autotest_entree integer, -- Identifiant de la ressource d'entree pour l'auto test
  id_ressource_autotest_sortie integer, -- Identifiant de la ressource de sortie pour l'auto test
  actif boolean NOT NULL DEFAULT false,
  parametres xml, -- Parametres d'execution necessaires au module sous forme XML casse insensible
  CONSTRAINT pk_type_module PRIMARY KEY (id),
  CONSTRAINT fk_ressource_autotest_entree FOREIGN KEY (id_ressource_autotest_entree)
      REFERENCES referentiel.ressources (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION, -- Cle etrangere vers la table ressource
  CONSTRAINT fk_ressource_autotest_sortie FOREIGN KEY (id_ressource_autotest_sortie)
      REFERENCES referentiel.ressources (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION, -- Cle etrangere vers la table ressource
  CONSTRAINT fk_script_entree FOREIGN KEY (id_script_entree)
      REFERENCES referentiel.ressources (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_script_sortie FOREIGN KEY (id_script_sortie)
      REFERENCES referentiel.ressources (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION, -- Cle etrangere vers la table ressource
  CONSTRAINT fk_type_module_suivant FOREIGN KEY (id_type_module_suivant)
      REFERENCES referentiel.type_module (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION, -- Cle etrangere vers la table type module
  CONSTRAINT uk_type_module UNIQUE (nom) -- Contrainte d'unicite sur le nom du module
)
WITH (
  OIDS=FALSE
);
ALTER TABLE referentiel.type_module
  OWNER TO postgres;
COMMENT ON COLUMN referentiel.type_module.id IS 'L''identifiant de la table ';
COMMENT ON COLUMN referentiel.type_module.nom IS 'Nom du module (utilise comme clef dans les requetes)';
COMMENT ON COLUMN referentiel.type_module.libelle IS 'Libelle du module (utilise pour l''affichage et les logs)';
COMMENT ON COLUMN referentiel.type_module.description IS 'Description du module';
COMMENT ON COLUMN referentiel.type_module.date IS 'date et heure de creation de la ligne';
COMMENT ON COLUMN referentiel.type_module.id_script_entree IS 'identifiant de la ressource';
COMMENT ON COLUMN referentiel.type_module.id_script_sortie IS 'Identifiant de la ressource
';
COMMENT ON COLUMN referentiel.type_module.id_type_module_suivant IS 'Identifiant de la ressource correspondant au type de module suivant utilise pour les appels webservices post traitement (valeur par defaut)';
COMMENT ON COLUMN referentiel.type_module.id_ressource_autotest_entree IS 'Identifiant de la ressource d''entree pour l''auto test';
COMMENT ON COLUMN referentiel.type_module.id_ressource_autotest_sortie IS 'Identifiant de la ressource de sortie pour l''auto test';
COMMENT ON COLUMN referentiel.type_module.parametres IS 'Parametres d''execution necessaires au module sous forme XML casse insensible';

COMMENT ON CONSTRAINT fk_ressource_autotest_entree ON referentiel.type_module IS 'Cle etrangere vers la table ressource';
COMMENT ON CONSTRAINT fk_ressource_autotest_sortie ON referentiel.type_module IS 'Cle etrangere vers la table ressource';
COMMENT ON CONSTRAINT fk_script_sortie ON referentiel.type_module IS 'Cle etrangere vers la table ressource';
COMMENT ON CONSTRAINT fk_type_module_suivant ON referentiel.type_module IS 'Cle etrangere vers la table type module';
COMMENT ON CONSTRAINT uk_type_module ON referentiel.type_module IS 'Contrainte d''unicite sur le nom du module';


-- Index: referentiel.fki_ressource_autotest_entree

-- DROP INDEX referentiel.fki_ressource_autotest_entree;

CREATE INDEX fki_ressource_autotest_entree
  ON referentiel.type_module
  USING btree
  (id_ressource_autotest_entree);

-- Index: referentiel.fki_ressource_autotest_sortie

-- DROP INDEX referentiel.fki_ressource_autotest_sortie;

CREATE INDEX fki_ressource_autotest_sortie
  ON referentiel.type_module
  USING btree
  (id_ressource_autotest_sortie);

-- Index: referentiel.fki_script_entree

-- DROP INDEX referentiel.fki_script_entree;

CREATE INDEX fki_script_entree
  ON referentiel.type_module
  USING btree
  (id_script_entree);

-- Index: referentiel.fki_script_sortie

-- DROP INDEX referentiel.fki_script_sortie;

CREATE INDEX fki_script_sortie
  ON referentiel.type_module
  USING btree
  (id_script_sortie);

-- Index: referentiel.fki_type_module_suivant

-- DROP INDEX referentiel.fki_type_module_suivant;

CREATE INDEX fki_type_module_suivant
  ON referentiel.type_module
  USING btree
  (id_type_module_suivant);

  ALTER TABLE referentiel.ressources 
   ADD CONSTRAINT fk_type_module FOREIGN KEY (id_type_module)
      REFERENCES referentiel.type_module (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
   ADD CONSTRAINT uk_ressources UNIQUE (nom, id_type_module);
   
   -- Table: referentiel.signalements

--DROP TABLE referentiel.signalements;

CREATE TABLE referentiel.signalements
(
  id integer NOT NULL DEFAULT nextval(('referentiel.seq_signalements'::text)::regclass), -- Identifiant unique pour la table
  code character varying(4) NOT NULL, -- Code du signalement qui correspond a une erreure, une alerte ou un log
  libelle character varying(100) NOT NULL, -- Libelle de l'erreur, alerte ou log
  description text, -- Description du signalement
  type character varying(10) NOT NULL, -- Le differents type de signalement...
  date timestamp with time zone NOT NULL DEFAULT now(), -- Date de creation ou de modification de l'erreur, de l'alerte ou de log
  CONSTRAINT pk_signalements PRIMARY KEY (id), -- Cle primaire de la table
  CONSTRAINT uk_signalements UNIQUE (code)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE referentiel.signalements
  OWNER TO postgres;
COMMENT ON TABLE referentiel.signalements
  IS 'La table signalement contient tous les types d''erreurs, alertes et logs';
COMMENT ON COLUMN referentiel.signalements.id IS 'Identifiant unique pour la table';
COMMENT ON COLUMN referentiel.signalements.code IS 'Code du signalement qui correspond a une erreure, une alerte ou un log';
COMMENT ON COLUMN referentiel.signalements.libelle IS 'Libelle de l''erreur, alerte ou log';
COMMENT ON COLUMN referentiel.signalements.description IS 'Description du signalement';
COMMENT ON COLUMN referentiel.signalements.type IS 'Le differents type de signalement
1. ERREUR
2. ALERTE
3. LOG';
COMMENT ON COLUMN referentiel.signalements.date IS 'Date de creation ou de modification de l''erreur, de l''alerte ou de log ';

COMMENT ON CONSTRAINT pk_signalements ON referentiel.signalements IS 'Cle primaire de la table';


-- Table: referentiel.source_appro

--DROP TABLE referentiel.source_appro;

CREATE TABLE referentiel.source_appro
(
  id bigint NOT NULL DEFAULT nextval(('referentiel.seq_source_appro'::text)::regclass), -- Identifiant unique de la table source_appro
  id_fournisseur bigint NOT NULL, -- Identifiant unique du fournisseur
  nom character varying(50) NOT NULL, -- Nom de la source d’alimentation (reprit de pour les logs)
  commentaire character varying(2000), -- Commentaire
  type character varying(20) NOT NULL, -- Type de la source a traiter
  nb_connexion_simultanees integer NOT NULL, -- Nombre de connections simultanées possible à la source d’alimentation pour une meme session
  actif boolean NOT NULL, -- Précise si la source est prise en compte dans les captation a réaliser
  adresse character varying(250), -- Adresse de connexion pour la session FTP/ ou serveur mail
  "user" character varying(60), -- Utilisateur de connexion pour la session FTP/mail
  pass character varying(60), -- Mot de passe pour la session FTP/mail
  ftpmode character varying(20), -- Mode de la session
  ftpport integer, -- Port pour la session FTP
  chemin character varying(250), -- Chemin sur le FTP distant ou chemin NFS à scruter selon le type
  delai integer NOT NULL, -- Délai de des fichiers à comparer en secondes les fichiers plus vieux ne sont pas à prendre en compte
  purge boolean NOT NULL, -- Signale si la matière d’entrée doit être supprimée
  CONSTRAINT pk_source_appro PRIMARY KEY (id),
  CONSTRAINT fk_fournisseur FOREIGN KEY (id_fournisseur)
      REFERENCES referentiel.fournisseur (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION -- Clé etrangère sur la table fournisseur
)
WITH (
  OIDS=FALSE
);
ALTER TABLE referentiel.source_appro
  OWNER TO postgres;
COMMENT ON COLUMN referentiel.source_appro.id IS 'Identifiant unique de la table source_appro';
COMMENT ON COLUMN referentiel.source_appro.id_fournisseur IS 'Identifiant unique du fournisseur';
COMMENT ON COLUMN referentiel.source_appro.nom IS 'Nom de la source d’alimentation (reprit de pour les logs)';
COMMENT ON COLUMN referentiel.source_appro.commentaire IS 'Commentaire';
COMMENT ON COLUMN referentiel.source_appro.type IS 'Type de la source a traiter';
COMMENT ON COLUMN referentiel.source_appro.nb_connexion_simultanees IS 'Nombre de connections simultanées possible à la source d’alimentation pour une meme session';
COMMENT ON COLUMN referentiel.source_appro.actif IS 'Précise si la source est prise en compte dans les captation a réaliser';
COMMENT ON COLUMN referentiel.source_appro.adresse IS 'Adresse de connexion pour la session FTP/ ou serveur mail';
COMMENT ON COLUMN referentiel.source_appro."user" IS 'Utilisateur de connexion pour la session FTP/mail';
COMMENT ON COLUMN referentiel.source_appro.pass IS 'Mot de passe pour la session FTP/mail';
COMMENT ON COLUMN referentiel.source_appro.ftpmode IS 'Mode de la session';
COMMENT ON COLUMN referentiel.source_appro.ftpport IS 'Port pour la session FTP';
COMMENT ON COLUMN referentiel.source_appro.chemin IS 'Chemin sur le FTP distant ou chemin NFS à scruter selon le type';
COMMENT ON COLUMN referentiel.source_appro.delai IS 'Délai de des fichiers à comparer en secondes les fichiers plus vieux ne sont pas à prendre en compte';
COMMENT ON COLUMN referentiel.source_appro.purge IS 'Signale si la matière d’entrée doit être supprimée';

COMMENT ON CONSTRAINT fk_fournisseur ON referentiel.source_appro IS 'Clé etrangère sur la table fournisseur';


-- Index: referentiel.fki_fournisseur

-- DROP INDEX referentiel.fki_fournisseur;

CREATE INDEX fki_fournisseur
  ON referentiel.source_appro
  USING btree
  (id_fournisseur);

-- Table: referentiel.modalite_appro

--DROP TABLE referentiel.modalite_appro;

CREATE TABLE referentiel.modalite_appro
(
  id integer NOT NULL DEFAULT nextval(('referentiel.seq_modalite_appro'::text)::regclass), -- Identifiant de la table modalite_appro
  id_source integer NOT NULL, -- Identifiant source de traitement/ point d'alimentation
  id_gps integer NOT NULL, -- Identifiant GPS
  id_rds integer, -- Identifiant RDS venant de GPS
  libelle_source character varying(50) NOT NULL, -- Libellé de la source issue de GPS
  libelle_modalite character varying(60) NOT NULL, -- Libellé de la modalité de traitement
  masque_fichier character varying(250) NOT NULL, -- Expression régulière correspondant au fichier a traiter
  priorite_panel character varying(20) NOT NULL, -- Priorité de la source issue de GPS
  rang_panel integer NOT NULL, -- Rang de la source issue de GPS
  type_contenu_utilisateur character varying(10) NOT NULL, -- Type de matière a utiliser lors de la génération des images....
  pointage_auto boolean NOT NULL, -- publication a pointer automatiquement selon les informations disponibles dans la matières reçue
  date_fiable boolean NOT NULL, -- Date pouvant etre prise pour effectuer le pointage
  generation_lien_hypertexte boolean NOT NULL, -- Prise en compte des informations de lien pour rediriger la vignette vers l'editeur dans le livrable final presse
  generation_vignette boolean NOT NULL, -- Information a prendre en compte pour la génération des images
  delai_conservation_des_archives integer NOT NULL, -- Délai de rétention des archives sur les points de montage
  delai_conservation_donnees_de_traitement integer NOT NULL, -- Délai de conservation des informations de traitement en base de données
  type_modalite character varying(15) NOT NULL, -- Type de modalité d'approvisionnement
  active boolean NOT NULL, -- Vrai si la source est active
  traitement boolean NOT NULL DEFAULT false, -- Vrai si la source doit être prise en compte par les traitement, faux par défaut
  mot_de_passe_pdf character varying(200), -- Contient le mot de passe pour déverouiller le pdf
  date_modif timestamp with time zone NOT NULL DEFAULT now(), -- Date de modification
  CONSTRAINT pk_modalite_appro PRIMARY KEY (id),
  CONSTRAINT fk_id_source FOREIGN KEY (id_source)
      REFERENCES referentiel.source_appro (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION -- Clé étrangère vers l'id de la source
)
WITH (
  OIDS=FALSE
);
ALTER TABLE referentiel.modalite_appro
  OWNER TO postgres;
COMMENT ON COLUMN referentiel.modalite_appro.id IS 'Identifiant de la table modalite_appro';
COMMENT ON COLUMN referentiel.modalite_appro.id_source IS 'Identifiant source de traitement/ point d''alimentation';
COMMENT ON COLUMN referentiel.modalite_appro.id_gps IS 'Identifiant GPS';
COMMENT ON COLUMN referentiel.modalite_appro.id_rds IS 'Identifiant RDS venant de GPS';
COMMENT ON COLUMN referentiel.modalite_appro.libelle_source IS 'Libellé de la source issue de GPS ';
COMMENT ON COLUMN referentiel.modalite_appro.libelle_modalite IS 'Libellé de la modalité de traitement';
COMMENT ON COLUMN referentiel.modalite_appro.masque_fichier IS 'Expression régulière correspondant au fichier a traiter';
COMMENT ON COLUMN referentiel.modalite_appro.priorite_panel IS 'Priorité de la source issue de GPS';
COMMENT ON COLUMN referentiel.modalite_appro.rang_panel IS 'Rang de la source issue de GPS';
COMMENT ON COLUMN referentiel.modalite_appro.type_contenu_utilisateur IS 'Type de matière a utiliser lors de la génération des images.
1. CONTENU : matière xml + image
2. PAGE : pdf page ou pdf mutli pages ou image jpeg';
COMMENT ON COLUMN referentiel.modalite_appro.pointage_auto IS 'publication a pointer automatiquement selon les informations disponibles dans la matières reçue';
COMMENT ON COLUMN referentiel.modalite_appro.date_fiable IS 'Date pouvant etre prise pour effectuer le pointage';
COMMENT ON COLUMN referentiel.modalite_appro.generation_lien_hypertexte IS 'Prise en compte des informations de lien pour rediriger la vignette vers l''editeur dans le livrable final presse';
COMMENT ON COLUMN referentiel.modalite_appro.generation_vignette IS 'Information a prendre en compte pour la génération des images';
COMMENT ON COLUMN referentiel.modalite_appro.delai_conservation_des_archives IS 'Délai de rétention des archives sur les points de montage';
COMMENT ON COLUMN referentiel.modalite_appro.delai_conservation_donnees_de_traitement IS 'Délai de conservation des informations de traitement en base de données';
COMMENT ON COLUMN referentiel.modalite_appro.type_modalite IS 'Type de modalité d''approvisionnement';
COMMENT ON COLUMN referentiel.modalite_appro.active IS 'Vrai si la source est active';
COMMENT ON COLUMN referentiel.modalite_appro.traitement IS 'Vrai si la source doit être prise en compte par les traitement, faux par défaut';
COMMENT ON COLUMN referentiel.modalite_appro.mot_de_passe_pdf IS 'Contient le mot de passe pour déverouiller le pdf';
COMMENT ON COLUMN referentiel.modalite_appro.date_modif IS 'Date de modification';

COMMENT ON CONSTRAINT fk_id_source ON referentiel.modalite_appro IS 'Clé étrangère vers l''id de la source';


-- Index: referentiel.fki_id_source

-- DROP INDEX referentiel.fki_id_source;

CREATE INDEX fki_id_source
  ON referentiel.modalite_appro
  USING btree
  (id_source);

  
  -- Sequence: referentiel.seq_ressource

-- DROP SEQUENCE referentiel.seq_ressource;

CREATE SEQUENCE referentiel.seq_ressource
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 43
  CACHE 1;
ALTER TABLE referentiel.seq_ressource
  OWNER TO postgres;
  
  
  -- Sequence: referentiel.seq_signalements

-- DROP SEQUENCE referentiel.seq_signalements;

CREATE SEQUENCE referentiel.seq_signalements
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 7
  CACHE 1;
ALTER TABLE referentiel.seq_signalements
  OWNER TO postgres;


-- Sequence: referentiel.seq_source_appro

-- DROP SEQUENCE referentiel.seq_source_appro;

CREATE SEQUENCE referentiel.seq_source_appro
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE referentiel.seq_source_appro
  OWNER TO postgres;

  -- Sequence: referentiel.seq_fournisseur

-- DROP SEQUENCE referentiel.seq_fournisseur;

CREATE SEQUENCE referentiel.seq_fournisseur
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE referentiel.seq_fournisseur
  OWNER TO postgres;

  
  -- Sequence: referentiel.seq_type_module

-- DROP SEQUENCE referentiel.seq_type_module;

CREATE SEQUENCE referentiel.seq_type_module
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 8
  CACHE 1;
ALTER TABLE referentiel.seq_type_module
  OWNER TO postgres;
  
  -- Sequence: referentiel.seq_modalite_appro

-- DROP SEQUENCE referentiel.seq_modalite_appro;

CREATE SEQUENCE referentiel.seq_modalite_appro
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE referentiel.seq_modalite_appro
  OWNER TO postgres;

