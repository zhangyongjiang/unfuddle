package com.gaoshin.dao.jpa;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gaoshin.dao.GroupDao;
import com.gaoshin.entity.PostEntity;

@Repository("groupDao")
public class GroupDaoJpaImpl extends DaoComponentImpl implements GroupDao {

    @Override
    public List<PostEntity> listLatestGroupPosts(Long groupId, Long beforeId, int size) {
        String sql = "select p from PostEntity p where p.group.id=" + groupId;
        if (beforeId != null && beforeId > 0l) {
            sql += " and p.id < " + beforeId;
        }
        sql += " order by p.createTime desc";
        return entityManager.createQuery(sql).setMaxResults(size).getResultList();
    }

    @Override
    public List<PostEntity> listLatestUserPosts(Long userId, Long beforeId, int size) {
        String sql = "select p from PostEntity p where p.author.id=" + userId;
        if (beforeId != null && beforeId > 0l) {
            sql += " and p.id < " + beforeId;
        }
        sql += " order by p.createTime desc";
        return entityManager.createQuery(sql).setMaxResults(size).getResultList();
    }

    @Override
    public List<PostEntity> listByScores(Long groupId, int start, int size) {
        String sql = "select p from PostEntity p where p.group.id=" + groupId;
        sql += " order by p.thumbup desc";
        return entityManager.createQuery(sql).setFirstResult(start).setMaxResults(size).getResultList();
    }
}
