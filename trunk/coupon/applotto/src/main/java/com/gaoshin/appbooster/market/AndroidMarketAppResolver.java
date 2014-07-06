package com.gaoshin.appbooster.market;

import org.springframework.stereotype.Component;

import com.gaoshin.appbooster.entity.Application;
import com.gaoshin.appbooster.entity.ApplicationType;
import com.gaoshin.appbooster.service.AppResolver;

@Component
public class AndroidMarketAppResolver implements AppResolver {

    @Override
    public Application getApplication(String id) {
        if(id.equals("com.apptera.raved")) {
            Application app = new Application();
            app.setType(ApplicationType.Android);
            app.setMarketId(id);
            app.setName("Raved");
            app.setIcon("http://profile.ak.fbcdn.net/hprofile-ak-snc4/372865_171891189587320_1465414931_n.jpg");
            app.setDescription("Raved combines aspects of and content from Facebook Inc., Foursquare and Yelp Inc., all of which are partners, to let users see nearby restaurants, bars, shops and service providers and those that friends recommend. This means users don’t have to rely on anonymous online tips. The app is built atop aggregated data from various social graphs, content sources and a database of almost 20 million U.S. merchants. Users don’t need to find or follow anyone to get started; Raved can automatically integrate “friends,” their “likes” and “check-ins,” which it translates to “raves,” from users’ social networks.");
            return app;
        }
        
        throw new RuntimeException("not found " + id );
    }

    @Override
    public ApplicationType getType() {
        return ApplicationType.Android;
    }

}
