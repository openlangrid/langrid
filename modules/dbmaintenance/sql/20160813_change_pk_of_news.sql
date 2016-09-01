begin;
alter table news drop constraint news_pkey;
alter table news add primary key(gridid, id);
commit;
