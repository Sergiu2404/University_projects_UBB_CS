create database Flashscore;
use Flashscore;



---LAB1
create table Sports(
sportId int primary key not null,
sportName varchar(50) not null,
sportDescription varchar(250)
);

insert into Sports(sportId,sportName,sportDescription)
values
('1','soccer','...');
insert into Sports(sportId,sportName,sportDescription)
values
('2','bascketball','...');
insert into Sports(sportId,sportName,sportDescription)
values
('3','voleyball','...');
insert into Sports(sportId,sportName,sportDescription)
values
('4','cricket',null);

select * from Sports;




create table Teams(
teamId int primary key not null,
teamName varchar(50) not null,
teamHome varchar(250) not null,
sportId int,
foreign key (sportId) references Sports(sportId)
);

insert into Teams(teamId,teamName,teamHome,sportId)
values
('1','UTA Arad','Arad-Francisc Neuman','1'),
('2','Steaua Bucuresti','Bucuresti-Ghencea','1'),
('3','Barcelona','Barcelona-Camp Nou','1');
insert into Teams(teamId,teamName,teamHome,sportId)
values
('4','CSM Oradea','Sala Polivalenta Oradea','2'),
('5','UBT Cluj','BT Arena','2');
insert into Teams(teamId,teamName,teamHome,sportId)
values
('6','U Cluj','BT Arena','3'),
('7','Rapid','Bucuresti','3'), --already deleted 
('8','Steaua Bucuresti','Bucuresti','3');

select * from Teams;




create table Players(
playerId int primary key not null,
playerName varchar(50) not null,
playerBirthDate Date,
playerNationality varchar(50),
playerPosition varchar(250),
teamId int,
foreign key(teamId) references Teams(teamId)
);

insert into Players(playerId,playerName,playerbirthDate,playerNationality,playerPosition,teamId)
values
('1','Rares Pop','2005-05-20','romanian','wing','1'),
('2','Cristian Mihai','2004-09-15','romanian','midfileder','1'),
('3','Darius Olaru','1999-02-23','romanian','midfileder','2'),
('4','Florinel Coman','2000-06-12','romanian','striker','2'),
('5','Gavi','2002-07-25','spanish','midfileder','3'),
('6','Sergiu Goian','2003-04-24','romanian','center back','3');

select * from Players;




create table Matches(
matchId int primary key not null,
matchDateTime Date, --for storing timezone information
matchInformation varchar(250),
sportId int,
foreign key(sportId) references Sports(sportId)
); 


insert into Matches(matchId,matchDateTime,matchInformation,sportId)
values
('1','2023-10-10','Friendly match: UTA-Steaua','1'),
('2','2023-10-13','Friendly match: UTA-Barcelona','1'),
('3','2023-10-16','Friendly match: Barcelona-Steaua','1');

select * from Matches;




create table Championships(
championshipId int primary key not null,
championshipName varchar(50) not null,
startDate Date,
endDate Date,
sportId int,
foreign key(sportId) references Sports(sportId)
);

insert into Championships(championshipId,championshipName,startDate,endDate,sportId)
values
('1','Superliga 1','2023-07-28','2024-06-02','1'),
('2','Friendly International Cup','2023-10-10','2023-10-16','1');
insert into Championships(championshipId,championshipName,startDate,endDate,sportId)
values
('3','World Cup 2022', '2022-11-10','2022-12-18','1');
--delete from Championships where sportId='1';

select * from Championships;




create table ChampionshipTeams(
championshipId int not null,
teamId int,
primary key (championshipId,teamId),
foreign key (championshipId) references Championships(championshipId),
foreign key (teamId) references Teams(teamId)
);--table for adding multiple teams in one championship 

insert into ChampionshipTeams (championshipId,teamId) values
('1','1'),--superliga teams
('1','2'),
('2','1'),--friendly teams
('2','2'),
('2','3');

select * from ChampionshipTeams;




create table Scores(
scoreId int primary key not null,
matchId int,
homeTeamScore int not null,
awayTeamScore int not null,
foreign key(matchId) references Matches(matchId)
);
insert into Scores(scoreId,matchId,homeTeamScore,awayTeamScore)
values
('1','1','3','1'),
('2','2','2','2'),
('3','3','4','1');




create table SportsEvents(
eventId int primary key not null,
eventType varchar(50),
playerId int,
matchId int,
foreign key (playerId) references Players(playerId),
foreign key (matchId) references Matches(matchId)
);
insert into SportsEvents(eventId,eventType,playerId,matchId)
values
('1','hat-trick','1','1');




create table News(
newsId int primary key not null,
newsTitle varchar(255) not null,
newsDescription text,
publicationDate date
); 
drop table News
drop table Users



create table Comentary(
comentaryId int primary key not null,
matchId int,
comentaryText text,
foreign key (matchId) references Matches(matchId)
);
insert into Comentary(comentaryId,matchId,comentaryText)
values
('1','1','comentary1...');

insert into Comentary(comentaryId,matchId,comentaryText)
values
('1','10','comentary2...');
--matchId=10 does not exist as primary key, so it
--violates referential integrity constraints

insert into Comentary(comentaryId,matchId,comentaryText)
values
('2','2','comentary3...');

insert into Comentary(comentaryId,matchId)
values
('3','1');




create table Users(
userId int primary key not null,
username varchar(50)  not null,
email varchar(255) not null,
password varchar(100) not null,
newsId int,
foreign key (newsId) references News(newsId)
);




