--examen practic 1/grupul anului
--grile
--1) a, b
--2) b
--3) b

--creare baza de date
create database RugbyModel
use RugbyModel;
create table Cities(
cityId int primary key identity(1,1),
cityName varchar(50) unique
);
insert into Cities values('Arad'), ('Timisoara'), ('Cluj');

create table Stadiums(
stadiumId int primary key identity(1,1),
stadiumName varchar(50),
cityId int foreign key references Cities(cityId)
);
insert into Stadiums values
('stadium1', 1),
('stadium2', 2),
('stadium3', 3);

create table Players(
playerId int primary key identity(1,1),
playerName varchar(50),
birthDate date,
playerNationality varchar(50),
position varchar(50),
captain bit,
teamId int foreign key references Teams(teamId)
);
insert into Players values
('plaeyr1', '2000-01-12', 'ROM', 'p1', 1, 1),
('plaeyr2', '2001-01-15', 'ROM', 'p2', 0, 1),
('plaeyr3', '2003-01-13', 'UNG', 'p1', 1, 2),
('plaeyr4', '2002-01-17', 'SPA', 'p6', 1, 3);
create table Coaches(
coachId int primary key identity(1,1),
coachName varchar(50),
coachNationality varchar(50),
teamId int foreign key references Teams(teamId)
);
insert into Coaches values
('c1', 'ROM', 1),
('c2', 'ROM', 1),
('c3', 'UNG', 2);

create table Teams(
teamId int primary key identity(1,1),
country varchar(50) unique
);
insert into Teams values
('ROM'),
('UNG'),
('SPA');
insert into Teams values
('GER');

drop table Games
create table Games(
gameId int primary key identity(1,1),
gameDate date,
teamId1 int foreign key references Teams(teamId),
teamId2 int foreign key references Teams(teamId),
stadiumId int foreign key references Stadiums(stadiumId),
finalScore varchar(50),
winner int foreign key references Teams(teamId),
overtime bit
);
insert into Games values
('2023-02-19', 1, 2, 1, 'score', 1, 0),
('2023-02-21', 1, 3, 1, 'score2', 1, 1),
('2023-02-25', 2, 3, 3, 'score3', 3, 1);
delete from Games


drop table Cities

--2
create or alter procedure addGame @date date, @team1 varchar(50), @team2 varchar(50), @stadiumId int, @finalScore varchar(50), @winner int, @overtime bit
as
	declare @nr int;
	set @nr = 0;
	select @nr = count(*) from Games where gameDate=@date and teamId1 = @team1 and teamId2 = @team2
	
	if(@nr <> 0)
	begin
		update Games 
		set finalScore = @finalScore, winner = @winner, overtime = @overtime
		where gameDate=@date and teamId1 = @team1 and teamId2 = @team2
	end
	else
	begin
		insert into Games values(@date, @team1, @team2, @stadiumId, @finalScore, @winner, @overtime);
	end
go
exec addGame '2023-04-24', 1, 5, 3, '3-1', 1, 0;
select * from Games;


--3
create or alter view showStadiums as
	select stadiumName, count(G.stadiumId) from Games G
	inner join Stadiums S on S.stadiumId = G.stadiumId and overtime = 1;
SELECT * FROM showStadiums;




--4

drop function GetTeamsWonAllGamesWithScoreDifferenceGreaterThanR
CREATE FUNCTION GetTeamsWonAllGamesWithScoreDifferenceGreaterThanR
(
    @StadiumName VARCHAR(50),
    @ScoreDifferenceThreshold INT
)
RETURNS INT
AS
BEGIN
    DECLARE @NumTeamsWonAllGames1 INT;

	DECLARE @NumTeamsWonAllGames2 INT;

    SELECT @NumTeamsWonAllGames2 = COUNT(DISTINCT teamId2) 
    FROM Games G
    WHERE G.stadiumId = (SELECT stadiumId FROM Stadiums WHERE stadiumName = @StadiumName)
      AND ABS(CAST(SUBSTRING(G.finalScore, 1, CHARINDEX('-', G.finalScore) - 1) AS INT) -
              CAST(SUBSTRING(G.finalScore, CHARINDEX('-', G.finalScore) + 1, LEN(G.finalScore)) AS INT)) > @ScoreDifferenceThreshold
      AND G.winner = G.teamId2; -- Use teamId1 as one of the teams involved in the game

    SELECT @NumTeamsWonAllGames1 = COUNT(DISTINCT teamId1) 
    FROM Games G
    WHERE G.stadiumId = (SELECT stadiumId FROM Stadiums WHERE stadiumName = @StadiumName)
      AND ABS(CAST(SUBSTRING(G.finalScore, 1, CHARINDEX('-', G.finalScore) - 1) AS INT) -
              CAST(SUBSTRING(G.finalScore, CHARINDEX('-', G.finalScore) + 1, LEN(G.finalScore)) AS INT)) > @ScoreDifferenceThreshold
      AND G.winner = G.teamId1; -- Use teamId1 as one of the teams involved in the game

    RETURN @NumTeamsWonAllGames1 + @NumTeamsWonAllGames2;
END;
GO

DECLARE @Result INT;
SET @Result = dbo.GetTeamsWonAllGamesWithScoreDifferenceGreaterThanR('stadium1', 0);
SELECT @Result;
