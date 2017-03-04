CREATE INDEX accesslog_idx
  ON accesslog
  USING btree
  (datetime, usergridid, userid, serviceandnodegridid, serviceid);
