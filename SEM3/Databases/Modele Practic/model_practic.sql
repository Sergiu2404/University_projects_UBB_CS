--1
create table TrainTypes(
typeId int primary key identity(1,1),
description varchar(100)
);
insert into TrainTypes(description) values
('interRegio'),
('regio'),
('interCity');


create table Trains(
trainId int primary key identity(1,1),
trainName varchar(50),
typeId int foreign key references TrainTypes(typeId)
);
insert into Trains values
('train1',2),
('train2',1),
('train3',3);
select * from Trains union select * from TrainTypes


create table Stations(
stationId int primary key identity(1,1),
stationName varchar(50) unique
);
insert into Stations values
('arad'),
('lipova'),
('deva'),
('timisoara'),
('ortisoara'),
('vladimirescu'),
('paulis'),
('sambateni');


create table Routes(
routeId int primary key identity(1,1),
routeName varchar(50) unique,
trainId int foreign key references Trains(trainId)
);
insert into Routes values
('arad-lipova',1),
('arad-timisoara',2),
('arad-bucuresti',3),
('arad-iasi',3);


create table RoutesStations(
routeId int foreign key references Routes(routeId),
stationId int foreign key references Stations(stationId),
arrTime time,
depTime time,
constraint pk_routeStaions primary key (routeId, stationId)
);
insert into RoutesStations values
(1,1,'1:00:00','2:00:00'),
(1,2,'3:00:00','4:20:00'),
(1,6,'2:10:00','2:20:00'),
(2,1,'5:00:00','6:00:00'),
(3,1,'11:00:00','13:00:00');


--2
create or alter procedure addStationToRoute @routeId int, @stationId int, @arrTime time, @depTime time
as
begin
  declare @nr int;
  set @nr = 0;
  --select @nr = count(*) from RoutesStations where @routeId=routeId and stationId=@stationId
  --if @nr <> 0 
  --OR like below
  if exists (select routeId from RoutesStations where @stationId = stationId and routeId = @routeId)
  begin
    update RoutesStations
	set arrTime = @arrTime, depTime = @depTime
	where routeId = @routeId and stationId = @stationId
  end
  else
  begin
    insert into RoutesStations values(@routeId, @stationId, @arrTime, @depTime)
  end
end
exec addStationToRoute 1,8,'21:00:00', '21:50:00';
select * from RoutesStations;


--3
create or alter view showRouteWithAllStations
as
  select routeName from Routes r
  inner join RoutesStations rs on r.routeId=rs.routeId
  group by routeName
  having count(*) = (select count(*) from Stations); 
  --first count for the result of inner join, second for Stations table

select * from showRouteWithAllStations

insert into RoutesStations values
(1,3,'5:00:00','6:00:00'),
(1,4,'11:00:00','13:00:00'),
(1,5,'11:00:00','13:00:00');


--4
create or alter function StationsWithNRoutes(@n int)
returns table
as
return
  select s.stationId, stationName, count(stationName) as NoOfRoutes
  from Stations s inner join RoutesStations rs on s.stationId=rs.stationId
  group by s.stationId, stationName
  having count(stationName) >= @n
go

select * from StationsWithNRoutes(1);
select * from StationsWithNRoutes(2);