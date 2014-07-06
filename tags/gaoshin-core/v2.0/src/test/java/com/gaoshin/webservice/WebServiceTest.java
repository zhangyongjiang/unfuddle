package com.gaoshin.webservice;

import org.junit.Assert;
import org.junit.Test;

import com.gaoshin.beans.Category;
import com.gaoshin.beans.Dimension;
import com.gaoshin.beans.DimensionValue;
import com.gaoshin.beans.GenericResponse;
import com.gaoshin.beans.Location;
import com.gaoshin.beans.ObjectBean;
import com.gaoshin.beans.ObjectCategory;
import com.gaoshin.beans.User;
import com.sun.jersey.api.client.UniformInterfaceException;
import common.web.ServiceError;

public class WebServiceTest extends GaoshinTester {
    private Category root = null;

    private synchronized Category getRootCategory() {
        if (root == null)
            root = getBuilder("/category/1").get(Category.class);
        return root;
    }

    @Test
    public void testCategory() {
        // create a new category
        Category createdCategory1 = createCategory(getRootCategory());

        // verify if created
        Category newRoot = getBuilder("/category/1").get(Category.class);
        Assert.assertTrue(newRoot.contains(createdCategory1.getId()));

        // create a new category
        Category createdCategory2 = createCategory(getRootCategory());

        // verify if created
        Category newRoot2 = getBuilder("/category/1").get(Category.class);
        Assert.assertTrue(newRoot2.contains(createdCategory1.getId()));
        Assert.assertTrue(newRoot2.contains(createdCategory2.getId()));

        Category actualCat2 = getBuilder("/category/" + createdCategory2.getId()).get(Category.class);

        Dimension saved1 = createDimension();
        Category catHasDim = getBuilder("/category/add-dimension/" + createdCategory1.getId() + "/" + saved1.getId()).post(Category.class, " ");
        Assert.assertTrue(catHasDim.hasDimension(saved1.getId()));

        Dimension saved2 = createDimension();
        Category cat3 = getBuilder("/category/add-dimension/" + createdCategory1.getId() + "/" + saved2.getId()).post(Category.class, " ");
        Assert.assertTrue(cat3.hasDimension(saved1.getId()));
        Assert.assertTrue(cat3.hasDimension(saved2.getId()));

        // remove dim1
        getBuilder("/dimension/remove/" + saved1.getId()).post(" ");

        try {
            getBuilder("/dimension/" + saved1.getId()).get(String.class);
            Assert.assertTrue(false);
        } catch (UniformInterfaceException e) {
            Assert.assertEquals(ServiceError.NotFound.getErrorCode(), e.getResponse().getStatus());
        }

        Category cat4 = getBuilder("/category/" + createdCategory1.getId()).get(Category.class);
        Assert.assertFalse(cat4.hasDimension(saved1.getId()));
    }

    @Test
    public void testDimension() {
        createDimension();
    }

    @Test
    public void testDimensionValues() {
        Dimension dim = createDimension();
        DimensionValue value1 = addDimensionValue(dim);
        DimensionValue value2 = addDimensionValue(dim);
        Dimension actual = getBuilder("/dimension/" + dim.getId()).get(Dimension.class);
        Assert.assertEquals(2, actual.getValues().size());
    }

    @Test
    public void testCreateObject() {
        User user = createUser();
        login(user);

        Category cat = createCategory(getRootCategory());
        Dimension dim = createDimension();
        getBuilder("/category/add-dimension/" + cat.getId() + "/" + dim.getId()).post(" ");
        DimensionValue value1 = addDimensionValue(dim);
        DimensionValue value2 = addDimensionValue(dim);

        Category cat2 = createCategory(getRootCategory());
        Dimension dim2 = createDimension();
        getBuilder("/category/add-dimension/" + cat2.getId() + "/" + dim2.getId()).post(" ");
        DimensionValue value3 = addDimensionValue(dim2);
        DimensionValue value4 = addDimensionValue(dim2);

        ObjectBean bean = new ObjectBean();
        bean.setDescription(System.currentTimeMillis() + " description mobile advertiser");
        bean.setTitle(System.currentTimeMillis() + " title");
        ObjectCategory objectCategory = new ObjectCategory();
        bean.getCategories().add(objectCategory);
        objectCategory.setCategory(cat);
        objectCategory.getDimValues().add(value1);

        try {
            getBuilder("/object/create").post(ObjectBean.class, bean);
            Assert.assertTrue(false);
        } catch (UniformInterfaceException e) {
            Assert.assertEquals(ServiceError.InvalidInput.getErrorCode(), e.getResponse().getStatus());
        }

        Location location = createLocation(Latitude, Longitude, true);
        bean.setLocation(location);

        ObjectBean created = getBuilder("/object/create").post(ObjectBean.class, bean);
    }

    @Test
    public void testCatch() {
        // System.out.println(getBuilder("/nihao").get(String.class));
        // System.out.println(getBuilder("/ni/hao", "a", "1", "b",
        // "2").get(String.class));
        // System.out.println(getBuilder("/object").get(String.class));
        // System.out.println(getBuilder("/object/abc").get(String.class));
        // System.out.println(getBuilder("/object/abc/def").get(String.class));
    }

    @Test
    public void testUser() {
        User user = createUser();
        User login = getBuilder("/user/login").post(User.class, user);
        User current = getBuilder("/user/profile").get(User.class);
        GenericResponse logout = getBuilder("/user/logout").post(GenericResponse.class, " ");
        try {
            current = getBuilder("/user/profile").get(User.class);
        } catch (UniformInterfaceException e) {
            Assert.assertEquals(ServiceError.NoGuest.getErrorCode(), e.getResponse().getStatus());
        }
    }

    private Category createCategory(Category parent) {
        Category category = new Category();
        category.setName(System.currentTimeMillis() + "");
        category.setParent(parent);
        Category created = getBuilder("/category/create").post(Category.class, category);
        return created;
    }

    private Dimension createDimension() {
        Long id = System.currentTimeMillis() + System.nanoTime();
        String random = id + "";

        Dimension dim = new Dimension();
        dim.setName(random);
        return getBuilder("/dimension/create").post(Dimension.class, dim);
    }

    private DimensionValue addDimensionValue(Dimension dim) {
        DimensionValue value = new DimensionValue();
        value.setDvalue(System.currentTimeMillis() + "");
        DimensionValue added = getBuilder("/dimension/add-dimension-value/" + dim.getId()).post(DimensionValue.class, value);
        return added;
    }

}
