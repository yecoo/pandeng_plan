create table day_top_videos (
	day varchar(10) not null,
	videos_id bigint(10) not null,
	times int(10) not null,
	primary key (day,videos_id)
);


create table area_top_videos (
	day varchar(10) not null,
	num int(10) not null,
	area varchar(20) not null,
	times bigint(10) not null,
	times_rank int(10) not null,
	primary key (day,num,area)
);


create table flow_top_videos (
	day varchar(10) not null,
	num bigint(10) not null,
	flow bigint(10) not null,
	primary key (day,num)
);
