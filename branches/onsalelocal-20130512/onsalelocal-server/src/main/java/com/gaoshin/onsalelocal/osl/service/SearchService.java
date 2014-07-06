package com.gaoshin.onsalelocal.osl.service;

import com.gaoshin.onsalelocal.osl.beans.OfferSearchRequest;
import com.gaoshin.onsalelocal.osl.beans.OfferSearchResponse;

public interface SearchService {
    OfferSearchResponse search(OfferSearchRequest req) throws Exception;
}
