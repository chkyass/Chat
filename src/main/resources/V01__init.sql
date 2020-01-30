create table users(
	username varchar(50) not null primary key,
	password BINARY(60) not null,
	enabled boolean not null
);

create table authorities (
	username varchar(50) not null,
	authority varchar(50) not null,
	constraint fk_authorities_users foreign key(username) references users(username) ON UPDATE CASCADE ON DELETE CASCADE
);
create unique index ix_auth_username on authorities (username,authority);


create table rooms (
    id BIGINT not null primary key AUTO_INCREMENT,
    name varchar (50)
);

--create table messages (
--    room_id BIGINT not null,
--    username varchar(50) not null,
--    message varchar(50),
--    constraint fk_messages_rooms foreign key(room_id) references rooms(id) ON UPDATE CASCADE ON DELETE CASCADE
--);

--create table rooms_users (
--    username_id varchar(50),
--    room_id BIGINT,
--    constraint fk_username_join foreign key(username_id) references users(username) ON UPDATE CASCADE ON DELETE CASCADE,
--    constraint fk_room_join foreign key(room_id) references rooms(id) ON UPDATE CASCADE ON DELETE CASCADE
--);


INSERT INTO users (username, password, enabled)
    values ('admin',
            '$2y$12$15Qla9EKmiY4AiiiXgGZKuv/wPs4EQTs5tSbAUdWnNpWvFaxyHLBq',
            true);

INSERT INTO authorities (username, authority)
    values ('admin', 'ROLE_ADMIN');

