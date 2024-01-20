package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTransferDaoTests extends BaseDaoTests{


    protected static final User USER_1 = new User(1001, "user1", "user1", "USER");
    protected static final User USER_2 = new User(1002, "user2", "user2", "USER");
    private static final User USER_3 = new User(1003, "user3", "user3", "USER");
    protected static final Transfer TRANSFER_1 = new Transfer();

    private JdbcUserDao sut;
    private JdbcTransferDao tut;

    @Before
    public void setup(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(jdbcTemplate);
        tut = new JdbcTransferDao(jdbcTemplate);
    }

    @Test()
    public void getTransfersById_given_invalid_id_returns_null(){
        Transfer actualTransfer = tut.getTransferById(-1);
        Assert.assertNul(actualTransfer);

    }


}