create table Odds(
oddId int primary key not null,
oddNumber float not null,
matchId int,
newsId int,
foreign key (matchId) references Matches(matchId),
foreign key (newsid) references News(newsId)
);
drop table Odds

--a player could play in multiple championships
CREATE TABLE PlayerChampionship (
    playerId INT,
    championshipId INT,
    PRIMARY KEY (playerId, championshipId),
    FOREIGN KEY (playerId) REFERENCES Players (playerId),
    FOREIGN KEY (championshipId) REFERENCES Championships (championshipId)
);
insert into PlayerChampionship(playerId,championshipId)
values
('1','1'), --player 1 in superliga
('1','3'), --player 1 in world cup
('2','1'), --player 2 in superliga
('2','2'); --player 2 in friendly international cup



create table SuperLiga(
teamId int,
eventId int,
foreign key (teamId) references Teams (teamId),
foreign key (eventId) references SportsEvents (eventId)
);









--update queries LAB 2
update Users
set username='sergiu'
where email like 'ab@yahoo.com';

update Comentary
set comentaryText='comentary for match UTA-Steaua'
where comentaryText IS NOT NULL AND matchId='1';

update Matches
set matchInformation='Friendly match: UTA-Steaua: already played'
where matchDateTime between '2023-10-08' and '2023-10-10';

update Players
set playerPosition='midfielder'
where playerId in ('1','2');

--delete queries
delete from Comentary
where comentaryText is null;

delete from Teams
where teamName='Rapid';


--select queries
--union/or
select playerName, teamName
from Players AS player
join Teams as team on player.teamId = team.teamId
where team.teamName = 'UTA Arad'
union
select player.playerName, team.teamName
from Players as player
join Teams as team on player.teamId = team.teamId
where team.teamName = 'Steaua Bucuresti'

select playerName
from Players
where playerId='1' or playerId='2';

--intersect/in
select C.teamId, T.teamName
from ChampionshipTeams C
join Teams T on C.teamId=T.teamId
where C.championshipId='1'
intersect
select C.teamId, T.teamName
from ChampionshipTeams C
join Teams T on C.teamId=T.teamId
where championshipId='2'

select teamName
from Teams
where teamId in ('1','2','3');

--except/not in
select * from Players
except
select * from Players
where playerId in('1','2','3','4');

select * from players
where playerId not in('1','2','3','4');

--4 JOIN queries
--inner for retrieving the matches and their corresponding teams in a championship
--many-to-many with 3 tables
select M.matchId, M.matchDateTime, T.teamName
from Matches as M
INNER JOIN ChampionshipTeams as CT on M.matchId = CT.championshipId
INNER JOIN Teams as T on CT.teamId = T.teamId;

--2 many to many relationships joined: query retrieves the name of players and championships where players are asociated with both PlayerChampionship and ChampionshipTeams
select P.playerName as PlayerName, C.championshipName as ChampionshipName
from PlayerChampionship PC
join Players P on PC.playerId = P.playerId
join Championships C on PC.championshipId = C.championshipId
join ChampionshipTeams CT on PC.championshipId = CT.championshipId
where CT.teamId = P.teamId;

--left
select T.teamName, P.playerName
from Teams as T
LEFT JOIN Players as P on T.teamId = P.teamId;
--get teams with their players, including teams with no players

--right join-get all players and their teams including players with no teams
select T.teamName, P.playerName
from Teams AS T
RIGHT JOIN Players as P on T.teamId = P.teamId;

--full join
select userId, userName, newsDescription from Users
full join News
on Users.newsId = News.newsId where Users.userId is not null;



--in and where + subquery
select playerName 
from Players where teamId in(
select teamId from ChampionshipTeams
where championshipId in(
select championshipId from Championships
)
);

select matchId, matchInformation from Matches
where matchid in(
select matchId from Scores
where homeTeamScore is not null and awayTeamScore is not null
);



--exist and from
select * from Players
where exists(
select playerName from Players
where playerName LIKE 'R%'
) and teamId = '1';

--and distinct
select distinct teamName from Teams
where exists(
select playerNationality from Players
where playerNationality = 'romanian'
);



--Group by/having/order by
select sportId, count(*) as teamCount
from Teams
group by sportId
having count(*) > 1;

select teamName from Teams order by teamName;

select teamId, count(playerId) as TotalPlayers
from Players
group by teamId
having count(playerId) <3;

select S.matchId, avg(S.homeTeamScore + S.awayTeamScore) as AverageGoals
from Scores S
group by S.matchId
having avg(S.homeTeamScore + S.awayTeamScore) > 2.5;

select teamId, MIN(playerBirthDate) AS MinBirthdate, MAX(playerBirthDate) AS MaxBirthdate
from Players
group by teamId
having MIN(playerBirthDate) <= '2000-01-01' AND MAX(playerBirthDate) >= '2000-01-01';


--all/any
select teamId,teamName from Teams
where teamId = any(
select teamId from Players
where playerId > 2
);

--Find teams with players whose maximum age is less than the maximum age of players in team '1'.
SELECT teamId, teamName
FROM Teams
WHERE teamId = ANY (
    SELECT teamId
    FROM Players
    GROUP BY teamId
    HAVING MAX(YEAR(GETDATE()) - YEAR(playerBirthDate)) > ALL (
        SELECT MAX(YEAR(GETDATE()) - YEAR(playerBirthDate))
        FROM Players
        WHERE teamId = '1'
        GROUP BY teamId
    )
);

