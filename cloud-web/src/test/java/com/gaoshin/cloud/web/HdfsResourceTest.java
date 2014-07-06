package com.gaoshin.cloud.web;

import org.junit.Test;

import com.gaoshin.cloud.web.bean.hadoop.HdfsFile;
import common.util.JacksonUtil;

public class HdfsResourceTest extends GaoshinResourceTester {
    @Test
	public void testInfo() {
        HdfsFile hdfsFile = getBuilder("/hadoop/hdfs/info", "path", "/user/hadoop/output_wc").get(HdfsFile.class);
        System.out.println(JacksonUtil.obj2Json(hdfsFile));
	}
}
