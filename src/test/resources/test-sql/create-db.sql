create sequence localized_news_sequence start 1 increment 1;
create sequence news_sequence start 1 increment 1;

create table languages
(
    id   bigserial not null,
    code varchar(2),
    primary key (id)
);

create table news
(
    id bigserial not null,
    primary key (id)
);

create table localized_news
(
    id          bigserial not null,
    brief       varchar(500),
    content     varchar(2048),
    date        date,
    title       varchar(100),
    language_id int8,
    news_id     int8,
    primary key (id)
);

create table test_table (
    id bigserial not null
);

alter table if exists localized_news add constraint localized_news_news_id_language_id unique (news_id, language_id);
alter table if exists localized_news add constraint localized_language_id_fk foreign key (language_id) references languages;
alter table if exists localized_news add constraint localized_news_news_id_fk foreign key (news_id) references news;
