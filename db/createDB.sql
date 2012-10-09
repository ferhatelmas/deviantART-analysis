
create table deviants (
 deviant_id int auto_increment not null,
 deviant_name varchar(512) not null,
 deviant_type tinyint not null,
 deviant_date datetime,
 primary key(deviant_id)
);

create table deviations (
 deviation_id int auto_increment not null,
 deviation_deviantart_id bigint not null,
 deviation_name varchar(512) not null,
 deviation_url varchar(512) not null,
 deviation_deviant_id int not null,
 deviation_date datetime not null,
 foreign key(deviation_deviant_id) references deviants(deviant_id) on delete no action on update no action,
 primary key(deviation_id)
);

create table favorites (
 deviant_id int not null,
 deviation_id int not null,
 favorite_date datetime not null,
 foreign key(deviant_id) references deviants(deviant_id) on delete no action on update no action,
 foreign key(deviation_id) references deviations(deviation_id) on delete no action on update no action,
 primary key(deviant_id, deviation_id)
);