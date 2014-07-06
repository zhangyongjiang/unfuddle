/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.util.web;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import com.gaoshin.points.server.bean.ItemList;
import com.gaoshin.points.server.bean.UserBalanceList;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

@Provider
public class JAXBContextResolver implements ContextResolver<JAXBContext> {
    private JAXBContext context;
    private Class[] types = {
            ItemList.class,
            UserBalanceList.class,
            };

    public JAXBContextResolver() throws Exception {
        this.context = new JSONJAXBContext(JSONConfiguration
                .mapped()
                .arrays("list", "children", "items", "scenes", "msgs",
                        "values", "ucmList", "members", "attrNames").build(),
                types);
    }

    public JAXBContext getContext(Class<?> objectType) {
        for (Class type : types) {
            if (type.equals(objectType)) {
                return context;
            }
        }
        return null;
    }
}
