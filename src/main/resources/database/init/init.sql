CREATE DATABASE IF NOT EXISTS maildb;


USE maildb;

ALTER DATABASE maildb DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

create table if not exists users
(
    id        int         not null auto_increment,
    username  varchar(64) not null,
    email     varchar(64) not null,
    firstname varchar(64) not null,
    lastname  varchar(64) not null,
    password  varchar(64) not null,
    user_role enum('USER','ADMIN'),
    confirmed boolean,
    locked    boolean,
    enabled   boolean,
    primary key (id)
);

create table if not exists mails
(
    id               int          not null auto_increment,
    sender_id        int          not null,
    recipient_id     int          not null,
    topic            varchar(64)  not null,
    body             varchar(2000) not null,
    important        boolean      not null,
    stared             boolean      not null,
    readed             boolean      not null,
    date_of_sending  timestamp    not null,
    primary key (id)
);
/*
create table if not exists drafts
(
    id               int          not null auto_increment,
    sender_id        int          not null,
    recipient_id     int          not null,
    topic            varchar(64)  not null,
    body             varchar(250) not null,
    save_date        timestamp    not null,
    primary key (id)
);
*/
create table if not exists persistent_logins
(
    username  varchar(64) not null,
    series    varchar(64) not null,
    token     varchar(64) not null,
    last_used timestamp   not null,
    primary key (series)
);
/*
create table if not exists files
(
    id        int  not null auto_increment,
    mail_id   int  not null,
    path      varchar(255),
    file_data BLOB not null,
    primary key (id)
);
*/
create table if not exists confirmation_token
(
    id                 int          not null auto_increment,
    confirmation_token varchar(200) not null,
    created_date       varchar(200) not null,
    user_id            int          not null,
    primary key (id)
);

ALTER TABLE maildb.mails CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE maildb.users CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE maildb.persistent_logins CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE maildb.confirmation_token CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
