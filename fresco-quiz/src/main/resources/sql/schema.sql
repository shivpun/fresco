CREATE TABLE Q (
Q VARCHAR(99999),
f_Id INTEGER,
create_date timestamp,
write_date timestamp
);

CREATE TABLE A (
A VARCHAR(99999),
f_Id Integer,
create_date timestamp,
write_date timestamp
);

CREATE TABLE QA(
fq_Id Integer,
fa_Id Integer,
s_Id Integer,
g_Id Integer,
score Integer,
total Integer,
minq Integer,
create_date timestamp,
write_date timestamp
);

CREATE TABLE QC(
fq_Id Integer,
fa_Id Integer,
create_date timestamp,
write_date timestamp
);


CREATE TABLE QW (
fq_Id Integer,
fa_Id Integer,
create_date timestamp,
write_date timestamp
);

CREATE TABLE AT(
fq_Id Integer,
fa_Id Integer,
score Integer,
total Integer,
minq Integer,
seq Integer,
create_date timestamp,
write_date timestamp
);

CREATE SEQUENCE FRESCO_AT_SEQ START 1;