# TP2: Gestionnaire de Persistance

** Fait sur dmi gitlab. Nouveau repo sur github (https://dmigit.uqtr.ca/simarfr/INF1035-Gestionnaire-Persistance) <br>
Auteurs: Frédérick Simard, Pascal Godin et David Mongeau <br>
Un gestionnaire de persistance permettant d’aller chercher des données dans une base de données avec des beans. <br>
Travail pratique #2 du cours INF1035

## dev
```
docker-compose up -d pg_persistance
```

## restore
```
cat dump.sql | docker exec -i pg_persistance psql -U dev
```

## reset
```
docker-compose stop pg_persistance;
docker-compose rm -f pg_persistance;
docker-compose up -d pg_persistance;
```

## dump 
```
docker exec -t pg_persistance pg_dumpall -c -U dev > dump.sql
```

## terminal
```
docker exec -it pg_persistance psql -U dev -W dev
\q: quit
\dt+: show tables
```

## migrations
```
CREATE TABLE public.students(
id SERIAL UNIQUE, 
fname character varying(255), 
lname character varying(255),
age integer
);

CREATE TABLE public.courses(
id SERIAL UNIQUE, 
name character varying(255), 
sigle character varying(255), 
description text
);

CREATE TABLE public.registrations(
id SERIAL UNIQUE, 
student_id integer,
course_id integer,
date timestamp without time zone
);

ALTER TABLE ONLY public.registrations
    ADD CONSTRAINT "fk_student" FOREIGN KEY (student_id) REFERENCES public.students(id);

ALTER TABLE ONLY public.registrations
    ADD CONSTRAINT "fk_course" FOREIGN KEY (course_id) REFERENCES public.courses(id);
```

### data
```
INSERT INTO students(fname, lname, age) VALUES ('Steve', 'Gagnon', 21);
```