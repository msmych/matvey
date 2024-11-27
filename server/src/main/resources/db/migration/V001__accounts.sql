create table if not exists accounts
(
    id         uuid          not null default gen_random_uuid() primary key,
    username   varchar(255)  not null,
    state      varchar(31)   not null,
    tags       varchar(63)[] not null default '{}',
    pass_hash  varchar(255)  null,
    email      varchar(255)  null,
    created_at timestamp     not null,
    updated_at timestamp     not null default now()
);

create unique index if not exists accounts_username_idx on accounts (username) where state <> 'DELETED';
