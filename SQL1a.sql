create database Accounts;
use Accounts;
create table Signin(
	Fullname nvarchar(80) not null,
	Username varchar(30) primary key,
	Password varchar(50) not null,
	NumberPhone varchar(20) not null,
	Email varchar(100) not null
)
select*from Signin

create database Product;
use Product;
create table in4Product(
	ID varchar(10) primary key,
	nameSP nvarchar(200) not null,
	nhaSX nvarchar(200) not null,
	namSX int not null,
	loaiSP nvarchar(100) not null,
	xuatxu nvarchar(100) not null,
	gianhapkho int not null,
	giaban int not null,
	daban int not null,
	dangban int not null
)
select*from Information
update Information
set nhaSX = N'Công ty cổ phẩn Hải Âu'
where ID = 'KB111'

select ID,nameSP,nhaSX,namSX,loaiSP,xuatxu,gianhapkho,giaban,daban,dangban
from Information
order by  gianhapkho DESC

use Accounts;
select*from Signin;