--find teams with players whose age is greater than all players in team '1'.
SELECT teamId, teamName
FROM Teams
WHERE teamId NOT IN (
    SELECT teamId
    FROM Players
    WHERE YEAR(playerBirthDate) <= ALL (
        SELECT YEAR(playerBirthDate)
        FROM Players
        WHERE teamId = '1'
    )
);

--find teams with players whose average age is greater than the average age of players in team '1'.
SELECT teamId, teamName
FROM Teams
WHERE teamId = ALL (
    SELECT teamId
    FROM Players
    GROUP BY teamId
    HAVING AVG(DATEDIFF(YEAR, playerBirthDate, GETDATE())) > ALL (
        SELECT AVG(DATEDIFF(YEAR, playerBirthDate, GETDATE()))
        FROM Players
        WHERE teamId = '1'
        GROUP BY teamId
    )
);

--arith exp
select * from Players
where month(playerBirthDate) <> 5 and teamId >= 2;

select top 3 playerId, playerName from Players
order by playerBirthDate desc;










--LAB3
create procedure addColumn
as
alter table Championships
add exWinner varchar(100);
go
exec addColumn;
drop procedure addColumn;

create procedure removeColumn
as
alter table Championships
drop column exWinner;
go
exec removeColumn;
drop procedure removeColumn;


create procedure addPKSuperliga
as
alter table SuperLiga
add id int identity(1,1); --start val and inc val

alter table SuperLiga
add constraint PK_SuperLiga_id primary key (id);
go
exec addPKSuperliga;
drop procedure addPKSuperliga;


create procedure removePKSuperliga
as
alter table SuperLiga
drop constraint PK_SuperLiga_id;

alter table SuperLiga
drop column id;
go
exec removePKSuperliga;
drop procedure removePKSuperliga;



create procedure addCKUsers
as
alter table Users
add phoneNumber varchar(10);
go
exec addCKUsers;
drop procedure addCKUsers;

create procedure removeCKUsers
as
alter table Users
drop column phoneNumber;
go
exec removeCKUsers;
drop procedure removeCKUsers;



create procedure addFKSuperLiga
as
alter table SuperLiga
add playerId int;

alter table SuperLiga
add constraint FK_playerId
foreign key (playerId) references Players (playerId);
go
exec addFKSuperLiga;
drop procedure addFKSuperLiga;


create procedure removeFKSuperLiga
as
  alter table SuperLiga 
  drop constraint FK_playerId

  alter table SuperLiga
  drop column playerId;
go
exec removeFKSuperLiga;
drop procedure removeFKSuperLiga;



create procedure modifyType
as
  alter table Sports
  alter column sportDescription TEXT;
go
exec modifyType;
drop procedure modifyType;


create procedure modifyTypeRevert
as
  alter table Sports
  alter column sportDescription varchar(250);
go
exec modifyTypeRevert;
drop procedure modifyTypeRevert;



create procedure addDefConstraint 
as
  alter table Sports
  add constraint defSportId default 'id' --only for the fields that have NOT NULL
  for sportId;
go
exec addDefConstraint;
drop procedure addDefConstraint;


create procedure removeDefConstraint
as
  alter table Sports
  drop constraint defSportId;
go
exec removeDefConstraint;
drop procedure removeDefConstraint;


create procedure createTable
as
create table MatchesStatistics(
matchId int,
team1Id int,
team2Id int,
posTeam1 int,
posTeam2 int,
foreign key (matchId) references Matches(matchId),
foreign key (team1Id) references Teams(teamId),
foreign key (team2Id) references Teams(teamId),
);
go
drop table MatchesStatistics;
exec createTable;
drop procedure createTable;

create procedure dropTable
as
  drop table MatchesStatistics;
go
exec dropTable;
drop procedure dropTable;


create table VersionTable(
versionNr int primary key
);
drop table VersionTable;

insert into VersionTable values(1);

create table ProceduresTable(
oldVersion int,
newVersion int,
procedureName varchar(100),
primary key (oldVersion, newVersion)
);
drop table ProceduresTable;



insert into ProceduresTable values
(1,2, 'addCKUsers'),
(2,1, 'removeCKUsers'),
(2,3, 'addDefConstraint'),
(3,2, 'removeDefConstraint'),
(3,4, 'addFKSuperLiga'),
(4,3, 'removeFKSuperLiga'),
(4,5, 'addPKSuperliga'),
(5,4, 'removePKSuperliga'),
(5,6, 'createTable'),
(6,5, 'dropTable'),
(6,7, 'modifyType'),
(7,6, 'modifyTypeRevert'),
(7,8, 'addColumn'),
(8,7, 'removeColumn');


create procedure executeVersionTable(@newVersion int)
as
	declare @currentVersion int;
	declare @procedureName varchar(100);
	select @currentVersion = versionNr from VersionTable;
	if (@newVersion > (select max(newVersion) from ProceduresTable ) or @newVersion < 1)
		raiserror('procedure version not found', 10, 1);
	else
		begin
		if @newVersion = @currentVersion
			print('you are on the new version');
		else
			begin
			if @currentVersion > @newVersion
				begin
				while @currentVersion > @newVersion
					begin
					select @procedureName = procedureName from ProceduresTable
					where oldVersion = @currentVersion and newVersion = @currentVersion-1;
					exec(@procedureName);
					set @currentVersion = @currentVersion-1;
					end
				end

			if @currentVersion < @newVersion
				begin
				while @currentVersion < @newVersion
					begin
					select @procedureName = procedureName from ProceduresTable
					where oldVersion = @currentVersion and newVersion = @currentVersion+1;
					print('executing ' + @procedureName);
					exec(@procedureName);
					set @currentVersion = @currentVersion+1;
					end
				end

			update VersionTable set versionNr = @newVersion;
			end
		end;
