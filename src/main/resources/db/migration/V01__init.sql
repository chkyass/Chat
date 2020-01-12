create table users(
	username varchar(50) not null primary key,
	password BINARY(60) not null,
	enabled boolean not null
);

create table authorities (
	username varchar(50) not null,
	authority varchar(50) not null,
	constraint fk_authorities_users foreign key(username) references users(username)
);

create unique index ix_auth_username on authorities (username,authority);

INSERT INTO users (username, password, enabled)
    values ('user',
        '$2y$12$.jQtuJRPLuttDteHmD9O3O25uXzALrODVM9TZsFOcILNxvBuf73j6',
        true);

INSERT INTO users (username, password, enabled)
    values ('admin',
            '$2y$12$.jQtuJRPLuttDteHmD9O3O25uXzALrODVM9TZsFOcILNxvBuf73j6',
            true);

INSERT INTO authorities (username, authority)
    values ('admin', 'ROLE_ADMIN');

INSERT INTO authorities (username, authority)
    values ('user', 'ROLE_USER');