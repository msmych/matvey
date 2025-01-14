create table if not exists movies
(
    id             int           not null primary key,
    title          varchar(1023) not null,
    release_date   date          null,
    original_title varchar(1023) null,
    poster_path    varchar(1023) null,
    directors_ids  int[]         not null default '{}',
    created_at     timestamp     not null,
    updated_at     timestamp     not null default now()
);

create table if not exists directors
(
    id         int           not null primary key,
    name       varchar(1023) not null,
    created_at timestamp     not null,
    updated_at timestamp     not null default now()
);

create table if not exists accounts_movies
(
    account_id int       not null,
    movie_id   int       not null,
    to_watch   boolean   not null default false,
    watched    boolean   not null default false,
    created_at timestamp not null,
    updated_at timestamp not null default now(),
    primary key (account_id, movie_id)
);