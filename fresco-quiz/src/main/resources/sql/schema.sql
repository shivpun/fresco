CREATE TABLE Q (
Q VARCHAR(99999),
f_Id INTEGER
);

CREATE TABLE A (
A VARCHAR(99999),
f_Id Integer
);

CREATE TABLE QA(
fq_Id Integer,
fa_Id Integer,
s_Id Integer,
g_Id Integer,
score Integer,
total Integer,
minq Integer
);

CREATE TABLE QC(
fq_Id Integer,
fa_Id Integer
);


CREATE TABLE QW (
fq_Id Integer,
fa_Id Integer
);

CREATE TABLE AT(
fq_Id Integer,
fa_Id Integer,
score Integer,
total Integer,
minq Integer,
seq Integer
);

CREATE SEQUENCE FRESCO_AT_SEQ START 1;