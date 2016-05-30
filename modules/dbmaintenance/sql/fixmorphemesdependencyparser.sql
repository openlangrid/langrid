begin transaction;
insert into servicetype select
       domainid, 'MorphemesDependencyParser', createddatetime, updateddatetime, description, 
       servicetypename
       from servicetype
       where servicetypeid='MophemesDependencyParser';
update serviceinterfacedefinition set servicetype_servicetypeid='MorphemesDependencyParser' where servicetype_servicetypeid='MophemesDependencyParser';
update servicetype_servicemetaattribute set servicetype_servicetypeid='MorphemesDependencyParser' where servicetype_servicetypeid='MophemesDependencyParser';
delete from servicetype where servicetypeid='MophemesDependencyParser';
commit transaction;
