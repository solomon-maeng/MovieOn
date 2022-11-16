create table user_roles
(
    user_id   bigint not null,
    role_name varchar(255)
) engine=InnoDB;

CREATE INDEX idx_user_id ON user_roles (user_id);
CREATE INDEX idx_role_name ON user_roles (role_name);
