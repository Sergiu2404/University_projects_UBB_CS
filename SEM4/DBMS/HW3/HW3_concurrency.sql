
--- ROLLBACK

---Procedures
CREATE OR ALTER PROCEDURE addTransactT1 (@name VARCHAR(30), @value REAL) AS
    BEGIN TRAN
    BEGIN TRY
        EXEC addT1 @name, @value
        COMMIT TRAN
    END TRY
    BEGIN CATCH
        ROLLBACK TRAN
    END CATCH
GO

CREATE OR ALTER PROCEDURE addTransactT2 (@name VARCHAR(30), @value REAL) AS
    BEGIN TRAN
    BEGIN TRY
        EXEC addT2 @name, @value
        COMMIT TRAN
    END TRY
    BEGIN CATCH
        ROLLBACK TRAN
    END CATCH
GO

CREATE OR ALTER PROCEDURE addTransactT1mn2 (@id1 INT, @id2 INT) AS
    BEGIN TRAN
    BEGIN TRY
        EXEC addT1mn2 @id1, @id2
        COMMIT TRAN
    END TRY
    BEGIN CATCH
        ROLLBACK TRAN
    END CATCH
GO

CREATE OR ALTER PROCEDURE run1RollbackAddSuccess AS
    BEGIN TRAN
    BEGIN TRY
        -- autogenerate the names based on the ids that will be assigned
        DECLARE @id1 INT = 1 + (SELECT MAX(id) FROM [T1])
        DECLARE @id2 INT = 1 + (SELECT MAX(id) FROM [T2])
        IF @id1 IS NULL
            SET @id1 = 0
        IF @id2 iS NULl
            SET @id2 = 0
        DECLARE @name1 VARCHAR(30) = CONCAT('Name 1-', @id1)
        DECLARE @name2 VARCHAR(30) = CONCAT('Name 2-', @id2)

        EXEC addT1  @name1, 15
        EXEC addT2 @name2, 20
        SET @id1 = (SELECT MAX(id) FROM [T1])
        SET @id2 = (SELECT MAX(id) FROM [T2])
        EXEC addT1mn2 @id1, @id2
        COMMIT TRAN
    END TRY
    BEGIN CATCH
        ROLLBACK TRAN
        RETURN
    END CATCH
GO

CREATE OR ALTER PROCEDURE run1RollbackAddFailure AS
    BEGIN TRAN
    BEGIN TRY
        EXEC addT1 'Name 1-2', 15
        EXEC addT2 'Name 2-2', -1 -- fails bc. @value < 0
        EXEC addT1mn2 2, 2
        COMMIT TRAN
    END TRY
    BEGIN CATCH
        ROLLBACK TRAN
    END CATCH
GO

CREATE OR ALTER PROCEDURE run2RecoverAddSuccess AS
    -- autogenerate the names based on the ids that will be assigned
    DECLARE @id1 INT = 1 + (SELECT MAX(id) FROM [T1])
    DECLARE @id2 INT = 1 + (SELECT MAX(id) FROM [T2])
    IF @id1 IS NULL
        SET @id1 = 0
    IF @id2 iS NULl
        SET @id2 = 0
    DECLARE @name1 VARCHAR(30) = CONCAT('Name 1-', @id1)
    DECLARE @name2 VARCHAR(30) = CONCAT('Name 2-', @id2)

    EXEC addTransactT1 @name1, 25
    EXEC addTransactT2 @name2, 125
    SET @id1 = (SELECT MAX(id) FROM [T1])
    SET @id2 = (SELECT MAX(id) FROM [T2])
    EXEC addTransactT1mn2 @id1, @id2
GO

