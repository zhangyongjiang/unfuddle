package com.gaoshin.dao.jpa;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gaoshin.dao.ObjectDao;
import com.gaoshin.entity.CategoryEntity;
import com.gaoshin.entity.ObjectEntity;

@Repository("objectDao")
public class ObjectDaoJpaImpl extends DaoComponentImpl implements ObjectDao {
    @Override
    public List<CategoryEntity> getObjectCategories(ObjectEntity entity) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public List<ObjectEntity> search(String keywords, List<String> dimValueList, Float latitude, Float longitude,
            int mile, int offset, int size) {
        float lat0 = latitude - (float) mile * 0.01f;
        float lat1 = latitude + (float) mile * 0.01f;
        float lng0 = longitude - (float) mile * 0.005f;
        float lng1 = longitude + (float) mile * 0.005f;

        String query = "select o.id as ID, o.title, o.description " +
                " from object o, obj_dim od, user_location loc " +
                " where o.loc_id is not null and o.id = od.obj_id " +
                " and loc.latitude is not null and o.loc_id = loc.id " +
                " and loc.latitude > " + lat0 + " and loc.latitude < " + lat1 +
                " and loc.longitude > " + lng0 + " and loc.longitude < " + lng1 +
                " and od.dim_value in (" + getInSql(dimValueList) + ")";

        if (isMysql() && keywords != null && keywords.trim().length() > 0) {
            query += " and match (title, description) against ('" + keywords + "'  IN BOOLEAN MODE)";
        }

        Query nativeQuery = entityManager.createNativeQuery(query, ObjectEntity.class);
        nativeQuery.setFirstResult(offset);
        nativeQuery.setMaxResults(size);
        return nativeQuery.getResultList();
    }

    @Override
    public void init() {
        if (isMysql()) {
            createMysqlFullTextIndex();
        } else if (isH2()) {
            createH2FullTextIndex();
        }
    }

    private void createMysqlFullTextIndex() {
        String sql = null;

        sql = "SHOW COLUMNS FROM object WHERE field = 'title'";
        Object[] column = (Object[]) nativeQuerySelectRow(sql);
        if (!"text".equalsIgnoreCase(column[1].toString())) {
            sql = "alter table object change title title text not null";
            nativeQueryUpdate(sql);
        }

        sql = "SHOW COLUMNS FROM object WHERE field = 'description'";
        Object[] columns = (Object[]) nativeQuerySelectRow(sql);
        if (!"text".equalsIgnoreCase(columns[1].toString())) {
            sql = "alter table object change description description text not null";
            nativeQueryUpdate(sql);
        }

        sql = "SHOW INDEX FROM object WHERE Key_name = 'obj_search_index'";
        List<Object[]> index = nativeQuerySelect(sql);
        if (index == null || index.size() == 0) {
            // sql =
            // "alter table object add fulltext obj_search_index (title, description)";
            // nativeQueryUpdate(sql);
        }
    }

    private void createH2FullTextIndex() {
        String sql = null;

        sql = "CREATE ALIAS IF NOT EXISTS FTL_INIT FOR \"org.h2.fulltext.FullTextLucene.init\"";
        nativeQueryUpdate(sql);

        // fulltext search of in-memory database is not supported
        sql = "CALL FTL_INIT()";
        nativeQueryUpdate(sql);
    }
}
