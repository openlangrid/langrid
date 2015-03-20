CREATE TABLE ${TABLENAME}
(
  "entityId" serial PRIMARY KEY,
  "ja" text,
  "en" text,
  "zh-CN" text,
  "ko" text,
  date timestamp DEFAULT now()
);
ALTER TABLE ${TABLENAME} OWNER TO ${DBUSER};
