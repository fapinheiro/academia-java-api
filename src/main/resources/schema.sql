create table if not exists clients (
   client_id int primary key,
   name varchar(100) not null,
   nif varchar(9) not null,
   email varchar(100) not null,
   active char(1)
);

-- SEQUENCE
create sequence if not exists seq_clients start with 1 increment by 1 maxvalue 99999999;

-- CONSTRAINT
alter table clients add constraint clients_nif_unique unique(email);
