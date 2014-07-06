package com.gaoshin.onsalelocal.osl.service;

import java.io.IOException;
import java.io.OutputStream;

import com.gaoshin.onsalelocal.osl.beans.OfferSearchRequest;
import com.gaoshin.onsalelocal.osl.beans.OfferSearchResponse;

public interface SearchService {
    OfferSearchResponse search(OfferSearchRequest req) throws Exception;
    void searchStore(String query, OutputStream out) throws IOException;
}
