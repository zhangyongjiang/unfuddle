package com.gaoshin.business;

import com.gaoshin.beans.Review;
import com.gaoshin.beans.ReviewSummary;
import com.gaoshin.beans.ReviewTarget;

public interface ReviewService {

    Review create(Review review);

    ReviewSummary list(ReviewTarget type, Long id);
}
