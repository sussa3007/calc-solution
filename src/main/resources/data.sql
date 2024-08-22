insert into `users` (`username`,
                     `password`,
                     `nick_name`,
                     `memo`,
                     `user_status`,
                     `user_level`,
                     `calculate_bank`,
                     `calculate_account`,
                     `calculate_account_name`,
                     `deposit_bank`,
                     `deposit_account`,
                     `deposit_account_name`,
                     `commission`,
                     `total_commission`,
                     `telegram`,
                     `balance`,
                     `total_deposit`,
                     `admin_commission_balance`,
                     `agent1commission_balance`,
                     `agent2commission_balance`,
                     `agent3commission_balance`,
                     `top_admin_id`,
                     `top_admin_username`,
                     `top_admin_nick_name`,
                     `top_admin_commission`,
                     `agent1id`,
                     `agent1username`,
                     `agent1nick_name`,
                     `agent1commission`,
                     `agent2id`,
                     `agent2username`,
                     `agent2nick_name`,
                     `agent2commission`,
                     `agent3id`,
                     `agent3username`,
                     `agent3nick_name`,
                     `agent3commission`

)

values ('admin', '{noop}1111!', '관리자','관리자','ACTIVE','ADMIN',
        '정산 은행','정산 계좌','정산 계좌 예금주', '입금 은행','입금 계좌','입금 계좌 예금주',0.0,0.0,'admin',
        0, 0, 0, 0, 0, 0,null,null,null,0.0,null,null,null,0.0,null,null,null,0.0,null,null,null,0.0
       ),
       ('office1', '{noop}1111!', 'office1','office1','ACTIVE','OFFICE',
        '정산 은행','정산 계좌','정산 계좌 예금주', '입금 은행','입금 계좌','입금 계좌 예금주',2.2,0.0,'office1',
        0, 0, 0, 0, 0, 0,1,'admin','관리자',0.0,3,'agent1','agent1',1.0,4,'agent2','agent2',0.2,null,null,null,0.0
       ),
       ('agent1', '{noop}1111!', 'agent1','agent1','ACTIVE','AGENT',
        '정산 은행','정산 계좌','정산 계좌 예금주', '입금 은행','입금 계좌','입금 계좌 예금주',0.0,0.0,'agent1',
        0, 0, 0, 0, 0, 0,1,'admin','관리자',0.0,null,null,null,0.0,null,null,null,0.0,null,null,null,0.0
       ),
       ('agent2', '{noop}1111!', 'agent2','agent2','ACTIVE','AGENT',
        '정산 은행','정산 계좌','정산 계좌 예금주', '입금 은행','입금 계좌','입금 계좌 예금주',0.0,0.0,'agent2',
        0, 0, 0, 0, 0, 0,1,'admin','관리자',0.0,null,null,null,0.0,null,null,null,0.0,null,null,null,0.0
       ),
       ('office2', '{noop}1111!', 'office2','office2','ACTIVE','OFFICE',
        '정산 은행','정산 계좌','정산 계좌 예금주', '입금 은행','입금 계좌','입금 계좌 예금주',3.0,0.0,'office',
        0, 0, 0, 0, 0, 0,1,'admin','관리자',0.0,3,'agent1','agent1',1.0,4,'agent2','agent2',0.2,null,null,null,0.0
       )
;

insert into `user_roles` (`user_user_id`, `roles`)

values (1, 'ADMIN'),
       (1, 'USER'),
       (2, 'USER'),
       (3, 'USER'),
       (4, 'USER'),
       (5, 'USER')
;

insert into `basic_users` (
                           `username`,
                           `nick_name`,
                           `bank`,
                           `account`,
                           `user_status`,
                           `office_id`,
                           `office_username`,
                           `office_nick_name`,
                           `total_deposit_balance`
)

values
    ('test1', 'test1','test1','test1','ACTIVE',2,'office','office',123482842),
    ('test2', 'test2','test2','test2','ACTIVE',2,'office','office',4783264824),
    ('test3', 'test3','test3','test3','ACTIVE',2,'office','office',65829691),
    ('test4', 'test4','test4','test4','ACTIVE',2,'office','office',3478273592),
    ('test5', 'test5','test5','test5','ACTIVE',2,'office','office',23423524),
    ('test6', 'test6','test6','test6','ACTIVE',2,'office','office',1823455)
;