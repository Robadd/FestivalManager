package de.robadd.festivalmanager.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.robadd.festivalmanager.db.CrudRepository;
import de.robadd.festivalmanager.model.type.Identifiable;

public class DbUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(DbUtils.class);
    private static Map<Class<? extends Identifiable>, CrudRepository<? extends Identifiable>> cachedRepos = new HashMap<>();

    private DbUtils()
    {
    }

    @SuppressWarnings("unchecked") // checked by access class
    public static <T extends Identifiable> boolean writeToDb(final List<T> objects, final Class<T> clazz)
    {
        cachedRepos.putIfAbsent(clazz, CrudRepository.of(clazz));
        CrudRepository<?> crudRepository = cachedRepos.get(clazz);
        return ((CrudRepository<T>) crudRepository).save(objects);
    }
}
