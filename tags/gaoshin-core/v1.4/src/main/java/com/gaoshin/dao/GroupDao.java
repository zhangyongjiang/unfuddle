package com.gaoshin.dao;

import java.util.List;

import com.gaoshin.dao.jpa.DaoComponent;
import com.gaoshin.entity.PostEntity;

public interface GroupDao extends DaoComponent {

    List<PostEntity> listLatestGroupPosts(Long groupId, Long beforeId, int size);

    List<PostEntity> listLatestUserPosts(Long userId, Long beforeId, int size);

    List<PostEntity> listByScores(Long groupId, int start, int size);

}
