--
-- PostgreSQL database cluster dump
--

SET default_transaction_read_only = off;

SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;

--
-- Drop databases
--

DROP DATABASE dev;




--
-- Drop roles
--

DROP ROLE dev;


--
-- Roles
--

CREATE ROLE dev;
ALTER ROLE dev WITH SUPERUSER INHERIT CREATEROLE CREATEDB LOGIN REPLICATION BYPASSRLS PASSWORD 'md5c43812121e594f158520698ba706118f';






--
-- Database creation
--

CREATE DATABASE dev WITH TEMPLATE = template0 OWNER = dev;
REVOKE CONNECT,TEMPORARY ON DATABASE template1 FROM PUBLIC;
GRANT CONNECT ON DATABASE template1 TO PUBLIC;


\connect dev

SET default_transaction_read_only = off;

--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.20
-- Dumped by pg_dump version 9.6.20

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: courses; Type: TABLE; Schema: public; Owner: dev
--

CREATE TABLE public.courses (
    id integer NOT NULL,
    name character varying(255),
    sigle character varying(255),
    description text
);


ALTER TABLE public.courses OWNER TO dev;

--
-- Name: courses_id_seq; Type: SEQUENCE; Schema: public; Owner: dev
--

CREATE SEQUENCE public.courses_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.courses_id_seq OWNER TO dev;

--
-- Name: courses_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dev
--

ALTER SEQUENCE public.courses_id_seq OWNED BY public.courses.id;


--
-- Name: registrations; Type: TABLE; Schema: public; Owner: dev
--

CREATE TABLE public.registrations (
    id integer NOT NULL,
    student_id integer,
    course_id integer,
    date timestamp without time zone
);


ALTER TABLE public.registrations OWNER TO dev;

--
-- Name: registrations_id_seq; Type: SEQUENCE; Schema: public; Owner: dev
--

CREATE SEQUENCE public.registrations_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.registrations_id_seq OWNER TO dev;

--
-- Name: registrations_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dev
--

ALTER SEQUENCE public.registrations_id_seq OWNED BY public.registrations.id;


--
-- Name: students; Type: TABLE; Schema: public; Owner: dev
--

CREATE TABLE public.students (
    id integer NOT NULL,
    fname character varying(255),
    lname character varying(255),
    age integer
);


ALTER TABLE public.students OWNER TO dev;

--
-- Name: students_id_seq; Type: SEQUENCE; Schema: public; Owner: dev
--

CREATE SEQUENCE public.students_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.students_id_seq OWNER TO dev;

--
-- Name: students_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dev
--

ALTER SEQUENCE public.students_id_seq OWNED BY public.students.id;


--
-- Name: courses id; Type: DEFAULT; Schema: public; Owner: dev
--

ALTER TABLE ONLY public.courses ALTER COLUMN id SET DEFAULT nextval('public.courses_id_seq'::regclass);


--
-- Name: registrations id; Type: DEFAULT; Schema: public; Owner: dev
--

ALTER TABLE ONLY public.registrations ALTER COLUMN id SET DEFAULT nextval('public.registrations_id_seq'::regclass);


--
-- Name: students id; Type: DEFAULT; Schema: public; Owner: dev
--

ALTER TABLE ONLY public.students ALTER COLUMN id SET DEFAULT nextval('public.students_id_seq'::regclass);


--
-- Data for Name: courses; Type: TABLE DATA; Schema: public; Owner: dev
--

COPY public.courses (id, name, sigle, description) FROM stdin;
1	Concept	INF-1014	Un bon cours
2	Concept avance	INF1035	Un tres bon cours
3	Concept avance	INF1035	Un tres bon cours
4	Concept avance	INF1035	Un tres bon cours
5	Concept avance	INF1035	Un tres bon cours
6	Concept avance	INF1035	Un tres bon cours
7	Concept avance	INF1035	Un tres bon cours
8	Math info	PIF1006	Un cours
9	Math info	PIF1006	Un cours
\.


--
-- Name: courses_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dev
--

SELECT pg_catalog.setval('public.courses_id_seq', 9, true);


--
-- Data for Name: registrations; Type: TABLE DATA; Schema: public; Owner: dev
--

COPY public.registrations (id, student_id, course_id, date) FROM stdin;
1	1	1	\N
2	1	2	\N
3	1	3	\N
4	1	4	\N
5	2	5	\N
6	2	6	\N
7	3	7	\N
8	4	9	\N
9	5	8	\N
\.


--
-- Name: registrations_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dev
--

SELECT pg_catalog.setval('public.registrations_id_seq', 9, true);


--
-- Data for Name: students; Type: TABLE DATA; Schema: public; Owner: dev
--

COPY public.students (id, fname, lname, age) FROM stdin;
1	Steve	Gagnon	21
2	Steve	Jovi	21
3	Steve	Gagnon	21
4	David	Mongeau	22
5	Bob	Geaorges	50
6	Steve	Gagnon	21
7	Steve	Gagnon	21
8	Steve	Gagnon	21
9	Steve	Gagnon	21
10	Steve	Gagnon	21
11	Steve	Gagnon	21
12	Steve	Gagnon	21
13	Steve	Gagnon	21
14	Steve	Gagnon	21
15	Steve	Gagnon	21
16	Steve	Gagnon	21
17	Steve	Gagnon	21
18	Steve	Gagnon	21
19	Steve	Gagnon	21
20	Steve	Gagnon	21
\.


--
-- Name: students_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dev
--

SELECT pg_catalog.setval('public.students_id_seq', 20, true);


--
-- Name: courses courses_id_key; Type: CONSTRAINT; Schema: public; Owner: dev
--

ALTER TABLE ONLY public.courses
    ADD CONSTRAINT courses_id_key UNIQUE (id);


--
-- Name: registrations registrations_id_key; Type: CONSTRAINT; Schema: public; Owner: dev
--

ALTER TABLE ONLY public.registrations
    ADD CONSTRAINT registrations_id_key UNIQUE (id);


--
-- Name: students students_id_key; Type: CONSTRAINT; Schema: public; Owner: dev
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT students_id_key UNIQUE (id);


--
-- Name: registrations fk_course; Type: FK CONSTRAINT; Schema: public; Owner: dev
--

ALTER TABLE ONLY public.registrations
    ADD CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES public.courses(id);


--
-- Name: registrations fk_student; Type: FK CONSTRAINT; Schema: public; Owner: dev
--

ALTER TABLE ONLY public.registrations
    ADD CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES public.students(id);


--
-- PostgreSQL database dump complete
--

\connect postgres

SET default_transaction_read_only = off;

--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.20
-- Dumped by pg_dump version 9.6.20

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: DATABASE postgres; Type: COMMENT; Schema: -; Owner: dev
--

COMMENT ON DATABASE postgres IS 'default administrative connection database';


--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- PostgreSQL database dump complete
--

\connect template1

SET default_transaction_read_only = off;

--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.20
-- Dumped by pg_dump version 9.6.20

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: DATABASE template1; Type: COMMENT; Schema: -; Owner: dev
--

COMMENT ON DATABASE template1 IS 'default template for new databases';


--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- PostgreSQL database dump complete
--

--
-- PostgreSQL database cluster dump complete
--

