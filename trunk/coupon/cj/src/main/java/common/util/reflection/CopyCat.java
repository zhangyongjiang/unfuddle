package common.util.reflection;

import org.springframework.beans.BeanUtils;

public class CopyCat {
	public static Object copyProperties(Object src, Object dst, String... ignores) {
		if(src == null)
			return null;
		BeanUtils.copyProperties(src, dst, ignores);
		return dst;
	}
	
	public static <T> T copyProperties(Object src, Class<T> dstClass, String... ignores) {
		if(src == null)
			return null;
		T dst = null;
		try {
			dst = dstClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		BeanUtils.copyProperties(src, dst, ignores);
		return dst;
	}
}