go
exec executeVersionTable 7;

select * from VersionTable;
drop procedure executeVersionTable;













--Lab 4 TESTS
GO
CREATE OR ALTER PROCEDURE addTableToTables(@tableName VARCHAR(50))
AS
BEGIN
	IF @tableName IN (SELECT [Name] FROM [TABLES])
	BEGIN
		PRINT 'The table is already in the Tables'
		RETURN
	END
	IF @tableName NOT IN (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES)
	BEGIN
		PRINT 'The table is not present in the database'
		RETURN
	END
	INSERT INTO [Tables] ([Name])
	VALUES
	(@tableName)
END

GO

CREATE OR ALTER PROCEDURE addViewToViews(@viewName VARCHAR(50))
AS
BEGIN
	IF @viewName IN (SELECT [Name] FROM [Views])
	BEGIN
		PRINT 'The view is already in the views table'
		RETURN
	END
	IF @viewName NOT IN (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.VIEWS)
	BEGIN
		PRINT 'The view is not in the database'
		RETURN
	END
	INSERT INTO [Views] ([Name])
	VALUES
	(@viewName)
END

GO

CREATE OR ALTER PROCEDURE addTestToTests(@testName VARCHAR(50))
AS
BEGIN
	IF @testName IN (SELECT [Name] FROM [Tests])
	BEGIN
		PRINT 'the test is already in the table Tests'
		RETURN
	END
	INSERT INTO [Tests] ([Name])
	VALUES
	(@testName)
END
GO

CREATE OR ALTER PROCEDURE connectTableToTest(@tableName VARCHAR(50), @testName VARCHAR(50),@rows INT, @position INT) AS
BEGIN
	IF @tableName NOT IN(SELECT [Name] FROM [Tables])	
	BEGIN
		PRINT 'Table is not present in tables'
		RETURN
	END
	IF @testName NOT IN (SELECT [Name] FROM [Tests])
	BEGIN
		PRINT 'Test not present in Tests'
		RETURN
	END
	IF EXISTS(
			SELECT * FROM TestTables T1 
			JOIN Tests T2
			ON T1.TestID=T2.TestID
			WHERE T2.[Name]=@testName and Position=@position
	)
	BEGIN
		PRINT 'the position is the same as the previous positions'
		RETURN
	END
	INSERT INTO [TestTables] (TestID, TableID, NoOfRows, Position)
	VALUES(
	(SELECT [Tests].TestID FROM [Tests] WHERE [Name]=@testName),
	(SELECT [Tables].TableID FROM [Tables] WHERE [Name]=@tableName),
	@rows,
	@position
	)
END
GO

CREATE OR ALTER PROCEDURE connectViewToTest(@viewName VARCHAR(50),@testName VARCHAR(50)) 
AS
BEGIN
	IF @viewName NOT IN (SELECT [Name] FROM [Views])
	BEGIN
		PRINT 'the view is not in the views table'
		RETURN
	END
	IF @testName NOT IN (SELECT [Name] FROM [Tests])
	BEGIN
		PRINT 'the test in not in the tests table'
		RETURN
	END
	INSERT INTO [TestViews](TestID, ViewID)
	VALUES
	(
		(SELECT [Tests].TestID FROM [Tests] WHERE [Name]=@testName),
		(SELECT [Views].ViewID FROM [Views] WHERE [Name]=@viewName)
	)
END
GO

CREATE OR ALTER PROCEDURE deleteFromTable(@tableID INT)
AS
BEGIN
	IF @tableID NOT IN(SELECT [TableID] FROM [Tables])
	BEGIN
		PRINT 'table is not in tables'
		RETURN
	END
	DECLARE @tableName NVARCHAR(50)=(SELECT [Name] FROM [Tables] WHERE TableID=@tableID)
	PRINT 'deleting data from ' + @tableName
	DECLARE @query NVARCHAR(100)=N'DELETE FROM ' + @tableName
	PRINT @query
	EXEC sp_executesql @query
END
GO

CREATE OR ALTER PROCEDURE deleteFromAllTables(@testID INT)
AS
BEGIN
	IF @testID NOT IN (SELECT [TestID] FROM [Tests])
	BEGIN
		PRINT 'Test is not present in Tests'
		RETURN
	END
	PRINT 'deleting data from all tables for the test ' + CONVERT(VARCHAR,@testID)
	DECLARE @tableID INT
	DECLARE @position INT
	DECLARE allTableCursorDesc CURSOR LOCAL FOR
		SELECT T1.TableID, T1.Position
		FROM TestTables T1
		INNER JOIN Tests T2 ON T2.TestID=T1.TestID
		WHERE T2.TestID=@testID
		ORDER BY T1.Position DESC

	OPEN allTableCursorDesc
	FETCH allTableCursorDesc INTO @tableID, @position
	WHILE @@FETCH_STATUS=0
	BEGIN
		EXEC deleteFromTable @tableID
		FETCH NEXT FROM allTableCursorDesc INTO @tableID, @position
	END
	CLOSE allTableCursorDesc
	DEALLOCATE allTableCursorDesc
END
GO

