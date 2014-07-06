package common.util.db;

public class ClassTree {
    private Class thisClass;
    private ClassTree parent;
    private ClassTree child;
    
    public ClassTree(Class thisClass, ClassTree parent) {
        this.thisClass = thisClass;
        this.parent = parent;
    }

    public ClassTree getParent() {
        return parent;
    }

    public void setParent(ClassTree parent) {
        this.parent = parent;
    }

    public Class getThisClass() {
        return thisClass;
    }

    public void setThisClass(Class thisClass) {
        this.thisClass = thisClass;
    }

    public Class getDetailsClass() {
        try {
            return this.getClass().forName(thisClass.getName() + "Details");
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Class getDetailsListClass() {
        try {
            return this.getClass().forName(thisClass.getName() + "DetailsList");
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDetailsFieldName() {
        String name = thisClass.getName() + "Details";
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    public String getDetailsListFieldName() {
        String name = thisClass.getName() + "DetailsList";
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    public String getIdFieldName() {
        String name = getThisClass().getSimpleName();
        return name.substring(0, 1).toLowerCase() + name.substring(1) + "Id";
    }

    public ClassTree getChild() {
        return child;
    }

    public void setChild(ClassTree child) {
        this.child = child;
    }
}
