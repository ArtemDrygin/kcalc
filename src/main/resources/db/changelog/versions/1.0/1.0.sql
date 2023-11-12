create table settings
(
    id bigserial primary key,
    user_id uuid not null,
    daily_calorie_goal smallint not null default 1800,
    created_at timestamp not null,
    updated_at timestamp not null
);

create table calorie_event
(
    id bigserial primary key,
    calorie smallint not null,
    settings_id bigserial references settings,
    created_at timestamp not null
);

create index idx_calorie_event_settings_id ON calorie_event(settings_id);

