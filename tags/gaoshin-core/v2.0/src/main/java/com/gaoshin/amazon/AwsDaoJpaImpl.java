package com.gaoshin.amazon;

import org.springframework.stereotype.Repository;

import com.gaoshin.dao.jpa.DaoComponentImpl;

@Repository("awsDao")
public class AwsDaoJpaImpl extends DaoComponentImpl implements AwsDao {

}
