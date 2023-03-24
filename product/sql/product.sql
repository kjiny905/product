drop table product;
create table product(
    pid         number(10),
    pname       varchar(30),
    quantity    number(10),
    price       number(10)
);
--기본키
alter table product add constraint product_pid_pk primary key(pid);

--시퀀스생성
drop sequence product_pid_seq;
create sequence product_pid_seq;

commit;