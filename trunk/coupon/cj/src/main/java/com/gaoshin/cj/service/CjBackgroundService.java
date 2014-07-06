package com.gaoshin.cj.service;

public interface CjBackgroundService {

    void fetchLinks() throws Exception;

    int fetchAdvertisers() throws Exception;

    void fetchJoinedAdvertiser() throws Exception;

    void refreshAdvertiserLinks(String advertiserId) throws Exception;

}
