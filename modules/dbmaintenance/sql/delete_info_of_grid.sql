begin transaction;
SET @gridId = 'kyoto1.langrid';
delete from accesslimit where servicegridid='@gridId' or usergridid='@gridId';
delete from accessright where servicegridid='@gridId' or usergridid='@gridId';
delete from accessstat where serviceandnodegridid='@gridId' or usergridid='@gridId';
rollback;
