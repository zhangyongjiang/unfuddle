package com.gaoshin.cj.service;

import com.gaoshin.cj.beans.LinkDetails;
import com.gaoshin.cj.beans.LinkList;

public interface CjService {

    LinkDetails getLinkDetails(String linkId);

    LinkList getLinkList(int offset, int size);

}
