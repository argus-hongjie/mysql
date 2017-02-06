--insert nom attribut
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('ATTRIBUTS_nom','
<xsl:stylesheet xmlns:java="http://xml.apache.org/xslt/java" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" exclude-result-prefixes="java" version="1.0">  
 <xsl:output encoding="UTF-8" indent="yes" method="xml"/>   <xsl:template match="/">
   <xsl:value-of select="/LIVRAISON/FICHIERS/FICHIER/@nom"/></xsl:template></xsl:stylesheet>', (select id from referentiel.type_module where nom = 'ATTRIBUTS'));

--insert dedoublonnement calcul md5 
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('CALCUL_MD5','<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet exclude-result-prefixes="java md5sum" xmlns:java="http://xml.apache.org/xslt/java" xmlns:md5sum="fr.argus.socle.util.XSLFunctions" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="xml" indent="yes" encoding="UTF-8"/>
<xsl:template match="/">
 <xsl:param name="parameter"/>
 <xsl:value-of select="md5sum:generateHashByMD5($parameter)" />
</xsl:template>
</xsl:stylesheet>',(select id from referentiel.type_module where nom = 'DEDOUBLONNEMENT'));

-- insert génération de parmètres identPublication et datePublication
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('COVER_PAGE_PARAMETERS','<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet exclude-result-prefixes="java xslfunc" xmlns:java="http://xml.apache.org/xslt/java" xmlns:xslfunc="fr.argus.socle.util.XSLFunctions" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="xml" indent="yes" encoding="UTF-8"/>
<xsl:template match="/">
 <xsl:param name="url"/>
 <xsl:param name="filename"/>
 <xsl:param name="date"/>
 <xsl:value-of select="xslfunc:generateParameters($url,$filename,$date)" />
</xsl:template>
</xsl:stylesheet>', (select id from referentiel.type_module where nom = 'COUVERTURE_PAGE'));



-- insert param scindement_priorite select à modifier par le RA equivalent de la table DUAL sous oracle

INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('SCINDEMENT_PRIORITE','SELECT 1 AS prio', (select id from referentiel.type_module where nom = 'SOUS_LOTS'));

-- insert param scindement_quantite

INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('SCINDEMENT_QUANTITE','25', (select id from referentiel.type_module where nom = 'SOUS_LOTS'));

-- insert param scindement critère

INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('SCINDEMENT_CRITERES','SELECT ((row_number() over())/?) AS num_group,contenu.nom AS fichier FROM production.livraison_numerique AS livraison 
			INNER JOIN referentiel.source_appro AS source ON livraison.id_source_appro = source.id INNER JOIN referentiel.fournisseur AS fournisseur ON fournisseur.id = source.id_fournisseur 
			INNER JOIN production.livraison_numerique_contenu AS contenu ON livraison.id = contenu.id_livraison_numerique WHERE livraison.id = ? AND contenu.status = ? ORDER BY num_group,livraison.id,contenu.nom ', (select id from referentiel.type_module where nom = 'SOUS_LOTS'));

-- xsl de scindement par groupe sous lots
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('XSL_SCINDEMENT_CRITERES','<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet exclude-result-prefixes="java sindCritere" xmlns:java="http://xml.apache.org/xslt/java" xmlns:sindCritere="fr.argus.constitution.sous.lots.utils.Utils" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:param name="QUERY"/>
<xsl:param name="scindQte"/>
<xsl:param name="idLivraison"/>
<xsl:param name="status"/>
<xsl:param name="nom_unique_fichier"/>
<xsl:param name="ticket"/>
<xsl:param name="variableContenu"/>
<xsl:output method="xml" indent="yes" encoding="UTF-8"/>
<xsl:template match="/">
 <xsl:variable name="resultSQL" select="sindCritere:getScindementCriteres($QUERY,$scindQte,$idLivraison,$status)" />
 <xsl:message><xsl:value-of select ="$resultSQL"/></xsl:message>
 <xsl:value-of select="sindCritere:genetateSousLotsByGroupe($nom_unique_fichier, $idLivraison,$resultSQL,$ticket,$variableContenu)" />
</xsl:template>
</xsl:stylesheet>',(select id from referentiel.type_module where nom = 'SOUS_LOTS'));
			

--Insert Process TradExpress dedoublonnage
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('PROCESS_TRADEXPRESS','<?xml version="1.0" encoding="UTF-8"?><PROCESSTRADEXPRESS><ip>172.19.21.250</ip><command>/opt/TradeXpress5/bin/runprocess PFNUM_DEDOUBLON</command><login>edirec</login><password>edirec</password></PROCESSTRADEXPRESS>', (select id from referentiel.type_module where nom = 'DEDOUBLONNEMENT'));

--Insert Process TradExpress attributs
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('PROCESS_TRADEXPRESS','<?xml version="1.0" encoding="UTF-8"?><PROCESSTRADEXPRESS><ip>172.19.21.250</ip><command>/opt/TradeXpress5/bin/runprocess PFNUM_ATTRIBUT</command><login>edirec</login><password>edirec</password></PROCESSTRADEXPRESS>', (select id from referentiel.type_module where nom = 'ATTRIBUTS'));


--Insert Preocess TradExpress Sous_lots
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('PROCESS_TRADEXPRESS','<?xml version="1.0" encoding="UTF-8"?><PROCESSTRADEXPRESS><ip>172.19.21.250</ip><command>/opt/TradeXpress5/bin/runprocess PFNUM_SOUS_LOTS</command><login>edirec</login><password>edirec</password></PROCESSTRADEXPRESS>', (select id from referentiel.type_module where nom = 'SOUS_LOTS'));


--Insert process TradExpress page_cover
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('PROCESS_TRADEXPRESS','<?xml version="1.0" encoding="UTF-8"?><PROCESSTRADEXPRESS><ip>172.19.21.250</ip><command>/opt/TradeXpress5/bin/runprocess PFNUM_PAGE_COVER</command><login>edirec</login><password>edirec</password></PROCESSTRADEXPRESS>', (select id from referentiel.type_module where nom = 'COUVERTURE_PAGE'));


--Insert Process TradExpress Identifiant Presse
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('PROCESS_TRADEXPRESS','<?xml version="1.0" encoding="UTF-8"?><PROCESSTRADEXPRESS><ip>172.19.21.250</ip><command>/opt/TradeXpress5/bin/runprocess PFNUM_ID_PRESSE</command><login>edirec</login><password>edirec</password></PROCESSTRADEXPRESS>', (select id from referentiel.type_module where nom = 'IDENTIFIANT_PRESSE'));



--Insert Process TradExpress pointage
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('PROCESS_TRADEXPRESS','<?xml version="1.0" encoding="UTF-8"?><PROCESSTRADEXPRESS><ip>172.19.21.250</ip><command>/opt/TradeXpress5/bin/runprocess PFNUM_POINTAGE</command><login>edirec</login><password>edirec</password></PROCESSTRADEXPRESS>', (select id from referentiel.type_module where nom = 'POINTAGE'));



--Insert Process TradExpress generation fichiers images
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('PROCESS_TRADEXPRESS','<?xml version="1.0" encoding="UTF-8"?><PROCESSTRADEXPRESS><ip>172.19.21.250</ip><command>/opt/TradeXpress5/bin/runprocess PFNUM_IMG_POS</command><login>edirec</login><password>edirec</password></PROCESSTRADEXPRESS>', (select id from referentiel.type_module where nom = 'FICHIERS_IMAGES_POSITIONS'));


--Insert Process TradExpress attente pointage
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('PROCESS_TRADEXPRESS','<?xml version="1.0" encoding="UTF-8"?><PROCESSTRADEXPRESS><ip>172.19.21.250</ip><command>/opt/TradeXpress5/bin/runprocess PFNUM_A_POINTAGE</command><login>edirec</login><password>edirec</password></PROCESSTRADEXPRESS>', (select id from referentiel.type_module where nom = 'ATTENTE_POINTAGE'));

--Insert Process TradExpress livrable
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('PROCESS_TRADEXPRESS','<?xml version="1.0" encoding="UTF-8"?><PROCESSTRADEXPRESS><ip>172.19.21.250</ip><command>/opt/TradeXpress5/bin/runprocess PFNUM_LIVRABLE</command><login>edirec</login><password>edirec</password></PROCESSTRADEXPRESS>', (select id from referentiel.type_module where nom = 'LIVRABLES'));

--Insert Process TradExpress livrable
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('PROCESS_TRADEXPRESS_FIN','<?xml version="1.0" encoding="UTF-8"?><PROCESSTRADEXPRESS><ip>172.19.21.250</ip><command>/opt/TradeXpress5/bin/runprocess PFNUM_FIN_TICKET</command><login>edirec</login><password>edirec</password></PROCESSTRADEXPRESS>', (select id from referentiel.type_module where nom = 'LIVRABLES'));


--Insert Process TradExpress gestionaire de status pour l'ensemble des modules
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('PROCESS_TRADEXPRESS_STATUS','<?xml version="1.0" encoding="UTF-8"?><PROCESSTRADEXPRESS><ip>172.19.21.250</ip><command>/opt/TradeXpress5/bin/runprocess PFNUM_STATUS</command><login>edirec</login><password>edirec</password></PROCESSTRADEXPRESS>', (select id from referentiel.type_module where nom = 'FORMAT_PIVOT'));
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('PROCESS_TRADEXPRESS_STATUS','<?xml version="1.0" encoding="UTF-8"?><PROCESSTRADEXPRESS><ip>172.19.21.250</ip><command>/opt/TradeXpress5/bin/runprocess PFNUM_STATUS</command><login>edirec</login><password>edirec</password></PROCESSTRADEXPRESS>', (select id from referentiel.type_module where nom = 'DEDOUBLONNEMENT'));
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('PROCESS_TRADEXPRESS_STATUS','<?xml version="1.0" encoding="UTF-8"?><PROCESSTRADEXPRESS><ip>172.19.21.250</ip><command>/opt/TradeXpress5/bin/runprocess PFNUM_STATUS</command><login>edirec</login><password>edirec</password></PROCESSTRADEXPRESS>', (select id from referentiel.type_module where nom = 'ATTRIBUTS'));
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('PROCESS_TRADEXPRESS_STATUS','<?xml version="1.0" encoding="UTF-8"?><PROCESSTRADEXPRESS><ip>172.19.21.250</ip><command>/opt/TradeXpress5/bin/runprocess PFNUM_STATUS</command><login>edirec</login><password>edirec</password></PROCESSTRADEXPRESS>', (select id from referentiel.type_module where nom = 'SOUS_LOTS'));
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('PROCESS_TRADEXPRESS_STATUS','<?xml version="1.0" encoding="UTF-8"?><PROCESSTRADEXPRESS><ip>172.19.21.250</ip><command>/opt/TradeXpress5/bin/runprocess PFNUM_STATUS</command><login>edirec</login><password>edirec</password></PROCESSTRADEXPRESS>', (select id from referentiel.type_module where nom = 'COUVERTURE_PAGE'));
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('PROCESS_TRADEXPRESS_STATUS','<?xml version="1.0" encoding="UTF-8"?><PROCESSTRADEXPRESS><ip>172.19.21.250</ip><command>/opt/TradeXpress5/bin/runprocess PFNUM_STATUS</command><login>edirec</login><password>edirec</password></PROCESSTRADEXPRESS>', (select id from referentiel.type_module where nom = 'IDENTIFIANT_PRESSE'));
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('PROCESS_TRADEXPRESS_STATUS','<?xml version="1.0" encoding="UTF-8"?><PROCESSTRADEXPRESS><ip>172.19.21.250</ip><command>/opt/TradeXpress5/bin/runprocess PFNUM_STATUS</command><login>edirec</login><password>edirec</password></PROCESSTRADEXPRESS>', (select id from referentiel.type_module where nom = 'POINTAGE'));
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('PROCESS_TRADEXPRESS_STATUS','<?xml version="1.0" encoding="UTF-8"?><PROCESSTRADEXPRESS><ip>172.19.21.250</ip><command>/opt/TradeXpress5/bin/runprocess PFNUM_STATUS</command><login>edirec</login><password>edirec</password></PROCESSTRADEXPRESS>', (select id from referentiel.type_module where nom = 'FICHIERS_IMAGES_POSITIONS'));
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('PROCESS_TRADEXPRESS_STATUS','<?xml version="1.0" encoding="UTF-8"?><PROCESSTRADEXPRESS><ip>172.19.21.250</ip><command>/opt/TradeXpress5/bin/runprocess PFNUM_STATUS</command><login>edirec</login><password>edirec</password></PROCESSTRADEXPRESS>', (select id from referentiel.type_module where nom = 'ATTENTE_POINTAGE'));
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('PROCESS_TRADEXPRESS_STATUS','<?xml version="1.0" encoding="UTF-8"?><PROCESSTRADEXPRESS><ip>172.19.21.250</ip><command>/opt/TradeXpress5/bin/runprocess PFNUM_STATUS</command><login>edirec</login><password>edirec</password></PROCESSTRADEXPRESS>', (select id from referentiel.type_module where nom = 'LIVRABLES'));




--insert xsl Feuille de Pointage
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('XSL_FEUILLE_POINTAGE','<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" >

	<xsl:output method="xml" indent="yes" />

	<xsl:variable name="warning_titre">Titre non communiquée</xsl:variable>
	<xsl:variable name="warning_date_parution"> non communiquée</xsl:variable>

	<xsl:param name="pathDirJpg" />
	<xsl:param name="nb_pages" />
	<xsl:param name="id_publication" />
	<xsl:param name="fournisseur" />
	<xsl:param name="nom" />

	<xsl:template match="RECEPTION">

		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master margin-right="1cm" margin-left="1cm" margin-bottom="0.5cm" margin-top="1cm" page-width="21cm" page-height="29.7cm" master-name="first">
					<fo:region-body margin-right="0cm" margin-left="0cm" margin-bottom="0cm" margin-top="0cm"/>
					<fo:region-before extent="0cm" margin-right="0cm" margin-left="0cm" margin-bottom="0cm" margin-top="0cm"/>
					<fo:region-after extent="0cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>

			<fo:page-sequence master-reference="first" language="en" hyphenate="true">
				<fo:flow flow-name="xsl-region-body">

					<fo:table table-layout="fixed" border-width=".2mm" border-color="black" border-style="solid">
						<fo:table-column column-width="18cm"/>
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell>

									<xsl:variable name="express">
										
									</xsl:variable>

									<xsl:call-template name="entete">
										<xsl:with-param name="titre" select="$nom" />
										<xsl:with-param name="provenance" select="$fournisseur"/>
										<xsl:with-param name="express" select="$express"/>
										<xsl:with-param name="date_creation" select="normalize-space(@date_reception)"/>
										<xsl:with-param name="date_parution" select="normalize-space(@date_mise_a_dispo)"/>
										<xsl:with-param name="nbpages" select="$nb_pages"/>
									</xsl:call-template>

									
									<xsl:call-template name="codeBarre">
										<xsl:with-param name="cb" select="$id_publication" />
									</xsl:call-template>
									
									
									<fo:block text-align="center">
										<fo:external-graphic max-width="18cm" content-width="scale-to-fit" max-height="18cm" content-height="scale-to-fit" src="{$pathDirJpg}" />								
									</fo:block>
														
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

	<xsl:template name="entete">
	
		<xsl:param name="titre" />
		<xsl:param name="provenance" />
		<xsl:param name="express" />
		<xsl:param name="date_creation" />
		<xsl:param name="date_parution" />
		<xsl:param name="nbpages" />
		
		<fo:table text-align="center" table-layout="fixed">
		<fo:table-column column-width="18cm"/>
			<fo:table-body>
		
				<fo:table-row>
				   <fo:table-cell border-style="none" padding="0pt">
						 <fo:block border-style="none" padding="0pt" vertical-align="middle" font-weight="bold"  font-size="18pt" keep-together="always" text-align="center">
						 <xsl:value-of select="$titre"/><xsl:if test="string-length($titre) = 0"><xsl:value-of select="$warning_titre"/></xsl:if> <fo:inline font-size="20pt" font-weight="bold"> (<xsl:value-of select="$provenance"/>)</fo:inline>
					 </fo:block>
					   </fo:table-cell>
					</fo:table-row>
				<fo:table-row>
				   <fo:table-cell border-style="none" padding="0pt">
					 <fo:block border-style="none" padding="0pt" vertical-align="middle" font-size="18pt" keep-together="always" text-align="center">
						 <xsl:value-of select="$express"/>
					 </fo:block>
					   </fo:table-cell>
					</fo:table-row>
				<fo:table-row>
				   <fo:table-cell border-style="none" padding="0pt" display-align="center">
						 <fo:block border-style="none" padding="0pt" vertical-align="middle" font-size="18pt" keep-together="always" text-align="center">
						Date de parution: <xsl:value-of select="$date_parution"/><xsl:if test="string-length($date_parution) = 0"><xsl:value-of select="$warning_date_parution"/></xsl:if> 
					 </fo:block>
					   </fo:table-cell>
					</fo:table-row>
				<fo:table-row>
				   <fo:table-cell border-style="none" padding="0pt" display-align="center">
						 <fo:block border-style="none" padding="0pt" vertical-align="middle" font-size="18pt" keep-together="always" text-align="center">
						Nombre de pages: <xsl:value-of select="$nbpages"/> 
					 </fo:block>
					   </fo:table-cell>
					</fo:table-row>
				<fo:table-row>
				<fo:table-cell border-style="none" padding="0pt" display-align="center">
					<fo:block border-style="none" padding="0pt" vertical-align="middle" font-size="18pt" keep-together="always" text-align="center">
						Date de réception: <xsl:value-of select="$date_creation"/> 
					</fo:block>
			   </fo:table-cell>			  
			   </fo:table-row>
				
			
		 </fo:table-body>
	   </fo:table>
	</xsl:template>

	<xsl:template name="codeBarre">
		<xsl:param name="cb" />
		<fo:table text-align="center" table-layout="fixed" width="100%">
			<fo:table-column column-width="18cm"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell border-style="none" padding="0pt" display-align="center">
						<fo:block>
							<fo:instream-foreign-object>
								<barcode:barcode xmlns:barcode="http://barcode4j.krysalis.org/ns" msg="{$cb}" extension-element-prefixes="barcode">
									<barcode:ean-13>
										<barcode:height>2cm</barcode:height>
										<barcode:checksum>ignore</barcode:checksum>
										<barcode:human-readable>
											<barcode:placement>bottom</barcode:placement>
											<barcode:font-name>Helvetica</barcode:font-name>
											<barcode:font-size>8pt</barcode:font-size>
										</barcode:human-readable>										
									</barcode:ean-13>
									
								</barcode:barcode>
							</fo:instream-foreign-object>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>

</xsl:stylesheet>

',(select id from referentiel.type_module where nom = 'POINTAGE'));


--Insertion XSL Transformeur xml en PDF

INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('XSL_XML_TO_PDF_IMAGE_POSITION','<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:template match="/">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="content"
					page-width="210mm" page-height="297mm" margin="20mm 20mm 20mm 20mm">
					<fo:region-body />
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="content">
				<fo:flow flow-name="xsl-region-body">
					<fo:block> &lt;?xml version = "1.0" encoding = "UTF-8"?&gt;</fo:block>
					<fo:block>
						<xsl:apply-templates />
					</fo:block>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	<xsl:template match="@*">
		<xsl:text> </xsl:text>
		<xsl:value-of select="name()" />
		<xsl:text>="</xsl:text>
		<xsl:value-of select="." />
		<xsl:text>"</xsl:text>
	</xsl:template>
	<xsl:template match="*">
		<xsl:param name="indent">0</xsl:param>
		<fo:block margin-left="{$indent}">
			<xsl:text>&lt;</xsl:text>
			<xsl:value-of select="name()" />
			<xsl:apply-templates select="@*" />
			<xsl:text>&gt;</xsl:text>
			<xsl:apply-templates>
				<xsl:with-param name="indent" select="$indent+10" />
			</xsl:apply-templates>
			<xsl:text>&lt;/</xsl:text>
			<xsl:value-of select="name()" />
			<xsl:text>&gt;</xsl:text>
		</fo:block>
	</xsl:template>
</xsl:stylesheet>',
(select id from referentiel.type_module where nom = 'FICHIERS_IMAGES_POSITIONS'));

--Impression image position 
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('LISTE_FICHER','SELECT (xpath(/RECEPTION/FILES/FILE/@id_contenu, contenu_pivot))[1]::varchar as id_livraison_contenu, (xpath(/RECEPTION/@cheminLivraisonHash, contenu_pivot))[1]::varchar as cheminHash from production.livraison_numerique  where nom_fichier = ? ', (select id from referentiel.type_module where nom = 'FICHIERS_IMAGES_POSITIONS'));

-- insert maj argus demande pointage


INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('MAJ_ARGUS_DEMANDE_POINTAGE','INSERT INTO `argus_demande_pointage` (UNIQUE_KEY,`DISPLAY`,`CREATE_USER`,`CREATE_DATE`,`MODIFY_USER`,`MODIFY_DATE`,`STATUT`,`UNITE_SOURCE`,`ID_SOURCE`,`TITRE`,`UTILISATEUR`,`CODE_BARRE`,`CHEMIN_FEUILLE_POINTAGE`,`DATE_PARUTION`,`DATE_FACIALE`,`NOMBRE_PAGE`,`MESSAGE_POINTAGE`,`DATE_POINTAGE`,`PREDICTED_ISSUE`,`STATUT_LABEL`,`DATE_PARUTION_LABEL`,`DATE_POINTAGE_LABEL`,`NUMERO_PUBLICATION`,`NUMERO_LOT`,`MODE_APPROVISIONNEMENT`,`MODE_APPROVISIONNEMENT_LABEL`,`TYPE_DE_TRAITEMENT`,`INFO_SUPP`,`IS_REJEU`,`ID_INSTANCE_APPRO`,`TRAITEMENT_ID`,`TRAITEMENT_LABEL`) SELECT COALESCE(max(unique_key), 0)+1 ,NULL, ''ESB'', DATE_FORMAT(NOW(), ''%Y%m%d%H%i%s''), NULL, NULL, ''1'', :unite_source, :id_source, :titre, NULL, :code_barre, :chemin_feuille_pointage, :date_parution, :date_faciale, NULL, NULL, NULL, NULL, ''A compléter'', '''', NULL, '''', ''0'', ''6'', ''Flux numérique'', ''1'', NULL, NULL, ''0'', ''2'', ''TraitementPDFArticle'' FROM argus_demande_pointage', (select id from referentiel.type_module where nom = 'ATTENTE_POINTAGE'));

-- insert call ws esb

INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('CALL_WS_ESB','CALL `ps_maj_reception`(:identifiant_unite_source, :date_parution, :date_faciale, :code_barres, :chemin_feuille_pointage, 0, '''', 0, :date_fiable, 0, ''ESB'', :@code_retour, :@message_retour)', (select id from referentiel.type_module where nom = 'ATTENTE_POINTAGE'));

-- insert génération de parmètres identPublication et datePublication
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('XSL_CREATE_XML','<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="fr.argus.socle.util.XSLFunctions" version="2.0" exclude-result-prefixes="java"> 
	<xsl:param name="link_cover" />
	<xsl:param name="pageNum" />
	<xsl:output method="xml" omit-xml-declaration="no" indent="yes" encoding="UTF-8"/>
	
	<xsl:template match="/">
		<xsl:variable name="idocr" 				select="/LIVRAISON//ATTRIBUT[@NOM=''ID_OCR'']/@VALEUR" />
	
		<pagepublication>
			<flunum
			    fln_id_publication="{$idocr}"
			    fln_date_creation_argus=""
			    fln_fichier_source=""
			    fln_sceau=" "
			    fln_edition=""
			    fln_activation_edition="non"
			    fln_provenance="FAR"
			    fln_titre="sans titre"
			    fln_fil=""
			    fln_lotarticle=""
			    fln_num_page="{$pageNum}"
			    fln_groupe_editeur="NOUVEL OBSERVATEUR"
			    fln_url_pdf_bool="non"
			    fln_url_pdf="" />
			
		    <type_UD>technique</type_UD>

		    <erreur_sceau>Un probleme est survenu lors de la recuperation du sceau</erreur_sceau>
		
			<xsl:copy-of select="//FICHIER[@nom=''PRE_POINTAGE.xml'']//VALEUR/publication/*" />
			
			<generation_img>
				 <info_generation_img
					 img_nom_plateau="ext-machtrait06-rsi"
					 img_process="pdf2jpegone"
					 img_heure_debut="2016/09/19-16:00:58"
					 img_dure="3" />
			</generation_img>

  			<instructions />
  			
  			<ensemble
				ens_express="non"
				ens_etrangere="non"
				ens_normale="non"
				ens_sectorielle="non" />
			   
			<magazine
				mag_id_ocr="{$idocr}"
				mag_id_pointage="553158"
				mag_date_faciale="TEST PRESSREADER"
				mag_priorite_scan="3-prioritaire"
				mag_id_magazine="601948"
				mag_date_pointage="2016/09/19-16:09:16"
				mag_li_notes_ptn=""
				mag_li_notes_rouge=""
				mag_nom_magazine="TRIBUNE DE GENEVE"
				mag_nom_argus="T GENEVE (FLUX NUM)"
				mag_scan_allow="oui"
				mag_periodicite="Quotidien"
				mag_ojd="56333"
				mag_prix_cfc="50"
				mag_date_cfc="2010-06-17"
				mag_infographie_cfc="oui"
				mag_photos_cfc="oui"
				mag_langue="FR"
				mag_couleur="rouge"
				mag_type_papier="journal"
				mag_no_copy="non"
				mag_papier_disponible="oui"
				mag_no_reassort="oui"
				mag_numero_page="oui"
				mag_vignette_couverture="oui"
				mag_vignette_emplacement_article="oui"
				mag_floute_dose="non"
				mag_crop_page="oui"
				mag_nb_unite="2"
				mag_url_dans_pdf="non"
				mag_rescan="non"
				mag_nom_ensemble=""
				mag_ens_express="non"
				mag_ens_etrangere="non"
				mag_ens_normale="non"
				mag_ens_sectorielle="non"
				mag_adresse1="11 RUE DES ROIS CP 5306 1211 GENEVE 11"
				mag_adresse2=""
				mag_code_postal=" SUISSE"
				mag_telephone="022 322 40 00"
				mag_double_lecture="non"
				mag_traitement_id="3"
				mag_traitement_lib="TraitementPDFLineaire" />
				
			<pointage>
				<info_pointage
					ptg_utilisateur="argus2"
					ptg_nom="tib-bw1-pp"
					ptg_heure_debut="2016/09/19-16:09:16" />
			</pointage>

			<instructions_ocr
				ocr_rotation="0"
				ocr_recollage="non"
				ocr_recoupement="non"
				ocr_timeout_traitement="540"
				ocr_chaine_sortie="chaineocr"
				ocr_repertoire_sortie=""
				ocr_machine_cible=""
				ocr_sortie_texte_cesure="oui"
				ocr_sortie_texte_sans_cesure="oui"
				ocr_langue="FR"
				ocr_sortie_pos="oui"
				ocr_sortie_source="non"
				ocr_sortie_traite="oui"
				ocr_dpi_sortie_traite="300"
				ocr_compression_sortie_traite="075"
				ocr_sortie_nb="oui"
				ocr_dpi_sortie_nb="300"
				ocr_compression_sortie_nb="075"
				ocr_sortie_vignette="non"
				ocr_dpi_sortie_vignette="300"
				ocr_compression_sortie_vignette="075"
				ocr_taille_sortie_vignette="256*384"
				ocr_generation_pdf="non"
				ocr_instructions_profileur="oui" />
			
			<scan>
				<info_scan
					scn_nom_scanner="KODAKHFA"
					scn_numero_page="{$pageNum}"
					scn_utilisateur=""
					scn_couleur="non"
					scn_resolution="300"
					scn_nb_pages_publication="28"
					scn_mode_remise="non" />
				<page
					scn_id="1152940202408_009"
					scn_heure="2016/09/19-15:58:43"
					scn_tps_de_scan="0"
					scn_taille_pixels=";" />
			</scan>
  			
		</pagepublication>
	</xsl:template>
</xsl:stylesheet>', (select id from referentiel.type_module where nom = 'LIVRABLES'));

-- insert génération de parmètres identPublication et datePublication
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('XSL_CREATE_TAR','<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="fr.argus.socle.util.XSLFunctions" xmlns:saxon="http://saxon.sf.net/" exclude-result-prefixes="java saxon"> 
	<xsl:param name="link_cover" />
	<xsl:output method="xml" name="default" indent="yes" omit-xml-declaration="yes" />
	
	<xsl:variable name="jpgExtension" 		select="''scan.jpg''" />
	<xsl:variable name="tifExtension" 		select="''tif''" />
	<xsl:variable name="AUCUNE" 			select="java:getConstant(''AUCUNE'', ())" />
	<xsl:variable name="PREMIERE_PAGE" 		select="java:getConstant(''PREMIERE_PAGE'', ())" />
	<xsl:variable name="FTP" 				select="java:getConstant(''FTP'', ())" />
	<xsl:variable name="WS" 				select="java:getConstant(''WS'', ())" />
	<xsl:variable name="pages" 				select="java:getConstant(''REPERTTOIRE_TMP_GENRATION_FICHIERS_IMG'', ())" />
	<xsl:variable name="couvertureMainName"	select="java:getConstant(''COUVERTURE'', ())" />
	<xsl:variable name="xslCreateXmlKey" 	select="java:getProperty(''xsl.create.xml'', ''XSL_CREATE_XML'')" />
	<xsl:variable name="xslCreateXml" 		select="java:getResource($xslCreateXmlKey, ())" />
	<xsl:variable name="cheminOCR" 			select="java:getValue(''CHEMIN_OCR'', ())" />
	<xsl:variable name="cheminBypassOCR" 	select="java:getValue(''CHEMIN_BYPASS_OCR'', ())" />
	<xsl:variable name="xmlOrignal" 		select="saxon:serialize(/, ''default'')" />
	<xsl:variable name="cheminLivraisonHash" select="//RECEPTION/@cheminLivraisonHash/string()" />
	<xsl:variable name="idocr" 				select="/LIVRAISON//ATTRIBUT[@NOM=''ID_OCR'']/@VALEUR" />
	<xsl:variable name="pageNumToSkip" 		select="if ($link_cover = $PREMIERE_PAGE) then 1 else -1" />
    <xsl:variable name="pageNumDiff" 		select="if (index-of(($WS, $FTP), $link_cover)) then 1 else 0" />
	    
	<xsl:template match="/">
		<tars>
		<xsl:if test="$link_cover != $AUCUNE">
			<xsl:call-template name="tar">
				<xsl:with-param name="pageNum" select="''001''" />
				<xsl:with-param name="fileNames" select="(concat($couvertureMainName, ''.jpg''))"/>
			</xsl:call-template>
	    </xsl:if>

		<xsl:for-each-group select="//FICHIER[not(ends-with(@nom, ''.xml'')) and number(replace(@nom, ''\\D+'', ''''))!=$pageNumToSkip]" group-by="replace(@nom, ''\\D+'', '''')">
			<xsl:call-template name="tar">
				<xsl:with-param name="pageNum" select="format-number(number(current-grouping-key())+$pageNumDiff, ''000'')" />
				<xsl:with-param name="fileNames" select="current-group()/@nom/string()" />
			</xsl:call-template>
		</xsl:for-each-group>
		</tars>
	</xsl:template>
	
	<xsl:template name="tar">
		<xsl:param name="pageNum" />
		<xsl:param name="fileNames" />
		
		<xsl:variable name="tarMainName" 	select="concat($idocr, ''_'', $pageNum)" />
		<xsl:variable name="shouldBypassOCR" select="boolean($fileNames[ends-with(., ''.pos'')])" />
		<xsl:variable name="tarChemin" 		select="if ($shouldBypassOCR) then $cheminBypassOCR else $cheminOCR" />
		<xsl:variable name="tarPath" 		select="java:concatPath(($tarChemin, concat($tarMainName, ''.tar'')))" />
		<xsl:variable name="tarPathTmp" 	select="concat($tarPath, ''.tmp'')" />
		<tar path="{$tarPath}">	
		
		<xsl:variable name="entryName" 		select="concat($tarMainName, ''.xml'')" />
		<xsl:variable name="entryData" 		select="java:runXslTransform($xmlOrignal, $xslCreateXml, (''link_cover'', $link_cover, ''pageNum'', $pageNum))" />
		<xsl:variable name="fullEntryName"	select="java:addEntryToTar($tarPathTmp, $tarMainName, $entryName, $entryData)" />
		<entry pathInTar="{$fullEntryName}" data="{$entryData}" />
			
		<xsl:for-each select="$fileNames">
			<xsl:variable name="fileExtensionOld" select="tokenize(.,''[.]'')[last()]" />
			<xsl:variable name="fileExtension" select="if ($fileExtensionOld = ''jpg'') then $jpgExtension else if ($fileExtensionOld = ''tif'') then $tifExtension else $fileExtensionOld" />
			<xsl:variable name="entryName" 	select="concat($tarMainName, ''.'', $fileExtension)" />
			<xsl:variable name="pagesDir" select="if (starts-with(., $couvertureMainName)) then '''' else $pages" />
			<xsl:variable name="entryFile" 	select="java:concatPath(($cheminLivraisonHash, $pagesDir, .))" />
			<xsl:variable name="fullEntryName"	select="java:addFileToTar($tarPathTmp, $tarMainName, $entryName, $entryFile)" />
			<entry pathInTar="{$fullEntryName}" file="{$entryFile}" />
		</xsl:for-each>

		<xsl:value-of select="java:moveFile($tarPathTmp, $tarPath)" />
		</tar>
	</xsl:template>
	
</xsl:stylesheet>', (select id from referentiel.type_module where nom = 'LIVRABLES'));

-- insert génération de parmètres identPublication et datePublication
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('XSL_VERIFY_TAR','<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="fr.argus.socle.util.XSLFunctions"  xmlns:saxon="http://saxon.sf.net/"  version="2.0" exclude-result-prefixes="java"> 
	<xsl:param name="link_cover" />
	<xsl:output method="text" indent="yes" encoding="UTF-8"/>
	
    <xsl:variable name="xsdValidXmlKey" 	select="java:getProperty(''xsd.valid.xml'', ''XSD_VALID_XML'')" />
	<xsl:variable name="xsdValidXml" 		select="java:getResource($xsdValidXmlKey, ())" />
	<xsl:variable name="AUCUNE" 			select="java:getConstant(''AUCUNE'', ())" />
	<xsl:variable name="PREMIERE_PAGE" 		select="java:getConstant(''PREMIERE_PAGE'', ())" />
	<xsl:variable name="FTP" 				select="java:getConstant(''FTP'', ())" />
	<xsl:variable name="WS" 				select="java:getConstant(''WS'', ())" />
	<xsl:variable name="pageNumToSkip" 		select="if ($link_cover = $PREMIERE_PAGE) then 1 else -1" />
    <xsl:variable name="pageNumDiff" 		select="if (index-of(($WS, $FTP), $link_cover)) then 1 else 0" />
    <xsl:variable name="firstXml" 			select="saxon:parse((//entry[ends-with(@pathInTar, ''.xml'')])[1]/@data)" />
	
	<xsl:template match="/">
		<xsl:for-each select="//tar">
			<xsl:variable name="tarExisted"	select="java:fileExisted(@path)" />
			<xsl:value-of select="if (not($tarExisted)) then concat(''&#xa;Tar NotExsit: '',@path) else ''''" />

			<xsl:for-each select="./entry">
				<xsl:variable name="entryExisted"	select="if (boolean(@data)) then java:entryInTar(../@path, @pathInTar, @data) else if (boolean(@file)) then java:fileInTar(../@path, @pathInTar, @file) else false" />
				<xsl:value-of select="if (not($entryExisted)) then concat(''&#xa;Entry NotExsitInTar or ContentNoCorrect: '', @pathInTar) else ''''" />
				
				<xsl:if test="ends-with(@pathInTar, ''.xml'')">
					<xsl:variable name="xmlValid"	select="java:xmlValid(@data, $xsdValidXml)" />
					<xsl:value-of select="if (not($xmlValid)) then concat(''&#xa;NotValidXml: '', @pathInTar) else ''''" /> 
				</xsl:if>
			</xsl:for-each>
		</xsl:for-each>
		
		<xsl:variable name="nbPagesPointage" select="$firstXml//@smg_nb_pages/number()" />
		<xsl:variable name="nbPagesTars" select="count(//entry[ends-with(@pathInTar, ''.jpg'')]) - $pageNumDiff" />
		<xsl:value-of select="if ($nbPagesTars != $nbPagesPointage) then string-join((''&#xa;nbPagesPointage '', string($nbPagesPointage), ''!= nbPagesTars '', string($nbPagesTars)), '''') else ''''" />
		
		<xsl:variable name="idocr" select="$firstXml//@id_publication/string()" />
		<xsl:value-of select="if (not(boolean(java:isEAN13($idocr)))) then string-join((''&#xa;idocr '', $idocr, '' no valid.''), '''') else ''''" />
	</xsl:template>
</xsl:stylesheet>', (select id from referentiel.type_module where nom = 'LIVRABLES'));

-- insert génération de parmètres identPublication et datePublication
INSERT INTO referentiel.ressources(nom,contenu,id_type_module) VALUES('XSD_VALID_XML','<xs:schema attributeFormDefault="unqualified" elementFormDefault="qualified" xmlns:xs="http://www.w3.org/2001/XMLSchema">
  <xs:element name="pagepublication" type="pagepublicationType"/>
  <xs:complexType name="flunumType">
    <xs:simpleContent>
      <xs:extension base="xs:string">
        <xs:attribute type="xs:string" name="fln_activation_edition"/>
        <xs:attribute type="xs:string" name="fln_date_creation_argus"/>
        <xs:attribute type="xs:string" name="fln_edition"/>
        <xs:attribute type="xs:string" name="fln_fichier_source"/>
        <xs:attribute type="xs:string" name="fln_fil"/>
        <xs:attribute type="xs:string" name="fln_groupe_editeur"/>
        <xs:attribute type="xs:long" name="fln_id_publication"/>
        <xs:attribute type="xs:string" name="fln_lotarticle"/>
        <xs:attribute type="xs:byte" name="fln_num_page"/>
        <xs:attribute type="xs:string" name="fln_provenance"/>
        <xs:attribute type="xs:string" name="fln_sceau"/>
        <xs:attribute type="xs:string" name="fln_titre"/>
        <xs:attribute type="xs:string" name="fln_url_pdf"/>
        <xs:attribute type="xs:string" name="fln_url_pdf_bool"/>
      </xs:extension>
    </xs:simpleContent>
  </xs:complexType>
  <xs:complexType name="identificationType">
    <xs:simpleContent>
      <xs:extension base="xs:string">
        <xs:attribute type="xs:long" name="id_publication"/>
      </xs:extension>
    </xs:simpleContent>
  </xs:complexType>
  <xs:complexType name="source_magazineType">
    <xs:simpleContent>
      <xs:extension base="xs:string">
        <xs:attribute type="xs:string" name="smg_date_parution"/>
        <xs:attribute type="xs:string" name="smg_express"/>
        <xs:attribute type="xs:string" name="smg_fichier_source"/>
        <xs:attribute type="xs:byte" name="smg_nb_articles"/>
        <xs:attribute type="xs:byte" name="smg_nb_pages"/>
        <xs:attribute type="xs:string" name="smg_priorite"/>
        <xs:attribute type="xs:string" name="smg_provenance"/>
        <xs:attribute type="xs:string" name="smg_titre"/>
      </xs:extension>
    </xs:simpleContent>
  </xs:complexType>
  <xs:complexType name="info_extractionType">
    <xs:simpleContent>
      <xs:extension base="xs:string">
        <xs:attribute type="xs:byte" name="xtrac_duree"/>
        <xs:attribute type="xs:string" name="xtrac_heure_debut"/>
        <xs:attribute type="xs:string" name="xtrac_valide"/>
      </xs:extension>
    </xs:simpleContent>
  </xs:complexType>
  <xs:complexType name="extractionType">
    <xs:sequence>
      <xs:element type="info_extractionType" name="info_extraction"/>
    </xs:sequence>
  </xs:complexType>
  <xs:complexType name="info_verificationType">
    <xs:simpleContent>
      <xs:extension base="xs:string">
        <xs:attribute type="xs:byte" name="vrif_duree"/>
        <xs:attribute type="xs:string" name="vrif_heure_debut"/>
      </xs:extension>
    </xs:simpleContent>
  </xs:complexType>
  <xs:complexType name="verificationType">
    <xs:sequence>
      <xs:element type="info_verificationType" name="info_verification"/>
    </xs:sequence>
  </xs:complexType>
  <xs:complexType name="info_impression_publicationType">
    <xs:simpleContent>
      <xs:extension base="xs:string">
        <xs:attribute type="xs:byte" name="ipb_duree"/>
        <xs:attribute type="xs:string" name="ipb_heure_debut"/>
      </xs:extension>
    </xs:simpleContent>
  </xs:complexType>
  <xs:complexType name="impression_publicationType">
    <xs:sequence>
      <xs:element type="info_impression_publicationType" name="info_impression_publication"/>
    </xs:sequence>
  </xs:complexType>
  <xs:complexType name="info_generation_imgType">
    <xs:simpleContent>
      <xs:extension base="xs:string">
        <xs:attribute type="xs:byte" name="img_dure"/>
        <xs:attribute type="xs:string" name="img_heure_debut"/>
        <xs:attribute type="xs:string" name="img_nom_plateau"/>
        <xs:attribute type="xs:string" name="img_process"/>
      </xs:extension>
    </xs:simpleContent>
  </xs:complexType>
  <xs:complexType name="generation_imgType">
    <xs:sequence>
      <xs:element type="info_generation_imgType" name="info_generation_img"/>
    </xs:sequence>
  </xs:complexType>
  <xs:complexType name="ensembleType">
    <xs:simpleContent>
      <xs:extension base="xs:string">
        <xs:attribute type="xs:string" name="ens_etrangere"/>
        <xs:attribute type="xs:string" name="ens_express"/>
        <xs:attribute type="xs:string" name="ens_normale"/>
        <xs:attribute type="xs:string" name="ens_sectorielle"/>
      </xs:extension>
    </xs:simpleContent>
  </xs:complexType>
  <xs:complexType name="magazineType">
    <xs:simpleContent>
      <xs:extension base="xs:string">
        <xs:attribute type="xs:string" name="mag_adresse1"/>
        <xs:attribute type="xs:string" name="mag_adresse2"/>
        <xs:attribute type="xs:string" name="mag_code_postal"/>
        <xs:attribute type="xs:string" name="mag_couleur"/>
        <xs:attribute type="xs:string" name="mag_crop_page"/>
        <xs:attribute type="xs:date" name="mag_date_cfc"/>
        <xs:attribute type="xs:string" name="mag_date_faciale"/>
        <xs:attribute type="xs:string" name="mag_date_pointage"/>
        <xs:attribute type="xs:string" name="mag_double_lecture"/>
        <xs:attribute type="xs:string" name="mag_ens_etrangere"/>
        <xs:attribute type="xs:string" name="mag_ens_express"/>
        <xs:attribute type="xs:string" name="mag_ens_normale"/>
        <xs:attribute type="xs:string" name="mag_ens_sectorielle"/>
        <xs:attribute type="xs:string" name="mag_floute_dose"/>
        <xs:attribute type="xs:int" name="mag_id_magazine"/>
        <xs:attribute type="xs:long" name="mag_id_ocr"/>
        <xs:attribute type="xs:int" name="mag_id_pointage"/>
        <xs:attribute type="xs:string" name="mag_infographie_cfc"/>
        <xs:attribute type="xs:string" name="mag_langue"/>
        <xs:attribute type="xs:string" name="mag_li_notes_ptn"/>
        <xs:attribute type="xs:string" name="mag_li_notes_rouge"/>
        <xs:attribute type="xs:byte" name="mag_nb_unite"/>
        <xs:attribute type="xs:string" name="mag_no_copy"/>
        <xs:attribute type="xs:string" name="mag_no_reassort"/>
        <xs:attribute type="xs:string" name="mag_nom_argus"/>
        <xs:attribute type="xs:string" name="mag_nom_ensemble"/>
        <xs:attribute type="xs:string" name="mag_nom_magazine"/>
        <xs:attribute type="xs:string" name="mag_numero_page"/>
        <xs:attribute type="xs:int" name="mag_ojd"/>
        <xs:attribute type="xs:string" name="mag_papier_disponible"/>
        <xs:attribute type="xs:string" name="mag_periodicite"/>
        <xs:attribute type="xs:string" name="mag_photos_cfc"/>
        <xs:attribute type="xs:string" name="mag_priorite_scan"/>
        <xs:attribute type="xs:byte" name="mag_prix_cfc"/>
        <xs:attribute type="xs:string" name="mag_rescan"/>
        <xs:attribute type="xs:string" name="mag_scan_allow"/>
        <xs:attribute type="xs:string" name="mag_telephone"/>
        <xs:attribute type="xs:byte" name="mag_traitement_id"/>
        <xs:attribute type="xs:string" name="mag_traitement_lib"/>
        <xs:attribute type="xs:string" name="mag_type_papier"/>
        <xs:attribute type="xs:string" name="mag_url_dans_pdf"/>
        <xs:attribute type="xs:string" name="mag_vignette_couverture"/>
        <xs:attribute type="xs:string" name="mag_vignette_emplacement_article"/>
      </xs:extension>
    </xs:simpleContent>
  </xs:complexType>
  <xs:complexType name="info_pointageType">
    <xs:simpleContent>
      <xs:extension base="xs:string">
        <xs:attribute type="xs:string" name="ptg_heure_debut"/>
        <xs:attribute type="xs:string" name="ptg_nom"/>
        <xs:attribute type="xs:string" name="ptg_utilisateur"/>
      </xs:extension>
    </xs:simpleContent>
  </xs:complexType>
  <xs:complexType name="pointageType">
    <xs:sequence>
      <xs:element type="info_pointageType" name="info_pointage"/>
    </xs:sequence>
  </xs:complexType>
  <xs:complexType name="instructions_ocrType">
    <xs:simpleContent>
      <xs:extension base="xs:string">
        <xs:attribute type="xs:string" name="ocr_chaine_sortie"/>
        <xs:attribute type="xs:byte" name="ocr_compression_sortie_nb"/>
        <xs:attribute type="xs:byte" name="ocr_compression_sortie_traite"/>
        <xs:attribute type="xs:byte" name="ocr_compression_sortie_vignette"/>
        <xs:attribute type="xs:short" name="ocr_dpi_sortie_nb"/>
        <xs:attribute type="xs:short" name="ocr_dpi_sortie_traite"/>
        <xs:attribute type="xs:short" name="ocr_dpi_sortie_vignette"/>
        <xs:attribute type="xs:string" name="ocr_generation_pdf"/>
        <xs:attribute type="xs:string" name="ocr_instructions_profileur"/>
        <xs:attribute type="xs:string" name="ocr_langue"/>
        <xs:attribute type="xs:string" name="ocr_machine_cible"/>
        <xs:attribute type="xs:string" name="ocr_recollage"/>
        <xs:attribute type="xs:string" name="ocr_recoupement"/>
        <xs:attribute type="xs:string" name="ocr_repertoire_sortie"/>
        <xs:attribute type="xs:byte" name="ocr_rotation"/>
        <xs:attribute type="xs:string" name="ocr_sortie_nb"/>
        <xs:attribute type="xs:string" name="ocr_sortie_pos"/>
        <xs:attribute type="xs:string" name="ocr_sortie_source"/>
        <xs:attribute type="xs:string" name="ocr_sortie_texte_cesure"/>
        <xs:attribute type="xs:string" name="ocr_sortie_texte_sans_cesure"/>
        <xs:attribute type="xs:string" name="ocr_sortie_traite"/>
        <xs:attribute type="xs:string" name="ocr_sortie_vignette"/>
        <xs:attribute type="xs:string" name="ocr_taille_sortie_vignette"/>
        <xs:attribute type="xs:short" name="ocr_timeout_traitement"/>
      </xs:extension>
    </xs:simpleContent>
  </xs:complexType>
  <xs:complexType name="info_scanType">
    <xs:simpleContent>
      <xs:extension base="xs:string">
        <xs:attribute type="xs:string" name="scn_couleur"/>
        <xs:attribute type="xs:string" name="scn_mode_remise"/>
        <xs:attribute type="xs:byte" name="scn_nb_pages_publication"/>
        <xs:attribute type="xs:string" name="scn_nom_scanner"/>
        <xs:attribute type="xs:byte" name="scn_numero_page"/>
        <xs:attribute type="xs:short" name="scn_resolution"/>
        <xs:attribute type="xs:string" name="scn_utilisateur"/>
      </xs:extension>
    </xs:simpleContent>
  </xs:complexType>
  <xs:complexType name="pageType">
    <xs:simpleContent>
      <xs:extension base="xs:string">
        <xs:attribute type="xs:string" name="scn_heure"/>
        <xs:attribute type="xs:string" name="scn_id"/>
        <xs:attribute type="xs:string" name="scn_taille_pixels"/>
        <xs:attribute type="xs:byte" name="scn_tps_de_scan"/>
      </xs:extension>
    </xs:simpleContent>
  </xs:complexType>
  <xs:complexType name="scanType">
    <xs:sequence>
      <xs:element type="info_scanType" name="info_scan"/>
      <xs:element type="pageType" name="page"/>
    </xs:sequence>
  </xs:complexType>
  <xs:complexType name="pagepublicationType">
    <xs:sequence>
      <xs:element type="flunumType" name="flunum"/>
      <xs:element type="xs:string" name="type_UD"/>
      <xs:element type="xs:string" name="erreur_sceau"/>
      <xs:element type="identificationType" name="identification"/>
      <xs:element type="source_magazineType" name="source_magazine"/>
      <xs:element type="extractionType" name="extraction"/>
      <xs:element type="verificationType" name="verification"/>
      <xs:element type="impression_publicationType" name="impression_publication"/>
      <xs:element type="generation_imgType" name="generation_img"/>
      <xs:element type="xs:string" name="instructions"/>
      <xs:element type="ensembleType" name="ensemble"/>
      <xs:element type="magazineType" name="magazine"/>
      <xs:element type="pointageType" name="pointage"/>
      <xs:element type="instructions_ocrType" name="instructions_ocr"/>
      <xs:element type="scanType" name="scan"/>
    </xs:sequence>
  </xs:complexType>
</xs:schema>', (select id from referentiel.type_module where nom = 'LIVRABLES'));