CREATE OR ALTER PROCEDURE insertDataIntoTable (@testRunID INT, @testID INT, @tableID INT)
AS
BEGIN
	IF @testID NOT IN (SELECT [TestID] FROM [Tests])
	BEGIN
		PRINT 'Test not present in Tests'
		RETURN
	END
	IF @testRunID NOT IN (SELECT [TestRunID] FROM [TestRuns])
	BEGIN
		PRINT 'the test run is not present in TestRuns table'
		RETURN
	END
	IF @tableID NOT IN (SELECT [TableID] FROM [Tables])
	BEGIN
		PRINT 'table is not in tables table'
		RETURN
	END
	DECLARE @startTime DATETIME=SYSDATETIME()
	DECLARE @tableName VARCHAR(50)=(
		SELECT [Name] FROM [Tables]
		WHERE TableID=@tableID
	)
	PRINT 'inserting data into table '+@tableName
	DECLARE @numberOfRows INT=(
		SELECT [NoOfRows] FROM [TestTables]
		WHERE TestID=@testID AND TableID=@tableID
	)
	--EXEC generateRandomDataForTable @tableName, @numberOfRows
	DECLARE @endTime DATETIME=SYSDATETIME()
	INSERT INTO TestRunTables(TestRunID, TableID, StartAt, EndAt)
	VALUES
	(@testRunID,@tableID,@startTime,@endTime)
END
GO


CREATE OR ALTER PROCEDURE insertDataIntoAllTables (@testRunID INT, @testID INT) AS
BEGIN
	IF @testID NOT IN (SELECT [TestID] FROM [Tests])
	BEGIN
		PRINT 'Test not present in Tests.'
		RETURN
	END

	IF @testRunID NOT IN (SELECT [TestRunID] FROM [TestRuns])
	BEGIN
		PRINT 'Test run not present in TestRuns.'
		RETURN
	END

	PRINT 'Insert data in all the tables for the test ' + CONVERT(VARCHAR, @testID)
	DECLARE @tableID INT
	DECLARE @pos INT
	--cursor to iterate through the tables in ascending order of their position
	DECLARE allTableCursorAsc CURSOR LOCAL FOR
		SELECT T1.TableID, T1.Position
		FROM TestTables T1
		INNER JOIN Tests T2 ON T2.TestID = T1.TestID
		WHERE T1.TestID = @testID
		ORDER BY T1.Position ASC

	OPEN allTableCursorAsc
	FETCH allTableCursorAsc INTO @tableID, @pos
	WHILE @@FETCH_STATUS = 0
	BEGIN
		EXEC insertDataIntoTable @testRunID, @testID, @tableID
		FETCH NEXT FROM allTableCursorAsc INTO @tableID, @pos
	END
	CLOSE allTableCursorAsc
	DEALLOCATE allTableCursorAsc
END
GO

CREATE OR ALTER PROCEDURE selectDataFromView (@viewID INT, @testRunID INT) AS
BEGIN
	IF @viewID NOT IN (SELECT [ViewID] FROM [Views])
	BEGIN
		PRINT 'View not present in Views.'
		RETURN
	END

	IF @testRunID NOT IN (SELECT [TestRunID] FROM [TestRuns])
	BEGIN
		PRINT 'Test run not present in TestRuns.'
		RETURN
	END

	DECLARE @startTime DATETIME = SYSDATETIME()
	DECLARE @viewName VARCHAR(100) = (
		SELECT [Name]
		FROM [Views]
		WHERE ViewID = @viewID
	)
	PRINT 'Selecting data from the view ' + @viewName
	DECLARE @query NVARCHAR(150) = N'SELECT * FROM ' + @viewName
	EXEC sp_executesql @query

	DECLARE @endTime DATETIME = SYSDATETIME()
	INSERT INTO TestRunViews(TestRunID, ViewID, StartAt, EndAt)
	VALUES (@testRunID, @viewID, @startTime, @endTime)
END
GO

CREATE OR ALTER PROCEDURE selectAllViews (@testRunID INT, @testID INT) AS
BEGIN
	IF @testID NOT IN (SELECT [TestID] FROM [Tests])
	BEGIN
		PRINT 'Test not present in Tests.'
		RETURN
	END

	IF @testRunID NOT IN (SELECT [TestRunID] FROM [TestRuns])
	BEGIN
		PRINT 'Test run not present in TestRuns.'
		RETURN
	END

	PRINT 'Selecting data from all the views from the test ' + CONVERT(VARCHAR, @testID)
	DECLARE @viewID INT
	DECLARE selectAllViewsCursor CURSOR LOCAL FOR
		SELECT T1.ViewID FROM TestViews T1
		INNER JOIN Tests T2 ON T2.TestID = T1.TestID
		WHERE T1.TestID = @testID

	OPEN selectAllViewsCursor
	FETCH selectAllViewsCursor INTO @viewID
	WHILE @@FETCH_STATUS = 0 
	BEGIN
		EXEC selectDataFromView @viewID, @testRunID
		FETCH NEXT FROM selectAllViewsCursor INTO @viewID
	END
	CLOSE selectAllViewsCursor
	DEALLOCATE selectAllViewsCursor
END
GO

