package services.impl;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import entities.Enfant;
import entities.User;
import services.EnfantServices;

@Stateful
@LocalBean
public class EnfantServicesImpl implements EnfantServices{
	
	@PersistenceContext(name = "SchoolManager-Entity")
	private EntityManager em;

	@Override
	public Enfant getEnfant(String nom, String prenom) {
		Enfant enfant = (Enfant)em.createQuery("SELECT u From Enfant u Where u.nom LIKE :nom AND u.prenom LIKE :prenom ")
				.setParameter("nom", nom)
				.setParameter("prenom", prenom)
				.setMaxResults(1)
				.getSingleResult();
		
		if(enfant == null)System.out.println("!! Aucun enfant " + nom + " " + prenom + " trouvé !!");
		
		return enfant;
	}

	@Override
	public void addEnfant(Enfant enfant) {
		em.persist(enfant);
	}

	@Override
	public List<Enfant> getListEnfant() {
		Query query = em.createQuery("SELECT u FROM Enfant u");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enfant> getListEnfantClasse(int idClasse) {
		System.out.println("____ Enfant Service :  Id classe : "+idClasse); 
		Query query = em.createQuery("SELECT u FROM Enfant u WHERE u.TEClasseCla.idClasse =:id ", Enfant.class).setParameter("id", idClasse);
		return query.getResultList();
	}
		  
	public void editEnfant(Enfant modif) {
		Enfant enf = em.find(Enfant.class, modif.getIdPersonne());
		
		em.getTransaction().begin();
		enf.setNom(modif.getNom());
		enf.setPrenom(modif.getPrenom());
		enf.setTEClasseCla(modif.getTEClasseCla());
		enf.setDateNaissance(modif.getDateNaissance());
		em.getTransaction().commit();
	}

}