CREATE OR ALTER PROCEDURE run2RecoverAddFailure AS
    -- autogenerate the names based on the ids that will be assigned
    DECLARE @id1 INT = 1 + (SELECT MAX(id) FROM [T1])
    DECLARE @id2 INT = 1 + (SELECT MAX(id) FROM [T2])
    IF @id1 IS NULL
        SET @id1 = 0
    IF @id2 iS NULL
        SET @id2 = 0
    DECLARE @name1 VARCHAR(30) = CONCAT('Name 1-', @id1)
    DECLARE @name2 VARCHAR(30) = CONCAT('Name 2-', @id2)

    EXEC addTransactT1 @name1, 30
    EXEC addTransactT2 @name2, -30 -- fails, but the operation before is committed
    SET @id1 = (SELECT id FROM [T1] WHERE name = @name1)
    SET @id2 = (SELECT id FROM [T2] WHERE name = @name2)
    EXEC addTransactT1mn2 @id1, @id2 -- fails, because of the FK constraint (since @id2 won't be present if the prev. operation fails)
GO







--- run the rollback / recovery
-- 1 - rollback
EXEC run1RollbackAddSuccess
SELECT * FROM [LogsDML]
EXEC run1RollbackAddFailure
SELECT * FROM [LogsDML]


-- 2 - recover
EXEC run2RecoverAddSuccess
SELECT * FROM [LogsDML]
WAITFOR DELAY '00:00:03'
EXEC run2RecoverAddFailure
SELECT * FROM [LogsDML]





--- tables + validations
CREATE TABLE [T1] (
    [id] INT IDENTITY(1, 1) PRIMARY KEY,
    [name] VARCHAR(100),
    [value] real
)
DBCC CHECKIDENT ([T1], RESEED, 0)
GO


CREATE TABLE [T2] (
    [id] INT IDENTITY(1, 1) PRIMARY KEY,
    [name] VARCHAR(100),
    [value] real
)
DBCC CHECKIDENT ([T2], RESEED, 0)
GO

CREATE TABLE [T1mn2] (
    [id1] INT FOREIGN KEY REFERENCES [T1]([id]),
    [id2] INT FOREIGN KEY REFERENCES [T2]([id]),
    PRIMARY KEY (id1, id2)
)
GO

CREATE TABLE [LogsDML] (
    [id] INT IDENTITY(1, 1) PRIMARY KEY,
    [operation] VARCHAR(30),
    [table] VARCHAR(30),
    [executionDateTime] DATETIME
)
DBCC CHECKIDENT ([LogsDML], RESEED, 0)
GO

CREATE OR ALTER PROCEDURE addT1 (@name VARCHAR(30), @value REAL) AS
    EXEC validateT @name, @value
    INSERT INTO [T1] VALUES (@name, @value)
    INSERT INTO [LogsDML] VALUES ('add', 'T1', GETDATE())
GO

CREATE OR ALTER PROCEDURE addT2 (@name VARCHAR(30), @value REAL) AS
    EXEC validateT @name, @value
    INSERT INTO [T2] VALUES (@name, @value)
    INSERT INTO [LogsDML] VALUES ('add', 'T2', GETDATE())
GO

CREATE OR ALTER PROCEDURE addT1mn2 (@id1 INT, @id2 INT) AS
    INSERT INTO [T1mn2] VALUES (@id1, @id2) -- fails if FK constraint conflict
    INSERT INTO [LogsDML] VALUES ('add', 'T1mn2', GETDATE())
GO


CREATE OR ALTER PROCEDURE addTransactT1 (@name VARCHAR(30), @value REAL) AS
    BEGIN TRAN
    BEGIN TRY
        EXEC addT1 @name, @value
        COMMIT TRAN
    END TRY
    BEGIN CATCH
        ROLLBACK TRAN
    END CATCH
GO

CREATE OR ALTER PROCEDURE addTransactT2 (@name VARCHAR(30), @value REAL) AS
    BEGIN TRAN
    BEGIN TRY
        EXEC addT2 @name, @value
        COMMIT TRAN
    END TRY
    BEGIN CATCH
        ROLLBACK TRAN
    END CATCH
GO

CREATE OR ALTER PROCEDURE addTransactT1mn2 (@id1 INT, @id2 INT) AS
    BEGIN TRAN
    BEGIN TRY
        EXEC addT1mn2 @id1, @id2
        COMMIT TRAN
    END TRY
    BEGIN CATCH
        ROLLBACK TRAN
    END CATCH
GO

CREATE OR ALTER PROCEDURE validateName (@name VARCHAR(30)) AS
    IF @name IS NULL OR LEN(@name) < 2
        RAISERROR ('Invalid name', 14, 1)
GO

CREATE OR ALTER PROCEDURE validateValue (@value REAL) AS
    IF @value IS NULL OR @value < 0
        RAISERROR ('Invalid value', 14, 1)
GO


CREATE OR ALTER PROCEDURE validateT (@name VARCHAR(30), @value REAL) AS
    EXEC validateName @name
    EXEC validateValue @value
GO

CREATE OR ALTER PROCEDURE validateSportName (@sportName VARCHAR(50)) AS
BEGIN
    IF @sportName IS NULL OR LEN(@sportName) < 2
        RAISERROR ('Invalid sport name', 14, 1)
END
GO

CREATE OR ALTER PROCEDURE validateRating (@rating INT) AS
BEGIN
    IF @rating IS NULL OR @rating < 1 OR @rating > 5 -- Assuming rating ranges from 1 to 5
        RAISERROR ('Invalid rating', 14, 1)
END
GO

CREATE OR ALTER PROCEDURE validateTeamName (@teamName VARCHAR(50)) AS
BEGIN
    IF @teamName IS NULL OR LEN(@teamName) < 2
        RAISERROR ('Invalid team name', 14, 1)
END
GO

CREATE OR ALTER PROCEDURE validateNumberOfPlayers (@numberOfPlayers INT) AS
BEGIN
    IF @numberOfPlayers IS NULL OR @numberOfPlayers < 1
        RAISERROR ('Invalid number of players', 14, 1)
END
GO

CREATE OR ALTER PROCEDURE validateSportsTableEntry (@sportName VARCHAR(50), @rating INT) AS
BEGIN
    EXEC validateSportName @sportName
    EXEC validateRating @rating
END
GO

CREATE OR ALTER PROCEDURE validateTeamsTableEntry (@teamName VARCHAR(50), @numberOfPlayers INT) AS
BEGIN
    EXEC validateTeamName @teamName
    EXEC validateNumberOfPlayers @numberOfPlayers
END
GO

EXEC validateSportsTableEntry 'Football', -3;
EXEC validateTeamsTableEntry 'Barcelona', 11;




CREATE TABLE SportsTable(
sportId INT PRIMARY KEY,
sportName VARCHAR(50),
rating INT
);

select * from SportsTable;

INSERT INTO SportsTable(sportId, sportName, rating)
VALUES
(1, 'Basketball', 4),
(2, 'Voley', 5),
(3, 'Golf', 3),
(4, 'Ping Pong', 5),
(5, 'Chess', 2),
(6, 'Badminton', 5),
(7, 'Handball', 4),
(8, 'Tennis', 4),
(9, 'Squash', 5),
(10, 'Ballet', 3)
CREATE TABLE TeamsTable(
teamId INT PRIMARY KEY,
teamName VARCHAR(50),
sportId INT,
FOREIGN KEY (sportId) REFERENCES SportsTable(sportId),
numberOfPlayers INT,
teamLocation VARCHAR(50),
);

select * from TeamsTable;
INSERT INTO TeamsTable(teamId, teamName, sportId, numberOfPlayers, teamLocation)
VALUES
(1, 'CFR', 1, 13, 'Cluj-Napoca'),
(2, 'Rapid', 2, 5, 'Bucuresti'),
(3, 'Steaua', 3 ,10 , 'Bucuresti'),
(4, 'Dinamo', 4, 5, 'Bucuresti'),
(5, 'Craiova', 5, 6, 'Craiova'),
(6, 'Olimpia Satu Mare', 6, 10, 'Satu Mare'),
(7, 'UTA', 7, 11, 'Arad'),
(8, 'Viitorul', 8, 14, 'Constanta'),
(9, 'Farul', 9, 9, 'Constanta'),
(10, 'Poli Timisoara', 10, 8, 'Timisoara')

SELECT * FROM SportsTable INNER JOIN TeamsTable ON SportsTable.sportId = TeamsTable.sportId

CREATE TABLE CommentatorsTable(
commentatorId INT PRIMARY KEY,
commentatorName VARCHAR(50),
sportId INT,
FOREIGN KEY (sportId) REFERENCES SportsTable(sportId)
);

INSERT INTO CommentatorsTable VALUES
(1, 'ines', 1),
(2, 'sergiu', 1),
(3, 'vanessa', 2),
(4, 'maria', 3),
(5, 'iulia', 5);



create table Companies(
companyId int primary key,
companyName varchar(50),
companyBudget int
);
insert into Companies(companyId, companyName, companyBudget) values
('1', 'Bosch', '10000'),
('2', 'Audi', '4000'),
('3', 'Amazon', '2000000'),
('4', 'Google', '1500000'),
('5', 'Ayrton Trans', '30'),
('6', 'OMV Petrom', '3000');

select * from Companies;


create table Clients(
clientId int primary key,
clientName varchar(50),
companyId int,
FOREIGN KEY (companyId) REFERENCES Companies(companyId)
);
insert into Clients(clientId, clientName, companyId) values
('1', 'lamborghini', '1'),
('2', 'stellantis', '1'),
('3', 'sergiu', '2'),
('4', 'radu', '3'),
('5', 'alex', '4'),
('6', 'logistics', '5'),
('7', 'BVB', '6');

select * from Clients;



create table Employees(
employeeId int primary key,
employeeName varchar(50),
employeeAge int,
companyId int foreign key (companyId) references Companies(companyId)
);
insert into Employees(employeeId, employeeName, employeeAge, companyId) values
('1', 'sergiu', '21', '1'),
('2', 'emp2', '25', '2'),
('3', 'emp3', '31', '3'),
('4', 'emp4', '40', '3'),
('5', 'emp5', '23', '4'),
('6', 'emp6', '50', '5');

select * from Employees;





---deadlock t1
BEGIN TRAN
UPDATE Clients SET clientName='deadlock T1' WHERE clientId=2
WAITFOR DELAY '00:00:10'
UPDATE Companies SET companyBudget = 100 WHERE companyId = 4
COMMIT TRAN

---deadlock t2
BEGIN TRAN
UPDATE Companies SET companyBudget=77 WHERE companyId = 2
UPDATE Clients SET clientName='deadlock T2' WHERE clientId=2
WAITFOR DELAY '00:00:10'
COMMIT TRAN




---dirty read t1
BEGIN TRANSACTION
UPDATE Companies SET companyName='dirtyRead T1'
WHERE companyId = 2
WAITFOR DELAY '00:00:10'
ROLLBACK TRANSACTION

---dirty read t2
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
--SET TRANSACTION ISOLATION LEVEL READ COMMITTED --solution
BEGIN TRANSACTION
SELECT * FROM Companies
WAITFOR DELAY '00:00:15'
SELECT * FROM Companies
COMMIT TRAN




--- non repeatable read t1
INSERT INTO Companies
VALUES (100, 'non repeatable read t1', 777)
BEGIN TRAN
WAITFOR DELAY '00:00:05'
UPDATE Companies SET companyName='non repeatable read t1 updated'
WHERE companyId = 100
COMMIT TRAN

INSERT INTO Companies
VALUES (101, 'non repeatable read t1', 777)
BEGIN TRAN
WAITFOR DELAY '00:00:05'
UPDATE Companies SET companyName='non repeatable read t1 updated'
WHERE companyId = 101
COMMIT TRAN

--- non repeatable read t2
SET TRANSACTION ISOLATION LEVEL READ COMMITTED
--SET TRANSACTION ISOLATION LEVEL REPEATABLE READ -- solution
BEGIN TRAN
SELECT * FROM Companies
WAITFOR DELAY '00:00:05'
SELECT * FROM Companies
COMMIT TRAN



---phantom read t1
BEGIN TRAN
WAITFOR DELAY '00:00:04'
INSERT INTO Companies
VALUES (110, 'phantom read t1 company', 777)
COMMIT TRAN

BEGIN TRAN
WAITFOR DELAY '00:00:05'
INSERT INTO Companies
VALUES (111, 'phantom read t1 company', 777)
COMMIT TRAN

--- phantom read t2
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE -- solution
BEGIN TRAN
SELECT * FROM Companies
WAITFOR DELAY '00:00:05'
SELECT * FROM Companies
COMMIT TRAN




---update conflict t1
WAITFOR DELAY '00:00:05'
BEGIN TRAN
UPDATE Companies SET companyBudget = 777 WHERE companyId=5
WAITFOR DELAY '00:00:05'
COMMIT TRAN

SELECT * FROM Companies



---update conflict t2
WAITFOR DELAY '00:00:05'
BEGIN TRAN
UPDATE Companies SET companyBudget = 888 WHERE companyId=5
WAITFOR DELAY '00:00:05'
COMMIT TRAN