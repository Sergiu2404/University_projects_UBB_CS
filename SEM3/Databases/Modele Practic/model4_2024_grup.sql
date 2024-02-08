--model4/grup
--grile
--1) cinema victoria, casa de cultura => e)
--2) mcQueen, nuevoOrden, mephisto, safari, alcarras * 2, off compet
-- => a)
--3) d>i, sum(ifk1 + dfk2) * 2, set c6 = 100, fk1<=fk2
-- 1+1 *2 + 1+2 * 2 + 1+3 * 2 + 2+3 * 2 + 4+4 * 2 =
-- 4+6+8+10+16 = 44

--practic
--1)
create table Songs(
id int primary key identity(1,1),
name varchar(50),
duration int check(duration > 0 and duration < 20),
releaseDate date
);
insert into Songs values
('s1', 4, '2023-02-21'),
('s2', 3, '2023-05-25'),
('s3', 2, '2023-04-11'),
('s4', 4, '2023-01-02');


create table Artists(
id int primary key identity(1,1),
name varchar(50),
origin varchar(50),
birthDate date
);
insert into Artists values
('a1', 'o', '2000-01-02'),
('a2', '0', '2001-03-02'),
('a3', 'o', '1999-05-12'),
('a4', 'o', '2002-10-22');




create table SongsArtists(
songId int foreign key references Songs(id),
artistId int foreign key references  Artists(id),
isMainArtist bit default 1,
primary key (songId, artistId, isMainArtist)
);
insert into SongsArtists values
(1, 1, 1),
(1, 2, 0),
(2, 2, 1),
(2, 3, 0),
(3, 3, 0),
(4, 3, 1);


create table Accounts(
id int primary key identity(1,1),
username varchar(50) unique,
email varchar(50)
);
insert into Accounts values
('user1', 'email1'),
('user2', 'email2'),
('user3', 'email3'),
('user4', 'email4');

create table Playlists(
id int primary key identity(1,1),
name varchar(50),
);
insert into Playlists values
('p'),
('l'),
('q'),
('n');


create table PlaylistsSongs(
primary key (songId, playlistId),
songId int foreign key references Songs(id),
playlistId int foreign key references Playlists(id)
);
insert into PlaylistsSongs values
(1, 1),
(1, 2),
(1, 4),
(2, 1),
(3, 1),
(4, 3);


--2)

create or alter procedure addAccount @id int, @username varchar(50), @email varchar(50)
as
	declare @nr int;
	select @nr = count(*) from Accounts A
	where id = @id;

	if(@nr <> 0)
	begin
		update Accounts
		set username = @username, email = email
		where id = @id;
	end
	else begin
		insert into Accounts values (@username, @email);
	end

exec addAccount 5, 'usernew','email5'; 
select * from Accounts


--3)
create or alter view showPlaylists
as
	select P.name from Playlists P
	inner join PlaylistsSongs PS on P.id = PS.playlistId
	group by P.name
	having count(PS.songId) =( select top 1 songId from PlaylistsSongs)

select * from showPlaylists
--4)
create function nrOfArtists(@R int, @T int)
returns table
as
	return select SA.artistId from SongsArtists SA
	inner join PlaylistsSongs PS on PS.songId = SA.songId
	inner join Songs S on S.id = PS.songId
	inner join Playlists P on P.id = PS.playlistId
	group by artistId
	having count(S.duration) > @T and count(distinct P.id) > @R;

select * from nrOfArtists(1, 1);