package common.util.web;

import org.junit.Before;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

public class SpringResourceTester extends ResourceTester {
	@SuppressWarnings({"unchecked"})
	protected <T> T getTargetObject(Object proxy, Class<T> targetClass) throws Exception {
	  if (AopUtils.isJdkDynamicProxy(proxy)) {
		  return (T) ((Advised)proxy).getTargetSource().getTarget();
	  } else {
		  return (T) proxy; // expected to be cglib proxy then, which is simply a specialized class
	  }
	}
	
	@Before
	public void setup() throws Exception {
		super.setup();
		AnnotationSessionFactoryBean sessionFactory = getSpringBean(AnnotationSessionFactoryBean.class);
		System.out.println("===== got session factory " + sessionFactory.getHibernateProperties());
	}
}