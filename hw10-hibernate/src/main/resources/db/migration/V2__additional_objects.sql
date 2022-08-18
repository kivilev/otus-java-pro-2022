alter table client
    add column address_id bigint;

create table address
(
    id     bigserial not null,
    street varchar(255),
    primary key (id)
);

create table phone
(
    id        bigserial not null,
    phone     varchar(255),
    client_id bigint,
    primary key (id)
);

alter table if exists client
    add constraint client_address_id_fk foreign key (address_id) references address;
alter table if exists phone
    add constraint phone_client_id_fk foreign key (client_id) references client;
