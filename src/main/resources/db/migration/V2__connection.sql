--
-- Name: connection; Type: TABLE; Schema: public
--

CREATE TABLE public.connection (
    id bigint NOT NULL,
    database_name character varying(255) NOT NULL,
    host_name character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    user_name character varying(255) NOT NULL
);

--
-- Name: connection_id_seq; Type: SEQUENCE; Schema: public
--

CREATE SEQUENCE public.connection_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- Name: connection_id_seq; Type: SEQUENCE OWNED BY; Schema: public
--

ALTER SEQUENCE public.connection_id_seq OWNED BY public.connection.id;

--
-- Name: connection id; Type: DEFAULT; Schema: public
--

ALTER TABLE ONLY public.connection ALTER COLUMN id SET DEFAULT nextval('public.connection_id_seq'::regclass);



--
-- Name: connection_id_seq; Type: SEQUENCE SET; Schema: public
--

SELECT pg_catalog.setval('public.connection_id_seq', 1, false);


--
-- Name: connection connection_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.connection
    ADD CONSTRAINT connection_pkey PRIMARY KEY (id);
