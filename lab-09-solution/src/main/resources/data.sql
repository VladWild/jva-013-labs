-- Companies
insert into company(id, name)
values (1001, 'Luxoft'),
       (1002, 'IBM'),
       (1003, 'Oracle');

--- Orders
insert into orders(id, amount, customer_id)
values (2001, 10.00, 1001),
       (2002, 15.10, 1001),
       (2003, 100.00, 1002),
       (2004, 203.50, 1002),
       (2005, 10.00, 1003),
       (2006, 100.00, 1003);

-- Users
insert into users(id, login, password)
values (3001, 'user1', '$2a$10$L8BTPKYxARZdsv8DRTR7KeH.Q2CwqkpFd0FVKmAY4xwKzrBJ3ILUu'),
       (3002, 'user2', '$2a$10$L8BTPKYxARZdsv8DRTR7KeH.Q2CwqkpFd0FVKmAY4xwKzrBJ3ILUu'),
       (3003, 'admin', '$2a$10$L8BTPKYxARZdsv8DRTR7KeH.Q2CwqkpFd0FVKmAY4xwKzrBJ3ILUu');

-- User roles
insert into user_roles(user_id, roles)
values (3001, 'user'),
       (3002, 'user'),
       (3002, 'manager'),
       (3003, 'admin');

-- User-Company
insert into user_company(user_id, companies_id)
values (3001, 1001),
       (3001, 1002),
       (3002, 1003),
       (3003, 1001),
       (3003, 1002),
       (3003, 1003);

-- ACLs

create table acl_sid
(
    id        bigserial    not null primary key,
    principal boolean      not null,
    sid       varchar(100) not null,
    constraint unique_uk_1 unique (sid, principal)
);

create table acl_class
(
    id            bigserial    not null primary key,
    class         varchar(100) not null,
    class_id_type varchar(100),
    constraint unique_uk_2 unique (class)
);

create table acl_object_identity
(
    id                 bigserial primary key,
    object_id_class    bigint      not null,
    object_id_identity varchar(36) not null,
    parent_object      bigint,
    owner_sid          bigint,
    entries_inheriting boolean     not null,
    constraint unique_uk_3 unique (object_id_class, object_id_identity),
    constraint foreign_fk_1 foreign key (parent_object) references acl_object_identity (id),
    constraint foreign_fk_2 foreign key (object_id_class) references acl_class (id),
    constraint foreign_fk_3 foreign key (owner_sid) references acl_sid (id)
);

create table acl_entry
(
    id                  bigserial primary key,
    acl_object_identity bigint  not null,
    ace_order           int     not null,
    sid                 bigint  not null,
    mask                integer not null,
    granting            boolean not null,
    audit_success       boolean not null,
    audit_failure       boolean not null,
    constraint unique_uk_4 unique (acl_object_identity, ace_order),
    constraint foreign_fk_4 foreign key (acl_object_identity) references acl_object_identity (id),
    constraint foreign_fk_5 foreign key (sid) references acl_sid (id)
);

ALTER TABLE acl_entry
    ADD FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity(id);

ALTER TABLE acl_entry
    ADD FOREIGN KEY (sid) REFERENCES acl_sid(id);

--
-- Constraints for table acl_object_identity
--
ALTER TABLE acl_object_identity
    ADD FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id);

ALTER TABLE acl_object_identity
    ADD FOREIGN KEY (object_id_class) REFERENCES acl_class (id);

ALTER TABLE acl_object_identity
    ADD FOREIGN KEY (owner_sid) REFERENCES acl_sid (id);

-- ACLs data

INSERT INTO acl_class (id, class)
VALUES (1, 'com.luxoft.spingsecurity.model.Company');

INSERT INTO acl_sid (id, principal, sid)
VALUES (-100, 1, 'user1'),
       (-200, 1, 'user2'),
       (-300, 0, 'ROLE_ADMIN');

INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
VALUES (1, 1, 1001, NULL, -300, 0),
       (2, 1, 1002, NULL, -300, 0),
       (3, 1, 1003, NULL, -300, 0);

INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
VALUES (1, 1, 1, -100, 1, 1, 1, 1),
       (2, 1, 3, -300, 1, 1, 1, 1),
       (3, 2, 1, -200, 1, 1, 1, 1),
       (4, 2, 2, -300, 1, 1, 1, 1),
       (5, 3, 1, -300, 1, 1, 1, 1);