package com.gaoshin.shopping;

import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.beans.Category;
import com.gaoshin.beans.Dimension;
import com.gaoshin.beans.DimensionValue;
import com.gaoshin.business.ConfigurationService;
import com.gaoshin.business.ObjectService;
import common.util.JacksonUtil;

@Component
public class ShoppingConfiguration {
    @Autowired
    private ObjectService objectService;

    @Autowired
    private ConfigurationService configurationService;

    private Long shoppingCategoryId = null;
    private Dimension buySellDim = null;

    public Long getShoppingCategoryId() {
        return shoppingCategoryId;
    }

    public Dimension getBuySellDimension() {
        return buySellDim;
    }

    @PostConstruct
    public void createCategory() {
        if (configurationService.get(ShoppingConfiguration.class.getName(), "false").getBoolean()) {
            shoppingCategoryId = objectService.getCategoryByName(ShoppingConstant.ShoppingRootCategoryName).getId();
            return;
        }

        InputStream inputStream = ShoppingConfiguration.class.getResourceAsStream("/shopping/category.json.txt");
        Category category = JacksonUtil.jsonStream2Object(inputStream, Category.class);
        createCategory(category);

        if (true)
            return;

        category = objectService.getCategory(shoppingCategoryId);
        Dimension buySellDim = new Dimension();
        buySellDim.setName(ShoppingConstant.BuySellDimensionName);

        DimensionValue buy = new DimensionValue();
        buy.setDimvalue(ShoppingConstant.BuyDimensionValue);
        buySellDim.getValues().add(buy);

        DimensionValue sell = new DimensionValue();
        sell.setDimvalue(ShoppingConstant.SellDimensionValue);
        buySellDim.getValues().add(sell);

        buySellDim = objectService.createDimension(buySellDim);
        objectService.addDimension(category.getId(), buySellDim.getId());

        configurationService.save(ShoppingConfiguration.class.getName(), "true");
    }

    private void createCategory(Category category) {
        Category created = objectService.getCategoryByName(category.getName());
        if (created == null)
            created = objectService.createCategory(category);
        if (category.getName().equals(ShoppingConstant.ShoppingRootCategoryName))
            shoppingCategoryId = created.getId();

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
