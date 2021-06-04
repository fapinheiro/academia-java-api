create table if not exists customers (
   id int primary key auto_increment,
   name varchar(100) not null,
   nif varchar(9) not null,
   email varchar(100) not null,
   active char(1)
);

-- SEQUENCE
create sequence if not exists seq_customers start with 1 increment by 50 maxvalue 99999999;

-- CONSTRAINT
alter table customers add constraint clients_nif_unique unique(nif);
