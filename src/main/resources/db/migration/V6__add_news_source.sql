alter table if exists localized_news
    add column news_source_id int8;

create table news_source
(
    id   int8 not null,
    name varchar(255),
    primary key (id)
);

alter table if exists localized_news
    drop constraint if exists UK53k0bv733ygdrxdta5y82idvp;

alter table if exists localized_news
    add constraint UK53k0bv733ygdrxdta5y82idvp unique (news_id, language_id);

create sequence news_source_sequence start 1 increment 1;

alter table if exists localized_news
    add constraint FKt0jidq2tpkwkxaohcq8d4867s foreign key (news_source_id) references news_source;
