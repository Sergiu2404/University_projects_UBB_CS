--model3/grup
--grile
--1) pink floyd, queen => e)
--2) 0 => a)
--3) 0 => d)

--database
create table Clients(
cId int primary key identity(1,1),
cName varchar(50)
);
drop table Clients
insert into Clients values
('c1'),
('c2'),
('c3'),
('c4'),
('c5'),
('c6');


create table Banks(
bId int primary key identity(1,1),
bName varchar(50)
);
insert into Banks values
('b1'),
('b2'),
('b3'),
('b4'),
('b5'),
('b6');


create table BanksClients(
primary key (cId, bId),
cId int foreign key references Clients(cId),
bId int foreign key references Banks(bId)
);
drop table BanksClients;
insert into BanksClients values
(1, 1),
(2, 1),
(3, 1),
(1, 2),
(4, 2),
(5, 2),
(6, 4);


create table BankingServices(
bsId int primary key identity(1,1),
name varchar(50),
bId int foreign key references Banks(bId)
);
insert into BankingServices values
('bs1', 1),
('bs2', 1),
('bs3', 2),
('bs4', 2),
('bs5', 3);

create table BankingServicesClients(
primary key(cId, bsId),
cId int foreign key references Clients(cId),
bsId int foreign key references BankingServices(bsId)
); 
drop table BankingServicesClients
insert into BankingServicesClients values
(1, 1),
(2, 2),
(1, 2),
(2, 3),
(1, 4),
(2, 4);


create table InvestingServices(
isId int primary key identity(1,1),
name varchar(50)
);
insert into InvestingServices values
('is1'),
('is2'),
('is3'),
('is4'),
('is5');

create table ClientsInvestingServices(
primary key(cId, isId),
cId int foreign key references Clients(cId),
isId int foreign key references InvestingServices(isId)
);
insert into ClientsInvestingServices values
(1, 1),
(1, 2),
(1,3),
(2,2),
(3,2);


--2)

create or alter procedure AddClientToServicesList @bankName VARCHAR(50), @clientName VARCHAR(50)
as
    declare @bankId int;
	declare @clientId int;

    -- Get bank_id for the provided bankName
    SELECT bId FROM Banks WHERE bName = @bankName;

    -- Get client_id for the provided clientName
    SELECT cId FROM Clients WHERE cName = @clientName;

    -- Check if the client chose banking services
    IF EXISTS (SELECT 1 FROM BankingServicesClients WHERE cId = clientId AND bsId IN (SELECT bsId FROM BankingServices WHERE bId = bankId))
    begin
        -- Add client_id to the banking services list
        INSERT INTO BankingServicesClients (cId, bsId)
        SELECT clientId, bsId
        FROM BankingServices
        WHERE bId = bankId;
    END

    -- Check if the client chose investing services
    IF EXISTS (SELECT 1 FROM ClientsInvestingServices WHERE cId = clientId AND isId IN (SELECT isId FROM InvestingServices))
    begin
        -- Add client_name to the investing services list
        INSERT INTO ClientsInvestingServices (cId, isId)
        SELECT clientId, isId
        FROM InvestingServices;
    END


--3)
create or alter view showClients
as
	select distinct C.cId, C.cName from Clients C
	inner join BankingServicesClients BSC
	on BSC.cId = C.cId
	inner join ClientsInvestingServices CIS
	on CIS.cId = BSC.cId

select * from showClients



--4)
create function listNames()
returns table
as
begin
	select C.cName from Clients C
	inner join BanksClients BC on C.cId = BC.cId
	inner join BankingServicesClients BCS on BC.cId = BCS.cId
	group by C.cName
	having count(distinct BC.bId) > 1;
end