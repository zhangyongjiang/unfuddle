package com.gaoshin.dating;

import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.beans.Category;
import com.gaoshin.beans.Dimension;
import com.gaoshin.business.ConfigurationService;
import com.gaoshin.business.ObjectService;
import common.util.JacksonUtil;

@Component
public class DatingSeedData {
    public static final String DatingCategoryName = "Dating";

    @Autowired
    private ObjectService objectService;

    @Autowired
    private ConfigurationService configurationService;

    @PostConstruct
    public void createCategory() {
        if (configurationService.get(DatingSeedData.class.getName(), "false").getBoolean()) {
            return;
        }

        InputStream inputStream = DatingSeedData.class.getResourceAsStream("/dating/category.json.txt");
        Category category = JacksonUtil.jsonStream2Object(inputStream, Category.class);
        createCategory(category);

        configurationService.save(DatingSeedData.class.getName(), "true");
    }

    private void createCategory(Category category) {
        Category created = objectService.getCategoryByName(category.getName());
        if (created == null)
            created = objectService.createCategory(category);

        for (Category child : category.getChildren()) {
            child.setParent(created);
            createCategory(child);
        }
        for (Dimension dim : category.getDimensions()) {
            Dimension dimension = objectService.getDimensionByName(dim.getName());
            if (dimension == null) {
                dimension = objectService.createDimension(dim);
            }
            if (!created.hasDimension(dimension.getId())) {
                objectService.addDimension(created.getId(), dimension.getId());
            }
        }
    }
}
