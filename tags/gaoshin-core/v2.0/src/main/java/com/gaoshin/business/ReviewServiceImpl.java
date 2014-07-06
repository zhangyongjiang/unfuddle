package com.gaoshin.business;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.beans.Review;
import com.gaoshin.beans.ReviewSummary;
import com.gaoshin.beans.ReviewTarget;
import com.gaoshin.dao.UserDao;
import com.gaoshin.entity.ReviewEntity;
import com.gaoshin.entity.ReviewSummaryEntity;
import com.gaoshin.entity.UserEntity;
import common.web.BusinessException;
import common.web.ServiceError;

@Service("reviewService")
@Transactional
public class ReviewServiceImpl extends BaseServiceImpl implements ReviewService {
    @Autowired
    private UserDao userDao;

    @Override
    public Review create(Review review) {
        UserEntity userEntity = userDao.getUser(review.getAuthor());
        if (userEntity == null)
            throw new BusinessException(ServiceError.NotFound);

        ReviewSummaryEntity reviewSummaryEntity = userDao.getFirstEntityBy(ReviewSummaryEntity.class, "targetId", review.getTargetId(), "targetType", review.getTargetType());
        if (reviewSummaryEntity == null) {
            reviewSummaryEntity = new ReviewSummaryEntity();
            reviewSummaryEntity.setTargetId(review.getTargetId());
            reviewSummaryEntity.setTargetType(review.getTargetType());
            reviewSummaryEntity.setLastUpdateTime(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
            userDao.saveEntity(reviewSummaryEntity);
        }

        ReviewEntity entity = new ReviewEntity(review);
        entity.setAuthor(userEntity);
        entity.setCreateTime(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
        entity.setReviewSummaryEntity(reviewSummaryEntity);
        userDao.saveEntity(entity);

        reviewSummaryEntity.setThumbsdown(reviewSummaryEntity.getThumbsdown() + review.getThumbsdown());
        reviewSummaryEntity.setThumbsup(reviewSummaryEntity.getThumbsup() + review.getThumbsup());
        reviewSummaryEntity.getReviewEntities().add(entity);
        reviewSummaryEntity.setLastUpdateTime(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
        userDao.saveEntity(reviewSummaryEntity);

        return entity.getBean(Review.class);
    }

    @Override
    public ReviewSummary list(ReviewTarget type, Long id) {
        ReviewSummaryEntity reviewSummaryEntity = userDao.getFirstEntityBy(ReviewSummaryEntity.class, "targetId", id, "targetType", type);
        if (reviewSummaryEntity == null) {
            ReviewSummary reviewSummary = new ReviewSummary();
            return reviewSummary;
        }
        List<ReviewEntity> entities = userDao.findEntityBy(ReviewEntity.class, "targetId", id, "targetType", type);
        ReviewSummary list = reviewSummaryEntity.getBean(ReviewSummary.class);
        for (ReviewEntity entity : entities) {
            list.getReviews().add(entity.getBean(Review.class));
        }
        return list;
    }

}
