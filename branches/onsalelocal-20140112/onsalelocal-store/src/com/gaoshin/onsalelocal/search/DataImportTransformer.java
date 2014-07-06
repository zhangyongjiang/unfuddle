package com.gaoshin.onsalelocal.search;

import java.util.Map;

public interface DataImportTransformer {
    boolean transform(Map<String, String> row);
}
