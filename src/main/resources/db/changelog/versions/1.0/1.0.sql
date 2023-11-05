create table kcalc.users
(
    id         bigserial primary key,
    first_name text not null,
    last_name  text not null,
    email_id   text not null
);

