alter table resourcemetaattribute add column targetgridname character varying(255);
update federation set targetgridname = tagetgridname;
