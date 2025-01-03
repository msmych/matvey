create table if not exists titles
(
    id         uuid          not null default gen_random_uuid() primary key,
    type       varchar(63)   not null,
    title      varchar(1023) not null,
    created_at timestamp     not null,
    updated_at timestamp     not null default now()
);