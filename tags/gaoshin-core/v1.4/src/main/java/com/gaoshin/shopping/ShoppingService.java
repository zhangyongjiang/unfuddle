package com.gaoshin.shopping;

import com.gaoshin.beans.ObjectBean;

public interface ShoppingService {

    ObjectBean create(Long categoryId, ObjectBean object);

}
