/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.api.gestnotesapi.repository;

import com.api.gestnotesapi.dto.FormAnonymat;
import com.api.gestnotesapi.entities.Anonymat;
import com.api.gestnotesapi.entities.Etudiant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


/**
 *   
 * @author COMPUTER STORES
 */

public interface AnonymatRepo extends JpaRepository<Anonymat, Long> {

    public Anonymat findByValeur(String valeur);
    
    @Query(value = "SELECT COUNT(*) FROM ANONYMAT " +
           "WHERE COURS_ID = :EcId " +
           "AND SESSIONS = :sessions"+"AND ANNEEACADEMIQUE_ID = :AcaId",
           nativeQuery = true)
    List<Object []> countGeneratedAnonymats(@Param("EcId") Long EcId, @Param("sessions") int sessions,@Param("AcaId") long AcaId);
    
    
    @Query(value = 
     """
     SELECT e.*
     FROM (
         SELECT
             et.*,
             SUM(subquery.MOYENNE_COURS_UE * credit.VALEUR) / SUM(credit.VALEUR) AS MOYENNE_GENERALE,
             CASE
                 WHEN SUM(subquery.MOYENNE_COURS_UE * credit.VALEUR) / SUM(credit.VALEUR) >= 10 THEN 'VALIDE'
                 WHEN SUM(subquery.MOYENNE_COURS_UE * credit.VALEUR) / SUM(credit.VALEUR) < 7 THEN 'RATTRAPAGE'
             END AS STATUT
         FROM (
             SELECT
                 note.ETUDIANT_ID,
                 u.cours_ID AS COURS_ID,
                 SUM((note.VALEUR * CAST(ed.POURCENTAGE AS DECIMAL(10,2)) / 100)) / SUM(CAST(ed.POURCENTAGE AS DECIMAL(10,2)) / 100) AS MOYENNE_COURS_UE
             FROM Note note
             JOIN EVALUATIONDETAILS as ed ON note.EVALUATION_ID = ed.EVALUATION_ID
             JOIN uniteenseignement_cours u ON note.COURS_ID = u.cours_ID
             JOIN COURS c ON c.ID = u.cours_ID
             JOIN uniteenseignement ue ON ue.ID = u.uniteEnseignements_ID
             WHERE u.uniteEnseignements_ID = :ueId
               AND note.ANNEEACADEMIQUE_ID =(SELECT a.ID FROM anneeacademique a WHERE a.NUMERODEBUT=(SELECT u.NUMERODEBUT-1 FROM anneeacademique u WHERE u.ID=:acaId)
               AND note.SESSIONS = :sessions
               AND ed.TYPECOURS_ID = (SELECT c.TYPECOURS_ID FROM COURS c WHERE c.ID = u.cours_ID)
             GROUP BY note.ETUDIANT_ID, u.cours_ID
         ) AS subquery
         JOIN CREDIT credit ON subquery.COURS_ID = credit.COURS_ID
         JOIN etudiant et ON subquery.ETUDIANT_ID = et.ID
         GROUP BY subquery.ETUDIANT_ID
         HAVING STATUT = 'RATTRAPAGE'
     ) AS et , etudiant e where
     et.ID=e.ID
     """,
    nativeQuery = true)
    List<Object []> EligibleAnneeAvant(@Param("ueId") Long coursId,@Param("acaId") Long acaId,@Param("sessions") int sessions);
    
