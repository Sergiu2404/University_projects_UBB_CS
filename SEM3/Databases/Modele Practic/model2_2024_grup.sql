--model 2/grup
--grile
--1) jane austen, fitz, gabriel, agatha => none: e)
--2) 2 records => b)
--3) 123+133 + 100+110 + 55+65 + 100+110 + 89+99 + 120+130 =
-- = 1234
create table R(
fk1 int,
fk2 int,
c1 varchar(100),
c2 varchar(100),
c3 int,
c4 float,
c5 float
); drop table R
insert into R values
(9,6,'pride and prejudice', 'jane austen', 123, 43.99, 10),
(8,8,'the beautiful and dammed', 'f. scott fitzgerald', 100, 52.99, 9.8),
(7,9,'the wind-up bird', 'haruki murakami', 99, 39.99, 8.4),
(12,18,'emma', 'jane austen', 101, 40.99, 8),
(6,5,'love in the time of cholera', 'gabriel garcia marquez', 55, 53.99, 9.7),
(2,4, 'death on the nile', 'agatha cristie', 80, 49.99, 9.5),
(5,3, 'one hundred years', 'gabriel garcia marquez', 100, 50.99, 9.4),
(3,7,'sense', 'jane austen', 85, 43.99, 8.4),
(2,9,'the great Galsby', 'f. scott fitzgerald', 95, 32.99, 8.9),
(1,8,'kalka on the shore', 'haruki murakami', 123, 42.99, 7.9),
(6,4,'murder on the orient express', 'agatha cristie', 89, 29.99, 8.2),
(5,2,'the autumn of the patriarch', 'gabriel garcia marquez', 120, 45.99, 8.9);

select c2, avg(c5) as avgc5, max(c5) maxc5 
from R where c1 not like '%the' 
group by c2 
having avg(c5)>=8.6

 select * from (select fk1, fk2, c4+c3 c4sumc3 
 from R where fk1 < fk2)r1 
 inner join 
 (select fk1, fk2, c5 
 from R 
 where c5>8.4 and c5<9.8)r2 
 on r1.fk1 = r2.fk1 and r1.fk2 = r2.fk2

 create or alter trigger trOnUpdate
 on R for update
 as
	declare @total int = 0;
	select @total = sum(i.c3 + d.c3)
	from deleted d
	inner join inserted i
	on d.fk1 = i.fk1 and d.fk2 = i.fk2
	where d.c3 < i.c3
	print @total

update R set c3 = c3 + 10
where fk1 >= fk2



--database
create table Patients(
patientId int primary key identity(1,1),
patientName varchar(50),
birthDate date,
gender varchar(50)
);
insert into Patients values
('p1', '2000-02-21', 'male'),
('p2', '2002-01-24', 'female'),
('p3', '2005-05-29', 'female'),
('p4', '1999-11-23', 'male');


create table Doctors(
doctorId int primary key identity(1,1),
doctorName varchar(50),
department varchar(50)
);
insert into Doctors values
('d1', 'dep1'),
('d2', 'dep1'),
('d3', 'dep1'),
('d4', 'dep2'),
('d5', 'dep2')


create table Medications(
medicationId int primary key identity(1,1),
medicationName varchar(50) unique,
manufacturer varchar(50)
);
insert into Medications values
('med1', 'manufact1'),
('med2', 'manufact1'),
('med3', 'manufact2'),
('med4', 'manufact2')

create table Appointments(
appointmentId int primary key identity(1,1),
patientId int,
doctorId int,
appointmentDate date,
foreign key (patientId) references Patients(patientId),
foreign key (doctorId) references Doctors(doctorId),
);
drop table Appointments
insert into Appointments values
('1', '1', '2024-01-23'),
('2', '1', '2024-02-21'),
('3', '2', '2023-01-12'),
('1', '2', '2023-09-29');

create table Prescriptions(
prescriptionId int primary key identity(1,1),
appointmentId int foreign key references Appointments(appointmentId),
medicationId int foreign key references Medications(medicationId),
dosage float
);
insert into Prescriptions values
('1', '1', 12.10),
('2', '2', 15),
('1', '2', 22),
('3', '3', 21)

--2)
create or alter procedure addApp @patientId int, @doctorId int, @date date 
as
	declare @nr int;
	set @nr = 0;
	select @nr = count(*) from Appointments A where A.patientId = @patientId and A.doctorId = @doctorId

	if(@nr <> 0)
	begin
		update Appointments
		set appointmentDate = @date
		where patientId = @patientId and doctorId = @doctorId;
	end
	else begin
		insert into Appointments values (@patientId, @doctorId, @date);
	end

exec addApp '1', '2', '2024-02-21'
select * from Appointments


--3)
create or alter view showDoctors
as
	select distinct D.doctorName from Doctors D
	inner join
	Appointments A on D.doctorId = A.doctorId
	where month(A.appointmentDate) = 2 and year(A.appointmentDate) = 2024;

select * from showDoctors;


--4)
create function nrOfMedicationsPrescribedByDoctorD(@doctorId int)
returns table
as
return
	select M.medicationName, count(*) as number from Medications M
	inner join Prescriptions P on P.medicationId = M.medicationId
	inner join Appointments A on A.appointmentId = P.appointmentId
	inner join Doctors D on D.doctorId = A.doctorId
	where D.doctorId = '1'
	group by M.medicationName;

select * from nrOfMedicationsPrescribedByDoctorD(1);