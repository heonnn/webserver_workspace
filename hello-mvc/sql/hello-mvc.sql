--=================================================
-- 관리자 계정
--=================================================
-- web 계정 생성
alter session set "_oracle_script" = true; -- c##으로 시작하지 않는 일반계정생성 허용

create user web
identified by web
default tablespace users;

grant connect, resource to web;

alter user web quota unlimited on users;

--=================================================
-- web 계정
--=================================================
-- 회원테이블 생성
create table member (
    member_id varchar2(15),
    password varchar2(300) not null,
    member_name varchar2(50) not null,
    member_role char(1) default 'U' not null,
    gender char(1),
    birthday date,
    email varchar2(200),
    phone char(11) not null,
    address varchar2(200),
    hobby varchar2(200),
    enroll_date date default sysdate,
    constraint pk_member_id primary key(member_id),
    constraint ck_member_role check(member_role in ('U', 'A')),
    constraint ck_member_gender check(gender in ('M', 'F')),
    constraint uq_member_email unique(email)
);

--회원 추가
insert into member
values (
    'honggd', '1234', '홍길동', 'U', 'M', to_date('20000909','yyyymmdd'),
    'honggd@naver.com', '01012341234', '서울시 강남구', '운동,등산,독서', default
);

insert into member
values (
    'qwerty', '1234', '쿠어티', 'U', 'F', to_date('19900215','yyyymmdd'),
    'qwerty@naver.com', '01012341234', '서울시 송파구', '운동,등산', default
);

insert into member
values (
    'admin', '1234', '관리자', 'A', 'M', to_date('19801010','yyyymmdd'),
    'admin@naver.com', '01056785678', '서울시 관악구', '게임,독서', default
);

insert into member
values (
    'yoogs', '1234', '유관순', 'U', 'F', null,
    null, '01011112222', null, null, default
);

update member
set password = 'kvOffyqGmDjNUIXm8X/IIQm8+YzWKkfLw3njjegLvAITojzubkoT3myq4K3Yo5AnLW8Ig8J0Mgsf9g28/G3XUA=='
where member_id = 'admin';
select * from member;
commit;

-- 페이징쿼리
-- 1. rownum
-- 2. row_number
select *
from(
    select row_number() over(order by enroll_date desc) rnum, m.*
    from member m
    )
where
    rnum between 1 and 10;
    

-- select * from(select row_number() over(order by enroll_date desc) rnum, m.* from member m) where rnum between ? and ?;
    
/*
1page에 표시할 컨텐츠 : 10
-------------------------
1page : 1 ~ 10
2page : 11 ~ 20
3page : 21 ~ 30
...
11page : 101 ~ 110
12page : 111 ~ 120
*/

-- 게시판
create table board (
    no number,
    title varchar2(100) not null,
    member_id varchar2(20),
    content varchar2(400) not null,
    read_count number default 0,
    reg_date date default sysdate,
    constraint pk_board_no primary key(no),
    constraint fk_board_member_id foreign key(member_id) references member(member_id) on delete set null
);

create sequence seq_board_no;

create table attachment (
    no number,
    board_no number not null,
    original_filename varchar2(255) not null, -- 업로드한 파일명
    renamed_filename varchar2(255) not null, -- 저장된 파일명
    reg_date date default sysdate,
    constraint pk_attachment_no primary key(no),
    constraint fk_attachment_board_no foreign key(board_no) references board(no) on delete cascade
);

create sequence seq_attachment_no;

insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 1','honggd','반갑습니다',to_date('18/02/10','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 2','qwerty','안녕하세요',to_date('18/02/12','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 3','admin','반갑습니다',to_date('18/02/13','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 4','honggd','안녕하세요',to_date('18/02/14','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 5','qwerty','반갑습니다',to_date('18/02/15','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 6','admin','안녕하세요',to_date('18/02/16','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 7','honggd','반갑습니다',to_date('18/02/17','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 8','qwerty','안녕하세요',to_date('18/02/18','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 9','admin','반갑습니다',to_date('18/02/19','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 10','honggd','안녕하세',to_date('18/02/20','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 11','qwerty','반갑습니다',to_date('18/03/11','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 12','admin','안녕하세',to_date('18/03/12','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 13','honggd','반갑습니다',to_date('18/03/13','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 14','qwerty','안녕하세',to_date('18/03/14','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 15','admin','반갑습니다',to_date('18/03/15','RR/MM/DD'),0);


insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 16','honggd','안녕하세',to_date('18/03/16','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 17','qwerty','반갑습니다',to_date('18/03/17','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 18','admin','안녕하세',to_date('18/03/18','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 19','honggd','반갑습니다',to_date('18/03/19','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 20','qwerty','안녕하세',to_date('18/03/20','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 21','admin','반갑습니다',to_date('18/04/01','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 22','honggd','안녕하세',to_date('18/04/02','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 23','qwerty','반갑습니다',to_date('18/04/03','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 24','admin','안녕하세',to_date('18/04/04','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 25','honggd','반갑습니다',to_date('18/04/05','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 26','qwerty','안녕하세',to_date('18/04/06','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 27','admin','반갑습니다',to_date('18/04/07','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 28','honggd','안녕하세',to_date('18/04/08','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 29','qwerty','반갑습니다',to_date('18/04/09','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 30','admin','안녕하세',to_date('18/04/10','RR/MM/DD'),0);

insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 31','honggd','반갑습니다',to_date('18/04/16','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 32','qwerty','안녕하세',to_date('18/04/17','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 33','admin','반갑습니다',to_date('18/04/18','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 34','honggd','안녕하세',to_date('18/04/19','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 35','qwerty','반갑습니다',to_date('18/04/20','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 36','admin','안녕하세',to_date('18/05/01','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 37','honggd','반갑습니다',to_date('18/05/02','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 38','qwerty','안녕하세',to_date('18/05/03','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 39','admin','반갑습니다',to_date('18/05/04','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 40','honggd','안녕하세',to_date('18/05/05','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 41','qwerty','반갑습니다',to_date('18/05/06','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 42','admin','안녕하세',to_date('18/05/07','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 43','honggd','반갑습니다',to_date('18/05/08','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 44','qwerty','안녕하세',to_date('18/05/09','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 45','admin','반갑습니다',to_date('18/05/10','RR/MM/DD'),0);

insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 46','honggd','안녕하세',to_date('18/05/16','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 47','qwerty','반갑습니다',to_date('18/05/17','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 48','admin','안녕하세',to_date('18/05/18','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 49','honggd','반갑습니다',to_date('18/05/19','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 50','qwerty','안녕하세',to_date('18/05/20','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 51','admin','반갑습니다',to_date('18/05/01','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 52','honggd','안녕하세',to_date('18/06/02','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 53','qwerty','반갑습니다',to_date('18/06/03','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 54','admin','안녕하세',to_date('18/06/04','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 55','honggd','반갑습니다',to_date('18/06/05','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 56','qwerty','안녕하세',to_date('18/06/06','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 57','admin','반갑습니다',to_date('18/06/07','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 58','honggd','안녕하세',to_date('18/06/08','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 59','qwerty','반갑습니다',to_date('18/06/09','RR/MM/DD'),0);
insert into web.board (no,title,member_id,content,reg_date,read_count) values (seq_board_no.nextval,'안녕하세요, 게시판입니다 - 60','admin','안녕하세',to_date('18/06/10','RR/MM/DD'),0);

insert into web.attachment(no, board_no, original_filename, renamed_filename)
values(seq_attachment_no.nextval, 60, 'test.txt', '20211007_12345.txt');

commit;

select * from board order by reg_date desc;
select * from attachment order by reg_date desc;