create table users
(
    id         bigint       not null auto_increment,
    created_at datetime(6),
    deleted_at datetime(6),
    updated_at datetime(6),
    token      varchar(255) not null,
    verified   bit          not null,
    email      varchar(255) not null,
    password   varchar(255) not null,
    username   varchar(255) not null,
    user_key   varchar(255) not null,
    primary key (id)
) engine=InnoDB;

CREATE UNIQUE INDEX uk_idx_user_email ON users (email);
CREATE UNIQUE INDEX uk_idx_username ON users (username);
