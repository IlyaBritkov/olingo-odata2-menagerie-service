create table if not exists owner
(
    id         bigint primary key GENERATED ALWAYS AS IDENTITY,
    first_name varchar(35),
    last_name  varchar(40),
    age        int,
    is_alive   boolean NOT NULL
    )
;

create table if not exists pet
(
    id         bigint primary key GENERATED ALWAYS AS IDENTITY,
    breed      varchar(45),
    name       varchar(35),
    age        int,
    weight     double precision,
    is_alive   boolean NOT NULL,
    is_healthy boolean,
    owner_id   bigint,
    CONSTRAINT owner_id_fk FOREIGN KEY (owner_id) REFERENCES owner (id)
    )
;

create table if not exists cat
(
    id           bigint primary key,
    is_castrated boolean not null,
    constraint pet_id_fk foreign key (id) references pet (id)
    )
;

create table if not exists dog
(
    id          bigint primary key,
    nose_is_dry boolean not null,
    constraint pet_id_fk foreign key (id) references pet (id)
    )
;