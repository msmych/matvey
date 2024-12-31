create table if not exists wishlist_items
(
    id         uuid          not null default gen_random_uuid() primary key,
    account_id uuid          not null,
    name       varchar(255)  not null,
    url        varchar(1023) null,
    created_at timestamp     not null,
    updated_at timestamp     not null default now()
);