CREATE OR ALTER PROCEDURE runTest(@testID INT, @description VARCHAR(MAX)) AS
BEGIN
	IF @testID NOT IN 
	(SELECT [TestID] FROM [Tests])
	BEGIN
		PRINT 'the test is not present in tests'
	END
	PRINT 'Runnning test with id ' +CONVERT(VARCHAR,@testID)
	INSERT INTO TestRuns([Description])
	VALUES(@description)
	DECLARE @testRunID INT=(
		SELECT MAX(TestRunID)
		FROM TestRuns
		)
	DECLARE @startTime DATETIME = SYSDATETIME()
	EXEC insertDataIntoAllTables @testRunID, @testID
	EXEC selectAllViews @testRunID, @testID
	DECLARE @endTime DATETIME = SYSDATETIME()
	EXEC deleteFromAllTables @testID

	UPDATE [TestRuns] SET StartAt = @startTime, EndAt = @endTime
	DECLARE @timeDifference INT = DATEDIFF(SECOND, @startTime, @endTime)
	PRINT 'The test with id ' + CONVERT(VARCHAR, @testID) + ' took ' + CONVERT(VARCHAR, @timeDifference) + ' seconds.'
END
GO

CREATE OR ALTER PROCEDURE runAllTests AS
BEGIN
	DECLARE @testName VARCHAR(50)
	DECLARE @testID INT
	DECLARE @description VARCHAR(2000)
	DECLARE allTestsCursor CURSOR LOCAL FOR
		SELECT *
		FROM Tests

	OPEN allTestsCursor
	FETCH allTestsCursor INTO @testID, @testName
	WHILE @@FETCH_STATUS = 0
	BEGIN
		PRINT 'Running ' + @testName
		SET @description = 'Test results for test with the ID ' + CAST(@testID AS VARCHAR(2))
		EXEC runTest @testID, @description
		FETCH NEXT FROM allTestsCursor INTO @testID, @testName
	END
	CLOSE allTestsCursor
	DEALLOCATE allTestsCursor
END
GO



--News table: primary key + foreign key
--Teams table: primary key + foreign key
--Championships table: primary key + foreign key
--ChampionshipTeams table: multi-column primary key

--a view with select operating on one table
go
create or alter view getUTANews
as
  select N.newsId, N.newsTitle from News N where N.newsTitle like '%UTA%'

--a view with select operating on at least 2 tables
go  
create or alter view usersAndNewsReadByThem
as
  select U.userId, U.username as userName, N.newsTitle  
  from Users U
  left join News N
  on U.newsId = N.newsId

go
create or alter view soccerAndBascketballTeams
as
  select S.sportId, S.sportName, T.teamName, T.teamHome 
  from Sports S
  inner join Teams T
  on S.sportId = T.sportId
  where S.sportName = 'soccer' or sportName = 'bascketball'
 
--view with a select that has a group by on at least 2 tables
go
create or alter view groupTeamsByChampionships
as
  select T.teamId, T.teamName from Teams T
  inner join ChampionshipTeams CT
  on T.teamId = CT.teamId
  group by T.teamName, T.teamId

--test1: view with table 
insert into News(newsId,newsTitle,newsDescription,publicationDate)
values
('1','UTA Arad nearly champions','...','2023-10-17');
insert into News(newsId,newsTitle,newsDescription,publicationDate)
values
('2','Steaua ...','...','2023-10-19'),
('3','New transfer at UTA','...','2023-10-22');

insert into Users(userId,username,email,password,newsId) values
('1','abababab','ab@yahoo.com','abcd2003', 1);
insert into Users(userId,username,email,password,newsId) values
('2','bb+b','bb@yahoo.com','bbbb2003', 1),
('3','ccc','cc@yahoo.com','ccc2003', 2);
select * from News

exec addViewToViews 'getUTANews'
exec addTestToTests 'Test2'
exec addTableToTables 'News'
exec connectTableToTest 'News', 'Test2', 2, 1
exec connectViewToTest 'getUTANews', 'Test2'

--test2- usersAndNewsReadByThem
exec addViewToViews 'usersAndNewsReadByThem'
exec addTestToTests 'Test3'
exec connectTableToTest 'News', 'Test3', 2, 1
exec addTableToTables 'Users'
exec connectTableToTest 'Users', 'Test3', 2, 2
exec connectViewToTest 'usersAndNewsReadByThem', 'Test3'


exec runAllTests
SELECT *
FROM [Views]

SELECT *
FROM [Tables]

SELECT *
FROM [Tests]

SELECT *
FROM [TestTables]

SELECT *
FROM [TestViews]

SELECT *
FROM [TestRuns]

SELECT *
FROM [TestRunTables]

SELECT *
FROM [TestRunViews]




