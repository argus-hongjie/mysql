--Update module Import Pivot
UPDATE referentiel.type_module set id_type_module_suivant = (SELECT id FROM referentiel.type_module WHERE nom = 'ATTRIBUTS') where nom = 'FORMAT_PIVOT';


--Update module Attribut
UPDATE referentiel.type_module set id_type_module_suivant = (SELECT id FROM referentiel.type_module WHERE nom = 'DEDOUBLONNEMENT') where nom = 'ATTRIBUTS';


--Update module Dedoublonnage
UPDATE referentiel.type_module set id_type_module_suivant = (SELECT id FROM referentiel.type_module WHERE nom = 'SOUS_LOTS') where nom = 'DEDOUBLONNEMENT';


--Update module Sous-Lots
UPDATE referentiel.type_module set id_type_module_suivant = (SELECT id FROM referentiel.type_module WHERE nom = 'COUVERTURE_PAGE') where nom = 'SOUS_LOTS';


--Update module Couverture page
UPDATE referentiel.type_module set id_type_module_suivant = (SELECT id FROM referentiel.type_module WHERE nom = 'IDENTIFIANT_PRESSE') where nom = 'COUVERTURE_PAGE';

--Update module Pointage
UPDATE referentiel.type_module set id_type_module_suivant = (SELECT id FROM referentiel.type_module WHERE nom = 'POINTAGE') where nom = 'IDENTIFIANT_PRESSE';

--Update module Identifiant presse
UPDATE referentiel.type_module set id_type_module_suivant = (SELECT id FROM referentiel.type_module WHERE nom = 'FICHIERS_IMAGES_POSITIONS') where nom = 'POINTAGE';

--Update module fichiers images positions
UPDATE referentiel.type_module set id_type_module_suivant = (SELECT id FROM referentiel.type_module WHERE nom = 'ATTENTE_POINTAGE') where nom = 'FICHIERS_IMAGES_POSITIONS';

--Update module attente pointage
UPDATE referentiel.type_module set id_type_module_suivant = (SELECT id FROM referentiel.type_module WHERE nom = 'LIVRABLES') where nom = 'ATTENTE_POINTAGE';