    @Query(value = """
                   SELECT e.*
                   FROM Etudiant e
                   WHERE e.ID IN (SELECT ins.ETUDIANT_ID
                   FROM inscription ins
                   WHERE ins.ANNEEACADEMIQUE_ID = :acaId
                   and ins.PARCOURS_ID = (SELECT pp.ID FROM parcours pp WHERE pp.NIVEAU_ID=:niveauId and pp.FILIERE_ID=:optionId)
                   and e.ID NOT IN (SELECT a.ETUDIANT_ID FROM anonymat a WHERE a.ANNEEACADEMIQUE_ID =(SELECT a.ID FROM anneeacademique a WHERE a.NUMERODEBUT=(SELECT u.NUMERODEBUT-1 FROM anneeacademique u WHERE u.ID=:acaId)) 
                   and a.COURS_ID IN (SELECT uec.cours_id FROM ue_cours uec WHERE uec.ue_id = :ueId) 
                   and a.NIVEAU_ID = :niveauId))""",nativeQuery = true)
    List<Etudiant> EligibleNormaleAnneeEncours(@Param("ueId") Long ueId,@Param("acaId") Long acaId,@Param("niveauId") Long niveauId,@Param("optionId") Long optionId);
    
   
       @Query(value = 
     """
      SELECT e.*
      FROM (
          SELECT
                       et.*,
                       SUM(subquery.MOYENNE_COURS_UE * credit.VALEUR) / SUM(credit.VALEUR) AS MOYENNE_GENERALE,
                       CASE
                         WHEN SUM(subquery.MOYENNE_COURS_UE * credit.VALEUR) / SUM(credit.VALEUR) >= 10 THEN 'VALIDE'
                         WHEN SUM(subquery.MOYENNE_COURS_UE * credit.VALEUR) / SUM(credit.VALEUR) < 7 THEN 'RATTRAPAGE'
                       END AS STATUT
                     FROM (
                       SELECT
                         note.ETUDIANT_ID,
                         u.cours_ID AS COURS_ID,
                         SUM((note.VALEUR * CAST(ed.POURCENTAGE AS DECIMAL(10,2)) / 100)) / SUM(CAST(ed.POURCENTAGE AS DECIMAL(10,2)) / 100) AS MOYENNE_COURS_UE
                       FROM Note note
                       JOIN EVALUATIONDETAILS as ed ON note.EVALUATION_ID = ed.EVALUATION_ID
                       JOIN uniteenseignement_cours u ON note.COURS_ID = u.cours_ID
                       JOIN COURS c ON c.ID = u.cours_ID
                       JOIN uniteenseignement ue ON ue.ID = u.uniteEnseignements_ID
                       WHERE u.uniteEnseignements_ID = :ueId
                         AND note.ANNEEACADEMIQUE_ID =:acaId
                         AND note.SESSIONS = :sessions
                         AND ed.TYPECOURS_ID = (SELECT c.TYPECOURS_ID FROM COURS c WHERE c.ID = u.cours_ID)
                       GROUP BY note.ETUDIANT_ID, u.cours_ID
                     ) AS subquery
                     JOIN CREDIT credit ON subquery.COURS_ID = credit.COURS_ID
                     JOIN etudiant et ON subquery.ETUDIANT_ID = et.ID
                     GROUP BY subquery.ETUDIANT_ID
                     HAVING STATUT='RATTRAPAGE'
      ) AS et , etudiant e where
      et.ID=e.ID
     """,
    nativeQuery = true)
    List<Object []> EligibleRattrapageAnneeEncours(@Param("ueId") Long coursId,@Param("acaId") Long acaId,@Param("sessions") int sessions);
    
    @Query(value = """
                   SELECT e.MATRICULE,a.VALEUR,a.COURS_ID  
                   FROM `anonymat` a,etudiant e 
                   WHERE a.COURS_ID IN (SELECT uec.COURS_ID FROM uniteenseignement_cours uec WHERE uec.uniteEnseignements_ID = :ueId) AND e.ID = a.ETUDIANT_ID and a.ANNEEACADEMIQUE_ID =:acaId AND a.NIVEAU_ID=:niveauId and SESSIONS=:session
                   """,nativeQuery = true)
    List<Object []> ListAnonymat(@Param("ueId") Long ueId,@Param("acaId") Long acaId,@Param("niveauId") Long niveauId,@Param("session") int session);
    
  
    }
