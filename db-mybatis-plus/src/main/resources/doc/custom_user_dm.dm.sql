create table "custom_user_dm"
(
    "id"    BIGINT,
    "name"  VARCHAR(30),
    "age"   INTEGER,
    "email" VARCHAR(50)
);

alter table custom_user_dm add primary key("id");
alter table custom_user_dm add column "id" identity(1,1);

-- 达梦插入需要先设置 IDENTITY_INSERT on
set IDENTITY_INSERT custom_user_dm on;
INSERT INTO custom_user_dm (id, "name", age, email)
VALUES (1, 'Jone', 18, 'test1@baomidou.com'),
       (2, 'Jack', 20, 'test2@baomidou.com'),
       (3, 'Tom', 28, 'test3@baomidou.com'),
       (4, 'Sandy', 21, 'test4@baomidou.com'),
       (5, 'Billie', 24, 'test5@baomidou.com');
set IDENTITY_INSERT custom_user_dm off;