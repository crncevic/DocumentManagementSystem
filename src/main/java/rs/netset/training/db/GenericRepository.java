package rs.netset.training.db;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class GenericRepository<T> {

	private EntityManager em;
	private Class c;

	public GenericRepository() {

	}

	public GenericRepository(Class c, EntityManager em) {
		this.c = c;
		this.em = em;
	}

	public void save(T entity) {
		em.persist(entity);
	}

	public void update(T entity) {
		em.merge(entity);
	}

	public void delete(Object key) {
		T entity = (T) em.find(c, key);
		if (entity == null) {
			throw new RuntimeException("Entity doesn't exist's");
		}
		em.remove(entity);
	}

	public List<T> getAll(String namedQuery) {
		TypedQuery<T> query = em.createNamedQuery(namedQuery, c);
		return query.getResultList();
	}

	public T getSingleByParamFromNamedQuery(Object[] paramValues, String[] paramNames, String namedQuery) {

		TypedQuery<T> query = em.createNamedQuery(namedQuery, c);

		for (int i = 0; i < paramNames.length; i++) {
			query.setParameter(paramNames[i], paramValues[i]);
		}

		List<T> result = query.getResultList();
		if (result == null || result.isEmpty()) {
			return null;
		}

		return result.get(0);
	}

	public List<T> getListByParamFromNamedQuery(Object[] paramValues, String[] paramNames, String namedQuery) {

		TypedQuery<T> query = em.createNamedQuery(namedQuery, c);

		for (int i = 0; i < paramNames.length; i++) {
			query.setParameter(paramNames[i], paramValues[i]);
		}

		return query.getResultList();
	}

}