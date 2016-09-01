begin;
alter table resourcemetaattribute add column createddatetime timestamp without time zone;
alter table resourcemetaattribute add column updateddatetime timestamp without time zone;
update resourcemetaattribute set createddatetime=now(), updateddatetime=now();
commit;
