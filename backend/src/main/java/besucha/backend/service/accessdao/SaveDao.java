package besucha.backend.service.accessdao;

import besucha.backend.exception.DuplicationException;

/**
 * Interface to manage how db access classes save items.
 * @param <T> The Dao object to be saved
 */
public interface SaveDao<T> {

	T save(T item) throws DuplicationException;
}
