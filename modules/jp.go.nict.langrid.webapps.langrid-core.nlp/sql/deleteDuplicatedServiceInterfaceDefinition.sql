-- !!! use this at your own risk !!!
-- This SQL deletes duplicated serviceinterfacedefinition rows remaining
--  the row that has lowest id number or has no duplicates.

delete from serviceinterfacedefinition s1
 where
  (select count(*)
  from serviceinterfacedefinition s2
  where s1.servicetype_domainid=s2.servicetype_domainid
   and s1.servicetype_servicetypeid=s2.servicetype_servicetypeid
   and s1.protocolid=s2.protocolid
  ) > 1
  and
  s1.id <> (select min(id) from serviceinterfacedefinition s3
  where s1.servicetype_domainid=s3.servicetype_domainid
   and s1.servicetype_servicetypeid=s3.servicetype_servicetypeid
   and s1.protocolid=s3.protocolid
  )