CREATE TABLE [Tables] (
	[TableID] [int] IDENTITY (1, 1) NOT NULL ,
	--ID column is set as an identity column 
	--starting at 1 and incrementing by 1. When you insert a new row into this table 
	--without specifying a value for the ID column,
	--SQL Server automatically assigns a unique, incremental value to that column.
	[Name] [nvarchar] (50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL 
) ON [PRIMARY]--the data for this table should be stored in the primary filegroup

GO
CREATE TABLE [TestRunTables] (
	[TestRunID] [int] NOT NULL ,
	[TableID] [int] NOT NULL ,
	[StartAt] [datetime] NOT NULL ,
	[EndAt] [datetime] NOT NULL 
) ON [PRIMARY]

GO
CREATE TABLE [TestRunViews] (
	[TestRunID] [int] NOT NULL ,
	[ViewID] [int] NOT NULL ,
	[StartAt] [datetime] NOT NULL ,
	[EndAt] [datetime] NOT NULL 
) ON [PRIMARY]

GO
CREATE TABLE [TestRuns] (
	[TestRunID] [int] IDENTITY (1, 1) NOT NULL ,
	[Description] [nvarchar] (2000) COLLATE SQL_Latin1_General_CP1_CI_AS NULL ,
	[StartAt] [datetime] NULL ,
	[EndAt] [datetime] NULL 
) ON [PRIMARY]

GO
CREATE TABLE [TestTables] (
	[TestID] [int] NOT NULL ,
	[TableID] [int] NOT NULL ,
	[NoOfRows] [int] NOT NULL ,
	[Position] [int] NOT NULL 
) ON [PRIMARY]

GO
CREATE TABLE [TestViews] (
	[TestID] [int] NOT NULL ,
	[ViewID] [int] NOT NULL 
) ON [PRIMARY]
GO

CREATE TABLE [Tests] (
	[TestID] [int] IDENTITY (1, 1) NOT NULL ,
	[Name] [nvarchar] (50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL 
) ON [PRIMARY]

GO
CREATE TABLE [Views] (
	[ViewID] [int] IDENTITY (1, 1) NOT NULL ,
	[Name] [nvarchar] (50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL 
) ON [PRIMARY]
GO

ALTER TABLE [Tables] WITH NOCHECK ADD 
	CONSTRAINT [PK_Tables] PRIMARY KEY  CLUSTERED 
	(
		[TableID]
	)  ON [PRIMARY] 
GO
ALTER TABLE [TestRunTables] WITH NOCHECK ADD 
	CONSTRAINT [PK_TestRunTables] PRIMARY KEY  CLUSTERED 
	(
		[TestRunID],
		[TableID]
	)  ON [PRIMARY] 
GO
ALTER TABLE [TestRunViews] WITH NOCHECK ADD 
	CONSTRAINT [PK_TestRunViews] PRIMARY KEY  CLUSTERED 
	(
		[TestRunID],
		[ViewID]
	)  ON [PRIMARY] 
GO
ALTER TABLE [TestRuns] WITH NOCHECK ADD 
	CONSTRAINT [PK_TestRuns] PRIMARY KEY  CLUSTERED 
	(
		[TestRunID]
	)  ON [PRIMARY] 
GO
ALTER TABLE [TestTables] WITH NOCHECK ADD 
	CONSTRAINT [PK_TestTables] PRIMARY KEY  CLUSTERED 
	(
		[TestID],
		[TableID]
	)  ON [PRIMARY] 
GO
ALTER TABLE [TestViews] WITH NOCHECK ADD 
	CONSTRAINT [PK_TestViews] PRIMARY KEY  CLUSTERED 
	(
		[TestID],
		[ViewID]
	)  ON [PRIMARY] 
GO
ALTER TABLE [Tests] WITH NOCHECK ADD 
	CONSTRAINT [PK_Tests] PRIMARY KEY  CLUSTERED 
	(
		[TestID]
	)  ON [PRIMARY] 
GO
ALTER TABLE [Views] WITH NOCHECK ADD 
	CONSTRAINT [PK_Views] PRIMARY KEY  CLUSTERED 
	(
		[ViewID]
	)  ON [PRIMARY] 
GO
ALTER TABLE [TestRunTables] ADD 
	CONSTRAINT [FK_TestRunTables_Tables] FOREIGN KEY 
	(
		[TableID]
	) REFERENCES [Tables] (
		[TableID]
	) ON DELETE CASCADE  ON UPDATE CASCADE ,
	CONSTRAINT [FK_TestRunTables_TestRuns] FOREIGN KEY 
	(
		[TestRunID]
	) REFERENCES [TestRuns] (
		[TestRunID]
	) ON DELETE CASCADE  ON UPDATE CASCADE 
GO
ALTER TABLE [TestRunViews] ADD 
	CONSTRAINT [FK_TestRunViews_TestRuns] FOREIGN KEY 
	(
		[TestRunID]
	) REFERENCES [TestRuns] (
		[TestRunID]
	) ON DELETE CASCADE  ON UPDATE CASCADE ,
	CONSTRAINT [FK_TestRunViews_Views] FOREIGN KEY 
	(
		[ViewID]
	) REFERENCES [Views] (
		[ViewID]
	) ON DELETE CASCADE  ON UPDATE CASCADE 
GO
ALTER TABLE [TestTables] ADD 
	CONSTRAINT [FK_TestTables_Tables] FOREIGN KEY 
	(
		[TableID]
	) REFERENCES [Tables] (
		[TableID]
	) ON DELETE CASCADE  ON UPDATE CASCADE ,
	CONSTRAINT [FK_TestTables_Tests] FOREIGN KEY 
	(
		[TestID]
	) REFERENCES [Tests] (
		[TestID]
	) ON DELETE CASCADE  ON UPDATE CASCADE 
GO
ALTER TABLE [TestViews] ADD 
	CONSTRAINT [FK_TestViews_Tests] FOREIGN KEY 
	(
		[TestID]
	) REFERENCES [Tests] (
		[TestID]
	),
	CONSTRAINT [FK_TestViews_Views] FOREIGN KEY 
	(
		[ViewID]
	) REFERENCES [Views] (
		[ViewID]
	)
GO
if exists (select * from dbo.sysobjects where id = object_id(N'[FK_TestRunTables_Tables]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [TestRunTables] DROP CONSTRAINT FK_TestRunTables_Tables

GO

if exists (select * from dbo.sysobjects where id = object_id(N'[FK_TestTables_Tables]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [TestTables] DROP CONSTRAINT FK_TestTables_Tables

GO

if exists (select * from dbo.sysobjects where id = object_id(N'[FK_TestRunTables_TestRuns]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [TestRunTables] DROP CONSTRAINT FK_TestRunTables_TestRuns

GO

if exists (select * from dbo.sysobjects where id = object_id(N'[FK_TestRunViews_TestRuns]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [TestRunViews] DROP CONSTRAINT FK_TestRunViews_TestRuns

GO

if exists (select * from dbo.sysobjects where id = object_id(N'[FK_TestTables_Tests]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [TestTables] DROP CONSTRAINT FK_TestTables_Tests

GO

if exists (select * from dbo.sysobjects where id = object_id(N'[FK_TestViews_Tests]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [TestViews] DROP CONSTRAINT FK_TestViews_Tests

GO

if exists (select * from dbo.sysobjects where id = object_id(N'[FK_TestRunViews_Views]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [TestRunViews] DROP CONSTRAINT FK_TestRunViews_Views

GO

if exists (select * from dbo.sysobjects where id = object_id(N'[FK_TestViews_Views]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [TestViews] DROP CONSTRAINT FK_TestViews_Views

GO

if exists (select * from dbo.sysobjects where id = object_id(N'[Tables]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [Tables]

GO

if exists (select * from dbo.sysobjects where id = object_id(N'[TestRunTables]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [TestRunTables]

GO

if exists (select * from dbo.sysobjects where id = object_id(N'[TestRunViews]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [TestRunViews]

GO

if exists (select * from dbo.sysobjects where id = object_id(N'[TestRuns]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [TestRuns]

GO

if exists (select * from dbo.sysobjects where id = object_id(N'[TestTables]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [TestTables]

GO

if exists (select * from dbo.sysobjects where id = object_id(N'[TestViews]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [TestViews]

GO

if exists (select * from dbo.sysobjects where id = object_id(N'[Tests]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [Tests]

GO

if exists (select * from dbo.sysobjects where id = object_id(N'[Views]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [Views]
















--LAB 5
--A
create table Ta(
  aid int primary key,
  a2 int unique,
  a3 int
)

create table Tb(
  bid int primary key,
  b2 int
)

create table Tc(
  cid int primary key,
  aid int foreign key references Ta(aid),
  bid int foreign key references Tb(bid)
)

delete from Tc

--clusterd index scan
select * from Ta

--clustered index seek
select * from Ta where aid >= 7

--non-clustered index scan
select a2 from Ta order by a2

--non-clusterd index seek
select a2 from Ta where a2 between 100 and 120
--select a2 from Ta where a2 between 1 and 2

--key lookup - non-clustered indexseek + key lookup - the data is found in a non-clustered index => 
select a3, a2 from Ta where a2 = 107

--B
select * from Tb where b2 = 1007


--non clustered index scan
drop index Tb_b2_index on Tb
create nonclustered index Tb_b2_index on Tb(b2)


--C
go
create or alter View View1 as
  select A.aid, B.bid, C.cid from Tc C
  inner join Ta A on A.aid = C.aid
  inner join Tb B on B.bid = C.bid
  where A.a3 < 112 and B.b2 >= 1000
  
go 
select * from View1 


insert into Ta(aid, a2, a3) 
values
(1, 100, 110),
(2, 101, 111),
(3, 102, 112),
(4, 103, 113),
(5, 104, 114),
(6, 105, 115),
(7, 106, 116),
(8, 107, 117)

insert into Tb(bid, b2) 
values
(1, 1000),
(2, 1001),
(3, 1002),
(4, 1003),
(5, 1004),
(6, 1005),
(7, 1006),
(8, 1007)
--aid and bid are primary keys and create clustered indexes

insert into Tc (cid, aid, bid)
values
(1000, 1, 1),
(1001, 2, 2),
(1002, 3, 3),
(1003, 4, 4),
(1004, 5, 5),
(1005, 6, 6),
(1006, 7, 7),
(1007, 8, 8)















--delete data
IF EXISTS (SELECT [name] FROM sys.objects 
            WHERE object_id = OBJECT_ID('RandIntBetween'))
BEGIN
   DROP FUNCTION RandIntBetween;
END
GO


-- generating random numbers
CREATE FUNCTION RandIntBetween(@lower INT, @upper INT, @rand FLOAT)
RETURNS INT
AS
BEGIN
  DECLARE @result INT;
  DECLARE @range INT = @upper - @lower + 1;
  SET @result = FLOOR(@rand * @range + @lower);
  RETURN @result;
END
GO

-- With this procedure I insert some random data into the table Ta
CREATE OR ALTER PROC insertDataIntoTa
@nrOfRows INT
AS
BEGIN
	DECLARE @aid INT
	DECLARE @a2 INT
	DECLARE @a3 INT
	SET @aid  = (SELECT MAX(aid) + 1 FROM Ta)
	if @aid is NULL
		SET @aid = 1
	SET @a2 = (SELECT MAX(a2) + 1 FROM Ta)
	if @a2 is NULL
		SET @a2 = 1
	WHILE @nrOfRows > 0
	BEGIN
		SET @a3 = [dbo].[RandIntBetween](1, 100, RAND())
		INSERT INTO Ta(aid, a2, a3) VALUES (@aid, @a2, @a3)
		SET @nrOfRows = @nrOfRows - 1
		SET @aid = @aid + 1
		SET @a2 = @a2 + 1
	END
END
GO

EXEC insertDataIntoTa 200